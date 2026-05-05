package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.SceneTransaction;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: SceneTransaction.java */
/* JADX INFO: loaded from: classes.dex */
public class ta implements IActionListener<Boolean> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ String f1545a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ SceneTransaction.SceneTransactionCallback f1546b;

    public ta(String str, SceneTransaction.SceneTransactionCallback sceneTransactionCallback) {
        this.f1545a = str;
        this.f1546b = sceneTransactionCallback;
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(Boolean bool) {
        a.a.a.a.b.m.a.a(SceneTransaction.TAG, "On successful scene unbind, devId: " + this.f1545a + " ,result: " + bool.toString());
        SceneTransaction.SceneTransactionCallback sceneTransactionCallback = this.f1546b;
        if (sceneTransactionCallback != null) {
            sceneTransactionCallback.onSuccess(this.f1545a, bool);
        }
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onFailure(int i, String str) {
        a.a.a.a.b.m.a.b(SceneTransaction.TAG, "On Failed scene unbind, devId: " + this.f1545a + " ,errorCode: " + i + " ,desc: " + str);
        SceneTransaction.SceneTransactionCallback sceneTransactionCallback = this.f1546b;
        if (sceneTransactionCallback != null) {
            sceneTransactionCallback.onFailure(this.f1545a, i, str);
        }
    }
}
