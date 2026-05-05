package anet.channel;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import anet.channel.util.ALog;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class h implements ServiceConnection {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ Intent f1756a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ Context f1757b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ SessionRequest f1758c;

    h(SessionRequest sessionRequest, Intent intent, Context context) {
        this.f1758c = sessionRequest;
        this.f1756a = intent;
        this.f1757b = context;
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        ALog.d("awcn.SessionRequest", "onServiceConnected", null, new Object[0]);
        try {
            try {
                Messenger messenger = new Messenger(iBinder);
                Message message = new Message();
                message.getData().putParcelable("intent", this.f1756a);
                messenger.send(message);
            } catch (Exception e) {
                ALog.e("awcn.SessionRequest", "onServiceConnected sendMessage error.", null, e, new Object[0]);
            }
        } finally {
            this.f1757b.unbindService(this);
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        ALog.d("awcn.SessionRequest", "onServiceDisconnected", null, new Object[0]);
        this.f1757b.unbindService(this);
    }
}
