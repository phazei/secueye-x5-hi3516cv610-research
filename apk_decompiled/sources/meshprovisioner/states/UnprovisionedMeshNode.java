package meshprovisioner.states;

import android.os.Parcel;
import android.os.Parcelable;
import b.InterfaceC0367a;
import meshprovisioner.BaseMeshNode;

/* JADX INFO: loaded from: classes4.dex */
public final class UnprovisionedMeshNode extends BaseMeshNode {
    public static final Parcelable.Creator<UnprovisionedMeshNode> CREATOR = new Parcelable.Creator<UnprovisionedMeshNode>() { // from class: meshprovisioner.states.UnprovisionedMeshNode.1
        @Override // android.os.Parcelable.Creator
        public UnprovisionedMeshNode createFromParcel(Parcel parcel) {
            return new UnprovisionedMeshNode(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public UnprovisionedMeshNode[] newArray(int i) {
            return new UnprovisionedMeshNode[i];
        }
    };
    public static final String TAG = "UnprovisionedMeshNode";
    public InterfaceC0367a mCloudComfirmationProvisioningCallbacks;
    public byte[] serviceData;

    public UnprovisionedMeshNode() {
    }

    @Override // meshprovisioner.BaseMeshNode, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public final byte[] getAuthenticationValue() {
        return this.authenticationValue;
    }

    public InterfaceC0367a getCloudComfirmationProvisioningCallbacks() {
        return this.mCloudComfirmationProvisioningCallbacks;
    }

    @Override // meshprovisioner.BaseMeshNode
    public final byte[] getNetworkKey() {
        return this.networkKey;
    }

    public final byte[] getProvisioneeConfirmation() {
        return this.provisioneeConfirmation;
    }

    public final byte[] getProvisioneePublicKeyXY() {
        return this.provisioneePublicKeyXY;
    }

    public final byte[] getProvisioneeRandom() {
        return this.provisioneeRandom;
    }

    public final byte[] getProvisionerPublicKeyXY() {
        return this.provisionerPublicKeyXY;
    }

    public final byte[] getProvisionerRandom() {
        return this.provisionerRandom;
    }

    public byte[] getServiceData() {
        return this.serviceData;
    }

    public final byte[] getSharedECDHSecret() {
        return this.sharedECDHSecret;
    }

    @Override // meshprovisioner.BaseMeshNode
    public final void setAuthenticationValue(byte[] bArr) {
        this.authenticationValue = bArr;
    }

    public void setCloudComfirmationProvisioningCallbacks(InterfaceC0367a interfaceC0367a) {
        this.mCloudComfirmationProvisioningCallbacks = interfaceC0367a;
    }

    @Override // meshprovisioner.BaseMeshNode
    public final void setDeviceKey(byte[] bArr) {
        this.deviceKey = bArr;
    }

    @Override // meshprovisioner.BaseMeshNode
    public final void setNetworkKey(byte[] bArr) {
        this.networkKey = bArr;
    }

    public final void setProvisionedTime(long j) {
        this.mTimeStampInMillis = j;
    }

    @Override // meshprovisioner.BaseMeshNode
    public final void setProvisioneeConfirmation(byte[] bArr) {
        this.provisioneeConfirmation = bArr;
    }

    @Override // meshprovisioner.BaseMeshNode
    public final void setProvisioneePublicKeyXY(byte[] bArr) {
        this.provisioneePublicKeyXY = bArr;
    }

    @Override // meshprovisioner.BaseMeshNode
    public final void setProvisioneeRandom(byte[] bArr) {
        this.provisioneeRandom = bArr;
    }

    @Override // meshprovisioner.BaseMeshNode
    public final void setProvisionerPublicKeyXY(byte[] bArr) {
        this.provisionerPublicKeyXY = bArr;
    }

    @Override // meshprovisioner.BaseMeshNode
    public final void setProvisionerRandom(byte[] bArr) {
        this.provisionerRandom = bArr;
    }

    @Override // meshprovisioner.BaseMeshNode
    public void setProvisioningCapabilities(ProvisioningCapabilities provisioningCapabilities) {
        this.numberOfElements = provisioningCapabilities.getNumberOfElements();
        this.provisioningCapabilities = provisioningCapabilities;
    }

    public void setServiceData(byte[] bArr) {
        this.serviceData = bArr;
    }

    @Override // meshprovisioner.BaseMeshNode
    public final void setSharedECDHSecret(byte[] bArr) {
        this.sharedECDHSecret = bArr;
    }

    public boolean useCloudeConfirmationProvisioning() {
        return (this.mCloudComfirmationProvisioningCallbacks == null || this.provisioningCapabilities.getStaticOOBType() == 0) ? false : true;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte(this.isProvisioned ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isConfigured ? (byte) 1 : (byte) 0);
        parcel.writeString(this.nodeName);
        parcel.writeByteArray(this.provisionerPublicKeyXY);
        parcel.writeByteArray(this.provisioneePublicKeyXY);
        parcel.writeByteArray(this.sharedECDHSecret);
        parcel.writeByteArray(this.provisionerRandom);
        parcel.writeByteArray(this.provisioneeConfirmation);
        parcel.writeByteArray(this.authenticationValue);
        parcel.writeByteArray(this.provisioneeRandom);
        parcel.writeByteArray(this.networkKey);
        parcel.writeByteArray(this.identityKey);
        parcel.writeByteArray(this.keyIndex);
        parcel.writeByteArray(this.mFlags);
        parcel.writeByteArray(this.ivIndex);
        parcel.writeByteArray(this.unicastAddress);
        parcel.writeByteArray(this.deviceKey);
        parcel.writeInt(this.ttl);
        parcel.writeParcelable(this.provisioningCapabilities, i);
    }

    public UnprovisionedMeshNode(Parcel parcel) {
        this.isProvisioned = parcel.readByte() != 0;
        this.isConfigured = parcel.readByte() != 0;
        this.nodeName = parcel.readString();
        this.provisionerPublicKeyXY = parcel.createByteArray();
        this.provisioneePublicKeyXY = parcel.createByteArray();
        this.sharedECDHSecret = parcel.createByteArray();
        this.provisionerRandom = parcel.createByteArray();
        this.provisioneeConfirmation = parcel.createByteArray();
        this.authenticationValue = parcel.createByteArray();
        this.provisioneeRandom = parcel.createByteArray();
        this.networkKey = parcel.createByteArray();
        this.identityKey = parcel.createByteArray();
        this.keyIndex = parcel.createByteArray();
        this.mFlags = parcel.createByteArray();
        this.ivIndex = parcel.createByteArray();
        this.unicastAddress = parcel.createByteArray();
        this.deviceKey = parcel.createByteArray();
        this.ttl = parcel.readInt();
        this.provisioningCapabilities = (ProvisioningCapabilities) parcel.readParcelable(ProvisioningCapabilities.class.getClassLoader());
        this.numberOfElements = this.provisioningCapabilities.getNumberOfElements();
    }
}
