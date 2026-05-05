package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class S implements IActionListener<Boolean> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f1230a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ MeshService f1231b;

    public S(MeshService meshService, IActionListener iActionListener) {
        this.f1231b = meshService;
        this.f1230a = iActionListener;
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(Boolean bool) {
        IActionListener iActionListener = this.f1230a;
        if (iActionListener != null) {
            iActionListener.onSuccess(bool);
        }
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onFailure(int i, String str) {
        IActionListener iActionListener = this.f1230a;
        if (iActionListener != null) {
            iActionListener.onFailure(i, str);
        }
    }
}
