package anet.channel.c;

import com.taobao.orange.OrangeConfigListenerV1;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class b implements OrangeConfigListenerV1 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ a f1680a;

    b(a aVar) {
        this.f1680a = aVar;
    }

    public void onConfigUpdate(String str, boolean z) {
        this.f1680a.onConfigUpdate(str);
    }
}
