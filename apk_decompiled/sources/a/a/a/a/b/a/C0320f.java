package a.a.a.a.b.a;

import a.a.a.a.b.m.l;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;
import java.util.List;

/* JADX INFO: renamed from: a.a.a.a.b.a.f, reason: case insensitive filesystem */
/* JADX INFO: compiled from: RequestQueue.java */
/* JADX INFO: loaded from: classes.dex */
public class C0320f implements l.a<Object> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ List f1257a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ C0321g f1258b;

    public C0320f(C0321g c0321g, List list) {
        this.f1258b = c0321g;
        this.f1257a = list;
    }

    @Override // a.a.a.a.b.m.l.a
    public Object a(l.b bVar) {
        int i = 0;
        while (i < this.f1257a.size()) {
            int i2 = i + 1;
            SIGMeshBizRequest sIGMeshBizRequest = (SIGMeshBizRequest) this.f1257a.get(i);
            this.f1258b.e.a(sIGMeshBizRequest);
            while (i2 < this.f1257a.size() && ((SIGMeshBizRequest) this.f1257a.get(i2)).n()) {
                this.f1258b.e.a(sIGMeshBizRequest);
                i2++;
            }
            int i3 = i2 - 1;
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i = i3 + 1;
        }
        return null;
    }
}
