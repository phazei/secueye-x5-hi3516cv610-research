package a.a.a.a.b;

import android.os.Handler;
import android.os.Looper;
import b.u;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequestGenerator;
import com.alibaba.ailabs.iot.mesh.managers.MeshDeviceInfoManager;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import com.alibaba.android.multiendinonebridge.IoTMultiendInOneBridge;
import com.alibaba.android.multiendinonebridge.SigMeshNetworkParameters;
import datasource.bean.SigmeshIotDev;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: renamed from: a.a.a.a.b.d, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BleMeshHeartReportManager.java */
/* JADX INFO: loaded from: classes.dex */
public class C0329d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Handler f1313a;

    /* JADX INFO: renamed from: a.a.a.a.b.d$a */
    /* JADX INFO: compiled from: BleMeshHeartReportManager.java */
    private static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final C0329d f1314a = new C0329d(null);
    }

    public /* synthetic */ C0329d(C0328c c0328c) {
        this();
    }

    public static C0329d a() {
        return a.f1314a;
    }

    public void b() {
        a.a.a.a.b.m.a.c("BleMeshHeartReportManager", "updateMeshNetworkParameters");
        ConcurrentHashMap<String, SigmeshIotDev> sigmeshIotDevMap = MeshDeviceInfoManager.getInstance().getSigmeshIotDevMap();
        if (sigmeshIotDevMap == null || sigmeshIotDevMap.size() <= 0) {
            return;
        }
        SIGMeshBizRequest.NetworkParameter networkParameter = SIGMeshBizRequest.NetworkParameter.getNetworkParameter(sigmeshIotDevMap.size());
        u.a aVarD = G.a().d().d();
        SigMeshNetworkParameters sigMeshNetworkParameters = new SigMeshNetworkParameters();
        int i = 0;
        sigMeshNetworkParameters.ct_enable = networkParameter.cr_enable == 1;
        sigMeshNetworkParameters.send_ttl = (byte) 3;
        sigMeshNetworkParameters.group_delay_min = (byte) networkParameter.group_delay_min;
        sigMeshNetworkParameters.group_delay_max = (byte) networkParameter.group_delay_max;
        sigMeshNetworkParameters.boot_interval = (byte) networkParameter.boot_interval;
        sigMeshNetworkParameters.adv_duration = (byte) networkParameter.adv_duration;
        sigMeshNetworkParameters.per_interval = (byte) networkParameter.per_interval;
        IoTMultiendInOneBridge.getInstance().updateSigMeshNetworkParameter(MeshParserUtils.bytesToHex(aVarD.d(), false), sigMeshNetworkParameters);
        LinkedList linkedList = new LinkedList();
        while (true) {
            int i2 = 2;
            if (i >= 2) {
                a(linkedList);
                return;
            }
            int i3 = i == 0 ? 5 : 3;
            if (i == 0) {
                i2 = 5;
            }
            networkParameter.setSend_ttl(i3);
            linkedList.add(SIGMeshBizRequestGenerator.a(networkParameter.getParameter(), i2, new C0328c(this)));
            i++;
        }
    }

    public C0329d() {
        this.f1313a = new Handler(Looper.myLooper());
    }

    public void a(List<SIGMeshBizRequest> list) {
        b.K kE;
        a.a.a.a.b.m.a.c("BleMeshHeartReportManager", "offerBizRequest");
        if (list == null || list.size() == 0) {
            return;
        }
        u.a aVarH = list.get(0).h();
        if (aVarH != null && (kE = aVarH.e()) != null) {
            kE.a(list);
            return;
        }
        Iterator<SIGMeshBizRequest> it = list.iterator();
        while (it.hasNext()) {
            Utils.notifyFailed(it.next().m(), -30, "Internal error");
        }
    }
}
