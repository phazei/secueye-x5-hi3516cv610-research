package com.aliyun.alink.linksdk.channel.core.persistent.event;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.linksdk.tools.AError;
import java.util.HashMap;

/* JADX INFO: loaded from: classes2.dex */
public class PersistentEventDispatcher {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public HashMap<IOnPushListener, Boolean> f4108a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public HashMap<IConnectionStateListener, Boolean> f4109b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public HashMap<INetSessionStateListener, Boolean> f4110c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public c f4111d;

    public static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final PersistentEventDispatcher f4112a;

        static {
            PersistentEventDispatcher persistentEventDispatcher = new PersistentEventDispatcher();
            f4112a = persistentEventDispatcher;
            persistentEventDispatcher.a();
        }
    }

    public static PersistentEventDispatcher getInstance() {
        return b.f4112a;
    }

    public void a() {
        if (this.f4111d == null) {
            this.f4111d = new c();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00d7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void broadcastMessage(int r4, java.lang.String r5, byte[] r6, int r7, java.lang.String r8) {
        /*
            Method dump skipped, instruction units count: 605
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.aliyun.alink.linksdk.channel.core.persistent.event.PersistentEventDispatcher.broadcastMessage(int, java.lang.String, byte[], int, java.lang.String):void");
    }

    public void registerNetSessionStateListener(INetSessionStateListener iNetSessionStateListener, boolean z) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("PersistentEventDispatch", "registerNetSessionStateListener()");
        synchronized (this) {
            if (iNetSessionStateListener == null) {
                return;
            }
            if (this.f4110c == null) {
                this.f4110c = new HashMap<>();
            }
            this.f4110c.put(iNetSessionStateListener, Boolean.valueOf(z));
        }
    }

    public void registerOnPushListener(IOnPushListener iOnPushListener, boolean z) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("PersistentEventDispatch", "registerOnPushListener() needUISafety=" + z);
        synchronized (this) {
            if (iOnPushListener == null) {
                return;
            }
            if (this.f4108a == null) {
                this.f4108a = new HashMap<>();
            }
            this.f4108a.put(iOnPushListener, Boolean.valueOf(z));
        }
    }

    public void registerOnTunnelStateListener(IConnectionStateListener iConnectionStateListener, boolean z) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("PersistentEventDispatch", "registerOnTunnelStateListener() called with: listener = [" + iConnectionStateListener + "], needUISafety = [" + z + "]");
        synchronized (this) {
            if (iConnectionStateListener == null) {
                return;
            }
            if (this.f4109b == null) {
                this.f4109b = new HashMap<>();
            }
            this.f4109b.put(iConnectionStateListener, Boolean.valueOf(z));
        }
    }

    public void unregisterNetSessionStateListener(INetSessionStateListener iNetSessionStateListener) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("PersistentEventDispatch", "unregisterNetSessionStateListener()");
        synchronized (this) {
            if (iNetSessionStateListener != null) {
                if (this.f4110c != null && this.f4110c.size() > 0) {
                    this.f4110c.remove(iNetSessionStateListener);
                }
            }
        }
    }

    public void unregisterOnPushListener(IOnPushListener iOnPushListener) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("PersistentEventDispatch", "unregisterOnPushListener() called with: listener = [" + iOnPushListener + "]");
        synchronized (this) {
            if (iOnPushListener != null) {
                if (this.f4108a != null && this.f4108a.size() > 0) {
                    this.f4108a.remove(iOnPushListener);
                }
            }
        }
    }

    public void unregisterOnTunnelStateListener(IConnectionStateListener iConnectionStateListener) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("PersistentEventDispatch", "unregisterOnTunnelStateListener() called with: listener = [" + iConnectionStateListener + "]");
        synchronized (this) {
            if (iConnectionStateListener != null) {
                if (this.f4109b != null && this.f4109b.size() > 0) {
                    this.f4109b.remove(iConnectionStateListener);
                }
            }
        }
    }

    public PersistentEventDispatcher() {
        this.f4108a = null;
        this.f4109b = null;
        this.f4110c = null;
        this.f4111d = null;
    }

    public static class c extends Handler {
        public c() {
            super(Looper.getMainLooper());
        }

        public void a(int i, Object obj, int i2, String str) {
            Message messageObtainMessage = obtainMessage();
            messageObtainMessage.what = i;
            messageObtainMessage.obj = new a(obj, i2, str);
            sendMessageDelayed(messageObtainMessage, 10L);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Object obj;
            if (message == null || (obj = message.obj) == null || !(obj instanceof a)) {
                return;
            }
            a aVar = (a) obj;
            Object obj2 = aVar.f4113a;
            if (obj2 instanceof IOnPushListener) {
                IOnPushListener iOnPushListener = (IOnPushListener) obj2;
                if (message.what == 3) {
                    iOnPushListener.onCommand(aVar.f4116d, aVar.e);
                    return;
                }
                return;
            }
            if (obj2 instanceof IConnectionStateListener) {
                PersistentEventDispatcher.a(message.what, (IConnectionStateListener) obj2, aVar.f4115c, aVar.f4114b);
            } else if (obj2 instanceof INetSessionStateListener) {
                PersistentEventDispatcher.a(message.what, (INetSessionStateListener) obj2);
            }
        }

        public static class a {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public Object f4113a;

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            public String f4114b;

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            public int f4115c;

            /* JADX INFO: renamed from: d, reason: collision with root package name */
            public String f4116d;
            public byte[] e;

            public a(Object obj, int i, String str) {
                this.f4113a = obj;
                this.f4115c = i;
                this.f4114b = str;
            }

            public a(Object obj, String str, byte[] bArr) {
                this.f4113a = obj;
                this.f4116d = str;
                this.e = bArr;
            }
        }

        public void a(int i, Object obj, String str, byte[] bArr) {
            Message messageObtainMessage = obtainMessage();
            messageObtainMessage.what = i;
            messageObtainMessage.obj = new a(obj, str, bArr);
            sendMessageDelayed(messageObtainMessage, 10L);
        }
    }

    public static void a(int i, IConnectionStateListener iConnectionStateListener, int i2, String str) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("PersistentEventDispatch", "OnTunnelState()");
        if (iConnectionStateListener != null) {
            try {
                if (i == 1) {
                    iConnectionStateListener.onConnected();
                } else if (i == 2) {
                    iConnectionStateListener.onDisconnect();
                } else {
                    if (i != 7) {
                        return;
                    }
                    AError aError = new AError();
                    aError.setCode(i2);
                    aError.setMsg(str);
                    iConnectionStateListener.onConnectFail(JSON.toJSONString(aError));
                }
            } catch (Exception unused) {
                com.aliyun.alink.linksdk.channel.core.utils.a.b("PersistentEventDispatch", "catch exception from IConnectionStateListener");
            }
        }
    }

    public static void a(int i, INetSessionStateListener iNetSessionStateListener) {
        com.aliyun.alink.linksdk.channel.core.utils.a.a("PersistentEventDispatch", "OnSessionState()");
        if (iNetSessionStateListener != null) {
            try {
                if (i == 5) {
                    iNetSessionStateListener.onSessionEffective();
                } else if (i == 6) {
                    iNetSessionStateListener.onSessionInvalid();
                } else if (i != 4) {
                } else {
                    iNetSessionStateListener.onNeedLogin();
                }
            } catch (Exception unused) {
                com.aliyun.alink.linksdk.channel.core.utils.a.b("PersistentEventDispatch", "catch exception from INetSessionStateListener");
            }
        }
    }
}
