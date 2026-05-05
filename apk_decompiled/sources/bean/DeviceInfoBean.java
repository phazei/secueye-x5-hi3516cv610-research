package bean;

import java.io.Serializable;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DeviceInfoBean implements Serializable {
    private String categoryImage;
    public List<DeviceInfoBean> childDevices;
    private String description;
    private String deviceName;
    private String gmtCreate;
    private long gmtModified;
    private String identityAlias;
    private String identityId;
    public String image;
    private long imgUpdateTime;
    private String initiatorAlias;
    private String iotId;
    private boolean isChecked;
    private boolean isLike;
    private int isReceiver;
    private int lowPower;
    private String netType;
    private String nickName;
    private String nodeType;
    private int owned;
    private String productImage;
    private String productKey;
    private String productModel;
    private String productName;
    private String receiverAlias;
    private int receiverStatus;
    private String recordId;
    private int status;
    private String targetId;
    private String targetType;
    private String thingType;
    private String wakeUpData;
    public boolean isNvr = false;
    private int lowPowerMode = -1;
    private int consumed = -1;

    public String getImage() {
        return this.image;
    }

    public List<DeviceInfoBean> getChildDevices() {
        return this.childDevices;
    }

    public int getLowPowerMode() {
        return this.lowPowerMode;
    }

    public void setLowPowerMode(int i) {
        this.lowPowerMode = i;
    }

    public String getWakeUpData() {
        return this.wakeUpData;
    }

    public void setWakeUpData(String str) {
        this.wakeUpData = str;
    }

    public int getConsumed() {
        return this.consumed;
    }

    public void setConsumed(int i) {
        this.consumed = i;
    }

    public String getProductImage() {
        return this.productImage;
    }

    public void setProductImage(String str) {
        this.productImage = str;
    }

    public String getProductModel() {
        return this.productModel;
    }

    public void setProductModel(String str) {
        this.productModel = str;
    }

    public String getNickName() {
        String str = this.nickName;
        if (str != null && str.contains("狮安联讯")) {
            this.nickName = this.nickName.replace("狮安联讯", "小眯眼");
        }
        return this.nickName;
    }

    public void setNickName(String str) {
        this.nickName = str;
    }

    public long getImgUpdateTime() {
        return this.imgUpdateTime;
    }

    public void setImgUpdateTime(long j) {
        this.imgUpdateTime = j;
    }

    public String getGmtCreate() {
        return this.gmtCreate;
    }

    public void setGmtCreate(String str) {
        this.gmtCreate = str;
    }

    public String getTargetId() {
        return this.targetId;
    }

    public void setTargetId(String str) {
        this.targetId = str;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public String getTargetType() {
        return this.targetType;
    }

    public void setTargetType(String str) {
        this.targetType = str;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setChecked(boolean z) {
        this.isChecked = z;
    }

    public String getRecordId() {
        return this.recordId;
    }

    public void setRecordId(String str) {
        this.recordId = str;
    }

    public String getInitiatorAlias() {
        return this.initiatorAlias;
    }

    public void setInitiatorAlias(String str) {
        this.initiatorAlias = str;
    }

    public String getReceiverAlias() {
        return this.receiverAlias;
    }

    public void setReceiverAlias(String str) {
        this.receiverAlias = str;
    }

    public int getIsReceiver() {
        return this.isReceiver;
    }

    public void setIsReceiver(int i) {
        this.isReceiver = i;
    }

    public long getGmtModified() {
        return this.gmtModified;
    }

    public void setGmtModified(long j) {
        this.gmtModified = j;
    }

    public String getCategoryImage() {
        return this.categoryImage;
    }

    public void setCategoryImage(String str) {
        this.categoryImage = str;
    }

    public String getNetType() {
        return this.netType;
    }

    public void setNetType(String str) {
        this.netType = str;
    }

    public String getNodeType() {
        return this.nodeType;
    }

    public void setNodeType(String str) {
        this.nodeType = str;
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

    public String getName() {
        String str = this.nickName;
        if (str != null && str.contains("狮安联讯")) {
            this.nickName = this.nickName.replace("狮安联讯", "小眯眼");
        }
        String str2 = this.productName;
        if (str2 != null && str2.contains("狮安联讯")) {
            this.productName = this.productName.replace("狮安联讯", "小眯眼");
        }
        String str3 = this.nickName;
        return str3 != null ? str3 : this.productName;
    }

    public void setDeviceName(String str) {
        this.deviceName = str;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String str) {
        this.productName = str;
    }

    public String getIdentityAlias() {
        return this.identityAlias;
    }

    public void setIdentityAlias(String str) {
        this.identityAlias = str;
    }

    public String getIotId() {
        return this.iotId;
    }

    public void setIotId(String str) {
        this.iotId = str;
    }

    public int getOwned() {
        return this.owned;
    }

    public void setOwned(int i) {
        this.owned = i;
    }

    public String getIdentityId() {
        return this.identityId;
    }

    public void setIdentityId(String str) {
        this.identityId = str;
    }

    public String getThingType() {
        return this.thingType;
    }

    public void setThingType(String str) {
        this.thingType = str;
    }

    public int getReceiverStatus() {
        return this.receiverStatus;
    }

    public void setReceiverStatus(int i) {
        this.receiverStatus = i;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public int getLowPower() {
        return this.lowPower;
    }

    public void setLowPower(int i) {
        this.lowPower = i;
    }

    public boolean isLike() {
        return this.isLike;
    }

    public void setLike(boolean z) {
        this.isLike = z;
    }

    public String toString() {
        return "DeviceInfoBean{gmtModified=" + this.gmtModified + ", categoryImage='" + this.categoryImage + "', netType='" + this.netType + "', nodeType='" + this.nodeType + "', productKey='" + this.productKey + "', deviceName='" + this.deviceName + "', productName='" + this.productName + "', identityAlias='" + this.identityAlias + "', iotId='" + this.iotId + "', productImage='" + this.productImage + "', productModel='" + this.productModel + "', owned=" + this.owned + ", identityId='" + this.identityId + "', thingType='" + this.thingType + "', nickName='" + this.nickName + "', status=" + this.status + ", gmtCreate='" + this.gmtCreate + "', targetId='" + this.targetId + "', description='" + this.description + "', targetType='" + this.targetType + "', recordId='" + this.recordId + "', initiatorAlias='" + this.initiatorAlias + "', receiverAlias='" + this.receiverAlias + "', isReceiver=" + this.isReceiver + ", receiverStatus=" + this.receiverStatus + ", imgUpdateTime=" + this.imgUpdateTime + '}';
    }
}
