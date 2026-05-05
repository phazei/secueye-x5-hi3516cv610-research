package a.a.a.a.b.i.c;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.bleadvertise.callback.BleAdvertiseCallback;
import com.alibaba.ailabs.iot.mesh.utils.Utils;

/* JADX INFO: compiled from: TinyMeshAdvTransportLayer.java */
/* JADX INFO: loaded from: classes.dex */
public class e implements BleAdvertiseCallback<Boolean> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f1388a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ g f1389b;

    public e(g gVar, IActionListener iActionListener) {
        this.f1389b = gVar;
        this.f1388a = iActionListener;
    }

    @Override // com.alibaba.ailabs.iot.bleadvertise.callback.BleAdvertiseCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(Boolean bool) {
        Utils.notifySuccess((IActionListener<Object>) this.f1388a, (Object) null);
    }

    @Override // com.alibaba.ailabs.iot.bleadvertise.callback.BleAdvertiseCallback
    public void onFailure(int i, String str) {
        Utils.notifyFailed(this.f1388a, -1, "failed to advertise network data");
    }
}
