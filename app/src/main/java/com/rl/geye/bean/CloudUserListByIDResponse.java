package com.rl.geye.bean;

import java.util.List;

public class CloudUserListByIDResponse {
    private int status;
    private int msg;
    private int userNo;
    private List<CloudListUser> users;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<CloudListUser> getUsers() {
        return users;
    }

    public void setUsers(List<CloudListUser> users) {
        this.users = users;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }
}
