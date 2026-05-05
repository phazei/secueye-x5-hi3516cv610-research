package com.aliyun.alink.linksdk.tmp.service;

import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.linksdk.tmp.data.ut.PerformancePointData;
import com.aliyun.alink.linksdk.tmp.device.panel.data.PanelMethodExtraData;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class UTPoint {
    private static final String PerformanceTag = "PerformanceTag";
    private static final String TAG = "[Tmp]UTPoint";
    private PerformancePointData mPerformancePointData;

    public UTPoint(String str, String str2, String str3, String str4) {
        this.mPerformancePointData = new PerformancePointData(str, str2, str3, str4);
    }

    public UTPoint(PerformancePointData performancePointData) {
        this.mPerformancePointData = performancePointData;
    }

    public String getPerformanceId() {
        PerformancePointData performancePointData = this.mPerformancePointData;
        if (performancePointData != null) {
            return performancePointData.id;
        }
        return null;
    }

    public void trackStart() {
        PerformancePointData performancePointData = this.mPerformancePointData;
        if (performancePointData == null || TextUtils.isEmpty(performancePointData.id)) {
            return;
        }
        this.mPerformancePointData.f4298event = "req";
        ALog.d(TAG, "trackStart toJSONString start");
        ALog.i("PerformanceTag", getPerformanceLog(this.mPerformancePointData));
    }

    public void trackEnd() {
        PerformancePointData performancePointData = this.mPerformancePointData;
        if (performancePointData == null || TextUtils.isEmpty(performancePointData.id)) {
            return;
        }
        this.mPerformancePointData.f4298event = "res";
        ALog.d(TAG, "trackEnd toJSONString start");
        ALog.i("PerformanceTag", getPerformanceLog(this.mPerformancePointData));
    }

    public static UTPoint createUTPoint(PanelMethodExtraData panelMethodExtraData, String str) {
        if (panelMethodExtraData == null) {
            return null;
        }
        String str2 = panelMethodExtraData.performanceId;
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        PerformancePointData performancePointData = new PerformancePointData();
        performancePointData.method = str;
        performancePointData.f4298event = "req";
        performancePointData.mod = "tmp";
        performancePointData.id = str2;
        return new UTPoint(performancePointData);
    }

    public static UTPoint createUTPoint(Map<String, Object> map, String str) {
        String strValueOf;
        if (map == null) {
            return null;
        }
        try {
            Object obj = map.get(TmpConstant.KEY_IOT_PERFORMANCE_ID);
            strValueOf = obj != null ? String.valueOf(obj) : null;
        } catch (Exception unused) {
            strValueOf = null;
        }
        if (TextUtils.isEmpty(strValueOf)) {
            return null;
        }
        PerformancePointData performancePointData = new PerformancePointData();
        performancePointData.method = str;
        performancePointData.f4298event = "req";
        performancePointData.mod = "tmp";
        performancePointData.id = strValueOf;
        return new UTPoint(performancePointData);
    }

    public static String getPerformanceLog(PerformancePointData performancePointData) {
        if (performancePointData == null) {
            return null;
        }
        HashMap map = new HashMap();
        map.put("id", performancePointData.id);
        map.put("mod", performancePointData.mod);
        map.put("method", performancePointData.method);
        map.put(NotificationCompat.CATEGORY_EVENT, performancePointData.f4298event);
        return JSON.toJSONString(map);
    }
}
