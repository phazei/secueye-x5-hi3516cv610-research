package com.alibaba.sdk.android.emas;

import android.text.TextUtils;
import com.heytap.mcssdk.constant.IntentConstant;
import java.nio.charset.Charset;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: EmasSingleLog.java */
/* JADX INFO: loaded from: classes.dex */
public class g {
    String h;
    String i;
    long timestamp;

    public g(String str, String str2, long j) {
        this.i = str;
        this.h = str2;
        this.timestamp = j;
    }

    public int length() {
        return this.h.getBytes(Charset.forName("UTF-8")).length;
    }

    public JSONObject a() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(IntentConstant.EVENT_ID, this.i);
            jSONObject.put("rawLog", this.h);
            jSONObject.put("timestamp", this.timestamp);
            return jSONObject;
        } catch (JSONException unused) {
            return null;
        }
    }

    public static g a(JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        String strOptString = jSONObject.optString(IntentConstant.EVENT_ID);
        String strOptString2 = jSONObject.optString("rawLog");
        long jOptLong = jSONObject.optLong("timestamp");
        if (TextUtils.isEmpty(strOptString) || TextUtils.isEmpty(strOptString2) || jOptLong == 0) {
            return null;
        }
        return new g(strOptString, strOptString2, jOptLong);
    }
}
