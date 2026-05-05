package com.aliyun.alink.sdk.bone.plugins.app;

import android.os.Bundle;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class ConvertUtils {
    public static Bundle toBundle(JSONObject jSONObject) throws JSONException {
        Bundle bundle = new Bundle(jSONObject.length());
        if (jSONObject == null) {
            return null;
        }
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            Object obj = jSONObject.get(next);
            if (obj instanceof String) {
                bundle.putString(next, (String) obj);
            } else if (obj instanceof Boolean) {
                bundle.putBoolean(next, ((Boolean) obj).booleanValue());
            } else if (obj instanceof Integer) {
                bundle.putInt(next, ((Integer) obj).intValue());
            } else if (obj instanceof Long) {
                bundle.putDouble(next, ((Long) obj).longValue());
            } else if (obj instanceof Double) {
                bundle.putDouble(next, ((Double) obj).doubleValue());
            } else if (obj instanceof JSONObject) {
                bundle.putBundle(next, toBundle((JSONObject) obj));
            } else if (obj instanceof JSONArray) {
                Object objA = a((JSONArray) obj);
                if (objA instanceof String[]) {
                    bundle.putStringArray(next, (String[]) objA);
                } else if (objA instanceof int[]) {
                    bundle.putIntArray(next, (int[]) objA);
                } else if (objA instanceof long[]) {
                    bundle.putLongArray(next, (long[]) objA);
                } else if (objA instanceof boolean[]) {
                    bundle.putBooleanArray(next, (boolean[]) objA);
                } else if (objA instanceof double[]) {
                    bundle.putDoubleArray(next, (double[]) objA);
                } else if (objA instanceof Bundle[]) {
                    bundle.putParcelableArray(next, (Bundle[]) objA);
                } else if (objA instanceof Object[]) {
                    bundle.putStringArray(next, new String[0]);
                } else {
                    throw new IllegalArgumentException("unsupported array type : " + objA.getClass());
                }
            } else if (!jSONObject.isNull(next)) {
                throw new IllegalArgumentException("Could not convert object with key: " + next + ".");
            }
        }
        return bundle;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [android.os.Bundle[]] */
    /* JADX WARN: Type inference failed for: r2v2, types: [double[]] */
    /* JADX WARN: Type inference failed for: r2v4, types: [long[]] */
    /* JADX WARN: Type inference failed for: r2v5, types: [int[]] */
    /* JADX WARN: Type inference failed for: r2v7, types: [java.lang.String[]] */
    private static Object a(JSONArray jSONArray) {
        boolean[] zArr;
        int length = jSONArray.length();
        int i = 0;
        if (length == 0) {
            return new Object[0];
        }
        Object objOpt = jSONArray.opt(0);
        if (objOpt instanceof String) {
            zArr = new String[length];
            while (i < length) {
                zArr[i] = jSONArray.optString(i);
                i++;
            }
        } else if (objOpt instanceof Integer) {
            zArr = new int[length];
            while (i < length) {
                zArr[i] = jSONArray.optInt(i);
                i++;
            }
        } else if (objOpt instanceof Long) {
            zArr = new long[length];
            while (i < length) {
                zArr[i] = jSONArray.optLong(i);
                i++;
            }
        } else if (objOpt instanceof Boolean) {
            zArr = new boolean[length];
            while (i < length) {
                zArr[i] = jSONArray.optBoolean(i);
                i++;
            }
        } else if (objOpt instanceof Double) {
            zArr = new double[length];
            while (i < length) {
                zArr[i] = jSONArray.optDouble(i);
                i++;
            }
        } else if (objOpt instanceof JSONObject) {
            zArr = new Bundle[length];
            while (i < length) {
                zArr[i] = toBundle(jSONArray.optJSONObject(i));
                i++;
            }
        } else {
            throw new IllegalArgumentException("unsupported type" + objOpt.getClass() + " in array");
        }
        return zArr;
    }

    public static JSONObject fromBundle(Bundle bundle) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        if (bundle == null) {
            return jSONObject;
        }
        for (String str : bundle.keySet()) {
            Object obj = bundle.get(str);
            if (obj != null) {
                if (obj.getClass().isArray()) {
                    jSONObject.put(str, fromArray(obj));
                } else if (obj instanceof String) {
                    jSONObject.put(str, (String) obj);
                } else if (obj instanceof Number) {
                    if (obj instanceof Integer) {
                        jSONObject.put(str, obj);
                    } else {
                        jSONObject.put(str, ((Number) obj).doubleValue());
                    }
                } else if (obj instanceof Boolean) {
                    jSONObject.put(str, (Boolean) obj);
                } else if (obj instanceof Bundle) {
                    jSONObject.put(str, fromBundle((Bundle) obj));
                } else {
                    throw new IllegalArgumentException("Could not convert " + obj.getClass());
                }
            }
        }
        return jSONObject;
    }

    public static JSONArray fromArray(Object obj) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        int i = 0;
        if (obj instanceof String[]) {
            String[] strArr = (String[]) obj;
            int length = strArr.length;
            while (i < length) {
                jSONArray.put(strArr[i]);
                i++;
            }
        } else if (obj instanceof Bundle[]) {
            Bundle[] bundleArr = (Bundle[]) obj;
            int length2 = bundleArr.length;
            while (i < length2) {
                jSONArray.put(fromBundle(bundleArr[i]));
                i++;
            }
        } else if (obj instanceof int[]) {
            int[] iArr = (int[]) obj;
            int length3 = iArr.length;
            while (i < length3) {
                jSONArray.put(iArr[i]);
                i++;
            }
        } else if (obj instanceof float[]) {
            int length4 = ((float[]) obj).length;
            while (i < length4) {
                jSONArray.put(r5[i]);
                i++;
            }
        } else if (obj instanceof double[]) {
            double[] dArr = (double[]) obj;
            int length5 = dArr.length;
            while (i < length5) {
                jSONArray.put(dArr[i]);
                i++;
            }
        } else if (obj instanceof boolean[]) {
            boolean[] zArr = (boolean[]) obj;
            int length6 = zArr.length;
            while (i < length6) {
                jSONArray.put(zArr[i]);
                i++;
            }
        } else {
            throw new IllegalArgumentException("Unknown array type " + obj.getClass());
        }
        return jSONArray;
    }
}
