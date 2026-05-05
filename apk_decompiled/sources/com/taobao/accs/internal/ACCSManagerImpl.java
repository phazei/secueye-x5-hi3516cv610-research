package com.taobao.accs.internal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import anet.channel.SessionCenter;
import com.alibaba.sdk.android.logger.ILog;
import com.taobao.accs.ACCSClient;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.AccsErrorCode;
import com.taobao.accs.IACCSManager;
import com.taobao.accs.IAppReceiver;
import com.taobao.accs.ILoginInfo;
import com.taobao.accs.base.AccsAbstractDataListener;
import com.taobao.accs.base.TaoBaseService;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.common.Constants;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.data.Message;
import com.taobao.accs.data.g;
import com.taobao.accs.net.j;
import com.taobao.accs.utl.AccsLogger;
import com.taobao.accs.utl.AdapterUtilityImpl;
import com.taobao.accs.utl.AppMonitorAdapter;
import com.taobao.accs.utl.BaseMonitor;
import com.taobao.accs.utl.UtilityImpl;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class ACCSManagerImpl implements IACCSManager {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public com.taobao.accs.net.b f6327a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f6328b = 0;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private boolean f6329c = false;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private String f6330d;
    private ILog e;

    @Override // com.taobao.accs.IACCSManager
    public void forceDisableService(Context context) {
    }

    @Override // com.taobao.accs.IACCSManager
    public void forceEnableService(Context context) {
    }

    @Override // com.taobao.accs.IACCSManager
    public String getUserUnit() {
        return null;
    }

    public ACCSManagerImpl(Context context, String str) {
        GlobalClientInfo.f6289a = context.getApplicationContext();
        this.f6327a = new j(GlobalClientInfo.f6289a, 1, str);
        this.f6330d = str;
        this.e = AccsLogger.getLogger("ACCSMgrImpl_" + this.f6327a.m);
        ThreadPoolExecutorFactory.getScheduledExecutor().schedule(new a(this, str, context), 64L, TimeUnit.MILLISECONDS);
    }

    @Override // com.taobao.accs.IACCSManager
    public void bindApp(Context context, String str, String str2, IAppReceiver iAppReceiver) {
        bindApp(context, str, "accs", str2, iAppReceiver);
    }

    @Override // com.taobao.accs.IACCSManager
    public void bindApp(Context context, String str, String str2, String str3, IAppReceiver iAppReceiver) {
        if (context == null) {
            return;
        }
        this.f6329c = true;
        this.e.d("bindApp", "appKey", str);
        Message messageA = Message.a(context.getPackageName(), 1);
        if (this.f6327a.k() && TextUtils.isEmpty(this.f6327a.i.getAppSecret())) {
            this.e.w("isSecurityOff and null secret");
            this.f6327a.a(messageA, AccsErrorCode.APPSECRET_NULL);
            return;
        }
        if (TextUtils.isEmpty(str)) {
            this.e.w("appkey null");
            this.f6327a.a(messageA, AccsErrorCode.APPKEY_NULL);
            return;
        }
        com.taobao.accs.net.b bVar = this.f6327a;
        bVar.f6363a = str3;
        bVar.f6364b = str;
        bVar.i.getAppSecret();
        UtilityImpl.e(context, str);
        if (iAppReceiver != null) {
            com.taobao.accs.client.a.a().a(this.f6330d, com.taobao.accs.utl.a.a(iAppReceiver));
        }
        a(context, str, str3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Context context, String str, String str2) {
        Intent intentA = a(context, 1);
        try {
            String str3 = GlobalClientInfo.getInstance(context).getPackageInfo().versionName;
            boolean z = UtilityImpl.c(context) || UtilityImpl.utdidChanged(Constants.SP_FILE_NAME, context);
            if (z) {
                this.e.d("force bindApp");
                intentA.putExtra(Constants.KEY_FOUCE_BIND, true);
            }
            intentA.putExtra("appKey", str);
            intentA.putExtra(Constants.KEY_TTID, str2);
            intentA.putExtra("appVersion", str3);
            intentA.putExtra("app_sercet", this.f6327a.i.getAppSecret());
            if (AdapterUtilityImpl.isTargetProcess(context)) {
                Message messageA = Message.a(this.f6327a, context, intentA);
                if (messageA.e() != null) {
                    messageA.e().setDataId(messageA.q);
                    messageA.e().setMsgType(1);
                    messageA.e().setHost(messageA.f != null ? messageA.f.toString() : "");
                }
                a(context, messageA, 1, z);
            } else {
                this.e.w("bindApp only allow in target process");
            }
            this.f6327a.b(context.getApplicationContext());
        } catch (Throwable th) {
            this.e.e("bindApp exception", th);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0090  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void a(android.content.Context r5, com.taobao.accs.data.Message r6, int r7, boolean r8) {
        /*
            r4 = this;
            com.taobao.accs.net.b r0 = r4.f6327a
            r0.a()
            if (r6 != 0) goto L1f
            com.alibaba.sdk.android.logger.ILog r6 = r4.e
            java.lang.String r8 = "message is null"
            r6.e(r8)
            java.lang.String r5 = r5.getPackageName()
            com.taobao.accs.data.Message r5 = com.taobao.accs.data.Message.a(r5, r7)
            com.taobao.accs.net.b r6 = r4.f6327a
            com.alibaba.sdk.android.error.ErrorCode r7 = com.taobao.accs.AccsErrorCode.PARAMETER_ERROR
            r6.a(r5, r7)
            goto Laf
        L1f:
            r5 = 0
            r0 = 1
            switch(r7) {
                case 1: goto L46;
                case 2: goto L25;
                default: goto L24;
            }
        L24:
            goto L90
        L25:
            com.taobao.accs.net.b r8 = r4.f6327a
            com.taobao.accs.client.ClientManager r8 = r8.j()
            java.lang.String r1 = r6.f()
            boolean r8 = r8.isAppUnbinded(r1)
            if (r8 == 0) goto L90
            com.alibaba.sdk.android.logger.ILog r8 = r4.e
            java.lang.String r1 = "unbind app, already unbind"
            r8.i(r1)
            com.taobao.accs.net.b r8 = r4.f6327a
            com.alibaba.sdk.android.error.ErrorCode r1 = com.taobao.accs.AccsErrorCode.SUCCESS
            r8.a(r6, r1)
            r8 = r5
            goto L91
        L46:
            java.lang.String r1 = r6.f()
            com.taobao.accs.net.b r2 = r4.f6327a
            com.taobao.accs.client.ClientManager r2 = r2.j()
            boolean r2 = r2.isAppBinded(r1)
            if (r2 == 0) goto L77
            if (r8 != 0) goto L77
            com.alibaba.sdk.android.logger.ILog r8 = r4.e
            java.lang.String r1 = "bind app from cache"
            r8.i(r1)
            com.taobao.accs.AccsState r8 = com.taobao.accs.AccsState.getInstance()
            java.lang.String r1 = r4.f6330d
            java.lang.String r2 = "bfc"
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r0)
            r8.a(r1, r2, r3)
            com.taobao.accs.net.b r8 = r4.f6327a
            com.alibaba.sdk.android.error.ErrorCode r1 = com.taobao.accs.AccsErrorCode.SUCCESS
            r8.a(r6, r1)
            r8 = r5
            goto L91
        L77:
            com.taobao.accs.net.b r2 = r4.f6327a
            com.taobao.accs.client.ClientManager r2 = r2.j()
            boolean r2 = r2.isAppBinding(r1)
            if (r2 == 0) goto L87
            if (r8 != 0) goto L87
            r8 = r5
            goto L91
        L87:
            com.taobao.accs.net.b r8 = r4.f6327a
            com.taobao.accs.client.ClientManager r8 = r8.j()
            r8.onAppBinding(r1)
        L90:
            r8 = r0
        L91:
            if (r8 == 0) goto Laf
            com.alibaba.sdk.android.logger.ILog r8 = r4.e
            r1 = 3
            java.lang.Object[] r1 = new java.lang.Object[r1]
            java.lang.String r2 = "sendControlMessage"
            r1[r5] = r2
            java.lang.String r5 = "command"
            r1[r0] = r5
            r5 = 2
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)
            r1[r5] = r7
            r8.i(r1)
            com.taobao.accs.net.b r5 = r4.f6327a
            r5.b(r6, r0)
        Laf:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.internal.ACCSManagerImpl.a(android.content.Context, com.taobao.accs.data.Message, int, boolean):void");
    }

    @Override // com.taobao.accs.IACCSManager
    public void unbindApp(Context context) {
        this.e.i("unbindApp");
        this.f6329c = false;
        if (a(context)) {
            a(context, 2, (String) null, (String) null);
            return;
        }
        Intent intentA = a(context, 2);
        if (AdapterUtilityImpl.isTargetProcess(context)) {
            a(context, Message.a(this.f6327a, intentA), 2, false);
        }
    }

    @Override // com.taobao.accs.IACCSManager
    public void bindUser(Context context, String str) {
        bindUser(context, str, false);
    }

    @Override // com.taobao.accs.IACCSManager
    public void bindUser(Context context, String str, boolean z) {
        try {
            this.e.i("bindUser", "userId", str, "force", Boolean.valueOf(z));
            if (a(context)) {
                a(context, 3, (String) null, (String) null);
                return;
            }
            Intent intentA = a(context, 3);
            String strI = this.f6327a.i();
            if (TextUtils.isEmpty(strI)) {
                this.e.e("appkey null");
                return;
            }
            if (UtilityImpl.c(context) || z) {
                this.e.d("force bind user");
                intentA.putExtra(Constants.KEY_FOUCE_BIND, true);
                z = true;
            }
            intentA.putExtra("appKey", strI);
            intentA.putExtra(Constants.KEY_USER_ID, str);
            if (AdapterUtilityImpl.isTargetProcess(context)) {
                Message messageD = Message.d(this.f6327a, intentA);
                if (messageD.e() != null) {
                    messageD.e().setDataId(messageD.q);
                    messageD.e().setMsgType(2);
                    messageD.e().setHost(messageD.f != null ? messageD.f.toString() : "");
                }
                a(context, messageD, 3, z);
            } else {
                this.e.w("bindUser not target process, ignored");
            }
            this.f6327a.b(context.getApplicationContext());
        } catch (Throwable th) {
            this.e.e("bindUser", th);
        }
    }

    @Override // com.taobao.accs.IACCSManager
    public void unbindUser(Context context) {
        this.e.i("unBindUse");
        if (a(context)) {
            a(context, 4, (String) null, (String) null);
            return;
        }
        Intent intentA = a(context, 4);
        String strI = this.f6327a.i();
        if (TextUtils.isEmpty(strI)) {
            this.e.e("appKey null");
            return;
        }
        intentA.putExtra("appKey", strI);
        if (AdapterUtilityImpl.isTargetProcess(context)) {
            a(context, Message.e(this.f6327a, intentA), 4, false);
        } else {
            this.e.w("unBindUser not target process, ignored");
        }
    }

    @Override // com.taobao.accs.IACCSManager
    public void bindService(Context context, String str) {
        this.e.i("bindService", Constants.KEY_SERVICE_ID, str);
        if (a(context)) {
            a(context, 5, str, (String) null);
            return;
        }
        Intent intentA = a(context, 5);
        String strI = this.f6327a.i();
        if (TextUtils.isEmpty(strI)) {
            this.e.e("appKey null");
            return;
        }
        intentA.putExtra("appKey", strI);
        intentA.putExtra(Constants.KEY_SERVICE_ID, str);
        if (AdapterUtilityImpl.isTargetProcess(context)) {
            Message messageB = Message.b(this.f6327a, intentA);
            if (messageB.e() != null) {
                messageB.e().setDataId(messageB.q);
                messageB.e().setMsgType(3);
                messageB.e().setHost(messageB.f != null ? messageB.f.toString() : "");
            }
            a(context, messageB, 5, false);
        } else {
            this.e.w("bindService not target process, ignored");
        }
        this.f6327a.b(context.getApplicationContext());
    }

    @Override // com.taobao.accs.IACCSManager
    public void unbindService(Context context, String str) {
        this.e.i("unbindService", Constants.KEY_SERVICE_ID, str);
        if (a(context)) {
            a(context, 6, str, (String) null);
            return;
        }
        Intent intentA = a(context, 6);
        String strI = this.f6327a.i();
        if (TextUtils.isEmpty(strI)) {
            this.e.e("appKey null");
            return;
        }
        intentA.putExtra("appKey", strI);
        intentA.putExtra(Constants.KEY_SERVICE_ID, str);
        if (AdapterUtilityImpl.isTargetProcess(context)) {
            a(context, Message.c(this.f6327a, intentA), 6, false);
        } else {
            this.e.w("unbindService not target process, ignored");
        }
    }

    @Override // com.taobao.accs.IACCSManager
    public String sendData(Context context, String str, String str2, byte[] bArr, String str3) {
        return sendData(context, str, str2, bArr, str3, null);
    }

    @Override // com.taobao.accs.IACCSManager
    public String sendData(Context context, String str, String str2, byte[] bArr, String str3, String str4) {
        return sendData(context, str, str2, bArr, str3, str4, null);
    }

    @Override // com.taobao.accs.IACCSManager
    public String sendData(Context context, String str, String str2, byte[] bArr, String str3, String str4, URL url) {
        return sendData(context, new ACCSManager.AccsRequest(str, str2, bArr, str3, str4, url, null));
    }

    @Override // com.taobao.accs.IACCSManager
    public String sendData(Context context, ACCSManager.AccsRequest accsRequest) {
        try {
            if (!AdapterUtilityImpl.isTargetProcess(context)) {
                this.e.e("sendData not in target process");
                return null;
            }
            if (accsRequest == null) {
                AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, "", "1", "data null");
                this.e.e("sendData dataInfo null");
                return null;
            }
            if (TextUtils.isEmpty(accsRequest.dataId)) {
                synchronized (ACCSManagerImpl.class) {
                    this.f6328b++;
                    accsRequest.dataId = this.f6328b + "";
                }
            }
            if (TextUtils.isEmpty(this.f6327a.i())) {
                AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, accsRequest.serviceId, "1", "data appkey null");
                this.e.e("sendData appkey null", Constants.KEY_DATA_ID, accsRequest.dataId);
                return null;
            }
            this.f6327a.a();
            Message messageA = Message.a(this.f6327a, context, context.getPackageName(), accsRequest);
            if (messageA.e() != null) {
                messageA.e().onSend();
            }
            this.f6327a.b(messageA, true);
        } catch (Throwable th) {
            AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, accsRequest.serviceId, "1", "data " + th.toString());
            this.e.e("sendData", Constants.KEY_DATA_ID, accsRequest.dataId, th);
        }
        return accsRequest.dataId;
    }

    @Override // com.taobao.accs.IACCSManager
    public String sendRequest(Context context, String str, String str2, byte[] bArr, String str3, String str4) {
        return sendRequest(context, str, str2, bArr, str3, str4, null);
    }

    @Override // com.taobao.accs.IACCSManager
    public String sendRequest(Context context, String str, String str2, byte[] bArr, String str3, String str4, URL url) {
        return sendRequest(context, new ACCSManager.AccsRequest(str, str2, bArr, str3, str4, url, null));
    }

    @Override // com.taobao.accs.IACCSManager
    public String sendRequest(Context context, ACCSManager.AccsRequest accsRequest, String str, boolean z) {
        try {
            if (accsRequest == null) {
                this.e.e("sendRequest request null");
                AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, null, "1", "request null");
                return null;
            }
            if (!AdapterUtilityImpl.isTargetProcess(context)) {
                this.e.e("sendRequest not in target process");
                return null;
            }
            if (TextUtils.isEmpty(accsRequest.dataId)) {
                synchronized (ACCSManagerImpl.class) {
                    this.f6328b++;
                    accsRequest.dataId = this.f6328b + "";
                }
            }
            if (TextUtils.isEmpty(this.f6327a.i())) {
                AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, accsRequest.serviceId, "1", "request appkey null");
                this.e.e("sendRequest appkey null", Constants.KEY_DATA_ID, accsRequest.dataId);
                return null;
            }
            this.f6327a.a();
            if (str == null) {
                str = context.getPackageName();
            }
            Message messageA = Message.a(this.f6327a, context, str, Constants.TARGET_SERVICE_PRE, accsRequest, z);
            if (messageA.e() != null) {
                messageA.e().onSend();
            }
            this.f6327a.b(messageA, true);
        } catch (Throwable th) {
            if (accsRequest != null) {
                AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, accsRequest.serviceId, "1", "request " + th.toString());
                this.e.e("sendRequest", Constants.KEY_DATA_ID, accsRequest.dataId, th);
            }
        }
        return accsRequest.dataId;
    }

    @Override // com.taobao.accs.IACCSManager
    public String sendRequest(Context context, ACCSManager.AccsRequest accsRequest) {
        return sendRequest(context, accsRequest, null, true);
    }

    @Override // com.taobao.accs.IACCSManager
    public String sendPushResponse(Context context, ACCSManager.AccsRequest accsRequest, TaoBaseService.ExtraInfo extraInfo) {
        try {
            if (context == null || accsRequest == null) {
                this.e.e("sendPushResponse input null", context, accsRequest, "extraInfo", extraInfo);
                AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, "", "1", "sendPushResponse null");
                return null;
            }
            AppMonitorAdapter.commitAlarmSuccess("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, "push response total");
            if (TextUtils.isEmpty(this.f6327a.i())) {
                AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, accsRequest.serviceId, "1", "sendPushResponse appkey null");
                this.e.e("sendPushResponse appkey null", "dataid", accsRequest.dataId);
                return null;
            }
            if (TextUtils.isEmpty(accsRequest.dataId)) {
                synchronized (ACCSManagerImpl.class) {
                    this.f6328b++;
                    accsRequest.dataId = this.f6328b + "";
                }
            }
            if (extraInfo == null) {
                extraInfo = new TaoBaseService.ExtraInfo();
            }
            accsRequest.host = null;
            extraInfo.fromPackage = context.getPackageName();
            this.e.i("sendPushResponse", "host", extraInfo.fromHost, "pkg", extraInfo.fromPackage, Constants.KEY_DATA_ID, accsRequest.dataId);
            if (context.getPackageName().equals(extraInfo.fromPackage) && AdapterUtilityImpl.isTargetProcess(context)) {
                sendRequest(context, accsRequest, context.getPackageName(), true);
            }
        } catch (Throwable th) {
            AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, accsRequest.serviceId, "1", "push response " + th.toString());
            this.e.e("sendPushResponse", Constants.KEY_DATA_ID, accsRequest.dataId, th);
        }
        return null;
    }

    @Override // com.taobao.accs.IACCSManager
    public boolean isNetworkReachable(Context context) {
        return UtilityImpl.g(context);
    }

    private boolean a(Context context) {
        com.taobao.accs.net.b bVar = this.f6327a;
        return bVar == null || !bVar.j().isAppBinded(context.getPackageName());
    }

    private Intent a(Context context, int i) {
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_COMMAND);
        intent.setClassName(context.getPackageName(), AdapterUtilityImpl.channelService);
        intent.putExtra(Constants.KEY_PACKAGE_NAME, context.getPackageName());
        intent.putExtra("command", i);
        intent.putExtra("appKey", this.f6327a.f6364b);
        intent.putExtra(Constants.KEY_CONFIG_TAG, this.f6330d);
        return intent;
    }

    @Override // com.taobao.accs.IACCSManager
    @Deprecated
    public void setMode(Context context, int i) {
        ACCSClient.setEnvironment(context, i);
    }

    private void a(Context context, int i, String str, String str2) {
        Intent intent = new Intent(Constants.ACTION_RECEIVE);
        intent.setPackage(context.getPackageName());
        intent.putExtra("command", i);
        intent.putExtra(Constants.KEY_SERVICE_ID, str);
        intent.putExtra(Constants.KEY_DATA_ID, str2);
        intent.putExtra("appKey", this.f6327a.f6364b);
        intent.putExtra(Constants.KEY_CONFIG_TAG, this.f6330d);
        intent.putExtra(Constants.KEY_ERROR_OBJ, i == 2 ? AccsErrorCode.SUCCESS : AccsErrorCode.APP_NOT_BIND);
        g.a(context, intent);
    }

    @Override // com.taobao.accs.IACCSManager
    public void setProxy(Context context, String str, int i) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).edit();
        if (!TextUtils.isEmpty(str)) {
            editorEdit.putString(Constants.KEY_PROXY_HOST, str);
        }
        editorEdit.putInt(Constants.KEY_PROXY_PORT, i);
        editorEdit.apply();
    }

    @Override // com.taobao.accs.IACCSManager
    public void startInAppConnection(Context context, String str, String str2, IAppReceiver iAppReceiver) {
        startInAppConnection(context, str, null, str2, iAppReceiver);
    }

    @Override // com.taobao.accs.IACCSManager
    public void startInAppConnection(Context context, String str, String str2, String str3, IAppReceiver iAppReceiver) {
        if (iAppReceiver != null) {
            com.taobao.accs.client.a.a().a(this.f6330d, com.taobao.accs.utl.a.a(iAppReceiver));
        }
        if (!AdapterUtilityImpl.isTargetProcess(context)) {
            this.e.w("inapp only init in target process!");
            return;
        }
        this.e.i("startInAppConnection", str);
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (!TextUtils.equals(this.f6327a.i(), str)) {
            com.taobao.accs.net.b bVar = this.f6327a;
            bVar.f6363a = str3;
            bVar.f6364b = str;
            bVar.i.getAppSecret();
            UtilityImpl.e(context, str);
        }
        this.f6327a.a();
    }

    @Override // com.taobao.accs.IACCSManager
    public void setLoginInfo(Context context, ILoginInfo iLoginInfo) {
        GlobalClientInfo.getInstance(context).setLoginInfoImpl(this.f6327a.m, iLoginInfo);
    }

    @Override // com.taobao.accs.IACCSManager
    public void clearLoginInfo(Context context) {
        GlobalClientInfo.getInstance(context).clearLoginInfoImpl();
    }

    @Override // com.taobao.accs.IACCSManager
    public void clearBindInfo(Context context) {
        this.f6327a.j().onAppUnbind(context.getPackageName());
    }

    @Override // com.taobao.accs.IACCSManager
    public boolean cancel(Context context, String str) {
        return this.f6327a.a(str);
    }

    @Override // com.taobao.accs.IACCSManager
    public Map<String, Boolean> getChannelState() throws Exception {
        String strB = this.f6327a.b((String) null);
        HashMap map = new HashMap();
        map.put(strB, false);
        if (SessionCenter.getInstance(this.f6327a.i.getAppKey()).getThrowsException(strB, 60000L) != null) {
            map.put(strB, true);
        }
        this.e.i("getChannelState", map);
        return map;
    }

    @Override // com.taobao.accs.IACCSManager
    public Map<String, Boolean> forceReConnectChannel() throws Exception {
        SessionCenter.getInstance(this.f6327a.i.getAppKey()).forceRecreateAccsSession();
        return getChannelState();
    }

    @Override // com.taobao.accs.IACCSManager
    public boolean isChannelError(int i) {
        return AccsErrorCode.isChannelError(i);
    }

    @Override // com.taobao.accs.IACCSManager
    public void registerSerivce(Context context, String str, String str2) {
        GlobalClientInfo.getInstance(context).registerService(str, str2);
    }

    @Override // com.taobao.accs.IACCSManager
    public void unRegisterSerivce(Context context, String str) {
        GlobalClientInfo.getInstance(context).unRegisterService(str);
    }

    @Override // com.taobao.accs.IACCSManager
    public void registerDataListener(Context context, String str, AccsAbstractDataListener accsAbstractDataListener) {
        GlobalClientInfo.getInstance(context).registerListener(str, accsAbstractDataListener);
    }

    @Override // com.taobao.accs.IACCSManager
    public void unRegisterDataListener(Context context, String str) {
        GlobalClientInfo.getInstance(context).unregisterListener(str);
    }

    @Override // com.taobao.accs.IACCSManager
    public void sendBusinessAck(String str, String str2, String str3, short s, String str4, Map<Integer, String> map) {
        this.f6327a.a();
        this.f6327a.b(Message.a(this.f6327a, str, str2, str3, true, s, str4, map), true);
    }

    @Override // com.taobao.accs.IACCSManager
    public void updateConfig(AccsClientConfig accsClientConfig) {
        com.taobao.accs.net.b bVar = this.f6327a;
        if (bVar instanceof j) {
            ((j) bVar).a(accsClientConfig);
        }
    }

    @Override // com.taobao.accs.IACCSManager
    public void cleanLocalBindInfo() {
        this.f6327a.j().clearClients();
    }

    @Override // com.taobao.accs.IACCSManager
    public boolean isConnected() {
        return this.f6327a.l();
    }

    @Override // com.taobao.accs.IACCSManager
    public int getLastConnectErrorCode() {
        return this.f6327a.m();
    }

    @Override // com.taobao.accs.IACCSManager
    public void disconnect() {
        this.f6327a.n();
    }

    @Override // com.taobao.accs.IACCSManager
    public void reconnect() {
        this.f6327a.o();
    }

    @Override // com.taobao.accs.IACCSManager
    public void reset() {
        this.f6327a.p();
        try {
            SharedPreferences.Editor editorEdit = GlobalClientInfo.f6289a.getSharedPreferences(Constants.SP_FILE_NAME, 0).edit();
            editorEdit.clear();
            editorEdit.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
        com.taobao.accs.client.a.a().b(this.f6330d);
        this.f6329c = false;
    }
}
