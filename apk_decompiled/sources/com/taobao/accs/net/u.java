package com.taobao.accs.net;

import android.content.Context;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class u extends f {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private ScheduledFuture f6406c;

    protected u(Context context) {
        super(context);
    }

    @Override // com.taobao.accs.net.f
    protected void a(int i) {
        ScheduledFuture scheduledFuture = this.f6406c;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            this.f6406c = null;
        }
        long j = i;
        this.f6406c = ThreadPoolExecutorFactory.getScheduledExecutor().scheduleAtFixedRate(new v(this), j, j, TimeUnit.SECONDS);
    }
}
