package meshprovisioner.states;

import android.os.Build;
import c.a.d.a;
import datasource.bean.ConfigurationData;

/* JADX INFO: loaded from: classes4.dex */
public class UnprovisionedMeshNodeData {
    public static final int COMPANY_ID = 424;
    public static final int PROTOCOL_ID_MAX = 1;
    public static final int PROTOCOL_ID_MIN = 0;
    public static final String TAG = "UnprovisionedMeshNodeData";
    public int bleVersion;
    public int companyId;
    public ConfigurationData configurationInfo;
    public String deviceMac;
    public byte[] deviceUuid;
    public byte mFeatureFlag;
    public byte mFeatureFlag1;
    public byte mFeatureFlag2;
    public byte[] mac;
    public int productId;
    public int protocolId;
    public boolean supportEncrypt;
    public boolean supportOTA;

    public UnprovisionedMeshNodeData(byte[] bArr) {
        parseFromUnprovisionedAdData(bArr);
    }

    private void parseFromUnprovisionedAdData(byte[] bArr) {
        if (bArr == null || bArr.length < 16) {
            return;
        }
        this.deviceUuid = a.a(bArr, 0, 16);
        this.companyId += bArr[0] & 255;
        this.companyId += (bArr[1] & 255) << 8;
        this.protocolId = bArr[2] & 15;
        if (!isValid()) {
            a.a.a.a.b.m.a.d(TAG, "Invalid unprovision node, not ali node");
            return;
        }
        this.supportEncrypt = ((bArr[2] & 16) >> 4) == 1;
        this.supportOTA = ((bArr[2] & 32) >> 5) == 1;
        this.bleVersion = (bArr[2] & 192) >> 6;
        this.productId += bArr[3] & 255;
        this.productId += (bArr[4] & 255) << 8;
        this.productId += (bArr[5] & 255) << 16;
        this.productId += (bArr[6] & 255) << 24;
        this.deviceMac = String.format("%1$02x%2$02x%3$02x%4$02x%5$02x%6$02x", Byte.valueOf(bArr[12]), Byte.valueOf(bArr[11]), Byte.valueOf(bArr[10]), Byte.valueOf(bArr[9]), Byte.valueOf(bArr[8]), Byte.valueOf(bArr[7]));
        this.mFeatureFlag = bArr[13];
        this.mFeatureFlag1 = bArr[14];
        this.mFeatureFlag2 = bArr[15];
        this.mac = new byte[6];
        for (int i = 0; i < 6; i++) {
            this.mac[i] = bArr[12 - i];
        }
    }

    public int getBleVersion() {
        return this.bleVersion;
    }

    public int getCompanyId() {
        return this.companyId;
    }

    public ConfigurationData getConfigurationInfo() {
        return this.configurationInfo;
    }

    public String getDeviceMac() {
        return this.deviceMac;
    }

    public byte[] getDeviceUuid() {
        return this.deviceUuid;
    }

    public byte getFeatureFlag1() {
        return this.mFeatureFlag1;
    }

    public byte[] getMac() {
        return this.mac;
    }

    public int getProductId() {
        return this.productId;
    }

    public int getProtocolId() {
        return this.protocolId;
    }

    public boolean isComboMeshDevice() {
        return (this.mFeatureFlag1 & 8) == 8;
    }

    public boolean isFastProvisionMesh() {
        return (this.mFeatureFlag1 == 7 && Build.VERSION.SDK_INT >= 21) || isFastSupportGatt();
    }

    public boolean isFastSupportGatt() {
        if (a.a.a.a.b.d.a.f1316b) {
            byte b2 = this.mFeatureFlag1;
            return (b2 & 1) == 1 && (b2 & 4) == 0;
        }
        byte b3 = this.mFeatureFlag1;
        return (b3 == -125 || b3 == -109) && Build.VERSION.SDK_INT >= 21;
    }

    public boolean isQuietModel() {
        boolean z = (this.mFeatureFlag & 1) == 1;
        a.a.a.a.b.m.a.a(TAG, this.deviceMac + ": quite model: " + z);
        return z;
    }

    public boolean isSupportAutomaticallyGenerateShareAppKey() {
        if ((this.mFeatureFlag >> 1) <= 1) {
            return false;
        }
        byte b2 = this.mFeatureFlag2;
        return (b2 >> 5) == 0 && (b2 & 8) != 0;
    }

    public boolean isSupportEncrypt() {
        return this.supportEncrypt;
    }

    public boolean isSupportFastProvisioningV2() {
        return (this.mFeatureFlag >> 1) > 1 && (this.mFeatureFlag2 & 16) == 16;
    }

    public boolean isSupportLargeScaleMeshNetwork() {
        return (this.mFeatureFlag >> 1) > 1 && (this.mFeatureFlag2 & 1) == 1;
    }

    public boolean isSupportOTA() {
        return this.supportOTA;
    }

    public boolean isValid() {
        int i;
        return this.companyId == 424 && (i = this.protocolId) >= 0 && i <= 1;
    }

    public void setConfigurationInfo(ConfigurationData configurationData) {
        this.configurationInfo = configurationData;
    }
}
