package a.a.a.a.b.i;

import com.alibaba.ailabs.iot.mesh.TgMeshManager;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;

/* JADX INFO: compiled from: WiFiConfigOverMeshLogicController.java */
/* JADX INFO: loaded from: classes.dex */
public class M implements SIGMeshBizRequest.b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ P f1360a;

    public M(P p) {
        this.f1360a = p;
    }

    @Override // com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest.b
    public byte[] getEncodedParameters() {
        return C0335a.a((String) this.f1360a.e.get("ssid"), (String) this.f1360a.e.get("password"), this.f1360a.e.containsKey(TgMeshManager.KEY_PROVISION_COMBO_MESH_WIFI_BSSID) ? (String) this.f1360a.e.get(TgMeshManager.KEY_PROVISION_COMBO_MESH_WIFI_BSSID) : null, this.f1360a.e.get(TgMeshManager.KEY_PROVISION_COMBO_MESH_WIFI_REGION_INDEX) != null ? ((Byte) this.f1360a.e.get(TgMeshManager.KEY_PROVISION_COMBO_MESH_WIFI_REGION_INDEX)).byteValue() : (byte) 0);
    }
}
