package anetwork.channel.unified;

import anetwork.channel.Response;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class d implements Future<Response> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private k f2057a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f2058b;

    @Override // java.util.concurrent.Future
    public /* synthetic */ Response get(long j, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        return b();
    }

    public d(k kVar) {
        this.f2057a = kVar;
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean z) {
        if (!this.f2058b) {
            this.f2057a.b();
            this.f2058b = true;
        }
        return true;
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return this.f2058b;
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        throw new RuntimeException("NOT SUPPORT!");
    }

    @Override // java.util.concurrent.Future
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public Response get() throws ExecutionException, InterruptedException {
        throw new RuntimeException("NOT SUPPORT!");
    }

    public Response b() throws ExecutionException, InterruptedException, TimeoutException {
        throw new RuntimeException("NOT SUPPORT!");
    }
}
