package com.aliyun.alink.sdk.bone.plugins;

import android.content.Context;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.sdk.bone.plugins.alog.BoneALog;
import com.aliyun.alink.sdk.bone.plugins.app.BoneApp;
import com.aliyun.alink.sdk.bone.plugins.config.BoneConfig;
import com.aliyun.alink.sdk.bone.plugins.request.BoneRequest;
import com.aliyun.alink.sdk.bone.plugins.system.BoneSystem;
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
public final class BaseBoneServiceFactory implements BoneServiceFactory {
    @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneServiceFactory
    public String getSDKName() {
        return "BoneBaseSDK";
    }

    @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneServiceFactory
    public String getSDKVersion() {
        return "0.1.5";
    }

    @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneServiceFactory
    public List<String> getServiceNameList() {
        return Arrays.asList(BoneALog.API_NAME, "BoneUserTrack", BoneApp.API_NAME, BoneRequest.API_NAME, BoneSystem.API_NAME, BoneConfig.API_NAME);
    }

    @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneServiceFactory
    public BoneService generateInstance(Context context, String str) {
        if (BoneALog.API_NAME.equalsIgnoreCase(str)) {
            return new BoneService() { // from class: com.aliyun.alink.sdk.bone.plugins.alog.BoneALog$$_Proxy

                /* JADX INFO: renamed from: a, reason: collision with root package name */
                private BoneALog f4460a = new BoneALog();

                @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
                public void onInitialize(Context context2) {
                    a(this.f4460a.getClass());
                    this.f4460a.onInitialize(context2);
                }

                private final void a(Class cls) {
                    if (cls == null) {
                        return;
                    }
                    if (cls == BaseBoneService.class) {
                        try {
                            Field declaredField = cls.getDeclaredField("isBoneInit");
                            declaredField.setAccessible(true);
                            declaredField.set(this.f4460a, false);
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    a(cls.getSuperclass());
                }

                @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
                public boolean onCall(JSContext jSContext, BoneCall boneCall, BoneCallback boneCallback) {
                    JSONArray jSONArray = boneCall.args;
                    if ("log".equalsIgnoreCase(boneCall.methodName)) {
                        this.f4460a.log(jSONArray.optString(0), jSONArray.optString(1), jSONArray.optString(2), boneCallback);
                        return true;
                    }
                    if (!"uploadLog".equalsIgnoreCase(boneCall.methodName)) {
                        return false;
                    }
                    this.f4460a.uploadLog(jSONArray.optJSONObject(0), boneCallback);
                    return true;
                }

                @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
                public void onDestroy() {
                    this.f4460a.onDestroy();
                }
            };
        }
        if ("BoneUserTrack".equalsIgnoreCase(str)) {
            return new BoneService() { // from class: com.aliyun.alink.sdk.bone.plugins.ut.BoneUserTrack$$_Proxy

                /* JADX INFO: renamed from: a, reason: collision with root package name */
                private BoneUserTrack f4481a = new BoneUserTrack();

                @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
                public void onInitialize(Context context2) {
                    a(this.f4481a.getClass());
                    this.f4481a.onInitialize(context2);
                }

                private final void a(Class cls) {
                    if (cls == null) {
                        return;
                    }
                    if (cls == BaseBoneService.class) {
                        try {
                            Field declaredField = cls.getDeclaredField("isBoneInit");
                            declaredField.setAccessible(true);
                            declaredField.set(this.f4481a, false);
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    a(cls.getSuperclass());
                }

                @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
                public boolean onCall(JSContext jSContext, BoneCall boneCall, BoneCallback boneCallback) {
                    JSONArray jSONArray = boneCall.args;
                    if (!"record".equalsIgnoreCase(boneCall.methodName)) {
                        return false;
                    }
                    this.f4481a.record(jSONArray.optString(0), jSONArray.optJSONObject(1), boneCallback);
                    return true;
                }

                @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
                public void onDestroy() {
                    this.f4481a.onDestroy();
                }
            };
        }
        if (BoneApp.API_NAME.equalsIgnoreCase(str)) {
            return new BoneService() { // from class: com.aliyun.alink.sdk.bone.plugins.app.BoneApp$$_Proxy

                /* JADX INFO: renamed from: a, reason: collision with root package name */
                private BoneApp f4463a = new BoneApp();

                @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
                public void onInitialize(Context context2) {
                    a(this.f4463a.getClass());
                    this.f4463a.onInitialize(context2);
                }

                private final void a(Class cls) {
                    if (cls == null) {
                        return;
                    }
                    if (cls == BaseBoneService.class) {
                        try {
                            Field declaredField = cls.getDeclaredField("isBoneInit");
                            declaredField.setAccessible(true);
                            declaredField.set(this.f4463a, false);
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    a(cls.getSuperclass());
                }

                @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
                public boolean onCall(JSContext jSContext, BoneCall boneCall, BoneCallback boneCallback) {
                    JSONArray jSONArray = boneCall.args;
                    if ("reload".equalsIgnoreCase(boneCall.methodName)) {
                        this.f4463a.reload(jSContext, boneCallback);
                        return true;
                    }
                    if (!"exit".equalsIgnoreCase(boneCall.methodName)) {
                        return false;
                    }
                    this.f4463a.exit(jSContext, jSONArray.optJSONObject(0), boneCallback);
                    return true;
                }

                @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
                public void onDestroy() {
                    this.f4463a.onDestroy();
                }
            };
        }
        if (BoneRequest.API_NAME.equalsIgnoreCase(str)) {
            return new BoneService() { // from class: com.aliyun.alink.sdk.bone.plugins.request.BoneRequest$$_Proxy

                /* JADX INFO: renamed from: a, reason: collision with root package name */
                private BoneRequest f4466a = new BoneRequest();

                @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
                public void onInitialize(Context context2) {
                    a(this.f4466a.getClass());
                    this.f4466a.onInitialize(context2);
                }

                private final void a(Class cls) {
                    if (cls == null) {
                        return;
                    }
                    if (cls == BaseBoneService.class) {
                        try {
                            Field declaredField = cls.getDeclaredField("isBoneInit");
                            declaredField.setAccessible(true);
                            declaredField.set(this.f4466a, false);
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    a(cls.getSuperclass());
                }

                @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
                public boolean onCall(JSContext jSContext, BoneCall boneCall, BoneCallback boneCallback) {
                    JSONArray jSONArray = boneCall.args;
                    if (!"send".equalsIgnoreCase(boneCall.methodName)) {
                        return false;
                    }
                    this.f4466a.send(jSONArray.optString(0), jSONArray.optString(1), jSONArray.optJSONObject(2), jSONArray.optJSONObject(3), boneCallback);
                    return true;
                }

                @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
                public void onDestroy() {
                    this.f4466a.onDestroy();
                }
            };
        }
        if (BoneSystem.API_NAME.equalsIgnoreCase(str)) {
            return new BoneService() { // from class: com.aliyun.alink.sdk.bone.plugins.system.BoneSystem$$_Proxy

                /* JADX INFO: renamed from: a, reason: collision with root package name */
                private BoneSystem f4473a = new BoneSystem();

                @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
                public void onInitialize(Context context2) {
                    a(this.f4473a.getClass());
                    this.f4473a.onInitialize(context2);
                }

                private final void a(Class cls) {
                    if (cls == null) {
                        return;
                    }
                    if (cls == BaseBoneService.class) {
                        try {
                            Field declaredField = cls.getDeclaredField("isBoneInit");
                            declaredField.setAccessible(true);
                            declaredField.set(this.f4473a, false);
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    a(cls.getSuperclass());
                }

                @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
                public boolean onCall(JSContext jSContext, BoneCall boneCall, BoneCallback boneCallback) {
                    JSONArray jSONArray = boneCall.args;
                    if ("getSystemInfo".equalsIgnoreCase(boneCall.methodName)) {
                        this.f4473a.getSystemInfo(boneCallback);
                        return true;
                    }
                    if ("getContainerInfo".equalsIgnoreCase(boneCall.methodName)) {
                        this.f4473a.getContainerInfo(boneCallback);
                        return true;
                    }
                    if ("getNetworkType".equalsIgnoreCase(boneCall.methodName)) {
                        this.f4473a.getNetworkType(boneCallback);
                        return true;
                    }
                    if ("getTimeZone".equalsIgnoreCase(boneCall.methodName)) {
                        this.f4473a.getTimeZone(jSONArray.optJSONObject(0), boneCallback);
                        return true;
                    }
                    if ("isMobileDataEnable".equalsIgnoreCase(boneCall.methodName)) {
                        this.f4473a.isMobileDataEnable(boneCallback);
                        return true;
                    }
                    if ("isBluetoothEnabled".equalsIgnoreCase(boneCall.methodName)) {
                        this.f4473a.isBluetoothEnabled(boneCallback);
                        return true;
                    }
                    if ("stopListenNetworkStatusChange".equalsIgnoreCase(boneCall.methodName)) {
                        this.f4473a.stopListenNetworkStatusChange(boneCallback);
                        return true;
                    }
                    if ("startListenNetworkStatusChange".equalsIgnoreCase(boneCall.methodName)) {
                        this.f4473a.startListenNetworkStatusChange(jSContext, boneCallback);
                        return true;
                    }
                    if ("startListenBluetoothStatusChange".equalsIgnoreCase(boneCall.methodName)) {
                        this.f4473a.startListenBluetoothStatusChange(jSContext, boneCallback);
                        return true;
                    }
                    if ("stopListenBluetoothStatusChange".equalsIgnoreCase(boneCall.methodName)) {
                        this.f4473a.stopListenBluetoothStatusChange(boneCallback);
                        return true;
                    }
                    if ("startListenKeyBoardChange".equalsIgnoreCase(boneCall.methodName)) {
                        this.f4473a.startListenKeyBoardChange(jSContext, boneCallback);
                        return true;
                    }
                    if ("stopListenKeyBoardChange".equalsIgnoreCase(boneCall.methodName)) {
                        this.f4473a.stopListenKeyBoardChange(boneCallback);
                        return true;
                    }
                    if ("sendBroadcast".equalsIgnoreCase(boneCall.methodName)) {
                        this.f4473a.sendBroadcast(jSContext, jSONArray.optString(0), jSONArray.optJSONObject(1), boneCallback);
                        return true;
                    }
                    if (!"setStatusBarStyle".equalsIgnoreCase(boneCall.methodName)) {
                        return false;
                    }
                    this.f4473a.setStatusBarStyle(jSContext, jSONArray.optInt(0), boneCallback);
                    return true;
                }

                @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
                public void onDestroy() {
                    this.f4473a.onDestroy();
                }
            };
        }
        if (BoneConfig.API_NAME.equalsIgnoreCase(str)) {
            return new BoneService() { // from class: com.aliyun.alink.sdk.bone.plugins.config.BoneConfig$$_Proxy

                /* JADX INFO: renamed from: a, reason: collision with root package name */
                private BoneConfig f4465a = new BoneConfig();

                @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
                public void onInitialize(Context context2) {
                    a(this.f4465a.getClass());
                    this.f4465a.onInitialize(context2);
                }

                private final void a(Class cls) {
                    if (cls == null) {
                        return;
                    }
                    if (cls == BaseBoneService.class) {
                        try {
                            Field declaredField = cls.getDeclaredField("isBoneInit");
                            declaredField.setAccessible(true);
                            declaredField.set(this.f4465a, false);
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    a(cls.getSuperclass());
                }

                @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
                public boolean onCall(JSContext jSContext, BoneCall boneCall, BoneCallback boneCallback) {
                    JSONArray jSONArray = boneCall.args;
                    if (TmpConstant.PROPERTY_IDENTIFIER_GET.equalsIgnoreCase(boneCall.methodName)) {
                        this.f4465a.get(jSONArray.optJSONArray(0), boneCallback);
                        return true;
                    }
                    if ("getAll".equalsIgnoreCase(boneCall.methodName)) {
                        this.f4465a.getAll(boneCallback);
                        return true;
                    }
                    if (!TmpConstant.PROPERTY_IDENTIFIER_SET.equalsIgnoreCase(boneCall.methodName)) {
                        return false;
                    }
                    this.f4465a.set(jSONArray.optJSONObject(0), boneCallback);
                    return true;
                }

                @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneService
                public void onDestroy() {
                    this.f4465a.onDestroy();
                }
            };
        }
        return null;
    }

    @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneServiceFactory
    public BoneServiceMode getMode(String str) {
        if (BoneALog.API_NAME.equalsIgnoreCase(str)) {
            return BoneServiceMode.ALWAYS_NEW;
        }
        if ("BoneUserTrack".equalsIgnoreCase(str)) {
            return BoneServiceMode.ALWAYS_NEW;
        }
        if (BoneApp.API_NAME.equalsIgnoreCase(str)) {
            return BoneServiceMode.ALWAYS_NEW;
        }
        if (BoneRequest.API_NAME.equalsIgnoreCase(str)) {
            return BoneServiceMode.SINGLE_INSTANCE;
        }
        if (BoneSystem.API_NAME.equalsIgnoreCase(str)) {
            return BoneServiceMode.DEFAULT;
        }
        if (BoneConfig.API_NAME.equalsIgnoreCase(str)) {
            return BoneServiceMode.ALWAYS_NEW;
        }
        return BoneServiceMode.DEFAULT;
    }

    @Override // com.aliyun.iot.aep.sdk.bridge.core.service.BoneServiceFactory
    public List<BoneMethodSpec> getMethods(String str) {
        ArrayList arrayList = new ArrayList();
        if (BoneALog.API_NAME.equalsIgnoreCase(str)) {
            BoneMethodSpec boneMethodSpec = new BoneMethodSpec();
            boneMethodSpec.name = "log";
            boneMethodSpec.parameterTypes = new ArrayList(Arrays.asList(String.class, String.class, String.class, BoneCallback.class));
            arrayList.add(boneMethodSpec);
            BoneMethodSpec boneMethodSpec2 = new BoneMethodSpec();
            boneMethodSpec2.name = "uploadLog";
            boneMethodSpec2.parameterTypes = new ArrayList(Arrays.asList(JSONObject.class, BoneCallback.class));
            arrayList.add(boneMethodSpec2);
        } else if ("BoneUserTrack".equalsIgnoreCase(str)) {
            BoneMethodSpec boneMethodSpec3 = new BoneMethodSpec();
            boneMethodSpec3.name = "record";
            boneMethodSpec3.parameterTypes = new ArrayList(Arrays.asList(String.class, JSONObject.class, BoneCallback.class));
            arrayList.add(boneMethodSpec3);
        } else if (BoneApp.API_NAME.equalsIgnoreCase(str)) {
            BoneMethodSpec boneMethodSpec4 = new BoneMethodSpec();
            boneMethodSpec4.name = "reload";
            boneMethodSpec4.parameterTypes = new ArrayList(Arrays.asList(JSContext.class, BoneCallback.class));
            arrayList.add(boneMethodSpec4);
            BoneMethodSpec boneMethodSpec5 = new BoneMethodSpec();
            boneMethodSpec5.name = "exit";
            boneMethodSpec5.parameterTypes = new ArrayList(Arrays.asList(JSContext.class, JSONObject.class, BoneCallback.class));
            arrayList.add(boneMethodSpec5);
        } else if (BoneRequest.API_NAME.equalsIgnoreCase(str)) {
            BoneMethodSpec boneMethodSpec6 = new BoneMethodSpec();
            boneMethodSpec6.name = "send";
            boneMethodSpec6.parameterTypes = new ArrayList(Arrays.asList(String.class, String.class, JSONObject.class, JSONObject.class, BoneCallback.class));
            arrayList.add(boneMethodSpec6);
        } else if (BoneSystem.API_NAME.equalsIgnoreCase(str)) {
            BoneMethodSpec boneMethodSpec7 = new BoneMethodSpec();
            boneMethodSpec7.name = "getSystemInfo";
            boneMethodSpec7.parameterTypes = new ArrayList(Arrays.asList(BoneCallback.class));
            arrayList.add(boneMethodSpec7);
            BoneMethodSpec boneMethodSpec8 = new BoneMethodSpec();
            boneMethodSpec8.name = "getContainerInfo";
            boneMethodSpec8.parameterTypes = new ArrayList(Arrays.asList(BoneCallback.class));
            arrayList.add(boneMethodSpec8);
            BoneMethodSpec boneMethodSpec9 = new BoneMethodSpec();
            boneMethodSpec9.name = "getNetworkType";
            boneMethodSpec9.parameterTypes = new ArrayList(Arrays.asList(BoneCallback.class));
            arrayList.add(boneMethodSpec9);
            BoneMethodSpec boneMethodSpec10 = new BoneMethodSpec();
            boneMethodSpec10.name = "getTimeZone";
            boneMethodSpec10.parameterTypes = new ArrayList(Arrays.asList(JSONObject.class, BoneCallback.class));
            arrayList.add(boneMethodSpec10);
            BoneMethodSpec boneMethodSpec11 = new BoneMethodSpec();
            boneMethodSpec11.name = "isMobileDataEnable";
            boneMethodSpec11.parameterTypes = new ArrayList(Arrays.asList(BoneCallback.class));
            arrayList.add(boneMethodSpec11);
            BoneMethodSpec boneMethodSpec12 = new BoneMethodSpec();
            boneMethodSpec12.name = "isBluetoothEnabled";
            boneMethodSpec12.parameterTypes = new ArrayList(Arrays.asList(BoneCallback.class));
            arrayList.add(boneMethodSpec12);
            BoneMethodSpec boneMethodSpec13 = new BoneMethodSpec();
            boneMethodSpec13.name = "stopListenNetworkStatusChange";
            boneMethodSpec13.parameterTypes = new ArrayList(Arrays.asList(BoneCallback.class));
            arrayList.add(boneMethodSpec13);
            BoneMethodSpec boneMethodSpec14 = new BoneMethodSpec();
            boneMethodSpec14.name = "startListenNetworkStatusChange";
            boneMethodSpec14.parameterTypes = new ArrayList(Arrays.asList(JSContext.class, BoneCallback.class));
            arrayList.add(boneMethodSpec14);
            BoneMethodSpec boneMethodSpec15 = new BoneMethodSpec();
            boneMethodSpec15.name = "startListenBluetoothStatusChange";
            boneMethodSpec15.parameterTypes = new ArrayList(Arrays.asList(JSContext.class, BoneCallback.class));
            arrayList.add(boneMethodSpec15);
            BoneMethodSpec boneMethodSpec16 = new BoneMethodSpec();
            boneMethodSpec16.name = "stopListenBluetoothStatusChange";
            boneMethodSpec16.parameterTypes = new ArrayList(Arrays.asList(BoneCallback.class));
            arrayList.add(boneMethodSpec16);
            BoneMethodSpec boneMethodSpec17 = new BoneMethodSpec();
            boneMethodSpec17.name = "startListenKeyBoardChange";
            boneMethodSpec17.parameterTypes = new ArrayList(Arrays.asList(JSContext.class, BoneCallback.class));
            arrayList.add(boneMethodSpec17);
            BoneMethodSpec boneMethodSpec18 = new BoneMethodSpec();
            boneMethodSpec18.name = "stopListenKeyBoardChange";
            boneMethodSpec18.parameterTypes = new ArrayList(Arrays.asList(BoneCallback.class));
            arrayList.add(boneMethodSpec18);
            BoneMethodSpec boneMethodSpec19 = new BoneMethodSpec();
            boneMethodSpec19.name = "sendBroadcast";
            boneMethodSpec19.parameterTypes = new ArrayList(Arrays.asList(JSContext.class, String.class, JSONObject.class, BoneCallback.class));
            arrayList.add(boneMethodSpec19);
            BoneMethodSpec boneMethodSpec20 = new BoneMethodSpec();
            boneMethodSpec20.name = "setStatusBarStyle";
            boneMethodSpec20.parameterTypes = new ArrayList(Arrays.asList(JSContext.class, Integer.TYPE, BoneCallback.class));
            arrayList.add(boneMethodSpec20);
        } else if (BoneConfig.API_NAME.equalsIgnoreCase(str)) {
            BoneMethodSpec boneMethodSpec21 = new BoneMethodSpec();
            boneMethodSpec21.name = TmpConstant.PROPERTY_IDENTIFIER_GET;
            boneMethodSpec21.parameterTypes = new ArrayList(Arrays.asList(JSONArray.class, BoneCallback.class));
            arrayList.add(boneMethodSpec21);
            BoneMethodSpec boneMethodSpec22 = new BoneMethodSpec();
            boneMethodSpec22.name = "getAll";
            boneMethodSpec22.parameterTypes = new ArrayList(Arrays.asList(BoneCallback.class));
            arrayList.add(boneMethodSpec22);
            BoneMethodSpec boneMethodSpec23 = new BoneMethodSpec();
            boneMethodSpec23.name = TmpConstant.PROPERTY_IDENTIFIER_SET;
            boneMethodSpec23.parameterTypes = new ArrayList(Arrays.asList(JSONObject.class, BoneCallback.class));
            arrayList.add(boneMethodSpec23);
        }
        return arrayList;
    }
}
