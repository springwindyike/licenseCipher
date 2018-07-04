package com.ejiahe.jeservice.jsonrpc.server.session;

import com.alibaba.fastjson.JSONObject;
import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCError;
import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 该类对JSONRPC2Session的write()的2个重载方法进行实现,<br/>
 * 将参数数据抽象为JSONRPC2Response, 并调用具体的JSONRPC2Session的write()进行输出
 * @author MaiLingFeng
 *
 */
public abstract class JSONRPC2AbstractSession implements JSONRPC2Session {

	private static Logger Log = LoggerFactory.getLogger(JSONRPC2AbstractSession.class);

	protected Map<String,Object> attrs = new HashMap<String,Object>();
	

	@Override
	public void write(JSONRPCError error, Object id) {
		JSONRPCResponse response = new JSONRPCResponse(error , id);

		write(response);
	}

	@Override
	public void write(JSONRPCResponse response) {
		write((Object)response);
	}

    @Override
    public void write(JSONObject params, Object id) {
        write(new JSONRPCResponse(params, id));
    }

    @Override
	public Object getAttr(String name) {
		return attrs.get(name);
	}

	@Override
	public void putAttr(String name, Object value) {
		if(name != null && value != null){
			attrs.put(name, value);
		}
	}

	@Override
	public Set<String> getAttrNames() {
		return attrs.keySet();
	}


}
