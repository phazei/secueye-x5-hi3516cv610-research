package datasource.channel.data;

import datasource.bean.ControlProtocol;
import java.io.Serializable;

/* JADX INFO: loaded from: classes3.dex */
public class DownStreamControlDataForTg implements Serializable {
    public String aiIcon;
    public String elementCode;
    public String familyId;
    public String linked;
    public String lowPower;
    public String mac;
    public String productKey;
    public ControlProtocol pushCmd;
    public String tmpProductKey;
    public String vendorModelVersion;

    public String getAiIcon() {
        return this.aiIcon;
    }

    public String getElementCode() {
        return this.elementCode;
    }

    public String getFamilyId() {
        return this.familyId;
    }

    public String getLinked() {
        return this.linked;
    }

    public String getLowPower() {
        return this.lowPower;
    }

    public String getMac() {
        return this.mac;
    }

    public String getProductKey() {
        return this.productKey;
    }

    public ControlProtocol getPushCmd() {
        return this.pushCmd;
    }

    public String getTmpProductKey() {
        return this.tmpProductKey;
    }

    public String getVendorModelVersion() {
        return this.vendorModelVersion;
    }

    public void setAiIcon(String str) {
        this.aiIcon = str;
    }

    public void setElementCode(String str) {
        this.elementCode = str;
    }

    public void setFamilyId(String str) {
        this.familyId = str;
    }

    public void setLinked(String str) {
        this.linked = str;
    }

    public void setLowPower(String str) {
        this.lowPower = str;
    }

    public void setMac(String str) {
        this.mac = str;
    }

    public void setProductKey(String str) {
        this.productKey = str;
    }

    public void setPushCmd(ControlProtocol controlProtocol) {
        this.pushCmd = controlProtocol;
    }

    public void setTmpProductKey(String str) {
        this.tmpProductKey = str;
    }

    public void setVendorModelVersion(String str) {
        this.vendorModelVersion = str;
    }

    public String toString() {
        return "DownStreamControlDataForTg{aiIcon='" + this.aiIcon + "', linked='" + this.linked + "', tmpProductKey='" + this.tmpProductKey + "', mac='" + this.mac + "', familyId='" + this.familyId + "', lowPower='" + this.lowPower + "', elementCode='" + this.elementCode + "', vendorModelVersion='" + this.vendorModelVersion + "', productKey='" + this.productKey + "', pushCmd=" + this.pushCmd + '}';
    }
}
