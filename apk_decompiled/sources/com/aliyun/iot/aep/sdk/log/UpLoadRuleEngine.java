package com.aliyun.iot.aep.sdk.log;

import com.huawei.hms.push.constant.RemoteMessageConst;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class UpLoadRuleEngine {
    public static boolean needUpload(JSONObject jSONObject) {
        try {
            return RuleManager.hasTag("default_ruleset", jSONObject.getString(RemoteMessageConst.Notification.TAG));
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void setRule(String str) {
    }
}
