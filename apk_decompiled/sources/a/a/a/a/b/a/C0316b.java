package a.a.a.a.b.a;

import android.util.Pair;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.utils.Utils;

/* JADX INFO: renamed from: a.a.a.a.b.a.b, reason: case insensitive filesystem */
/* JADX INFO: compiled from: DefaultExecutionDispatcher.java */
/* JADX INFO: loaded from: classes.dex */
public class C0316b implements IActionListener<Object> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ SIGMeshBizRequest f1250a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ I f1251b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ IActionListener f1252c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ C0318d f1253d;

    public C0316b(C0318d c0318d, SIGMeshBizRequest sIGMeshBizRequest, I i, IActionListener iActionListener) {
        this.f1253d = c0318d;
        this.f1250a = sIGMeshBizRequest;
        this.f1251b = i;
        this.f1252c = iActionListener;
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onFailure(int i, String str) {
        if (i == -13) {
            this.f1253d.b(this.f1250a);
        } else {
            this.f1253d.c(this.f1250a);
            Utils.notifyFailed(this.f1252c, i, str);
        }
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onSuccess(Object obj) {
        this.f1253d.c(this.f1250a);
        I i = this.f1251b;
        if (i == null) {
            Utils.notifySuccess((IActionListener<Object>) this.f1252c, obj);
            return;
        }
        Pair<Integer, ?> response = i.parseResponse(obj);
        if (response != null) {
            Integer num = (Integer) response.first;
            if (num.intValue() == 0) {
                Utils.notifySuccess((IActionListener<Object>) this.f1252c, response.second);
            } else {
                Utils.notifyFailed(this.f1252c, num.intValue(), (String) response.second);
            }
        }
    }
}
