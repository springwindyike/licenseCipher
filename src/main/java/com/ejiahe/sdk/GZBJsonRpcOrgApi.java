package com.ejiahe.sdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ejiahe.Application;
import com.ejiahe.jeservice.jsonrpc.client.JSONRPCHttpSender;
import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCResponse;
import com.ejiahe.sdk.jsonrpcbean.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * GZB jsonrpc组织架构接口
 * Created by liaofei on 2018/1/8.
 */
@Named
public class GZBJsonRpcOrgApi {
    private static Logger LOG = LoggerFactory.getLogger(GZBJsonRpcOrgApi.class);

    @Inject
    private JSONRPCHttpSender jsonrpcHttpSender;

    /**
     * userIds转换sipAccounts
     * @param userIds
     * @return
     */
    public List<String> userIdsToSipAccounts(List<String> userIds){
        List<String> sipAccounts = new ArrayList<String>();
        ItemsInfo<UserInfo> tempUserItems = getUsers(userIds);
        for(UserInfo user : tempUserItems.getItems()){
            sipAccounts.add(user.getSipAccount());
        }
        return sipAccounts;
    }

    /**
     * 根据sipAccountIds转换userIds,map<sipAccount, userId>
     * @param sipAccountIds
     * @return
     */
    public Map<String, String> sipAccountsToUsersMap(List<String> sipAccountIds) {
        Map<String, String> sipAccountUserIdMap = new HashMap<String, String>();
        ItemsInfo<UserInfo> sipAccountUsers = searchUsers(sipAccountIds);
        if(sipAccountUsers != null && sipAccountUsers.getItems() != null) {
            for(UserInfo info : sipAccountUsers.getItems()){
                sipAccountUserIdMap.put(info.getSipAccount(), info.getUserId());
            }
        }
        return sipAccountUserIdMap;
    }

    /**
     * sipAccounts转换userIds
     * @param sipAccountIds
     * @return
     */
    public List<String> sipAccountsToUserIds(List<String> sipAccountIds) {
        List<String> userIds = new ArrayList<String>();
        ItemsInfo<UserInfo> sipAccountUsers = searchUsers(sipAccountIds);
        if(sipAccountUsers != null && sipAccountUsers.getItems() != null) {
            for(UserInfo info : sipAccountUsers.getItems()){
                userIds.add(info.getUserId());
            }
        }
        return userIds;
    }

    /**
     * tenementIds转换sipCodes
     * @param tenementIds
     * @return
     */
    public List<String> tenementIdsToSipCodes(Collection<String> tenementIds){
        if(tenementIds.isEmpty()){
            return Collections.emptyList();
        }

        List<String> sipCodes = new ArrayList<String>();
        if(tenementIds.size() == 1){//单条转换
            TenementInfo tenementInfo = getTenementInfo(tenementIds.iterator().next());
            if(tenementInfo != null){
                sipCodes.add(tenementInfo.getSipCode());
            }
        }else{//多条转换
            ItemsInfo<TenementInfo> itemsInfo = getTenements(tenementIds, null, null);
            if(itemsInfo != null) {
                for (TenementInfo info : itemsInfo.getItems()) {
                    sipCodes.add(info.getSipCode());
                }
            }
        }

        return sipCodes;
    }
    /**
     * 根据sipCodes转换tenementIds,map<sipCode, tenementId>
     * @param sipCodes
     * @return
     */
    public Map<String, String> sipCodesToTenementsMap(List<String> sipCodes) {
        Map<String, String> sipCodeTenementIdMap = new HashMap<String, String>();
        List<TenementSimpleInfo> tenementSimpleInfos = searchTenementsBySipCodes(sipCodes);
        if(tenementSimpleInfos != null && !tenementSimpleInfos.isEmpty()){
            for(TenementSimpleInfo info : tenementSimpleInfos){
                sipCodeTenementIdMap.put(info.getSipCode(), info.getTenementId());
            }
        }
        return sipCodeTenementIdMap;
    }

    /**
     * 获取企业信息
     * @param tenementId
     * @return
     */
    public TenementInfo getTenementInfo(String tenementId){
        JSONObject params = new JSONObject();
        params.put("tenementId", tenementId);
        params.put("withDepartments", false);
        JSONRPCResponse resp = jsonrpcHttpSender.send("org.tenement.getTenement", params);
        if(resp != null && resp.isSuccess()){
            return resp.getResultAsObject(TenementInfo.class);
        }else{
            return null;
        }
    }


    /**
     * 获取企业列表
     * @param tenementIds
     * @param startIndex
     * @param resultRows
     * @return
     */
    public ItemsInfo<TenementInfo> getTenements(Collection<String> tenementIds, Integer startIndex, Integer resultRows){
        JSONObject params = new JSONObject();
        params.put("tenementIds", tenementIds);
        params.put("startIndex", startIndex);
        params.put("resultRows", resultRows);
        JSONRPCResponse resp = jsonrpcHttpSender.send("org.tenement.getTenements", params);
        if(resp != null && resp.isSuccess()){
            if(resp.getResult() == null){
                return null;
            }else{
                try{
                    return JSON.parseObject(resp.getResult().toJSONString(), new TypeReference<ItemsInfo<TenementInfo>>(){});
                }catch (Exception e){
                    LOG.warn(e.getMessage());
                    return null;
                }
            }
        }else{
            return null;
        }
    }

    /**
     * 搜索企业列表
     * @param searchKey
     * @param startIndex
     * @param resultRows
     * @return
     */
    public ItemsInfoOfTenement<TenementInfo> searchTenements(String searchKey, Integer startIndex, Integer resultRows){
        JSONObject params = new JSONObject();
        params.put("searchKey", searchKey);
        params.put("startIndex", startIndex);
        params.put("resultRows", resultRows);
        params.put("asSipCode", true);
        JSONRPCResponse resp = jsonrpcHttpSender.send("org.tenement.search", params);
        if(resp != null && resp.isSuccess()){
            if(resp.getResult() == null){
                return null;
            }else{
                try{
                    return JSON.parseObject(resp.getResult().toJSONString(), new TypeReference<ItemsInfoOfTenement<TenementInfo>>() {
                    });
                }catch (Exception e){
                    LOG.warn(e.getMessage());
                    return null;
                }
            }
        }else{
            return null;
        }
    }
    /**
     * 根据sipcode批量转换tenementId
     * @param sipCodes
     * @return
     */
    public List<TenementSimpleInfo> searchTenementsBySipCodes(List<String> sipCodes){
        JSONObject params = new JSONObject();
        params.put("sipCodes", sipCodes);
        JSONRPCResponse resp = jsonrpcHttpSender.send("org.tenement.searchBySipCodes", params);
        if(resp != null && resp.isSuccess()){
            if(resp.getResult() == null){
                return null;
            }else{
                try{
                    ItemsInfo<TenementSimpleInfo> itemsInfo = JSON.parseObject(resp.getResult().toJSONString(),
                            new TypeReference<ItemsInfo<TenementSimpleInfo>>(){});
                    return itemsInfo.getItems();
                }catch (Exception e){
                    LOG.warn(e.getMessage());
                    return null;
                }
            }
        }else{
            return null;
        }
    }

    public String getTenementIdBySipCode(String sipCode){
        String tenementId = "";
        List<TenementSimpleInfo> tenementSimpleInfos = searchTenementsBySipCodes(Arrays.asList(sipCode));
        if(tenementSimpleInfos != null && !tenementSimpleInfos.isEmpty()){
            tenementId = tenementSimpleInfos.iterator().next().getTenementId();
        }
        return tenementId;
    }

    /**
     * 搜索企业,返回id集合
     * @param tenementKey
     * @return
     */
    public List<String> searchTenements(String tenementKey){
        List<String> tenementIds = new ArrayList<String>();
        ItemsInfoOfTenement<TenementInfo> searchTenements = searchTenements(tenementKey, 0, 15);
        if(searchTenements != null && !searchTenements.getTenements().isEmpty()){
            for(TenementInfo tenementInfo : searchTenements.getTenements()){
                tenementIds.add(tenementInfo.getTenementId());
            }
        }
        return tenementIds;
    }

    /**
     * 获取企业下的部门列表
     * @param tenementId
     * @return
     */
    public ItemsInfoOfDepartment<DepartmentInfo> getDepartmentsByTenementId(String tenementId){
        JSONObject params = new JSONObject();
        params.put("tenementId", tenementId);
        JSONRPCResponse resp = jsonrpcHttpSender.send("org.department.getDepartments", params);
        if(resp != null && resp.isSuccess()){
            if(resp.getResult() == null){
                return null;
            }else{
                try{
                    return JSON.parseObject(resp.getResult().toJSONString(),
                            new TypeReference<ItemsInfoOfDepartment<DepartmentInfo>>(){});
                }catch (Exception e){
                    LOG.warn(e.getMessage());
                    return null;
                }
            }
        }else{
            return null;
        }
    }
    /**
     * 批量获取部门列表，可包含部门下的用户列表
     * @param departmentIds
     * @param fetchUsers
     * @return
     */
    public ItemsInfo<DepartmentAndUsersInfo> getDepartmentsBatch(List<String> departmentIds, boolean fetchUsers){
        JSONObject params = new JSONObject();
        params.put("departmentIds", departmentIds);
        params.put("fetchUsers", fetchUsers);
        JSONRPCResponse resp = jsonrpcHttpSender.send("org.department.getDepartmentBatch", params);
        if(resp != null && resp.isSuccess()){
            if(resp.getResult() == null){
                return null;
            }else{
                try{
                    return JSON.parseObject(resp.getResult().toJSONString(),
                            new TypeReference<ItemsInfo<DepartmentAndUsersInfo>>(){});
                }catch (Exception e){
                    LOG.warn(e.getMessage());
                    return null;
                }
            }
        }else{
            return null;
        }
    }

    /**
     * 获取单个部门信息
     * @param departmentId
     * @return
     */
    public DepartmentAndUsersInfo getDepartment(String departmentId, boolean fetchUsers){
        JSONObject params = new JSONObject();
        params.put("departmentId", departmentId);
        params.put("fetchUsers", fetchUsers);
        JSONRPCResponse resp = jsonrpcHttpSender.send("org.department.getDepartment", params);
        if(resp != null && resp.isSuccess()){
            if(resp.getResult() == null){
                return null;
            }else{
                try{
                    return JSON.parseObject(resp.getResult().toJSONString(), DepartmentAndUsersInfo.class);
                }catch (Exception e){
                    LOG.warn(e.getMessage());
                    return null;
                }
            }
        }else{
            return null;
        }
    }

    /**
     * 获取用户列表
     * @param userIds
     * @param tenementId
     * @param startIndex
     * @param resultRows
     * @return
     */
    public ItemsInfo<UserInfo> getUsers(Collection<String> userIds, String tenementId,
                                        Integer startIndex, Integer resultRows){
        String returnType = "simple";//detail
        JSONObject params = new JSONObject();
        params.put("userIds", userIds);
        params.put("tenementId", tenementId);
        params.put("startIndex", startIndex);
        params.put("resultRows", resultRows);
        params.put("returnType", returnType);
        JSONRPCResponse resp = jsonrpcHttpSender.send("org.user.getUsers", params);
        if(resp != null && resp.isSuccess()){
            if(resp.getResult() == null){
                return null;
            }else{
                try{
                    return JSON.parseObject(resp.getResult().toJSONString(), new TypeReference<ItemsInfo<UserInfo>>(){});
                }catch (Exception e){
                    LOG.warn(e.getMessage());
                    return null;
                }
            }
        }else{
            return null;
        }
    }

    public ItemsInfo<UserInfo> getUsers(Collection<String> userIds){
        return getUsers(userIds, null, null, null);
    }

    public Map<String, String> getUsersWithFullDepartment(Collection<String> userIds, String tenementId){
        Map<String, String> userMap = new HashMap<String, String>();
        JSONObject params = new JSONObject();
        params.put("userIds", userIds);
        params.put("tenementId", tenementId);
        JSONRPCResponse resp = jsonrpcHttpSender.send("org.user.getUsersWithDepartments", params);
        if(resp != null && resp.isSuccess()){
            if(resp.getResult() == null){
                return userMap;
            }else{
                try{
                    ItemsInfo<UserFullDepartment> itemsInfo =
                            JSON.parseObject(resp.getResult().toJSONString(), new TypeReference<ItemsInfo<UserFullDepartment>>(){});
                    for(UserFullDepartment user : itemsInfo.getItems()){
                        userMap.put(user.getUserId(), user.getFullDepartmentName());
                    }
                    return userMap;
                }catch (Exception e){
                    LOG.warn(e.getMessage());
                    return userMap;
                }
            }
        }else{
            return userMap;
        }
    }

    public ItemsInfo<UserInfo> searchUsers(String key, String tenementId,
                                           Integer startIndex, Integer resultRows){
        return searchUsers(key, tenementId, null, startIndex, resultRows);
    }
    /**
     * 搜索用户列表
     * @param key
     * @param tenementId
     * @param startIndex
     * @param resultRows
     * @return
     */
    private ItemsInfo<UserInfo> searchUsers(String key, String tenementId, String departmentName,
                                        Integer startIndex, Integer resultRows){
        String returnType = "simple";//detail
        JSONObject params = new JSONObject();
        params.put("key", key);
        params.put("tenementId", tenementId);
        params.put("departmentName", departmentName);
        params.put("asUserName", true);
        params.put("asMobile", true);
        params.put("startIndex", startIndex);
        params.put("resultRows", resultRows);
        params.put("returnType", returnType);
        JSONRPCResponse resp = jsonrpcHttpSender.send("org.user.search", params);
        if(resp != null && resp.isSuccess()){
            if(resp.getResult() == null){
                return null;
            }else{
                try{
                    return JSON.parseObject(resp.getResult().toJSONString(), new TypeReference<ItemsInfo<UserInfo>>(){});
                }catch (Exception e){
                    LOG.warn(e.getMessage());
                    return null;
                }
            }
        }else{
            return null;
        }
    }

    /**
     * 批量关键字（手机号码集）搜索
     * @param keys
     * @return
     */
    public ItemsInfo<UserInfo> searchUsers(Collection<String> keys){
        JSONObject params = new JSONObject();
        params.put("keys", keys);
        params.put("returnType", "simple");
        JSONRPCResponse resp = jsonrpcHttpSender.send("org.user.getUsers", params);
        if(resp != null && resp.isSuccess()){
            if(resp.getResult() == null){
                return null;
            }else{
                try{
                    return JSON.parseObject(resp.getResult().toJSONString(), new TypeReference<ItemsInfo<UserInfo>>(){});
                }catch (Exception e){
                    LOG.warn(e.getMessage());
                    return null;
                }
            }
        }else{
            return null;
        }
    }

    /**
     * 根据客户端code获取鉴权用户信息
     * @param code
     * @return
     */
    public UserAuthInfo getUserByCode(String code){
        JSONObject params = new JSONObject();
        params.put("code", code);
        JSONRPCResponse resp = jsonrpcHttpSender.send("oauth2.getUserByCode", params);
        if(resp != null && resp.isSuccess()){
            if(resp.getResult() == null){
                return null;
            }else{
                try{
                    return JSON.parseObject(resp.getResult().toJSONString(), UserAuthInfo.class);
                }catch (Exception e){
                    LOG.warn(e.getMessage());
                    return null;
                }
            }
        }else{
            return null;
        }
    }

    /**
     * 获取部门树
     * @param tenementId
     * @return
     */
    public DepartmentTreeInfo getDepartmentTreeInfo(String tenementId){
        DepartmentTreeInfo departmentTreeInfo = new DepartmentTreeInfo();
        DepartmentTreeInfo departmentTreeInfoWithTenement = new DepartmentTreeInfo();
        JSONObject params = new JSONObject();
        params.put("tenementId", tenementId);
        JSONRPCResponse resp = jsonrpcHttpSender.send("org.tenement.getDepartmentTree", params);
        if(resp != null && resp.isSuccess()){
            if(resp.getResult() == null){
                return null;
            }else{
                try{
                    ItemsInfo<DepartmentTreeInfo> itemsInfo = JSON.parseObject(resp.getResult().toJSONString(),
                            new TypeReference<ItemsInfo<DepartmentTreeInfo>>(){});
                    departmentTreeInfoWithTenement = itemsInfo.getItems().iterator().next();
                    departmentTreeInfo = departmentTreeInfoWithTenement.getChildren().iterator().next();
                    departmentTreeInfo.setName(departmentTreeInfoWithTenement.getName());
                    departmentTreeInfo.setUserCount(departmentTreeInfoWithTenement.getUserCount());
                    return departmentTreeInfo;
                }catch (Exception e){
                    LOG.warn(e.getMessage());
                    return null;
                }
            }
        }else{
            return null;
        }
    }

    public List<String> searchUsers(String key, String tenementId, String departmentName){
        List<String> userIds = new ArrayList<String>();
        ItemsInfo<UserInfo> searchUsers = searchUsers(key, tenementId, departmentName, 0, 100000);
        if(searchUsers != null && !searchUsers.getItems().isEmpty()){
            for(UserInfo userInfo : searchUsers.getItems()){
                userIds.add(userInfo.getUserId());
            }
        }
        return userIds;
    }

    public static void main(String[] args) {
        String fileResourcePath = "classpath:static/images/icon_bill.png";
        ResourceLoader resourceLoader = Application.getContext();
        Resource resource = resourceLoader.getResource(fileResourcePath);

        if (resource == null) {
            return;
        }
        InputStream input = null;
        try {
            input = resource.getInputStream();
            LOG.info("input:{}", input.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(input != null)
                    input.close();
            }catch (Exception e) {
                // Ignore.
            }
        }

    }

//    /**
//     * 获取eim http接口地址，并持久化到本地
//     */
//    public void getEimServiceURL(){
//        JSONObject params = new JSONObject();
//        JSONRPCResponse resp = jsonrpcHttpSender.send("open.config.getServiceURL", params);
//        if(resp != null && resp.isSuccess()){
//            if(resp.getResult() != null){
//                JSONObject object = resp.getResult();
//                LOG.info("获取eim http接口地址, {}", object.toString());
//                GZBApiConfig.getInstance().setEimBaseUrl(object.getString("baseURL"));
//                GZBApiConfig.getInstance().setEimFileBaseUrl(object.getString("fileBaseURL"));
//                GZBApiConfig.getInstance().setEimAuthBaseUrl(object.getString("oauthBaseURL"));
//            }
//        }else{
//            LOG.warn("获取eim http接口地址失败");
//        }
//    }
//    /**
//     * 获取gzbsms 外网地址，并持久化到本地
//     */
//    public String getGzbSmsURL(){
//        String gzbSmsUrl = "";
//        JSONObject params = new JSONObject();
//        params.put("propNames", Arrays.asList(GZBApiConfig.PROP_GZBSMS_URL));
//        JSONRPCResponse resp = jsonrpcHttpSender.send("config.getCustomProp", params);
//        if(resp != null && resp.isSuccess()){
//            if(resp.getResult() != null){
//                JSONObject object = resp.getResult();
//                LOG.info("获取gzbsms 外网地址, {}", object.toString());
//                try {
//                    JSONArray array = object.getJSONArray("customProperties");
//                    gzbSmsUrl = array.getJSONObject(0).getString("value");
//                    GZBApiConfig.getInstance().setGzbSmsUrl(gzbSmsUrl);
//                } catch (Exception e) {
//                    LOG.error("获取eim http接口地址失败,e:{}", e.getMessage());
//                }
//            }
//        }else{
//            LOG.warn("获取eim http接口地址失败");
//        }
//        return gzbSmsUrl;
//    }

}
