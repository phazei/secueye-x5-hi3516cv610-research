package com.aliyun.alink.apiclient.utils;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class GsonUtils {
    private static Gson mGson = new Gson();

    public static <T> T parseJson(String str, Type type) {
        try {
            return (T) mGson.fromJson(str, type);
        } catch (JsonIOException e) {
            e.printStackTrace();
            return null;
        } catch (JsonSyntaxException e2) {
            e2.printStackTrace();
            return null;
        } catch (Exception e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public static String toJsonString(Map map) {
        return mGson.toJson(map);
    }
}
