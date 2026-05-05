package anetwork.channel.unified;

import anet.channel.thread.ThreadPoolExecutorFactory;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class f implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ e f2066a;

    f(e eVar) {
        this.f2066a = eVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        ThreadPoolExecutorFactory.submitPriorityTask(this.f2066a, ThreadPoolExecutorFactory.Priority.HIGH);
    }
}
