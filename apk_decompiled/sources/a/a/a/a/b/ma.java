package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import meshprovisioner.configuration.bean.CfgMsgModelSubscriptionStatus;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class ma implements IActionListener<Object> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ Runnable f1513a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ IActionListener f1514b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ MeshService.b f1515c;

    public ma(MeshService.b bVar, Runnable runnable, IActionListener iActionListener) {
        this.f1515c = bVar;
        this.f1513a = runnable;
        this.f1514b = iActionListener;
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onFailure(int i, String str) {
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onSuccess(Object obj) {
        MeshService.this.mHandler.removeCallbacks(this.f1513a);
        if (!(obj instanceof CfgMsgModelSubscriptionStatus)) {
            Utils.notifyFailed(this.f1514b, -30, "internal error");
            return;
        }
        CfgMsgModelSubscriptionStatus cfgMsgModelSubscriptionStatus = (CfgMsgModelSubscriptionStatus) obj;
        if (cfgMsgModelSubscriptionStatus.isSuccessful()) {
            Utils.notifySuccess((IActionListener<boolean>) this.f1514b, true);
        } else {
            Utils.notifyFailed(this.f1514b, -40, CfgMsgModelSubscriptionStatus.parseStatusMessage(MeshService.this.getApplicationContext(), cfgMsgModelSubscriptionStatus.getStatus()));
        }
    }
}
