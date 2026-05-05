package com.aliyun.alink.linksdk.tmp.devicemodel;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.devicemodel.specs.ArraySpec;
import com.aliyun.alink.linksdk.tmp.devicemodel.specs.EnumSpec;
import com.aliyun.alink.linksdk.tmp.devicemodel.specs.MetaSpec;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class DataType<T> {
    private T specs;
    private String type;

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public Object getSpecs() {
        return this.specs;
    }

    public void setSpecs(T t) {
        this.specs = t;
    }

    public static class DataTypeJsonSerializer implements JsonSerializer<DataType> {
        @Override // com.google.gson.JsonSerializer
        public JsonElement serialize(DataType dataType, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonElement jsonElementSerialize = null;
            if (dataType == null) {
                return null;
            }
            JsonObject jsonObject = new JsonObject();
            if ("string".equalsIgnoreCase(dataType.getType()) || "bool".equalsIgnoreCase(dataType.getType()) || "int".equalsIgnoreCase(dataType.getType()) || "float".equalsIgnoreCase(dataType.getType()) || TmpConstant.TYPE_VALUE_DOUBLE.equalsIgnoreCase(dataType.getType()) || "text".equalsIgnoreCase(dataType.getType()) || "date".equalsIgnoreCase(dataType.getType())) {
                jsonElementSerialize = jsonSerializationContext.serialize(dataType.getSpecs(), MetaSpec.class);
            } else if (TmpConstant.TYPE_VALUE_ARRAY.equalsIgnoreCase(dataType.getType())) {
                jsonElementSerialize = jsonSerializationContext.serialize(dataType.getSpecs(), ArraySpec.class);
            } else if ("enum".equalsIgnoreCase(dataType.getType())) {
                jsonElementSerialize = jsonSerializationContext.serialize(dataType.getSpecs(), EnumSpec.class);
            } else if ("struct".equalsIgnoreCase(dataType.getType())) {
                jsonElementSerialize = jsonSerializationContext.serialize(dataType.getSpecs(), List.class);
            }
            jsonObject.addProperty("type", dataType.getType());
            jsonObject.add("specs", jsonElementSerialize);
            return jsonObject;
        }
    }

    public static class DataTypeJsonDeSerializer implements JsonDeserializer<DataType> {
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.JsonDeserializer
        public DataType deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (jsonElement == null || !jsonElement.isJsonObject()) {
                return null;
            }
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            JsonElement jsonElement2 = asJsonObject.get("type");
            JsonElement jsonElement3 = asJsonObject.get("specs");
            if (jsonElement2 == null) {
                return null;
            }
            String asString = jsonElement2.getAsString();
            if (TextUtils.isEmpty(asString)) {
                return null;
            }
            DataType dataType = new DataType();
            dataType.setType(asString);
            if ("string".equalsIgnoreCase(asString) || "bool".equalsIgnoreCase(asString) || "int".equalsIgnoreCase(asString) || "float".equalsIgnoreCase(asString) || "text".equalsIgnoreCase(asString) || "date".equalsIgnoreCase(asString)) {
                dataType.setSpecs((MetaSpec) jsonDeserializationContext.deserialize(jsonElement3, MetaSpec.class));
            } else if (TmpConstant.TYPE_VALUE_ARRAY.equalsIgnoreCase(asString)) {
                dataType.setSpecs((ArraySpec) jsonDeserializationContext.deserialize(jsonElement3, ArraySpec.class));
            } else if ("enum".equalsIgnoreCase(asString)) {
                dataType.setSpecs((EnumSpec) jsonDeserializationContext.deserialize(jsonElement3, EnumSpec.class));
            } else if ("struct".equalsIgnoreCase(asString)) {
                dataType.setSpecs((List) jsonDeserializationContext.deserialize(jsonElement3, List.class));
            }
            return dataType;
        }
    }
}
