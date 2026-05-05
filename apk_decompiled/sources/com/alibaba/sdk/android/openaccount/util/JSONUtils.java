package com.alibaba.sdk.android.openaccount.util;

import android.util.Base64;
import com.alibaba.sdk.android.openaccount.model.Result;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class JSONUtils {
    public static Integer optInteger(JSONObject jSONObject, String str) {
        if (jSONObject.has(str)) {
            return Integer.valueOf(jSONObject.optInt(str));
        }
        return null;
    }

    public static String optString(JSONObject jSONObject, String str) {
        if (jSONObject.has(str)) {
            return jSONObject.optString(str);
        }
        return null;
    }

    public static Long optLong(JSONObject jSONObject, String str) {
        if (jSONObject.has(str)) {
            return Long.valueOf(jSONObject.optLong(str));
        }
        return null;
    }

    public static Boolean optBoolean(JSONObject jSONObject, String str) {
        return Boolean.valueOf(jSONObject.has(str) ? jSONObject.optBoolean(str) : false);
    }

    public static String toJson(Map<String, Object> map) {
        return toJsonObject(map).toString();
    }

    public static JSONObject toJsonObject(Map<String, ? extends Object> map) {
        JSONObject jSONObject = new JSONObject();
        try {
            for (Map.Entry<String, ? extends Object> entry : map.entrySet()) {
                Object value = entry.getValue();
                if (value != null) {
                    if (value instanceof Map) {
                        jSONObject.put(entry.getKey(), toJsonObject((Map) value));
                    } else if (value instanceof List) {
                        jSONObject.put(entry.getKey(), toJsonArray((List<Object>) value));
                    } else if (value.getClass().isArray()) {
                        jSONObject.put(entry.getKey(), toJsonArray((Object[]) value));
                    } else {
                        jSONObject.put(entry.getKey(), value);
                    }
                }
            }
            return jSONObject;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static JSONArray toJsonArray(Object[] objArr) {
        JSONArray jSONArray = new JSONArray();
        for (Object obj : objArr) {
            if (obj instanceof Map) {
                jSONArray.put(toJsonObject((Map) obj));
            } else {
                jSONArray.put(obj);
            }
        }
        return jSONArray;
    }

    public static JSONArray toJsonArray(List<Object> list) {
        JSONArray jSONArray = new JSONArray();
        for (Object obj : list) {
            if (obj instanceof Map) {
                jSONArray.put(toJsonObject((Map) obj));
            } else {
                jSONArray.put(obj);
            }
        }
        return jSONArray;
    }

    public static Map<String, Object> toMap(JSONObject jSONObject) throws JSONException {
        HashMap map = new HashMap();
        if (jSONObject == null) {
            return map;
        }
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            Object objOpt = jSONObject.opt(next);
            if (objOpt instanceof JSONObject) {
                map.put(next, toMap((JSONObject) objOpt));
            } else if (objOpt instanceof JSONArray) {
                map.put(next, toList((JSONArray) objOpt));
            } else {
                map.put(next, objOpt);
            }
        }
        return map;
    }

    public static List<Object> toList(JSONArray jSONArray) throws JSONException {
        if (jSONArray == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList(jSONArray.length());
        int length = jSONArray.length();
        for (int i = 0; i < length; i++) {
            Object obj = jSONArray.get(i);
            if (obj instanceof JSONObject) {
                arrayList.add(toMap((JSONObject) obj));
            } else if (obj instanceof JSONArray) {
                arrayList.add(toList((JSONArray) obj));
            } else {
                arrayList.add(jSONArray.get(i));
            }
        }
        return arrayList;
    }

    public static String[] toStringArray(JSONArray jSONArray) throws JSONException {
        if (jSONArray == null) {
            return new String[0];
        }
        String[] strArr = new String[jSONArray.length()];
        int length = jSONArray.length();
        for (int i = 0; i < length; i++) {
            strArr[i] = jSONArray.optString(i);
        }
        return strArr;
    }

    /* JADX WARN: Type inference failed for: r2v5, types: [T, java.lang.String] */
    public static Result<String> toStringResult(String str) {
        Result<String> result = new Result<>();
        try {
            JSONObject jSONObject = new JSONObject(str);
            result.code = jSONObject.optInt("code");
            result.data = optString(jSONObject, "data");
            result.message = optString(jSONObject, "message");
            return result;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJSONString(Result<String> result) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("code", result.code);
            jSONObject.put("message", result.message);
            jSONObject.put("data", result.data);
            return jSONObject.toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v11, types: [T, java.lang.Character[]] */
    /* JADX WARN: Type inference failed for: r5v9, types: [T, java.lang.Byte[]] */
    public static <T> T parseStringValue(String str, Class<T> cls) {
        if (str == 0 || cls == null) {
            return null;
        }
        if (String.class.equals(cls)) {
            return str;
        }
        if (Short.TYPE.equals(cls) || Short.class.equals(cls)) {
            return (T) Short.valueOf(str);
        }
        if (Integer.TYPE.equals(cls) || Integer.class.equals(cls)) {
            return (T) Integer.valueOf(str);
        }
        if (Long.TYPE.equals(cls) || Long.class.equals(cls)) {
            return (T) Long.valueOf(str);
        }
        if (Boolean.TYPE.equals(cls) || Boolean.class.equals(cls)) {
            return (T) Boolean.valueOf(str);
        }
        if (Float.TYPE.equals(cls) || Float.class.equals(cls)) {
            return (T) Float.valueOf(str);
        }
        if (Double.TYPE.equals(cls) || Double.class.equals(cls)) {
            return (T) Double.valueOf(str);
        }
        if (Byte.TYPE.equals(cls) || Byte.class.equals(cls)) {
            return (T) Byte.valueOf(str);
        }
        int i = 0;
        if (Character.TYPE.equals(cls) || Character.class.equals(cls)) {
            return (T) Character.valueOf(str.charAt(0));
        }
        if (Date.class.isAssignableFrom(cls)) {
            try {
                return (T) new SimpleDateFormat("yyyyMMddHHmmssSSSZ", Locale.US).parse(str);
            } catch (ParseException e) {
                throw new RuntimeException("Parse Date error", e);
            }
        }
        char cCharAt = str.charAt(0);
        if (!cls.isArray()) {
            if (cCharAt == '{') {
                try {
                    JSONObject jSONObject = new JSONObject(str);
                    if (Map.class.isAssignableFrom(cls)) {
                        return (T) toMap(jSONObject);
                    }
                    return (T) toPOJO(jSONObject, cls);
                } catch (Exception e2) {
                    throw new RuntimeException(e2);
                }
            }
            if (cls.isAssignableFrom(String.class)) {
                return str;
            }
            return null;
        }
        Class<?> componentType = cls.getComponentType();
        if (cCharAt == '[') {
            try {
                return (T) toPOJOArray(new JSONArray(str), componentType);
            } catch (Exception e3) {
                throw new RuntimeException(e3);
            }
        }
        if (String.class.equals(componentType)) {
            return (T) str.split(",");
        }
        if (Character.TYPE.equals(componentType)) {
            return (T) str.toCharArray();
        }
        if (Character.class.equals(componentType)) {
            char[] charArray = str.toCharArray();
            ?? r5 = (T) new Character[charArray.length];
            while (i < r5.length) {
                r5[i] = Character.valueOf(charArray[i]);
                i++;
            }
            return r5;
        }
        if (Byte.TYPE.equals(componentType)) {
            return (T) Base64.decode(str, 0);
        }
        if (!Byte.class.equals(componentType)) {
            return null;
        }
        byte[] bArrDecode = Base64.decode(str, 0);
        ?? r52 = (T) new Byte[bArrDecode.length];
        while (i < r52.length) {
            r52[i] = Byte.valueOf(bArrDecode[i]);
            i++;
        }
        return r52;
    }

    public static <T> T toPOJO(JSONObject jSONObject, Class<T> cls) {
        Object pojo;
        if (jSONObject == null || cls == null || cls == Void.TYPE) {
            return null;
        }
        try {
            T tNewInstance = cls.newInstance();
            for (Field field : cls.getFields()) {
                Class<?> type = field.getType();
                String name = field.getName();
                if (jSONObject.has(name)) {
                    if (!type.isPrimitive()) {
                        if (type == String.class) {
                            pojo = jSONObject.getString(name);
                        } else if (type == Boolean.class || type == Integer.class || type == Short.class || type == Long.class || type == Double.class) {
                            pojo = jSONObject.get(name);
                        } else if (type.isArray()) {
                            pojo = toPOJOArray(jSONObject.getJSONArray(name), type.getComponentType());
                        } else if (Map.class.isAssignableFrom(type)) {
                            pojo = toMap(jSONObject.getJSONObject(name));
                        } else {
                            pojo = toPOJO(jSONObject.getJSONObject(name), type);
                        }
                        field.set(tNewInstance, pojo);
                    } else if (type == Boolean.TYPE) {
                        field.setBoolean(tNewInstance, jSONObject.getBoolean(name));
                    } else if (type == Byte.TYPE) {
                        field.setByte(tNewInstance, (byte) jSONObject.getInt(name));
                    } else if (type == Character.TYPE) {
                        String string = jSONObject.getString(name);
                        field.setChar(tNewInstance, (string == null || string.length() == 0) ? (char) 0 : string.charAt(0));
                    } else if (type == Short.TYPE) {
                        field.setShort(tNewInstance, (short) jSONObject.getInt(name));
                    } else if (type == Integer.TYPE) {
                        field.setInt(tNewInstance, jSONObject.getInt(name));
                    } else if (type == Long.TYPE) {
                        field.setLong(tNewInstance, jSONObject.getLong(name));
                    } else if (type == Float.TYPE) {
                        field.setFloat(tNewInstance, (float) jSONObject.getDouble(name));
                    } else if (type == Double.TYPE) {
                        field.setDouble(tNewInstance, jSONObject.getDouble(name));
                    }
                }
            }
            return tNewInstance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T[] toPOJOArray(JSONArray jSONArray, Class<T> cls) {
        Object pojo;
        if (jSONArray == null || cls == null || cls == Void.TYPE) {
            return null;
        }
        Object objNewInstance = Array.newInstance((Class<?>) cls, jSONArray.length());
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                if (!cls.isPrimitive()) {
                    if (cls == String.class) {
                        pojo = jSONArray.getString(i);
                    } else if (cls == Boolean.class || cls == Integer.class || cls == Short.class || cls == Long.class || cls == Double.class) {
                        pojo = jSONArray.get(i);
                    } else if (cls.isArray()) {
                        pojo = toPOJOArray(jSONArray.getJSONArray(i), cls.getComponentType());
                    } else if (Map.class.isAssignableFrom(cls)) {
                        pojo = toMap(jSONArray.getJSONObject(i));
                    } else {
                        pojo = toPOJO(jSONArray.getJSONObject(i), cls);
                    }
                    Array.set(objNewInstance, i, pojo);
                } else if (cls == Boolean.TYPE) {
                    Array.setBoolean(objNewInstance, i, jSONArray.getBoolean(i));
                } else if (cls == Byte.TYPE) {
                    Array.setByte(objNewInstance, i, (byte) jSONArray.getInt(i));
                } else if (cls == Character.TYPE) {
                    String string = jSONArray.getString(i);
                    Array.setChar(objNewInstance, i, (string == null || string.length() == 0) ? (char) 0 : string.charAt(0));
                } else if (cls == Short.TYPE) {
                    Array.setShort(objNewInstance, i, (short) jSONArray.getInt(i));
                } else if (cls == Integer.TYPE) {
                    Array.setInt(objNewInstance, i, jSONArray.getInt(i));
                } else if (cls == Long.TYPE) {
                    Array.setLong(objNewInstance, i, jSONArray.getLong(i));
                } else if (cls == Float.TYPE) {
                    Array.setFloat(objNewInstance, i, (float) jSONArray.getDouble(i));
                } else if (cls == Double.TYPE) {
                    Array.setDouble(objNewInstance, i, jSONArray.getDouble(i));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return (T[]) ((Object[]) objNewInstance);
    }
}
