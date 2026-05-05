package com.aliyun.iot.aep.sdk.credential.IotCredentialManager;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.credential.data.CompanyData;
import com.aliyun.iot.aep.sdk.credential.data.IoTCredentialData;
import com.aliyun.iot.aep.sdk.credential.listener.IoTTokenCreatedListener;
import com.aliyun.iot.aep.sdk.credential.listener.IoTTokenInvalidListener;
import com.aliyun.iot.aep.sdk.credential.listener.OnReqCompanyCallback;
import com.aliyun.iot.aep.sdk.credential.oa.OADepBiz;
import com.aliyun.iot.aep.sdk.credential.utils.ReflectUtils;
import com.aliyun.iot.aep.sdk.log.ALog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class IoTCredentialManageImpl implements IoTCredentialManage {
    public static final String APP_DATA_FILE = "APP_DATA_FILE";
    public static final String AUTH_IOT_TOKEN_IDENTITY_ID_KEY = "identityId";
    public static final String AUTH_IOT_TOKEN_STATUS_CHANGE_BROADCAST = "com.ilop.auth.iotToken.change";
    public static final String AUTH_IOT_TOKEN_STATUS_INVALID = "invalid";
    public static final String AUTH_IOT_TOKEN_STATUS_KEY = "status";
    public static final String AUTH_IOT_TOKEN_STATUS_REFRESH_FAIL = "refreshFail";
    public static final String AUTH_IOT_TOKEN_STATUS_REFRESH_SUCCESS = "refreshSuccess";
    public static final String COMPANY_TYPE = "company";
    public static String DefaultDailyALiYunCreateIotTokenRequestHost = "";
    public static final String KEY_ACCOUNT_TYPE = "KEY_ACCOUNT_TYPE";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    static String f4650a = null;
    public static String appKey = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static boolean f4651b = false;
    private static IoTCredentialManageImpl g = null;
    private static String h = "";

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Context f4652c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private volatile IoTCredentialData f4653d;
    private IoTTokenInvalidListener i;
    private IoTCallback j;
    private String m;
    private OnReqCompanyCallback n;
    private volatile boolean e = false;
    private volatile List<IoTCredentialListener> f = Collections.synchronizedList(new ArrayList());
    private ICredentialDataSource k = null;
    private List<IoTTokenCreatedListener> l = Collections.synchronizedList(new ArrayList());
    private OnReqCompanyCallback o = new OnReqCompanyCallback() { // from class: com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageImpl.1
        @Override // com.aliyun.iot.aep.sdk.credential.listener.OnReqCompanyCallback
        public void onSuccess(int i, List<CompanyData> list) {
            if (i == 200 && list != null && list.size() > 0 && list.get(0) != null) {
                ALog.i("IoTCredentialManage", "defaultOnReqCompanyCallback onSuccess companyId:" + list.get(0).companyId + " name:" + list.get(0).companyName);
                IoTCredentialManageImpl.this.setCompanyId(list.get(0).companyId);
                IoTCredentialManageImpl.this.asyncRefreshIoTCredential(null);
                return;
            }
            ALog.i("IoTCredentialManage", "defaultOnReqCompanyCallback onSuccess empty");
            IoTCredentialManageImpl.this.a(false, new IoTCredentialManageError(6, "companyId params can't empty"), (IoTRequest) null);
        }

        @Override // com.aliyun.iot.aep.sdk.credential.listener.OnReqCompanyCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            ALog.e("IoTCredentialManage", "defaultOnReqCompanyCallback onFailure:" + exc.toString());
            IoTCredentialManageImpl.this.a(false, new IoTCredentialManageError(6, "companyId params can't empty"), (IoTRequest) null);
        }
    };

    public void setOnReqCompanyCallback(OnReqCompanyCallback onReqCompanyCallback) {
        this.n = onReqCompanyCallback;
    }

    @Deprecated
    public void setIotCredentialListenerList(IoTTokenInvalidListener ioTTokenInvalidListener) {
        this.i = ioTTokenInvalidListener;
    }

    public void registerIotTokenCreatedListener(IoTTokenCreatedListener ioTTokenCreatedListener) {
        if (ioTTokenCreatedListener == null) {
            return;
        }
        this.l.add(ioTTokenCreatedListener);
    }

    public void unRegisterIotTokenCreatedListener(IoTTokenCreatedListener ioTTokenCreatedListener) {
        if (this.l.isEmpty() || ioTTokenCreatedListener == null) {
            return;
        }
        try {
            this.l.remove(ioTTokenCreatedListener);
        } catch (Exception e) {
            ALog.i("IoTCredentialManage", "unRegisterIotTokenCreatedListener error:" + e.toString());
        }
    }

    public void setIotCredentialPlugin(ICredentialDataSource iCredentialDataSource) {
        ALog.d("IoTCredentialManage", "setIotCredentialPlugin() called with: dataSource = [" + iCredentialDataSource + "]");
        this.k = iCredentialDataSource;
    }

    public void setCompanyId(String str) {
        this.m = str;
    }

    public void setIotTokenInvalidListener(IoTTokenInvalidListener ioTTokenInvalidListener) {
        this.i = ioTTokenInvalidListener;
    }

    public void setAccountTypeCompany() {
        setAccountType(COMPANY_TYPE);
    }

    public void setAccountType(String str) {
        h = str;
        Context context = this.f4652c;
        if (context == null) {
            return;
        }
        context.getSharedPreferences(APP_DATA_FILE, 0).edit().putString(KEY_ACCOUNT_TYPE, str).apply();
    }

    public String getAccountType() {
        Context context = this.f4652c;
        return context == null ? "" : context.getSharedPreferences(APP_DATA_FILE, 0).getString(KEY_ACCOUNT_TYPE, "");
    }

    public void setAuthCode(String str) {
        f4650a = str;
    }

    public static IoTCredentialManageImpl getInstance(Application application) {
        if (g == null) {
            synchronized (IoTCredentialManageImpl.class) {
                if (g == null) {
                    g = new IoTCredentialManageImpl(application);
                }
            }
        }
        return g;
    }

    public static void init(String str) {
        if (f4651b) {
            return;
        }
        f4651b = true;
        if (ReflectUtils.hasOADep() && !OADepBiz.hasOAAdapter()) {
            throw new IllegalArgumentException("loginAdapter can't be null, need call LoginBusiness.init first");
        }
        appKey = str;
    }

    private IoTCredentialManageImpl(Context context) {
        if (!f4651b) {
            throw new IllegalArgumentException("must call init first");
        }
        ALog.i("IoTCredentialManage", "IoTTokenManager() init");
        if (context == null) {
            throw new IllegalArgumentException("Context Can't Be NULL");
        }
        this.f4652c = context;
        this.f4653d = IoTCredentialUtils.getIoTCredentialData(context);
        StringBuilder sb = new StringBuilder();
        sb.append("IoTCredentialManageImpl(): ioTCredentialData:");
        sb.append(this.f4653d == null ? "" : this.f4653d.toString());
        sb.append(" getIoTToken:");
        sb.append(getIoTToken());
        ALog.i("IoTCredentialManage", sb.toString());
        if (ReflectUtils.hasOADep()) {
            OADepBiz.registerLoginListener(new a());
        }
        h = getAccountType();
    }

    public void requestCompanyList(final String str, final OnReqCompanyCallback onReqCompanyCallback) {
        HashMap map = new HashMap(2);
        map.put("authCode", str);
        map.put("codeType", "SESSION_ID");
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/user/account/company/listbyauthcode").setApiVersion("1.0.1").setParams(map).build(), new IoTCallback() { // from class: com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageImpl.2
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                OnReqCompanyCallback onReqCompanyCallback2 = onReqCompanyCallback;
                if (onReqCompanyCallback2 != null) {
                    onReqCompanyCallback2.onFailure(ioTRequest, exc);
                }
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getCode() != 200) {
                    IoTCredentialManageImpl.this.a(str, onReqCompanyCallback);
                } else if (JSON.parseArray(ioTResponse.getData().toString()).size() > 0) {
                    onReqCompanyCallback.onSuccess(ioTResponse.getCode(), JSON.parseArray(ioTResponse.getData().toString(), CompanyData.class));
                } else {
                    IoTCredentialManageImpl.this.a(str, onReqCompanyCallback);
                }
            }
        });
    }

    void a(String str, final OnReqCompanyCallback onReqCompanyCallback) {
        HashMap map = new HashMap(2);
        map.put("authCode", str);
        map.put("codeType", "SESSION_ID");
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/user/account/employee/createbyauthcode").setApiVersion("1.0.0").setParams(map).build(), new IoTCallback() { // from class: com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageImpl.3
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                OnReqCompanyCallback onReqCompanyCallback2 = onReqCompanyCallback;
                if (onReqCompanyCallback2 != null) {
                    onReqCompanyCallback2.onFailure(ioTRequest, exc);
                }
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getCode() != 200) {
                    onReqCompanyCallback.onSuccess(ioTResponse.getCode(), null);
                    return;
                }
                if (ioTResponse.getData() != null) {
                    JSONObject jSONObject = (JSONObject) ioTResponse.getData();
                    CompanyData companyData = new CompanyData();
                    companyData.companyId = jSONObject.optString("companyId");
                    companyData.companyName = jSONObject.optString("companyName");
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(companyData);
                    onReqCompanyCallback.onSuccess(ioTResponse.getCode(), arrayList);
                    return;
                }
                onReqCompanyCallback.onSuccess(ioTResponse.getCode(), null);
            }
        });
    }

    @Override // com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManage
    public void asyncRefreshIoTCredential(IoTCredentialListener ioTCredentialListener) {
        ALog.i("IoTCredentialManage", "asyncRefreshIoTCredential ()  isRefreshing? " + this.e);
        if (ioTCredentialListener != null) {
            this.f.add(ioTCredentialListener);
        }
        if (this.e) {
            return;
        }
        this.e = true;
        if ((ReflectUtils.hasOADep() && OADepBiz.isLogin()) || this.k != null) {
            a();
        } else {
            a(false, new IoTCredentialManageError(0, null), (IoTRequest) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void a(boolean z, IoTCredentialManageError ioTCredentialManageError, IoTRequest ioTRequest) {
        ALog.i("IoTCredentialManage", "entering dealCacheIoTTokenListeners");
        try {
            ALog.i("IoTCredentialManage", "dealCacheIoTTokenListeners()  result:" + z);
            if (ioTCredentialManageError != null && ioTCredentialManageError.errorCode == 3 && this.i != null) {
                this.i.onIoTTokenInvalid();
            }
            this.e = false;
            if (z) {
                IoTCredentialUtils.saveIoTCredentialData(this.f4652c, this.f4653d);
                if (this.l != null && !this.l.isEmpty() && ioTRequest != null) {
                    ALog.i("IoTCredentialManage", "ioTTokenCreatedListenerList is not empty ,size is :" + this.l.size());
                    for (int i = 0; i < IoTCredentialUtils.CREATE_IOTTOKEN_REQUEST_PATH_ARRAY.length; i++) {
                        if (TextUtils.equals(IoTCredentialUtils.CREATE_IOTTOKEN_REQUEST_PATH_ARRAY[i], ioTRequest.getPath())) {
                            ArrayList arrayList = new ArrayList();
                            arrayList.addAll(this.l);
                            ALog.i("IoTCredentialManage", "start to exec iottokenCreatedListener callback list");
                            Iterator it = arrayList.iterator();
                            while (it.hasNext()) {
                                ((IoTTokenCreatedListener) it.next()).onIoTTokenCreated();
                            }
                        }
                    }
                }
                a(this.f4652c, this.f4653d == null ? "" : this.f4653d.identity, AUTH_IOT_TOKEN_STATUS_REFRESH_SUCCESS);
            } else {
                a(this.f4652c, this.f4653d == null ? "" : this.f4653d.identity, AUTH_IOT_TOKEN_STATUS_REFRESH_FAIL);
            }
        } catch (Exception e) {
            ALog.i("IoTCredentialManage", "dealCacheIoTTokenListeners exception :" + e.toString());
        }
        if (this.f != null && !this.f.isEmpty()) {
            ArrayList<IoTCredentialListener> arrayList2 = new ArrayList();
            arrayList2.addAll(this.f);
            this.f.clear();
            ALog.i("IoTCredentialManage", "dealCacheIoTTokenListeners listener callback start");
            for (IoTCredentialListener ioTCredentialListener : arrayList2) {
                if (ioTCredentialListener != null) {
                    if (z) {
                        ioTCredentialListener.onRefreshIoTCredentialSuccess(getIoTCredential());
                    } else {
                        ioTCredentialListener.onRefreshIoTCredentialFailed(ioTCredentialManageError);
                    }
                }
            }
            ALog.i("IoTCredentialManage", "dealCacheIoTTokenListeners listener callback end");
            ALog.i("IoTCredentialManage", "leaving dealCacheIoTTokenListeners");
        }
    }

    private void a(Context context, String str, String str2) {
        ALog.d("IoTCredentialManage", "broadcastIoTRefreshStatus() called with: appContext = [" + context + "], identityId = [" + str + "], status = [" + str2 + "]");
        if (context == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setPackage(context.getPackageName());
        intent.setAction(AUTH_IOT_TOKEN_STATUS_CHANGE_BROADCAST);
        intent.putExtra("status", str2);
        intent.putExtra(AUTH_IOT_TOKEN_IDENTITY_ID_KEY, str);
        LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(intent);
    }

    private void a() {
        IoTRequest refreshIoTCredentialRequest;
        ALog.i("IoTCredentialManage", "refreshIoTTokenLocked()");
        if (TextUtils.isEmpty(getIoTToken())) {
            ALog.i("IoTCredentialManage", "refreshIoTCredentialLocked():iotToken is empty,need create iotToken first, sessionid is: " + OADepBiz.getSessionId());
            refreshIoTCredentialRequest = IoTCredentialUtils.getCreateIoTCredentialRequest(OADepBiz.getSessionId(), appKey, h, this.m);
        } else {
            ALog.i("IoTCredentialManage", "refreshIoTTokenLocked():iotToken is not empty,need refresh iotToken, sessionid is: " + OADepBiz.getSessionId());
            refreshIoTCredentialRequest = IoTCredentialUtils.getRefreshIoTCredentialRequest(getIoTRefreshToken(), getIoTIdentity());
        }
        if (refreshIoTCredentialRequest == null) {
            ALog.i("IoTCredentialManage", "refreshIoTTokenLocked(): accountType is company but companyId is empty");
            OnReqCompanyCallback onReqCompanyCallback = this.n;
            if (onReqCompanyCallback == null) {
                onReqCompanyCallback = this.o;
            }
            requestCompanyList(OADepBiz.getSessionId(), onReqCompanyCallback);
            this.e = false;
            return;
        }
        IoTAPIClient client = new IoTAPIClientFactory().getClient();
        this.j = new IoTCallback() { // from class: com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageImpl.4
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                IoTCredentialManageImpl.this.e = false;
                ALog.i("IoTCredentialManage", "refreshIoTTokenLocked() failed: " + exc.toString());
                IoTCredentialManageImpl.this.a(false, new IoTCredentialManageError(-1, exc), ioTRequest);
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                IoTCredentialManageError ioTCredentialManageError;
                boolean z = false;
                IoTCredentialManageImpl.this.e = false;
                if (ioTResponse == null) {
                    IoTCredentialManageImpl.this.a(false, new IoTCredentialManageError(-1, ioTResponse), ioTRequest);
                    return;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("onResponse():");
                sb.append(ioTResponse.getData() == null ? TmpConstant.GROUP_ROLE_UNKNOWN : ioTResponse.getData().toString());
                ALog.i("IoTCredentialManage", sb.toString());
                if (ioTResponse.getCode() == 200) {
                    if (ioTResponse.getData() != null) {
                        IoTCredentialManageError ioTCredentialManageError2 = null;
                        try {
                            if (ioTResponse.getData() instanceof JSONObject) {
                                IoTCredentialManageImpl.this.f4653d.update((JSONObject) ioTResponse.getData());
                                ALog.i("IoTCredentialManage", "update ioTCredentialData success, new token data is: " + IoTCredentialManageImpl.this.f4653d.toString());
                            }
                            z = true;
                        } catch (JSONException e) {
                            IoTCredentialManageError ioTCredentialManageError3 = new IoTCredentialManageError(4, ioTResponse);
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("onResponse(): parse tokenJson error, original json: ");
                            sb2.append(ioTResponse.getData() == null ? "" : ioTResponse.getData().toString());
                            sb2.append(" exception: ");
                            sb2.append(e.toString());
                            ALog.i("IoTCredentialManage", sb2.toString());
                            ioTCredentialManageError2 = ioTCredentialManageError3;
                        }
                        ALog.i("IoTCredentialManage", "before enter dealCacheIoTTokenListeners");
                        IoTCredentialManageImpl.this.a(z, ioTCredentialManageError2, ioTRequest);
                        ALog.i("IoTCredentialManage", "after enter dealCacheIoTTokenListeners");
                        return;
                    }
                } else {
                    if (ioTResponse.getCode() == 2460 || ioTResponse.getCode() == 2401 || ioTResponse.getCode() == 2405) {
                        ioTCredentialManageError = new IoTCredentialManageError(3, ioTResponse);
                    } else if (ioTResponse.getCode() == 2459) {
                        ioTCredentialManageError = new IoTCredentialManageError(5, ioTResponse);
                    } else if (ioTResponse.getCode() == 2462 || ioTResponse.getCode() == 2407) {
                        ioTCredentialManageError = new IoTCredentialManageError(2, ioTResponse);
                    } else if (ioTResponse.getCode() == 2461) {
                        ioTCredentialManageError = new IoTCredentialManageError(1, ioTResponse);
                    } else {
                        ioTCredentialManageError = new IoTCredentialManageError(-1, ioTResponse);
                    }
                    IoTCredentialManageImpl.this.a(false, ioTCredentialManageError, ioTRequest);
                }
                IoTCredentialManageImpl.this.a(false, new IoTCredentialManageError(-1, ioTResponse), ioTRequest);
            }
        };
        if (this.k != null) {
            ALog.i("IoTCredentialManage", "use external credentialDataSource = " + this.k);
            if (TextUtils.isEmpty(getIoTToken())) {
                this.k.credentialDidCreate(this.j);
                return;
            } else {
                this.k.credentialDidUpdate(this.j);
                return;
            }
        }
        client.send(refreshIoTCredentialRequest, this.j);
    }

    @Override // com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManage
    public String getIoTToken() {
        return this.f4653d != null ? this.f4653d.iotToken : "";
    }

    @Override // com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManage
    public String getIoTIdentity() {
        if (this.f4653d == null) {
            return null;
        }
        return this.f4653d.identity;
    }

    @Override // com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManage
    public String getIoTRefreshToken() {
        return this.f4653d == null ? "" : this.f4653d.refreshToken;
    }

    @Override // com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManage
    public boolean isIoTTokenExpired() {
        if (this.f4653d == null) {
            return true;
        }
        return this.f4653d.isIotTokenExpire();
    }

    @Override // com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManage
    public boolean isIoTRefreshTokenExpired() {
        if (this.f4653d == null) {
            return true;
        }
        return this.f4653d.isRefreshTokenExpire();
    }

    @Override // com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManage
    public IoTCredentialData getIoTCredential() {
        return this.f4653d;
    }

    public void clearIoTTokenInfo() {
        if (this.f4653d != null) {
            IoTCallback ioTCallback = new IoTCallback() { // from class: com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageImpl.5
                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onFailure(IoTRequest ioTRequest, Exception exc) {
                }

                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                }
            };
            if (this.k != null) {
                ALog.i("IoTCredentialManage", "user external credentialDataSource invalid session.");
                this.k.credentialDidDelete(ioTCallback);
            } else {
                new IoTAPIClientFactory().getClient().send(IoTCredentialUtils.getInvalidSessionRequest(getIoTToken(), this.f4653d.identity, h), ioTCallback);
            }
            a(this.f4652c, this.f4653d == null ? "" : this.f4653d.identity, AUTH_IOT_TOKEN_STATUS_INVALID);
            this.f4653d.clear();
            ALog.i("IoTCredentialManage", "clear token data");
            IoTCredentialUtils.saveIoTCredentialData(this.f4652c, this.f4653d);
        }
    }

    class a implements OADepBiz.OALoginStatusChangeListener {
        private a() {
        }

        @Override // com.aliyun.iot.aep.sdk.credential.oa.OADepBiz.OALoginStatusChangeListener, com.aliyun.iot.aep.sdk.login.ILoginStatusChangeListener
        public void onLoginStatusChange() {
            if (ReflectUtils.hasOADep()) {
                if (!OADepBiz.isLogin()) {
                    ALog.i("IoTCredentialManage", "clear iotToken");
                    IoTCredentialManageImpl.this.clearIoTTokenInfo();
                    IoTCredentialManageImpl.this.setCompanyId("");
                    return;
                }
                ALog.i("IoTCredentialManage", "get Login Success info,clear local iotToken");
                if (IoTCredentialManageImpl.this.f4653d != null) {
                    IoTCredentialManageImpl.this.f4653d.clear();
                    IoTCredentialUtils.saveIoTCredentialData(IoTCredentialManageImpl.this.f4652c, IoTCredentialManageImpl.this.f4653d);
                }
                if (TextUtils.equals(IoTCredentialManageImpl.COMPANY_TYPE, IoTCredentialManageImpl.this.getAccountType())) {
                    ALog.i("IoTCredentialManage", "accountType is company, will not refresh IoTToken");
                } else {
                    IoTCredentialManageImpl.this.asyncRefreshIoTCredential(null);
                }
            }
        }
    }
}
