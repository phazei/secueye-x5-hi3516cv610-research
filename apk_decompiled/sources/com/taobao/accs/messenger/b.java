package com.taobao.accs.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class b implements ServiceConnection {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Context f6349a;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Messenger f6351c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private String f6352d;
    private a e;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f6350b = 1;
    private long f = System.currentTimeMillis();

    public b(Context context, String str, a aVar) {
        this.f6349a = context;
        this.f6352d = str;
        this.e = aVar;
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (iBinder == null) {
            this.f6349a.unbindService(this);
            this.f6350b = 0;
        } else {
            this.f6351c = new Messenger(iBinder);
            this.f6350b = 2;
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        this.e.a(this.f6352d, this);
        this.f6350b = 0;
        this.f6351c = null;
    }

    public void a(Intent intent) throws RemoteException {
        Message message = new Message();
        message.getData().putParcelable("intent", intent);
        this.f6351c.send(message);
    }

    public boolean a() {
        return this.f6350b == 2;
    }

    public boolean b() {
        int i = this.f6350b;
        return i == 1 || i == 2;
    }

    public boolean c() {
        return this.f6350b == 1 && System.currentTimeMillis() - this.f > 5000;
    }
}
