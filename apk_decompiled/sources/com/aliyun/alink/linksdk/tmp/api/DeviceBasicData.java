package com.aliyun.alink.linksdk.tmp.api;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.utils.TextHelper;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class DeviceBasicData implements Cloneable {
    private static final String TAG = "[Tmp]DeviceBasicData";
    public transient String addr;
    public transient String desc;
    public transient String deviceModelJson;
    public String deviceName;
    public Map<String, Object> extraData;
    public transient String iotId;
    public transient boolean isLocal;
    public boolean isPluginFound;
    public transient int localDiscoveryType;
    public transient String mac;
    public String modelType;
    public transient int port;
    public String productKey;
    public transient int supportedNetType;

    public DeviceBasicData() {
        this.isPluginFound = false;
        this.isLocal = false;
    }

    public DeviceBasicData(boolean z) {
        this.isPluginFound = false;
        this.isLocal = z;
    }

    public DeviceBasicData(DeviceBasicData deviceBasicData) {
        this.isPluginFound = false;
        if (deviceBasicData == null) {
            ALog.e(TAG, "DeviceBasicData basicData empty");
            return;
        }
        this.modelType = deviceBasicData.modelType;
        this.productKey = deviceBasicData.getProductKey();
        this.deviceName = deviceBasicData.getDeviceName();
        this.desc = deviceBasicData.getDesc();
        this.addr = deviceBasicData.getAddr();
        this.deviceModelJson = deviceBasicData.getDeviceModelJson();
        this.iotId = deviceBasicData.getIotId();
        this.port = deviceBasicData.getPort();
        this.isLocal = deviceBasicData.isLocal;
        setSupportedNetType(deviceBasicData.getSupportedNetType());
        this.mac = deviceBasicData.mac;
        this.localDiscoveryType = deviceBasicData.localDiscoveryType;
        this.isPluginFound = deviceBasicData.isPluginFound;
        this.extraData = deviceBasicData.extraData;
    }

    @Deprecated
    public String getProdKey() {
        return getProductKey();
    }

    @Deprecated
    public void setProdKey(String str) {
        setProductKey(str);
    }

    public int getSupportedNetType() {
        return this.supportedNetType;
    }

    public void setSupportedNetType(int i) {
        this.supportedNetType = i;
    }

    public boolean isBleMeshDevice() {
        return this.supportedNetType == TmpEnum.DeviceNetType.NET_BLE_MESH.getValue();
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int i) {
        this.port = i;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean isLocal() {
        return this.isLocal;
    }

    public void setLocal(boolean z) {
        this.isLocal = z;
    }

    public String getDeviceModelJson() {
        return this.deviceModelJson;
    }

    public void setDeviceModelJson(String str) {
        this.deviceModelJson = str;
    }

    public String getAddr() {
        return this.addr;
    }

    public void setAddr(String str) {
        this.addr = str;
    }

    public String getModelType() {
        return this.modelType;
    }

    public void setModelType(String str) {
        this.modelType = str;
    }

    public String getProductKey() {
        return this.productKey;
    }

    public void setProductKey(String str) {
        this.productKey = str;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String str) {
        this.deviceName = str;
    }

    public String getDevId() {
        return TextHelper.combineStr(getProductKey(), getDeviceName());
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String str) {
        this.desc = str;
    }

    public String getIotId() {
        return this.iotId;
    }

    public void setIotId(String str) {
        this.iotId = str;
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append(TextUtils.isEmpty(this.productKey) ? " pk null" : this.productKey);
        sb.append(TextUtils.isEmpty(this.deviceName) ? " dn null" : this.deviceName);
        if (TextUtils.isEmpty(this.mac)) {
            str = " mac null";
        } else {
            str = "mac:" + this.mac;
        }
        sb.append(str);
        sb.append("moduleType:" + this.modelType);
        sb.append(" localDiscoveryType: " + this.localDiscoveryType);
        sb.append(" supportednetType: " + this.supportedNetType);
        sb.append(" isPluginFound:" + this.isPluginFound);
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" extraData:");
        Object obj = this.extraData;
        if (obj == null) {
            obj = " null";
        }
        sb2.append(obj);
        sb.append(sb2.toString());
        return sb.toString();
    }
}
