package meshprovisioner;

import android.os.Parcelable;
import android.text.TextUtils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.jvm.internal.ByteCompanionObject;
import meshprovisioner.states.ProvisioningCapabilities;
import meshprovisioner.utils.Element;
import meshprovisioner.utils.SecureUtils;
import meshprovisioner.utils.SparseIntArrayParcelable;

/* JADX INFO: loaded from: classes4.dex */
public abstract class BaseMeshNode implements Parcelable {
    public static final String TAG = "BaseMeshNode";
    public byte[] authenticationValue;
    public String bluetoothAddress;
    public String bluetoothDeviceAddress;
    public byte[] deviceKey;
    public boolean friendFeatureSupported;
    public byte[] generatedNetworkId;
    public byte[] identityKey;
    public boolean isConfigured;
    public boolean isProvisioned;
    public byte[] ivIndex;
    public byte[] keyIndex;
    public boolean lowPowerFeatureSupported;
    public byte[] mFlags;
    public int mReceivedSequenceNumber;
    public long mTimeStampInMillis;
    public byte[] networkKey;
    public String nodeIdentifier;
    public int numberOfElements;
    public byte[] provisioneeConfirmation;
    public byte[] provisioneePublicKeyXY;
    public byte[] provisioneeRandom;
    public byte[] provisionerPublicKeyXY;
    public byte[] provisionerRandom;
    public ProvisioningCapabilities provisioningCapabilities;
    public boolean proxyFeatureSupported;
    public boolean relayFeatureSupported;
    public byte[] sharedECDHSecret;
    public byte[] unicastAddress;
    public byte[] mConfigurationSrc = {ByteCompanionObject.MAX_VALUE, -1};
    public String nodeName = "My Node";
    public int ttl = 5;
    public Integer companyIdentifier = null;
    public Integer productIdentifier = null;
    public Integer versionIdentifier = null;
    public Integer crpl = null;
    public Integer features = null;
    public final Map<Integer, Element> mElements = new LinkedHashMap();
    public List<Integer> mAddedAppKeyIndexes = new ArrayList();
    public Map<Integer, String> mAddedAppKeys = new LinkedHashMap();
    public SparseIntArrayParcelable mSeqAuth = new SparseIntArrayParcelable();
    public Set<Integer> flatSubscribeGroupAddress = new HashSet();
    public boolean supportFastProvision = false;
    public boolean supportFastGattProvision = false;
    public boolean supportAutomaticallyGenerateShareAppKey = false;

    public void addElement(int i, Element element) {
        this.mElements.put(Integer.valueOf(i), element);
        this.flatSubscribeGroupAddress.addAll(element.getFlatSubscribedGroupAddress());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getBluetoothAddress() {
        return this.bluetoothAddress;
    }

    public String getBluetoothDeviceAddress() {
        return this.bluetoothDeviceAddress;
    }

    public final byte[] getConfigurationSrc() {
        return this.mConfigurationSrc;
    }

    public byte[] getDeviceKey() {
        return this.deviceKey;
    }

    public Map<Integer, Element> getElements() {
        return this.mElements;
    }

    public final byte[] getFlags() {
        return this.mFlags;
    }

    public Set<Integer> getFlatSubscribeGroupAddress() {
        return this.flatSubscribeGroupAddress;
    }

    public final byte[] getIdentityKey() {
        return this.identityKey;
    }

    public final byte[] getIvIndex() {
        return this.ivIndex;
    }

    public final byte[] getKeyIndex() {
        return this.keyIndex;
    }

    public byte[] getNetworkKey() {
        return this.networkKey;
    }

    public final String getNodeName() {
        return this.nodeName;
    }

    public int getNumberOfElements() {
        return this.numberOfElements;
    }

    public ProvisioningCapabilities getProvisioningCapabilities() {
        return this.provisioningCapabilities;
    }

    public boolean getSupportFastGattProvision() {
        return this.supportFastGattProvision;
    }

    public boolean getSupportFastProvision() {
        return this.supportFastProvision;
    }

    public long getTimeStamp() {
        return this.mTimeStampInMillis;
    }

    public int getTtl() {
        return this.ttl;
    }

    public final byte[] getUnicastAddress() {
        return this.unicastAddress;
    }

    public final int getUnicastAddressInt() {
        byte[] bArr = this.unicastAddress;
        if (bArr == null) {
            return -1;
        }
        return ByteBuffer.wrap(bArr).order(ByteOrder.BIG_ENDIAN).getShort();
    }

    public final boolean isConfigured() {
        return this.isConfigured;
    }

    public boolean isProvisioned() {
        return this.isProvisioned;
    }

    public boolean isSupportAutomaticallyGenerateShareAppKey() {
        return this.supportAutomaticallyGenerateShareAppKey;
    }

    public void setAddedAppKeyIndexes(List<Integer> list) {
        this.mAddedAppKeyIndexes = list;
    }

    public void setAddedAppKeys(Map<Integer, String> map) {
        this.mAddedAppKeys = map;
    }

    public void setAuthenticationValue(byte[] bArr) {
        this.authenticationValue = bArr;
    }

    public void setBluetoothAddress(String str) {
        this.bluetoothAddress = str;
    }

    public void setBluetoothDeviceAddress(String str) {
        this.bluetoothDeviceAddress = str;
    }

    public void setCompanyIdentifier(Integer num) {
        this.companyIdentifier = num;
    }

    public final void setConfigurationSrc(byte[] bArr) {
        this.mConfigurationSrc = bArr;
    }

    public final void setConfigured(boolean z) {
        this.isConfigured = z;
    }

    public void setCrpl(Integer num) {
        this.crpl = num;
    }

    public void setDeviceKey(byte[] bArr) {
        this.deviceKey = bArr;
    }

    public void setElements(Map<Integer, Element> map) {
        this.mElements.clear();
        this.mElements.putAll(map);
    }

    public void setFeatures(Integer num) {
        this.features = num;
    }

    public final void setFlags(byte[] bArr) {
        this.mFlags = bArr;
    }

    public void setFriendFeatureSupported(boolean z) {
        this.friendFeatureSupported = z;
    }

    public void setGeneratedNetworkId(byte[] bArr) {
        this.generatedNetworkId = bArr;
    }

    public void setIdentityKey(byte[] bArr) {
        this.identityKey = bArr;
    }

    public final void setIsProvisioned(boolean z) {
        this.identityKey = SecureUtils.calculateIdentityKey(this.networkKey);
        this.isProvisioned = z;
    }

    public final void setIvIndex(byte[] bArr) {
        this.ivIndex = bArr;
    }

    public final void setKeyIndex(byte[] bArr) {
        this.keyIndex = bArr;
    }

    public void setLowPowerFeatureSupported(boolean z) {
        this.lowPowerFeatureSupported = z;
    }

    public void setNetworkKey(byte[] bArr) {
        this.networkKey = bArr;
    }

    public void setNodeIdentifier(String str) {
        this.nodeIdentifier = str;
    }

    public final void setNodeName(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.nodeName = str;
    }

    public void setNumberOfElements(int i) {
        this.numberOfElements = i;
    }

    public void setProductIdentifier(Integer num) {
        this.productIdentifier = num;
    }

    public void setProvisioned(boolean z) {
        this.isProvisioned = z;
    }

    public void setProvisioneeConfirmation(byte[] bArr) {
        this.provisioneeConfirmation = bArr;
    }

    public void setProvisioneePublicKeyXY(byte[] bArr) {
        this.provisioneePublicKeyXY = bArr;
    }

    public void setProvisioneeRandom(byte[] bArr) {
        this.provisioneeRandom = bArr;
    }

    public void setProvisionerPublicKeyXY(byte[] bArr) {
        this.provisionerPublicKeyXY = bArr;
    }

    public void setProvisionerRandom(byte[] bArr) {
        this.provisionerRandom = bArr;
    }

    public void setProvisioningCapabilities(ProvisioningCapabilities provisioningCapabilities) {
        this.provisioningCapabilities = provisioningCapabilities;
    }

    public void setProxyFeatureSupported(boolean z) {
        this.proxyFeatureSupported = z;
    }

    public void setRelayFeatureSupported(boolean z) {
        this.relayFeatureSupported = z;
    }

    public void setSeqAuth(SparseIntArrayParcelable sparseIntArrayParcelable) {
        this.mSeqAuth = sparseIntArrayParcelable;
    }

    public void setSharedECDHSecret(byte[] bArr) {
        this.sharedECDHSecret = bArr;
    }

    public void setSupportAutomaticallyGenerateShareAppKey(boolean z) {
        this.supportAutomaticallyGenerateShareAppKey = z;
    }

    public void setSupportFastGattProvision(boolean z) {
        this.supportFastGattProvision = z;
    }

    public void setSupportFastProvision(boolean z) {
        this.supportFastProvision = z;
    }

    public void setTimeStampInMillis(long j) {
        this.mTimeStampInMillis = j;
    }

    public void setTtl(int i) {
        this.ttl = i;
    }

    public final void setUnicastAddress(byte[] bArr) {
        this.unicastAddress = bArr;
    }

    public void setVersionIdentifier(Integer num) {
        this.versionIdentifier = num;
    }

    public void setmConfigurationSrc(byte[] bArr) {
        this.mConfigurationSrc = bArr;
    }

    public void setmFlags(byte[] bArr) {
        this.mFlags = bArr;
    }

    public void setmReceivedSequenceNumber(int i) {
        this.mReceivedSequenceNumber = i;
    }

    public void addElement(LinkedHashMap<Integer, Element> linkedHashMap) {
        this.mElements.putAll(linkedHashMap);
        Iterator<Element> it = linkedHashMap.values().iterator();
        while (it.hasNext()) {
            this.flatSubscribeGroupAddress.addAll(it.next().getFlatSubscribedGroupAddress());
        }
    }
}
