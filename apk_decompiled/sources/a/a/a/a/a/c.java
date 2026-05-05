package a.a.a.a.a;

import com.alibaba.ailabs.iot.bleadvertise.callback.BleAdvertiseCallback;

/* JADX INFO: compiled from: AdvertiseManager.java */
/* JADX INFO: loaded from: classes.dex */
public class c implements BleAdvertiseCallback<Boolean> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ a.a.a.a.a.a.a.b.a f1176a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ g f1177b;

    public c(g gVar, a.a.a.a.a.a.a.b.a aVar) {
        this.f1177b = gVar;
        this.f1176a = aVar;
    }

    @Override // com.alibaba.ailabs.iot.bleadvertise.callback.BleAdvertiseCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(Boolean bool) {
        a.a.a.a.b.m.a.c("AdvertiseManager", "send message success, messageId = " + ((int) this.f1176a.c()) + ", networkId = " + ((int) this.f1176a.d()));
        BleAdvertiseCallback<Boolean> bleAdvertiseCallbackB = this.f1176a.b();
        if (bleAdvertiseCallbackB != null) {
            bleAdvertiseCallbackB.onSuccess(true);
        }
    }

    @Override // com.alibaba.ailabs.iot.bleadvertise.callback.BleAdvertiseCallback
    public void onFailure(int i, String str) {
        a.a.a.a.b.m.a.c("AdvertiseManager", "errorCodee = " + i + ", desc = " + str);
        this.f1177b.l = false;
        BleAdvertiseCallback<Boolean> bleAdvertiseCallbackB = this.f1176a.b();
        if (bleAdvertiseCallbackB != null) {
            bleAdvertiseCallbackB.onFailure(i, str);
        }
    }
}
