package meshprovisioner.configuration;

import android.content.Context;
import b.InterfaceC0369c;
import b.d.a;
import b.d.b;
import b.d.c;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import meshprovisioner.configuration.MeshMessageState;
import meshprovisioner.models.SigModelParser;
import meshprovisioner.models.VendorModel;
import meshprovisioner.utils.AddressUtils;
import meshprovisioner.utils.DeviceFeatureUtils;
import meshprovisioner.utils.Element;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: loaded from: classes4.dex */
public final class ConfigCompositionDataStatus extends ConfigMessageState {
    public static final String TAG = "ConfigCompositionDataStatus";
    public int companyIdentifier;
    public int crpl;
    public int features;
    public boolean friendFeatureSupported;
    public boolean lowPowerFeatureSupported;
    public Map<Integer, Element> mElements;
    public int mUnicastAddress;
    public int productIdentifier;
    public boolean proxyFeatureSupported;
    public boolean relayFeatureSupported;
    public int versionIdentifier;

    public ConfigCompositionDataStatus(Context context, ProvisionedMeshNode provisionedMeshNode, InterfaceC0369c interfaceC0369c) {
        super(context, provisionedMeshNode, interfaceC0369c);
        this.mElements = new LinkedHashMap();
    }

    private void pareCompositionDataPages(a aVar, int i) {
        byte[] bArrU = aVar.u();
        this.companyIdentifier = (bArrU[3] << 8) | bArrU[2];
        a.a.a.a.b.m.a.a(TAG, "Company identifier: " + String.format(Locale.US, "%04X", Integer.valueOf(this.companyIdentifier)));
        this.productIdentifier = (bArrU[5] << 8) | bArrU[4];
        a.a.a.a.b.m.a.a(TAG, "Product identifier: " + String.format(Locale.US, "%04X", Integer.valueOf(this.productIdentifier)));
        this.versionIdentifier = (bArrU[7] << 8) | bArrU[6];
        a.a.a.a.b.m.a.a(TAG, "Version identifier: " + String.format(Locale.US, "%04X", Integer.valueOf(this.versionIdentifier)));
        this.crpl = (bArrU[9] << 8) | bArrU[8];
        a.a.a.a.b.m.a.a(TAG, "crpl: " + String.format(Locale.US, "%04X", Integer.valueOf(this.crpl)));
        this.features = (bArrU[11] << 8) | bArrU[10];
        a.a.a.a.b.m.a.a(TAG, "Features: " + String.format(Locale.US, "%04X", Integer.valueOf(this.features)));
        this.relayFeatureSupported = DeviceFeatureUtils.supportsRelayFeature(this.features);
        a.a.a.a.b.m.a.a(TAG, "Relay feature: " + this.relayFeatureSupported);
        this.proxyFeatureSupported = DeviceFeatureUtils.supportsProxyFeature(this.features);
        a.a.a.a.b.m.a.a(TAG, "Proxy feature: " + this.proxyFeatureSupported);
        this.friendFeatureSupported = DeviceFeatureUtils.supportsFriendFeature(this.features);
        a.a.a.a.b.m.a.a(TAG, "Friend feature: " + this.friendFeatureSupported);
        this.lowPowerFeatureSupported = DeviceFeatureUtils.supportsLowPowerFeature(this.features);
        a.a.a.a.b.m.a.a(TAG, "Low power feature: " + this.lowPowerFeatureSupported);
        parseElements(bArrU, aVar.r(), 12);
        a.a.a.a.b.m.a.a(TAG, "Number of elements: " + this.mElements.size());
    }

    private int parseCompanyIdentifier(short s) {
        return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(s).getShort(0);
    }

    private int parseCrpl(short s) {
        return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(s).getShort(0);
    }

    private void parseElements(byte[] bArr, byte[] bArr2, int i) {
        ConfigCompositionDataStatus configCompositionDataStatus = this;
        int i2 = 0;
        byte[] bArr3 = null;
        int i3 = i;
        while (i3 < bArr.length) {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            int i4 = ((bArr[i3 + 1] & 255) << 8) | bArr[i3];
            a.a.a.a.b.m.a.a(TAG, "Location identifier: " + String.format(Locale.US, "%04X", Integer.valueOf(i4)));
            int i5 = i3 + 2;
            byte b2 = bArr[i5];
            a.a.a.a.b.m.a.a(TAG, "Number of sig models: " + String.format(Locale.US, "%04X", Integer.valueOf(b2)));
            int i6 = i5 + 1;
            byte b3 = bArr[i6];
            a.a.a.a.b.m.a.a(TAG, "Number of vendor models: " + String.format(Locale.US, "%04X", Integer.valueOf(b3)));
            i3 = i6 + 1;
            if (b2 > 0) {
                int i7 = i3;
                for (int i8 = 0; i8 < b2; i8++) {
                    int i9 = ((bArr[i7 + 1] & 255) << 8) | (bArr[i7] & 255);
                    linkedHashMap.put(Integer.valueOf(i9), SigModelParser.getSigModel(i9));
                    a.a.a.a.b.m.a.a(TAG, "Sig model ID " + i8 + " : " + String.format(Locale.US, "%04X", Integer.valueOf(i9)));
                    i7 += 2;
                }
                i3 = i7;
            }
            if (b3 > 0) {
                for (int i10 = 0; i10 < b3; i10++) {
                    int i11 = ((bArr[i3 + 1] & 255) << 24) | ((bArr[i3] & 255) << 16) | ((bArr[i3 + 3] & 255) << 8) | (bArr[i3 + 2] & 255);
                    linkedHashMap.put(Integer.valueOf(i11), new VendorModel(i11));
                    a.a.a.a.b.m.a.a(TAG, "Vendor - model ID " + i10 + " : " + String.format(Locale.US, "%08X", Integer.valueOf(i11)));
                    i3 += 4;
                }
            }
            byte[] bArrIncrementUnicastAddress = i2 == 0 ? bArr2 : AddressUtils.incrementUnicastAddress(bArr3);
            i2++;
            Element element = new Element(bArrIncrementUnicastAddress, i4, b2, b3, linkedHashMap);
            int unicastAddressInt = AddressUtils.getUnicastAddressInt(bArrIncrementUnicastAddress);
            this.mElements.put(Integer.valueOf(unicastAddressInt), element);
            this.mUnicastAddress = unicastAddressInt;
            bArr3 = bArrIncrementUnicastAddress;
            configCompositionDataStatus = this;
        }
    }

    private int parseFeatures(short s) {
        return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(s).getShort(0);
    }

    private int parseProductIdentifier(short s) {
        return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(s).getShort(0);
    }

    private int parseVersionIdentifier(short s) {
        return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(s).getShort(0);
    }

    public int getCompanyIdentifier() {
        return this.companyIdentifier;
    }

    public int getCrpl() {
        return this.crpl;
    }

    public Map<Integer, Element> getElements() {
        return this.mElements;
    }

    public int getFeatures() {
        return this.features;
    }

    public int getProductIdentifier() {
        return this.productIdentifier;
    }

    @Override // meshprovisioner.configuration.ConfigMessageState, meshprovisioner.configuration.MeshMessageState
    public MeshMessageState.MessageState getState() {
        return MeshMessageState.MessageState.COMPOSITION_DATA_STATUS_STATE;
    }

    public int getUnicastAddress() {
        return this.mUnicastAddress;
    }

    public int getVersionIdentifier() {
        return this.versionIdentifier;
    }

    public boolean isFriendFeatureSupported() {
        return this.friendFeatureSupported;
    }

    public boolean isLowPowerFeatureSupported() {
        return this.lowPowerFeatureSupported;
    }

    public boolean isProxyFeatureSupported() {
        return this.proxyFeatureSupported;
    }

    public boolean isRelayFeatureSupported() {
        return this.relayFeatureSupported;
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public final boolean parseMeshPdu(byte[] bArr) {
        c pdu = this.mMeshTransport.parsePdu(this.mSrc, bArr);
        if (pdu == null) {
            a.a.a.a.b.m.a.a(TAG, "Message reassembly may not be complete yet");
            return false;
        }
        if (!(pdu instanceof a)) {
            parseControlMessage((b) pdu, this.mPayloads.size());
            return false;
        }
        a aVar = (a) pdu;
        if (aVar.n() != 2) {
            this.mMeshStatusCallbacks.onUnknownPduReceived(this.mProvisionedMeshNode);
            return false;
        }
        a.a.a.a.b.m.a.a(TAG, "Received composition data status");
        pareCompositionDataPages(aVar, 2);
        this.mProvisionedMeshNode.setCompositionData(this);
        this.mMeshStatusCallbacks.onCompositionDataStatusReceived(this.mProvisionedMeshNode);
        this.mInternalTransportCallbacks.a(this.mProvisionedMeshNode);
        return true;
    }

    @Override // b.f.f
    public final void sendSegmentAcknowledgementMessage(b bVar) {
        b bVarCreateSegmentBlockAcknowledgementMessage = this.mMeshTransport.createSegmentBlockAcknowledgementMessage(bVar);
        a.a.a.a.b.m.a.a(TAG, "Sending acknowledgement: " + MeshParserUtils.bytesToHex(bVarCreateSegmentBlockAcknowledgementMessage.m().get(0), false));
        this.mInternalTransportCallbacks.sendPdu(this.mProvisionedMeshNode, bVarCreateSegmentBlockAcknowledgementMessage.m().get(0));
        this.mMeshStatusCallbacks.onBlockAcknowledgementSent(this.mProvisionedMeshNode);
    }
}
