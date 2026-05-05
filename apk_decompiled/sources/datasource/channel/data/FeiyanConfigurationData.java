package datasource.channel.data;

import com.aliyun.alink.linksdk.connectsdk.BaseApiResponse;
import datasource.bean.ConfigResultMap;
import datasource.bean.ConfigurationData;

/* JADX INFO: loaded from: classes3.dex */
public class FeiyanConfigurationData extends BaseApiResponse {
    public ConfigResultMap configResultMap;
    public String deviceKey;
    public Object primaryUnicastAddress;

    public ConfigurationData convert2ConfigurationData() {
        ConfigurationData configurationData = new ConfigurationData();
        configurationData.setConfigResultMap(getConfigResultMap());
        configurationData.setDeviceKey(getDeviceKey());
        configurationData.setPrimaryUnicastAddress(getPrimaryUnicastAddress());
        return configurationData;
    }

    public ConfigResultMap getConfigResultMap() {
        return this.configResultMap;
    }

    public String getDeviceKey() {
        return this.deviceKey;
    }

    public Object getPrimaryUnicastAddress() {
        return this.primaryUnicastAddress;
    }

    public void setConfigResultMap(ConfigResultMap configResultMap) {
        this.configResultMap = configResultMap;
    }

    public void setDeviceKey(String str) {
        this.deviceKey = str;
    }

    public void setPrimaryUnicastAddress(Object obj) {
        this.primaryUnicastAddress = obj;
    }
}
