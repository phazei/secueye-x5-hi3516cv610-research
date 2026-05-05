package com.aliyun.alink.linksdk.tmp.device.payload;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes2.dex */
public class ArgPair<T> {
    protected String arg;
    protected T value;

    public String getArg() {
        return this.arg;
    }

    public void setArg(String str) {
        this.arg = str;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T t) {
        this.value = t;
    }

    public static class ArgPairJsonDeSerializer implements JsonDeserializer<ArgPair> {
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.JsonDeserializer
        public ArgPair deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (jsonElement == null && !jsonElement.isJsonObject()) {
                return null;
            }
            ArgPair argPair = new ArgPair();
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            JsonElement jsonElement2 = asJsonObject.get("arg");
            if (jsonElement2 != null && jsonElement2.isJsonPrimitive() && ((JsonPrimitive) jsonElement2).isString()) {
                argPair.setArg(jsonElement2.getAsString());
            }
            JsonElement jsonElement3 = asJsonObject.get("value");
            if (jsonElement3 != null && jsonElement3.isJsonPrimitive()) {
                JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonElement3;
                if (jsonPrimitive.isNumber()) {
                    argPair.setValue(Integer.valueOf(jsonPrimitive.getAsInt()));
                } else if (jsonPrimitive.isBoolean()) {
                    argPair.setValue(Boolean.valueOf(jsonPrimitive.getAsBoolean()));
                } else if (jsonPrimitive.isString()) {
                    argPair.setValue(jsonPrimitive.getAsString());
                }
            }
            return argPair;
        }
    }
}
