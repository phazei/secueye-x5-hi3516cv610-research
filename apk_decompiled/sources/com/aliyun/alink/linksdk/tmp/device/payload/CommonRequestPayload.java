package com.aliyun.alink.linksdk.tmp.device.payload;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class CommonRequestPayload<T> {
    protected String id;
    protected String method;
    protected T params;
    protected String sessionKey;
    protected String version;

    public CommonRequestPayload(String str, String str2) {
        this();
    }

    public CommonRequestPayload() {
        this.version = "1.0";
        this.id = String.valueOf(IdCreater.getInstance().getNextId());
    }

    public T getParams() {
        return this.params;
    }

    public void setParams(T t) {
        this.params = t;
    }

    public String getSessionKey() {
        return this.sessionKey;
    }

    public void setSessionKey(String str) {
        this.sessionKey = str;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String str) {
        this.version = str;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String str) {
        this.method = str;
    }

    public static class CommonRequestPayloadJsonDeSerializer implements JsonDeserializer<CommonRequestPayload> {
        /* JADX WARN: Can't rename method to resolve collision */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.gson.JsonDeserializer
        public CommonRequestPayload deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject asJsonObject;
            Object objDeserialize = null;
            if (jsonElement == null || !jsonElement.isJsonObject() || (asJsonObject = jsonElement.getAsJsonObject()) == null) {
                return null;
            }
            CommonRequestPayload commonRequestPayload = new CommonRequestPayload();
            JsonElement jsonElement2 = asJsonObject.get("params");
            if (jsonElement2 != null) {
                if (jsonElement2.isJsonArray()) {
                    objDeserialize = jsonDeserializationContext.deserialize(jsonElement2, new TypeToken<List<String>>() { // from class: com.aliyun.alink.linksdk.tmp.device.payload.CommonRequestPayload.CommonRequestPayloadJsonDeSerializer.1
                    }.getType());
                } else if (jsonElement2.isJsonObject()) {
                    objDeserialize = jsonDeserializationContext.deserialize(jsonElement2, new TypeToken<Map<String, ValueWrapper>>() { // from class: com.aliyun.alink.linksdk.tmp.device.payload.CommonRequestPayload.CommonRequestPayloadJsonDeSerializer.2
                    }.getType());
                } else {
                    objDeserialize = jsonDeserializationContext.deserialize(jsonElement2, new TypeToken<Object>() { // from class: com.aliyun.alink.linksdk.tmp.device.payload.CommonRequestPayload.CommonRequestPayloadJsonDeSerializer.3
                    }.getType());
                }
            }
            commonRequestPayload.setParams(objDeserialize);
            JsonElement jsonElement3 = asJsonObject.get("id");
            if (jsonElement3 != null && jsonElement3.isJsonPrimitive()) {
                commonRequestPayload.setId(jsonElement3.getAsJsonPrimitive().getAsString());
            }
            JsonElement jsonElement4 = asJsonObject.get("version");
            if (jsonElement4 != null && jsonElement4.isJsonPrimitive()) {
                commonRequestPayload.setVersion(jsonElement4.getAsJsonPrimitive().getAsString());
            }
            JsonElement jsonElement5 = asJsonObject.get("method");
            if (jsonElement5 != null && jsonElement5.isJsonPrimitive()) {
                commonRequestPayload.setMethod(jsonElement5.getAsJsonPrimitive().getAsString());
            }
            return commonRequestPayload;
        }
    }
}
