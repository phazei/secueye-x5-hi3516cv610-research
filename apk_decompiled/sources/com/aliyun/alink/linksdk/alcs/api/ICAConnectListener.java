package com.aliyun.alink.linksdk.alcs.api;

import com.aliyun.alink.linksdk.alcs.data.ica.ICADeviceInfo;

/* JADX INFO: loaded from: classes2.dex */
public interface ICAConnectListener {
    public static final int ALCS_CONN_AUTHLISTEMPTY = 504;
    public static final int ALCS_CONN_ILLEGALSIGN = 506;
    public static final int ALCS_CONN_INVALIDPARAM = 503;
    public static final int ALCS_CONN_OK = 200;
    public static final int ALCS_CONN_REVOCATE = 501;
    public static final int ALCS_CONN_UNMATCHPREFIX = 502;
    public static final int ALCS_CONN_VERNOTSUPPORT = 505;

    void onLoad(int i, String str, ICADeviceInfo iCADeviceInfo);
}
