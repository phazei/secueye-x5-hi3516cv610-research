package com.taobao.accs.internal;

import aisble.BleManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.text.TextUtils;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.ReportProgressUtil;
import com.facebook.internal.NativeProtocol;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.AccsErrorCode;
import com.taobao.accs.IChannelInit;
import com.taobao.accs.client.AdapterGlobalClientInfo;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.common.Constants;
import com.taobao.accs.data.Message;
import com.taobao.accs.data.g;
import com.taobao.accs.net.w;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AppMonitorAdapter;
import com.taobao.accs.utl.BaseMonitor;
import com.taobao.accs.utl.UTMini;
import com.taobao.accs.utl.UtilityImpl;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class ServiceImpl extends d {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Service f6331b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Context f6332c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private long f6333d;
    private String e;

    @Override // com.taobao.accs.internal.d, com.taobao.accs.base.IBaseService
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // com.taobao.accs.internal.d, com.taobao.accs.base.IBaseService
    public boolean onUnbind(Intent intent) {
        return false;
    }

    public ServiceImpl(Service service) {
        super(service);
        this.f6331b = null;
        this.e = "unknown";
        this.f6331b = service;
        this.f6332c = service.getApplicationContext();
    }

    @Override // com.taobao.accs.internal.d, com.taobao.accs.base.IBaseService
    public void onCreate() {
        super.onCreate();
        a();
    }

    @Override // com.taobao.accs.internal.d
    public int a(Intent intent) {
        String action;
        Bundle extras;
        int i = 2;
        if (ALog.isPrintLog(ALog.Level.I)) {
            ALog.i("ServiceImpl", "onHostStartCommand", "intent", intent);
        }
        try {
            try {
                if (ALog.isPrintLog(ALog.Level.D) && intent != null && (extras = intent.getExtras()) != null) {
                    for (String str : extras.keySet()) {
                        ALog.d("ServiceImpl", "onHostStartCommand", "key", str, " value", extras.get(str));
                    }
                }
                int iC = com.taobao.accs.utl.d.c();
                if (iC > 3) {
                    try {
                        ALog.e("ServiceImpl", "onHostStartCommand load SO fail 4 times, don't auto restart", new Object[0]);
                        AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_POINT_SOFAIL, UtilityImpl.a(iC), 0.0d);
                    } catch (Throwable th) {
                        th = th;
                        ALog.e("ServiceImpl", "onHostStartCommand", th, new Object[0]);
                    }
                } else {
                    i = 1;
                }
                action = intent == null ? null : intent.getAction();
            } catch (Throwable th2) {
                th = th2;
                i = 1;
            }
            if (TextUtils.isEmpty(action)) {
                b();
                a(false, false);
                return i;
            }
            a(intent, action);
            return i;
        } finally {
            AdapterGlobalClientInfo.mStartServiceTimes.incrementAndGet();
        }
    }

    private void a(Context context) {
        List<String> listM = UtilityImpl.m(context);
        ArrayList arrayList = new ArrayList();
        if (listM != null && listM.size() > 0) {
            for (int i = 0; i < listM.size(); i++) {
                try {
                    Class<?> cls = Class.forName(listM.get(i));
                    if (IChannelInit.class.isAssignableFrom(cls)) {
                        try {
                            ((IChannelInit) cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0])).init(context);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        arrayList.add(listM.get(i));
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }
        if (arrayList.size() > 0) {
            UtilityImpl.a(context, arrayList);
        }
    }

    private void a() {
        ALog.d("ServiceImpl", "init start", new Object[0]);
        a(this.f6332c);
        GlobalClientInfo.getInstance(this.f6332c);
        AdapterGlobalClientInfo.mStartServiceTimes.incrementAndGet();
        this.f6333d = System.currentTimeMillis();
        this.e = UtilityImpl.f(this.f6332c);
        if (ALog.isPrintLog(ALog.Level.I)) {
            ALog.i("ServiceImpl", "init", "sdkVersion", Integer.valueOf(Constants.SDK_VERSION_CODE), "procStart", Integer.valueOf(AdapterGlobalClientInfo.mStartServiceTimes.intValue()));
        }
        UTMini.getInstance().commitEvent(66001, ReportProgressUtil.TAG_START, UtilityImpl.e(), "PROXY");
        long jH = UtilityImpl.h(this.f6332c);
        ALog.d("ServiceImpl", "getServiceAliveTime", "aliveTime", Long.valueOf(jH));
        if (jH > BleManager.CONNECTION_TIMEOUT_THRESHOLD) {
            AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_SERVICE_ALIVE, "", jH / 1000);
        }
        UtilityImpl.a(this.f6332c, Constants.SP_KEY_SERVICE_START, System.currentTimeMillis());
        UTMini.getInstance().commitEvent(66001, "NOTIFY", UtilityImpl.k(this.f6332c));
    }

    private void a(Intent intent, String str) {
        ALog.d("ServiceImpl", "handleAction", NativeProtocol.WEB_DIALOG_ACTION, str);
        try {
            b();
            if (TextUtils.equals(str, "android.intent.action.PACKAGE_REMOVED")) {
                return;
            }
            if (TextUtils.equals(str, "android.net.conn.CONNECTIVITY_CHANGE")) {
                String strF = UtilityImpl.f(this.f6332c);
                boolean zG = UtilityImpl.g(this.f6332c);
                ALog.i("ServiceImpl", "network change:" + this.e + " to " + strF, new Object[0]);
                if (zG) {
                    this.e = strF;
                    c();
                    a(true, false);
                    UTMini.getInstance().commitEvent(66001, "CONNECTIVITY_CHANGE", strF, UtilityImpl.e(), "0");
                }
                if (strF.equals("unknown")) {
                    c();
                    this.e = strF;
                    return;
                }
                return;
            }
            if (TextUtils.equals(str, "android.intent.action.BOOT_COMPLETED")) {
                a(true, false);
                return;
            }
            if (TextUtils.equals(str, "android.intent.action.USER_PRESENT")) {
                ALog.d("ServiceImpl", "action android.intent.action.USER_PRESENT", new Object[0]);
                a(true, false);
            } else if (str.equals(Constants.ACTION_COMMAND)) {
                b(intent);
            }
        } catch (Throwable th) {
            ALog.e("ServiceImpl", "handleAction", th, new Object[0]);
        }
    }

    private void b(Intent intent) {
        com.taobao.accs.net.b bVar;
        Message.ReqType reqType;
        URL url;
        Message messageA;
        int intExtra = intent.getIntExtra("command", -1);
        ALog.i("ServiceImpl", "handleCommand", "command", Integer.valueOf(intExtra));
        String stringExtra = intent.getStringExtra(Constants.KEY_PACKAGE_NAME);
        String stringExtra2 = intent.getStringExtra(Constants.KEY_SERVICE_ID);
        String stringExtra3 = intent.getStringExtra(Constants.KEY_USER_ID);
        String stringExtra4 = intent.getStringExtra("appKey");
        String stringExtra5 = intent.getStringExtra(Constants.KEY_CONFIG_TAG);
        String stringExtra6 = intent.getStringExtra(Constants.KEY_TTID);
        intent.getStringExtra("sid");
        intent.getStringExtra(Constants.KEY_ANTI_BRUSH_COOKIE);
        if (intExtra == 201) {
            a(Message.a(true, 0), true);
            d();
        }
        if (intExtra <= 0 || TextUtils.isEmpty(stringExtra)) {
            return;
        }
        com.taobao.accs.net.b bVarA = a(this.f6332c, stringExtra5, true);
        if (bVarA != null) {
            bVarA.a();
            Message messageA2 = null;
            if (intExtra != 1) {
                bVar = bVarA;
                if (intExtra == 2) {
                    ALog.e("ServiceImpl", "onHostStartCommand COMMAND_UNBIND_APP", new Object[0]);
                    if (bVar.j().isAppUnbinded(stringExtra)) {
                        Message messageA3 = Message.a(bVar, stringExtra);
                        ALog.i("ServiceImpl", stringExtra + " isAppUnbinded", new Object[0]);
                        bVar.a(messageA3, AccsErrorCode.SUCCESS);
                        return;
                    }
                } else if (intExtra == 5) {
                    messageA = Message.a(stringExtra, stringExtra2);
                } else if (intExtra == 6) {
                    messageA = Message.b(stringExtra, stringExtra2);
                } else if (intExtra == 3) {
                    messageA = Message.c(stringExtra, stringExtra3);
                } else if (intExtra == 4) {
                    messageA = Message.a(stringExtra);
                } else if (intExtra == 100) {
                    byte[] byteArrayExtra = intent.getByteArrayExtra("data");
                    String stringExtra7 = intent.getStringExtra(Constants.KEY_DATA_ID);
                    String stringExtra8 = intent.getStringExtra(Constants.KEY_TARGET);
                    String stringExtra9 = intent.getStringExtra(Constants.KEY_BUSINESSID);
                    String stringExtra10 = intent.getStringExtra(Constants.KEY_EXT_TAG);
                    try {
                        reqType = (Message.ReqType) intent.getSerializableExtra(Constants.KEY_SEND_TYPE);
                    } catch (Exception unused) {
                        reqType = null;
                    }
                    if (byteArrayExtra != null) {
                        try {
                            url = new URL("https://" + ((w) bVar).r());
                        } catch (Exception unused2) {
                            url = null;
                        }
                        ACCSManager.AccsRequest accsRequest = new ACCSManager.AccsRequest(stringExtra3, stringExtra2, byteArrayExtra, stringExtra7, stringExtra8, url, stringExtra9);
                        accsRequest.setTag(stringExtra10);
                        if (reqType == null) {
                            messageA2 = Message.a(bVar, this.f6332c, stringExtra, accsRequest, false);
                        } else if (reqType == Message.ReqType.REQ) {
                            messageA2 = Message.a(bVar, this.f6332c, stringExtra, Constants.TARGET_SERVICE_PRE, accsRequest, false);
                        }
                    }
                    messageA = messageA2;
                } else if (intExtra == 106) {
                    intent.setAction(Constants.ACTION_RECEIVE);
                    intent.putExtra("command", -1);
                    g.a(this.f6332c, intent);
                    return;
                }
                messageA = null;
            } else {
                if (!stringExtra.equals(this.f6332c.getPackageName())) {
                    ALog.e("ServiceImpl", "handleCommand bindapp pkg error", new Object[0]);
                    return;
                }
                bVar = bVarA;
                messageA = Message.a(this.f6332c, stringExtra5, stringExtra4, intent.getStringExtra("app_sercet"), stringExtra, stringExtra6, intent.getStringExtra("appVersion"));
                bVar.f6363a = stringExtra6;
                UtilityImpl.e(this.f6332c, stringExtra4);
                if (bVar.j().isAppBinded(stringExtra) && !intent.getBooleanExtra(Constants.KEY_FOUCE_BIND, false)) {
                    ALog.i("ServiceImpl", stringExtra + " isAppBinded", new Object[0]);
                    bVar.a(messageA, AccsErrorCode.SUCCESS);
                    return;
                }
            }
            if (messageA != null) {
                ALog.d("ServiceImpl", "try send message", new Object[0]);
                if (messageA.e() != null) {
                    messageA.e().onSend();
                }
                bVar.b(messageA, true);
                return;
            }
            ALog.e("ServiceImpl", "message is null", new Object[0]);
            bVar.a(Message.a(stringExtra, intExtra), AccsErrorCode.PARAMETER_ERROR);
            return;
        }
        ALog.e("ServiceImpl", "no connection", Constants.KEY_CONFIG_TAG, stringExtra5, "command", Integer.valueOf(intExtra));
    }

    @Override // com.taobao.accs.internal.d, com.taobao.accs.base.IBaseService
    public void onDestroy() {
        super.onDestroy();
        ALog.e("ServiceImpl", "Service onDestroy", new Object[0]);
        UtilityImpl.a(this.f6332c, Constants.SP_KEY_SERVICE_END, System.currentTimeMillis());
        this.f6331b = null;
        this.f6332c = null;
        e();
        Process.killProcess(Process.myPid());
    }

    private synchronized void b() {
        if (f6340a != null && f6340a.size() != 0) {
            for (Map.Entry<String, com.taobao.accs.net.b> entry : f6340a.entrySet()) {
                com.taobao.accs.net.b value = entry.getValue();
                if (value == null) {
                    ALog.e("ServiceImpl", "tryConnect connection null", "appkey", value.i());
                    return;
                }
                ALog.i("ServiceImpl", "tryConnect", "appkey", value.i(), Constants.KEY_CONFIG_TAG, entry.getKey());
                if (value.k() && TextUtils.isEmpty(value.i.getAppSecret())) {
                    ALog.e("ServiceImpl", "tryConnect secret is null", new Object[0]);
                } else {
                    value.a();
                }
            }
            return;
        }
        ALog.w("ServiceImpl", "tryConnect no connections", new Object[0]);
    }

    private void a(Message message, boolean z) {
        if (f6340a == null || f6340a.size() == 0) {
            return;
        }
        Iterator<Map.Entry<String, com.taobao.accs.net.b>> it = f6340a.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().b(message, z);
        }
    }

    private void a(boolean z, boolean z2) {
        if (f6340a == null || f6340a.size() == 0) {
            return;
        }
        Iterator<Map.Entry<String, com.taobao.accs.net.b>> it = f6340a.entrySet().iterator();
        while (it.hasNext()) {
            com.taobao.accs.net.b value = it.next().getValue();
            value.a(z, z2);
            ALog.i("ServiceImpl", "ping connection", "appkey", value.i());
        }
    }

    private void c() {
        if (f6340a == null || f6340a.size() == 0) {
            return;
        }
        Iterator<Map.Entry<String, com.taobao.accs.net.b>> it = f6340a.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().b();
        }
    }

    private void d() {
        if (f6340a == null || f6340a.size() == 0) {
            return;
        }
        Iterator<Map.Entry<String, com.taobao.accs.net.b>> it = f6340a.entrySet().iterator();
        while (it.hasNext()) {
            com.taobao.accs.ut.a.c cVarC = it.next().getValue().c();
            if (cVarC != null) {
                cVarC.h = this.f6333d;
                cVarC.a();
            }
        }
    }

    private void e() {
        if (f6340a == null || f6340a.size() == 0) {
            return;
        }
        Iterator<Map.Entry<String, com.taobao.accs.net.b>> it = f6340a.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().e();
        }
    }
}
