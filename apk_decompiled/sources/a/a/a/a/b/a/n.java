package a.a.a.a.b.a;

import com.alibaba.ailabs.iot.mesh.callback.IActionListener;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: SIGMeshBizRequestGenerator.java */
/* JADX INFO: loaded from: classes.dex */
public class n implements IActionListener<Boolean> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f1272a;

    public n(IActionListener iActionListener) {
        this.f1272a = iActionListener;
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(Boolean bool) {
        IActionListener iActionListener = this.f1272a;
        if (iActionListener != null) {
            iActionListener.onSuccess(bool);
        }
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onFailure(int i, String str) {
        IActionListener iActionListener = this.f1272a;
        if (iActionListener != null) {
            iActionListener.onFailure(i, str);
        }
    }
}
