package com.alibaba.sdk.android.openaccount.model;

import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class OAWSecurityData {
    public String apdId;
    public String t;
    public String umidToken;
    public String wua;

    public String toString() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("wua", this.wua);
            jSONObject.put("apdId", this.apdId);
            jSONObject.put("umidToken", this.umidToken);
            jSONObject.put("t", this.t);
        } catch (Exception unused) {
        }
        return jSONObject.toString();
    }
}
