package meshprovisioner.configuration.bean;

/* JADX INFO: loaded from: classes4.dex */
public class CfgAppKeyStatus {
    public static final String TAG = "CfgAppKeyStatus";
    public String appKey;
    public byte[] appKeyIndex;
    public boolean isSuccessful;
    public byte[] netKeyIndex;
    public int status;
    public String statusMessage;

    public CfgAppKeyStatus(int i, byte[] bArr, byte[] bArr2) {
        this.status = i;
        this.netKeyIndex = bArr;
        this.appKeyIndex = bArr2;
        this.isSuccessful = i == 0;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public byte[] getAppKeyIndex() {
        return this.appKeyIndex;
    }

    public byte[] getNetKeyIndex() {
        return this.netKeyIndex;
    }

    public int getStatus() {
        return this.status;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public boolean isSuccessful() {
        return this.isSuccessful;
    }

    public void setAppKey(String str) {
        this.appKey = str;
    }

    public void setAppKeyIndex(byte[] bArr) {
        this.appKeyIndex = bArr;
    }

    public void setNetKeyIndex(byte[] bArr) {
        this.netKeyIndex = bArr;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public void setStatusMessage(String str) {
        this.statusMessage = str;
    }

    public void setSuccessful(boolean z) {
        this.isSuccessful = z;
    }
}
