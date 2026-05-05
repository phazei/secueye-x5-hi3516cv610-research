package bean;

import com.alibaba.fastjson.JSON;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class FoundDevice {
    public String deviceName;
    public String productKey;
    public String token;

    public String getToken() {
        return this.token;
    }

    public void setToken(String str) {
        this.token = str;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String str) {
        this.deviceName = str;
    }

    public String getProductKey() {
        return this.productKey;
    }

    public void setProductKey(String str) {
        this.productKey = str;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FoundDevice)) {
            return false;
        }
        FoundDevice foundDevice = (FoundDevice) obj;
        return Objects.equals(this.deviceName, foundDevice.deviceName) && Objects.equals(this.productKey, foundDevice.productKey);
    }

    public String toString() {
        return JSON.toJSONString(this);
    }
}
