package com.aliyun.iotx.linkvisual.media.audio;

/* JADX INFO: loaded from: classes2.dex */
public class LiveIntercomRequest {
    byte[] iv;
    byte[] key;
    String url;

    public byte[] getIv() {
        return this.iv;
    }

    public byte[] getKey() {
        return this.key;
    }

    public String getUrl() {
        return this.url;
    }

    public LiveIntercomRequest setIv(byte[] bArr) {
        this.iv = bArr;
        return this;
    }

    public LiveIntercomRequest setKey(byte[] bArr) {
        this.key = bArr;
        return this;
    }

    public LiveIntercomRequest setUrl(String str) {
        this.url = str;
        return this;
    }
}
