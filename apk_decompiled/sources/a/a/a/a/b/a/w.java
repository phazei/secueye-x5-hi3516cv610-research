package a.a.a.a.b.a;

import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.utils.Utils;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: SIGMeshBizRequestGenerator.java */
/* JADX INFO: loaded from: classes.dex */
public class w implements IActionListener<Object> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f1283a;

    public w(IActionListener iActionListener) {
        this.f1283a = iActionListener;
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onFailure(int i, String str) {
        Utils.notifyFailed(this.f1283a, i, str);
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onSuccess(Object obj) {
        Utils.notifySuccess((IActionListener<boolean>) this.f1283a, true);
    }
}
