package anet.channel;

import anetwork.channel.cache.CacheManager;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
final class j implements Runnable {
    j() {
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            anet.channel.b.a aVar = new anet.channel.b.a();
            aVar.a();
            CacheManager.addCache(aVar, new k(this), 1);
        } catch (Exception unused) {
        }
    }
}
