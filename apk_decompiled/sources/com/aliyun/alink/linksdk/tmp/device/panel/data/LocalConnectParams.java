package com.aliyun.alink.linksdk.tmp.device.panel.data;

import androidx.annotation.NonNull;
import com.aliyun.alink.linksdk.alcs.lpbs.api.AlcsPalConst;

/* JADX INFO: loaded from: classes2.dex */
public class LocalConnectParams {
    public int mConnectKeepType = AlcsPalConst.CONNECT_KEEP_TYPE_DYNAMIC;

    @NonNull
    public String toString() {
        return "mConnectKeepType:" + this.mConnectKeepType;
    }
}
