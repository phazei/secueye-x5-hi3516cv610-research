package com.aliyun.alink.business.devicecenter.biz;

import com.alibaba.fastjson.JSONArray;

/* JADX INFO: loaded from: classes.dex */
public interface MeshDiscoverCallback {
    void onFailure(String str);

    void onSuccess(JSONArray jSONArray);
}
