package com.aliyun.alink.linksdk.alcs.lpbs.component.jsengine;

/* JADX INFO: loaded from: classes2.dex */
public interface IJSEngine {
    public static final String PROTOCAL_TO_RAWDATA_FUNCNAME = "protocolToRawData";
    public static final String RAWDATA_TO_PROTOCAL_FUNCNAME = "rawDataToProtocol";

    byte[] protocolToRawData(String str, String str2);

    String rawDataToProtocol(String str, byte[] bArr);
}
