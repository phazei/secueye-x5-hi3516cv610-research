package com.aliyun.alink.linksdk.tmp.device.a;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.aliyun.alink.linksdk.tmp.connect.f;
import com.aliyun.alink.linksdk.tmp.utils.LogCat;

/* JADX INFO: compiled from: MessageHandler.java */
/* JADX INFO: loaded from: classes2.dex */
public class f extends Handler {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected static final String f4365a = "[Tmp]MessageHandler";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final int f4366b = 1;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final int f4367c = 2;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final int f4368d = 3;

    public f(Looper looper) {
        super(looper);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        if (message.what == 1) {
            LogCat.d(f4365a, "handleMessage ONLOAD_MSG");
            f.c cVar = (f.c) message.obj;
            if (cVar != null) {
                if (cVar.f4284d != null) {
                    cVar.f4284d.a(cVar.f4282b, cVar.f4283c);
                }
                if (cVar.f4281a != null) {
                    cVar.f4281a.a(cVar.f4282b, cVar.f4283c);
                    return;
                }
                return;
            }
            return;
        }
        if (message.what == 2) {
            LogCat.d(f4365a, "handleMessage ONERROR_MSG");
            f.a aVar = (f.a) message.obj;
            if (aVar != null) {
                if (aVar.f4280d != null) {
                    aVar.f4280d.a(aVar.f4278b, aVar.f4279c);
                }
                if (aVar.f4277a != null) {
                    aVar.f4277a.a(aVar.f4278b, aVar.f4279c);
                    return;
                }
                return;
            }
            return;
        }
        if (message.what == 3) {
            LogCat.d(f4365a, "handleMessage ONNOTIFY_MSG");
            f.d dVar = (f.d) message.obj;
            if (dVar == null || dVar.f4285a == null) {
                return;
            }
            dVar.f4285a.onMessage(dVar.f4286b, dVar.f4287c);
            return;
        }
        LogCat.e(f4365a, "handleMessage other");
        super.handleMessage(message);
    }
}
