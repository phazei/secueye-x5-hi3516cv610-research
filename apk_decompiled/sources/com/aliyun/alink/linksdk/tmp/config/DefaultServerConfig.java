package com.aliyun.alink.linksdk.tmp.config;

import com.aliyun.alink.linksdk.tmp.config.DeviceConfig;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class DefaultServerConfig extends DeviceConfig {
    public String mBkList;
    private ConnectType mConnectType = ConnectType.COAP;
    public String mIotDeviceName;
    public String mIotProductKey;
    public String mIotSecret;
    private String mPrefix;
    private Map<String, ValueWrapper> mPropertValues;
    private String mSecret;

    public DefaultServerConfig() {
        this.mDeviceType = DeviceConfig.DeviceType.SERVER;
    }

    public void setConnectType(ConnectType connectType) {
        this.mConnectType = connectType;
    }

    public ConnectType getConnectType() {
        return this.mConnectType;
    }

    public Map<String, ValueWrapper> getPropertValues() {
        return this.mPropertValues;
    }

    public void setPropertValues(Map<String, ValueWrapper> map) {
        this.mPropertValues = map;
    }

    public String getPrefix() {
        return this.mPrefix;
    }

    public void setPrefix(String str) {
        this.mPrefix = str;
    }

    public String getSecret() {
        return this.mSecret;
    }

    public void setSecret(String str) {
        this.mSecret = str;
    }

    public enum ConnectType {
        COAP,
        MQTT,
        COAP_AND_MQTT;

        public static boolean isConnectContainCoap(ConnectType connectType) {
            return COAP == connectType || COAP_AND_MQTT == connectType;
        }

        public static boolean isConnectContainMqtt(ConnectType connectType) {
            return MQTT == connectType || COAP_AND_MQTT == connectType;
        }
    }
}
