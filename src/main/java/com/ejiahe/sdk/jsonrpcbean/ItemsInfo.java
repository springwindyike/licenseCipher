package com.ejiahe.sdk.jsonrpcbean;

import java.util.List;

/**
 * Created by liaofei on 2018/1/11.
 */
public class ItemsInfo<T> {
    private Integer totalItems;

    private Integer startIndex;

    private Integer resultRows;

    private String returnType;

    private List<T> items;

//    //org.tenement.search接口时返回
//    private Integer count;
//
//    //org.tenement.search接口时返回
//    private List<T> tenements;
//
//    //org.department.getDepartments接口时返回
//    private List<T> departments;

    public Integer getTotalItems() {
        return totalItems;
//        if(totalItems != null){
//            return totalItems;
//        }else{
//            return count;
//        }
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getResultRows() {
        return resultRows;
    }

    public void setResultRows(Integer resultRows) {
        this.resultRows = resultRows;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<T> getItems() {
        return items;
//        if(items != null){
//            return items;
//        }else{
//            return tenements;
//        }
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

}
