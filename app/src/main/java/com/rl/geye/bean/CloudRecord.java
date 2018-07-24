package com.rl.geye.bean;

import android.widget.Checkable;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.rl.geye.constants.Constants;
import com.rl.geye.db.bean.EdwinDevice;
import com.rl.geye.db.bean.SubDevice;
import com.rl.p2plib.constants.P2PConstants;

public class CloudRecord implements Checkable, MultiItemEntity {
    private String devId;
    private int type;
    private int callStatus;
    private String date;
    private String file1;
    private String file2;
    private String file3;
    private EdwinDevice device; // 关联设备
    private SubDevice subDev;   // 子设备（报警器、遥控器）
    private boolean checked;

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(int callStatus) {
        this.callStatus = callStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFile1() {
        return file1;
    }

    public void setFile1(String file1) {
        this.file1 = file1;
    }

    public String getFile2() {
        return file2;
    }

    public void setFile2(String file2) {
        this.file2 = file2;
    }

    public String getFile3() {
        return file3;
    }

    public void setFile3(String file3) {
        this.file3 = file3;
    }

    public boolean isChecked(){
        return checked;
    }

    public boolean isAnswered(){
        switch (device.getType()) {
            case P2PConstants.DeviceType.CAT_DOUBLE_EYE:
            case P2PConstants.DeviceType.CAT_SING_EYE:
                return ((callStatus & 0xff0000) >> 16) != 0;
        }
        return callStatus != 0;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public EdwinDevice getDevice(){
        return device;
    }

    public void setDevice(EdwinDevice device) {
        synchronized (this) {
            this.device = device;
        }
    }

    public SubDevice getSubDev() {
        return subDev;
    }

    public void setSubDev(SubDevice subDev) {
        this.subDev = subDev;
    }

    public void toggle() {
        checked = !checked;
    }

    @Override
    public int getItemType() {
        return Constants.LevelType.CHILD;
    }
}
