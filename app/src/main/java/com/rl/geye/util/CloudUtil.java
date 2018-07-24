package com.rl.geye.util;

import android.util.Log;

import com.lzy.okgo.OkGo;
import com.rl.geye.bean.CloudRecord;
import com.rl.geye.bean.CloudRecordsResponse;
import com.rl.geye.constants.Constants;
import com.rl.p2plib.utils.JSONUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;


public class CloudUtil {
    private String username;	// 云用户
    private String password;	// 云用户密码
    private String accessToken;	// 云用户令牌
    private int result;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public synchronized void getCloudRecords(CgiCallback call){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("username", username);
        requestParams.put("accessToken", accessToken);
        String mGetRecordCgi = Constants.CloudCgi.CgiGetRecord;

        OkGo.post(mGetRecordCgi)
        .tag(this)
        .params(requestParams)
        .execute(call);
    }

    public synchronized List<CloudRecord> getCloudRecords(String dateStart,String dateEnd) throws IOException{
            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("username", username);
            requestParams.put("dateStart",dateStart);
            requestParams.put("dateEnd",dateEnd);
            requestParams.put("accessToken", accessToken);
            String mGetRecordCgi = Constants.CloudCgi.CgiGetRecord;

            Log.e("cloud","request records from [" + dateStart + "] - [" + dateEnd + "]");

            Response response = OkGo.post(mGetRecordCgi)
            .tag(this)
            .params(requestParams)
            .execute();

            if(response == null) return null;
        //           Log.e("cloud",response.body().string());
            CloudRecordsResponse cloudRecordsResponse = JSONUtil.fromJson(response.body().string(), CloudRecordsResponse.class);
            if(cloudRecordsResponse == null){
                return null;
            }
            switch (cloudRecordsResponse.getStatus()){
                case 1:
                    Log.e("cloud","events count:[" + cloudRecordsResponse.getDevRecords().size() + "]");
                    return cloudRecordsResponse.getDevRecords();
                default:
                    break;
            }
            return null;
    }

    public synchronized void getCloudDevices(CgiCallback call){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("username", username);
        requestParams.put("accessToken", accessToken);
        String mGetRecordCgi = Constants.CloudCgi.CgiGetDevices;

        Log.e("cloud","start capture device list.");

        OkGo.post(mGetRecordCgi)
        .tag(this)
        .params(requestParams)
        .execute(call);
    }

    public void removeCloudDevice(String deviceID, CgiCallback call){
        removeCloudDevice(deviceID,username,call);
    }

    public synchronized void removeCloudDevice(String deviceID, String username, CgiCallback call){

        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("username", username);
        requestParams.put("dev", deviceID);
        requestParams.put("accessToken",accessToken);
        String mDelDeviceCgi = Constants.CloudCgi.CgiDelDevice;

        Log.e("cloud","delete user:[" + username + "] from device:[" + deviceID + "]");

        OkGo.delete(mDelDeviceCgi);
        OkGo.post(mDelDeviceCgi)
            .tag(this)
            .params(requestParams)
            .execute(call);
    }

    public synchronized void insertCloudDevice(String deviceID,int deviceType,int userType,String addCode,CgiCallback call){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("username", username);
        requestParams.put("dev", deviceID);
        requestParams.put("devType", String.valueOf(deviceType));
        requestParams.put("userType",String.valueOf(userType));
        if(addCode != null && addCode.isEmpty() == false) {
            requestParams.put("addCode", addCode);
        }
        requestParams.put("accessToken",accessToken);
        String mLoginCgi = Constants.CloudCgi.CgiAddDevice;

        Log.e("cloud","insert device:["+ deviceID +"] by share mode:[" + addCode + "]");

        OkGo.post(mLoginCgi)
                .tag(this)
                .params(requestParams)
                .execute(call);
    }

    public synchronized void insertCloudDevice(String deviceID,int deviceType,int userType,CgiCallback call) {
        insertCloudDevice(deviceID,deviceType,userType,null,call);
    }

    public synchronized void addShareCode(String devID,String addCode,int timeout,CgiCallback call){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("devId", devID);
        requestParams.put("addCode",addCode);
        requestParams.put("timeout",String.valueOf(timeout));
        requestParams.put("accessToken", accessToken);
        String mAddShareCodeCgi = Constants.CloudCgi.CgiAddShareCode;

        Log.e("cloud","share device:["+ devID +"] by share mode:[" + addCode + "]");

        OkGo.delete(mAddShareCodeCgi);
        OkGo.post(mAddShareCodeCgi)
                .tag(this)
                .params(requestParams)
                .execute(call);
    }

    public synchronized void getUsersByDevID(String devID,CgiCallback call){
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("devId", devID);
        requestParams.put("username", username);
        requestParams.put("accessToken", accessToken);
        String mGetUsersByDevID = Constants.CloudCgi.CgiGetUsersByDevID;

        Log.e("cloud","get users list by device:["+ devID +"] from user:[" + username + "]");

        OkGo.delete(mGetUsersByDevID);
        OkGo.post(mGetUsersByDevID)
                .tag(this)
                .params(requestParams)
                .execute(call);
    }
}
