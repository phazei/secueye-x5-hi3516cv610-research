package com.aliyun.alink.linksdk.tmp.devicemodel;

import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class Service<T> {
    private static final String TAG = "[Tmp]Service";
    private String callType;
    private String desc;
    private String identifier;
    private List<T> inputData;
    private String method;
    private String name;
    private List<Arg> outputData;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String str) {
        this.desc = str;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String str) {
        this.identifier = str;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String str) {
        this.method = str;
    }

    public List<T> getInputData() {
        return this.inputData;
    }

    public void setInputData(List<T> list) {
        this.inputData = list;
    }

    public List<Arg> getOutputData() {
        return this.outputData;
    }

    public void setOutputData(List<Arg> list) {
        this.outputData = list;
    }

    public String getCallType() {
        return this.callType;
    }

    public void setCallType(String str) {
        this.callType = str;
    }

    public static class ServiceJsonDeSerializer implements JsonDeserializer<Service> {
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.JsonDeserializer
        public Service deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject asJsonObject;
            Service normalService;
            String asString = null;
            if (jsonElement == null || !jsonElement.isJsonObject() || (asJsonObject = jsonElement.getAsJsonObject()) == null) {
                return null;
            }
            JsonElement jsonElement2 = asJsonObject.get("identifier");
            if (jsonElement2 != null && jsonElement2.isJsonPrimitive()) {
                asString = jsonElement2.getAsJsonPrimitive().getAsString();
            }
            JsonElement jsonElement3 = asJsonObject.get(TmpConstant.SERVICE_INPUTDATA);
            if (TmpConstant.PROPERTY_IDENTIFIER_GET.equalsIgnoreCase(asString)) {
                normalService = new GetService();
                if (jsonElement3 != null) {
                    ALog.d(Service.TAG, "GetService inputParamsEle:" + jsonElement3.toString());
                    normalService.setInputData((List) jsonDeserializationContext.deserialize(jsonElement3, new TypeToken<List<String>>() { // from class: com.aliyun.alink.linksdk.tmp.devicemodel.Service.ServiceJsonDeSerializer.1
                    }.getType()));
                }
            } else {
                normalService = new NormalService();
                if (jsonElement3 != null) {
                    normalService.setInputData((List) jsonDeserializationContext.deserialize(jsonElement3, new TypeToken<List<Arg>>() { // from class: com.aliyun.alink.linksdk.tmp.devicemodel.Service.ServiceJsonDeSerializer.2
                    }.getType()));
                }
            }
            normalService.setIdentifier(asString);
            JsonElement jsonElement4 = asJsonObject.get("name");
            if (jsonElement4 != null && jsonElement4.isJsonPrimitive()) {
                normalService.setName(jsonElement4.getAsJsonPrimitive().getAsString());
            }
            JsonElement jsonElement5 = asJsonObject.get("method");
            if (jsonElement5 != null && jsonElement5.isJsonPrimitive()) {
                normalService.setMethod(jsonElement5.getAsJsonPrimitive().getAsString());
            }
            JsonElement jsonElement6 = asJsonObject.get(TmpConstant.SERVICE_DESC);
            if (jsonElement6 != null && jsonElement6.isJsonPrimitive()) {
                normalService.setDesc(jsonElement6.getAsJsonPrimitive().getAsString());
            }
            JsonElement jsonElement7 = asJsonObject.get(TmpConstant.SERVICE_OUTPUTDATA);
            if (jsonElement7 != null) {
                normalService.setOutputData((List) jsonDeserializationContext.deserialize(jsonElement7, new TypeToken<List<Arg>>() { // from class: com.aliyun.alink.linksdk.tmp.devicemodel.Service.ServiceJsonDeSerializer.3
                }.getType()));
            }
            JsonElement jsonElement8 = asJsonObject.get(TmpConstant.SERVICE_CALLTYPE);
            if (jsonElement8 != null) {
                normalService.setCallType(jsonElement8.getAsJsonPrimitive().getAsString());
            }
            return normalService;
        }
    }
}
