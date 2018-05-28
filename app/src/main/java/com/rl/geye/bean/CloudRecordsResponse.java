package com.rl.geye.bean;

import java.util.List;

public class CloudRecordsResponse {
    private int status;
    private String msg;
    private List<CloudRecord> devRecords;

    public List<CloudRecord> getDevRecords() {
        return devRecords;
    }

    public void setDevRecords(List<CloudRecord> devRecords) {
        this.devRecords = devRecords;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
