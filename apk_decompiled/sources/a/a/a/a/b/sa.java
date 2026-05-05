package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.SceneTransaction;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: SceneTransaction.java */
/* JADX INFO: loaded from: classes.dex */
public class sa implements IActionListener<Boolean> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ Map f1541a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ SceneTransaction.SceneTransactionCallback f1542b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ String f1543c;

    public sa(Map map, SceneTransaction.SceneTransactionCallback sceneTransactionCallback, String str) {
        this.f1541a = map;
        this.f1542b = sceneTransactionCallback;
        this.f1543c = str;
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(Boolean bool) {
        a.a.a.a.b.m.a.a(SceneTransaction.TAG, "On successful scene bind, devIdInfo:" + this.f1541a + " ,result:" + bool.toString());
        SceneTransaction.SceneTransactionCallback sceneTransactionCallback = this.f1542b;
        if (sceneTransactionCallback != null) {
            sceneTransactionCallback.onSuccess(this.f1543c, bool);
        }
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onFailure(int i, String str) {
        a.a.a.a.b.m.a.b(SceneTransaction.TAG, "On Failed scene bind, devIdInfo:" + this.f1541a + " ,errorCode: " + i + " ,desc: " + str);
        SceneTransaction.SceneTransactionCallback sceneTransactionCallback = this.f1542b;
        if (sceneTransactionCallback != null) {
            sceneTransactionCallback.onFailure(this.f1543c, i, str);
        }
    }
}
