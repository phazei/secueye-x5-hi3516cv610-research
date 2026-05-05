package datasource.bean;

import java.io.Serializable;

/* JADX INFO: loaded from: classes3.dex */
public class DeviceItem implements Serializable {
    public String deviceId;
    public String macAddr;
    public String productId;
    public String title;

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getMacAddr() {
        return this.macAddr;
    }

    public String getProductId() {
        return this.productId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDeviceId(String str) {
        this.deviceId = str;
    }

    public void setMacAddr(String str) {
        this.macAddr = str;
    }

    public void setProductId(String str) {
        this.productId = str;
    }

    public void setTitle(String str) {
        this.title = str;
    }
}
