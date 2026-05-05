package com.aliyun.alink.linksdk.alcs.lpbs.data;

import com.aliyun.alink.linksdk.alcs.lpbs.api.AlcsPalConst;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;

/* JADX INFO: loaded from: classes2.dex */
public class PalConnectParams<ExtraConnectparams> {
    public Object authInfo;
    public int connectKeepStrategy = AlcsPalConst.CONNECT_KEEP_TYPE_DYNAMIC;
    public String dataFormat;
    public PalDeviceInfo deviceInfo;
    public ExtraConnectparams extraConnectParams;
    public String pluginId;

    public String getDevId() {
        PalDeviceInfo palDeviceInfo = this.deviceInfo;
        return palDeviceInfo == null ? TmpConstant.GROUP_ROLE_UNKNOWN : palDeviceInfo.getDevId();
    }
}
