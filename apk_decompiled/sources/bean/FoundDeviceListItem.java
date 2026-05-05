package bean;

import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.discovery.DiscoveryType;

/* JADX INFO: loaded from: classes.dex */
public class FoundDeviceListItem extends FoundDevice {
    public static final String NEED_BIND = "need_bind";
    public static final String NEED_CONNECT = "need_connect";
    public DeviceInfo deviceInfo;
    public String deviceStatus;
    public DiscoveryType discoveryType;

    public DeviceInfo getDeviceInfo() {
        return this.deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getDeviceStatus() {
        return this.deviceStatus;
    }

    public void setDeviceStatus(String str) {
        this.deviceStatus = str;
    }

    public DiscoveryType getDiscoveryType() {
        return this.discoveryType;
    }

    public void setDiscoveryType(DiscoveryType discoveryType) {
        this.discoveryType = discoveryType;
    }
}
