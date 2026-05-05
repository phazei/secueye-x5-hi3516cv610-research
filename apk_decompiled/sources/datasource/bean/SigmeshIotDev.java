package datasource.bean;

import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class SigmeshIotDev {
    public String appKey;
    public int appKeyIndex;
    public String devId;
    public String deviceKey;
    public String deviceVersion;
    public boolean hbConfiged;
    public String iotId;
    public boolean lowPower;
    public String mac;
    public String netKey;
    public int netKeyIndex;
    public String productKey;
    public String secret;
    public List<SigmeshKey> sigmeshKeys = null;
    public boolean support8201;
    public int unicastaddress;

    public String getAppKey() {
        return this.appKey;
    }

    public int getAppKeyIndex() {
        return this.appKeyIndex;
    }

    public String getDevId() {
        return this.devId;
    }

    public String getDeviceKey() {
        return this.deviceKey;
    }

    public String getDeviceVersion() {
        return this.deviceVersion;
    }

    public String getIotId() {
        return this.iotId;
    }

    public String getMac() {
        return this.mac;
    }

    public Object getNetKey() {
        return this.netKey;
    }

    public int getNetKeyIndex() {
        return this.netKeyIndex;
    }

    public String getProductKey() {
        return this.productKey;
    }

    public String getSecret() {
        return this.secret;
    }

    public List<SigmeshKey> getSigmeshKeys() {
        return this.sigmeshKeys;
    }

    public int getUnicastaddress() {
        return this.unicastaddress;
    }

    public boolean isHbConfiged() {
        return this.hbConfiged;
    }

    public boolean isLowPower() {
        return this.lowPower;
    }

    public boolean isSupport8201() {
        return this.support8201;
    }

    public void setAppKey(String str) {
        this.appKey = str;
    }

    public void setAppKeyIndex(int i) {
        this.appKeyIndex = i;
    }

    public void setDevId(String str) {
        this.devId = str;
    }

    public void setDeviceKey(String str) {
        this.deviceKey = str;
    }

    public void setDeviceVersion(String str) {
        this.deviceVersion = str;
    }

    public void setHbConfiged(boolean z) {
        this.hbConfiged = z;
    }

    public void setIotId(String str) {
        this.iotId = str;
    }

    public void setLowPower(boolean z) {
        this.lowPower = z;
    }

    public void setMac(String str) {
        this.mac = str;
    }

    public void setNetKey(String str) {
        this.netKey = str;
    }

    public void setNetKeyIndex(int i) {
        this.netKeyIndex = i;
    }

    public void setProductKey(String str) {
        this.productKey = str;
    }

    public void setSecret(String str) {
        this.secret = str;
    }

    public void setSigmeshKeys(List<SigmeshKey> list) {
        this.sigmeshKeys = list;
    }

    public void setSupport8201(boolean z) {
        this.support8201 = z;
    }

    public void setUnicastaddress(int i) {
        this.unicastaddress = i;
    }
}
