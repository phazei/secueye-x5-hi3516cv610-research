package com.aliyun.alink.business.devicecenter.api.add;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkHelper;
import com.aliyun.alink.business.devicecenter.base.LocalDevice;
import com.aliyun.alink.business.devicecenter.cache.CacheCenter;
import com.aliyun.alink.business.devicecenter.cache.CacheType;
import com.aliyun.alink.business.devicecenter.config.model.DCAlibabaConfigParams;
import com.aliyun.alink.business.devicecenter.discover.CloudEnrolleeDeviceModel;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.google.firebase.analytics.FirebaseAnalytics;
import datasource.bean.ConfigurationData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class DeviceInfo extends LKDeviceInfo {
    public static final String TAG = "DeviceInfo";
    public String authDevice;
    public String brand;
    public boolean comboMeshFlag;
    public ConfigurationData configurationInfo;
    public String confirmCloud;
    public String discoveredSource;
    public String flowId;
    public String icon;
    public String platform;
    public String random;
    public int rssi;
    public String subDeviceId;
    public long time;
    public String timeTmp;
    public String userId;
    public String id = null;
    public String linkType = "ForceAliLinkTypeNone";
    public String linkUserType = FirebaseAnalytics.Event.LOGIN;
    public String accessKeyId = null;
    public String accessKeySecret = null;
    public String encodeType = null;
    public String encodeKey = null;
    public String extUserId = null;
    public String productEncryptKey = null;
    public String securityRandom = null;
    public String token = null;
    public String devType = null;

    @Deprecated
    public AwssVersion awssVer = null;
    public String mac = null;
    public boolean isInSide = false;
    public String remainTime = null;
    public String protocolVersion = "1.0";
    public String regProductKey = null;
    public String regDeviceName = null;

    @Deprecated
    public String addDeviceFrom = null;
    public String service = null;
    public String fwVersion = null;
    public Object tag = null;
    public DeviceBindResultInfo bindResultInfo = null;
    public RegionInfo regionInfo = null;
    public String iotId = null;
    public String deviceId = null;
    public String productName = null;
    public String image = null;
    public String netType = null;
    public String nodeType = null;
    public String categoryKey = null;
    public String categoryName = null;
    public String categoryId = null;
    public String regIotId = null;
    public Map extraDeviceInfo = null;
    public int genieProvisionModel = 7;
    public boolean authFlag = false;
    public String familyId = null;

    public static DeviceInfo convertLocalDevice(LocalDevice localDevice) {
        if (localDevice == null) {
            return null;
        }
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.deviceName = localDevice.deviceName;
        deviceInfo.productKey = localDevice.productKey;
        deviceInfo.devType = localDevice.type;
        deviceInfo.token = localDevice.token;
        deviceInfo.mac = localDevice.mac;
        deviceInfo.id = AlinkHelper.getHalfMacFromMac(deviceInfo.mac);
        return deviceInfo;
    }

    public DeviceInfo copy() {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.productKey = this.productKey;
        deviceInfo.deviceName = this.deviceName;
        deviceInfo.productId = this.productId;
        deviceInfo.id = this.id;
        deviceInfo.linkType = this.linkType;
        deviceInfo.productEncryptKey = this.productEncryptKey;
        deviceInfo.securityRandom = this.securityRandom;
        deviceInfo.token = this.token;
        deviceInfo.devType = this.devType;
        deviceInfo.mac = this.mac;
        deviceInfo.remainTime = this.remainTime;
        deviceInfo.protocolVersion = this.protocolVersion;
        deviceInfo.regProductKey = this.regProductKey;
        deviceInfo.regDeviceName = this.regDeviceName;
        deviceInfo.addDeviceFrom = this.addDeviceFrom;
        deviceInfo.tag = this.tag;
        deviceInfo.regionInfo = this.regionInfo;
        deviceInfo.mac = this.mac;
        deviceInfo.devType = this.devType;
        deviceInfo.bindResultInfo = this.bindResultInfo;
        deviceInfo.extraDeviceInfo = this.extraDeviceInfo;
        deviceInfo.iotId = this.iotId;
        deviceInfo.regIotId = this.regIotId;
        deviceInfo.deviceId = this.deviceId;
        deviceInfo.subDeviceId = this.subDeviceId;
        deviceInfo.genieProvisionModel = this.genieProvisionModel;
        deviceInfo.userId = this.userId;
        deviceInfo.flowId = this.flowId;
        deviceInfo.platform = this.platform;
        deviceInfo.icon = this.icon;
        deviceInfo.timeTmp = this.timeTmp;
        deviceInfo.time = this.time;
        deviceInfo.brand = this.brand;
        deviceInfo.productName = this.productName;
        deviceInfo.image = this.image;
        deviceInfo.netType = this.netType;
        deviceInfo.nodeType = this.nodeType;
        deviceInfo.categoryKey = this.categoryKey;
        deviceInfo.categoryName = this.categoryName;
        deviceInfo.categoryId = this.categoryId;
        deviceInfo.linkUserType = this.linkUserType;
        deviceInfo.extUserId = this.extUserId;
        deviceInfo.accessKeyId = this.accessKeyId;
        deviceInfo.accessKeySecret = this.accessKeySecret;
        deviceInfo.encodeType = this.encodeType;
        deviceInfo.encodeKey = this.encodeKey;
        deviceInfo.authFlag = this.authFlag;
        deviceInfo.authDevice = this.authDevice;
        deviceInfo.random = this.random;
        deviceInfo.confirmCloud = this.confirmCloud;
        deviceInfo.rssi = this.rssi;
        deviceInfo.configurationInfo = this.configurationInfo;
        deviceInfo.comboMeshFlag = this.comboMeshFlag;
        deviceInfo.familyId = this.familyId;
        return deviceInfo;
    }

    public boolean equals(Object obj) {
        String str;
        String str2;
        if (!(obj instanceof DeviceInfo)) {
            return false;
        }
        DeviceInfo deviceInfo = (DeviceInfo) obj;
        if (isSameApWithNoPk(deviceInfo)) {
            return true;
        }
        if (!isValid()) {
            return false;
        }
        if (TextUtils.isEmpty(this.productKey) && TextUtils.isEmpty(this.productId)) {
            return false;
        }
        if (!TextUtils.isEmpty(this.productKey) && this.productKey.equals(deviceInfo.productKey) && !TextUtils.isEmpty(this.deviceName) && this.deviceName.equals(deviceInfo.deviceName)) {
            return true;
        }
        if (!TextUtils.isEmpty(this.productKey) && this.productKey.equals(deviceInfo.productKey) && !TextUtils.isEmpty(this.id) && this.id.equals(deviceInfo.id)) {
            return true;
        }
        if (!TextUtils.isEmpty(this.mac) && this.mac.equals(deviceInfo.mac)) {
            return true;
        }
        if (!TextUtils.isEmpty(this.productId) && this.productId.equals(deviceInfo.productId) && !TextUtils.isEmpty(this.id) && this.id.equals(deviceInfo.id)) {
            return true;
        }
        if (!TextUtils.isEmpty(this.productKey) && this.productKey.equals(deviceInfo.productKey) && TextUtils.isEmpty(this.deviceName) && !TextUtils.isEmpty(deviceInfo.deviceName) && !TextUtils.isEmpty(this.id) && (str2 = deviceInfo.mac) != null && str2.length() > 6 && this.id.equals(AlinkHelper.getHalfMacFromMac(deviceInfo.mac))) {
            ALog.d(TAG, "enrollee equal to provisioned.");
            return true;
        }
        if (TextUtils.isEmpty(this.productKey) || !this.productKey.equals(deviceInfo.productKey) || TextUtils.isEmpty(this.deviceName) || !TextUtils.isEmpty(deviceInfo.deviceName) || TextUtils.isEmpty(deviceInfo.id) || (str = this.mac) == null || str.length() <= 6 || !deviceInfo.id.equals(AlinkHelper.getHalfMacFromMac(this.mac))) {
            return false;
        }
        ALog.d(TAG, "provisioned equal to enrollee.");
        return true;
    }

    public DCAlibabaConfigParams getDCConfigParams() {
        List cachedModel;
        DCAlibabaConfigParams dCAlibabaConfigParams = new DCAlibabaConfigParams();
        String str = this.protocolVersion;
        dCAlibabaConfigParams.protocolVersion = str;
        dCAlibabaConfigParams.productKey = this.productKey;
        dCAlibabaConfigParams.deviceName = this.deviceName;
        dCAlibabaConfigParams.productId = this.productId;
        dCAlibabaConfigParams.id = this.id;
        dCAlibabaConfigParams.productEncryptKey = this.productEncryptKey;
        dCAlibabaConfigParams.securityRandom = this.securityRandom;
        dCAlibabaConfigParams.regionInfo = this.regionInfo;
        dCAlibabaConfigParams.protocolVersion = str;
        dCAlibabaConfigParams.devType = this.devType;
        dCAlibabaConfigParams.mac = this.mac;
        dCAlibabaConfigParams.isInSide = this.isInSide;
        dCAlibabaConfigParams.regIoTId = this.regIotId;
        dCAlibabaConfigParams.iotId = this.iotId;
        dCAlibabaConfigParams.regDeviceName = this.regDeviceName;
        dCAlibabaConfigParams.regProductKey = this.regProductKey;
        Map map = this.extraDeviceInfo;
        if (map != null && map.containsKey(AlinkConstants.KEY_APP_SSID) && (getExtraDeviceInfo(AlinkConstants.KEY_APP_SSID) instanceof String)) {
            dCAlibabaConfigParams.deviceApSsid = (String) getExtraDeviceInfo(AlinkConstants.KEY_APP_SSID);
        }
        if (TextUtils.isEmpty(this.linkType)) {
            if (AlinkConstants.ADD_DEVICE_FROM_ZERO.equals(this.addDeviceFrom)) {
                this.linkType = LinkType.ALI_ZERO_AP.getName();
            } else {
                this.linkType = LinkType.ALI_BROADCAST.getName();
            }
        }
        dCAlibabaConfigParams.linkType = LinkType.getLinkType(this.linkType);
        if (LinkType.ALI_ZERO_AP.equals(dCAlibabaConfigParams.linkType) && ((TextUtils.isEmpty(this.regDeviceName) || TextUtils.isEmpty(this.regProductKey)) && (cachedModel = CacheCenter.getInstance().getCachedModel(CacheType.CLOUD_ENROLLEE, this.productKey, this.deviceName, null, null)) != null && cachedModel.size() > 0 && (cachedModel.get(0) instanceof CloudEnrolleeDeviceModel))) {
            CloudEnrolleeDeviceModel cloudEnrolleeDeviceModel = (CloudEnrolleeDeviceModel) cachedModel.get(0);
            ALog.d(TAG, "find cached model=" + cloudEnrolleeDeviceModel);
            if (cloudEnrolleeDeviceModel != null && "1".equals(cloudEnrolleeDeviceModel.type)) {
                dCAlibabaConfigParams.regDeviceName = cloudEnrolleeDeviceModel.regDeviceName;
                dCAlibabaConfigParams.regProductKey = cloudEnrolleeDeviceModel.regProductKey;
            } else if (cloudEnrolleeDeviceModel != null && "0".equals(cloudEnrolleeDeviceModel.type)) {
                dCAlibabaConfigParams.regDeviceName = cloudEnrolleeDeviceModel.regDeviceName;
                dCAlibabaConfigParams.regProductKey = cloudEnrolleeDeviceModel.regProductKey;
            }
        }
        dCAlibabaConfigParams.extraInfoMap = this.extraDeviceInfo;
        dCAlibabaConfigParams.deviceId = this.deviceId;
        dCAlibabaConfigParams.subDeviceId = this.subDeviceId;
        dCAlibabaConfigParams.userId = this.userId;
        dCAlibabaConfigParams.flowId = this.flowId;
        dCAlibabaConfigParams.platform = this.platform;
        dCAlibabaConfigParams.icon = this.icon;
        dCAlibabaConfigParams.timeTmp = this.timeTmp;
        dCAlibabaConfigParams.time = this.time;
        dCAlibabaConfigParams.brand = this.brand;
        dCAlibabaConfigParams.genieProvisionModel = this.genieProvisionModel;
        dCAlibabaConfigParams.productName = this.productName;
        dCAlibabaConfigParams.image = this.image;
        dCAlibabaConfigParams.netType = this.netType;
        dCAlibabaConfigParams.nodeType = this.nodeType;
        dCAlibabaConfigParams.categoryKey = this.categoryKey;
        dCAlibabaConfigParams.categoryName = this.categoryName;
        dCAlibabaConfigParams.categoryId = this.categoryId;
        dCAlibabaConfigParams.linkUserType = this.linkUserType;
        dCAlibabaConfigParams.extUserId = this.extUserId;
        dCAlibabaConfigParams.accessKeyId = this.accessKeyId;
        dCAlibabaConfigParams.accessKeySecret = this.accessKeySecret;
        dCAlibabaConfigParams.encodeType = this.encodeType;
        dCAlibabaConfigParams.encodeKey = this.encodeKey;
        dCAlibabaConfigParams.authFlag = this.authFlag;
        dCAlibabaConfigParams.authDevice = this.authDevice;
        dCAlibabaConfigParams.random = this.random;
        dCAlibabaConfigParams.confirmCloud = this.confirmCloud;
        dCAlibabaConfigParams.rssi = this.rssi;
        dCAlibabaConfigParams.configurationInfo = this.configurationInfo;
        return dCAlibabaConfigParams;
    }

    public Object getExtraDeviceInfo(String str) {
        if (this.extraDeviceInfo == null || TextUtils.isEmpty(str)) {
            return null;
        }
        return this.extraDeviceInfo.get(str);
    }

    public boolean isSameApWithNoPk(DeviceInfo deviceInfo) {
        if (!TextUtils.isEmpty(this.productKey) || !TextUtils.isEmpty(deviceInfo.productKey) || !TextUtils.isEmpty(this.deviceName) || !TextUtils.isEmpty(deviceInfo.deviceName) || !TextUtils.isEmpty(this.productId) || !TextUtils.isEmpty(deviceInfo.productId) || getExtraDeviceInfo(AlinkConstants.KEY_APP_SSID) == null || TextUtils.isEmpty(String.valueOf(getExtraDeviceInfo(AlinkConstants.KEY_APP_SSID))) || !getExtraDeviceInfo(AlinkConstants.KEY_APP_SSID).equals(deviceInfo.getExtraDeviceInfo(AlinkConstants.KEY_APP_SSID))) {
            return false;
        }
        ALog.d(TAG, "same self define wifi.");
        return true;
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.LKDeviceInfo, com.aliyun.alink.business.devicecenter.api.add.ICacheModel
    public boolean isValid() {
        if (super.isValid()) {
            return true;
        }
        if (!TextUtils.isEmpty(this.productEncryptKey) && (LinkType.ALI_BROADCAST.getName().equals(this.linkType) || LinkType.ALI_DEFAULT.getName().equals(this.linkType) || LinkType.ALI_BROADCAST_IN_BATCHES.getName().equals(this.linkType))) {
            return true;
        }
        if (ProtocolVersion.NO_PRODUCT.getVersion().equals(this.protocolVersion) && TextUtils.isEmpty(this.productKey)) {
            return true;
        }
        if (TextUtils.isEmpty(this.productId) && !TextUtils.isEmpty(this.mac) && LinkType.ALI_BLE.getName().equals(this.linkType)) {
            return true;
        }
        if (TextUtils.isEmpty(this.productId) || !LinkType.ALI_BLE.getName().equals(this.linkType)) {
            return !(!LinkType.ALI_GATEWAY_MESH.getName().equals(this.linkType) || TextUtils.isEmpty(this.regIotId) || TextUtils.isEmpty(this.deviceId)) || LinkType.ALI_GENIE_SOUND_BOX.getName().equalsIgnoreCase(this.linkType) || LinkType.ALI_GENIE_QR.getName().equalsIgnoreCase(this.linkType) || LinkType.ALI_GENIE_STATIC_QR.getName().equalsIgnoreCase(this.linkType);
        }
        return true;
    }

    public void setExtraDeviceInfo(String str, Object obj) {
        if (this.extraDeviceInfo == null) {
            this.extraDeviceInfo = new HashMap();
        }
        this.extraDeviceInfo.put(str, obj);
    }

    public void setExtraMapInfo(Map map) {
        if (map == null) {
            map = new HashMap();
        }
        if (this.extraDeviceInfo == null) {
            this.extraDeviceInfo = new HashMap();
        }
        this.extraDeviceInfo.putAll(map);
    }

    public String toString() {
        return "{id='" + this.id + "', linkType='" + this.linkType + "', token='" + this.token + "', devType='" + this.devType + "', awssVer=" + this.awssVer + ", mac='" + this.mac + "', remainTime='" + this.remainTime + "', protocolVersion='" + this.protocolVersion + "', regProductKey='" + this.regProductKey + "', regDeviceName='" + this.regDeviceName + "', addDeviceFrom='" + this.addDeviceFrom + "', service='" + this.service + "', fwVersion='" + this.fwVersion + "', tag=" + this.tag + ", bindResultInfo=" + this.bindResultInfo + ", regionInfo=" + this.regionInfo + ", iotId='" + this.iotId + "', deviceId='" + this.deviceId + "', subDeviceId='" + this.subDeviceId + "', regIotId='" + this.regIotId + "', extraDeviceInfo=" + this.extraDeviceInfo + ", userId=" + this.userId + ", genieProvisionModel=" + this.genieProvisionModel + ", productKey=" + this.productKey + ", productId=" + this.productId + ", deviceName=" + this.deviceName + ", platform=" + this.platform + ", icon=" + this.icon + ", timeTmp=" + this.timeTmp + ", time=" + this.time + ", brand=" + this.brand + ", flowId=" + this.flowId + ", productName=" + this.productName + ", image=" + this.image + ", netType=" + this.netType + ", nodeType=" + this.nodeType + ", categoryKey=" + this.categoryKey + ", categoryName=" + this.categoryName + ", categoryId=" + this.categoryId + ", linkUserType=" + this.linkUserType + ", extUserId=" + this.extUserId + ", accessKeyId=" + this.accessKeyId + ", accessKeySecret=" + this.accessKeySecret + ", encodeType=" + this.encodeType + ", encodeKey=" + this.encodeKey + ", authFlag=" + this.authFlag + ", authDevice=" + this.authDevice + ", random=" + this.random + ", confirmCloud=" + this.confirmCloud + ", rssi=" + this.rssi + ", configResultMap=" + this.configurationInfo + '}';
    }

    public Map getExtraDeviceInfo() {
        return this.extraDeviceInfo;
    }

    public static DeviceInfo convertLocalDevice(JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.deviceName = jSONObject.getString("deviceName");
        deviceInfo.productKey = jSONObject.getString("productKey");
        deviceInfo.devType = jSONObject.getString("type");
        deviceInfo.token = jSONObject.getString("token");
        deviceInfo.mac = jSONObject.getString(AlinkConstants.KEY_MAC);
        deviceInfo.id = AlinkHelper.getHalfMacFromMac(deviceInfo.mac);
        deviceInfo.service = jSONObject.getString("service");
        deviceInfo.fwVersion = jSONObject.getString(AlinkConstants.KEY_FW_VERSION);
        return deviceInfo;
    }
}
