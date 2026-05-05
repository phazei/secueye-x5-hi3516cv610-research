package bean;

import com.alibaba.fastjson.JSON;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class BleFoundDevice implements Cloneable {
    public String addr;
    public String desc;
    public String deviceModelJson;
    public String deviceName;
    public String iotId;
    public String modelType;
    public int port;
    public String productKey;

    public String getModelType() {
        return this.modelType;
    }

    public void setModelType(String str) {
        this.modelType = str;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String str) {
        this.desc = str;
    }

    public String getAddr() {
        return this.addr;
    }

    public void setAddr(String str) {
        this.addr = str;
    }

    public String getDeviceModelJson() {
        return this.deviceModelJson;
    }

    public void setDeviceModelJson(String str) {
        this.deviceModelJson = str;
    }

    public String getIotId() {
        return this.iotId;
    }

    public void setIotId(String str) {
        this.iotId = str;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int i) {
        this.port = i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BleFoundDevice)) {
            return false;
        }
        BleFoundDevice bleFoundDevice = (BleFoundDevice) obj;
        return Objects.equals(this.deviceName, bleFoundDevice.deviceName) && Objects.equals(this.productKey, bleFoundDevice.productKey);
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public BleFoundDevice m8clone() {
        try {
            return (BleFoundDevice) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return this;
        }
    }

    public String toString() {
        return JSON.toJSONString(this);
    }

    public void copyDeviceBasicData(DeviceBasicData deviceBasicData) {
        this.productKey = deviceBasicData.getProductKey();
        this.deviceName = deviceBasicData.getDeviceName();
        this.modelType = deviceBasicData.getModelType();
        this.desc = deviceBasicData.getDesc();
        this.addr = deviceBasicData.getAddr();
        this.deviceModelJson = deviceBasicData.getDeviceModelJson();
        this.iotId = deviceBasicData.getIotId();
        this.port = deviceBasicData.getPort();
    }
}
