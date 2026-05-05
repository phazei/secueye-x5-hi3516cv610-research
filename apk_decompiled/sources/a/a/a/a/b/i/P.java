package a.a.a.a.b.i;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import b.u;
import com.alibaba.ailabs.iot.mesh.bean.MeshNodeStatus;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.managers.MeshDeviceInfoManager;
import com.alibaba.ailabs.iot.mesh.provision.WiFiConfigReplyParser;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import com.alibaba.fastjson.JSONObject;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import meshprovisioner.configuration.ProvisionedMeshNode;

/* JADX INFO: compiled from: WiFiConfigOverMeshLogicController.java */
/* JADX INFO: loaded from: classes.dex */
public class P {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static byte[] f1363a = {-16, 6};

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public String f1366d;
    public Map<String, Object> e;
    public IActionListener<String> f;
    public ProvisionedMeshNode g;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final String f1364b = "wifi_config_" + P.class.getName();
    public Runnable h = null;
    public final int i = com.taobao.accs.net.b.ACCS_RECEIVE_TIMEOUT;
    public WiFiConfigReplyParser j = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public Handler f1365c = new Handler(Looper.getMainLooper());

    public P(String str, Map<String, Object> map, IActionListener<String> iActionListener) {
        this.f1366d = MeshDeviceInfoManager.getInstance().coverIotIdToDevId(str);
        this.g = a.a.a.a.b.G.a().d().b(this.f1366d);
        this.e = map;
        this.f = iActionListener;
    }

    public byte[] b() {
        ProvisionedMeshNode provisionedMeshNode = this.g;
        if (provisionedMeshNode == null) {
            return null;
        }
        return provisionedMeshNode.getUnicastAddress();
    }

    public final void c() {
        if (this.j != null) {
            return;
        }
        this.j = new WiFiConfigReplyParser(new O(this));
    }

    public void d() {
        if (this.g == null) {
            Utils.notifyFailed(this.f, -53, "meshNode is null");
            return;
        }
        SIGMeshBizRequest sIGMeshBizRequestA = new SIGMeshBizRequest.a().a(SIGMeshBizRequest.Type.VENDOR_ATTRIBUTE_SET).a(this.g).a(this.g.getUnicastAddress()).a(true).a(new L(this)).a(0).a(new N(this)).a(new M(this)).a();
        LinkedList linkedList = new LinkedList();
        linkedList.add(sIGMeshBizRequestA);
        u.a aVarH = sIGMeshBizRequestA.h();
        if (aVarH == null) {
            Utils.notifyFailed(this.f, -30, "Internal error");
            return;
        }
        b.K kE = aVarH.e();
        if (kE != null) {
            kE.a(linkedList);
        } else {
            Utils.notifyFailed(this.f, -23, "Target mesh network unreachable");
        }
    }

    public void a(byte[] bArr, String str, byte[] bArr2) {
        if (!Arrays.equals(bArr, this.g.getUnicastAddress()) || !"d4a801".equalsIgnoreCase(str) || bArr2 == null || bArr2.length < 6) {
            return;
        }
        byte b2 = bArr2[0];
        if (Arrays.equals(new byte[]{bArr2[2], bArr2[1]}, f1363a)) {
            if (this.j == null) {
                c();
            }
            byte[] bArr3 = new byte[bArr2.length - 3];
            System.arraycopy(bArr2, 3, bArr3, 0, bArr3.length);
            this.j.a(bArr3);
        }
    }

    public final void a(boolean z, int i, int i2, String str) {
        a();
        if (z) {
            a.a.a.a.b.m.a.c(this.f1364b, "on successful to config Wi-Fi info");
        } else {
            a.a.a.a.b.m.a.b(this.f1364b, "on failed to config Wi-Fi info, error code: " + i + " , " + str);
        }
        Intent intent = new Intent(Utils.ACTION_PROVISIONING_STATE);
        intent.putExtra(Utils.EXTRA_PROVISIONING_STATE, MeshNodeStatus.COMBO_WIFI_CONFIG_STATUS.getState());
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("isSuccess", (Object) Boolean.valueOf(z));
        jSONObject.put("subErrorCode", (Object) Integer.valueOf(i2));
        jSONObject.put("errorMessage", (Object) str);
        intent.putExtra(Utils.EXTRA_PROVISIONING_FAIL_MSG, jSONObject.toJSONString());
        if (z) {
            Utils.notifySuccess(this.f, "");
        } else {
            Utils.notifyFailed(this.f, i, jSONObject.toJSONString());
        }
    }

    public final void a() {
        Runnable runnable = this.h;
        if (runnable != null) {
            this.f1365c.removeCallbacks(runnable);
            this.h = null;
        }
    }
}
