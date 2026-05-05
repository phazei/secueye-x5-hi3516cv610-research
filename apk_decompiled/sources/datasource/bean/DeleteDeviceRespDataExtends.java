package datasource.bean;

import java.io.Serializable;

/* JADX INFO: loaded from: classes3.dex */
public class DeleteDeviceRespDataExtends implements Serializable {
    public String deviceKey;
    public int netKeyIndex;
    public int primaryUnicastAddress;

    public String getDeviceKey() {
        return this.deviceKey;
    }

    public int getNetKeyIndex() {
        return this.netKeyIndex;
    }

    public int getPrimaryUnicastAddress() {
        return this.primaryUnicastAddress;
    }

    public void setDeviceKey(String str) {
        this.deviceKey = str;
    }

    public void setNetKeyIndex(int i) {
        this.netKeyIndex = i;
    }

    public void setPrimaryUnicastAddress(int i) {
        this.primaryUnicastAddress = i;
    }
}
