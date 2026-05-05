package meshprovisioner.configuration;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.VisibleForTesting;
import com.alibaba.fastjson.annotation.JSONField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import meshprovisioner.BaseMeshNode;
import meshprovisioner.states.UnprovisionedMeshNode;
import meshprovisioner.utils.Element;
import meshprovisioner.utils.SecureUtils;
import meshprovisioner.utils.SparseIntArrayParcelable;

/* JADX INFO: loaded from: classes4.dex */
public class ProvisionedMeshNode extends BaseMeshNode implements Cloneable {
    public static final Parcelable.Creator<ProvisionedMeshNode> CREATOR = new Parcelable.Creator<ProvisionedMeshNode>() { // from class: meshprovisioner.configuration.ProvisionedMeshNode.1
        @Override // android.os.Parcelable.Creator
        public ProvisionedMeshNode createFromParcel(Parcel parcel) {
            return new ProvisionedMeshNode(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public ProvisionedMeshNode[] newArray(int i) {
            return new ProvisionedMeshNode[i];
        }
    };
    public String devId;
    public SecureUtils.K2Output k2Output;

    @VisibleForTesting(otherwise = 4)
    public ProvisionedMeshNode() {
    }

    private void sortElements(HashMap<Integer, Element> map) {
        ArrayList arrayList = new ArrayList(map.keySet());
        Collections.sort(arrayList);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            int iIntValue = ((Integer) it.next()).intValue();
            this.mElements.put(Integer.valueOf(iIntValue), map.get(Integer.valueOf(iIntValue)));
        }
    }

    public Object clone() {
        return super.clone();
    }

    @Override // meshprovisioner.BaseMeshNode, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @JSONField(deserialize = false)
    public final Map<Integer, String> getAddedAppKeys() {
        return Collections.unmodifiableMap(this.mAddedAppKeys);
    }

    public final Integer getCompanyIdentifier() {
        return this.companyIdentifier;
    }

    public final Integer getCrpl() {
        return this.crpl;
    }

    public String getDevId() {
        return this.devId;
    }

    @Override // meshprovisioner.BaseMeshNode
    public final byte[] getDeviceKey() {
        return this.deviceKey;
    }

    @Override // meshprovisioner.BaseMeshNode
    @JSONField(deserialize = false)
    public final Map<Integer, Element> getElements() {
        return Collections.unmodifiableMap(this.mElements);
    }

    public final Integer getFeatures() {
        return this.features;
    }

    public final byte[] getGeneratedNetworkId() {
        return this.generatedNetworkId;
    }

    public SecureUtils.K2Output getK2Output() {
        return this.k2Output;
    }

    public final String getNodeIdentifier() {
        return this.nodeIdentifier;
    }

    public final Integer getProductIdentifier() {
        return this.productIdentifier;
    }

    public Integer getSeqAuth(byte[] bArr) {
        if (this.mSeqAuth.size() == 0) {
            return null;
        }
        return Integer.valueOf(this.mSeqAuth.get((bArr[1] & 255) | ((bArr[0] & 255) << 8)));
    }

    public final int getSequenceNumber() {
        return this.mReceivedSequenceNumber;
    }

    @Override // meshprovisioner.BaseMeshNode
    public final int getTtl() {
        return this.ttl;
    }

    public final Integer getVersionIdentifier() {
        return this.versionIdentifier;
    }

    public final boolean isFriendFeatureSupported() {
        return this.friendFeatureSupported;
    }

    public final boolean isLowPowerFeatureSupported() {
        return this.lowPowerFeatureSupported;
    }

    public final boolean isProxyFeatureSupported() {
        return this.proxyFeatureSupported;
    }

    public final boolean isRelayFeatureSupported() {
        return this.relayFeatureSupported;
    }

    public final void setAddedAppKey(int i, String str) {
        this.mAddedAppKeys.put(Integer.valueOf(i), str);
    }

    public final void setAppKeyBindStatus(ConfigModelAppStatus configModelAppStatus) {
        if (configModelAppStatus == null || !configModelAppStatus.isSuccessful()) {
            return;
        }
        MeshModel meshModel = this.mElements.get(Integer.valueOf(configModelAppStatus.getElementAddressInt())).getMeshModels().get(Integer.valueOf(configModelAppStatus.getModelIdentifierInt()));
        int appKeyIndexInt = configModelAppStatus.getAppKeyIndexInt();
        meshModel.setBoundAppKey(appKeyIndexInt, this.mAddedAppKeys.get(Integer.valueOf(appKeyIndexInt)));
    }

    public final void setAppKeyUnbindStatus(ConfigModelAppStatus configModelAppStatus) {
        if (configModelAppStatus == null || !configModelAppStatus.isSuccessful()) {
            return;
        }
        MeshModel meshModel = this.mElements.get(Integer.valueOf(configModelAppStatus.getElementAddressInt())).getMeshModels().get(Integer.valueOf(configModelAppStatus.getModelIdentifierInt()));
        int appKeyIndexInt = configModelAppStatus.getAppKeyIndexInt();
        meshModel.removeBoundAppKey(appKeyIndexInt, this.mAddedAppKeys.get(Integer.valueOf(appKeyIndexInt)));
    }

    @Override // meshprovisioner.BaseMeshNode
    public final void setBluetoothDeviceAddress(String str) {
        this.bluetoothAddress = str;
    }

    public final void setCompositionData(ConfigCompositionDataStatus configCompositionDataStatus) {
        if (configCompositionDataStatus != null) {
            this.companyIdentifier = Integer.valueOf(configCompositionDataStatus.getCompanyIdentifier());
            this.productIdentifier = Integer.valueOf(configCompositionDataStatus.getProductIdentifier());
            this.versionIdentifier = Integer.valueOf(configCompositionDataStatus.getVersionIdentifier());
            this.crpl = Integer.valueOf(configCompositionDataStatus.getCrpl());
            this.features = Integer.valueOf(configCompositionDataStatus.getFeatures());
            this.relayFeatureSupported = configCompositionDataStatus.isRelayFeatureSupported();
            this.proxyFeatureSupported = configCompositionDataStatus.isProxyFeatureSupported();
            this.friendFeatureSupported = configCompositionDataStatus.isFriendFeatureSupported();
            this.lowPowerFeatureSupported = configCompositionDataStatus.isLowPowerFeatureSupported();
            this.mElements.putAll(configCompositionDataStatus.getElements());
        }
    }

    public void setDevId(String str) {
        this.devId = str.toLowerCase();
    }

    @Override // meshprovisioner.BaseMeshNode
    public void setDeviceKey(byte[] bArr) {
        this.deviceKey = bArr;
    }

    @Override // meshprovisioner.BaseMeshNode
    public final void setGeneratedNetworkId(byte[] bArr) {
        this.generatedNetworkId = bArr;
    }

    public final void setK2Ouput(SecureUtils.K2Output k2Output) {
        this.k2Output = k2Output;
    }

    public void setK2Output(SecureUtils.K2Output k2Output) {
        this.k2Output = k2Output;
    }

    @Override // meshprovisioner.BaseMeshNode
    public final void setNodeIdentifier(String str) {
        this.nodeIdentifier = str;
    }

    public void setSeqAuth(byte[] bArr, int i) {
        this.mSeqAuth.put(bArr[1] | ((bArr[0] << 8) & 255), i);
    }

    public final void setSequenceNumber(int i) {
        this.mReceivedSequenceNumber = i;
    }

    @Override // meshprovisioner.BaseMeshNode
    public final void setTtl(int i) {
        this.ttl = i;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte(this.isProvisioned ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isConfigured ? (byte) 1 : (byte) 0);
        parcel.writeString(this.nodeName);
        parcel.writeByteArray(this.networkKey);
        parcel.writeByteArray(this.identityKey);
        parcel.writeByteArray(this.keyIndex);
        parcel.writeByteArray(this.mFlags);
        parcel.writeByteArray(this.ivIndex);
        parcel.writeByteArray(this.unicastAddress);
        parcel.writeByteArray(this.deviceKey);
        parcel.writeInt(this.ttl);
        parcel.writeInt(this.mReceivedSequenceNumber);
        parcel.writeString(this.bluetoothAddress);
        parcel.writeParcelable(this.k2Output, i);
        parcel.writeString(this.nodeIdentifier);
        parcel.writeValue(this.companyIdentifier);
        parcel.writeValue(this.productIdentifier);
        parcel.writeValue(this.versionIdentifier);
        parcel.writeValue(this.crpl);
        parcel.writeValue(this.features);
        parcel.writeByte(this.relayFeatureSupported ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.proxyFeatureSupported ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.friendFeatureSupported ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.lowPowerFeatureSupported ? (byte) 1 : (byte) 0);
        parcel.writeByteArray(this.generatedNetworkId);
        parcel.writeMap(this.mElements);
        parcel.writeMap(this.mAddedAppKeys);
        parcel.writeList(this.mAddedAppKeyIndexes);
        parcel.writeLong(this.mTimeStampInMillis);
        parcel.writeByteArray(this.mConfigurationSrc);
        parcel.writeParcelable(this.mSeqAuth, i);
        parcel.writeInt(this.numberOfElements);
        parcel.writeString(this.devId);
    }

    public ProvisionedMeshNode(UnprovisionedMeshNode unprovisionedMeshNode) {
        this.isProvisioned = unprovisionedMeshNode.isProvisioned();
        this.isConfigured = unprovisionedMeshNode.isConfigured();
        this.nodeName = unprovisionedMeshNode.getNodeName();
        this.networkKey = unprovisionedMeshNode.getNetworkKey();
        this.identityKey = unprovisionedMeshNode.getIdentityKey();
        this.keyIndex = unprovisionedMeshNode.getKeyIndex();
        this.mFlags = unprovisionedMeshNode.getFlags();
        this.ivIndex = unprovisionedMeshNode.getIvIndex();
        this.unicastAddress = unprovisionedMeshNode.getUnicastAddress();
        this.deviceKey = unprovisionedMeshNode.getDeviceKey();
        this.ttl = unprovisionedMeshNode.getTtl();
        this.k2Output = SecureUtils.calculateK2(this.networkKey, SecureUtils.K2_MASTER_INPUT);
        this.mTimeStampInMillis = unprovisionedMeshNode.getTimeStamp();
        this.mConfigurationSrc = unprovisionedMeshNode.getConfigurationSrc();
        this.numberOfElements = unprovisionedMeshNode.getNumberOfElements();
        this.supportFastProvision = unprovisionedMeshNode.getSupportFastProvision();
        this.supportFastGattProvision = unprovisionedMeshNode.getSupportFastGattProvision();
        this.bluetoothAddress = unprovisionedMeshNode.getBluetoothAddress();
    }

    public ProvisionedMeshNode(Parcel parcel) {
        this.isProvisioned = parcel.readByte() != 0;
        this.isConfigured = parcel.readByte() != 0;
        this.nodeName = parcel.readString();
        this.networkKey = parcel.createByteArray();
        this.identityKey = parcel.createByteArray();
        this.keyIndex = parcel.createByteArray();
        this.mFlags = parcel.createByteArray();
        this.ivIndex = parcel.createByteArray();
        this.unicastAddress = parcel.createByteArray();
        this.deviceKey = parcel.createByteArray();
        this.ttl = parcel.readInt();
        this.mReceivedSequenceNumber = parcel.readInt();
        this.bluetoothAddress = parcel.readString();
        this.k2Output = (SecureUtils.K2Output) parcel.readParcelable(SecureUtils.K2Output.class.getClassLoader());
        this.nodeIdentifier = parcel.readString();
        this.companyIdentifier = (Integer) parcel.readValue(Integer.class.getClassLoader());
        this.productIdentifier = (Integer) parcel.readValue(Integer.class.getClassLoader());
        this.versionIdentifier = (Integer) parcel.readValue(Integer.class.getClassLoader());
        this.crpl = (Integer) parcel.readValue(Integer.class.getClassLoader());
        this.features = (Integer) parcel.readValue(Integer.class.getClassLoader());
        this.relayFeatureSupported = parcel.readByte() != 0;
        this.proxyFeatureSupported = parcel.readByte() != 0;
        this.friendFeatureSupported = parcel.readByte() != 0;
        this.lowPowerFeatureSupported = parcel.readByte() != 0;
        this.generatedNetworkId = parcel.createByteArray();
        sortElements(parcel.readHashMap(Element.class.getClassLoader()));
        this.mAddedAppKeys = parcel.readHashMap(String.class.getClassLoader());
        this.mAddedAppKeyIndexes = parcel.readArrayList(Integer.class.getClassLoader());
        this.mTimeStampInMillis = parcel.readLong();
        this.mConfigurationSrc = parcel.createByteArray();
        this.mSeqAuth = (SparseIntArrayParcelable) parcel.readParcelable(SparseIntArrayParcelable.class.getClassLoader());
        this.numberOfElements = parcel.readInt();
        this.devId = parcel.readString();
    }
}
