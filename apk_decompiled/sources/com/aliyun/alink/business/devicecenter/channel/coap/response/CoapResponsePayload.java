package com.aliyun.alink.business.devicecenter.channel.coap.response;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class CoapResponsePayload<T> implements Serializable {
    public String code;
    public T data = null;
    public String id;
}
