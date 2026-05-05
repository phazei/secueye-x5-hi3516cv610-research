package a.a.a.a.b.i.b;

import a.a.a.a.a.a.b.a.d;
import a.a.a.a.b.m.c;
import android.text.TextUtils;
import b.InterfaceC0370d;
import com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionV2StatusCallback;
import com.alibaba.ailabs.iot.mesh.provision.state.FastProvisioningState;
import com.alibaba.ailabs.iot.mesh.utils.AliMeshUUIDParserUtil;
import datasource.bean.ProvisionInfo;
import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;
import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.states.UnprovisionedMeshNode;
import meshprovisioner.utils.AddressUtils;
import meshprovisioner.utils.MeshParserUtils;
import meshprovisioner.utils.SecureUtils;

/* JADX INFO: compiled from: FastProvisioningDataState.java */
/* JADX INFO: loaded from: classes.dex */
public class b extends FastProvisioningState {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final String f1375a = "InexpensiveMesh" + b.class.getSimpleName();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final UnprovisionedMeshNode f1376b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final FastProvisionV2StatusCallback f1377c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final InterfaceC0370d f1378d;
    public final ProvisionInfo e;

    public b(UnprovisionedMeshNode unprovisionedMeshNode, FastProvisionV2StatusCallback fastProvisionV2StatusCallback, InterfaceC0370d interfaceC0370d, ProvisionInfo provisionInfo) {
        this.f1376b = unprovisionedMeshNode;
        this.f1377c = fastProvisionV2StatusCallback;
        this.f1378d = interfaceC0370d;
        this.e = provisionInfo;
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.state.FastProvisioningState
    public void a() {
        d();
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.state.FastProvisioningState
    public FastProvisioningState.State b() {
        return FastProvisioningState.State.PROVISINING_DATA;
    }

    public final byte[] c() {
        String strExtractMacAddressFromUUID = AliMeshUUIDParserUtil.extractMacAddressFromUUID(MeshParserUtils.bytesToHex(this.f1376b.getServiceData(), false));
        if (TextUtils.isEmpty(strExtractMacAddressFromUUID)) {
            a.a.a.a.b.m.a.d(this.f1375a, "Can not extract mac address from UUID");
            strExtractMacAddressFromUUID = this.f1376b.getBluetoothAddress();
        }
        d dVar = new d(c.a(strExtractMacAddressFromUUID), (byte) 0, this.f1376b.getNetworkKey(), this.f1376b.getIvIndex()[0], AddressUtils.getUnicastAddressBytes(this.e.getPrimaryUnicastAddress().intValue()), this.e.getServerConfirmation());
        if (dVar.b()) {
            return dVar.a();
        }
        return null;
    }

    public final void d() {
        FastProvisionV2StatusCallback fastProvisionV2StatusCallback;
        byte[] bArrC = c();
        if (bArrC != null || (fastProvisionV2StatusCallback = this.f1377c) == null) {
            this.f1378d.sendPdu(this.f1376b, bArrC);
        } else {
            fastProvisionV2StatusCallback.onProvisioningFailed(this.f1376b, -60, "failed to generate encrypted provision data");
        }
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.state.FastProvisioningState
    public boolean a(byte[] bArr) {
        if (bArr == null || bArr.length < 1 || bArr[0] != 3) {
            return false;
        }
        ProvisionedMeshNode provisionedMeshNode = new ProvisionedMeshNode(this.f1376b);
        provisionedMeshNode.setDeviceKey(a(this.e.getServerConfirmation()));
        provisionedMeshNode.setUnicastAddress(AddressUtils.getUnicastAddressBytes(this.e.getPrimaryUnicastAddress().intValue()));
        FastProvisionV2StatusCallback fastProvisionV2StatusCallback = this.f1377c;
        if (fastProvisionV2StatusCallback != null) {
            fastProvisionV2StatusCallback.onProvisioningComplete(provisionedMeshNode);
        }
        return true;
    }

    public final byte[] a(String str) {
        try {
            byte[] bArrCalculateSha256 = SecureUtils.calculateSha256((str + "DeviceKey").getBytes("ASCII"));
            if (bArrCalculateSha256 == null || bArrCalculateSha256.length < 16) {
                return null;
            }
            byte[] bArr = new byte[16];
            System.arraycopy(bArrCalculateSha256, 0, bArr, 0, bArr.length);
            return bArr;
        } catch (UnsupportedEncodingException | UnsupportedCharsetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
