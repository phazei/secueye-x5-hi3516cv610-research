package com.alibaba.ailabs.iot.aisbase.plugin.ota;

import com.alibaba.ailabs.iot.aisbase.Da;
import com.alibaba.ailabs.iot.aisbase.RequestManage;
import com.alibaba.fastjson.JSONObject;
import com.huawei.hms.push.constant.RemoteMessageConst;

/* JADX INFO: loaded from: classes.dex */
public class ReportProgressUtil {
    public static final String CODE_ERR = "ERR";
    public static final String CODE_OK = "OK";
    public static final String TAG_FINISH = "FINISH";
    public static final String TAG_START = "START";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f2639a = "ReportProgressUtil";

    public static void reportOtaProgress(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("currentVersion", (Object) str);
        jSONObject.put("targetVersion", (Object) str2);
        jSONObject.put(RemoteMessageConst.Notification.TAG, (Object) str5);
        jSONObject.put("code", (Object) str6);
        jSONObject.put("message", (Object) str7);
        RequestManage.getInstance().gmaOtaProgressReport(str3, str4, jSONObject.toJSONString(), new Da());
    }
}
