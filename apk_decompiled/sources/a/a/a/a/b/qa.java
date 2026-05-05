package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.MeshSceneJob;
import com.alibaba.ailabs.iot.mesh.SceneTransaction;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;

/* JADX INFO: compiled from: SceneTransaction.java */
/* JADX INFO: loaded from: classes.dex */
public class qa implements IActionListener<Boolean> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ String f1532a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ SceneTransaction.SceneTransactionCallback f1533b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ MeshSceneJob f1534c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ SceneTransaction f1535d;

    public qa(SceneTransaction sceneTransaction, String str, SceneTransaction.SceneTransactionCallback sceneTransactionCallback, MeshSceneJob meshSceneJob) {
        this.f1535d = sceneTransaction;
        this.f1532a = str;
        this.f1533b = sceneTransactionCallback;
        this.f1534c = meshSceneJob;
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(Boolean bool) {
        a.a.a.a.b.m.a.a(SceneTransaction.TAG, "On successful scene delete, devId: " + this.f1532a + " ,result:" + bool.toString());
        SceneTransaction.SceneTransactionCallback sceneTransactionCallback = this.f1533b;
        if (sceneTransactionCallback != null) {
            sceneTransactionCallback.onSuccess(this.f1532a, bool);
        }
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onFailure(int i, String str) {
        this.f1534c.addDevice(this.f1532a);
        a.a.a.a.b.m.a.b(SceneTransaction.TAG, "On Failed scene delete, devId: " + this.f1532a + ", errorCode: " + i + " ,desc: " + str);
        SceneTransaction.SceneTransactionCallback sceneTransactionCallback = this.f1533b;
        if (sceneTransactionCallback != null) {
            sceneTransactionCallback.onFailure(this.f1532a, i, str);
        }
    }
}
