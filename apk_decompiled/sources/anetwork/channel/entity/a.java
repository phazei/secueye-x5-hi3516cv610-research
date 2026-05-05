package anetwork.channel.entity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final ExecutorService[] f2027a = new ExecutorService[2];

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static AtomicInteger f2028b = new AtomicInteger(0);

    static {
        for (int i = 0; i < 2; i++) {
            f2027a[i] = Executors.newSingleThreadExecutor(new b());
        }
    }

    public static void a(int i, Runnable runnable) {
        f2027a[Math.abs(i % 2)].submit(runnable);
    }
}
