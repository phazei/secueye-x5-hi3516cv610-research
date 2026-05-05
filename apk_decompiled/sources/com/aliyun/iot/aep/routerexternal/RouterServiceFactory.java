package com.aliyun.iot.aep.routerexternal;

import android.content.Context;
import com.aliyun.iot.aep.sdk.bridge.base.BaseBoneService;
import com.aliyun.iot.aep.sdk.bridge.core.context.JSContext;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCall;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCallback;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneMethodSpec;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneService;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneServiceFactory;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneServiceMode;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public final class RouterServiceFactory implements BoneServiceFactory {
    @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneServiceFactory
    public String getSDKName() {
        return "BoneRouterSDK";
    }

    @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneServiceFactory
    public String getSDKVersion() {
        return "0.0.6";
    }

    @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneServiceFactory
    public List<String> getServiceNameList() {
        return Arrays.asList(RouterBoneService.API_NAME);
    }

    @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneServiceFactory
    public BoneService generateInstance(Context context, String str) {
        if (RouterBoneService.API_NAME.equalsIgnoreCase(str)) {
            return new BoneService() { // from class: com.aliyun.iot.aep.routerexternal.RouterBoneService$$_Proxy
                private RouterBoneService boneService = new RouterBoneService();

                @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
                public void onInitialize(Context context2) {
                    setBoneInit(this.boneService.getClass());
                    this.boneService.onInitialize(context2);
                }

                private final void setBoneInit(Class cls) {
                    if (cls == null) {
                        return;
                    }
                    if (cls == BaseBoneService.class) {
                        try {
                            Field declaredField = cls.getDeclaredField("isBoneInit");
                            declaredField.setAccessible(true);
                            declaredField.set(this.boneService, false);
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    setBoneInit(cls.getSuperclass());
                }

                @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
                public boolean onCall(JSContext jSContext, BoneCall boneCall, BoneCallback boneCallback) {
                    JSONArray jSONArray = boneCall.args;
                    if (!"open".equalsIgnoreCase(boneCall.methodName)) {
                        return false;
                    }
                    this.boneService.open(jSContext, jSONArray.optString(0), jSONArray.optJSONObject(1), jSONArray.optJSONObject(2), boneCallback);
                    return true;
                }

                @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
                public void onDestroy() {
                    this.boneService.onDestroy();
                }
            };
        }
        return null;
    }

    @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneServiceFactory
    public BoneServiceMode getMode(String str) {
        if (RouterBoneService.API_NAME.equalsIgnoreCase(str)) {
            return BoneServiceMode.DEFAULT;
        }
        return BoneServiceMode.DEFAULT;
    }

    @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneServiceFactory
    public List<BoneMethodSpec> getMethods(String str) {
        ArrayList arrayList = new ArrayList();
        if (RouterBoneService.API_NAME.equalsIgnoreCase(str)) {
            BoneMethodSpec boneMethodSpec = new BoneMethodSpec();
            boneMethodSpec.name = "open";
            boneMethodSpec.parameterTypes = new ArrayList(Arrays.asList(JSContext.class, String.class, JSONObject.class, JSONObject.class, BoneCallback.class));
            arrayList.add(boneMethodSpec);
        }
        return arrayList;
    }
}
