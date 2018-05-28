package com.rl.geye.bean;

import java.util.List;

public class CloudDevicesResponse {
    private int status;
    private int msg;
    private List<CloudDevice> dev;
    private List<CloudDevice> authorization;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }

    public List<CloudDevice> getDev() {
        return dev;
    }

    public void setDev(List<CloudDevice> dev) {
        this.dev = dev;
    }

    public List<CloudDevice> getAuthorization() {
        return authorization;
    }

    public void setAuthorization(List<CloudDevice> authorization) {
        this.authorization = authorization;
    }
}
