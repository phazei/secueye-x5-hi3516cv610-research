package com.aliyun.alink.linksdk.tmp.device.payload.cloud;

import com.aliyun.alink.linksdk.cmp.core.base.AResponse;

/* JADX INFO: loaded from: classes2.dex */
public class ResponsePayload extends AResponse {
    public int code;
    public int id;

    public ResponsePayload(int i, int i2) {
        this.id = i;
        this.code = i2;
    }
}
