package datasource.bean;

import java.io.Serializable;

/* JADX INFO: loaded from: classes3.dex */
public class IotDeviceInfo implements Serializable {
    public Object alias;
    public Object brand;
    public Object children;
    public Object cookie;
    public String devId;
    public String devType;
    public String devTypeEn;
    public Object deviceName;
    public String deviceToken;
    public String extensions;
    public Object feature;
    public String gmtCreate;
    public String gmtModified;
    public String icon;
    public int id;
    public boolean isAppControlable;
    public Object jumpUrl;
    public String mac;
    public Object model;
    public Object networkType;
    public Object onlineState;
    public Object parentDeviceId;
    public Object particularModel;
    public PlatformBean platform;
    public String productKey;
    public Object properties;
    public int skillId;
    public String source;
    public String status;
    public String typeGroup;
    public int unicastAddress;
    public String userId;
    public String uuid;
    public int version;
    public Object zone;

    public static class PlatformBean implements Serializable {
        public String name;

        public String getName() {
            return this.name;
        }

        public void setName(String str) {
            this.name = str;
        }
    }

    public Object getAlias() {
        return this.alias;
    }

    public Object getBrand() {
        return this.brand;
    }

    public Object getChildren() {
        return this.children;
    }

    public Object getCookie() {
        return this.cookie;
    }

    public String getDevId() {
        return this.devId;
    }

    public String getDevType() {
        return this.devType;
    }

    public String getDevTypeEn() {
        return this.devTypeEn;
    }

    public Object getDeviceName() {
        return this.deviceName;
    }

    public String getDeviceToken() {
        return this.deviceToken;
    }

    public String getExtensions() {
        return this.extensions;
    }

    public Object getFeature() {
        return this.feature;
    }

    public String getGmtCreate() {
        return this.gmtCreate;
    }

    public String getGmtModified() {
        return this.gmtModified;
    }

    public String getIcon() {
        return this.icon;
    }

    public int getId() {
        return this.id;
    }

    public Object getJumpUrl() {
        return this.jumpUrl;
    }

    public String getMac() {
        return this.mac;
    }

    public Object getModel() {
        return this.model;
    }

    public Object getNetworkType() {
        return this.networkType;
    }

    public Object getOnlineState() {
        return this.onlineState;
    }

    public Object getParentDeviceId() {
        return this.parentDeviceId;
    }

    public Object getParticularModel() {
        return this.particularModel;
    }

    public PlatformBean getPlatform() {
        return this.platform;
    }

    public String getProductKey() {
        return this.productKey;
    }

    public Object getProperties() {
        return this.properties;
    }

    public int getSkillId() {
        return this.skillId;
    }

    public String getSource() {
        return this.source;
    }

    public String getStatus() {
        return this.status;
    }

    public String getTypeGroup() {
        return this.typeGroup;
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

    public int getVersion() {
        return this.version;
    }

    public Object getZone() {
        return this.zone;
    }

    public boolean isIsAppControlable() {
        return this.isAppControlable;
    }

    public void setAlias(Object obj) {
        this.alias = obj;
    }

    public void setBrand(Object obj) {
        this.brand = obj;
    }

    public void setChildren(Object obj) {
        this.children = obj;
    }

    public void setCookie(Object obj) {
        this.cookie = obj;
    }

    public void setDevId(String str) {
        this.devId = str;
    }

    public void setDevType(String str) {
        this.devType = str;
    }

    public void setDevTypeEn(String str) {
        this.devTypeEn = str;
    }

    public void setDeviceName(Object obj) {
        this.deviceName = obj;
    }

    public void setDeviceToken(String str) {
        this.deviceToken = str;
    }

    public void setExtensions(String str) {
        this.extensions = str;
    }

    public void setFeature(Object obj) {
        this.feature = obj;
    }

    public void setGmtCreate(String str) {
        this.gmtCreate = str;
    }

    public void setGmtModified(String str) {
        this.gmtModified = str;
    }

    public void setIcon(String str) {
        this.icon = str;
    }

    public void setId(int i) {
        this.id = i;
    }

    public void setIsAppControlable(boolean z) {
        this.isAppControlable = z;
    }

    public void setJumpUrl(Object obj) {
        this.jumpUrl = obj;
    }

    public void setMac(String str) {
        this.mac = str;
    }

    public void setModel(Object obj) {
        this.model = obj;
    }

    public void setNetworkType(Object obj) {
        this.networkType = obj;
    }

    public void setOnlineState(Object obj) {
        this.onlineState = obj;
    }

    public void setParentDeviceId(Object obj) {
        this.parentDeviceId = obj;
    }

    public void setParticularModel(Object obj) {
        this.particularModel = obj;
    }

    public void setPlatform(PlatformBean platformBean) {
        this.platform = platformBean;
    }

    public void setProductKey(String str) {
        this.productKey = str;
    }

    public void setProperties(Object obj) {
        this.properties = obj;
    }

    public void setSkillId(int i) {
        this.skillId = i;
    }

    public void setSource(String str) {
        this.source = str;
    }

    public void setStatus(String str) {
        this.status = str;
    }

    public void setTypeGroup(String str) {
        this.typeGroup = str;
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

    public void setVersion(int i) {
        this.version = i;
    }

    public void setZone(Object obj) {
        this.zone = obj;
    }
}
