package a.a.a.a.b.a;

import b.C0378l;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import java.util.Locale;
import meshprovisioner.utils.AddressUtils;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: renamed from: a.a.a.a.b.a.d, reason: case insensitive filesystem */
/* JADX INFO: compiled from: DefaultExecutionDispatcher.java */
/* JADX INFO: loaded from: classes.dex */
public class C0318d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public C0378l f1255a;

    public C0318d(C0378l c0378l) {
        this.f1255a = c0378l;
    }

    public void a(SIGMeshBizRequest sIGMeshBizRequest) {
        boolean zA;
        if (sIGMeshBizRequest == null) {
            a.a.a.a.b.m.a.b("DefaultExecutionDispatcher", "Execute null request");
            return;
        }
        a.a.a.a.b.m.a.a("DefaultExecutionDispatcher", String.format(Locale.US, "Execute request(to %s)", Utils.bytes2HexString(sIGMeshBizRequest.j())));
        SIGMeshBizRequest.Type typeL = sIGMeshBizRequest.l();
        IActionListener iActionListenerM = sIGMeshBizRequest.m();
        byte[] bArrJ = sIGMeshBizRequest.j();
        SIGMeshBizRequest.InteractionModel interactionModel = typeL.getInteractionModel();
        if (AddressUtils.isValidGroupAddress(bArrJ)) {
            a.a.a.a.b.m.a.c("DefaultExecutionDispatcher", "Dst address is group address, fire and forget");
            interactionModel = SIGMeshBizRequest.InteractionModel.FIRE_AND_FORGET;
        }
        C0378l c0378lI = this.f1255a;
        if (sIGMeshBizRequest.i() != null) {
            c0378lI = sIGMeshBizRequest.i();
        }
        if (c0378lI == null) {
            return;
        }
        int i = C0317c.f1254a[interactionModel.ordinal()];
        if (i != 1) {
            if (i != 2) {
                return;
            }
            I iG = sIGMeshBizRequest.g();
            sIGMeshBizRequest.o();
            c0378lI.a(sIGMeshBizRequest.f, sIGMeshBizRequest.d() != null ? MeshParserUtils.bytesToHex(sIGMeshBizRequest.d(), false) : null, bArrJ, sIGMeshBizRequest.f(), sIGMeshBizRequest.e(), sIGMeshBizRequest.c(), new C0316b(this, sIGMeshBizRequest, iG, iActionListenerM));
            return;
        }
        if (sIGMeshBizRequest.d() != null) {
            c0378lI.a(sIGMeshBizRequest.f, typeL.isAccess(), MeshParserUtils.bytesToHex(sIGMeshBizRequest.d(), false), bArrJ, false, -1, typeL.getOpcode(), sIGMeshBizRequest.c());
            zA = true;
        } else {
            zA = c0378lI.a(sIGMeshBizRequest.f, typeL.isAccess(), bArrJ, typeL.getOpcode(), sIGMeshBizRequest.c());
        }
        if (zA) {
            Utils.notifySuccess((IActionListener<boolean>) iActionListenerM, true);
        } else {
            Utils.notifyFailed(iActionListenerM, -21, "Parameters appKeys are not passed");
        }
        try {
            Thread.sleep(200L);
            c(sIGMeshBizRequest);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void b(SIGMeshBizRequest sIGMeshBizRequest) {
        Utils.notifyFailed(sIGMeshBizRequest.m(), -13, "Timeout! the device is not reply");
    }

    public void c(SIGMeshBizRequest sIGMeshBizRequest) {
    }
}
