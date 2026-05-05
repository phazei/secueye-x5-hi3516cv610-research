package a.a.a.a.b.k;

import com.alibaba.ailabs.iot.bleadvertise.callback.BleAdvertiseCallback;
import meshprovisioner.BaseMeshNode;

/* JADX INFO: compiled from: TinyMeshMessageAdvSender.java */
/* JADX INFO: loaded from: classes.dex */
public class b implements BleAdvertiseCallback<Boolean> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BaseMeshNode f1468a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ byte[] f1469b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ d f1470c;

    public b(d dVar, BaseMeshNode baseMeshNode, byte[] bArr) {
        this.f1470c = dVar;
        this.f1468a = baseMeshNode;
        this.f1469b = bArr;
    }

    @Override // com.alibaba.ailabs.iot.bleadvertise.callback.BleAdvertiseCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(Boolean bool) {
        a.a.a.a.b.m.a.c(d.f1472a, "send control msg success");
        this.f1470c.f1474c.onFastProvisionDataSend(this.f1468a, this.f1469b);
        d dVar = this.f1470c;
        dVar.a(dVar.f1473b);
    }

    @Override // com.alibaba.ailabs.iot.bleadvertise.callback.BleAdvertiseCallback
    public void onFailure(int i, String str) {
        a.a.a.a.b.m.a.c(d.f1472a, "send control msg failed, errorCode: " + i + ", desc: " + str);
    }
}
