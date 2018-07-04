package com.ejiahe.sdk;

import com.ejiahe.jeservice.util.CodeResult;
import com.ejiahe.jeservice.util.StringUtils;
import com.ejiahe.jeservice.util.common.AppUtil;
import com.ejiahe.jeservice.util.common.GZBApiException;
import com.ejiahe.sdk.api.*;
import com.ejiahe.sdk.api.request.RequestEventSend;
import com.ejiahe.sdk.api.request.RequestMsgSend;
import com.ejiahe.sdk.auth.GZBTokenService;
import com.ejiahe.sdk.config.GZBApiConfig;
import com.ejiahe.sdk.openbean.*;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * GZB开放接口
 */
public class GZBOpenApi {

	private static final GZBOpenApi apiServ = new GZBOpenApi();
	
	private GZBTokenService tokenServ = GZBTokenService.getInstance();
	
	private GZBOpenApi(){
		
	}
	
	
	public static GZBOpenApi getInstance(){
		return apiServ;
	}
	
	/**
	 * 获取指定公司的基本信息
	 * @param corp_id 公司id,不可为空;
	 * @return
	 * @throws GZBApiException
	 */
	public CorpInfo getCorpInfo(String corp_id) throws GZBApiException {
		return CorpApi.getCorpInfo(getAccessToken(corp_id));
	}
	/**
	 * 获取接入应用agentId等信息
	 * @param corpId
	 * @return
	 * @throws GZBApiException
	 */
	public AppAgentInfo getAppAgentInfo(String corpId) throws GZBApiException {
		return CorpApi.getAppAgentInfo(getAccessToken(corpId), GZBApiConfig.getInstance().getLicenseAppId(), corpId);
	}

	public void getFile(String appName,String corp_id,String file_id,String thumbnail,OutputStream out) throws GZBApiException {
		if (StringUtils.isNotBlank(thumbnail)) {
			FileApi.get(getAccessToken(corp_id), file_id, thumbnail, out);
		}else{
			FileApi.get(getAccessToken(corp_id), file_id, out);
		}
	}

	public void getFile(String appName,String corp_id,String file_id,String thumbnail,HttpServletResponse resp) throws GZBApiException {
		FileApi.get(getAccessToken(corp_id), file_id, thumbnail, resp);
	}
	/**
	 * 获取用户临时授权信息;
	 * @param code 授权code
	 * @param corp_id 公司id,备注:该值可为null
	 * @return
	 * @throws GZBApiException
	 */
	public UserLoginInfo getUserInfoByCode(String code, String corp_id) throws GZBApiException {
		
		return MemberApi.getByCode(getAccessToken(corp_id), code);
	}

	public ChatRoomInfo getChatRoom(String chatRoomId, String corp_id) throws GZBApiException {
		return ChatRoomApi.getChatRoom(getAccessToken(corp_id), chatRoomId);
	}
	
	/**
	 * 通过用户id 获取用户的详细信息
	 * @param corp_id
	 * @param userId
	 * @return
	 * @throws GZBApiException
	 */
	public UserInfo getByUserId(String corp_id, String userId) throws GZBApiException {
		return MemberApi.getByUserId(getAccessToken(corp_id), userId);
	}

	/**
	 * 通过用户id 获取用户的基本信息
	 */
	public UserBaseInfo getBaseByUserId(String corp_id,String userId) throws GZBApiException {
		return UserApi.getById(getAccessToken(corp_id), userId);
	}

	public boolean markFile(String corp_id,String fileId) throws GZBApiException {
		return FileApi.markFile(getAccessToken(corp_id), fileId);
	}

	/**
	 * 通过关键字，搜索用户信息
	 * @param corp_id
	 * @param key 搜索关键字，只包括用户名和手机号
	 * @param offset
	 * @param limit
	 * @param isDetail
	 * @return
	 * @throws GZBApiException
	 */
	public List<UserInfo> searchUsers(String corp_id, String key, int offset, int limit, boolean isDetail) throws GZBApiException {
		return MemberApi.searchUsers(getAccessToken(corp_id), corp_id, key, offset, limit, isDetail);
	}
	
	/**
	 * 批量搜索用户信息
	 * 通过拼音进行排序查询
	 * @param corp_id
	 * @param userIds
	 * @return
	 * @throws GZBApiException
	 */
	public List<UserInfo> getBatch(String corp_id, List<String> userIds) throws GZBApiException {
		return MemberApi.getBatch(getAccessToken(corp_id), userIds, false, true);
	}

	/**
	 * 批量获取用户基本信息
	 * @param corp_id
	 * @param userIds
	 * @return
	 * @throws GZBApiException
	 */
	public UserBaseInfoBatch getBaseBatch(String corp_id, Collection<String> userIds) throws GZBApiException {
		return getBaseBatch(corp_id, userIds, null);
	}
	/**
	 * 批量获取用户基本信息（排序）
	 * @param corp_id
	 * @param userIds
	 * @param order_by
	 * @return
	 * @throws GZBApiException
	 */
	public UserBaseInfoBatch getBaseBatch(String corp_id, Collection<String> userIds, String order_by) throws GZBApiException {
		return UserApi.getByBatch(getAccessToken(corp_id), userIds, order_by);
	}

	/**
	 * 批量搜索用户基本信息
	 * @param corp_id
	 * @param name 用户名
	 * @return
	 * @throws GZBApiException
	 */
	public UserBaseInfoSearch searchBaseBatch(String corp_id, String name, Integer start_index, Integer max_items) throws GZBApiException {
		UserSearchCommand command = new UserSearchCommand();
		command.setKey(name);
        command.setAs_name(true);
        command.setAs_mobile(true);
        command.setAs_custom_id(true);
        command.setStart_index(start_index);
        command.setMax_items(max_items);
		return UserApi.searchUsers(getAccessToken(corp_id), command);
	}

	/**
	 * 批量搜索用户基本信息,剔除管理员账号
	 * @param corp_id
	 * @param name 用户名
	 * @return
	 * @throws GZBApiException
	 */
	public UserBaseInfoSearch searchBaseBatchWithCorpId(String corp_id, String name, Integer start_index, Integer max_items) throws GZBApiException {
		UserSearchCommand command = new UserSearchCommand();
		command.setKey(name);
		command.setAs_name(true);
		command.setAs_mobile(true);
		command.setAs_custom_id(true);
		command.setStart_index(start_index);
		command.setMax_items(max_items);
		if(corp_id != null){
			command.setCorps(Arrays.asList(corp_id));
		}
		return UserApi.searchUsers(getAccessToken(corp_id), command);
	}

	/**
	 * 获取部门信息和部门成员
	 * @param corp_id
	 * @param department_id
	 * @return
	 * @throws GZBApiException
	 */
	public DepartmentResult getDepartment(String corp_id, String department_id) throws GZBApiException {
		return DepartmentApi.getDepartmentMembers(getAccessToken(corp_id), department_id);
	}
	
	/**
	 * 推送消息
	 * @param corp_id
	 * @param req
	 * @return
	 * @throws GZBApiException
	 */
	public MsgSendResult sendMessage(String corp_id, RequestMsgSend req) throws GZBApiException {
		return MessageApi.send(getAccessToken(corp_id), req);
	}


	public CodeResult updateChatroom(String corp_id,ChatRoomUpdateVO chatRoomUpdateVO) throws GZBApiException {
		return ChatRoomApi.updateChatRoom(getAccessToken(corp_id), chatRoomUpdateVO);
	}

	public ChatRoomInfo createChatroom(String corp_id,ChatRoomCreateVO chatRoomCreateVO) throws GZBApiException {
		return ChatRoomApi.createChatRoom(getAccessToken(corp_id),chatRoomCreateVO);
	}

	/**
	 * 推送红点提醒
	 * @param corp_id
	 * @param req
	 * @return
	 * @throws GZBApiException
	 */
	public CodeResult sendEvent(String corp_id, RequestEventSend req) throws GZBApiException {
		return EventApi.send(getAccessToken(corp_id), req);
	}
	
	public FileUploadResult uploadImg(String corp_id, InputStream input) throws GZBApiException {
		return FileApi.upload(getAccessToken(corp_id), "image", input, null);
	}
	public FileUploadResult uploadHeadImg(String corp_id, InputStream input) throws GZBApiException {
		return FileApi.upload(getAccessToken(corp_id), "headImg", input, null);
	}

	public FileUploadResult upload(String corp_id, String type, InputStream input, String filename) throws GZBApiException {
		return FileApi.upload(getAccessToken(corp_id), type, input, filename);
	}


	public byte[] download(String corp_id, String file_id) throws GZBApiException {
		return FileApi.get(getAccessToken(corp_id), file_id);
	}
	
	/**
	 * 获取AccessToken,以便调用企业接口;
	 * @param corp_id 根据该值,来决定是尝试调用一般应用代理Token,还是套件应用代理token
	 * @return
	 * @throws GZBApiException
	 */
	private String getAccessToken(String corp_id) throws GZBApiException {
		AccessToken token = null;

		token = tokenServ.getAccessToken(GZBApiConfig.getInstance().getLicenseAppId(), GZBApiConfig.getInstance().getLicenseAppSecret(), corp_id);

		return token.getAccess_token();
	}

	/**
	 * 发送邮件
	 * @author focus
	 * @date 2016年8月22日 上午9:32:22
	 * @param param
	 * @throws GZBApiException
	 */
	public void sendEmail(EmailParam param) throws GZBApiException {
		String tenementId = param.getTenement_id();
		String appName = param.getAppName();

		EmailApi.sendEmail(getAccessToken(tenementId), param);
	}

	/**
	 * 通过userId 获取部门id
	 * @author focus
	 * @date 2016年2月25日
	 * @time 下午4:43:14
	 */
	public List<String> getDepartmentIds(String tenementId, String userId){
		List<String> deptIds = null;
		try {
			UserInfo user = getByUserId(tenementId, userId);
			if(user != null){
				deptIds =  user.getDepartments();
			}
			return deptIds;
		} catch (GZBApiException e) {
		}
		return new ArrayList<String>();
	}

	/**
	 * 通过部门id列表获取部门名称
	 * @author focus
	 * @date 2016年2月25日
	 * @time 下午4:45:16
	 */
	public List<String> getDepartmentNames(List<String> deptIds, String tenementId){

		List<String> deptNames = new ArrayList<String>();
		deptIds = new ArrayList<String>(new LinkedHashSet<String>(deptIds));

		for(String deptId : deptIds){
			DepartmentResult dept = null;
			try {
				dept = getDepartment(tenementId, deptId);
			} catch (GZBApiException e) {
			}
			if(dept == null) continue;
			String deptName = dept.getName();
			if(AppUtil.isEmpty(deptName)){
				deptName = "默认部门";
			}
			deptNames.add(deptName);
		}
		return deptNames;
	}

	/**
	 * 通过部门id 查询部门下的人员列表 返回userIds
	 * @author focus
	 * @date 2016年2月25日
	 * @time 下午5:48:47
	 */
	public List<String> getUserIdsByDeptIds(List<String> deptIds,String tenementId){

		LinkedHashSet<String> userIds = new LinkedHashSet<String>();
		for(String deptId : deptIds){
			DepartmentResult dept = null;
			try {
				dept = getDepartment(tenementId, deptId);
			} catch (GZBApiException e) {
			}
			if(dept != null){
				List<UserInfo> userInfos = dept.getUsers();
				if(userInfos != null){
					for(UserInfo userInfo : userInfos){
						userIds.add(userInfo.getUser_id());
					}
				}
			}
		}

		return new ArrayList<String>(userIds);
	}

	public List<String> getBatchUserIdWithOrderByPinying(List<String> userIds, String tenementId){
		try {

			if(AppUtil.isEmpty(userIds)) return new ArrayList<String>();
			userIds = new ArrayList<String>(new HashSet<String>(userIds));
//			List<UserInfo> userInfos = getBatch(appName, tenementId, userIds);
			UserBaseInfoBatch userBaseInfoBatch = GZBOpenApi.getInstance().getBaseBatch(tenementId,
					userIds, "short_py");
			List<UserBaseInfo> userBaseInfos = userBaseInfoBatch.getItems();
			if (AppUtil.isEmpty(userBaseInfos)) return userIds;

			userIds.clear();
			for(UserBaseInfo userInfo : userBaseInfos){
				userIds.add(userInfo.getUser_id());
			}
		} catch (Exception e) {
		}
		return userIds;
	}
	/**
	 * 用户是否在企业中
	 * @author focus
	 * @date 2016年4月5日
	 * @time 下午5:17:03
	 */
	public boolean isInTenement(String tenementId,String userId){
		try {
			UserBaseInfo userInfo = getBaseByUserId(tenementId, userId);
			if(userInfo != null) return true;
		} catch (Exception e) {
			return false;
		}
		return false;
	}
}
