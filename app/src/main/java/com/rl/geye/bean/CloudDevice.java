package com.rl.geye.bean;

import android.text.TextUtils;

import com.rl.geye.db.bean.EdwinDevice;

public class CloudDevice {
    private String devID;
    private int devType;
    private int userType;
    private String username;
    private String status;

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getDevID() {
        return devID;
    }

    public void setDevID(String devID) {
        this.devID = devID;
    }

    public int getDevType() {
        return devType;
    }

    public void setDevType(int devType) {
        this.devType = devType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        String dstID = null;

        if (o.getClass() == EdwinDevice.class){
            EdwinDevice that = (EdwinDevice)o;
            dstID = that.getDevId();
        }
        if (o.getClass() == getClass()){
            CloudDevice that = (CloudDevice) o;
            dstID = that.getDevID();
        }
        return dstID != null && TextUtils.equals(devID, dstID);
    }
}
