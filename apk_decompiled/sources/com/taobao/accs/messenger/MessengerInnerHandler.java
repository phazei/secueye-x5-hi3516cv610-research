package com.taobao.accs.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import com.taobao.accs.utl.ALog;
import java.lang.ref.WeakReference;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public final class MessengerInnerHandler extends Handler {
    private String tag;
    WeakReference<Service> weakReference;

    public MessengerInnerHandler(String str, Service service) {
        this.tag = str;
        this.weakReference = new WeakReference<>(service);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        Service service;
        if (message != null) {
            ALog.i(this.tag, "handleMessage on receive msg", "msg", message.toString());
            Intent intent = (Intent) message.getData().getParcelable("intent");
            if (intent != null) {
                ALog.i(this.tag, "handleMessage get intent success", "intent", intent.toString());
                WeakReference<Service> weakReference = this.weakReference;
                if (weakReference == null || (service = weakReference.get()) == null) {
                    return;
                }
                service.onStartCommand(intent, 0, 0);
            }
        }
    }
}
