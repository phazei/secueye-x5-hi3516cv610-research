package com.taobao.accs;

import android.content.Context;
import android.text.TextUtils;
import anet.channel.AwcnConfig;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.SessionCenter;
import anet.channel.entity.ENV;
import anet.channel.util.ALog;
import com.alibaba.sdk.android.logger.ILog;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.base.AccsAbstractDataListener;
import com.taobao.accs.base.TaoBaseService;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.AccsLogger;
import com.taobao.accs.utl.AdapterUtilityImpl;
import com.taobao.accs.utl.UtilityImpl;
import com.taobao.accs.utl.Utils;
import com.taobao.accs.utl.g;
import com.taobao.accs.utl.i;
import com.taobao.accs.utl.j;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class ACCSClient {
    private static ILog defaultLog = AccsLogger.getLogger("ACCSClient");
    public static Map<String, ACCSClient> mACCSClients = new ConcurrentHashMap(2);
    private static Context mContext;
    private HashSet<ConnectionListener> listeners = new HashSet<>();
    private ILog log;
    protected IACCSManager mAccsManager;
    private AccsClientConfig mConfig;

    @Deprecated
    public static void enableChannelProcessHeartbeat(Context context, boolean z) {
    }

    public ACCSClient(AccsClientConfig accsClientConfig) {
        this.mConfig = accsClientConfig;
        this.log = AccsLogger.getLogger("ACCSClient" + accsClientConfig.getTag());
        this.mAccsManager = ACCSManager.getAccsInstance(mContext, accsClientConfig.getAppKey(), accsClientConfig.getTag());
    }

    public static void changeNetworkSdkLoggerToAccs() {
        defaultLog.d("changeNetworkSdkLoggerToAccs");
        ALog.setLog(new i(new j(), g.a()));
    }

    public static synchronized String init(Context context, AccsClientConfig accsClientConfig) throws AccsException {
        if (context == null || accsClientConfig == null) {
            defaultLog.e("init AccsClient params error", "context", context, "config", accsClientConfig);
            throw new AccsException("init AccsClient params error");
        }
        GlobalClientInfo.getInstance(context);
        mContext = context.getApplicationContext();
        setCurrentProcessName(context);
        defaultLog.d("init", "config", accsClientConfig);
        AccsState.getInstance().a(AccsState.SDK_VERSION, "1.0");
        changeNetworkSdkLoggerToAccs();
        try {
            AwcnConfig.setAccsSessionCreateForbiddenInBg(false);
        } catch (Throwable unused) {
        }
        return accsClientConfig.getTag();
    }

    public static synchronized ACCSClient getAccsClient(String str) throws AccsException {
        if (TextUtils.isEmpty(str)) {
            str = AccsClientConfig.DEFAULT_CONFIGTAG;
            defaultLog.w("getAccsClient with null tag, use default");
        }
        AccsClientConfig configByTag = AccsClientConfig.getConfigByTag(str);
        if (configByTag == null) {
            defaultLog.e("getAccsClient with null config, please init config first", Constants.KEY_CONFIG_TAG, str);
            throw new AccsException("configTag not exist");
        }
        ACCSClient aCCSClient = mACCSClients.get(str);
        if (aCCSClient == null) {
            defaultLog.d("getAccsClient create client");
            ACCSClient aCCSClient2 = new ACCSClient(configByTag);
            mACCSClients.put(str, aCCSClient2);
            aCCSClient2.updateConfig(configByTag);
            return aCCSClient2;
        }
        if (!configByTag.equals(aCCSClient.mConfig)) {
            defaultLog.w("getAccsClient update config", "old", aCCSClient.mConfig, "new", configByTag);
            aCCSClient.updateConfig(configByTag);
        }
        return aCCSClient;
    }

    @Deprecated
    public static void enableChannelProcess(Context context, boolean z) {
        UtilityImpl.a(context, z);
    }

    public static void setCurrentProcessName(Context context) {
        try {
            GlobalAppRuntimeInfo.setCurrentProcess(AdapterUtilityImpl.getProcessName(context.getApplicationContext()));
        } catch (Throwable th) {
            defaultLog.e("setCurrentProcess", th);
        }
        try {
            GlobalAppRuntimeInfo.setTargetProcess(AdapterUtilityImpl.getTargetProcess(context.getApplicationContext()));
        } catch (Throwable th2) {
            defaultLog.e("setCurrentProcess", th2);
        }
    }

    private void updateConfig(AccsClientConfig accsClientConfig) {
        this.mConfig = accsClientConfig;
        this.mAccsManager = ACCSManager.getAccsInstance(mContext, accsClientConfig.getAppKey(), accsClientConfig.getTag());
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager != null) {
            iACCSManager.updateConfig(accsClientConfig);
        }
    }

    public static synchronized void setEnvironment(Context context, @AccsClientConfig.ENV int i) {
        if (i < 0 || i > 2) {
            try {
                try {
                    defaultLog.w("env invalid, reset to release", "env", Integer.valueOf(i));
                    i = 0;
                } finally {
                    Utils.setMode(context, i);
                }
            } catch (Throwable th) {
                defaultLog.e("setEnvironment", th);
            }
        }
        int i2 = AccsClientConfig.mEnv;
        AccsClientConfig.mEnv = i;
        if (i2 != i && AdapterUtilityImpl.isTargetProcess(context)) {
            defaultLog.i("setEnvironment", "pre", Integer.valueOf(i2), "to", Integer.valueOf(i));
            Utils.clearAllSharePreferences(context);
            Utils.clearAgooBindCache(context);
            Utils.killService(context);
            if (i == 2) {
                SessionCenter.switchEnvironment(ENV.TEST);
            } else if (i == 1) {
                SessionCenter.switchEnvironment(ENV.PREPARE);
            }
            Iterator<Map.Entry<String, ACCSClient>> it = mACCSClients.entrySet().iterator();
            while (it.hasNext()) {
                try {
                    getAccsClient(it.next().getKey());
                } catch (AccsException e) {
                    defaultLog.e("setEnvironment update client", e);
                }
            }
        }
    }

    public void bindApp(String str, IAppReceiver iAppReceiver) {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("bindApp mAccsManager null");
            com.taobao.accs.utl.a.a(AccsErrorCode.ERROR_SHOULD_NEVER_HAPPEN.copy().detail("bindApp accs is null").build(), iAppReceiver, null);
        } else {
            iACCSManager.bindApp(mContext, this.mConfig.getAppKey(), this.mConfig.getAppSecret(), str, iAppReceiver);
        }
    }

    public void cleanLocalBindInfo() {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("cleanLocalBindInfo mAccsManager null");
        } else {
            iACCSManager.cleanLocalBindInfo();
        }
    }

    public void bindUser(String str) {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("bindUser mAccsManager null");
        } else {
            iACCSManager.bindUser(mContext, str);
        }
    }

    public void bindUser(String str, boolean z) {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("bindUser mAccsManager null");
        } else {
            iACCSManager.bindUser(mContext, str, z);
        }
    }

    public void unbindUser() {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("unbindUser mAccsManager null");
        } else {
            iACCSManager.unbindUser(mContext);
        }
    }

    public void bindService(String str) {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("bindService mAccsManager null");
        } else {
            iACCSManager.bindService(mContext, str);
        }
    }

    public void unbindService(String str) {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("unbindService mAccsManager null");
        } else {
            iACCSManager.unbindService(mContext, str);
        }
    }

    public String sendData(ACCSManager.AccsRequest accsRequest) {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("sendData mAccsManager null");
            return null;
        }
        return iACCSManager.sendData(mContext, accsRequest);
    }

    public String sendRequest(ACCSManager.AccsRequest accsRequest) {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("sendRequest mAccsManager null");
            return null;
        }
        return iACCSManager.sendRequest(mContext, accsRequest);
    }

    public String sendPushResponse(ACCSManager.AccsRequest accsRequest, TaoBaseService.ExtraInfo extraInfo) {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("sendPushResponse mAccsManager null");
            return null;
        }
        return iACCSManager.sendPushResponse(mContext, accsRequest, extraInfo);
    }

    public boolean isNetworkReachable() {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("isNetworkReachable mAccsManager null");
            return false;
        }
        return iACCSManager.isNetworkReachable(mContext);
    }

    public void startInAppConnection(String str, IAppReceiver iAppReceiver) {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("startInAppConnection mAccsManager null");
        } else {
            iACCSManager.startInAppConnection(mContext, this.mConfig.getAppKey(), this.mConfig.getAppSecret(), str, iAppReceiver);
        }
    }

    public void setLoginInfo(ILoginInfo iLoginInfo) {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("setLoginInfo mAccsManager null");
        } else {
            iACCSManager.setLoginInfo(mContext, iLoginInfo);
        }
    }

    public void clearLoginInfo() {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("clearLoginInfo mAccsManager null");
        } else {
            iACCSManager.clearLoginInfo(mContext);
        }
    }

    public boolean cancel(String str) {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("cancel mAccsManager null");
            return false;
        }
        return iACCSManager.cancel(mContext, str);
    }

    public boolean isChannelError(int i) {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("isChannelError mAccsManager null");
            return true;
        }
        return iACCSManager.isChannelError(i);
    }

    public Map<String, Boolean> getChannelState() throws Exception {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("getChannelState mAccsManager null");
            return null;
        }
        return iACCSManager.getChannelState();
    }

    public Map<String, Boolean> forceReConnectChannel() throws Exception {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("forceReConnectChannel mAccsManager null");
            return null;
        }
        return iACCSManager.forceReConnectChannel();
    }

    public void registerSerivce(String str, String str2) {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("registerSerivce mAccsManager null");
        } else {
            iACCSManager.registerSerivce(mContext, str, str2);
        }
    }

    public void unRegisterSerivce(String str) {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("unRegisterSerivce mAccsManager null");
        } else {
            iACCSManager.unRegisterSerivce(mContext, str);
        }
    }

    public void registerDataListener(String str, AccsAbstractDataListener accsAbstractDataListener) {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("registerDataListener mAccsManager null");
        } else {
            iACCSManager.registerDataListener(mContext, str, accsAbstractDataListener);
        }
    }

    public void unRegisterDataListener(String str) {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("unRegisterDataListener mAccsManager null");
        } else {
            iACCSManager.unRegisterDataListener(mContext, str);
        }
    }

    public void sendBusinessAck(String str, String str2, String str3, short s, String str4, Map<Integer, String> map) {
        IACCSManager iACCSManager = this.mAccsManager;
        if (iACCSManager == null) {
            this.log.e("sendBusinessAck mAccsManager null");
        } else {
            iACCSManager.sendBusinessAck(str, str2, str3, s, str4, map);
        }
    }

    public void addConnectionListener(ConnectionListener connectionListener) {
        if (connectionListener != null) {
            this.listeners.add(connectionListener);
        }
    }

    public void removeConnectionListener(ConnectionListener connectionListener) {
        if (connectionListener != null) {
            this.listeners.remove(connectionListener);
        }
    }

    public List<ConnectionListener> getConnectionListeners() {
        return new ArrayList(this.listeners);
    }

    public boolean isConnected() {
        return this.mAccsManager.isConnected();
    }

    public int getLastConnectErrorCode() {
        return this.mAccsManager.getLastConnectErrorCode();
    }

    public void disconnect() {
        this.mAccsManager.disconnect();
    }

    public void reconnect() {
        this.mAccsManager.reconnect();
    }

    public void reset() {
        this.mAccsManager.reset();
    }
}
