package com.ejiahe.sdk.openbean;

import java.util.List;

/**
 * Created by mjf on 2017/4/14.
 */
public class UserSearchCommand {

    public static final String RETURN_TYPE_SIMPLE = "simple";
    public static final String RETURN_TYPE_DETAIL = "detail";

    private String key;
    private String return_type = RETURN_TYPE_SIMPLE;

    private Boolean as_user_id = true;
    private Boolean as_name;
    private Boolean as_custom_id;
    private Boolean as_mobile;
    private Boolean as_email;

    private List<String> corps;

    private Integer start_index;
    private Integer max_items;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getReturn_type() {
        return return_type;
    }

    public void setReturn_type(String return_type) {
        this.return_type = return_type;
    }

    public Boolean getAs_user_id() {
        return as_user_id;
    }

    public void setAs_user_id(Boolean as_user_id) {
        this.as_user_id = as_user_id;
    }

    public Boolean getAs_name() {
        return as_name;
    }

    public void setAs_name(Boolean as_name) {
        this.as_name = as_name;
    }

    public Boolean getAs_custom_id() {
        return as_custom_id;
    }

    public void setAs_custom_id(Boolean as_custom_id) {
        this.as_custom_id = as_custom_id;
    }

    public Boolean getAs_mobile() {
        return as_mobile;
    }

    public void setAs_mobile(Boolean as_mobile) {
        this.as_mobile = as_mobile;
    }

    public Boolean getAs_email() {
        return as_email;
    }

    public void setAs_email(Boolean as_email) {
        this.as_email = as_email;
    }

    public Integer getStart_index() {
        return start_index;
    }

    public void setStart_index(Integer start_index) {
        this.start_index = start_index;
    }

    public Integer getMax_items() {
        return max_items;
    }

    public void setMax_items(Integer max_items) {
        this.max_items = max_items;
    }

    public List<String> getCorps() {
        return corps;
    }

    public void setCorps(List<String> corps) {
        this.corps = corps;
    }
}
