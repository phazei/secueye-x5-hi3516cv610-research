package com.taobao.accs.messenger;

import android.app.Service;
import android.os.Messenger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public abstract class MessengerService extends Service {
    public static final String INTENT = "intent";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private ExecutorService f6344b = Executors.newSingleThreadExecutor();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    MessengerInnerHandler f6343a = new MessengerInnerHandler("MessengerService", this);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Messenger f6345c = new Messenger(this.f6343a);
}
