package com.ejiahe.sdk.openbean;


import com.ejiahe.jeservice.util.CodeResult;

import java.util.Date;

/**
 * 文件查询
 * Created by focus on 2018/3/13.
 */
public class FileQueryResult extends CodeResult {

    private String status;
    private long size;
    private long uploadedBytes;
    private Date creationDate;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getUploadedBytes() {
        return uploadedBytes;
    }

    public void setUploadedBytes(long uploadedBytes) {
        this.uploadedBytes = uploadedBytes;
    }
}
