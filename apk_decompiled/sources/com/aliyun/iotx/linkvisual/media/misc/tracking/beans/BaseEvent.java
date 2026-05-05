package com.aliyun.iotx.linkvisual.media.misc.tracking.beans;

import com.alibaba.fastjson.JSON;
import java.text.SimpleDateFormat;
import java.util.Date;

/* JADX INFO: loaded from: classes2.dex */
public class BaseEvent {
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    int code;
    String message;
    String method;
    String version;

    public BaseEvent() {
    }

    public BaseEvent(String str, String str2) {
        this.version = str;
        this.method = str2;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getMethod() {
        return this.method;
    }

    public String getVersion() {
        return this.version;
    }

    public BaseEvent setCode(int i) {
        this.code = i;
        return this;
    }

    public BaseEvent setMessage(String str) {
        this.message = str;
        return this;
    }

    public void setMethod(String str) {
        this.method = str;
    }

    public void setVersion(String str) {
        this.version = str;
    }

    public String toString() {
        return "[" + simpleDateFormat.format(new Date()) + "] type=app; result=" + JSON.toJSONString(this);
    }
}
