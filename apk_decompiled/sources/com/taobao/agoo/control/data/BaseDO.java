package com.taobao.agoo.control.data;

/* JADX INFO: loaded from: classes3.dex */
public abstract class BaseDO {
    public static final String JSON_CMD = "cmd";
    public static final String JSON_DEVICE_ID = "deviceId";
    public static final String JSON_ERRORCODE = "resultCode";
    public static final String JSON_SUCCESS = "success";
    public static final String JSON_UTDID = "utdid";
    public String cmd;

    public abstract byte[] buildData();
}
