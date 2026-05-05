package a.a.a.a.b.m;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.codec.language.Soundex;

/* JADX INFO: compiled from: PriorityThreadFactory.java */
/* JADX INFO: loaded from: classes.dex */
public class i implements ThreadFactory {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final int f1501a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final AtomicInteger f1502b = new AtomicInteger();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final String f1503c;

    public i(String str, int i) {
        this.f1503c = str;
        this.f1501a = i;
    }

    @Override // java.util.concurrent.ThreadFactory
    public Thread newThread(Runnable runnable) {
        return new h(this, runnable, this.f1503c + Soundex.SILENT_MARKER + this.f1502b.getAndIncrement());
    }
}
