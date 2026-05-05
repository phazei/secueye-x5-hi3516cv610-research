package b;

import android.util.Pair;
import b.C0378l;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.utils.Utils;

/* JADX INFO: renamed from: b.k, reason: case insensitive filesystem */
/* JADX INFO: compiled from: MeshManagerApi.java */
/* JADX INFO: loaded from: classes.dex */
public class C0377k implements IActionListener<Object> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ C0378l.a f2189a;

    public C0377k(C0378l.a aVar) {
        this.f2189a = aVar;
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onFailure(int i, String str) {
        if (this.f2189a.f2195b != null) {
            C0378l.this.n.removeCallbacks(this.f2189a.f2195b);
        }
        Utils.notifyFailed(this.f2189a.f2196c, i, str);
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onSuccess(Object obj) {
        if (this.f2189a.f2195b != null) {
            C0378l.this.n.removeCallbacks(this.f2189a.f2195b);
        }
        if (this.f2189a.f2197d == null) {
            Utils.notifySuccess((IActionListener<Object>) this.f2189a.f2196c, obj);
            return;
        }
        Pair<Integer, ?> response = this.f2189a.f2197d.parseResponse(obj);
        if (response != null) {
            Integer num = (Integer) response.first;
            if (num.intValue() == 0) {
                Utils.notifySuccess((IActionListener<Object>) this.f2189a.f2196c, response.second);
            } else {
                Utils.notifyFailed(this.f2189a.f2196c, num.intValue(), (String) response.second);
            }
        }
    }
}
