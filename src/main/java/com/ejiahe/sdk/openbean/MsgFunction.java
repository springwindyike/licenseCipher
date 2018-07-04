package com.ejiahe.sdk.openbean;

/**
 * function推送消息类型
 * Created by liaofei on 2017/3/16.
 */
public class MsgFunction extends SsoLoginArticle implements Msg{
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public MsgFunction(String name, String title, String description, String url, String picUrl,
                       String appId, String corpId){
        super(title, url, picUrl, description, appId, corpId);
        this.name = name;
    }

    @Override
    public String getMsg_type() {
        return Msg.TYPE_FUNCTION;
    }
}
