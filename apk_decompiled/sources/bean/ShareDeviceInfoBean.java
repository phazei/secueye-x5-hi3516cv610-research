package bean;

/* JADX INFO: loaded from: classes.dex */
public class ShareDeviceInfoBean {
    private String categoryImage;
    private String description;
    private String deviceName;
    private String gmtCreate;
    private String gmtModified;
    private String initiatorAlias;
    private int isReceiver;
    private String productName;
    private String receiverAlias;
    private String recordId;
    private int status;
    private String targetId;
    private String targetType;

    public String getGmtCreate() {
        return this.gmtCreate;
    }

    public void setGmtCreate(String str) {
        this.gmtCreate = str;
    }

    public String getGmtModified() {
        return this.gmtModified;
    }

    public void setGmtModified(String str) {
        this.gmtModified = str;
    }

    public String getTargetId() {
        return this.targetId;
    }

    public void setTargetId(String str) {
        this.targetId = str;
    }

    public String getCategoryImage() {
        return this.categoryImage;
    }

    public void setCategoryImage(String str) {
        this.categoryImage = str;
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

    public String getRecordId() {
        return this.recordId;
    }

    public void setRecordId(String str) {
        this.recordId = str;
    }

    public String getDeviceName() {
        return this.deviceName;
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

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public DeviceInfoBean toDeviceInfoBean() {
        DeviceInfoBean deviceInfoBean = new DeviceInfoBean();
        deviceInfoBean.setGmtCreate(this.gmtCreate);
        deviceInfoBean.setTargetId(this.targetId);
        deviceInfoBean.setDescription(this.description);
        deviceInfoBean.setTargetType(this.targetType);
        deviceInfoBean.setRecordId(this.recordId);
        deviceInfoBean.setInitiatorAlias(this.initiatorAlias);
        deviceInfoBean.setReceiverAlias(this.receiverAlias);
        deviceInfoBean.setIsReceiver(this.isReceiver);
        deviceInfoBean.setReceiverStatus(this.status);
        deviceInfoBean.setGmtModified(Long.parseLong(this.gmtModified));
        deviceInfoBean.setCategoryImage(this.categoryImage);
        deviceInfoBean.setDeviceName(this.deviceName);
        deviceInfoBean.setProductName(this.productName);
        return deviceInfoBean;
    }
}
