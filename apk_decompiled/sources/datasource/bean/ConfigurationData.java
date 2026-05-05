package datasource.bean;

import datasource.bean.Sigmesh;

/* JADX INFO: loaded from: classes3.dex */
public class ConfigurationData {
    public Sigmesh.Action action;
    public ConfigResultMap configResultMap;
    public Sigmesh.Device device;
    public String deviceKey;
    public Object primaryUnicastAddress;
    public String serverConfirmation = null;

    public Sigmesh.Action getAction() {
        return this.action;
    }

    public ConfigResultMap getConfigResultMap() {
        return this.configResultMap;
    }

    public Sigmesh.Device getDevice() {
        return this.device;
    }

    public String getDeviceKey() {
        return this.deviceKey;
    }

    public Object getPrimaryUnicastAddress() {
        return this.primaryUnicastAddress;
    }

    public String getServerConfirmation() {
        return this.serverConfirmation;
    }

    public void setAction(Sigmesh.Action action) {
        this.action = action;
    }

    public void setConfigResultMap(ConfigResultMap configResultMap) {
        this.configResultMap = configResultMap;
    }

    public void setDevice(Sigmesh.Device device) {
        this.device = device;
    }

    public void setDeviceKey(String str) {
        this.deviceKey = str;
    }

    public void setPrimaryUnicastAddress(Object obj) {
        this.primaryUnicastAddress = obj;
    }

    public void setServerConfirmation(String str) {
        this.serverConfirmation = str;
    }
}
