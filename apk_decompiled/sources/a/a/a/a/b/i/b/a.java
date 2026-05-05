package a.a.a.a.b.i.b;

import a.a.a.a.b.G;
import a.a.a.a.b.m.c;
import android.annotation.SuppressLint;
import android.text.TextUtils;
import b.InterfaceC0370d;
import com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionV2StatusCallback;
import com.alibaba.ailabs.iot.mesh.provision.state.FastProvisioningState;
import com.alibaba.ailabs.iot.mesh.utils.AliMeshUUIDParserUtil;
import com.alibaba.fastjson.JSONObject;
import datasource.bean.ProvisionInfo;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: compiled from: FastProvisioningAddAppKeyState.java */
/* JADX INFO: loaded from: classes.dex */
public class a extends FastProvisioningState {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final ProvisionedMeshNode f1372b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final FastProvisionV2StatusCallback f1373c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final InterfaceC0370d f1374d;
    public final ProvisionInfo e;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final String f1371a = "InexpensiveMesh" + a.class.getSimpleName();
    public String f = null;

    public a(ProvisionedMeshNode provisionedMeshNode, FastProvisionV2StatusCallback fastProvisionV2StatusCallback, InterfaceC0370d interfaceC0370d, ProvisionInfo provisionInfo) {
        List<String> appKeys;
        this.f1372b = provisionedMeshNode;
        this.f1373c = fastProvisionV2StatusCallback;
        this.f1374d = interfaceC0370d;
        this.e = provisionInfo;
        if (provisionInfo.getAppKeyIndexes() == null || (appKeys = provisionInfo.getAppKeys()) == null) {
            return;
        }
        Iterator<Integer> it = provisionInfo.getAppKeyIndexes().iterator();
        while (it.hasNext()) {
            int iIntValue = it.next().intValue();
            if (iIntValue < appKeys.size()) {
                provisionedMeshNode.setAddedAppKey(iIntValue, appKeys.get(iIntValue));
            }
        }
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.state.FastProvisioningState
    public void a() {
        this.f1374d.sendPdu(this.f1372b, d());
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.state.FastProvisioningState
    public FastProvisioningState.State b() {
        return FastProvisioningState.State.PROVISIONING_ADDAPPKEY;
    }

    public String c() {
        return this.f;
    }

    public final byte[] d() {
        Integer num = this.e.getAppKeyIndexes() != null ? this.e.getAppKeyIndexes().get(0) : null;
        if (num == null) {
            num = 0;
        }
        String str = this.e.getAppKeys() != null ? this.e.getAppKeys().get(0) : ((ProvisionedMeshNode) G.a().b()).getAddedAppKeys().get(0);
        a.a.a.a.b.m.a.a(this.f1371a, "appKey = " + str);
        byte[] byteArray = MeshParserUtils.toByteArray(str);
        String strExtractMacAddressFromUUID = AliMeshUUIDParserUtil.extractMacAddressFromUUID(this.f1372b.getDevId());
        if (TextUtils.isEmpty(strExtractMacAddressFromUUID)) {
            a.a.a.a.b.m.a.d(this.f1371a, "Can not extract mac address from UUID");
            strExtractMacAddressFromUUID = this.f1372b.getBluetoothAddress();
        }
        return new a.a.a.a.a.a.b.a.a(c.a(strExtractMacAddressFromUUID), (byte) num.intValue(), byteArray, this.e.getServerConfirmation()).a();
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.state.FastProvisioningState
    @SuppressLint({"DefaultLocale"})
    public boolean a(byte[] bArr) {
        if (bArr != null && bArr.length >= 1 && bArr[0] == 10) {
            byte b2 = bArr[1];
            if (b2 == 0) {
                if (bArr.length > 7) {
                    byte[] bArrCopyOfRange = Arrays.copyOfRange(bArr, 7, bArr.length);
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("opcode", (Object) "D301A8");
                    jSONObject.put("parameters", (Object) MeshParserUtils.bytesToHex(bArrCopyOfRange, false));
                    this.f = jSONObject.toJSONString();
                }
                return true;
            }
            FastProvisionV2StatusCallback fastProvisionV2StatusCallback = this.f1373c;
            ProvisionedMeshNode provisionedMeshNode = this.f1372b;
            fastProvisionV2StatusCallback.onProvisioningFailed(provisionedMeshNode, -62, String.format("device(%s) responded that the appKey addition failed, status: %d", provisionedMeshNode.getBluetoothAddress(), Byte.valueOf(b2)));
        }
        return false;
    }
}
