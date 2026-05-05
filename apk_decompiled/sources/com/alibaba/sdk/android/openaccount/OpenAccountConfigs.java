package com.alibaba.sdk.android.openaccount;

import com.alibaba.sdk.android.openaccount.mtop.MtopLoginService;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class OpenAccountConfigs {
    public static String clientLocal = "zh_CN";
    public static boolean enableOpenAccountMtopSession = false;
    public static final Map<String, String> extraRpcHttpHeaders = new HashMap();
    public static boolean ignoreWebViewSSLError = false;
    public static String jaqAppKey;
    public static MtopLoginService mtopLoginService;
    public static String stringResourceSuffix;
}
