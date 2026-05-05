package com.aliyun.iotx.linkvisual.media.audio;

/* JADX INFO: loaded from: classes2.dex */
public interface ILVOssUploadCallback {
    void onCompletion(String str, String str2);

    void onError(String str, String str2, int i, String str3);

    void onProgress(long j, long j2);
}
