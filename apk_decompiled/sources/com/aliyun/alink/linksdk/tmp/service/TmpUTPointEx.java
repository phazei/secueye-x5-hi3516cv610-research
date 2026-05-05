package com.aliyun.alink.linksdk.tmp.service;

import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.alink.linksdk.tools.ut.AUserTrack;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class TmpUTPointEx {
    private static final String TAG = "[Tmp]TmpUTPointEx";
    public static final int UNKNOWN_ERRORCODE = -1;
    private static final String UTEVENT_SETPROP = "setPropties";
    private static final String UTKEY_CHANNEL = "channel";
    public static final String UTVALUE_ALCS = "alcs";
    public static final String UTVALUE_CLOUD = "cloud";
    private static final String UTVALUE_TMP = "tmp";
    protected Map<String, String> mUtProperties = new HashMap();

    public TmpUTPointEx(String str, String str2, String str3) {
        ALog.w(TAG, "TmpUTPointEx iotId:" + str + " performanceId:" + str2 + " channel:" + str3);
        this.mUtProperties.put(AUserTrack.UTKEY_PERFORMANCEID, str2);
        this.mUtProperties.put("deviceIotId", str);
        this.mUtProperties.put("channel", str3);
        this.mUtProperties.put(AUserTrack.UTKEY_MODULE, UTVALUE_TMP);
    }

    public void trackStart() {
        this.mUtProperties.put(AUserTrack.UTKEY_START_TIME, String.valueOf(System.currentTimeMillis()));
    }

    public void trackEnd(int i) {
        this.mUtProperties.put(AUserTrack.UTKEY_END_TIME, String.valueOf(System.currentTimeMillis()));
        this.mUtProperties.put("errorCode", String.valueOf(i));
        AUserTrack.record(UTEVENT_SETPROP, this.mUtProperties);
    }
}
