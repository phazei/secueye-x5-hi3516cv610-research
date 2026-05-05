package com.taobao.accs.utl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class JsonUtility {
    public static String getString(JSONObject jSONObject, String str, String str2) throws JSONException {
        String strOptString;
        return (jSONObject == null || !jSONObject.has(str) || (strOptString = jSONObject.optString(str, null)) == null) ? str2 : strOptString;
    }

    public static Map<String, String> getMap(JSONObject jSONObject, String str) {
        if (jSONObject == null || !jSONObject.has(str)) {
            return null;
        }
        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject(str);
        Iterator<String> itKeys = jSONObjectOptJSONObject.keys();
        HashMap map = new HashMap();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            map.put(next, jSONObjectOptJSONObject.optString(next));
        }
        return map;
    }

    public static Map<String, String> toMap(JSONObject jSONObject) throws JSONException {
        HashMap map = new HashMap();
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            map.put(next, jSONObject.get(next) == null ? null : jSONObject.get(next).toString());
        }
        return map;
    }

    /* JADX INFO: compiled from: Taobao */
    public static class JsonObjectBuilder {
        JSONObject jObject = new JSONObject();

        public JsonObjectBuilder put(String str, String str2) {
            if (str2 != null && str != null) {
                try {
                    this.jObject.put(str, str2);
                } catch (JSONException unused) {
                }
            }
            return this;
        }

        public JsonObjectBuilder put(String str, Integer num) {
            if (num == null) {
                return this;
            }
            try {
                this.jObject.put(str, num);
            } catch (JSONException unused) {
            }
            return this;
        }

        public JsonObjectBuilder put(String str, Boolean bool) {
            if (bool == null) {
                return this;
            }
            try {
                this.jObject.put(str, bool);
            } catch (JSONException unused) {
            }
            return this;
        }

        public JsonObjectBuilder put(String str, Long l) {
            if (l == null) {
                return this;
            }
            try {
                this.jObject.put(str, l);
            } catch (JSONException unused) {
            }
            return this;
        }

        public JsonObjectBuilder put(String str, JSONArray jSONArray) {
            if (jSONArray == null) {
                return this;
            }
            try {
                this.jObject.put(str, jSONArray);
            } catch (JSONException unused) {
            }
            return this;
        }

        public JSONObject build() {
            return this.jObject;
        }
    }
}
