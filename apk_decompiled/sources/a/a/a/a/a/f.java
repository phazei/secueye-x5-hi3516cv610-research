package a.a.a.a.a;

import a.a.a.a.a.g;
import com.alibaba.ailabs.iot.bleadvertise.callback.BleAdvertiseCallback;

/* JADX INFO: compiled from: AdvertiseManager.java */
/* JADX INFO: loaded from: classes.dex */
public class f implements BleAdvertiseCallback<Boolean> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ g.a f1181a;

    public f(g.a aVar) {
        this.f1181a = aVar;
    }

    @Override // com.alibaba.ailabs.iot.bleadvertise.callback.BleAdvertiseCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(Boolean bool) {
        a.a.a.a.b.m.a.c("AdvertiseManager", "sendSinglePayloadTask: onSuccess");
        if (this.f1181a.f1188c != null && this.f1181a.f1189d == this.f1181a.f1188c.size() - 1) {
            g.a.c(this.f1181a);
            if (this.f1181a.f == 1) {
                this.f1181a.e.onSuccess(true);
            }
        }
        if (this.f1181a.f1188c == null || this.f1181a.f1188c.size() <= 1) {
            return;
        }
        g.this.e.postDelayed(this.f1181a, 200L);
    }

    @Override // com.alibaba.ailabs.iot.bleadvertise.callback.BleAdvertiseCallback
    public void onFailure(int i, String str) {
        a.a.a.a.b.m.a.c("AdvertiseManager", "sendSinglePayloadTask: errorCode " + i + ", desc " + str);
    }
}
