package com.taobao.agoo;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.sdk.android.error.ErrorCode;
import com.aliyun.ams.emas.push.AgooInnerService;
import com.aliyun.ams.emas.push.CommonCallback;
import com.aliyun.ams.emas.push.IReportPushArrive;
import com.aliyun.ams.emas.push.PushConfigHolder;
import com.aliyun.ams.emas.push.data.NotificationDataManager;
import com.aliyun.ams.emas.push.notification.CPushMessage;
import com.taobao.accs.ACCSClient;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.AccsErrorCode;
import com.taobao.accs.AccsException;
import com.taobao.accs.IACCSManager;
import com.taobao.accs.IAgooAppReceiver;
import com.taobao.accs.client.AdapterGlobalClientInfo;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AdapterUtilityImpl;
import com.taobao.accs.utl.UTMini;
import com.taobao.accs.utl.UtilityImpl;
import com.taobao.agoo.control.RequestListener;
import com.taobao.agoo.control.data.AliasDO;
import com.taobao.agoo.control.data.RegisterDO;
import com.taobao.agoo.control.data.SwitchDO;
import com.xiaomi.mipush.sdk.MiPushClient;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.android.agoo.common.CallBack;
import org.android.agoo.common.Config;
import org.android.agoo.control.AgooFactory;

/* JADX INFO: loaded from: classes3.dex */
public final class TaobaoRegister {
    private static final int EVENT_ID = 66001;
    static final String PREFERENCES = "Agoo_AppStore";
    static final String PROPERTY_APP_NOTIFICATION_CUSTOM_SOUND = "app_notification_custom_sound";
    static final String PROPERTY_APP_NOTIFICATION_ICON = "app_notification_icon";
    static final String PROPERTY_APP_NOTIFICATION_SOUND = "app_notification_sound";
    static final String PROPERTY_APP_NOTIFICATION_VIBRATE = "app_notification_vibrate";
    private static final String SERVICEID = "agooSend";
    protected static final String TAG = "TaobaoRegister";
    private static RequestListener mRequestListener;

    private interface BuildAliasData {
        byte[] build(String str, String str2);
    }

    @Deprecated
    public static void setBuilderSound(Context context, String str) {
    }

    @Deprecated
    public static void setNotificationIcon(Context context, int i) {
    }

    @Deprecated
    public static void setNotificationSound(Context context, boolean z) {
    }

    @Deprecated
    public static void setNotificationVibrate(Context context, boolean z) {
    }

    private TaobaoRegister() {
        throw new UnsupportedOperationException();
    }

    public static synchronized void setAccsConfigTag(Context context, String str) {
        Config.mAccsConfigTag = str;
        AccsClientConfig configByTag = AccsClientConfig.getConfigByTag(str);
        if (configByTag == null) {
            throw new RuntimeException("accs config not exist!! please set accs config first!!");
        }
        ALog.i(TAG, "setAccsConfigTag", "config", configByTag.toString());
        AdapterGlobalClientInfo.mAuthCode = configByTag.getAuthCode();
        Config.setAgooAppKey(context, configByTag.getAppKey());
        AdapterUtilityImpl.mAgooAppSecret = configByTag.getAppSecret();
        if (!TextUtils.isEmpty(AdapterUtilityImpl.mAgooAppSecret)) {
            AdapterGlobalClientInfo.mSecurityType = 2;
        }
        PushConfigHolder.init(context);
    }

    @Deprecated
    public static synchronized void register(Context context, String str, String str2, String str3, IRegister iRegister) throws AccsException {
        register(context, str, str, str2, str3, iRegister);
    }

    public static synchronized void register(Context context, String str, final String str2, String str3, final String str4, final IRegister iRegister) throws AccsException {
        if (context != null) {
            if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str)) {
                ALog.i(TAG, "register", "appKey", str2, Constants.KEY_CONFIG_TAG, str);
                final Context applicationContext = context.getApplicationContext();
                Config.mAccsConfigTag = str;
                Config.setAgooAppKey(context, str2);
                AdapterUtilityImpl.mAgooAppSecret = str3;
                if (!TextUtils.isEmpty(str3)) {
                    AdapterGlobalClientInfo.mSecurityType = 2;
                }
                PushConfigHolder.init(context);
                AccsClientConfig configByTag = AccsClientConfig.getConfigByTag(str);
                if (configByTag == null) {
                    new AccsClientConfig.Builder().setAppKey(str2).setAppSecret(str3).setTag(str).build();
                } else {
                    AdapterGlobalClientInfo.mAuthCode = configByTag.getAuthCode();
                }
                final IACCSManager accsInstance = ACCSManager.getAccsInstance(context, str2, str);
                accsInstance.bindApp(applicationContext, str2, str3, str4, new IAgooAppReceiver() { // from class: com.taobao.agoo.TaobaoRegister.1
                    @Override // com.taobao.accs.IAppReceiverV2
                    public void onBindApp(int i, String str5, String str6) {
                        if (i == AccsErrorCode.SUCCESS.getCodeInt()) {
                            onBindApp(i, str6);
                        } else if (iRegister != null) {
                            ErrorCode errorCodeBuild = AgooErrorCode.converAccsErrorCode(i, str5).detail("bindApp").build();
                            iRegister.onFailure(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
                        }
                    }

                    @Override // com.taobao.accs.IAppReceiverV2, com.taobao.accs.IAppReceiverV1
                    public void onBindApp(int i, String str5) {
                        try {
                            ALog.i(TaobaoRegister.TAG, "onBindApp", "errorCode", Integer.valueOf(i));
                            if (i == AccsErrorCode.SUCCESS.getCodeInt()) {
                                if (TaobaoRegister.mRequestListener == null) {
                                    RequestListener unused = TaobaoRegister.mRequestListener = new RequestListener(applicationContext);
                                }
                                accsInstance.registerDataListener(applicationContext, TaobaoConstants.SERVICE_ID_DEVICECMD, TaobaoRegister.mRequestListener);
                                if (RequestListener.mAgooBindCache.isAgooRegistered(applicationContext.getPackageName()) && Config.getDeviceToken(applicationContext) != null) {
                                    ALog.i(TaobaoRegister.TAG, "agoo aready Registered return ", new Object[0]);
                                    if (iRegister != null) {
                                        iRegister.onSuccess(Config.getDeviceToken(applicationContext));
                                        return;
                                    }
                                    return;
                                }
                                byte[] bArrBuildRegister = RegisterDO.buildRegister(applicationContext, str2, str4);
                                if (bArrBuildRegister == null) {
                                    if (iRegister != null) {
                                        iRegister.onFailure(AgooErrorCode.REGISTER_DATA_ERROR.getCode(), AgooErrorCode.REGISTER_DATA_ERROR.getMsg());
                                        return;
                                    }
                                    return;
                                }
                                String strSendRequest = accsInstance.sendRequest(applicationContext, new ACCSManager.AccsRequest(null, TaobaoConstants.SERVICE_ID_DEVICECMD, bArrBuildRegister, null));
                                if (TextUtils.isEmpty(strSendRequest)) {
                                    if (iRegister != null) {
                                        iRegister.onFailure(AgooErrorCode.REGISTER_DATA_ERROR.getCode(), AgooErrorCode.REGISTER_DATA_ERROR.getMsg());
                                        return;
                                    }
                                    return;
                                } else {
                                    if (iRegister != null) {
                                        TaobaoRegister.mRequestListener.mListeners.put(strSendRequest, iRegister);
                                        return;
                                    }
                                    return;
                                }
                            }
                            if (iRegister != null) {
                                ErrorCode errorCodeBuild = AgooErrorCode.converAccsErrorCode(i, "no error msg").detail("bindApp").build();
                                iRegister.onFailure(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
                            }
                        } catch (Throwable th) {
                            ALog.e(TaobaoRegister.TAG, "register onBindApp", th, new Object[0]);
                        }
                    }

                    @Override // com.taobao.accs.IAgooAppReceiver
                    public String getAppkey() {
                        return str2;
                    }
                });
                return;
            }
        }
        ALog.e(TAG, "register params null", "appkey", str2, Constants.KEY_CONFIG_TAG, str);
    }

    private static class ListAliasBuilder implements BuildAliasData {
        private ListAliasBuilder() {
        }

        @Override // com.taobao.agoo.TaobaoRegister.BuildAliasData
        public byte[] build(String str, String str2) {
            return AliasDO.buildListAliasOnThisDevice(str, str2);
        }
    }

    private static class AddAliasBuilder implements BuildAliasData {
        private final String alias;

        private AddAliasBuilder(String str) {
            this.alias = str;
        }

        @Override // com.taobao.agoo.TaobaoRegister.BuildAliasData
        public byte[] build(String str, String str2) {
            return AliasDO.buildAddAliasToCurrentDevice(str, str2, this.alias);
        }
    }

    private static class RemoveAliasBuilder implements BuildAliasData {
        private final String alias;
        private final String token;

        private RemoveAliasBuilder(String str, String str2) {
            this.alias = str;
            this.token = str2;
        }

        @Override // com.taobao.agoo.TaobaoRegister.BuildAliasData
        public byte[] build(String str, String str2) {
            return AliasDO.buildRemoveAliasFromThisDevice(str, str2, this.alias, this.token);
        }
    }

    private static class RemoveAllAliasBuilder implements BuildAliasData {
        private RemoveAllAliasBuilder() {
        }

        @Override // com.taobao.agoo.TaobaoRegister.BuildAliasData
        public byte[] build(String str, String str2) {
            return AliasDO.buildRemoveAllAliasFromThisDevice(str, str2);
        }
    }

    private static class ResetAliasBuilder implements BuildAliasData {
        private final String alias;

        public ResetAliasBuilder(String str) {
            this.alias = str;
        }

        @Override // com.taobao.agoo.TaobaoRegister.BuildAliasData
        public byte[] build(String str, String str2) {
            return AliasDO.buildRemoveAllDeviceWithThisAliasAndBindCurrentDevice(str, str2, this.alias);
        }
    }

    private static class ResetDeviceBuilder implements BuildAliasData {
        private final String alias;

        public ResetDeviceBuilder(String str) {
            this.alias = str;
        }

        @Override // com.taobao.agoo.TaobaoRegister.BuildAliasData
        public byte[] build(String str, String str2) {
            return AliasDO.buildRemoveAllAliasFromThisDeviceAndBindThisAlias(str, str2, this.alias);
        }
    }

    private static class ResetAliasDeviceOne2OneBuilder implements BuildAliasData {
        private final String alias;

        public ResetAliasDeviceOne2OneBuilder(String str) {
            this.alias = str;
        }

        @Override // com.taobao.agoo.TaobaoRegister.BuildAliasData
        public byte[] build(String str, String str2) {
            return AliasDO.buildResetAliasDeviceOne2One(str, str2, this.alias);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void doAliasOperation(String str, Context context, ICallback iCallback, BuildAliasData buildAliasData) {
        ErrorCode errorCodeBuild;
        ALog.i(TAG, str, new Object[0]);
        String deviceToken = Config.getDeviceToken(context);
        String agooAppKey = Config.getAgooAppKey(context);
        if (TextUtils.isEmpty(agooAppKey) || TextUtils.isEmpty(deviceToken) || context == null) {
            if (iCallback != null) {
                if (context == null) {
                    errorCodeBuild = AgooErrorCode.INVALID_ARG.copy().detail(str + " context is null").build();
                } else if (TextUtils.isEmpty(deviceToken)) {
                    errorCodeBuild = AgooErrorCode.INVALID_ARG.copy().detail(str + " deviceId is null").build();
                } else {
                    errorCodeBuild = AgooErrorCode.INVALID_ARG.copy().detail(str + " appKey is null").build();
                }
                iCallback.onFailure(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
            }
            ALog.e(TAG, str + " param null", "appkey", agooAppKey, "deviceId", deviceToken, "context", context);
            return;
        }
        try {
            if (mRequestListener == null) {
                mRequestListener = new RequestListener(context.getApplicationContext());
            }
            IACCSManager accsInstance = ACCSManager.getAccsInstance(context, agooAppKey, Config.getAccsConfigTag(context));
            if (!RequestListener.mAgooBindCache.isAgooRegistered(context.getPackageName())) {
                if (iCallback != null) {
                    iCallback.onFailure(AgooErrorCode.AGOO_NOT_BIND.getCode(), AgooErrorCode.AGOO_NOT_BIND.getMsg());
                    return;
                }
                return;
            }
            accsInstance.registerDataListener(context, TaobaoConstants.SERVICE_ID_DEVICECMD, mRequestListener);
            String strSendRequest = accsInstance.sendRequest(context, new ACCSManager.AccsRequest(null, TaobaoConstants.SERVICE_ID_DEVICECMD, buildAliasData.build(agooAppKey, deviceToken), null));
            if (TextUtils.isEmpty(strSendRequest)) {
                if (iCallback != null) {
                    iCallback.onFailure(AgooErrorCode.ACCS_CHECK_ERROR.getCode(), AgooErrorCode.ACCS_CHECK_ERROR.getMsg());
                }
            } else if (iCallback != null) {
                mRequestListener.mListeners.put(strSendRequest, iCallback);
            }
        } catch (Throwable th) {
            ALog.e(TAG, str, th, new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void removeAliasInList(Context context, Map<String, String> map, final ICallback iCallback) {
        if (map == null || map.size() == 0) {
            iCallback.onSuccess();
            return;
        }
        final ArrayList arrayList = new ArrayList(map.keySet());
        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        for (final String str : map.keySet()) {
            doAliasOperation(AliasDO.JSON_CMD_REMOVEALIAS, context, new ICallback() { // from class: com.taobao.agoo.TaobaoRegister.2
                @Override // com.taobao.agoo.ICallback
                public void onSuccess() {
                    arrayList.remove(str);
                    if (atomicBoolean.get() || arrayList.size() != 0) {
                        return;
                    }
                    iCallback.onSuccess();
                }

                @Override // com.taobao.agoo.ICallback
                public void onFailure(String str2, String str3) {
                    if (atomicBoolean.compareAndSet(false, true)) {
                        iCallback.onFailure(str2, str3);
                    }
                }
            }, new RemoveAliasBuilder(str, map.get(str)));
        }
    }

    private static ICallback checkNull(ICallback iCallback) {
        return iCallback == null ? new ICallback() { // from class: com.taobao.agoo.TaobaoRegister.3
            @Override // com.taobao.agoo.ICallback
            public void onFailure(String str, String str2) {
            }

            @Override // com.taobao.agoo.ICallback
            public void onSuccess() {
            }
        } : iCallback;
    }

    public static synchronized void setAlias(final Context context, final String str, ICallback iCallback) {
        ALog.i(TAG, "setAlias " + str, new Object[0]);
        final ICallback iCallbackCheckNull = checkNull(iCallback);
        if (context != null && str != null) {
            doAliasOperation("listAlias", context, new IListAliasCallbackInner() { // from class: com.taobao.agoo.TaobaoRegister.4
                @Override // com.taobao.agoo.IListAliasCallbackInner
                public void onSuccess(Map<String, String> map) {
                    TaobaoRegister.removeAliasInList(context, map, new ICallback() { // from class: com.taobao.agoo.TaobaoRegister.4.1
                        @Override // com.taobao.agoo.ICallback
                        public void onSuccess() {
                            TaobaoRegister.doAliasOperation(AliasDO.JSON_CMD_ADDALIAS, context, iCallbackCheckNull, new AddAliasBuilder(str));
                        }

                        @Override // com.taobao.agoo.ICallback
                        public void onFailure(String str2, String str3) {
                            iCallbackCheckNull.onFailure(str2, str3);
                        }
                    });
                }

                @Override // com.taobao.agoo.ICallback
                public void onFailure(String str2, String str3) {
                    ICallback iCallback2 = iCallbackCheckNull;
                    String str4 = str;
                    iCallback2.extra = str4;
                    TaobaoRegister.doAliasOperation(AliasDO.JSON_CMD_ADDALIAS, context, iCallback2, new AddAliasBuilder(str4));
                }
            }, new ListAliasBuilder());
            return;
        }
        ErrorCode errorCodeBuild = AgooErrorCode.INVALID_ARG.copy().detail("setAlias " + context + " " + str).build();
        iCallbackCheckNull.onFailure(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
    }

    public static synchronized void removeAlias(final Context context, ICallback iCallback) {
        ALog.i(TAG, AliasDO.JSON_CMD_REMOVEALIAS, new Object[0]);
        final ICallback iCallbackCheckNull = checkNull(iCallback);
        if (context == null) {
            ErrorCode errorCodeBuild = AgooErrorCode.INVALID_ARG.copy().detail("removeAlias before 2.4.x context is null").build();
            iCallbackCheckNull.onFailure(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
        } else {
            doAliasOperation("removeAllAlias", context, new ICallback() { // from class: com.taobao.agoo.TaobaoRegister.5
                @Override // com.taobao.agoo.ICallback
                public void onSuccess() {
                    iCallbackCheckNull.onSuccess();
                }

                @Override // com.taobao.agoo.ICallback
                public void onFailure(String str, String str2) {
                    TaobaoRegister.doAliasOperation("listAlias", context, new IListAliasCallbackInner() { // from class: com.taobao.agoo.TaobaoRegister.5.1
                        @Override // com.taobao.agoo.IListAliasCallbackInner
                        public void onSuccess(Map<String, String> map) {
                            TaobaoRegister.removeAliasInList(context, map, iCallbackCheckNull);
                        }

                        @Override // com.taobao.agoo.ICallback
                        public void onFailure(String str3, String str4) {
                            ArrayList<String> aliasList = LocalStorage.getAliasList(context);
                            if (aliasList != null && aliasList.size() > 0) {
                                String str5 = aliasList.get(0);
                                String aliasToken = LocalStorage.getAliasToken(context, str5);
                                if (aliasToken == null || aliasToken.isEmpty() || str5 == null) {
                                    iCallbackCheckNull.onFailure(AgooErrorCode.REMOVE_ALIAS_FAIL_NO_TOKEN.getCode(), AgooErrorCode.REMOVE_ALIAS_FAIL_NO_TOKEN.getMsg());
                                    return;
                                } else {
                                    iCallbackCheckNull.extra = str5;
                                    TaobaoRegister.doAliasOperation(AliasDO.JSON_CMD_REMOVEALIAS, context, iCallbackCheckNull, new RemoveAliasBuilder(str5, aliasToken));
                                    return;
                                }
                            }
                            iCallbackCheckNull.onFailure(AgooErrorCode.REMOVE_ALIAS_FAIL_NO_ALIAS.getCode(), AgooErrorCode.REMOVE_ALIAS_FAIL_NO_ALIAS.getMsg());
                        }
                    }, new ListAliasBuilder());
                }
            }, new RemoveAllAliasBuilder());
        }
    }

    public static synchronized void listAlias(Context context, IListAliasCallback iListAliasCallback) {
        ALog.i(TAG, "listAlias", new Object[0]);
        ICallback iCallbackCheckNull = checkNull(iListAliasCallback);
        if (context == null) {
            ErrorCode errorCodeBuild = AgooErrorCode.INVALID_ARG.copy().detail("listAlias context is null").build();
            iCallbackCheckNull.onFailure(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
        } else {
            doAliasOperation("listAlias", context, iCallbackCheckNull, new ListAliasBuilder());
        }
    }

    public static synchronized void addAlias(Context context, String str, ICallback iCallback) {
        ALog.i(TAG, "addAlias", "alias", str);
        ICallback iCallbackCheckNull = checkNull(iCallback);
        if (context != null && str != null) {
            doAliasOperation("addAlias", context, iCallbackCheckNull, new AddAliasBuilder(str));
            return;
        }
        ErrorCode errorCodeBuild = AgooErrorCode.INVALID_ARG.copy().detail("addAlias " + context + " " + str).build();
        iCallbackCheckNull.onFailure(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static synchronized void removeAlias(final Context context, final String str, ICallback iCallback) {
        ALog.i(TAG, "removeAlias " + str, new Object[0]);
        final ICallback iCallbackCheckNull = checkNull(iCallback);
        if (context != null && str != null) {
            doAliasOperation(AliasDO.JSON_CMD_REMOVEALIAS, context, new ICallback() { // from class: com.taobao.agoo.TaobaoRegister.6
                @Override // com.taobao.agoo.ICallback
                public void onSuccess() {
                    iCallbackCheckNull.onSuccess();
                }

                @Override // com.taobao.agoo.ICallback
                public void onFailure(final String str2, final String str3) {
                    TaobaoRegister.doAliasOperation("listAlias", context, new IListAliasCallbackInner() { // from class: com.taobao.agoo.TaobaoRegister.6.1
                        @Override // com.taobao.agoo.IListAliasCallbackInner
                        public void onSuccess(Map<String, String> map) {
                            String str4 = map.get(str);
                            if (str4 != null) {
                                TaobaoRegister.doAliasOperation(AliasDO.JSON_CMD_REMOVEALIAS, context, iCallbackCheckNull, new RemoveAliasBuilder(str, str4));
                            } else {
                                iCallbackCheckNull.onFailure(str2, str3);
                            }
                        }

                        @Override // com.taobao.agoo.ICallback
                        public void onFailure(String str4, String str5) {
                            iCallbackCheckNull.onFailure(str4, str5);
                        }
                    }, new ListAliasBuilder());
                }
            }, new RemoveAliasBuilder(str, null));
            return;
        }
        ErrorCode errorCodeBuild = AgooErrorCode.INVALID_ARG.copy().detail("removeAlias " + context + " " + str).build();
        iCallbackCheckNull.onFailure(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
    }

    public static synchronized void removeAllAliasOnCurrentDevice(Context context, ICallback iCallback) {
        ALog.i(TAG, "removeAllAliasOnCurrentDevice ", new Object[0]);
        ICallback iCallbackCheckNull = checkNull(iCallback);
        if (context == null) {
            ErrorCode errorCodeBuild = AgooErrorCode.INVALID_ARG.copy().detail("removeAllAliasOnCurrentDevice " + context).build();
            iCallbackCheckNull.onFailure(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
            return;
        }
        doAliasOperation("removeAllAliasOnCurrentDevice", context, iCallbackCheckNull, new RemoveAllAliasBuilder());
    }

    public static synchronized void removeAllDeviceOnThisAliasAndBindCurrentDevice(Context context, String str, ICallback iCallback) {
        ALog.i(TAG, "removeAllDeviceOnThisAliasAndBindCurrentDevice alias : " + str, new Object[0]);
        ICallback iCallbackCheckNull = checkNull(iCallback);
        if (context != null && str != null) {
            doAliasOperation("removeAllDeviceOnThisAliasAndBindCurrentDevice", context, iCallbackCheckNull, new ResetAliasBuilder(str));
            return;
        }
        ErrorCode errorCodeBuild = AgooErrorCode.INVALID_ARG.copy().detail("removeAllDeviceOnThisAliasAndBindCurrentDevice context is null").build();
        iCallbackCheckNull.onFailure(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
    }

    public static synchronized void removeAllAliasOnCurrentDeviceAndAddThisAlias(Context context, String str, ICallback iCallback) {
        ALog.i(TAG, "removeAllAliasOnCurrentDeviceAndAddThisAlias alias : " + str, new Object[0]);
        ICallback iCallbackCheckNull = checkNull(iCallback);
        if (context != null && str != null) {
            doAliasOperation("removeAllAliasOnCurrentDeviceAndAddThisAlias", context, iCallbackCheckNull, new ResetDeviceBuilder(str));
            return;
        }
        ErrorCode errorCodeBuild = AgooErrorCode.INVALID_ARG.copy().detail("removeAllAliasOnCurrentDeviceAndAddThisAlias context is null").build();
        iCallbackCheckNull.onFailure(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
    }

    public static synchronized void resetDeviceAndAliasToOne2One(Context context, String str, ICallback iCallback) {
        ALog.i(TAG, "resetDeviceAndAliasToOne2One alias : " + str, new Object[0]);
        ICallback iCallbackCheckNull = checkNull(iCallback);
        if (context != null && str != null) {
            doAliasOperation("resetDeviceAndAliasToOne2One", context, iCallbackCheckNull, new ResetAliasDeviceOne2OneBuilder(str));
            return;
        }
        ErrorCode errorCodeBuild = AgooErrorCode.INVALID_ARG.copy().detail("resetDeviceAndAliasToOne2One " + context + " " + str).build();
        iCallbackCheckNull.onFailure(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
    }

    @Deprecated
    public static void bindAgoo(Context context, String str, String str2, CallBack callBack) {
        bindAgoo(context, null);
    }

    @Deprecated
    public static void unBindAgoo(Context context, String str, String str2, CallBack callBack) {
        unbindAgoo(context, null);
    }

    private static synchronized void sendSwitch(Context context, ICallback iCallback, boolean z) {
        String deviceToken;
        String agooAppKey;
        String deviceId;
        try {
            deviceToken = Config.getDeviceToken(context);
            agooAppKey = Config.getAgooAppKey(context);
            deviceId = UtilityImpl.getDeviceId(context);
        } catch (Throwable th) {
            ALog.e(TAG, "sendSwitch", th, new Object[0]);
        }
        if (!TextUtils.isEmpty(agooAppKey) && context != null && (!TextUtils.isEmpty(deviceToken) || !TextUtils.isEmpty(deviceId))) {
            IACCSManager accsInstance = ACCSManager.getAccsInstance(context, agooAppKey, Config.getAccsConfigTag(context));
            if (mRequestListener == null) {
                mRequestListener = new RequestListener(context.getApplicationContext());
            }
            accsInstance.registerDataListener(context, TaobaoConstants.SERVICE_ID_DEVICECMD, mRequestListener);
            String strSendRequest = accsInstance.sendRequest(context, new ACCSManager.AccsRequest(null, TaobaoConstants.SERVICE_ID_DEVICECMD, SwitchDO.buildSwitchDO(agooAppKey, deviceToken, deviceId, z), null));
            if (TextUtils.isEmpty(strSendRequest)) {
                if (iCallback != null) {
                    iCallback.onFailure(AgooErrorCode.ACCS_CHECK_ERROR.getCode(), AgooErrorCode.ACCS_CHECK_ERROR.getMsg());
                }
            } else if (iCallback != null) {
                mRequestListener.mListeners.put(strSendRequest, iCallback);
            }
            return;
        }
        if (iCallback != null) {
            ErrorCode errorCodeBuild = AgooErrorCode.INVALID_ARG.copy().detail("sendSwitch " + context + " " + agooAppKey + " " + deviceToken + " " + deviceId).build();
            iCallback.onFailure(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
        }
        ALog.e(TAG, "sendSwitch param null", "appkey", agooAppKey, "deviceId", deviceToken, "context", context, SwitchDO.JSON_CMD_ENABLEPUSH, Boolean.valueOf(z));
    }

    public static void bindAgoo(Context context, ICallback iCallback) {
        sendSwitch(context, iCallback, true);
        UTMini.getInstance().commitEvent(EVENT_ID, "bindAgoo", UtilityImpl.getDeviceId(context));
    }

    public static void unbindAgoo(Context context, ICallback iCallback) {
        if (mRequestListener == null) {
            mRequestListener = new RequestListener(context);
        }
        RequestListener.mAgooBindCache.onAgooUnregister(context.getPackageName());
        IACCSManager accsInstance = ACCSManager.getAccsInstance(context, Config.getAgooAppKey(context), Config.getAccsConfigTag(context));
        if (accsInstance == null) {
            return;
        }
        try {
            accsInstance.unbindApp(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        accsInstance.clearBindInfo(context);
        sendSwitch(context, iCallback, false);
        UTMini.getInstance().commitEvent(EVENT_ID, MiPushClient.COMMAND_UNREGISTER, UtilityImpl.getDeviceId(context));
    }

    public static void clickMessage(Context context, String str, String str2) {
        AgooFactory.getInstance(context).clickMessage(context, str, str2);
    }

    public static void dismissMessage(Context context, String str, String str2) {
        AgooFactory.getInstance(context).dismissMessage(context, str, str2);
    }

    public static void pingApp(Context context, String str, String str2, String str3, int i) {
        AgooFactory.getInstance(context).getNotifyManager().pingApp(str, str2, str3, i);
    }

    public static void setAgooMsgReceiveService(String str) {
        AdapterGlobalClientInfo.mAgooCustomServiceName = str;
    }

    public static void setEnv(Context context, @AccsClientConfig.ENV int i) {
        ACCSClient.setEnvironment(context, i);
    }

    @Deprecated
    public static void unregister(Context context, CallBack callBack) {
        unbindAgoo(context, null);
    }

    public static boolean isPushApi() {
        return AgooInnerService.class.getName().equals(AdapterGlobalClientInfo.mAgooCustomServiceName);
    }

    public static void setPushMsgReceiveService(Class cls) {
        AdapterGlobalClientInfo.mAgooCustomServiceName = AgooInnerService.class.getName();
        PushConfigHolder.setMessageIntentService(cls);
    }

    public static void setReportPushArrive(IReportPushArrive iReportPushArrive) {
        PushConfigHolder.setReportPushArrive(iReportPushArrive);
    }

    public static void setDoNotDisturbMode(boolean z) {
        PushConfigHolder.setDoNotDisturbMode(z);
    }

    public static void setDoNotDisturb(int i, int i2, int i3, int i4, CommonCallback commonCallback) {
        PushConfigHolder.setDoNotDisturb(i, i2, i3, i4, commonCallback);
    }

    public static void clickMessage(CPushMessage cPushMessage) {
        PushConfigHolder.clickMessage(cPushMessage);
    }

    public static void dismissMessage(CPushMessage cPushMessage) {
        PushConfigHolder.dismissMessage(cPushMessage);
    }

    public static void clearNotificationCreatedByAliyun(Context context) {
        NotificationDataManager.getInstance().clearNotification(context);
    }

    public static void reset() {
        if (RequestListener.mAgooBindCache != null) {
            RequestListener.mAgooBindCache.clear();
        }
        try {
            ACCSClient.getAccsClient(Config.mAccsConfigTag).reset();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        Config.clear(GlobalClientInfo.getContext());
    }
}
