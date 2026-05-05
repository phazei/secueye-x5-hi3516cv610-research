package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.MeshSceneJob;
import com.alibaba.ailabs.iot.mesh.SceneTransaction;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;

/* JADX INFO: compiled from: SceneTransaction.java */
/* JADX INFO: loaded from: classes.dex */
public class pa implements IActionListener<Boolean> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ String f1526a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ SceneTransaction.SceneTransactionCallback f1527b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ MeshSceneJob f1528c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ SceneTransaction f1529d;

    public pa(SceneTransaction sceneTransaction, String str, SceneTransaction.SceneTransactionCallback sceneTransactionCallback, MeshSceneJob meshSceneJob) {
        this.f1529d = sceneTransaction;
        this.f1526a = str;
        this.f1527b = sceneTransactionCallback;
        this.f1528c = meshSceneJob;
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(Boolean bool) {
        a.a.a.a.b.m.a.a(SceneTransaction.TAG, "On successful scene store, devId: " + this.f1526a + " ,result:" + bool.toString());
        SceneTransaction.SceneTransactionCallback sceneTransactionCallback = this.f1527b;
        if (sceneTransactionCallback != null) {
            sceneTransactionCallback.onSuccess(this.f1526a, bool);
        }
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onFailure(int i, String str) {
        this.f1528c.addDevice(this.f1526a);
        a.a.a.a.b.m.a.b(SceneTransaction.TAG, "On Failed scene store, devId: " + this.f1526a + ", errorCode: " + i + " ,desc: " + str);
        SceneTransaction.SceneTransactionCallback sceneTransactionCallback = this.f1527b;
        if (sceneTransactionCallback != null) {
            sceneTransactionCallback.onFailure(this.f1526a, i, str);
        }
    }
}
