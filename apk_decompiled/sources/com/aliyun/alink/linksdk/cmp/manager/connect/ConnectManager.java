package com.aliyun.alink.linksdk.cmp.manager.connect;

import android.content.Context;
import android.text.TextUtils;
import com.aliyun.alink.linksdk.cmp.connect.alcs.AlcsConnect;
import com.aliyun.alink.linksdk.cmp.connect.alcs.AlcsConnectConfig;
import com.aliyun.alink.linksdk.cmp.connect.alcs.AlcsDiscoveryConnect;
import com.aliyun.alink.linksdk.cmp.connect.alcs.AlcsServerConnect;
import com.aliyun.alink.linksdk.cmp.connect.alcs.AlcsServerConnectConfig;
import com.aliyun.alink.linksdk.cmp.connect.apigw.ApiGatewayConnect;
import com.aliyun.alink.linksdk.cmp.connect.apigw.ApiGatewayConnectConfig;
import com.aliyun.alink.linksdk.cmp.connect.channel.PersistentConnect;
import com.aliyun.alink.linksdk.cmp.connect.channel.PersistentConnectConfig;
import com.aliyun.alink.linksdk.cmp.connect.hubapi.HubApiClientConnect;
import com.aliyun.alink.linksdk.cmp.connect.hubapi.HubApiClientConnectConfig;
import com.aliyun.alink.linksdk.cmp.core.base.AConnect;
import com.aliyun.alink.linksdk.cmp.core.base.AConnectConfig;
import com.aliyun.alink.linksdk.cmp.core.base.AConnectInfo;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.CmpError;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectAuth;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectInitListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener;
import com.aliyun.alink.linksdk.cmp.manager.connect.auth.alcs.AlcsAuthHttpRequest;
import com.aliyun.alink.linksdk.cmp.manager.connect.auth.alcs.AlcsClientAuthValue;
import com.aliyun.alink.linksdk.cmp.manager.connect.auth.alcs.AlcsServerAuthValue;
import com.aliyun.alink.linksdk.cmp.manager.connect.auth.mqtt.MobileAuthHttpRequest;
import com.aliyun.alink.linksdk.cmp.manager.connect.auth.mqtt.MobileTripleValue;
import com.aliyun.alink.linksdk.cmp.manager.connect.auth.mqtt.MobileTripleValueManager;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes2.dex */
public class ConnectManager {
    private static final String TAG = "ConnectManager";
    private ConcurrentHashMap<IConnectNotifyListener, List<String>> notifyListeners;
    private Map<String, AConnect> connectMap = new ConcurrentHashMap();
    private CommonNotifyListener commonNotifyListener = null;
    private Map<String, String> alcsConnectsMap = null;
    private PersistentConnect persistentConnect = null;
    private boolean persistentConnectNotifyFlag = false;

    private static class InstanceHolder {
        private static final ConnectManager sInstance = new ConnectManager();

        private InstanceHolder() {
        }
    }

    public static ConnectManager getInstance() {
        return InstanceHolder.sInstance;
    }

    public void registerApiGatewayConnect(Context context, ApiGatewayConnectConfig apiGatewayConnectConfig, final IRegisterConnectListener iRegisterConnectListener) {
        ALog.d(TAG, "registerApiGatewayConnect()");
        try {
            synchronized (this) {
                if (this.connectMap.containsKey(ApiGatewayConnect.CONNECT_ID)) {
                    ALog.d(TAG, "registerApiGatewayConnect(),REGISTER_CONNECT_ERROR_EXIST");
                    if (iRegisterConnectListener != null) {
                        new CmpError();
                        iRegisterConnectListener.onFailure(CmpError.REGISTER_CONNECT_ERROR_EXIST());
                    }
                    return;
                }
                ApiGatewayConnect apiGatewayConnect = new ApiGatewayConnect();
                this.connectMap.put(ApiGatewayConnect.CONNECT_ID, apiGatewayConnect);
                apiGatewayConnect.init(context, apiGatewayConnectConfig, new IConnectInitListener() { // from class: com.aliyun.alink.linksdk.cmp.manager.connect.ConnectManager.1
                    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectInitListener
                    public void onPrepareAuth(IConnectAuth iConnectAuth) {
                        ALog.d(ConnectManager.TAG, "registerApiGatewayConnect, onPrepareAuth()");
                    }

                    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
                    public void onSuccess() {
                        ALog.d(ConnectManager.TAG, "registerApiGatewayConnect, onSuccess()");
                        IRegisterConnectListener iRegisterConnectListener2 = iRegisterConnectListener;
                        if (iRegisterConnectListener2 != null) {
                            iRegisterConnectListener2.onSuccess();
                        }
                    }

                    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
                    public void onFailure(AError aError) {
                        ALog.d(ConnectManager.TAG, "registerApiGatewayConnect, onFailure()");
                        ConnectManager.this.connectMap.remove(ApiGatewayConnect.CONNECT_ID);
                        IRegisterConnectListener iRegisterConnectListener2 = iRegisterConnectListener;
                        if (iRegisterConnectListener2 != null) {
                            iRegisterConnectListener2.onFailure(aError);
                        }
                    }
                });
            }
        } catch (Exception e) {
            ALog.e(TAG, "registerApiGatewayConnect error :" + e.toString());
            if (iRegisterConnectListener != null) {
                new CmpError();
                iRegisterConnectListener.onFailure(CmpError.REGISTER_CONNECT_ERROR_EXIST());
            }
        }
    }

    public void registerHubApiClientConnect(Context context, HubApiClientConnectConfig hubApiClientConnectConfig, final IRegisterConnectListener iRegisterConnectListener) {
        ALog.d(TAG, "registerHubApiConnect()");
        try {
            synchronized (this) {
                if (this.connectMap.containsKey(HubApiClientConnect.CONNECT_ID)) {
                    ALog.d(TAG, "registerHubApiClientConnect(),REGISTER_CONNECT_ERROR_EXIST");
                    if (iRegisterConnectListener != null) {
                        new CmpError();
                        iRegisterConnectListener.onFailure(CmpError.REGISTER_CONNECT_ERROR_EXIST());
                    }
                    return;
                }
                HubApiClientConnect hubApiClientConnect = new HubApiClientConnect();
                this.connectMap.put(HubApiClientConnect.CONNECT_ID, hubApiClientConnect);
                hubApiClientConnect.init(context, hubApiClientConnectConfig, new IConnectInitListener() { // from class: com.aliyun.alink.linksdk.cmp.manager.connect.ConnectManager.2
                    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectInitListener
                    public void onPrepareAuth(IConnectAuth iConnectAuth) {
                        ALog.d(ConnectManager.TAG, "registerHubApiConnect, onPrepareAuth()");
                    }

                    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
                    public void onSuccess() {
                        ALog.d(ConnectManager.TAG, "registerHubApiConnect, onSuccess()");
                        IRegisterConnectListener iRegisterConnectListener2 = iRegisterConnectListener;
                        if (iRegisterConnectListener2 != null) {
                            iRegisterConnectListener2.onSuccess();
                        }
                    }

                    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
                    public void onFailure(AError aError) {
                        ALog.d(ConnectManager.TAG, "registerHubApiConnect, onFailure()");
                        ConnectManager.this.connectMap.remove(HubApiClientConnect.CONNECT_ID);
                        IRegisterConnectListener iRegisterConnectListener2 = iRegisterConnectListener;
                        if (iRegisterConnectListener2 != null) {
                            iRegisterConnectListener2.onFailure(aError);
                        }
                    }
                });
            }
        } catch (Exception e) {
            ALog.e(TAG, "registerHubApiClientConnect error :" + e.toString());
            if (iRegisterConnectListener != null) {
                new CmpError();
                iRegisterConnectListener.onFailure(CmpError.REGISTER_CONNECT_ERROR_EXIST());
            }
        }
    }

    public void registerPersistentConnect(final Context context, final PersistentConnectConfig persistentConnectConfig, final IRegisterConnectListener iRegisterConnectListener) {
        ALog.d(TAG, "registerPersistentConnect()");
        try {
            synchronized (this) {
                if (this.connectMap.containsKey(PersistentConnect.CONNECT_ID)) {
                    ALog.d(TAG, "registerPersistentConnect(),REGISTER_CONNECT_ERROR_EXIST");
                    if (iRegisterConnectListener != null) {
                        new CmpError();
                        iRegisterConnectListener.onFailure(CmpError.REGISTER_CONNECT_ERROR_EXIST());
                    }
                    return;
                }
                if (this.persistentConnect == null) {
                    this.persistentConnect = new PersistentConnect();
                }
                this.connectMap.put(PersistentConnect.CONNECT_ID, this.persistentConnect);
                this.persistentConnect.init(context, persistentConnectConfig, new IConnectInitListener() { // from class: com.aliyun.alink.linksdk.cmp.manager.connect.ConnectManager.3
                    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectInitListener
                    public void onPrepareAuth(IConnectAuth iConnectAuth) {
                        ALog.d(ConnectManager.TAG, "registerPersistentConnect, onPrepareAuth()");
                        if (!ConnectManager.this.connectMap.containsKey(ApiGatewayConnect.CONNECT_ID)) {
                            ConnectManager.this.connectMap.remove(PersistentConnect.CONNECT_ID);
                            IRegisterConnectListener iRegisterConnectListener2 = iRegisterConnectListener;
                            if (iRegisterConnectListener2 != null) {
                                new CmpError();
                                iRegisterConnectListener2.onFailure(CmpError.REGISTER_MQTT_CONNECT_ERROR_APIGW_NOT_REG());
                                return;
                            }
                            return;
                        }
                        ConnectManager.this.getTripleValueAndConnect(context, persistentConnectConfig.appkey, persistentConnectConfig.securityGuardAuthcode, iConnectAuth);
                    }

                    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
                    public void onSuccess() {
                        StringBuilder sb = new StringBuilder();
                        sb.append("registerPersistentConnect, onSuccess() persistentConnect=");
                        sb.append(ConnectManager.this.persistentConnect);
                        sb.append(", isDestroyed=");
                        sb.append(ConnectManager.this.persistentConnect == null ? "true" : Boolean.valueOf(ConnectManager.this.persistentConnect.getIsDestroyed()));
                        sb.append(", connectMap=");
                        sb.append(ConnectManager.this.connectMap);
                        ALog.d(ConnectManager.TAG, sb.toString());
                        if (ConnectManager.this.persistentConnect != null && !ConnectManager.this.persistentConnect.getIsDestroyed() && ConnectManager.this.connectMap != null) {
                            ConnectManager.this.connectMap.put(PersistentConnect.CONNECT_ID, ConnectManager.this.persistentConnect);
                        }
                        IRegisterConnectListener iRegisterConnectListener2 = iRegisterConnectListener;
                        if (iRegisterConnectListener2 != null) {
                            iRegisterConnectListener2.onSuccess();
                        }
                    }

                    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
                    public void onFailure(AError aError) {
                        ALog.d(ConnectManager.TAG, "registerPersistentConnect, onFailure()");
                        ConnectManager.this.connectMap.remove(PersistentConnect.CONNECT_ID);
                        IRegisterConnectListener iRegisterConnectListener2 = iRegisterConnectListener;
                        if (iRegisterConnectListener2 != null) {
                            iRegisterConnectListener2.onFailure(aError);
                        }
                    }
                });
                if (this.persistentConnectNotifyFlag) {
                    return;
                }
                if (this.commonNotifyListener == null) {
                    this.commonNotifyListener = new CommonNotifyListener();
                }
                this.persistentConnect.setNotifyListener(this.commonNotifyListener);
                this.persistentConnectNotifyFlag = true;
            }
        } catch (Exception e) {
            ALog.e(TAG, "registerPersistentConnect error:" + e.toString());
            if (iRegisterConnectListener != null) {
                new CmpError();
                iRegisterConnectListener.onFailure(CmpError.REGISTER_CONNECT_ERROR_EXIST());
            }
        }
    }

    public void registerAlcsServerConnect(Context context, AlcsServerConnectConfig alcsServerConnectConfig, final IRegisterConnectListener iRegisterConnectListener) {
        ALog.d(TAG, "registerAlcsServerConnect()");
        try {
            synchronized (this) {
                if (this.connectMap.containsKey(AlcsServerConnect.CONNECT_ID)) {
                    if (iRegisterConnectListener != null) {
                        new CmpError();
                        iRegisterConnectListener.onFailure(CmpError.REGISTER_CONNECT_ERROR_EXIST());
                    }
                } else {
                    AlcsServerConnect alcsServerConnect = new AlcsServerConnect();
                    this.connectMap.put(AlcsServerConnect.CONNECT_ID, alcsServerConnect);
                    alcsServerConnect.init(context, alcsServerConnectConfig, new IConnectInitListener() { // from class: com.aliyun.alink.linksdk.cmp.manager.connect.ConnectManager.4
                        @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectInitListener
                        public void onPrepareAuth(IConnectAuth iConnectAuth) {
                            ALog.d(ConnectManager.TAG, "registerAlcsServerConnect, onPrepareAuth()");
                            ConnectManager.this.authAlcsServer(iConnectAuth);
                        }

                        @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
                        public void onSuccess() {
                            ALog.d(ConnectManager.TAG, "registerAlcsServerConnect, onSuccess()");
                            IRegisterConnectListener iRegisterConnectListener2 = iRegisterConnectListener;
                            if (iRegisterConnectListener2 != null) {
                                iRegisterConnectListener2.onSuccess();
                            }
                        }

                        @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
                        public void onFailure(AError aError) {
                            ALog.d(ConnectManager.TAG, "registerAlcsServerConnect, onFailure()");
                            ConnectManager.this.connectMap.remove(AlcsServerConnect.CONNECT_ID);
                            IRegisterConnectListener iRegisterConnectListener2 = iRegisterConnectListener;
                            if (iRegisterConnectListener2 != null) {
                                iRegisterConnectListener2.onFailure(aError);
                            }
                        }
                    });
                    if (this.commonNotifyListener == null) {
                        this.commonNotifyListener = new CommonNotifyListener();
                    }
                    alcsServerConnect.setNotifyListener(this.commonNotifyListener);
                }
            }
        } catch (Exception e) {
            ALog.e(TAG, "registerAlcsServerConnect error :" + e.toString());
            if (iRegisterConnectListener != null) {
                new CmpError();
                iRegisterConnectListener.onFailure(CmpError.REGISTER_CONNECT_ERROR_EXIST());
            }
        }
    }

    public void registerAlcsDiscoveryConnect(Context context, AConnectConfig aConnectConfig, final IRegisterConnectListener iRegisterConnectListener) {
        ALog.d(TAG, "registerAlcsDiscoveryConnect()");
        try {
            synchronized (this) {
                if (this.connectMap.containsKey(AlcsDiscoveryConnect.CONNECT_ID)) {
                    if (iRegisterConnectListener != null) {
                        new CmpError();
                        iRegisterConnectListener.onFailure(CmpError.REGISTER_CONNECT_ERROR_EXIST());
                    }
                } else {
                    AlcsDiscoveryConnect alcsDiscoveryConnect = new AlcsDiscoveryConnect();
                    this.connectMap.put(AlcsDiscoveryConnect.CONNECT_ID, alcsDiscoveryConnect);
                    alcsDiscoveryConnect.init(context, aConnectConfig, new IConnectInitListener() { // from class: com.aliyun.alink.linksdk.cmp.manager.connect.ConnectManager.5
                        @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectInitListener
                        public void onPrepareAuth(IConnectAuth iConnectAuth) {
                            ALog.d(ConnectManager.TAG, "registerAlcsDiscoveryConnect, onPrepareAuth()");
                        }

                        @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
                        public void onSuccess() {
                            ALog.d(ConnectManager.TAG, "registerAlcsDiscoveryConnect, onSuccess()");
                            IRegisterConnectListener iRegisterConnectListener2 = iRegisterConnectListener;
                            if (iRegisterConnectListener2 != null) {
                                iRegisterConnectListener2.onSuccess();
                            }
                        }

                        @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
                        public void onFailure(AError aError) {
                            ALog.d(ConnectManager.TAG, "registerAlcsDiscoveryConnect, onFailure()");
                            ConnectManager.this.connectMap.remove(AlcsDiscoveryConnect.CONNECT_ID);
                            IRegisterConnectListener iRegisterConnectListener2 = iRegisterConnectListener;
                            if (iRegisterConnectListener2 != null) {
                                iRegisterConnectListener2.onFailure(aError);
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            ALog.e(TAG, "registerAlcsDiscoveryConnect error :" + e.toString());
            if (iRegisterConnectListener != null) {
                new CmpError();
                iRegisterConnectListener.onFailure(CmpError.REGISTER_CONNECT_ERROR_EXIST());
            }
        }
    }

    public void registerAlcsConnect(Context context, final String str, final AlcsConnectConfig alcsConnectConfig, final IRegisterConnectListener iRegisterConnectListener) {
        ALog.d(TAG, "registerAlcsConnect(), connectId = " + str);
        if (TextUtils.isEmpty(str)) {
            if (iRegisterConnectListener != null) {
                new CmpError();
                iRegisterConnectListener.onFailure(CmpError.REGISTER_CONNECT_ERROR_EXIST());
                return;
            }
            return;
        }
        try {
            synchronized (this) {
                if (this.connectMap.containsKey(str)) {
                    if (iRegisterConnectListener != null) {
                        new CmpError();
                        iRegisterConnectListener.onFailure(CmpError.REGISTER_CONNECT_ERROR_EXIST());
                    }
                    return;
                }
                if (this.alcsConnectsMap == null) {
                    this.alcsConnectsMap = new ConcurrentHashMap();
                }
                if (!TextUtils.isEmpty(alcsConnectConfig.getDstAddr())) {
                    this.alcsConnectsMap.put(str, alcsConnectConfig.getDstAddr());
                }
                AlcsConnect alcsConnect = new AlcsConnect();
                this.connectMap.put(str, alcsConnect);
                alcsConnect.setConnectId(str);
                alcsConnect.init(context, alcsConnectConfig, new IConnectInitListener() { // from class: com.aliyun.alink.linksdk.cmp.manager.connect.ConnectManager.6
                    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectInitListener
                    public void onPrepareAuth(IConnectAuth iConnectAuth) {
                        ALog.d(ConnectManager.TAG, "registerAlcsConnect, onPrepareAuth()");
                        ConnectManager.this.authAlcsClient(alcsConnectConfig.getIotId(), iConnectAuth);
                    }

                    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
                    public void onSuccess() {
                        ALog.d(ConnectManager.TAG, "registerAlcsConnect, onSuccess()");
                        IRegisterConnectListener iRegisterConnectListener2 = iRegisterConnectListener;
                        if (iRegisterConnectListener2 != null) {
                            iRegisterConnectListener2.onSuccess();
                        }
                    }

                    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
                    public void onFailure(AError aError) {
                        ALog.d(ConnectManager.TAG, "registerAlcsConnect, onFailure()");
                        if (ConnectManager.this.connectMap != null && ConnectManager.this.connectMap.containsKey(str)) {
                            ALog.d(ConnectManager.TAG, "registerAlcsConnect, onFailure()，remove  connectId =" + str);
                            ConnectManager.this.connectMap.remove(str);
                        }
                        IRegisterConnectListener iRegisterConnectListener2 = iRegisterConnectListener;
                        if (iRegisterConnectListener2 != null) {
                            iRegisterConnectListener2.onFailure(aError);
                        }
                    }
                });
                if (this.commonNotifyListener == null) {
                    this.commonNotifyListener = new CommonNotifyListener();
                }
                alcsConnect.setNotifyListener(this.commonNotifyListener);
            }
        } catch (Exception e) {
            ALog.e(TAG, "registerAlcsConnect error :" + e.toString());
            if (iRegisterConnectListener != null) {
                new CmpError();
                iRegisterConnectListener.onFailure(CmpError.REGISTER_CONNECT_ERROR_EXIST());
            }
        }
    }

    public void unregisterConnect(String str) {
        Map<String, AConnect> map;
        ALog.d(TAG, "unregisterConnect(), connectId = " + str);
        if (TextUtils.isEmpty(str) || (map = this.connectMap) == null || map.size() == 0) {
            return;
        }
        if (this.connectMap.containsKey(str)) {
            AConnect aConnect = this.connectMap.get(str);
            if (aConnect != null) {
                aConnect.onDestroy();
            }
            this.connectMap.remove(str);
        }
        Map<String, String> map2 = this.alcsConnectsMap;
        if (map2 == null || !map2.containsKey(str)) {
            return;
        }
        this.alcsConnectsMap.remove(str);
    }

    @Deprecated
    public void destroyConnect(String str) {
        Map<String, AConnect> map;
        ALog.d(TAG, "destroyConnect(), connectId = " + str);
        if (TextUtils.isEmpty(str) || (map = this.connectMap) == null || map.size() == 0 || !this.connectMap.containsKey(str)) {
            return;
        }
        this.connectMap.get(str).onDestroy();
    }

    public AConnect getApiGatewayConnect() {
        return getConnect(ApiGatewayConnect.CONNECT_ID);
    }

    public AConnect getPersistentConnect() {
        return getConnect(PersistentConnect.CONNECT_ID);
    }

    public AConnect getAlcsMultiportConnnect() {
        return getConnect(AlcsServerConnect.CONNECT_ID);
    }

    public String getAlcsConnectIdWithIp(String str) {
        Map<String, String> map;
        if (TextUtils.isEmpty(str) || (map = this.alcsConnectsMap) == null || map.size() == 0) {
            return null;
        }
        for (String str2 : this.alcsConnectsMap.keySet()) {
            if (this.alcsConnectsMap.get(str2).equals(str)) {
                return str2;
            }
        }
        return null;
    }

    public AConnect getConnect(String str) {
        if (TextUtils.isEmpty(str) || !this.connectMap.containsKey(str)) {
            return null;
        }
        return this.connectMap.get(str);
    }

    public ConnectState getConnectState(String str) {
        AConnect aConnect;
        ALog.d(TAG, "getConnectState()");
        if (TextUtils.isEmpty(str) || (aConnect = this.connectMap.get(str)) == null) {
            return null;
        }
        return aConnect.getConnectState();
    }

    public AConnectInfo getConnectInfo(String str) {
        ALog.d(TAG, "getConnectInfo()");
        if (TextUtils.isEmpty(str) || !this.connectMap.containsKey(str)) {
            return null;
        }
        return this.connectMap.get(str).getConnectInfo();
    }

    public void registerNofityListener(String str, IConnectNotifyListener iConnectNotifyListener) {
        ALog.d(TAG, "registerNofityListener(),connectId = " + str + ", listener = " + iConnectNotifyListener);
        if (PersistentConnect.CONNECT_ID.equals(str) && !this.persistentConnectNotifyFlag) {
            if (this.commonNotifyListener == null) {
                this.commonNotifyListener = new CommonNotifyListener();
            }
            if (this.persistentConnect == null) {
                this.persistentConnect = new PersistentConnect();
            }
            this.persistentConnect.setNotifyListener(this.commonNotifyListener);
            this.persistentConnectNotifyFlag = true;
        }
        if (this.notifyListeners == null) {
            this.notifyListeners = new ConcurrentHashMap<>();
        }
        if (iConnectNotifyListener == null) {
            return;
        }
        List<String> copyOnWriteArrayList = this.notifyListeners.get(iConnectNotifyListener);
        if (copyOnWriteArrayList == null) {
            copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        }
        if (copyOnWriteArrayList.contains(str)) {
            return;
        }
        copyOnWriteArrayList.add(str);
        ALog.d(TAG, "registerNofityListener(),listener = " + iConnectNotifyListener + ",size = " + copyOnWriteArrayList.size());
        this.notifyListeners.put(iConnectNotifyListener, copyOnWriteArrayList);
    }

    public void unregisterNofityListener(IConnectNotifyListener iConnectNotifyListener) {
        ALog.d(TAG, "unregisterNofityListener(),listener = " + iConnectNotifyListener);
        ConcurrentHashMap<IConnectNotifyListener, List<String>> concurrentHashMap = this.notifyListeners;
        if (concurrentHashMap == null || iConnectNotifyListener == null || !concurrentHashMap.containsKey(iConnectNotifyListener)) {
            return;
        }
        ALog.d(TAG, "unregisterNofityListener(),remove " + iConnectNotifyListener);
        this.notifyListeners.remove(iConnectNotifyListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getTripleValueAndConnect(final Context context, String str, String str2, final IConnectAuth iConnectAuth) {
        ALog.d(TAG, "getTripleValueAndConnect");
        MobileTripleValue tripleValue = MobileTripleValueManager.getInstance().getTripleValue(context);
        if (tripleValue != null && tripleValue.checkValid()) {
            if (iConnectAuth != null) {
                iConnectAuth.onAuth(tripleValue.getMap());
            }
        } else if (!TextUtils.isEmpty(str)) {
            MobileAuthHttpRequest.request(context, str, str2, new MobileAuthHttpRequest.IAuthHttpRequestCallback() { // from class: com.aliyun.alink.linksdk.cmp.manager.connect.ConnectManager.7
                @Override // com.aliyun.alink.linksdk.cmp.manager.connect.auth.mqtt.MobileAuthHttpRequest.IAuthHttpRequestCallback
                public void onSuceess(MobileTripleValue mobileTripleValue) {
                    if (!MobileTripleValueManager.getInstance().saveTripleValue(context, mobileTripleValue)) {
                        ALog.d(ConnectManager.TAG, "save trilpe error");
                    }
                    IConnectAuth iConnectAuth2 = iConnectAuth;
                    if (iConnectAuth2 != null) {
                        iConnectAuth2.onAuth(mobileTripleValue.getMap());
                    }
                }

                @Override // com.aliyun.alink.linksdk.cmp.manager.connect.auth.mqtt.MobileAuthHttpRequest.IAuthHttpRequestCallback
                public void onFailed(String str3) {
                    ALog.e(ConnectManager.TAG, "mobile Auth onFailed,msg =" + str3);
                    if (iConnectAuth != null) {
                        CmpError cmpErrorREGISTER_MQTT_CONNECT_ERROR_AUTH_FAIL = CmpError.REGISTER_MQTT_CONNECT_ERROR_AUTH_FAIL();
                        cmpErrorREGISTER_MQTT_CONNECT_ERROR_AUTH_FAIL.setSubMsg(str3);
                        iConnectAuth.onPrepareAuthFail(cmpErrorREGISTER_MQTT_CONNECT_ERROR_AUTH_FAIL);
                    }
                }
            });
        } else if (iConnectAuth != null) {
            CmpError cmpErrorREGISTER_MQTT_CONNECT_ERROR_AUTH_FAIL = CmpError.REGISTER_MQTT_CONNECT_ERROR_AUTH_FAIL();
            cmpErrorREGISTER_MQTT_CONNECT_ERROR_AUTH_FAIL.setSubMsg("appkey is empty");
            iConnectAuth.onPrepareAuthFail(cmpErrorREGISTER_MQTT_CONNECT_ERROR_AUTH_FAIL);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void authAlcsClient(String str, final IConnectAuth iConnectAuth) {
        ALog.d(TAG, "authAlcsClient()");
        AlcsAuthHttpRequest.requestClientInfo(str, new AlcsAuthHttpRequest.IAlcsAuthCallback() { // from class: com.aliyun.alink.linksdk.cmp.manager.connect.ConnectManager.8
            @Override // com.aliyun.alink.linksdk.cmp.manager.connect.auth.alcs.AlcsAuthHttpRequest.IAlcsAuthCallback
            public void onSuccess(Object obj) {
                IConnectAuth iConnectAuth2;
                ALog.d(ConnectManager.TAG, "authAlcsClient(),onSuccess");
                if (obj == null || (iConnectAuth2 = iConnectAuth) == null || !(obj instanceof AlcsClientAuthValue)) {
                    return;
                }
                iConnectAuth2.onAuth(((AlcsClientAuthValue) obj).getMap());
            }

            @Override // com.aliyun.alink.linksdk.cmp.manager.connect.auth.alcs.AlcsAuthHttpRequest.IAlcsAuthCallback
            public void onFailed(AError aError) {
                IConnectAuth iConnectAuth2 = iConnectAuth;
                if (iConnectAuth2 != null) {
                    iConnectAuth2.onPrepareAuthFail(aError);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void authAlcsServer(final IConnectAuth iConnectAuth) {
        ALog.d(TAG, "authAlcsServer()");
        AlcsAuthHttpRequest.requestServerInfo(new AlcsAuthHttpRequest.IAlcsAuthCallback() { // from class: com.aliyun.alink.linksdk.cmp.manager.connect.ConnectManager.9
            @Override // com.aliyun.alink.linksdk.cmp.manager.connect.auth.alcs.AlcsAuthHttpRequest.IAlcsAuthCallback
            public void onSuccess(Object obj) {
                IConnectAuth iConnectAuth2;
                ALog.d(ConnectManager.TAG, "authAlcsServer(),onSuccess");
                if (obj == null || (iConnectAuth2 = iConnectAuth) == null || !(obj instanceof AlcsClientAuthValue)) {
                    return;
                }
                iConnectAuth2.onAuth(((AlcsServerAuthValue) obj).getMap());
            }

            @Override // com.aliyun.alink.linksdk.cmp.manager.connect.auth.alcs.AlcsAuthHttpRequest.IAlcsAuthCallback
            public void onFailed(AError aError) {
                IConnectAuth iConnectAuth2 = iConnectAuth;
                if (iConnectAuth2 != null) {
                    iConnectAuth2.onPrepareAuthFail(aError);
                }
            }
        });
    }

    private class CommonNotifyListener implements IConnectNotifyListener {
        @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener
        public boolean shouldHandle(String str, String str2) {
            return true;
        }

        private CommonNotifyListener() {
        }

        @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener
        public void onNotify(String str, String str2, AMessage aMessage) {
            ALog.d(ConnectManager.TAG, "onNotify(), connectId = " + str + " , topic = " + str2 + "");
            if (ConnectManager.this.notifyListeners == null || ConnectManager.this.notifyListeners.size() == 0) {
                return;
            }
            for (IConnectNotifyListener iConnectNotifyListener : ConnectManager.this.notifyListeners.keySet()) {
                ALog.d(ConnectManager.TAG, "onNotify(),listener = " + iConnectNotifyListener);
                List list = (List) ConnectManager.this.notifyListeners.get(iConnectNotifyListener);
                if (list != null && list.contains(str)) {
                    if (!iConnectNotifyListener.shouldHandle(str, str2)) {
                        ALog.d(ConnectManager.TAG, "onNotify(),item should handle return false");
                    } else {
                        ALog.d(ConnectManager.TAG, "onNotify(), send notify");
                        iConnectNotifyListener.onNotify(str, str2, aMessage);
                    }
                }
            }
        }

        @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener
        public void onConnectStateChange(String str, ConnectState connectState) {
            StringBuilder sb = new StringBuilder();
            sb.append("CommonNofity, onConnectStateChange(),state = ");
            sb.append(connectState != null ? connectState.name() : TmpConstant.GROUP_ROLE_UNKNOWN);
            ALog.d(ConnectManager.TAG, sb.toString());
            if (ConnectManager.this.notifyListeners == null || ConnectManager.this.notifyListeners.size() == 0) {
                return;
            }
            for (IConnectNotifyListener iConnectNotifyListener : ConnectManager.this.notifyListeners.keySet()) {
                ALog.d(ConnectManager.TAG, "CommonNofity(),listener = " + iConnectNotifyListener);
                List list = (List) ConnectManager.this.notifyListeners.get(iConnectNotifyListener);
                if (list != null && list.contains(str)) {
                    ALog.d(ConnectManager.TAG, "CommonNofity, onConnectStateChange(), send notify");
                    iConnectNotifyListener.onConnectStateChange(str, connectState);
                }
            }
        }
    }
}
