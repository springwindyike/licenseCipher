package com.ejiahe.jeservice.jsonrpc.server;

import com.ejiahe.jeservice.jsonrpc.server.annotation.NotRequirePermission;
import com.ejiahe.jeservice.jsonrpc.server.annotation.RequirePermission;

/**
 * 该接口定义了jsonrpc服务所需要实现的方法
 * <br/>
 * JSON-RPC服务应该能自动响应JSONRPC2Request, 并返回JSONRPC2Response或JSONRPC2Error
 * <p/>
 * <ul>JSONRPC服务的特点:
 * <li>每个JSONRPC服务都实现了{@link JSONRPC2Service}接口;
 * <li>每个JSONRPCService都应该有一个作用域(scope),其完整的JSONRPC Method = scope + "." + classMethod;
 * <li>JSONRPC接口实现格式为public void xxxMethod(JSONRPC2Request req , JSONRPC2Session session);
 * <li>通过{@link NotRequirePermission}和{@link RequirePermission}来控制访问授权, 未携带注解的则认为接口需要最高权限访问;
 * <p/>
 * <h4>JSONRPC服务示例:</h4>
 * <p/>
 * <blockquote><pre>
 * public class JSONRPC2TestService implements JSONRPC2Service{
 * <p/>
 *     //@NotRequirePermission
 *     public void testRequest(JSONRPCRequest req , JSONRPCSession session){
 *          JSONObject params = req.getParams();
 *          String key = params.optString("key","hello!");
 * <p/>
 *          //send response to client
 *          session.write(key, req.getID());
 *     }
 * <p/>
 *     public String getScope(){
 *          return "test";
 *     }
 * <p/>
 *     public void start(){
 *          //if want to load something before start service , code here;
 *     }
 * <p/>
 *     public void stop(){
 *         //if want to release something when stop service, code here;
 *     }
 * }
 * </pre></blockquote>
 * </P>
 *
 * @author MaiLingFeng
 */
public interface JSONRPC2Service {


    /**
     * 获取服务的作用域
     * <br/>
     * 例如该JSONRPC2Service的作用域为organization,
     * 那么所有Method为organization.xxx的JSONRPC Request都会分派到该JSONRPC2Service处理
     *
     * @return
     */
    String getScope();

    /**
     * 启动, 常用于装载数据资源、触发周期性任务等;
     * 该接口在JSONRPC2Service装配时被触发;
     */
    void start();

    /**
     * 停止, 常用于资源的销毁、数据的持久化保存、周期任务的关闭等;
     * 该接口在JSONRPC2Service卸载时被触发;
     */
    void stop();

}
