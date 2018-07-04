package com.rl.p2plib.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class DevLockResult implements Parcelable {
    int doornumb;
    int openmode;
    int result;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public int getDoornumb() {
        return doornumb;
    }

    public void setDoornumb(int doornumb) {
        this.doornumb = doornumb;
    }

    public int getOpenmode() {
        return openmode;
    }

    public void setOpenmode(int openmode) {
        this.openmode = openmode;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
