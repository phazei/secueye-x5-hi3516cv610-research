package bean;

/* JADX INFO: loaded from: classes.dex */
public class ThingInfoBean {
    private Long activeTime;
    private String deviceSecret;
    private String firmwareVersion;
    private long gmtCreate;
    private long gmtModified;
    private String iotId;
    private String mac;
    private String name;
    private String netAddress;
    private String nickname;
    private String productKey;
    private String rbacTenantId;
    private String sdkVersion;
    private String sn;
    private int status;
    private int statusLast;
    private String thingType;

    public long getGmtModified() {
        return this.gmtModified;
    }

    public void setGmtModified(long j) {
        this.gmtModified = j;
    }

    public Long getActiveTime() {
        return this.activeTime;
    }

    public void setActiveTime(Long l) {
        this.activeTime = l;
    }

    public long getGmtCreate() {
        return this.gmtCreate;
    }

    public void setGmtCreate(long j) {
        this.gmtCreate = j;
    }

    public String getProductKey() {
        return this.productKey;
    }

    public void setProductKey(String str) {
        this.productKey = str;
    }

    public int getStatusLast() {
        return this.statusLast;
    }

    public void setStatusLast(int i) {
        this.statusLast = i;
    }

    public String getMac() {
        return this.mac;
    }

    public void setMac(String str) {
        this.mac = str;
    }

    public String getNetAddress() {
        return this.netAddress;
    }

    public void setNetAddress(String str) {
        this.netAddress = str;
    }

    public String getDeviceSecret() {
        return this.deviceSecret;
    }

    public void setDeviceSecret(String str) {
        this.deviceSecret = str;
    }

    public String getIotId() {
        return this.iotId;
    }

    public void setIotId(String str) {
        this.iotId = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String str) {
        this.nickname = str;
    }

    public String getSdkVersion() {
        return this.sdkVersion;
    }

    public void setSdkVersion(String str) {
        this.sdkVersion = str;
    }

    public String getSn() {
        return this.sn;
    }

    public void setSn(String str) {
        this.sn = str;
    }

    public String getThingType() {
        return this.thingType;
    }

    public void setThingType(String str) {
        this.thingType = str;
    }

    public String getFirmwareVersion() {
        return this.firmwareVersion;
    }

    public void setFirmwareVersion(String str) {
        this.firmwareVersion = str;
    }

    public String getRbacTenantId() {
        return this.rbacTenantId;
    }

    public void setRbacTenantId(String str) {
        this.rbacTenantId = str;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public String toString() {
        return "ThingInfoBean{gmtModified=" + this.gmtModified + ", activeTime=" + this.activeTime + ", gmtCreate=" + this.gmtCreate + ", productKey='" + this.productKey + "', statusLast=" + this.statusLast + ", mac='" + this.mac + "', netAddress='" + this.netAddress + "', deviceSecret='" + this.deviceSecret + "', iotId='" + this.iotId + "', name='" + this.name + "', nickname='" + this.nickname + "', sdkVersion='" + this.sdkVersion + "', sn='" + this.sn + "', thingType='" + this.thingType + "', firmwareVersion='" + this.firmwareVersion + "', rbacTenantId='" + this.rbacTenantId + "', status=" + this.status + '}';
    }
}
