package com.rl.geye.util;

import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.convert.StringConvert;

import okhttp3.Response;

public abstract class CgiCallback extends AbsCallback<String> {

    public Object paramOutside;

    public CgiCallback(Object paramOutside) {
        this.paramOutside = paramOutside;
    }

    @Override
    public String convertSuccess(Response response) throws Exception {
        String s = StringConvert.create().convertSuccess(response);
        response.close();
        return s;
    }
}
