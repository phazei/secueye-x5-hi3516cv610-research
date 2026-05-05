package com.aliyun.alink.linksdk.alcs.lpbs.data;

import com.aliyun.alink.linksdk.alcs.lpbs.api.AlcsPalConst;

/* JADX INFO: loaded from: classes2.dex */
public class PalProbeResult {
    public static final int PAL_PROBE_ERROR_RSPERROR = 2;
    public static final int PAL_PROBE_ERROR_TIMEOUT = 1;
    public static final int PAL_PROBE_OK = 0;
    public String probePluginId;
    public int probeResult;

    public PalProbeResult(int i) {
        this(i, AlcsPalConst.DEFAULT_PLUGIN_ID);
    }

    public PalProbeResult(int i, String str) {
        this.probeResult = i;
        this.probePluginId = str;
    }
}
