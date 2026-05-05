package datasource.bean;

/* JADX INFO: loaded from: classes3.dex */
public class IotDevice {
    public String devId;
    public String extensions;
    public String mac;
    public String platform;
    public String productKey;
    public String source;
    public int unicastAddress;
    public String userId;
    public String uuid;

    public String getDevId() {
        return this.devId;
    }

    public String getExtensions() {
        return this.extensions;
    }

    public String getMac() {
        return this.mac;
    }

    public String getPlatform() {
        return this.platform;
    }

    public String getProductKey() {
        return this.productKey;
    }

    public String getSource() {
        return this.source;
    }

    public int getUnicastAddress() {
        return this.unicastAddress;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setDevId(String str) {
        this.devId = str;
    }

    public void setExtensions(String str) {
        this.extensions = str;
    }

    public void setMac(String str) {
        this.mac = str;
    }

    public void setPlatform(String str) {
        this.platform = str;
    }

    public void setProductKey(String str) {
        this.productKey = str;
    }

    public void setSource(String str) {
        this.source = str;
    }

    public void setUnicastAddress(int i) {
        this.unicastAddress = i;
    }

    public void setUserId(String str) {
        this.userId = str;
    }

    public void setUuid(String str) {
        this.uuid = str;
    }
}
