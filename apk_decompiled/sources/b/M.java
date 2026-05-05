package b;

import android.text.TextUtils;
import b.K;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManager;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import com.huawei.agconnect.exception.AGCServerException;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: compiled from: SubnetsBiz.java */
/* JADX INFO: loaded from: classes.dex */
public class M implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ K.b f2123a;

    public M(K.b bVar) {
        this.f2123a = bVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        SIGMeshBizRequest sIGMeshBizRequest;
        K.d dVar;
        LinkedList linkedList = new LinkedList();
        for (Map.Entry entry : K.this.f.entrySet()) {
            if (((K.d) entry.getValue()).d()) {
                linkedList.add(entry.getValue());
            }
        }
        this.f2123a.a(linkedList.size() * 4);
        if (linkedList.size() == 1) {
            a.a.a.a.b.a.K.f1248c = AGCServerException.AUTHENTICATION_INVALID;
        } else {
            a.a.a.a.b.a.K.f1248c = 200;
        }
        this.f2123a.g = 0;
        K.b bVar = this.f2123a;
        bVar.f = Math.min(a.a.a.a.b.a.K.f1247b, K.this.C.size());
        if (this.f2123a.f == 0) {
            a.a.a.a.b.m.a.d(this.f2123a.j, "interrupting the dispatch biz-request process, there may be no proxy connections currently available");
            this.f2123a.h = false;
            while (!K.this.C.isEmpty()) {
                Utils.notifyFailed(((SIGMeshBizRequest) K.this.C.poll()).m(), -23, "Unreachable");
            }
            return;
        }
        int size = 0;
        for (int i = 0; i < this.f2123a.f && K.this.C.size() > 0; i++) {
            synchronized (K.this.C) {
                sIGMeshBizRequest = (SIGMeshBizRequest) K.this.C.poll();
            }
            if (sIGMeshBizRequest != null) {
                String strK = sIGMeshBizRequest.k();
                if (!TextUtils.isEmpty(strK) && (dVar = (K.d) K.this.f.get(strK.toUpperCase())) != null && dVar.f2116a.getWriteReadType() == BleMeshManager.WriteReadType.WRITE) {
                    dVar.a(BleMeshManager.WriteReadType.WRITE_AND_READ);
                    sIGMeshBizRequest.a(new L(this, strK, dVar));
                }
                K.d dVar2 = (K.d) linkedList.get(size);
                sIGMeshBizRequest.a(dVar2.f2117b);
                size = (size + 1) % linkedList.size();
                if (i != 0) {
                    try {
                        Thread.sleep(a.a.a.a.b.a.K.f1248c);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                a.a.a.a.b.m.a.a(this.f2123a.j, String.format(Locale.US, "Execute request(to %s) via proxy node: %s", Utils.bytes2HexString(sIGMeshBizRequest.j()), dVar2.f2119d.getAddress()));
                this.f2123a.a(sIGMeshBizRequest);
            }
        }
    }
}
