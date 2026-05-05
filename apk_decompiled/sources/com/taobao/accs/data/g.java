package com.taobao.accs.data;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.alibaba.sdk.android.error.ErrorCode;
import com.taobao.accs.AccsErrorCode;
import com.taobao.accs.IAppReceiver;
import com.taobao.accs.base.AccsAbstractDataListener;
import com.taobao.accs.base.TaoBaseService;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.common.Constants;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.dispatch.IntentDispatch;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AdapterUtilityImpl;
import com.taobao.accs.utl.AppMonitorAdapter;
import com.taobao.accs.utl.BaseMonitor;
import com.taobao.accs.utl.UTMini;
import com.taobao.accs.utl.UtilityImpl;
import com.vivo.push.PushClientConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static volatile g f6319a;

    public static g a() {
        if (f6319a == null) {
            synchronized (g.class) {
                if (f6319a == null) {
                    f6319a = new g();
                }
            }
        }
        return f6319a;
    }

    public static void a(Context context, Intent intent) {
        a(context, null, intent);
    }

    public static void a(Context context, com.taobao.accs.net.b bVar, Intent intent) {
        try {
            ThreadPoolExecutorFactory.getScheduledExecutor().execute(new h(context, bVar, intent));
        } catch (Throwable th) {
            if (bVar != null && intent != null) {
                String stringExtra = intent.getStringExtra(Constants.KEY_CONFIG_TAG);
                String stringExtra2 = intent.getStringExtra(Constants.KEY_DATA_ID);
                if (!TextUtils.isEmpty(stringExtra) && !TextUtils.isEmpty(stringExtra2)) {
                    bVar.b(Message.a(stringExtra2, intent.getStringExtra(Constants.KEY_SERVICE_ID), bVar.b((String) null), 3), true);
                }
            }
            ALog.e("MsgDistribute", "distribMessage", th, new Object[0]);
            UTMini.getInstance().commitEvent(66001, "MsgToBuss8", "distribMessage" + th.toString(), Integer.valueOf(Constants.SDK_VERSION_CODE));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:122:0x024a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void b(android.content.Context r23, com.taobao.accs.net.b r24, android.content.Intent r25) {
        /*
            Method dump skipped, instruction units count: 631
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.data.g.b(android.content.Context, com.taobao.accs.net.b, android.content.Intent):void");
    }

    protected boolean a(int i, String str) {
        if (i != 100 && !GlobalClientInfo.AGOO_SERVICE_ID.equals(str)) {
            long jB = UtilityImpl.b();
            if (jB != -1 && jB <= 5242880) {
                AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, str, "1", "space low " + jB);
                ALog.e("MsgDistribute", "user space low, don't distribute", "size", Long.valueOf(jB), Constants.KEY_SERVICE_ID, str);
                return true;
            }
        }
        return false;
    }

    protected boolean a(Context context, String str, String str2, Intent intent, ArrayList<IAppReceiver> arrayList) {
        try {
            if (TextUtils.isEmpty(str)) {
                return false;
            }
            String service = null;
            if (arrayList != null) {
                Iterator<IAppReceiver> it = arrayList.iterator();
                while (it.hasNext()) {
                    service = it.next().getService(str);
                    if (!TextUtils.isEmpty(service)) {
                        break;
                    }
                }
            }
            if (TextUtils.isEmpty(service)) {
                service = GlobalClientInfo.getInstance(context).getService(str);
            }
            if (!TextUtils.isEmpty(service) || AdapterUtilityImpl.isTargetProcess(context)) {
                return false;
            }
            if ("accs".equals(str)) {
                ALog.e("MsgDistribute", "start MsgDistributeService", Constants.KEY_DATA_ID, str2);
            } else {
                ALog.i("MsgDistribute", "start MsgDistributeService", Constants.KEY_DATA_ID, str2);
            }
            intent.setClassName(intent.getPackage(), b());
            IntentDispatch.dispatchIntent(context, intent, b());
            return true;
        } catch (Throwable th) {
            ALog.e("MsgDistribute", "handleMsgInChannelProcess", th, new Object[0]);
            return false;
        }
    }

    private void a(Intent intent, String str, int i, String str2, String str3, String str4, ArrayList<IAppReceiver> arrayList, ErrorCode errorCode) {
        ALog.d("agoo_tag", String.valueOf(i), new Object[0]);
        if (ALog.isPrintLog(ALog.Level.D)) {
            ALog.d("MsgDistribute", "handleControlMsg", Constants.KEY_CONFIG_TAG, str, Constants.KEY_DATA_ID, str4, Constants.KEY_SERVICE_ID, str3, "command", Integer.valueOf(i), "errorCode", errorCode);
            if (arrayList != null) {
                Iterator<IAppReceiver> it = arrayList.iterator();
                while (it.hasNext()) {
                    IAppReceiver next = it.next();
                    Object[] objArr = new Object[2];
                    objArr[0] = "appReceiver";
                    objArr[1] = next == null ? null : next.getClass().getName();
                    ALog.d("MsgDistribute", "handleControlMsg", objArr);
                }
            }
        }
        if (errorCode.getCodeInt() != AccsErrorCode.SUCCESS.getCodeInt() && i != 103 && i != 101) {
            ALog.e("MsgDistribute", "handleControlMsg", "command", Integer.valueOf(i), "errorCode", errorCode);
        }
        if (arrayList != null) {
            switch (i) {
                case 1:
                    ALog.d("MsgDistribute", "onBindApp", "code", errorCode);
                    Iterator<IAppReceiver> it2 = arrayList.iterator();
                    while (it2.hasNext()) {
                        com.taobao.accs.utl.a.a(errorCode, it2.next(), null);
                    }
                    break;
                case 2:
                    ALog.d("MsgDistribute", "onUnbindApp", "code", errorCode);
                    Iterator<IAppReceiver> it3 = arrayList.iterator();
                    while (it3.hasNext()) {
                        com.taobao.accs.utl.a.a(errorCode, it3.next());
                    }
                    break;
                case 3:
                    ALog.d("MsgDistribute", "onBindUser", "code", errorCode);
                    Iterator<IAppReceiver> it4 = arrayList.iterator();
                    while (it4.hasNext()) {
                        com.taobao.accs.utl.a.b(errorCode, it4.next(), str2);
                    }
                    break;
                case 4:
                    ALog.d("MsgDistribute", "onUnbindUser", "code", errorCode);
                    Iterator<IAppReceiver> it5 = arrayList.iterator();
                    while (it5.hasNext()) {
                        com.taobao.accs.utl.a.b(errorCode, it5.next());
                    }
                    break;
                default:
                    switch (i) {
                        case 100:
                            if (TextUtils.isEmpty(str3)) {
                                ALog.d("MsgDistribute", "handleControlMsg COMMAND_SEND_DATA serviceId isEmpty", new Object[0]);
                                ALog.d("MsgDistribute", "onSendData", "code", errorCode);
                                Iterator<IAppReceiver> it6 = arrayList.iterator();
                                while (it6.hasNext()) {
                                    it6.next().onSendData(str4, errorCode.getCodeInt());
                                }
                            }
                            break;
                        case 101:
                            if (TextUtils.isEmpty(str3)) {
                                ALog.d("MsgDistribute", "handleControlMsg serviceId isEmpty", new Object[0]);
                                byte[] byteArrayExtra = intent.getByteArrayExtra("data");
                                if (byteArrayExtra != null) {
                                    ALog.d("MsgDistribute", "onData", "code", errorCode);
                                    Iterator<IAppReceiver> it7 = arrayList.iterator();
                                    while (it7.hasNext()) {
                                        it7.next().onData(str2, str4, byteArrayExtra);
                                    }
                                }
                            }
                            break;
                    }
                    break;
            }
        }
        if ((arrayList != null && arrayList.size() != 0) || i == 104 || i == 103) {
            return;
        }
        AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, str3, "1", "appReceiver null return");
        UTMini.getInstance().commitEvent(66001, "MsgToBuss7", "commandId=" + i, "serviceId=" + str3 + " errorCode=" + errorCode + " dataId=" + str4, Integer.valueOf(Constants.SDK_VERSION_CODE));
    }

    protected void a(Context context, com.taobao.accs.net.b bVar, ArrayList<IAppReceiver> arrayList, Intent intent, String str, String str2, int i, ErrorCode errorCode) {
        String service;
        ALog.i("MsgDistribute", "handleBusinessMsg start", Constants.KEY_DATA_ID, str2, Constants.KEY_SERVICE_ID, str, "command", Integer.valueOf(i));
        if (arrayList != null) {
            Iterator<IAppReceiver> it = arrayList.iterator();
            service = null;
            while (it.hasNext()) {
                service = it.next().getService(str);
                if (!TextUtils.isEmpty(service)) {
                    break;
                }
            }
        } else {
            service = null;
        }
        if (TextUtils.isEmpty(service)) {
            service = GlobalClientInfo.getInstance(context).getService(str);
        }
        if (!TextUtils.isEmpty(service)) {
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d("MsgDistribute", "handleBusinessMsg to start service", PushClientConstants.TAG_CLASS_NAME, service);
            }
            intent.setClassName(context, service);
            IntentDispatch.dispatchIntent(context, intent, service);
        } else {
            AccsAbstractDataListener listener = GlobalClientInfo.getInstance(context).getListener(str);
            if (listener != null) {
                if (ALog.isPrintLog(ALog.Level.D)) {
                    ALog.d("MsgDistribute", "handleBusinessMsg getListener not null", new Object[0]);
                }
                AccsAbstractDataListener.onReceiveData(context, intent, listener);
            } else {
                if (bVar != null) {
                    bVar.b(Message.a(str2, str, bVar.b((String) null), 0), true);
                }
                ALog.e("MsgDistribute", "handleBusinessMsg getListener also null", new Object[0]);
                AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, str, "1", "service is null");
            }
        }
        UTMini.getInstance().commitEvent(66001, "MsgToBuss", "commandId=" + i, "serviceId=" + str + " errorCode=" + errorCode + " dataId=" + str2, Integer.valueOf(Constants.SDK_VERSION_CODE));
        StringBuilder sb = new StringBuilder();
        sb.append("2commandId=");
        sb.append(i);
        sb.append("serviceId=");
        sb.append(str);
        AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_POINT_TO_BUSS, sb.toString(), 0.0d);
    }

    protected void a(Context context, Intent intent, int i, ErrorCode errorCode) {
        ALog.i("MsgDistribute", "handBroadCastMsg", "command", Integer.valueOf(i));
        HashMap map = new HashMap();
        ArrayList<IAppReceiver> arrayListB = com.taobao.accs.client.a.a().b();
        if (arrayListB != null) {
            Iterator<IAppReceiver> it = arrayListB.iterator();
            while (it.hasNext()) {
                Map<String, String> allServices = it.next().getAllServices();
                if (allServices != null) {
                    map.putAll(allServices);
                }
            }
        }
        if (i != 103) {
            if (i == 104) {
                for (String str : map.keySet()) {
                    String service = (String) map.get(str);
                    if (TextUtils.isEmpty(service)) {
                        service = GlobalClientInfo.getInstance(context).getService(str);
                    }
                    if (!TextUtils.isEmpty(service)) {
                        intent.setClassName(context, service);
                        IntentDispatch.dispatchIntent(context, intent, service);
                    }
                }
                return;
            }
            if (i == 1 || i == 2 || i == 3 || i == 4 || i == 5 || i == 6) {
                ALog.d("MsgDistribute", "handBroadCastMsg not handled command " + i, new Object[0]);
                return;
            }
            ALog.w("MsgDistribute", "handBroadCastMsg not handled command " + i, new Object[0]);
            return;
        }
        for (String str2 : map.keySet()) {
            if ("accs".equals(str2) || "windvane".equals(str2) || "motu-remote".equals(str2)) {
                String service2 = (String) map.get(str2);
                if (TextUtils.isEmpty(service2)) {
                    service2 = GlobalClientInfo.getInstance(context).getService(str2);
                }
                if (!TextUtils.isEmpty(service2)) {
                    intent.setClassName(context, service2);
                    IntentDispatch.dispatchIntent(context, intent, service2);
                }
            }
        }
        boolean booleanExtra = intent.getBooleanExtra(Constants.KEY_CONNECT_AVAILABLE, false);
        String stringExtra = intent.getStringExtra("host");
        boolean booleanExtra2 = intent.getBooleanExtra(Constants.KEY_TYPE_INAPP, false);
        boolean booleanExtra3 = intent.getBooleanExtra(Constants.KEY_CENTER_HOST, false);
        TaoBaseService.ConnectInfo connectInfo = null;
        if (!TextUtils.isEmpty(stringExtra)) {
            if (booleanExtra) {
                connectInfo = new TaoBaseService.ConnectInfo(stringExtra, booleanExtra2, booleanExtra3);
            } else {
                ALog.e("MsgDistribute", "InAppConnection Not Available ", "error", errorCode);
                connectInfo = new TaoBaseService.ConnectInfo(stringExtra, booleanExtra2, booleanExtra3, errorCode.getCodeInt(), errorCode.getMsg());
            }
            connectInfo.connected = booleanExtra;
        }
        if (connectInfo != null) {
            ALog.d("MsgDistribute", "handBroadCastMsg ACTION_CONNECT_INFO", connectInfo);
            Intent intent2 = new Intent(Constants.ACTION_CONNECT_INFO);
            intent2.setPackage(context.getPackageName());
            intent2.putExtra(Constants.KEY_CONNECT_INFO, connectInfo);
            context.sendBroadcast(intent2, context.getPackageName() + ".ACCS");
            return;
        }
        ALog.e("MsgDistribute", "handBroadCastMsg connect info null, host empty", new Object[0]);
    }

    protected String b() {
        return AdapterUtilityImpl.msgService;
    }

    private boolean a(Intent intent) {
        boolean booleanExtra = intent.getBooleanExtra("routingAck", false);
        intent.getBooleanExtra("routingMsg", false);
        return booleanExtra;
    }

    private boolean b(Context context, Intent intent) {
        return !context.getPackageName().equals(intent.getPackage());
    }
}
