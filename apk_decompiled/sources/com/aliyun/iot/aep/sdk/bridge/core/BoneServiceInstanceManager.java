package com.aliyun.iot.aep.sdk.bridge.core;

import android.content.Context;
import android.text.TextUtils;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iot.aep.sdk.bridge.core.context.JSContext;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCall;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCallback;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneService;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneServiceFactory;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneServiceMode;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes2.dex */
public class BoneServiceInstanceManager {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static Map<String, BoneServiceFactory> f4604b = new ConcurrentHashMap(16);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static Map<String, BoneService> f4605c = new ConcurrentHashMap(16);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static Map<String, Integer> f4606d = new ConcurrentHashMap(16);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Context f4607a;
    private Map<String, BoneService> e = new ConcurrentHashMap(32);

    public BoneServiceInstanceManager(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context can not be null");
        }
        this.f4607a = context;
    }

    public String getServiceId(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("sdkName can not be empty");
        }
        if (TextUtils.isEmpty(str2)) {
            throw new IllegalArgumentException("serviceName can not be empty");
        }
        String str3 = str + "/" + str2;
        Iterator<Map.Entry<String, BoneServiceFactory>> it = f4604b.entrySet().iterator();
        while (it.hasNext()) {
            String key = it.next().getKey();
            if (Pattern.matches(str3 + "@[0-9]+", key)) {
                return key;
            }
        }
        BoneServiceFactory boneServiceFactoryFindBoneServiceFactory = BoneServiceFactoryRegistry.findBoneServiceFactory(str, str2);
        if (boneServiceFactoryFindBoneServiceFactory == null) {
            return null;
        }
        String str4 = str3 + "@" + boneServiceFactoryFindBoneServiceFactory.hashCode();
        f4604b.put(str4, boneServiceFactoryFindBoneServiceFactory);
        return str4;
    }

    public BoneService getServiceInstance(String str) throws Exception {
        BoneService aVar = this.e.get(str);
        if (aVar == null && f4605c.containsKey(str)) {
            aVar = f4605c.get(str);
            this.e.put(str, aVar);
            f4606d.put(str, Integer.valueOf(f4606d.get(str).intValue() + 1));
        }
        if (aVar == null && f4604b.containsKey(str)) {
            BoneServiceFactory boneServiceFactory = f4604b.get(str);
            String strA = a(str);
            try {
                BoneService boneServiceGenerateInstance = boneServiceFactory.generateInstance(this.f4607a, strA);
                if (boneServiceGenerateInstance == null) {
                    throw new RuntimeException("BoneServiceFactory.generateInstance() return null, service id is " + str);
                }
                try {
                    boneServiceGenerateInstance.onInitialize(this.f4607a);
                    BoneServiceMode mode = boneServiceFactory.getMode(strA);
                    if (BoneServiceMode.DEFAULT == mode) {
                        this.e.put(str, boneServiceGenerateInstance);
                    } else if (BoneServiceMode.SINGLE_INSTANCE == mode) {
                        this.e.put(str, boneServiceGenerateInstance);
                        f4605c.put(str, boneServiceGenerateInstance);
                        f4606d.put(str, 1);
                    } else if (BoneServiceMode.ALWAYS_NEW == mode) {
                        aVar = new a(str, boneServiceGenerateInstance);
                    }
                    aVar = boneServiceGenerateInstance;
                } catch (Exception e) {
                    ALog.e("BoneServiceInstanceManager", "exception happen when call BoneService.initialize()", e);
                    throw e;
                }
            } catch (Exception e2) {
                ALog.e("BoneServiceInstanceManager", "exception happen when call BoneServiceFactory.generateInstance()", e2);
                throw e2;
            }
        }
        if (aVar == null) {
            return null;
        }
        return aVar;
    }

    private String a(String str) {
        int iIndexOf = str.indexOf("/") + 1;
        int iIndexOf2 = str.indexOf("@");
        return iIndexOf >= iIndexOf2 ? "" : str.substring(iIndexOf, iIndexOf2);
    }

    public void reset() {
        for (Map.Entry<String, BoneService> entry : this.e.entrySet()) {
            String key = entry.getKey();
            BoneService value = entry.getValue();
            BoneServiceFactory boneServiceFactory = f4604b.get(key);
            boolean z = true;
            if (boneServiceFactory == null) {
                ALog.e("BoneServiceInstanceManager", "reset: can not find factory for " + key);
            } else if (BoneServiceMode.DEFAULT != boneServiceFactory.getMode(a(key))) {
                if (BoneServiceMode.SINGLE_INSTANCE != boneServiceFactory.getMode(a(key))) {
                    z = false;
                } else {
                    int iIntValue = f4606d.get(key).intValue() - 1;
                    if (iIntValue > 0) {
                        f4606d.put(key, Integer.valueOf(iIntValue));
                        z = false;
                    } else {
                        f4606d.remove(key);
                        f4605c.remove(key);
                    }
                }
            }
            if (z) {
                try {
                    value.onDestroy();
                } catch (Exception e) {
                    ALog.e("BoneServiceInstanceManager", "exception happen when call BoneService.onDestroy(), service name is " + entry.getKey(), e);
                }
            }
        }
        this.e.clear();
    }

    public void onDestroy() {
        reset();
        this.f4607a = null;
    }

    static class a implements BoneService {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        String f4608a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        BoneService f4609b;

        @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
        public void onDestroy() {
        }

        @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
        public void onInitialize(Context context) {
        }

        private a(String str, BoneService boneService) {
            this.f4608a = str;
            this.f4609b = boneService;
        }

        @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
        public boolean onCall(JSContext jSContext, BoneCall boneCall, BoneCallback boneCallback) throws Exception {
            boolean zOnCall = this.f4609b.onCall(jSContext, boneCall, boneCallback);
            try {
                this.f4609b.onDestroy();
                return zOnCall;
            } catch (Exception e) {
                ALog.e("BoneServiceInstanceManager", "exception happen when call BoneService.onDestroy(), service id is " + this.f4608a, e);
                throw e;
            }
        }
    }
}
