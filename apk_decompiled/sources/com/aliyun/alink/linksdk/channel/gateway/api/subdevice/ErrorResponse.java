package com.aliyun.alink.linksdk.channel.gateway.api.subdevice;

import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;

/* JADX INFO: loaded from: classes2.dex */
public class ErrorResponse<T> implements Serializable {
    public String code = null;
    public T data = null;
    public String message = null;

    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
