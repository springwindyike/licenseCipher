package com.ejiahe.app;

import java.util.Arrays;
import java.util.List;

/**
 * Created by focus on 2018/4/26.
 */
public class AppConstant {
    public static final String APP_LICENSE = "gzb_license";
    public static final String ATTRI_TOKEN_INFO = "tokenInfo";

    public static final List<String> clientTypeList = Arrays.asList(new String[]{"qxAndroid", "qxIOS", "qxWindows", "qxMAC"});
    /**
     * 客户端登录
     * @param clientType
     * @return
     */
    public static boolean isClientLogin(String clientType){
        return clientTypeList.contains(clientType);
    }
}
