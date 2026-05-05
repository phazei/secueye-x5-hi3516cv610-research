package com.aliyun.alink.linksdk.tmp.device.payload.discovery;

import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.device.payload.CommonResponsePayload;
import com.aliyun.alink.linksdk.tmp.devicemodel.Profile;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class DiscoveryResponsePayload extends CommonResponsePayload<DiscoveryResponseData> {
    protected static final String TAG = "[Tmp]DiscoveryResponsePayload";

    public static class MulDevicesData {
        public String addr;
        public int port;
        public List<Profile> profile;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public DeviceBasicData getDeviceInfo() {
        if (this.data == 0) {
            return null;
        }
        return ((DiscoveryResponseData) this.data).getDeviceBasicData();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public String getDeviceModel() {
        return ((DiscoveryResponseData) this.data).getDeviceModel();
    }

    public static class DiscoveryResponseData {
        protected DeviceBasicData deviceBasicData;
        protected String deviceModel;
        public MulDevicesData devices;

        public String getDeviceModel() {
            return this.deviceModel;
        }

        public void setDeviceModel(String str) {
            this.deviceModel = str;
        }

        public DeviceBasicData getDeviceBasicData() {
            return this.deviceBasicData;
        }

        public void setDeviceBasicData(DeviceBasicData deviceBasicData) {
            this.deviceBasicData = deviceBasicData;
        }
    }

    public static class DiscoveryResponseDataDeserializer implements JsonDeserializer<DiscoveryResponseData> {
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.JsonDeserializer
        public DiscoveryResponseData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (jsonElement == null || !jsonElement.isJsonObject()) {
                return null;
            }
            DiscoveryResponseData discoveryResponseData = new DiscoveryResponseData();
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            JsonElement jsonElement2 = asJsonObject.get("deviceModel");
            if (jsonElement2 != null && jsonElement2.isJsonObject()) {
                JsonObject asJsonObject2 = jsonElement2.getAsJsonObject();
                JsonElement jsonElement3 = asJsonObject2.get("profile");
                Profile profile = jsonElement3 != null ? (Profile) jsonDeserializationContext.deserialize(jsonElement3, Profile.class) : null;
                if (profile != null) {
                    DeviceBasicData deviceBasicData = new DeviceBasicData();
                    deviceBasicData.setProductKey(profile.getProdKey());
                    deviceBasicData.setDeviceName(profile.getName());
                    deviceBasicData.setAddr(profile.addr);
                    deviceBasicData.setPort(profile.port);
                    deviceBasicData.setLocal(true);
                    discoveryResponseData.setDeviceBasicData(deviceBasicData);
                }
                JsonElement jsonElement4 = asJsonObject2.get(TmpConstant.DEVICE_MODEL_PROPERTIES);
                JsonElement jsonElement5 = asJsonObject2.get(TmpConstant.DEVICE_MODEL_SERVICES);
                JsonElement jsonElement6 = asJsonObject2.get(TmpConstant.DEVICE_MODEL_EVENTS);
                if ((jsonElement4 == null || !jsonElement4.isJsonArray()) && ((jsonElement5 == null || !jsonElement5.isJsonArray()) && (jsonElement6 == null || !jsonElement6.isJsonArray()))) {
                    ALog.e(DiscoveryResponsePayload.TAG, "parse discovery model empty");
                } else {
                    discoveryResponseData.setDeviceModel(jsonElement2.toString());
                }
            }
            JsonElement jsonElement7 = asJsonObject.get(TmpConstant.DEVICES);
            if (jsonElement7 != null) {
                discoveryResponseData.devices = (MulDevicesData) jsonDeserializationContext.deserialize(jsonElement7, MulDevicesData.class);
            }
            return discoveryResponseData;
        }
    }
}
