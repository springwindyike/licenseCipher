package com.ejiahe.jeservice.auth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ejiahe.jeservice.util.Base64Utils;
import com.ejiahe.jeservice.util.JEConstants;
import com.ejiahe.jeservice.util.concurrent.CacheFactory;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import io.jsonwebtoken.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Named;

@Named
public class AuthTokenService{
	
	private static Logger LOG = LoggerFactory.getLogger(AuthTokenService.class);

	private static final String EncodedKey = "dFwewmO5F4NbDG23pfUN3Q==";
	private static final String Algorithm = "HmacSHA256";

	private Cache<String,String> cache;
	
	public AuthTokenService(){
		cache = CacheFactory.createLocalCache("AuthToken", 7*JEConstants.DAY, 100000);
	}

	public String encodeJSONWebToken(Token token) throws AuthTokenException {
		if(token == null) {
			throw new AuthTokenException(AuthTokenException.CODE_ARGUMENT_ILLEGAL);
		}

		SecretKey key = getSecretKey();
		String compactJws = Jwts.builder()
				.setPayload(token.getProperties().toJSONString())
				.signWith(SignatureAlgorithm.HS256, key)
				.compact();

		return compactJws;
	}


	public Token decodeJSONWebToken(String encodingToken) throws AuthTokenException {
		if(Strings.isNullOrEmpty(encodingToken)) {
			throw new AuthTokenException(AuthTokenException.CODE_ARGUMENT_ILLEGAL);
		}

		try{
			Claims body = Jwts.parser().setSigningKey(getSecretKey())
					.parseClaimsJws(encodingToken).getBody();

			Token token = new BaseToken(new JSONObject(body));

			return token;
		}catch (SignatureException e){
			throw new AuthTokenException(AuthTokenException.CODE_AUTH_FAILD,e.getMessage());
		}catch (ExpiredJwtException e){
			throw new AuthTokenException(AuthTokenException.CODE_EXPIRE,e.getMessage());
		}catch (Exception e){
			throw new AuthTokenException(AuthTokenException.CODE_ARGUMENT_ILLEGAL,e.getMessage());
		}
	}

	
	public String encodeToken(Token token) throws AuthTokenException{
		if(token == null){
			throw new AuthTokenException(AuthTokenException.CODE_ARGUMENT_ILLEGAL);
		}
		
		String secretKey = token.getNonce();
		
		String encrpted = Base64Utils.encodeURLSafe(DigestUtils.shaHex(secretKey));
		
		LOG.debug("Decrypt Auth Code:" + secretKey);
		LOG.debug("Encrypt Auth Code:" + encrpted);
		
		cache.put(encrpted, doSerialize(token));
		
		return encrpted;
	}
	
	/**
	 * 解码Token
	 * @param encodingToken
	 * @return
	 * @throws AuthTokenException 若Token不存在或参数有误,则抛出该异常;
	 *
	 */
	public Token decodeToken(String encodingToken) throws AuthTokenException{
		if(Strings.isNullOrEmpty(encodingToken)){
			throw new AuthTokenException(AuthTokenException.CODE_ARGUMENT_ILLEGAL);
		}
		
		String tokenString = cache.getIfPresent(encodingToken);
		Token token = doUnSerialize(tokenString);
		
		if(token == null){
			throw new AuthTokenException(AuthTokenException.CODE_NOT_FOUND,encodingToken);
		}
		
		return token;
	}

	/**
	 * 检出Token, 检出后,Token将失效;
	 * @param encodingToken
	 * @return
	 * @throws AuthTokenException 若Token不存在或参数有误,则抛出该异常;
	 *
	 */
	public Token checkoutToken(String encodingToken) throws AuthTokenException{
		if(Strings.isNullOrEmpty(encodingToken)){
			throw new AuthTokenException(AuthTokenException.CODE_ARGUMENT_ILLEGAL);
		}
		
		String tokenString = cache.getIfPresent(encodingToken);

		cache.invalidate(encodingToken);

		Token token = doUnSerialize(tokenString);
		
		if(token == null){
			throw new AuthTokenException(AuthTokenException.CODE_NOT_FOUND,encodingToken);
		}
		
		return token;
	}

	
	private Token doUnSerialize(String serializeString){
		if(Strings.isNullOrEmpty(serializeString)){
			return null;
		}
		
		try{
			JSONObject o = JSON.parseObject(serializeString);
			return new BaseToken(o);
		}catch(Exception e){
			LOG.warn(e.getMessage());
			return null;
		}
	}
	
	
	private String doSerialize(Token token){
		return token.getProperties().toJSONString();
	}

	private SecretKey getSecretKey(){
		byte[] raw = Base64Utils.decodeToBytes(EncodedKey);
		SecretKeySpec skeySpec = new SecretKeySpec(raw, Algorithm);
		return skeySpec;
	}


}
