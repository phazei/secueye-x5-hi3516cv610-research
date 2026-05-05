package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.SceneTransaction;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: SceneTransaction.java */
/* JADX INFO: loaded from: classes.dex */
public class ua implements IActionListener<Boolean> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ Map f1548a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ SceneTransaction.SceneTransactionCallback f1549b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ String f1550c;

    public ua(Map map, SceneTransaction.SceneTransactionCallback sceneTransactionCallback, String str) {
        this.f1548a = map;
        this.f1549b = sceneTransactionCallback;
        this.f1550c = str;
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(Boolean bool) {
        a.a.a.a.b.m.a.a(SceneTransaction.TAG, "On successful scene unbind, devId: " + this.f1548a + " ,result: " + bool.toString());
        SceneTransaction.SceneTransactionCallback sceneTransactionCallback = this.f1549b;
        if (sceneTransactionCallback != null) {
            sceneTransactionCallback.onSuccess(this.f1550c, bool);
        }
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onFailure(int i, String str) {
        a.a.a.a.b.m.a.b(SceneTransaction.TAG, "On Failed scene unbind, devId: " + this.f1548a + " ,errorCode: " + i + " ,desc: " + str);
        SceneTransaction.SceneTransactionCallback sceneTransactionCallback = this.f1549b;
        if (sceneTransactionCallback != null) {
            sceneTransactionCallback.onFailure(this.f1550c, i, str);
        }
    }
}
