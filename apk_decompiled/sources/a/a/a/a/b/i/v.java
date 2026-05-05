package a.a.a.a.b.i;

import com.alibaba.ailabs.iot.bleadvertise.callback.BleAdvertiseCallback;
import meshprovisioner.BaseMeshNode;
import meshprovisioner.states.UnprovisionedMeshNode;

/* JADX INFO: compiled from: FastProvisionWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class v implements BleAdvertiseCallback<Boolean> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BaseMeshNode f1443a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ byte[] f1444b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ J f1445c;

    public v(J j, BaseMeshNode baseMeshNode, byte[] bArr) {
        this.f1445c = j;
        this.f1443a = baseMeshNode;
        this.f1444b = bArr;
    }

    @Override // com.alibaba.ailabs.iot.bleadvertise.callback.BleAdvertiseCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(Boolean bool) {
        a.a.a.a.b.m.a.c(this.f1445c.f1354a, "send control msg success");
        this.f1445c.u.onFastProvisionDataSend(this.f1443a, this.f1444b);
        BaseMeshNode baseMeshNode = this.f1443a;
        if (baseMeshNode instanceof UnprovisionedMeshNode) {
            this.f1445c.j = (UnprovisionedMeshNode) baseMeshNode;
        }
        J j = this.f1445c;
        j.a(j.f1355b);
    }

    @Override // com.alibaba.ailabs.iot.bleadvertise.callback.BleAdvertiseCallback
    public void onFailure(int i, String str) {
        a.a.a.a.b.m.a.c(this.f1445c.f1354a, "send control msg failed, errorCode: " + i + ", desc: " + str);
    }
}
