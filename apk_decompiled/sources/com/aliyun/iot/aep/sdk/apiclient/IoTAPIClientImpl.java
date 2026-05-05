package com.aliyun.iot.aep.sdk.apiclient;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.securesigner.util.Utils;
import com.aliyun.iot.aep.sdk.apiclient.adapter.APIGatewayHttpAdapterImpl;
import com.aliyun.iot.aep.sdk.apiclient.adapter.IoTHttpClientAdapter;
import com.aliyun.iot.aep.sdk.apiclient.adapter.IoTHttpClientAdapterConfig;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponseImpl;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Env;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Method;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.hook.IoTAPIHook;
import com.aliyun.iot.aep.sdk.apiclient.hook.IoTAuthProvider;
import com.aliyun.iot.aep.sdk.apiclient.hook.IoTGlobalAPIHook;
import com.aliyun.iot.aep.sdk.apiclient.hook.IoTMockProvider;
import com.aliyun.iot.aep.sdk.apiclient.hook.IoTRequestPayloadCallback;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestPayload;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestWrapper;
import com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.net.SocketFactory;
import okhttp3.EventListener;
import okhttp3.OkHttpClient;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class IoTAPIClientImpl implements IoTAPIClient {
    public static IoTAPIClientImpl i;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public boolean f4516a = false;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public IoTHttpClientAdapter f4517b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public InitializeConfig f4518c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public h f4519d;
    public f e;
    public e f;
    public i g;
    public WeakReference<Context> h;

    public static class InitializeConfig {

        /* JADX INFO: renamed from: adapter, reason: collision with root package name */
        public IoTHttpClientAdapter f4520adapter;
        public Env apiEnv;

        @Deprecated
        public String appKey;
        public String authCode;
        public long connectTimeout;
        public EventListener eventListener;
        public String host;
        public long readTimeout;
        public SocketFactory socketFactory;
        public long writeTimeout;
        public boolean isHttpConnectionRetry = true;
        public boolean isDebug = false;
    }

    public class a implements IoTRequestPayloadCallback {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ IoTRequest f4521a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final /* synthetic */ IoTCallback f4522b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final /* synthetic */ IoTRequestPayload f4523c;

        public a(IoTRequest ioTRequest, IoTCallback ioTCallback, IoTRequestPayload ioTRequestPayload) {
            this.f4521a = ioTRequest;
            this.f4522b = ioTCallback;
            this.f4523c = ioTRequestPayload;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            IoTAPIClientImpl.this.f.a(exc);
            IoTAPIClientImpl.this.g.a(this.f4522b, this.f4521a, exc);
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.hook.IoTRequestPayloadCallback
        public void onIoTRequestPayloadReady() {
            IoTRequestWrapper ioTRequestWrapper = new IoTRequestWrapper();
            ioTRequestWrapper.request = this.f4521a;
            ioTRequestWrapper.callback = this.f4522b;
            ioTRequestWrapper.payload = this.f4523c;
            IoTAPIClientImpl.this.a(ioTRequestWrapper);
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            IoTAPIClientImpl.this.f.a(ioTResponse);
            IoTAPIClientImpl.this.g.a(this.f4522b, ioTRequest, ioTResponse);
        }
    }

    public class b implements IoTCallback {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ IoTRequestWrapper f4525a;

        public b(IoTRequestWrapper ioTRequestWrapper) {
            this.f4525a = ioTRequestWrapper;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            IoTAPIClientImpl.this.g.a(this.f4525a, exc);
            IoTAPIClientImpl.this.a(this.f4525a, exc);
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            IoTAPIClientImpl.this.g.a(this.f4525a, ioTResponse);
            IoTResponseImpl ioTResponseImpl = new IoTResponseImpl();
            if (200 != ioTResponse.getCode()) {
                ioTResponseImpl.setId(ioTResponse.getId());
                ioTResponseImpl.setCode(ioTResponse.getCode());
                ioTResponseImpl.setMessage(ioTResponse.getMessage());
                ioTResponseImpl.setLocalizedMsg(ioTResponse.getLocalizedMsg());
            } else if (ioTResponse.getData() instanceof JSONObject) {
                JSONObject jSONObject = (JSONObject) ioTResponse.getData();
                ioTResponseImpl.setId(jSONObject.optString("id"));
                ioTResponseImpl.setCode(jSONObject.optInt("code"));
                ioTResponseImpl.setMessage(jSONObject.optString("message"));
                ioTResponseImpl.setLocalizedMsg(jSONObject.optString(AlinkConstants.KEY_LOCALIZED_MSG));
                ioTResponseImpl.setData(jSONObject.opt("data"));
                ioTResponseImpl.setRawData(ioTResponse.getRawData());
            }
            IoTAPIClientImpl.this.a(this.f4525a, ioTResponseImpl);
        }
    }

    public class c implements IoTCallback {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ IoTRequestWrapper f4527a;

        public c(IoTRequestWrapper ioTRequestWrapper) {
            this.f4527a = ioTRequestWrapper;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            IoTAPIClientImpl.this.f.a(exc);
            i iVar = IoTAPIClientImpl.this.g;
            IoTRequestWrapper ioTRequestWrapper = this.f4527a;
            iVar.a(ioTRequestWrapper.callback, ioTRequestWrapper.request, exc);
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            IoTAPIClientImpl.this.f.a(ioTResponse);
            i iVar = IoTAPIClientImpl.this.g;
            IoTRequestWrapper ioTRequestWrapper = this.f4527a;
            iVar.a(ioTRequestWrapper.callback, ioTRequestWrapper.request, ioTResponse);
        }
    }

    public class d implements IoTCallback {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ IoTRequestWrapper f4529a;

        public d(IoTRequestWrapper ioTRequestWrapper) {
            this.f4529a = ioTRequestWrapper;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            IoTAPIClientImpl.this.f.a(exc);
            i iVar = IoTAPIClientImpl.this.g;
            IoTRequestWrapper ioTRequestWrapper = this.f4529a;
            iVar.a(ioTRequestWrapper.callback, ioTRequestWrapper.request, exc);
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            IoTAPIClientImpl.this.f.a(ioTResponse);
            i iVar = IoTAPIClientImpl.this.g;
            IoTRequestWrapper ioTRequestWrapper = this.f4529a;
            iVar.a(ioTRequestWrapper.callback, ioTRequestWrapper.request, ioTResponse);
        }
    }

    public class e {
        public e() {
        }

        public /* synthetic */ e(IoTAPIClientImpl ioTAPIClientImpl, a aVar) {
            this();
        }

        public final void b(String str) {
            if (TextUtils.isEmpty(str)) {
                throw new IllegalArgumentException("feature can not be empty");
            }
        }

        public final void a() {
            if (!IoTAPIClientImpl.this.f4516a) {
                throw new RuntimeException("initialize first");
            }
        }

        public final void a(IoTRequest ioTRequest, IoTCallback ioTCallback) {
            if (ioTRequest != null) {
                if (Method.POST == ioTRequest.getMethod()) {
                    if (!TextUtils.isEmpty(ioTRequest.getPath())) {
                        if (TextUtils.isEmpty(ioTRequest.getAPIVersion())) {
                            throw new IllegalArgumentException("apiVersion can not be empty");
                        }
                        if (ioTCallback == null) {
                            throw new IllegalArgumentException("callback can not be null");
                        }
                        return;
                    }
                    throw new IllegalArgumentException("path can not be empty !");
                }
                throw new IllegalArgumentException("only support POST at present");
            }
            throw new IllegalArgumentException("request can not be null");
        }

        public final void a(IoTResponse ioTResponse) {
            if (ioTResponse == null) {
                throw new IllegalArgumentException("response can not be null");
            }
        }

        public final void a(String str) {
            if (TextUtils.isEmpty(str)) {
                throw new IllegalArgumentException("authType can not be empty");
            }
        }

        public final void a(IoTAuthProvider ioTAuthProvider) {
            if (ioTAuthProvider == null) {
                throw new IllegalArgumentException("ioTAuthProvider can not be null");
            }
        }

        public final void a(IoTMockProvider ioTMockProvider) {
            if (ioTMockProvider == null) {
                throw new IllegalArgumentException("ioTMockProvider can not be null");
            }
        }

        public final void a(String str, IoTAPIHook ioTAPIHook) {
            if (TextUtils.isEmpty(str)) {
                throw new IllegalArgumentException("feature can not be empty");
            }
            if (ioTAPIHook != null) {
                if (IoTAPIClientImpl.this.e.a(str)) {
                    throw new IllegalArgumentException(String.format(Locale.ENGLISH, "hook with %s has been registered", str));
                }
                return;
            }
            throw new IllegalArgumentException("hook can not be empty");
        }

        public final void a(Exception exc) {
            if (exc == null) {
                throw new IllegalArgumentException("e can not be null");
            }
        }

        public final void a(Tracker tracker) {
            if (tracker == null) {
                throw new IllegalArgumentException("trackerManager can not be null");
            }
        }
    }

    public class f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public IoTGlobalAPIHook f4532a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public Map<String, IoTAPIHook> f4533b;

        public class a implements IoTRequestPayloadCallback {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public final /* synthetic */ IoTRequest f4534a;

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            public final /* synthetic */ IoTRequestPayload f4535b;

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            public final /* synthetic */ IoTRequestPayloadCallback f4536c;

            public a(IoTRequest ioTRequest, IoTRequestPayload ioTRequestPayload, IoTRequestPayloadCallback ioTRequestPayloadCallback) {
                this.f4534a = ioTRequest;
                this.f4535b = ioTRequestPayload;
                this.f4536c = ioTRequestPayloadCallback;
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                f.this.f4532a.onInterceptFailure(ioTRequest, this.f4535b, exc, this.f4536c);
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.hook.IoTRequestPayloadCallback
            public void onIoTRequestPayloadReady() {
                f.this.f4532a.onInterceptSend(this.f4534a, this.f4535b, this.f4536c);
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                f.this.f4532a.onInterceptResponse(ioTRequest, this.f4535b, ioTResponse, this.f4536c);
            }
        }

        public class b implements IoTCallback {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public final /* synthetic */ IoTRequestPayload f4538a;

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            public final /* synthetic */ IoTCallback f4539b;

            public b(IoTRequestPayload ioTRequestPayload, IoTCallback ioTCallback) {
                this.f4538a = ioTRequestPayload;
                this.f4539b = ioTCallback;
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                f.this.f4532a.onInterceptFailure(ioTRequest, this.f4538a, exc, this.f4539b);
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                f.this.f4532a.onInterceptResponse(ioTRequest, this.f4538a, ioTResponse, this.f4539b);
            }
        }

        public f(IoTAPIClientImpl ioTAPIClientImpl) {
            this.f4532a = new IoTGlobalAPIHook();
            this.f4533b = new HashMap();
        }

        public final void b(String str) {
            this.f4533b.remove(str);
        }

        public /* synthetic */ f(IoTAPIClientImpl ioTAPIClientImpl, a aVar) {
            this(ioTAPIClientImpl);
        }

        public final void a(String str, IoTAPIHook ioTAPIHook) {
            this.f4533b.put(str, ioTAPIHook);
        }

        public final boolean a(String str) {
            return this.f4533b.containsKey(str);
        }

        public final IoTRequestPayloadCallback a(IoTRequest ioTRequest, IoTRequestPayload ioTRequestPayload, IoTRequestPayloadCallback ioTRequestPayloadCallback) {
            return new a(ioTRequest, ioTRequestPayload, ioTRequestPayloadCallback);
        }

        public final void a(String str, IoTRequest ioTRequest, IoTRequestPayload ioTRequestPayload, IoTRequestPayloadCallback ioTRequestPayloadCallback) {
            IoTRequestPayloadCallback ioTRequestPayloadCallbackA = a(ioTRequest, ioTRequestPayload, ioTRequestPayloadCallback);
            IoTAPIHook ioTAPIHook = this.f4533b.get(str);
            if (ioTAPIHook == null) {
                ioTRequestPayloadCallbackA.onIoTRequestPayloadReady();
                return;
            }
            try {
                ioTAPIHook.onInterceptSend(ioTRequest, ioTRequestPayload, ioTRequestPayloadCallbackA);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public final IoTCallback a(IoTRequestPayload ioTRequestPayload, IoTCallback ioTCallback) {
            return new b(ioTRequestPayload, ioTCallback);
        }

        public final void a(String str, IoTRequestWrapper ioTRequestWrapper, Exception exc, IoTCallback ioTCallback) {
            IoTCallback ioTCallbackA = a(ioTRequestWrapper.payload, ioTCallback);
            IoTAPIHook ioTAPIHook = this.f4533b.get(str);
            IoTRequest ioTRequest = ioTRequestWrapper.request;
            IoTRequestPayload ioTRequestPayload = ioTRequestWrapper.payload;
            if (ioTAPIHook == null) {
                ioTCallbackA.onFailure(ioTRequest, exc);
                return;
            }
            try {
                ioTAPIHook.onInterceptFailure(ioTRequest, ioTRequestPayload, exc, ioTCallbackA);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public final void a(String str, IoTRequestWrapper ioTRequestWrapper, IoTResponse ioTResponse, IoTCallback ioTCallback) {
            IoTCallback ioTCallbackA = a(ioTRequestWrapper.payload, ioTCallback);
            IoTAPIHook ioTAPIHook = this.f4533b.get(str);
            IoTRequest ioTRequest = ioTRequestWrapper.request;
            IoTRequestPayload ioTRequestPayload = ioTRequestWrapper.payload;
            if (ioTAPIHook == null) {
                ioTCallbackA.onResponse(ioTRequest, ioTResponse);
                return;
            }
            try {
                ioTAPIHook.onInterceptResponse(ioTRequest, ioTRequestPayload, ioTResponse, ioTCallbackA);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class g {
        public g() {
        }

        public /* synthetic */ g(a aVar) {
            this();
        }

        public final void a(String str) {
            if ("api-performance.aliplus.com".equalsIgnoreCase(str)) {
                try {
                    Field declaredField = IoTRequestBuilder.class.getDeclaredField("defaultScheme");
                    if (declaredField != null) {
                        declaredField.setAccessible(true);
                        declaredField.set(null, Scheme.HTTP);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public class h {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public List<Tracker> f4541a;

        public h(IoTAPIClientImpl ioTAPIClientImpl) {
            this.f4541a = new LinkedList();
        }

        public final void b(Tracker tracker) {
            synchronized (this.f4541a) {
                if (this.f4541a.contains(tracker)) {
                    this.f4541a.remove(tracker);
                }
            }
        }

        public /* synthetic */ h(IoTAPIClientImpl ioTAPIClientImpl, a aVar) {
            this(ioTAPIClientImpl);
        }

        public final void a(Tracker tracker) {
            synchronized (this.f4541a) {
                if (this.f4541a.contains(tracker)) {
                    return;
                }
                this.f4541a.add(tracker);
            }
        }

        public final void a() {
            synchronized (this.f4541a) {
                this.f4541a.clear();
            }
        }

        public final void a(IoTRequest ioTRequest) {
            ArrayList arrayList = new ArrayList();
            synchronized (this.f4541a) {
                arrayList.addAll(this.f4541a);
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                try {
                    ((Tracker) it.next()).onSend(ioTRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public final void a(IoTRequest ioTRequest, Exception exc) {
            ArrayList arrayList = new ArrayList();
            synchronized (this.f4541a) {
                arrayList.addAll(this.f4541a);
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                try {
                    ((Tracker) it.next()).onFailure(ioTRequest, exc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public final void a(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            ArrayList arrayList = new ArrayList();
            synchronized (this.f4541a) {
                arrayList.addAll(this.f4541a);
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                try {
                    ((Tracker) it.next()).onResponse(ioTRequest, ioTResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public final void a(IoTRequestWrapper ioTRequestWrapper) {
            ArrayList arrayList = new ArrayList();
            synchronized (this.f4541a) {
                arrayList.addAll(this.f4541a);
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                try {
                    ((Tracker) it.next()).onRealSend(ioTRequestWrapper);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public final void a(IoTRequestWrapper ioTRequestWrapper, Exception exc) {
            ArrayList arrayList = new ArrayList();
            synchronized (this.f4541a) {
                arrayList.addAll(this.f4541a);
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                try {
                    ((Tracker) it.next()).onRawFailure(ioTRequestWrapper, exc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public final void a(IoTRequestWrapper ioTRequestWrapper, IoTResponse ioTResponse) {
            ArrayList arrayList = new ArrayList();
            synchronized (this.f4541a) {
                arrayList.addAll(this.f4541a);
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                try {
                    ((Tracker) it.next()).onRawResponse(ioTRequestWrapper, ioTResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class i {
        public i() {
        }

        public /* synthetic */ i(IoTAPIClientImpl ioTAPIClientImpl, a aVar) {
            this();
        }

        public final void a(IoTRequest ioTRequest) {
            IoTAPIClientImpl.this.f4519d.a(ioTRequest);
        }

        public final void a(IoTCallback ioTCallback, IoTRequest ioTRequest, Exception exc) {
            IoTAPIClientImpl.this.f4519d.a(ioTRequest, exc);
            ioTCallback.onFailure(ioTRequest, exc);
        }

        public final void a(IoTCallback ioTCallback, IoTRequest ioTRequest, IoTResponse ioTResponse) {
            IoTAPIClientImpl.this.f4519d.a(ioTRequest, ioTResponse);
            ioTCallback.onResponse(ioTRequest, ioTResponse);
        }

        public final void a(IoTRequestWrapper ioTRequestWrapper) {
            IoTAPIClientImpl.this.f4519d.a(ioTRequestWrapper);
        }

        public final void a(IoTRequestWrapper ioTRequestWrapper, Exception exc) {
            IoTAPIClientImpl.this.f4519d.a(ioTRequestWrapper, exc);
        }

        public final void a(IoTRequestWrapper ioTRequestWrapper, IoTResponse ioTResponse) {
            IoTAPIClientImpl.this.f4519d.a(ioTRequestWrapper, ioTResponse);
        }
    }

    public static IoTAPIClientImpl getInstance() {
        if (i == null) {
            i = new IoTAPIClientImpl();
        }
        return i;
    }

    public void clearTracker() {
        this.f4519d.a();
    }

    public Context getAppContext() {
        WeakReference<Context> weakReference = this.h;
        if (weakReference != null) {
            return weakReference.get();
        }
        return null;
    }

    public String getAuthCode() {
        if (this.f4516a) {
            return this.f4518c.authCode;
        }
        throw new RuntimeException("sdk not initialize !");
    }

    public String getDefaultHost() {
        if (this.f4516a) {
            return this.f4518c.host;
        }
        throw new RuntimeException("sdk not initialize !");
    }

    public OkHttpClient getOkHttpClient() {
        if (!this.f4516a) {
            throw new IllegalStateException("apiclient not initialized.");
        }
        IoTHttpClientAdapter ioTHttpClientAdapter = this.f4517b;
        if (ioTHttpClientAdapter instanceof APIGatewayHttpAdapterImpl) {
            return ((APIGatewayHttpAdapterImpl) ioTHttpClientAdapter).getOkHttpClient();
        }
        return null;
    }

    public boolean hasInited() {
        return this.f4516a;
    }

    public boolean hasIoTAUthProvider(String str) {
        this.f.a();
        this.f.a(str);
        return a("auth/" + str);
    }

    public boolean hasIoTMockProvider(String str) {
        this.f.a();
        this.f.a(str);
        return a("mock/" + str);
    }

    public void init(Context context, InitializeConfig initializeConfig) {
        if (this.f4516a) {
            throw new RuntimeException("can not duplicated initialize !");
        }
        a aVar = null;
        this.f = new e(this, aVar);
        this.g = new i(this, aVar);
        IoTHttpClientAdapter aPIGatewayHttpAdapterImpl = initializeConfig.f4520adapter;
        if (aPIGatewayHttpAdapterImpl == null) {
            IoTHttpClientAdapterConfig.Builder debug = new IoTHttpClientAdapterConfig.Builder().setAppKey(initializeConfig.appKey).setDefaultHost(initializeConfig.host).setApiEnv(initializeConfig.apiEnv).setAuthCode(initializeConfig.authCode).setConnectTimeout(initializeConfig.connectTimeout).setReadTimeout(initializeConfig.readTimeout).setWriteTimeout(initializeConfig.writeTimeout).setEventListener(initializeConfig.eventListener).setSocketFactory(initializeConfig.socketFactory).setHttpConnectionRetry(initializeConfig.isHttpConnectionRetry).setDebug(initializeConfig.isDebug);
            if (TextUtils.isEmpty(initializeConfig.authCode)) {
                initializeConfig.authCode = "114d";
                debug.setAuthCode("114d");
            }
            if (Utils.hasSecurityGuardDep()) {
                APIGatewayHttpAdapterImpl.checkSecurityPicture(context, initializeConfig.authCode);
            }
            if (initializeConfig.appKey == null) {
                String appKey = APIGatewayHttpAdapterImpl.getAppKey(context, initializeConfig.authCode);
                initializeConfig.appKey = appKey;
                debug.setAppKey(appKey);
            }
            aPIGatewayHttpAdapterImpl = new APIGatewayHttpAdapterImpl((Application) context.getApplicationContext(), debug.build());
        }
        if (TextUtils.isEmpty(initializeConfig.appKey)) {
            throw new IllegalArgumentException("appKey can not be empty !");
        }
        if (TextUtils.isEmpty(initializeConfig.host)) {
            throw new IllegalArgumentException("host can not be empty !");
        }
        new g(aVar).a(initializeConfig.host);
        this.f4517b = aPIGatewayHttpAdapterImpl;
        this.e = new f(this, aVar);
        this.f4519d = new h(this, aVar);
        this.h = new WeakReference<>(context.getApplicationContext());
        this.f4516a = true;
        this.f4518c = initializeConfig;
    }

    public void registerIoTAuthProvider(String str, IoTAuthProvider ioTAuthProvider) {
        this.f.a();
        this.f.a(str);
        this.f.a(ioTAuthProvider);
        a("auth/" + str, ioTAuthProvider);
    }

    public void registerMockProvider(String str, IoTMockProvider ioTMockProvider) {
        this.f.a();
        this.f.a(ioTMockProvider);
        a("mock/" + str, ioTMockProvider);
    }

    public void registerTracker(Tracker tracker) {
        this.f.a(tracker);
        this.f4519d.a(tracker);
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient
    public void send(IoTRequest ioTRequest, IoTCallback ioTCallback) {
        this.f.a();
        this.f.a(ioTRequest, ioTCallback);
        this.g.a(ioTRequest);
        a(ioTRequest, ioTCallback);
    }

    public void setDefaultHost(String str) {
        if (!this.f4516a) {
            throw new RuntimeException("sdk not initialize !");
        }
        this.f4518c.host = str;
        IoTHttpClientAdapter ioTHttpClientAdapter = this.f4517b;
        if (ioTHttpClientAdapter != null) {
            ioTHttpClientAdapter.setDefaultHost(str);
        }
    }

    public void setLanguage(String str) {
        this.e.f4532a.setLanguage(str);
    }

    @Deprecated
    public void setPerformanceTracker(Tracker tracker) {
        if (tracker == null) {
            return;
        }
        registerTracker(tracker);
    }

    public void unregisterIoTAuthProvider(String str) {
        this.f.a();
        this.f.a(str);
        b("auth/" + str);
    }

    public void unregisterIoTMockProvider(String str) {
        this.f.a();
        b("mock/" + str);
    }

    public void unregisterTracker(Tracker tracker) {
        this.f.a(tracker);
        this.f4519d.b(tracker);
    }

    public final void b(String str) {
        this.f.b(str);
        this.e.b(str);
    }

    public final void a(String str, IoTAPIHook ioTAPIHook) {
        this.f.a(str, ioTAPIHook);
        this.e.a(str, ioTAPIHook);
    }

    public final boolean a(String str) {
        this.f.b(str);
        return this.e.a(str);
    }

    public final IoTRequestPayload a(IoTRequest ioTRequest) {
        IoTRequestPayload ioTRequestPayload = new IoTRequestPayload(ioTRequest.getId(), "1.0");
        ioTRequestPayload.getRequest().put("apiVer", ioTRequest.getAPIVersion());
        ioTRequestPayload.getParams().putAll(ioTRequest.getParams());
        return ioTRequestPayload;
    }

    public final void a(IoTRequest ioTRequest, IoTCallback ioTCallback) {
        IoTRequestPayload ioTRequestPayloadA = a(ioTRequest);
        String str = "";
        String mockType = ioTRequest.getMockType();
        String authType = ioTRequest.getAuthType();
        if (!TextUtils.isEmpty(mockType)) {
            str = "mock/" + mockType;
        } else if (!TextUtils.isEmpty(authType)) {
            str = "auth/" + authType;
            if (!a(str)) {
                this.g.a(ioTCallback, ioTRequest, new IllegalArgumentException(String.format(Locale.ENGLISH, "unsupported auth type %s, maybe you forgot to register IoTAuthProvider", authType)));
                return;
            }
        }
        this.e.a(str, ioTRequest, ioTRequestPayloadA, new a(ioTRequest, ioTCallback, ioTRequestPayloadA));
    }

    public final void a(IoTRequestWrapper ioTRequestWrapper) {
        this.f4517b.send(ioTRequestWrapper, new b(ioTRequestWrapper));
        this.g.a(ioTRequestWrapper);
    }

    public final void a(IoTRequestWrapper ioTRequestWrapper, Exception exc) {
        String str = "";
        String authType = ioTRequestWrapper.request.getAuthType();
        if (!TextUtils.isEmpty(authType)) {
            str = "auth/" + authType;
        }
        this.e.a(str, ioTRequestWrapper, exc, new c(ioTRequestWrapper));
    }

    public final void a(IoTRequestWrapper ioTRequestWrapper, IoTResponse ioTResponse) {
        String authType = ioTRequestWrapper.request.getAuthType();
        if (TextUtils.isEmpty(authType)) {
            this.g.a(ioTRequestWrapper.callback, ioTRequestWrapper.request, ioTResponse);
            return;
        }
        String str = "auth/" + authType;
        if (!a(str)) {
            this.g.a(ioTRequestWrapper.callback, ioTRequestWrapper.request, new IllegalArgumentException(String.format(Locale.ENGLISH, "unsupported auth type %s, maybe you forgot to register IoTAuthProvider", authType)));
        } else {
            this.e.a(str, ioTRequestWrapper, ioTResponse, new d(ioTRequestWrapper));
        }
    }
}
