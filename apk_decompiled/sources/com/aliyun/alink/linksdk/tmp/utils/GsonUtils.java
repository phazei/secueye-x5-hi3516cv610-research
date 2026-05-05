package com.aliyun.alink.linksdk.tmp.utils;

import com.aliyun.alink.linksdk.tmp.device.payload.CommonRequestPayload;
import com.aliyun.alink.linksdk.tmp.device.payload.KeyValuePair;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.device.payload.discovery.DiscoveryResponsePayload;
import com.aliyun.alink.linksdk.tmp.devicemodel.DataType;
import com.aliyun.alink.linksdk.tmp.devicemodel.Service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes2.dex */
public class GsonUtils {
    protected static final String TAG = "[Tmp]GsonUtils";
    protected static Gson mGson;

    protected static synchronized Gson getGson() {
        if (mGson != null) {
            return mGson;
        }
        mGson = new GsonBuilder().setLenient().disableHtmlEscaping().registerTypeAdapter(DataType.class, new DataType.DataTypeJsonSerializer()).registerTypeAdapter(DataType.class, new DataType.DataTypeJsonDeSerializer()).registerTypeAdapter(KeyValuePair.class, new KeyValuePair.KeyValuePairJsonDeSerializer()).registerTypeAdapter(KeyValuePair.class, new KeyValuePair.KeyValuePairJsonSerializer()).registerTypeAdapter(ValueWrapper.class, new ValueWrapper.ValueWrapperJsonSerializer()).registerTypeAdapter(ValueWrapper.class, new ValueWrapper.ValueWrapperJsonDeSerializer()).registerTypeAdapter(DiscoveryResponsePayload.DiscoveryResponseData.class, new DiscoveryResponsePayload.DiscoveryResponseDataDeserializer()).registerTypeAdapter(CommonRequestPayload.class, new CommonRequestPayload.CommonRequestPayloadJsonDeSerializer()).registerTypeAdapter(Service.class, new Service.ServiceJsonDeSerializer()).create();
        return mGson;
    }

    public static <T> T fromJson(String str, Gson gson, Type type) {
        try {
            return (T) gson.fromJson(str, type);
        } catch (Throwable th) {
            th.printStackTrace();
            LogCat.e(TAG, "fromJson :" + th.toString());
            return null;
        }
    }

    public static <T> String toJson(T t, Gson gson) {
        if (gson == null) {
            gson = getGson();
        }
        try {
            return gson.toJson(t, new TypeToken<T>() { // from class: com.aliyun.alink.linksdk.tmp.utils.GsonUtils.1
            }.getType());
        } catch (Throwable th) {
            th.printStackTrace();
            LogCat.e(TAG, "toJson :" + th.toString());
            return null;
        }
    }

    public static <T> T fromJson(String str, Type type) {
        return (T) fromJson(str, getGson(), type);
    }

    public static <T> String toJson(T t) {
        try {
            return toJson(t, getGson());
        } catch (Throwable unused) {
            return null;
        }
    }
}
