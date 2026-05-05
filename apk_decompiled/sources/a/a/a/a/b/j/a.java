package a.a.a.a.b.j;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: compiled from: ControlTask.java */
/* JADX INFO: loaded from: classes.dex */
public class a extends Handler {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f1456a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public BlockingQueue<Runnable> f1457b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public Runnable f1458c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public AtomicBoolean f1459d;

    public a(Looper looper) {
        super(looper);
        this.f1456a = a.class.getSimpleName();
        this.f1457b = new LinkedBlockingDeque();
        this.f1459d = new AtomicBoolean(false);
        this.f1459d.set(false);
    }

    public final void a(int i) {
        if (i < 24) {
            if (b()) {
                b(i + 1);
                return;
            } else {
                b("err:takeMsg is status false");
                return;
            }
        }
        a();
        if (b()) {
            b(1);
        } else {
            this.f1459d.set(false);
        }
    }

    public final boolean b() {
        try {
            if (this.f1458c == null) {
                if (this.f1457b.isEmpty()) {
                    return false;
                }
                this.f1458c = this.f1457b.take();
            }
            this.f1458c.run();
            return true;
        } catch (InterruptedException e) {
            b("InterruptedException:" + e.getMessage());
            this.f1457b.remove(null);
            return true;
        }
    }

    @Override // android.os.Handler
    public void handleMessage(@NonNull Message message) {
        super.handleMessage(message);
        if (message.what != 1) {
            return;
        }
        a(((Integer) message.obj).intValue());
    }

    public final void b(int i) {
        a("sendAlarmTime index:" + i);
        sendMessageDelayed(obtainMessage(1, Integer.valueOf(i)), 50L);
    }

    public final void a() {
        this.f1458c = null;
    }

    public void a(Runnable runnable) {
        try {
            this.f1457b.put(runnable);
        } catch (InterruptedException e) {
            e.printStackTrace();
            this.f1457b.remove(runnable);
        }
        if (this.f1459d.compareAndSet(false, true)) {
            b(0);
        }
    }

    public final void b(String str) {
        a.a.a.a.b.m.a.b(this.f1456a, str);
    }

    public final void a(String str) {
        a.a.a.a.b.m.a.a(this.f1456a, str);
    }
}
