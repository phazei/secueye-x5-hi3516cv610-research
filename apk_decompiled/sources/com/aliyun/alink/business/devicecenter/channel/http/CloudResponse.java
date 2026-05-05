package com.aliyun.alink.business.devicecenter.channel.http;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class CloudResponse<T> implements Serializable {
    public String code;
    public T data = null;
    public String id;
}
