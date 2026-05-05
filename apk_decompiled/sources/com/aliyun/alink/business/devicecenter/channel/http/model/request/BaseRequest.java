package com.aliyun.alink.business.devicecenter.channel.http.model.request;

import com.aliyun.alink.business.devicecenter.channel.http.model.DataObject;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class BaseRequest extends DataObject {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Map<String, Object> f3506a;

    public Map<String, Object> getExtraInfo() {
        return this.f3506a;
    }

    public void setExtraInfo(Map<String, Object> map) {
        this.f3506a = map;
    }
}
