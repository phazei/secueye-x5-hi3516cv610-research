package com.aliyun.iot.aep.sdk.framework.log;

/* JADX INFO: loaded from: classes2.dex */
public interface IALogCloud {
    void configCloudLog(String str, String str2, String str3, String str4);

    void d(String str, String str2);

    void d(String str, String str2, boolean z);

    void e(String str, String str2);

    void e(String str, String str2, Exception exc);

    void e(String str, String str2, Exception exc, boolean z);

    void e(String str, String str2, String str3);

    void e(String str, String str2, String str3, boolean z);

    void e(String str, String str2, boolean z);

    void i(String str, String str2);

    void i(String str, String str2, boolean z);

    void setLevel(byte b2);

    void w(String str, String str2);

    void w(String str, String str2, boolean z);
}
