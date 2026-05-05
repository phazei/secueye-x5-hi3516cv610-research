package com.alibaba.ailabs.iot.aisbase.plugin.auth;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.plugin.IPlugin;

/* JADX INFO: loaded from: classes.dex */
public interface IAuthPlugin extends IPlugin {
    public static final String EVENT_AUTH_FAILED = "auth_failed";
    public static final String EVENT_AUTH_SUCCESS = "auth_success";

    void setAuthParams(byte[] bArr, String str, IActionListener<byte[]> iActionListener);
}
