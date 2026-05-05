package com.alibaba.ailabs.iot.mesh;

import a.a.a.a.b.C0327b;
import a.a.a.a.b.C0329d;
import a.a.a.a.b.G;
import a.a.a.a.b.a.C0315a;
import a.a.a.a.b.a.I;
import a.a.a.a.b.c.c;
import a.a.a.a.b.j.a;
import a.a.a.a.b.l.f;
import a.a.a.a.b.m.b;
import a.a.a.a.b.na;
import aisscanner.ScanRecord;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ParcelUuid;
import android.text.TextUtils;
import android.util.Pair;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import anetwork.channel.util.RequestConstant;
import b.u;
import com.alibaba.ailabs.iot.aisbase.scanner.BLEScannerProxy;
import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.ailabs.iot.mesh.bean.ConnectionParams;
import com.alibaba.ailabs.iot.mesh.bean.ExtendedBluetoothDevice;
import com.alibaba.ailabs.iot.mesh.bean.ExtendedMeshNode;
import com.alibaba.ailabs.iot.mesh.bean.MeshAccessPayload;
import com.alibaba.ailabs.iot.mesh.bean.MeshDeviceInfo;
import com.alibaba.ailabs.iot.mesh.bean.MeshNodeStatus;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequestGenerator;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManager;
import com.alibaba.ailabs.iot.mesh.callback.ConfigActionListener;
import com.alibaba.ailabs.iot.mesh.callback.DeviceOnlineStatusListener;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.callback.MeshMsgListener;
import com.alibaba.ailabs.iot.mesh.contant.MeshUtConst$MeshErrorEnum;
import com.alibaba.ailabs.iot.mesh.delegate.OnReadyToBindHandler;
import com.alibaba.ailabs.iot.mesh.grouping.IMeshGroupingService;
import com.alibaba.ailabs.iot.mesh.grouping.MeshGroupingService;
import com.alibaba.ailabs.iot.mesh.managers.MeshDeviceInfoManager;
import com.alibaba.ailabs.iot.mesh.task.bean.MeshControlDevice;
import com.alibaba.ailabs.iot.mesh.ut.UtError;
import com.alibaba.ailabs.iot.mesh.ut.UtTraceInfo;
import com.alibaba.ailabs.iot.mesh.utils.AliMeshUUIDParserUtil;
import com.alibaba.ailabs.iot.mesh.utils.Constants;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import com.alibaba.ailabs.tg.network.Callback;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.alibaba.ailabs.tg.utils.ListUtils;
import com.alibaba.android.multiendinonebridge.IResponseCallback;
import com.alibaba.android.multiendinonebridge.IoTMultiendInOneBridge;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import datasource.MeshConfigCallback;
import datasource.bean.ConfigResultMap;
import datasource.bean.ConfigurationData;
import datasource.bean.Sigmesh;
import datasource.bean.SigmeshIotDev;
import datasource.bean.SubscribeGroupAddr;
import datasource.channel.MeshNetworkBizOverUnifiedConnectChannel;
import datasource.implemention.DefaultMeshConfig;
import datasource.implemention.MeshRequestService;
import datasource.implemention.NetworkBusinessManager;
import datasource.implemention.data.IotGetListDeviceBaseInfoWithStatusRespData;
import datasource.implemention.model.GenieBoxOnlineStatus;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import meshprovisioner.BaseMeshNode;
import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.configuration.SequenceNumber;
import meshprovisioner.configuration.bean.CfgMsgModelSubscriptionStatus;
import meshprovisioner.states.UnprovisionedMeshNode;
import meshprovisioner.utils.AddressUtils;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: loaded from: classes.dex */
public class TgMeshManager {
    public static final String KEY_PROVISION_COMBO_MESH_WIFI_BSSID = "BSSID";
    public static final String KEY_PROVISION_COMBO_MESH_WIFI_PASSWORD = "password";
    public static final String KEY_PROVISION_COMBO_MESH_WIFI_REGION_INDEX = "regionIndex";
    public static final String KEY_PROVISION_COMBO_MESH_WIFI_SSID = "ssid";
    public static final String TAG = "tg_mesh_sdk_" + TgMeshManager.class.getSimpleName();
    public volatile String connectFail;
    public ExecutorService fixedThreadPool;
    public Handler handler;
    public boolean isBluetoothEnabled;
    public volatile boolean isBound;
    public volatile boolean isConnected;
    public boolean isConnecting;
    public volatile boolean isEnvInitialized;
    public volatile boolean isInitialized;
    public boolean isLocationEnabled;
    public MeshService.b mBinder;
    public final BroadcastReceiver mBroadcastReceiver;
    public final Deque<SIGMeshBizRequest> mConfigTaskQueue;
    public Context mContext;
    public a mControlTask;
    public ExtendedMeshNode mExtendedMeshNode;
    public volatile AtomicBoolean mInConfigProgress;
    public List<MeshStatusCallback> mMeshStatusCallbacks;
    public SIGMeshBizRequest.NetworkParameter mNetworkParameter;
    public final List<String> mPendingProvisionDeviceAddress;
    public OnReadyToBindHandler mPendingReadyToBindHandler;
    public byte mReceivedConfigCount;
    public ServiceConnection mServiceConnection;
    public ExecutorService mSingleThreadExecutor;
    public List<MeshMsgListener> meshMsgListenerList;
    public c multiEndinOneProxy;
    public String productKey;
    public int provisionFinishedCounter;
    public AtomicBoolean translateControlCommandByCloud;

    /* JADX INFO: renamed from: com.alibaba.ailabs.iot.mesh.TgMeshManager$33, reason: invalid class name */
    static /* synthetic */ class AnonymousClass33 {
        public static final /* synthetic */ int[] $SwitchMap$com$alibaba$ailabs$iot$mesh$bean$MeshNodeStatus = new int[MeshNodeStatus.values().length];
        public static final /* synthetic */ int[] $SwitchMap$com$alibaba$ailabs$iot$mesh$biz$SIGMeshBizRequest$Type;

        static {
            try {
                $SwitchMap$com$alibaba$ailabs$iot$mesh$bean$MeshNodeStatus[MeshNodeStatus.PROVISIONING_CAPABILITIES.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$alibaba$ailabs$iot$mesh$bean$MeshNodeStatus[MeshNodeStatus.PROVISIONING_COMPLETE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$alibaba$ailabs$iot$mesh$bean$MeshNodeStatus[MeshNodeStatus.PROVISIONING_FAILED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$alibaba$ailabs$iot$mesh$bean$MeshNodeStatus[MeshNodeStatus.REQUEST_FAILED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$alibaba$ailabs$iot$mesh$bean$MeshNodeStatus[MeshNodeStatus.COMBO_WIFI_CONFIG_STATUS.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            $SwitchMap$com$alibaba$ailabs$iot$mesh$biz$SIGMeshBizRequest$Type = new int[SIGMeshBizRequest.Type.values().length];
            try {
                $SwitchMap$com$alibaba$ailabs$iot$mesh$biz$SIGMeshBizRequest$Type[SIGMeshBizRequest.Type.CONFIG_MODEL_SUBSCRIPTION.ordinal()] = 1;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    public enum DevOnlineStatus {
        DEV_ST_DEFAULT(-1),
        DEV_ST_ONLINE(0),
        DEV_ST_OFFLINE(1),
        DEV_ST_WAIT_CONFIRM(2),
        DEV_ST_UNKNOWN(3);

        public int status;

        DevOnlineStatus(int i) {
            this.status = i;
        }

        public static DevOnlineStatus fromIntValue(int i) {
            return i != -1 ? i != 0 ? i != 1 ? DEV_ST_UNKNOWN : DEV_ST_OFFLINE : DEV_ST_ONLINE : DEV_ST_DEFAULT;
        }

        public int getStatus() {
            return this.status;
        }
    }

    class MeshConnection implements ServiceConnection {
        public MeshConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (!(iBinder instanceof MeshService.b)) {
                a.a.a.a.b.m.a.b(TgMeshManager.TAG, "Bind service occurs exception");
                return;
            }
            TgMeshManager.this.mBinder = (MeshService.b) iBinder;
            if (TgMeshManager.this.mPendingReadyToBindHandler != null) {
                TgMeshManager.this.mBinder.a(TgMeshManager.this.mPendingReadyToBindHandler);
            }
            a.a.a.a.b.m.a.a(TgMeshManager.TAG, "Connected to meshService");
            a.a.a.a.b.m.a.a(TgMeshManager.TAG, "isInitialized: " + TgMeshManager.this.isInitialized);
            TgMeshManager.this.isBound = true;
            if (TgMeshManager.this.isInitialized) {
                TgMeshManager.this.publishStatus(1, "Initialize success...");
                a.a.a.a.b.m.a.a(TgMeshManager.TAG, "Initialize success...");
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            TgMeshManager.this.mBinder = null;
            TgMeshManager.this.isBound = false;
            TgMeshManager.this.publishStatus(-1, "Disconnected to meshService");
            a.a.a.a.b.m.a.b(TgMeshManager.TAG, "Disconnected to meshService");
        }
    }

    private static class SingletonClassInstance {
        public static final TgMeshManager instance = new TgMeshManager();
    }

    private int checkEnv() {
        if (!this.isInitialized) {
            publishStatus(-1, "TgMeshManager has not initialized");
            a.a.a.a.b.m.a.b(TAG, "TgMeshManager has not initialized, " + hashCode());
            MeshUtConst$MeshErrorEnum meshUtConst$MeshErrorEnum = MeshUtConst$MeshErrorEnum.MESH_MANAGER_NOT_INITIALIZED_ERROR;
            b.a("ALSMesh", "ble", this.productKey, "", 0L, meshUtConst$MeshErrorEnum.getErrorCode(), meshUtConst$MeshErrorEnum.getErrorMsg());
            return 20003;
        }
        if (!this.isBound) {
            publishStatus(-1, "Has not call the \"enter\" or enter failed, please try enter again");
            a.a.a.a.b.m.a.b(TAG, "TgMeshManager has not bond to service, " + hashCode());
            MeshUtConst$MeshErrorEnum meshUtConst$MeshErrorEnum2 = MeshUtConst$MeshErrorEnum.MESH_MANAGER_NOT_BIND_SERVICE_ERROR;
            b.a("ALSMesh", "ble", this.productKey, "", 0L, meshUtConst$MeshErrorEnum2.getErrorCode(), meshUtConst$MeshErrorEnum2.getErrorMsg());
            return 20012;
        }
        this.isBluetoothEnabled = Utils.isBleEnabled();
        this.isLocationEnabled = Utils.isLocationEnabled(this.mContext);
        boolean zIsLocationPermissionsGranted = Utils.isLocationPermissionsGranted(this.mContext);
        if (Build.VERSION.SDK_INT < 33 && !this.isLocationEnabled) {
            publishStatus(-9, "Location not enabled");
            a.a.a.a.b.m.a.b(TAG, "Location not enabled, " + hashCode());
            MeshUtConst$MeshErrorEnum meshUtConst$MeshErrorEnum3 = MeshUtConst$MeshErrorEnum.LOCATION_NOT_ENABLED_ERROR;
            b.a("ALSMesh", "ble", this.productKey, "", 0L, meshUtConst$MeshErrorEnum3.getErrorCode(), meshUtConst$MeshErrorEnum3.getErrorMsg());
            return 20002;
        }
        if (this.isBluetoothEnabled) {
            if (zIsLocationPermissionsGranted) {
                return 0;
            }
            publishStatus(-10, "location permission not granted");
            a.a.a.a.b.m.a.b(TAG, "location permission not granted,  " + hashCode());
            return 20013;
        }
        publishStatus(-11, "Bluetooth not enabled");
        a.a.a.a.b.m.a.b(TAG, "Bluetooth disabled, " + hashCode());
        MeshUtConst$MeshErrorEnum meshUtConst$MeshErrorEnum4 = MeshUtConst$MeshErrorEnum.BLUETOOTH_NOT_ENABLED_ERROR;
        b.a("ALSMesh", "ble", this.productKey, "", 0L, meshUtConst$MeshErrorEnum4.getErrorCode(), meshUtConst$MeshErrorEnum4.getErrorMsg());
        return 20001;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deviceControlByServer(String str, String str2, final IActionListener iActionListener, final UtTraceInfo utTraceInfo) {
        a.a.a.a.b.m.a.a(TAG, "deviceControlByServer...");
        na.a().a(str, "3404", str2, new MeshConfigCallback<List<Sigmesh>>() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.32
            @Override // datasource.MeshConfigCallback
            public void onFailure(String str3, String str4) {
                a.a.a.a.b.m.a.a(TgMeshManager.TAG, "errorCode:" + str3 + ", errorMessage:" + str4);
                UtTraceInfo utTraceInfo2 = utTraceInfo;
                StringBuilder sb = new StringBuilder();
                sb.append(str3);
                sb.append(str4);
                a.a.a.a.b.l.c.a(utTraceInfo2, 20005, sb.toString());
                iActionListener.onFailure(-20, str4);
            }

            /* JADX WARN: Removed duplicated region for block: B:23:0x00bd  */
            @Override // datasource.MeshConfigCallback
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onSuccess(java.util.List<datasource.bean.Sigmesh> r12) {
                /*
                    Method dump skipped, instruction units count: 244
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: com.alibaba.ailabs.iot.mesh.TgMeshManager.AnonymousClass32.onSuccess(java.util.List):void");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void execWakeUpDevice(String str, boolean z, final IActionListener<Boolean> iActionListener) {
        na.a().a(str, z, new MeshConfigCallback<List<Sigmesh>>() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.12
            @Override // datasource.MeshConfigCallback
            public void onFailure(String str2, String str3) {
                a.a.a.a.b.m.a.a(TgMeshManager.TAG, "execWakeUpDevice failure:" + str2);
                iActionListener.onFailure(-28, str3);
            }

            @Override // datasource.MeshConfigCallback
            public void onSuccess(List<Sigmesh> list) {
                a.a.a.a.b.m.a.a(TgMeshManager.TAG, "execWakeUpDevice");
                if (ListUtils.isEmpty(list)) {
                    iActionListener.onFailure(-28, "sigmesh is null");
                } else if (TgMeshManager.this.mBinder == null || !TgMeshManager.this.mBinder.k()) {
                    iActionListener.onFailure(-29, "app not connect proxy");
                } else {
                    TgMeshManager.this.sendWakeUpMessage(list, iActionListener);
                }
            }
        });
    }

    public static TgMeshManager getInstance() {
        return SingletonClassInstance.instance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void groupControlByServer(String str, String str2, String str3, final IActionListener<Boolean> iActionListener) {
        if (TextUtils.isEmpty(str3)) {
            Utils.notifyFailed(iActionListener, -21, "Wrong parameter jsonString=null.");
            return;
        }
        if (TextUtils.isEmpty(str)) {
            Utils.notifyFailed(iActionListener, -21, "Wrong parameter groupId=null.");
        } else if (TextUtils.isEmpty(str2)) {
            Utils.notifyFailed(iActionListener, -21, "Wrong parameter method=null.");
        } else {
            na.a().d(str, str2, str3, new MeshConfigCallback<List<Sigmesh>>() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.31
                @Override // datasource.MeshConfigCallback
                public void onFailure(String str4, String str5) {
                    a.a.a.a.b.m.a.a(TgMeshManager.TAG, "errorCode:" + str4 + ", errorMessage:" + str5);
                    Utils.notifyFailed(iActionListener, -20, str5);
                }

                @Override // datasource.MeshConfigCallback
                public void onSuccess(List<Sigmesh> list) {
                    String str4 = TgMeshManager.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("sendGroupMessage: ");
                    sb.append(list != null ? list.size() : 0);
                    sb.append(";");
                    sb.append(list == null);
                    a.a.a.a.b.m.a.a(str4, sb.toString());
                    if (!ListUtils.isEmpty(list) && list.get(0) != null) {
                        Sigmesh sigmesh = list.get(0);
                        if (sigmesh.getDevice() != null && sigmesh.getAction() != null) {
                            int destAddr = sigmesh.getDevice().getDestAddr();
                            int appKeyIndex = sigmesh.getDevice().getAppKeyIndex();
                            int netKeyIndex = sigmesh.getDevice().getNetKeyIndex();
                            a.a.a.a.b.m.a.a(TgMeshManager.TAG, "sendGroupMessage: unicastAddress=" + destAddr + ";appKeyInder=" + appKeyIndex + ";netKeyIndex=" + netKeyIndex);
                            if (sigmesh.getAction().getOpcode() != null && sigmesh.getAction().getParameters() != null) {
                                int i = Integer.parseInt(sigmesh.getAction().getOpcode(), 16);
                                a.a.a.a.b.m.a.a(TgMeshManager.TAG, "sendGroupMessage: opcodeInt=" + i);
                                TgMeshManager.this.mBinder.a(netKeyIndex, appKeyIndex, destAddr, Utils.byteArray2Int(Utils.getOpCodeBytes(i)), MeshParserUtils.toByteArray(sigmesh.getAction().getParameters()), iActionListener);
                                return;
                            }
                        }
                    }
                    Utils.notifyFailed(iActionListener, -21, "Wrong parameter");
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBindState(Intent intent) {
        if (intent.getExtras() == null) {
            return;
        }
        int i = intent.getExtras().getInt(Utils.EXTRA_BIND_CODE, 0);
        if (i == -2) {
            String string = intent.getExtras().getString(Utils.EXTRA_BIND_STATE_MSG);
            publishStatus(-5, "unbind_user_failed: " + string);
            a.a.a.a.b.m.a.a(TAG, "unbind_user_failed: " + string);
            return;
        }
        if (i == -1) {
            String string2 = intent.getExtras().getString(Utils.EXTRA_BIND_STATE_MSG);
            publishStatus(-4, "bind_user_failed: " + string2);
            a.a.a.a.b.m.a.a(TAG, "bind_user_failed: " + string2);
            return;
        }
        if (i == 1) {
            publishStatus(4, intent.getExtras().getString(Utils.EXTRA_BIND_STATE_MSG));
            a.a.a.a.b.m.a.a(TAG, "bind_user_success, procuctKey:" + this.productKey);
            return;
        }
        if (i == 2) {
            publishStatus(5, "unbind_user_success");
            a.a.a.a.b.m.a.a(TAG, "unbind_user_success");
        } else {
            if (i != 10) {
                return;
            }
            publishStatus(10, "remove_node_success");
            a.a.a.a.b.m.a.a(TAG, "remove_node_success");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConfigurationStates(Intent intent) {
        if (intent.getExtras() == null) {
            return;
        }
        int i = intent.getExtras().getInt(Utils.EXTRA_CONFIGURATION_STATE);
        a.a.a.a.b.m.a.a(TAG, "configurationState: " + i);
        MeshNodeStatus meshNodeStatusFromStatusCode = MeshNodeStatus.fromStatusCode(i);
        if (this.mBinder.f() instanceof ProvisionedMeshNode) {
            this.mExtendedMeshNode.updateMeshNode((ProvisionedMeshNode) this.mBinder.f());
        }
        int i2 = AnonymousClass33.$SwitchMap$com$alibaba$ailabs$iot$mesh$bean$MeshNodeStatus[meshNodeStatusFromStatusCode.ordinal()];
        if (i2 == 3) {
            publishStatus(-3, intent.getExtras().getString(Utils.EXTRA_CONFIGURATION_FAIL_MSG));
        } else if (i2 != 4) {
            publishStatus(0, meshNodeStatusFromStatusCode.name());
        } else {
            publishStatus(-8, intent.getExtras().getString(Utils.EXTRA_REQUEST_FAIL_MSG));
        }
    }

    private void handleGenericState(Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action) || intent.getExtras() == null) {
            return;
        }
        byte b2 = -1;
        int iHashCode = action.hashCode();
        if (iHashCode != 781060501) {
            if (iHashCode != 1264097733) {
                if (iHashCode == 1322041106 && action.equals(Utils.ACTION_GENERIC_ON_OFF_STATE)) {
                    b2 = 0;
                }
            } else if (action.equals(Utils.ACTION_GENERIC_LEVEL_STATE)) {
                b2 = 1;
            }
        } else if (action.equals(Utils.ACTION_VENDOR_MODEL_MESSAGE_STATE)) {
            b2 = 2;
        }
        if (b2 == 0) {
            boolean z = intent.getExtras().getBoolean(Utils.EXTRA_GENERIC_PRESENT_STATE);
            boolean z2 = intent.getExtras().getBoolean(Utils.EXTRA_GENERIC_TARGET_STATE);
            intent.getExtras().getInt(Utils.EXTRA_GENERIC_TRANSITION_STEPS);
            intent.getExtras().getInt(Utils.EXTRA_GENERIC_TRANSITION_RES);
            a.a.a.a.b.m.a.a(TAG, "generic_on_off_state, presentOnOffState: " + z + ", targetOnOffState: " + z2);
            return;
        }
        if (b2 != 1) {
            return;
        }
        int i = intent.getExtras().getInt(Utils.EXTRA_GENERIC_PRESENT_STATE);
        int i2 = intent.getExtras().getInt(Utils.EXTRA_GENERIC_TARGET_STATE);
        intent.getExtras().getInt(Utils.EXTRA_GENERIC_TRANSITION_STEPS);
        intent.getExtras().getInt(Utils.EXTRA_GENERIC_TRANSITION_RES);
        a.a.a.a.b.m.a.a(TAG, "generic_on_off_state, presentLevel: " + i + ", targetLevel: " + i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleProvisioningStates(Intent intent) {
        if (intent.getExtras() == null || this.mBinder == null) {
            return;
        }
        MeshNodeStatus meshNodeStatusFromStatusCode = MeshNodeStatus.fromStatusCode(intent.getExtras().getInt(Utils.EXTRA_PROVISIONING_STATE));
        switch (AnonymousClass33.$SwitchMap$com$alibaba$ailabs$iot$mesh$bean$MeshNodeStatus[meshNodeStatusFromStatusCode.ordinal()]) {
            case 1:
                this.mExtendedMeshNode.updateMeshNode(this.mBinder.f());
                publishStatus(0, meshNodeStatusFromStatusCode.name());
                break;
            case 2:
                publishStatus(3, meshNodeStatusFromStatusCode.name());
                this.isConnecting = false;
                break;
            case 3:
                publishStatus(-3, intent.getExtras().getString(Utils.EXTRA_PROVISIONING_FAIL_MSG));
                break;
            case 4:
                publishStatus(-8, intent.getExtras().getString(Utils.EXTRA_REQUEST_FAIL_MSG));
                break;
            case 5:
                publishStatus(20, intent.getExtras().getString(Utils.EXTRA_PROVISIONING_FAIL_MSG));
                break;
            default:
                publishStatus(0, meshNodeStatusFromStatusCode.name());
                break;
        }
    }

    private void initBizServiceModule() {
        if (d.b.a().a(IMeshGroupingService.class) == null) {
            d.b.a().a(IMeshGroupingService.class, new MeshGroupingService());
        }
    }

    private void isGenieBoxOnline(final IActionListener<Boolean> iActionListener) {
        ((MeshRequestService) NetworkBusinessManager.getService(MeshRequestService.class)).getListDeviceBaseInfoWithStatus().enqueue(new Callback<IotGetListDeviceBaseInfoWithStatusRespData>() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.9
            public void onFailure(int i, String str, String str2) {
            }

            public void onSuccess(int i, IotGetListDeviceBaseInfoWithStatusRespData iotGetListDeviceBaseInfoWithStatusRespData) {
                boolean z;
                if (iotGetListDeviceBaseInfoWithStatusRespData == null || iotGetListDeviceBaseInfoWithStatusRespData.getModel() == null) {
                    iActionListener.onFailure(-3, "failed to query genie box status");
                    return;
                }
                if (iotGetListDeviceBaseInfoWithStatusRespData.getModel().size() == 0) {
                    iActionListener.onFailure(-4, "size of genie boxs is 0");
                    return;
                }
                Iterator<Map.Entry<String, GenieBoxOnlineStatus>> it = iotGetListDeviceBaseInfoWithStatusRespData.getModel().entrySet().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        z = true;
                        break;
                    }
                    GenieBoxOnlineStatus value = it.next().getValue();
                    if (value.getOnline() == 1) {
                        z = false;
                        a.a.a.a.b.m.a.c(TgMeshManager.TAG, "genie box online " + value.getUuid());
                        break;
                    }
                }
                if (!z) {
                    iActionListener.onSuccess(true);
                } else {
                    a.a.a.a.b.m.a.c(TgMeshManager.TAG, "all genie are offline");
                    iActionListener.onFailure(-5, "all genie are offline");
                }
            }
        });
    }

    private boolean isLowCostDeviceExist() {
        return MeshDeviceInfoManager.getInstance().isLowCostDeviceExist();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void nextRequest() {
        a.a.a.a.b.m.a.a(TAG, "nextRequest called, mInConfigProgress: " + this.mInConfigProgress);
        if (this.mInConfigProgress.compareAndSet(false, true)) {
            if (this.mConfigTaskQueue.size() <= 0) {
                this.mInConfigProgress.set(false);
                return;
            }
            final SIGMeshBizRequest first = this.mConfigTaskQueue.getFirst();
            if (first != null) {
                this.mInConfigProgress.set(true);
                if (AnonymousClass33.$SwitchMap$com$alibaba$ailabs$iot$mesh$biz$SIGMeshBizRequest$Type[first.l().ordinal()] == 1) {
                    if (this.mBinder == null) {
                        Utils.notifyFailed(first.m(), -30, "TgMeshManager has not bond to MeshService");
                        if (this.mConfigTaskQueue.size() == 0) {
                            return;
                        }
                        try {
                            this.mConfigTaskQueue.removeFirst();
                        } catch (NoSuchElementException unused) {
                        }
                        nextRequest();
                        return;
                    }
                    a.a.a.a.b.m.a.a(TAG, "Execute ConfigModelSubRequest");
                    final C0315a c0315a = (C0315a) first;
                    if (!TextUtils.isEmpty(c0315a.s()) && c0315a.s().length() == 32) {
                        first.p();
                        this.mBinder.a(c0315a.r(), c0315a.s(), c0315a.v(), c0315a.t(), c0315a.w(), c0315a.u(), new IActionListener<Boolean>() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.29
                            @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
                            public void onFailure(int i, String str) {
                                a.a.a.a.b.m.a.b(TgMeshManager.TAG, "On Failed config model subscription, devId: " + c0315a.t() + ", errorCode: " + i + " ,desc: " + str);
                                TgMeshManager.this.mInConfigProgress.set(false);
                                first.b();
                                Utils.notifyFailed(first.m(), i, str);
                                try {
                                    TgMeshManager.this.mConfigTaskQueue.removeFirst();
                                } catch (NoSuchElementException unused2) {
                                }
                                if (TgMeshManager.this.mConfigTaskQueue.size() == 0) {
                                    return;
                                }
                                TgMeshManager.this.nextRequest();
                            }

                            @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
                            public void onSuccess(Boolean bool) {
                                a.a.a.a.b.m.a.c(TgMeshManager.TAG, "On successful config model subscription, devId: " + c0315a.t());
                                TgMeshManager.this.mInConfigProgress.set(false);
                                first.b();
                                Utils.notifySuccess((IActionListener<boolean>) first.m(), true);
                                try {
                                    TgMeshManager.this.mConfigTaskQueue.removeFirst();
                                } catch (NoSuchElementException unused2) {
                                }
                                if (TgMeshManager.this.mConfigTaskQueue.size() == 0) {
                                    return;
                                }
                                TgMeshManager.this.nextRequest();
                            }
                        }, 0);
                    }
                    Utils.notifyFailed(first.m(), -32, "DeviceKey Cannot be empty or illegal deviceKey");
                    if (this.mConfigTaskQueue.size() == 0) {
                        return;
                    }
                    try {
                        this.mConfigTaskQueue.removeFirst();
                    } catch (NoSuchElementException unused2) {
                    }
                    nextRequest();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyCallback(int i, String str, String str2, byte[] bArr, byte[] bArr2, byte[] bArr3, int i2, String str3) {
        MeshAccessPayload meshAccessPayload = new MeshAccessPayload();
        meshAccessPayload.setOpCode(str2);
        meshAccessPayload.setParameters(bArr);
        meshAccessPayload.setNetKey(bArr3);
        meshAccessPayload.setTranslatedTSLDesc(null);
        meshAccessPayload.setSequenceNo(i2);
        meshAccessPayload.setIotId(str3);
        if (i == 0 && !TextUtils.isEmpty(str)) {
            try {
                JSONObject object = JSON.parseObject(str);
                if (object.getBoolean("success").booleanValue()) {
                    JSONObject jSONObject = object.getJSONObject("data");
                    if (jSONObject != null && jSONObject.containsKey("results")) {
                        meshAccessPayload.setTranslatedTSLDesc(jSONObject.getJSONObject("results").toJSONString());
                    }
                } else {
                    a.a.a.a.b.m.a.d(TAG, "Translation failed");
                }
            } catch (JSONException e) {
                a.a.a.a.b.m.a.b(TAG, e.toString());
            }
        }
        a.a.a.a.b.m.a.c(TAG, "notifyCallback sequenceNumber: " + i2 + " tsl: " + meshAccessPayload.getTranslatedTSLDesc());
        Iterator<MeshMsgListener> it = this.meshMsgListenerList.iterator();
        while (it.hasNext()) {
            try {
                it.next().onReceiveMeshMessage(bArr2, meshAccessPayload);
            } catch (Exception e2) {
                a.a.a.a.b.m.a.b(TAG, e2.toString());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMeshNetworkReady() {
        IoTMultiendInOneBridge.getInstance().onProxyConnected();
        for (int i = 0; i < this.mConfigTaskQueue.size(); i++) {
            ((SIGMeshBizRequest) ((LinkedList) this.mConfigTaskQueue).get(i)).b();
        }
        nextRequest();
        offerBizRequest(SIGMeshBizRequestGenerator.e());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTransactionStateReceived(Intent intent) {
        ExtendedMeshNode extendedMeshNode;
        String action = intent.getAction();
        ProvisionedMeshNode provisionedMeshNode = (ProvisionedMeshNode) this.mBinder.f();
        if (TextUtils.isEmpty(action)) {
            return;
        }
        byte b2 = -1;
        if (action.hashCode() == 404916711 && action.equals(Utils.ACTION_TRANSACTION_STATE)) {
            b2 = 0;
        }
        if (b2 == 0 && (extendedMeshNode = this.mExtendedMeshNode) != null) {
            extendedMeshNode.updateMeshNode(provisionedMeshNode);
            if (intent.getExtras() != null) {
                intent.getExtras().getInt(Utils.EXTRA_ELEMENT_ADDRESS);
                intent.getBooleanExtra(Utils.EXTRA_DATA, true);
                publishStatus(-3, "transaction_failed");
                a.a.a.a.b.m.a.a(TAG, "transaction_failed");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void publishStatus(int i, String str) {
        a.a.a.a.b.m.a.a(TAG, "publishStatus() called with: statusCode = [" + i + "], statusMsg = [" + str + "]");
        for (MeshStatusCallback meshStatusCallback : new ArrayList(this.mMeshStatusCallbacks)) {
            if (meshStatusCallback != null) {
                meshStatusCallback.onStatus(i, str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendWakeUpMessage(List<Sigmesh> list, final IActionListener<Boolean> iActionListener) {
        Sigmesh sigmesh = list.get(0);
        if (sigmesh.getDevice() == null || sigmesh.getAction() == null) {
            return;
        }
        int destAddr = sigmesh.getDevice().getDestAddr();
        int appKeyIndex = sigmesh.getDevice().getAppKeyIndex();
        int netKeyIndex = sigmesh.getDevice().getNetKeyIndex();
        if (sigmesh.getAction().getOpcode() == null || sigmesh.getAction().getParameters() == null) {
            return;
        }
        byte[] opCodeBytes = Utils.getOpCodeBytes(Integer.parseInt(sigmesh.getAction().getOpcode(), 16));
        sendMessge(destAddr, appKeyIndex, netKeyIndex, Utils.byteArray2Int(opCodeBytes), MeshParserUtils.toByteArray(sigmesh.getAction().getParameters()), new IActionListener() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.13
            @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
            public void onFailure(int i, String str) {
                a.a.a.a.b.m.a.a(TgMeshManager.TAG, "sendWakeUpMessage failure:" + i + ";msg:" + str);
                iActionListener.onFailure(i, str);
            }

            @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
            public void onSuccess(Object obj) {
                a.a.a.a.b.m.a.a(TgMeshManager.TAG, "sendWakeUpMessage success");
                iActionListener.onSuccess(true);
            }
        });
    }

    public void addDevice(final ExtendedBluetoothDevice extendedBluetoothDevice, final Map<String, Object> map) {
        a.a.a.a.b.m.a.a(TAG, "Add device with extensions ...");
        if (extendedBluetoothDevice == null) {
            publishStatus(-7, "DeviceId is null");
            a.a.a.a.b.m.a.b(TAG, "DeviceId is null");
            return;
        }
        if (checkEnv() != 0) {
            return;
        }
        MeshService.b bVar = this.mBinder;
        this.isConnecting = bVar != null && bVar.d() == 1;
        if (this.isConnecting) {
            a.a.a.a.b.m.a.a(TAG, "in connecting");
            publishStatus(8, "connecting...");
            return;
        }
        ScanRecord scanRecord = extendedBluetoothDevice.getScanRecord();
        if (scanRecord != null) {
            byte[] serviceData = scanRecord.getServiceData(new ParcelUuid(BleMeshManager.MESH_PROVISIONING_UUID));
            if (serviceData != null && serviceData.length >= 16) {
                int iExtractPIDFromUUID = AliMeshUUIDParserUtil.extractPIDFromUUID(serviceData);
                this.productKey = String.valueOf(iExtractPIDFromUUID);
                a.a.a.a.b.m.a.c(TAG, "productKey = " + this.productKey);
                b.a("ALSMesh", "ble", String.valueOf(iExtractPIDFromUUID), false, serviceData, "");
            } else if (serviceData == null) {
                a.a.a.a.b.m.a.b(TAG, "service data is null");
            } else {
                a.a.a.a.b.m.a.c(TAG, "service data length = " + serviceData.length);
            }
        } else {
            a.a.a.a.b.m.a.c(TAG, "scanRecord is null");
        }
        MeshService.b bVar2 = this.mBinder;
        if (bVar2 != null) {
            bVar2.a(new MeshService.OnDisconnectListener() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.6
                @Override // com.alibaba.ailabs.iot.mesh.MeshService.OnDisconnectListener
                public void onDisconnected() {
                    a.a.a.a.b.m.a.a(TgMeshManager.TAG, "onDisconnected, start connect device");
                    TgMeshManager.this.handler.postDelayed(new Runnable() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.6.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (TgMeshManager.this.mBinder == null) {
                                return;
                            }
                            TgMeshManager.this.mBinder.a(extendedBluetoothDevice.getAddress());
                            MeshService.b bVar3 = TgMeshManager.this.mBinder;
                            AnonymousClass6 anonymousClass6 = AnonymousClass6.this;
                            bVar3.a(extendedBluetoothDevice, false, map);
                            TgMeshManager.this.isConnecting = true;
                            TgMeshManager.this.mBinder.a(extendedBluetoothDevice.getAddress());
                        }
                    }, 500L);
                }
            });
        }
    }

    public void batchBindDevices(List<UnprovisionedBluetoothMeshDevice> list) {
        batchBindDevices(list, null);
    }

    public void bindDevice(UnprovisionedBluetoothMeshDevice unprovisionedBluetoothMeshDevice) {
        bindDevice(unprovisionedBluetoothMeshDevice, null);
    }

    public void cancelAllBizRequest() {
        MeshService.b bVar = this.mBinder;
        if (bVar != null) {
            bVar.a();
        }
    }

    public void configModelSubscription(List<Sigmesh> list, final ConfigActionListener<Boolean> configActionListener) {
        if (list == null || list.size() == 0) {
            return;
        }
        LinkedList linkedList = new LinkedList();
        for (Sigmesh sigmesh : list) {
            final String devId = sigmesh.getDevId();
            final Sigmesh.Action action = sigmesh.getAction();
            Sigmesh.Device device = sigmesh.getDevice();
            SIGMeshBizRequest.Type type = SIGMeshBizRequest.Type.COMMON_REQUEST_RESPONSE;
            type.setOpcode(Integer.parseInt(action.getOpcode(), 16));
            type.setExpectedOpcode((short) Integer.parseInt(action.getStatusOpcode(), 16));
            u.a aVarD = G.a().d().d();
            ProvisionedMeshNode provisionedMeshNode = aVarD != null ? (ProvisionedMeshNode) aVarD.d(AddressUtils.getUnicastAddressBytes(device.getDestAddr())) : null;
            SIGMeshBizRequest sIGMeshBizRequestA = new SIGMeshBizRequest.a().a(type).a(provisionedMeshNode).a(AddressUtils.getUnicastAddressBytes(device.getDestAddr())).a(new I<Object>() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.26
                @Override // a.a.a.a.b.a.I
                public Pair<Integer, ?> parseResponse(Object obj) {
                    if (obj instanceof CfgMsgModelSubscriptionStatus) {
                        return CfgMsgModelSubscriptionStatus.parseStatus(TgMeshManager.this.mContext, ((CfgMsgModelSubscriptionStatus) obj).getStatus());
                    }
                    if (obj instanceof byte[]) {
                        return CfgMsgModelSubscriptionStatus.parseStatus(TgMeshManager.this.mContext, ((byte[]) obj)[0]);
                    }
                    return null;
                }
            }).a(true).a(new IActionListener<Object>() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.25
                @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
                public void onFailure(int i, String str) {
                    ConfigActionListener configActionListener2 = configActionListener;
                    if (configActionListener2 != null) {
                        configActionListener2.onFailure(devId, i, str);
                    }
                }

                @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
                public void onSuccess(Object obj) {
                    ConfigActionListener configActionListener2 = configActionListener;
                    if (configActionListener2 != null) {
                        configActionListener2.onSuccess(devId, true);
                    }
                }
            }).a(new SIGMeshBizRequest.b() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.24
                @Override // com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest.b
                public byte[] getEncodedParameters() {
                    return ConvertUtils.hexString2Bytes(action.getParameters());
                }
            }).a();
            sIGMeshBizRequestA.a(ConvertUtils.hexString2Bytes(device.getDeviceKey()));
            if (provisionedMeshNode != null && provisionedMeshNode.getDeviceKey() == null) {
                provisionedMeshNode.setDeviceKey(ConvertUtils.hexString2Bytes(device.getDeviceKey()));
            }
            linkedList.add(sIGMeshBizRequestA);
        }
        offerBizRequest(linkedList);
    }

    public void configModelSubscriptionAdd(String str, int i, int i2, int i3, int i4, IActionListener<Boolean> iActionListener) {
        if (a.a.a.a.b.d.a.f1316b) {
            offerBizRequest(SIGMeshBizRequestGenerator.a(this.mContext, TmpConstant.GROUP_OP_ADD, str, i, i2, i3, i4, iActionListener));
            return;
        }
        final C0315a c0315aA = SIGMeshBizRequest.a(TmpConstant.GROUP_OP_ADD, str, i, i2, i3, i4);
        c0315aA.a(iActionListener);
        boolean z = this.mConfigTaskQueue.size() == 0 && isConnectedToMesh();
        if (!isConnectedToMesh()) {
            c0315aA.a(5000, new Runnable() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.22
                @Override // java.lang.Runnable
                public void run() {
                    TgMeshManager.this.mConfigTaskQueue.remove(c0315aA);
                }
            });
        }
        this.mConfigTaskQueue.add(c0315aA);
        if (z) {
            nextRequest();
        }
    }

    public void configModelSubscriptionDelete(String str, int i, int i2, int i3, int i4, IActionListener<Boolean> iActionListener) {
        if (a.a.a.a.b.d.a.f1316b) {
            offerBizRequest(SIGMeshBizRequestGenerator.a(this.mContext, RequestParameters.SUBRESOURCE_DELETE, str, i, i2, i3, i4, iActionListener));
            return;
        }
        final C0315a c0315aA = SIGMeshBizRequest.a(RequestParameters.SUBRESOURCE_DELETE, str, i, i2, i3, i4);
        c0315aA.a(iActionListener);
        boolean z = this.mConfigTaskQueue.size() == 0 && isConnectedToMesh();
        this.mConfigTaskQueue.add(c0315aA);
        if (!isConnectedToMesh()) {
            c0315aA.a(5000, new Runnable() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.23
                @Override // java.lang.Runnable
                public void run() {
                    TgMeshManager.this.mConfigTaskQueue.remove(c0315aA);
                }
            });
        }
        if (z) {
            nextRequest();
        }
    }

    public void configuration(Map<String, ConfigurationData> map, ConfigActionListener<Boolean> configActionListener) {
        byte[] bArrArray;
        LinkedList linkedList = new LinkedList();
        for (Map.Entry<String, ConfigurationData> entry : map.entrySet()) {
            String key = entry.getKey();
            a.a.a.a.b.m.a.c(TAG, "Configuration for device: " + key);
            ConfigurationData value = entry.getValue();
            String deviceKey = value.getDeviceKey();
            int iIntValue = ((Integer) value.getPrimaryUnicastAddress()).intValue();
            Sigmesh.Device device = value.getDevice();
            Sigmesh.Action action = value.getAction();
            if (action == null || device == null) {
                ConfigResultMap configResultMap = value.getConfigResultMap();
                if (configResultMap != null) {
                    List<SubscribeGroupAddr> subscribeGroupAddr = configResultMap.getSubscribeGroupAddr();
                    if (subscribeGroupAddr == null || subscribeGroupAddr.size() == 0) {
                        subscribeGroupAddr = configResultMap.getConfigModelSubscription();
                    }
                    if (subscribeGroupAddr == null || subscribeGroupAddr.size() == 0) {
                        if (configActionListener != null) {
                            configActionListener.onSuccess(key, true);
                        }
                        a.a.a.a.b.m.a.d(TAG, "No subscribe group address task, return");
                    } else {
                        SubscribeGroupAddr subscribeGroupAddr2 = subscribeGroupAddr.get(0);
                        int iIntValue2 = subscribeGroupAddr2.getModelId().intValue();
                        byte[] unicastAddressBytes = AddressUtils.getUnicastAddressBytes(subscribeGroupAddr2.getModelElementAddr().intValue());
                        byte[] unicastAddressBytes2 = AddressUtils.getUnicastAddressBytes(subscribeGroupAddr2.getGroupAddr().intValue());
                        if (iIntValue2 < -32768 || iIntValue2 > 32767) {
                            ByteBuffer byteBufferOrder = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
                            byteBufferOrder.put(unicastAddressBytes[1]);
                            byteBufferOrder.put(unicastAddressBytes[0]);
                            byteBufferOrder.put(unicastAddressBytes2[1]);
                            byteBufferOrder.put(unicastAddressBytes2[0]);
                            byte[] bArr = {(byte) ((iIntValue2 >> 24) & 255), (byte) ((iIntValue2 >> 16) & 255), (byte) ((iIntValue2 >> 8) & 255), (byte) (iIntValue2 & 255)};
                            byteBufferOrder.put(bArr[1]);
                            byteBufferOrder.put(bArr[0]);
                            byteBufferOrder.put(bArr[3]);
                            byteBufferOrder.put(bArr[2]);
                            bArrArray = byteBufferOrder.array();
                        } else {
                            ByteBuffer byteBufferOrder2 = ByteBuffer.allocate(6).order(ByteOrder.LITTLE_ENDIAN);
                            byteBufferOrder2.put(unicastAddressBytes[1]);
                            byteBufferOrder2.put(unicastAddressBytes[0]);
                            byteBufferOrder2.put(unicastAddressBytes2[1]);
                            byteBufferOrder2.put(unicastAddressBytes2[0]);
                            byteBufferOrder2.putShort((short) iIntValue2);
                            bArrArray = byteBufferOrder2.array();
                        }
                        Sigmesh sigmesh = new Sigmesh();
                        Sigmesh.Device device2 = new Sigmesh.Device();
                        device2.setDeviceKey(deviceKey);
                        device2.setDestAddr(iIntValue);
                        device2.setAppKeyIndex(0);
                        device2.setNetKeyIndex(0);
                        device2.setTtl(5);
                        Sigmesh.Action action2 = new Sigmesh.Action();
                        action2.setOpcode(Integer.toHexString(subscribeGroupAddr2.getOpcode().intValue()));
                        action2.setStatusOpcode(Integer.toHexString(subscribeGroupAddr2.getStatusOpcode().intValue()));
                        action2.setParameters(MeshParserUtils.bytesToHex(bArrArray, false));
                        a.a.a.a.b.m.a.a(TAG, String.format("Configuration for device(%s,%d) with action:[%s,%s,%s]", key, Integer.valueOf(device2.getDestAddr()), action2.getOpcode(), action2.getStatusOpcode(), action2.getParameters()));
                        sigmesh.setDevice(device2);
                        sigmesh.setAction(action2);
                        sigmesh.setDevId(key);
                        linkedList.add(sigmesh);
                    }
                } else if (configActionListener != null) {
                    configActionListener.onFailure(key, -32, "configurationData not be empty");
                }
            } else {
                if (!TextUtils.isEmpty(deviceKey)) {
                    device.setDeviceKey(deviceKey);
                }
                Sigmesh sigmesh2 = new Sigmesh();
                sigmesh2.setDevice(device);
                sigmesh2.setAction(action);
                sigmesh2.setDevId(key);
                linkedList.add(sigmesh2);
            }
        }
        if (linkedList.size() > 0) {
            configModelSubscription(linkedList, configActionListener);
        }
    }

    public void connect(final ExtendedBluetoothDevice extendedBluetoothDevice, final boolean z) {
        a.a.a.a.b.m.a.a(TAG, "Connect...");
        if (extendedBluetoothDevice == null) {
            publishStatus(-7, "DeviceId is null");
            a.a.a.a.b.m.a.b(TAG, "DeviceId is null");
            return;
        }
        boolean z2 = false;
        if (extendedBluetoothDevice.getScanRecord() != null) {
            byte[] serviceData = extendedBluetoothDevice.getScanRecord().getServiceData(new ParcelUuid(BleMeshManager.MESH_PROVISIONING_UUID));
            if (serviceData != null && serviceData.length >= 16) {
                int i = (serviceData[3] & 255) + 0 + ((serviceData[4] & 255) << 8) + ((serviceData[5] & 255) << 16) + ((serviceData[6] & 255) << 24);
                this.productKey = String.valueOf(i);
                a.a.a.a.b.m.a.c(TAG, "productKey = " + this.productKey);
                b.a("ALSMesh", "ble", String.valueOf(i), false, serviceData, "");
            } else if (serviceData == null) {
                a.a.a.a.b.m.a.b(TAG, "service data is null");
            } else {
                a.a.a.a.b.m.a.c(TAG, "service data length = " + serviceData.length);
            }
        } else {
            a.a.a.a.b.m.a.c(TAG, "scanRecord is null");
        }
        if (checkEnv() != 0) {
            return;
        }
        MeshService.b bVar = this.mBinder;
        if (bVar != null && bVar.d() == 1) {
            z2 = true;
        }
        this.isConnecting = z2;
        if (this.isConnecting) {
            a.a.a.a.b.m.a.a(TAG, "in connecting");
            publishStatus(8, "connecting...");
            return;
        }
        MeshService.b bVar2 = this.mBinder;
        if (bVar2 != null) {
            if (Constants.f2822a) {
                bVar2.a(extendedBluetoothDevice, z);
                this.isConnecting = true;
            } else {
                bVar2.a(new MeshService.OnDisconnectListener() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.5
                    @Override // com.alibaba.ailabs.iot.mesh.MeshService.OnDisconnectListener
                    public void onDisconnected() {
                        a.a.a.a.b.m.a.a(TgMeshManager.TAG, "onDisconnected, start connect device");
                        if (TgMeshManager.this.mBinder == null || TgMeshManager.this.isConnecting) {
                            return;
                        }
                        TgMeshManager.this.mBinder.a(extendedBluetoothDevice, z);
                        TgMeshManager.this.isConnecting = true;
                    }
                });
            }
        }
        a.a.a.a.b.m.a.a(TAG, "Connect to device: " + extendedBluetoothDevice.getName());
    }

    public void disconnect() {
        a.a.a.a.b.m.a.a(TAG, "Disconnect..");
        if (checkEnv() != 0) {
            return;
        }
        MeshService.b bVar = this.mBinder;
        if (bVar != null) {
            bVar.a(new MeshService.OnDisconnectListener() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.7
                @Override // com.alibaba.ailabs.iot.mesh.MeshService.OnDisconnectListener
                public void onDisconnected() {
                    a.a.a.a.b.m.a.a(TgMeshManager.TAG, "OnDisconnected");
                }
            });
            a.a.a.a.b.m.a.a(TAG, "prepare disconnect to device");
        }
        this.isConnecting = false;
    }

    public <S extends T, T> S getBizService(Class<T> cls) {
        return (S) d.b.a().a(cls);
    }

    public BaseMeshNode getMeshNode() {
        MeshService.b bVar = this.mBinder;
        if (bVar != null) {
            return bVar.f();
        }
        return null;
    }

    public int getNetkeyIndex() {
        MeshService.b bVar = this.mBinder;
        if (bVar == null) {
            return 0;
        }
        bVar.g();
        return 0;
    }

    public String getProxyNodeAddress() {
        u.a aVarD = G.a().d().d();
        if (aVarD == null || aVarD.e() == null || aVarD.e().h() == null) {
            return null;
        }
        return aVarD.e().h().getAddress();
    }

    public List<String> getProxyNodeAddresses() {
        LinkedList linkedList = new LinkedList();
        u.a aVarD = G.a().d().d();
        if (aVarD != null && aVarD.e() != null && aVarD.e().i() != null) {
            Iterator<BluetoothDevice> it = aVarD.e().i().iterator();
            while (it.hasNext()) {
                linkedList.add(it.next().getAddress());
            }
        }
        return linkedList;
    }

    public synchronized TgMeshManager init(Context context, AuthInfoListener authInfoListener) {
        a.a.a.a.b.m.a.a(TAG, "init, " + hashCode() + ", isInitialized: " + this.isInitialized + ", isBound: " + this.isBound);
        if (!this.isEnvInitialized) {
            if (context != null && authInfoListener != null) {
                this.mContext = context.getApplicationContext();
                this.isBluetoothEnabled = Utils.isBleEnabled();
                this.isLocationEnabled = Utils.isLocationEnabled(context);
                na.a().a(authInfoListener, a.a.a.a.b.d.a.f1315a ? new DefaultMeshConfig() : new MeshNetworkBizOverUnifiedConnectChannel());
                SequenceNumber.getInstance().init(this.mContext, na.a().b());
                LocalBroadcastManager.getInstance(this.mContext).registerReceiver(this.mBroadcastReceiver, Utils.createIntentFilters());
                this.isEnvInitialized = true;
            }
            a.a.a.a.b.m.a.d(TAG, "Context is null or authInfoListener is null...");
            return this;
        }
        if (!this.isBound) {
            try {
                Intent intent = new Intent(this.mContext, (Class<?>) MeshService.class);
                if (this.mServiceConnection == null) {
                    this.mServiceConnection = new MeshConnection();
                }
                boolean zBindService = this.mContext.bindService(intent, this.mServiceConnection, 1);
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("bindService... ");
                sb.append(zBindService);
                a.a.a.a.b.m.a.a(str, sb.toString());
            } catch (Exception e) {
                a.a.a.a.b.m.a.b(TAG, e.getMessage());
                e.printStackTrace();
            }
        }
        a.a.a.a.b.l.c.b();
        return this;
    }

    public void initIoTMultiend(String str) {
        a.a.a.a.b.m.a.c(TAG, "Start init IoTMultiendInOneSDK with uuid: " + str);
        c cVar = this.multiEndinOneProxy;
        if (cVar == null) {
            this.multiEndinOneProxy = new c(str, na.a());
        } else {
            cVar.a(str);
        }
        IoTMultiendInOneBridge.getInstance().setUpstreamProxy(this.multiEndinOneProxy);
        new Thread(new Runnable() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.15
            @Override // java.lang.Runnable
            public void run() {
                IoTMultiendInOneBridge.getInstance().init(TgMeshManager.this.mContext);
                if (TgMeshManager.this.isConnected) {
                    IoTMultiendInOneBridge.getInstance().enableHeartbeat(true);
                }
            }
        }).start();
    }

    public void isConnected4JsBridge(final IActionListener<Boolean> iActionListener) {
        a.a.a.a.b.m.a.c(TAG, "isConnected4JsBridge..");
        if (isLowCostDeviceExist()) {
            isGenieBoxOnline(new IActionListener<Boolean>() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.8
                @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
                public void onFailure(int i, String str) {
                    iActionListener.onSuccess(true);
                }

                @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
                public void onSuccess(Boolean bool) {
                    iActionListener.onFailure(-3, "recommend go genie box control");
                }
            });
        } else {
            iActionListener.onFailure(-2, "not low-cost mesh device exist");
        }
    }

    public boolean isConnectedToMesh() {
        if (!this.isInitialized) {
            a.a.a.a.b.m.a.b(TAG, "TgMeshManager has not initialized, " + hashCode());
            return false;
        }
        if (!this.isBound) {
            a.a.a.a.b.m.a.b(TAG, "TgMeshManager has not bond to service, " + hashCode());
            return false;
        }
        this.isBluetoothEnabled = Utils.isBleEnabled();
        this.isLocationEnabled = Utils.isLocationEnabled(this.mContext);
        boolean zIsLocationPermissionsGranted = Utils.isLocationPermissionsGranted(this.mContext);
        if (Build.VERSION.SDK_INT < 33 && !this.isLocationEnabled) {
            a.a.a.a.b.m.a.b(TAG, "Location not enabled, " + hashCode());
            return false;
        }
        if (!this.isBluetoothEnabled) {
            a.a.a.a.b.m.a.b(TAG, "Bluetooth disabled, " + hashCode());
            return false;
        }
        if (zIsLocationPermissionsGranted) {
            u.a aVarD = G.a().d().d();
            return aVarD != null && aVarD.f();
        }
        a.a.a.a.b.m.a.b(TAG, "location permission not granted,  " + hashCode());
        return false;
    }

    public boolean isConnecting() {
        return this.isConnecting;
    }

    public boolean isInitialized() {
        return this.isInitialized && this.isBound;
    }

    public boolean isLowPowerDevice(String str) {
        return MeshDeviceInfoManager.getInstance().isLowPowerDevice(MeshDeviceInfoManager.getInstance().coverIotIdToDevId(str));
    }

    public boolean isMeshNodeReachable(String str) {
        String strCoverIotIdToDevId = MeshDeviceInfoManager.getInstance().coverIotIdToDevId(str);
        u.a aVarC = G.a().d().c(strCoverIotIdToDevId);
        boolean z = aVarC != null && aVarC.f();
        a.a.a.a.b.m.a.a("TgMeshManager", String.format("%s:%s reachable result: %b", str, strCoverIotIdToDevId, Boolean.valueOf(z)));
        return z;
    }

    public void localControlTranslation(final String str, final String str2, final IActionListener<String> iActionListener) {
        a.a.a.a.b.m.a.c(TAG, String.format("localControlTranslation, devId:%s, params:%s", str, str2));
        this.handler.post(new Runnable() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.16
            @Override // java.lang.Runnable
            public void run() {
                IoTMultiendInOneBridge.getInstance().localControlTranslation(MeshDeviceInfoManager.getInstance().coverIotIdToDevId(str), str2, new IResponseCallback() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.16.1
                    @Override // com.alibaba.android.multiendinonebridge.IResponseCallback
                    public void onResponse(int i, String str3) {
                        a.a.a.a.b.m.a.a(TgMeshManager.TAG, "localControlTranslation code: " + i + ", data: " + str3);
                        if (i != 0) {
                            Utils.notifyFailed(iActionListener, -26, "local control failed");
                        } else if (JSON.parseObject(str3).getInteger("code").intValue() == 200) {
                            Utils.notifySuccess((IActionListener<String>) iActionListener, str3);
                        } else {
                            Utils.notifyFailed(iActionListener, -27, "The script does not support translation");
                        }
                    }
                });
            }
        });
    }

    public synchronized void notifyMeshMessage(final byte[] bArr, final String str, final byte[] bArr2, final byte[] bArr3, final int i, final String str2) {
        String strBytes2HexString = ConvertUtils.bytes2HexString(bArr2);
        short s = ByteBuffer.wrap(bArr).order(ByteOrder.BIG_ENDIAN).getShort();
        SigmeshIotDev sigmeshIotDevQueryDeviceInfoByUnicastAddress = MeshDeviceInfoManager.getInstance().queryDeviceInfoByUnicastAddress(s, bArr3);
        if (sigmeshIotDevQueryDeviceInfoByUnicastAddress != null) {
            a.a.a.a.b.m.a.c(TAG, String.format("localStateTranslation, devId:%s, opCode:%s, params:%s, receivedConfigCount:%d, seq:%s", sigmeshIotDevQueryDeviceInfoByUnicastAddress.getDevId(), str, strBytes2HexString, Byte.valueOf(this.mReceivedConfigCount), Integer.valueOf(i)));
            IoTMultiendInOneBridge.getInstance().localStateTranslation(sigmeshIotDevQueryDeviceInfoByUnicastAddress.getDevId(), str, strBytes2HexString, new IResponseCallback() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.2
                @Override // com.alibaba.android.multiendinonebridge.IResponseCallback
                public void onResponse(int i2, String str3) {
                    a.a.a.a.b.m.a.a(TgMeshManager.TAG, "localStateTranslation code: " + i2 + ", data: " + str3);
                    TgMeshManager.this.notifyCallback(i2, str3, str, bArr2, bArr, bArr3, i, str2);
                }
            });
            return;
        }
        a.a.a.a.b.m.a.d(TAG, "Failed to get device based on uniCast address: " + ((int) s));
        notifyCallback(-1, null, str, bArr2, bArr, bArr3, i, str2);
    }

    public void offerBizRequest(SIGMeshBizRequest sIGMeshBizRequest) {
        if (this.mBinder != null) {
            LinkedList linkedList = new LinkedList();
            linkedList.add(sIGMeshBizRequest);
            this.mBinder.b(linkedList);
        }
    }

    public DevOnlineStatus queryDeviceOnlineStatus(String str) {
        u.a aVarC = G.a().d().c(str);
        boolean z = aVarC != null && aVarC.f();
        DevOnlineStatus devOnlineStatusFromIntValue = DevOnlineStatus.fromIntValue(IoTMultiendInOneBridge.getInstance().queryDevOnlineStatus(str));
        return (z || devOnlineStatusFromIntValue != DevOnlineStatus.DEV_ST_ONLINE) ? devOnlineStatusFromIntValue : DevOnlineStatus.DEV_ST_OFFLINE;
    }

    public synchronized TgMeshManager refresh() {
        a.a.a.a.b.m.a.a(TAG, "refresh...");
        MeshService.b bVar = this.mBinder;
        if (bVar != null) {
            bVar.j();
            this.isConnecting = true;
        }
        return this;
    }

    public void refreshSIGMeshNodeList() {
        MeshService.b bVar = this.mBinder;
        if (bVar != null) {
            bVar.c();
        }
    }

    public TgMeshManager registerCallback(MeshStatusCallback meshStatusCallback) {
        a.a.a.a.b.m.a.a(TAG, "registerCallback...");
        if (meshStatusCallback != null && !this.mMeshStatusCallbacks.contains(meshStatusCallback)) {
            this.mMeshStatusCallbacks.add(meshStatusCallback);
        }
        return this;
    }

    public void registerDeviceOnlineStatusListener(DeviceOnlineStatusListener deviceOnlineStatusListener) {
        c cVar = this.multiEndinOneProxy;
        if (cVar == null) {
            a.a.a.a.b.m.a.b(TAG, "Internal error: multiEndinOneProxy is null");
        } else {
            cVar.a(deviceOnlineStatusListener);
        }
    }

    public void registerFastProvisionMeshScanStrategy() {
    }

    public synchronized void registerMeshMessageListener(MeshMsgListener meshMsgListener) {
        if (!this.meshMsgListenerList.contains(meshMsgListener)) {
            this.meshMsgListenerList.add(meshMsgListener);
        }
    }

    public void removeNodeAfterUnbind(String str, int i, int i2) {
        a.a.a.a.b.m.a.c(TAG, "removeNodeAfterUnbind: deviceKey = " + str + ", netKeyIndex = " + i + ", primaryUnicastAddress = " + i2);
        if (TextUtils.isEmpty(str)) {
            publishStatus(-1, "DeviceId is not empty or productKey is empty");
            a.a.a.a.b.m.a.b(TAG, "ProductKey is empty");
        } else {
            if (checkEnv() != 0) {
                return;
            }
            offerBizRequest(SIGMeshBizRequestGenerator.a(null, null, str, i, i2, new IActionListener<Boolean>() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.10
                @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
                public void onFailure(int i3, String str2) {
                    a.a.a.a.b.m.a.a(TgMeshManager.TAG, "onFailure() called with: errorCode = [" + i3 + "], desc = [" + str2 + "]");
                }

                @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
                public void onSuccess(Boolean bool) {
                    a.a.a.a.b.m.a.a(TgMeshManager.TAG, "onSuccess() called with: result = [" + bool + "]");
                }
            }));
            a.a.a.a.b.m.a.a(TAG, "removeNodeAfterUnbind");
        }
    }

    public void resetIoTMultiendInOneBridge() {
        a.a.a.a.b.m.a.c(TAG, "Reset IoTMultiendInOneBridge");
        IoTMultiendInOneBridge.getInstance().reset();
    }

    public void sendGroupMessage(String str, int i, String str2, String str3, Map<String, Object> map, IActionListener<Boolean> iActionListener) {
    }

    public void sendGroupMessage(final String str, final String str2, String str3, Map<String, Object> map, final IActionListener<Boolean> iActionListener) {
        int i;
        a.a.a.a.b.e.a.a();
        final JSONObject jSONObject = new JSONObject();
        if (a.a.a.a.b.d.a.f1315a) {
            jSONObject.put("msgId", (Object) "");
            jSONObject.put("devId", map.get("deviceId"));
            jSONObject.put("familyId", map.get("familyId"));
            HashMap map2 = new HashMap();
            map2.put("pushGenie", ((Integer) map.get("channel")).intValue() > 0 ? "true" : RequestConstant.FALSE);
            jSONObject.put("extension", (Object) map2);
        } else if (map.containsKey("iotId")) {
            jSONObject.put("iotId", map.get("iotId"));
        }
        jSONObject.put("method", (Object) str2);
        jSONObject.put("version", (Object) "1.0");
        Map map3 = (Map) JSONObject.parseObject(str3, Map.class);
        jSONObject.put("params", (Object) map3);
        a.a.a.a.b.m.a.a(TAG, "sendGroupMessage: " + jSONObject.toJSONString());
        try {
            i = Integer.parseInt(String.valueOf(map.get("groupAddress")));
        } catch (Exception unused) {
            i = 0;
        }
        final int i2 = i;
        if (i2 == 0) {
            groupControlByServer(str, str2, jSONObject.toJSONString(), iActionListener);
            return;
        }
        if (!"thing.attribute.set".equals(str2)) {
            groupControlByServer(str, str2, jSONObject.toJSONString(), iActionListener);
            return;
        }
        String strCoverIotIdToDevId = MeshDeviceInfoManager.getInstance().coverIotIdToDevId((String) map.get("deviceId"));
        a.a.a.a.b.m.a.a(TAG, "local group control decision making:" + strCoverIotIdToDevId);
        IoTMultiendInOneBridge.getInstance().localControlTranslation(strCoverIotIdToDevId, JSON.toJSONString(map3), new IResponseCallback() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.30
            @Override // com.alibaba.android.multiendinonebridge.IResponseCallback
            public void onResponse(int i3, String str4) {
                a.a.a.a.b.m.a.a(TgMeshManager.TAG, "localGroupControlTranslation code: " + i3 + ", result: " + str4);
                if (i3 == 0 && TgMeshManager.this.mBinder != null) {
                    JSONObject object = JSON.parseObject(str4);
                    if (object.getInteger("code").intValue() == 200) {
                        List javaList = object.getJSONArray("data").toJavaList(Sigmesh.class);
                        if (!ListUtils.isEmpty(javaList) && javaList.get(0) != null) {
                            Sigmesh sigmesh = (Sigmesh) javaList.get(0);
                            if (sigmesh.getDevice() != null && sigmesh.getAction() != null) {
                                int appKeyIndex = sigmesh.getDevice().getAppKeyIndex();
                                int netKeyIndex = sigmesh.getDevice().getNetKeyIndex();
                                if (sigmesh.getAction().getOpcode() != null && sigmesh.getAction().getParameters() != null) {
                                    TgMeshManager.this.mBinder.a(netKeyIndex, appKeyIndex, i2, Utils.byteArray2Int(Utils.getOpCodeBytes(Integer.parseInt(sigmesh.getAction().getOpcode(), 16))), MeshParserUtils.toByteArray(sigmesh.getAction().getParameters()), iActionListener);
                                    return;
                                }
                            }
                        }
                    }
                }
                TgMeshManager.this.groupControlByServer(str, str2, jSONObject.toJSONString(), iActionListener);
            }
        });
    }

    public void sendMessge(int i, int i2, int i3, int i4, byte[] bArr, final IActionListener iActionListener) {
        a.a.a.a.b.m.a.a(TAG, "SendMessage, opcode(" + i4 + "), parameters(" + MeshParserUtils.bytesToHex(bArr, false));
        if (i == 65535) {
            a.a.a.a.b.m.a.d(TAG, "The application cannot send messages to this multicast address: 0xffff");
            return;
        }
        if (i4 != 33281) {
            a.a.a.a.b.e.a.a();
        }
        final UtTraceInfo utTraceInfoA = f.a().a(i);
        a.a.a.a.b.l.c.a("SDKReceiveCmd", utTraceInfoA);
        int iCheckEnv = checkEnv();
        if (iCheckEnv != 0) {
            a.a.a.a.b.l.c.a(utTraceInfoA, iCheckEnv, "");
            iActionListener.onFailure(-22, "Unavailable");
            return;
        }
        if (G.a().d().d() == null) {
            a.a.a.a.b.l.c.a(utTraceInfoA, 20003, "Subnets are not initialized");
            Utils.notifyFailed(iActionListener, -25, "Subnets are not initialized");
            return;
        }
        if (MeshDeviceInfoManager.getInstance().isLowPowerDevice(i)) {
            a.a.a.a.b.m.a.a(TAG, "lowerPower sendMessage");
            MeshControlDevice meshControlDevice = new MeshControlDevice();
            meshControlDevice.d(i);
            meshControlDevice.a(i2);
            meshControlDevice.b(i3);
            meshControlDevice.c(i4);
            meshControlDevice.a(bArr);
            meshControlDevice.a(iActionListener);
            meshControlDevice.a(false);
            this.mControlTask.a(new a.a.a.a.b.j.b(this.mBinder, meshControlDevice));
            return;
        }
        MeshService.b bVar = this.mBinder;
        if (bVar == null) {
            a.a.a.a.b.l.c.a(utTraceInfoA, 20012, "mesh network not connected");
            iActionListener.onFailure(-24, "binder is null");
            return;
        }
        bVar.a(i3, i2, i, i4, bArr, new IActionListener<Boolean>() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.14
            @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
            public void onFailure(int i5, String str) {
                Utils.notifyFailed(iActionListener, i5, str);
                if (i5 == -30) {
                    a.a.a.a.b.l.c.a(utTraceInfoA, 20003, "Subnets are not initialized");
                } else {
                    if (i5 != -23) {
                        return;
                    }
                    a.a.a.a.b.l.c.a(utTraceInfoA, UtError.MESH_NETWORK_NOT_CONNECT.getCode(), UtError.MESH_NETWORK_NOT_CONNECT.getMsg());
                }
            }

            @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
            public void onSuccess(Boolean bool) {
                Utils.notifySuccess((IActionListener<Boolean>) iActionListener, bool);
            }
        });
        a.a.a.a.b.m.a.a(TAG, "sendMessage: " + i);
    }

    public void sendMsg2LowCostMeshDevice(int i, int i2, int i3, int i4, byte[] bArr, IActionListener iActionListener) {
        a.a.a.a.b.m.a.a(TAG, "sendMsg2LowCostMeshDevice, opcode(" + i4 + "), parameters(" + MeshParserUtils.bytesToHex(bArr, false));
        UtTraceInfo utTraceInfoA = f.a().a(i);
        a.a.a.a.b.l.c.a("SDKReceiveCmd", utTraceInfoA);
        int iCheckEnv = checkEnv();
        if (iCheckEnv != 0) {
            a.a.a.a.b.l.c.a(utTraceInfoA, iCheckEnv, "");
            iActionListener.onFailure(-22, "Unavailable");
            return;
        }
        if (this.mBinder == null) {
            a.a.a.a.b.l.c.a(utTraceInfoA, 20012, "mesh network not connected");
            iActionListener.onFailure(-24, "binder is null");
            return;
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("mesh_device_type", Integer.valueOf(Constants.AliMeshSolutionType.TINY_MESH_ADV_V1.getSolutionType()));
        this.mBinder.a(i3, i2, i, i4, bArr, linkedHashMap, (IActionListener<Boolean>) iActionListener);
        a.a.a.a.b.m.a.a(TAG, "SendMessage: " + i);
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x00d6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void setGatewayAccsMsg(java.lang.String r9) {
        /*
            Method dump skipped, instruction units count: 232
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.ailabs.iot.mesh.TgMeshManager.setGatewayAccsMsg(java.lang.String):void");
    }

    public void setMacAddressWhiteList(List<String> list) {
        MeshService.b bVar = this.mBinder;
        if (bVar != null) {
            bVar.c(list);
        }
    }

    public void setMeshDeviceInfoList(List<MeshDeviceInfo> list) {
        a.a.a.a.b.m.a.c(TAG, "setMeshDeviceInfoList");
        MeshDeviceInfoManager.getInstance().setMeshDeviceInfo(list);
    }

    public void setOnReadyToBindHandler(OnReadyToBindHandler onReadyToBindHandler) {
        MeshService.b bVar = this.mBinder;
        if (bVar != null) {
            bVar.a(onReadyToBindHandler);
        } else {
            this.mPendingReadyToBindHandler = onReadyToBindHandler;
        }
    }

    public void setTraceInfo(UtTraceInfo utTraceInfo) {
        f.a().a(utTraceInfo);
    }

    public synchronized void stop(Context context) {
        a.a.a.a.b.m.a.a(TAG, "onStop...");
        if (context == null) {
            a.a.a.a.b.m.a.d(TAG, "Context is null...");
            return;
        }
        if (this.isBound) {
            this.mBinder.b();
            try {
                if (this.mContext != null) {
                    this.mContext.unbindService(this.mServiceConnection);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            this.mBinder = null;
            this.isBound = false;
        }
        context.stopService(new Intent(context, (Class<?>) MeshService.class));
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this.mBroadcastReceiver);
        this.mMeshStatusCallbacks.clear();
        this.isConnecting = false;
        this.isConnected = false;
        this.connectFail = UtError.MESH_INIT_FAILED_STOP.getMsg();
        this.isEnvInitialized = false;
        this.isInitialized = false;
        this.isEnvInitialized = false;
        this.mServiceConnection = null;
        a.a.a.a.b.m.a.c(TAG, "set isInitialized" + this.isInitialized);
    }

    public void stopAddNode() {
        BLEScannerProxy.getInstance().unlock();
        G.a().d().h();
        MeshService.b bVar = this.mBinder;
        if (bVar != null) {
            bVar.l();
        }
        try {
            if (this.fixedThreadPool != null && !this.fixedThreadPool.isTerminated()) {
                this.fixedThreadPool.shutdownNow();
                this.fixedThreadPool = null;
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        G.a().d().f();
    }

    public void unbind(String str, String str2, boolean z) {
        a.a.a.a.b.m.a.a(TAG, "Unbind..");
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            publishStatus(-1, "DeviceId is not empty or productKey is empty");
            a.a.a.a.b.m.a.b(TAG, "DeviceId is not empty or productKey is empty");
        } else {
            if (checkEnv() != 0) {
                return;
            }
            if (!this.isConnected) {
                publishStatus(-2, "disconnected...");
                return;
            }
            MeshService.b bVar = this.mBinder;
            if (bVar != null) {
                bVar.a(str, str2, z);
                a.a.a.a.b.m.a.a(TAG, "Unbind to device");
            }
        }
    }

    public void unregisterCallback(MeshStatusCallback meshStatusCallback) {
        if (meshStatusCallback != null) {
            this.mMeshStatusCallbacks.remove(meshStatusCallback);
        }
    }

    public void unregisterDeviceOnlineStatusListener(DeviceOnlineStatusListener deviceOnlineStatusListener) {
        c cVar = this.multiEndinOneProxy;
        if (cVar == null) {
            a.a.a.a.b.m.a.b(TAG, "Internal error: multiEndinOneProxy is null");
        } else if (deviceOnlineStatusListener != null) {
            cVar.b(deviceOnlineStatusListener);
        }
    }

    public synchronized void unregisterMeshMessageListener(MeshMsgListener meshMsgListener) {
        List<MeshMsgListener> list = this.meshMsgListenerList;
        if (list != null) {
            list.remove(meshMsgListener);
        }
    }

    public void updateMeshNetworkParameters(boolean z) {
        if (!z || SIGMeshBizRequest.NetworkParameter.changeNetworkParameter(this.mNetworkParameter, false)) {
            this.mNetworkParameter = SIGMeshBizRequest.NetworkParameter.getNetworkParameter(MeshDeviceInfoManager.getInstance().getSigmeshIotDevMap().size());
            C0329d.a().b();
        }
    }

    public void wakeUpDevice(final String str, final IActionListener<Boolean> iActionListener) {
        a.a.a.a.b.m.a.a(TAG, "wakeUpDevice");
        isConnected4JsBridge(new IActionListener<Boolean>() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.11
            @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
            public void onFailure(int i, String str2) {
                TgMeshManager.this.execWakeUpDevice(str, true, iActionListener);
            }

            @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
            public void onSuccess(Boolean bool) {
                TgMeshManager.this.execWakeUpDevice(str, false, iActionListener);
            }
        });
    }

    public void wifiConfig(String str, Map<String, Object> map, IActionListener iActionListener) {
        a.a.a.a.b.m.a.c(TAG, String.format("config wifi info over mesh channel, devId [%s]", str));
        MeshService.b bVar = this.mBinder;
        if (bVar != null) {
            bVar.a(str, map, iActionListener);
        } else {
            Utils.notifyFailed(iActionListener, -24, "mesh binder is null");
        }
    }

    public TgMeshManager() {
        this.productKey = "";
        this.isEnvInitialized = false;
        this.handler = new Handler(Looper.getMainLooper());
        this.mControlTask = new a(Looper.getMainLooper());
        this.mPendingReadyToBindHandler = null;
        this.fixedThreadPool = null;
        this.mSingleThreadExecutor = null;
        this.mPendingProvisionDeviceAddress = new LinkedList();
        this.mReceivedConfigCount = (byte) 0;
        this.translateControlCommandByCloud = new AtomicBoolean(false);
        this.mBroadcastReceiver = new BroadcastReceiver() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent == null) {
                }
                String action = intent.getAction();
                if (TextUtils.isEmpty(action)) {
                    return;
                }
                switch (action) {
                    case "ACTION_INIT_SUCCESS":
                        TgMeshManager.this.isInitialized = true;
                        a.a.a.a.b.m.a.a(TgMeshManager.TAG, "isBound: " + TgMeshManager.this.isBound);
                        if (TgMeshManager.this.isBound) {
                            TgMeshManager.this.publishStatus(1, "Initialize success...");
                            a.a.a.a.b.m.a.a(TgMeshManager.TAG, "Initialize success..." + TgMeshManager.this.isInitialized);
                            break;
                        }
                        break;
                    case "ACTION_INIT_FAILED":
                        TgMeshManager tgMeshManager = TgMeshManager.this;
                        tgMeshManager.stop(tgMeshManager.mContext);
                        TgMeshManager.this.isInitialized = false;
                        TgMeshManager.this.publishStatus(-1, "Service initialize failed...");
                        a.a.a.a.b.m.a.a(TgMeshManager.TAG, "Service initialize failed...");
                        break;
                    case "ACTION_PROVISIONED_NODE_FOUND":
                        if (intent.getExtras() != null) {
                            String string = intent.getExtras().getString(Utils.EXTRA_DATA);
                            TgMeshManager.this.publishStatus(0, "Provisioned node " + string + " found");
                            a.a.a.a.b.m.a.a(TgMeshManager.TAG, "Provisioned node " + string + " found");
                            break;
                        }
                        break;
                    case "ACTION_CONNECTION_STATE":
                        if (intent.getExtras() != null) {
                            String string2 = intent.getExtras().getString(Utils.EXTRA_DATA);
                            TgMeshManager.this.publishStatus(0, string2);
                            a.a.a.a.b.m.a.a(TgMeshManager.TAG, string2);
                            break;
                        }
                        break;
                    case "ACTION_IS_RECONNECTING":
                        if (intent.getExtras() != null) {
                            boolean z = intent.getExtras().getBoolean(Utils.EXTRA_DATA);
                            TgMeshManager.this.publishStatus(0, "isReconnecting: " + z);
                            a.a.a.a.b.m.a.a(TgMeshManager.TAG, "isReconnecting: " + z);
                            break;
                        }
                        break;
                    case "ACTION_ON_DEVICE_READY":
                        if (intent.getExtras() != null) {
                            boolean z2 = intent.getExtras().getBoolean(Utils.EXTRA_DATA);
                            TgMeshManager.this.publishStatus(0, "isDeviceReady: " + z2);
                            a.a.a.a.b.m.a.a(TgMeshManager.TAG, "isDeviceReady: " + z2);
                            break;
                        }
                        break;
                    case "ACTION_IS_CONNECTED":
                        if (intent.getExtras() != null) {
                            TgMeshManager.this.isConnected = intent.getExtras().getBoolean(Utils.EXTRA_DATA);
                            if (!TgMeshManager.this.isConnected) {
                                IoTMultiendInOneBridge.getInstance().enableHeartbeat(TgMeshManager.this.isConnected);
                            }
                            TgMeshManager tgMeshManager2 = TgMeshManager.this;
                            tgMeshManager2.publishStatus(tgMeshManager2.isConnected ? 2 : -2, "isConnected: " + TgMeshManager.this.isConnected);
                            a.a.a.a.b.m.a.a(TgMeshManager.TAG, "isConnected: " + TgMeshManager.this.isConnected);
                            if (TgMeshManager.this.isConnected) {
                                if (a.a.a.a.b.d.a.f1315a) {
                                    TgMeshManager.this.mControlTask.postDelayed(new Runnable() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.1.1
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            a.a.a.a.b.l.c.d();
                                        }
                                    }, 3000L);
                                }
                                TgMeshManager.this.updateMeshNetworkParameters(false);
                                TgMeshManager.this.onMeshNetworkReady();
                            } else {
                                TgMeshManager.this.connectFail = intent.getExtras().getString(Utils.EXTRA_CONNECT_FAIL_MSG);
                            }
                        }
                        TgMeshManager.this.isConnecting = false;
                        break;
                    case "ACTION_PROVISIONING_STATE":
                        TgMeshManager.this.handleProvisioningStates(intent);
                        break;
                    case "ACTION_CONFIGURATION_STATE":
                        TgMeshManager.this.handleConfigurationStates(intent);
                        break;
                    case "ACTION_BIND_STATE":
                        TgMeshManager.this.handleBindState(intent);
                        break;
                    case "ACTION_OPERATE_SUCCESS":
                        TgMeshManager.this.publishStatus(9, intent.getStringExtra("message"));
                        break;
                    case "ACTION_TRANSACTION_STATE":
                        TgMeshManager.this.onTransactionStateReceived(intent);
                        break;
                    case "ACTION_COMMON_MESSAGE_STATUS_RECEIVED":
                        if (intent.getExtras() != null) {
                            TgMeshManager.this.publishStatus(9, intent.getExtras().getString(Utils.EXTRA_STATUS));
                            break;
                        }
                        break;
                    case "ACTION_MQTT_CONNECTED":
                        if (intent.getExtras() != null) {
                            TgMeshManager.this.initIoTMultiend(intent.getExtras().getString(Utils.EXTRA_MQTT_DATA));
                            break;
                        }
                        break;
                    case "ACTION_MQTT_RECEIVED":
                        if (intent.getExtras() != null) {
                            TgMeshManager.this.setGatewayAccsMsg(intent.getExtras().getString(Utils.EXTRA_MQTT_DATA));
                            break;
                        }
                        break;
                    case "com.aliyun.iot.ilop.agree.share.device":
                    case "com.aliyun.iot.ilop.unbind.share.device":
                    case "ACTION_MQTT_SHARE_DEVICE_UNBIND":
                        a.a.a.a.b.m.a.a(TgMeshManager.TAG, "share device is changed");
                        if (TgMeshManager.this.mBinder != null) {
                            TgMeshManager.this.mBinder.c();
                            break;
                        }
                        break;
                }
            }
        };
        this.provisionFinishedCounter = 0;
        this.mConfigTaskQueue = new LinkedList();
        this.mInConfigProgress = new AtomicBoolean(false);
        this.mExtendedMeshNode = new ExtendedMeshNode(new UnprovisionedMeshNode());
        this.mMeshStatusCallbacks = new ArrayList();
        this.meshMsgListenerList = new CopyOnWriteArrayList();
        this.multiEndinOneProxy = new c(na.a());
        initBizServiceModule();
    }

    public void batchBindDevices(List<UnprovisionedBluetoothMeshDevice> list, Map<String, Object> map) {
        a.a.a.a.b.m.a.a(TAG, "batchBindDevices() called with: devicesList.size = [" + list.size() + "]");
        a.a.a.a.b.m.a.c(TAG, "Build.VERSION.RELEASE: " + Build.VERSION.RELEASE + ", Build.MODEL: " + Build.MODEL + ", Build.BOARD: " + Build.BOARD);
        if (list == null || list.isEmpty()) {
            publishStatus(-7, "DeviceId is null");
            a.a.a.a.b.m.a.b(TAG, "DeviceId is null");
            return;
        }
        BLEScannerProxy.getInstance().lock();
        u uVarD = G.a().d();
        G.a().d().e();
        uVarD.a(false, new MeshService.OnDisconnectListener() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.4
            @Override // com.alibaba.ailabs.iot.mesh.MeshService.OnDisconnectListener
            public void onDisconnected() {
            }
        });
        uVarD.e();
        boolean z = this.mPendingProvisionDeviceAddress.size() == 0;
        a.a.a.a.b.m.a.c(TAG, "Fist provision: " + z);
        Iterator<UnprovisionedBluetoothMeshDevice> it = list.iterator();
        while (it.hasNext()) {
            this.mPendingProvisionDeviceAddress.add(it.next().getBluetoothDevice().getAddress());
        }
        int sigMeshProductID = list.get(0).getSigMeshProductID();
        if (z) {
            C0327b c0327bB = C0327b.b();
            int iMin = Math.min(4, this.mPendingProvisionDeviceAddress.size());
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < iMin; i++) {
                arrayList.add(this.mPendingProvisionDeviceAddress.get(i));
            }
            c0327bB.a(this.mPendingProvisionDeviceAddress);
            c0327bB.a(arrayList, sigMeshProductID);
        }
        int size = list.size();
        for (int i2 = 0; i2 < size; i2++) {
            bindDevice(list.get(i2), map);
        }
        MeshService.b bVar = this.mBinder;
        if (bVar != null) {
            bVar.a(list.get(size - 1).getBluetoothDevice().getAddress());
        }
    }

    public void bindDevice(UnprovisionedBluetoothMeshDevice unprovisionedBluetoothMeshDevice, final Map<String, Object> map) {
        ExecutorService executorService;
        if (unprovisionedBluetoothMeshDevice == null) {
            publishStatus(-7, "DeviceId is null");
            a.a.a.a.b.m.a.b(TAG, "DeviceId is null");
            return;
        }
        a.a.a.a.b.m.a.a(TAG, "Start binding device: " + unprovisionedBluetoothMeshDevice.getAddress() + " threadId=" + Thread.currentThread());
        b.a("ALSMesh", "ble", String.valueOf(unprovisionedBluetoothMeshDevice.getSigMeshProductID()), true, unprovisionedBluetoothMeshDevice.getUUID(), "");
        if (checkEnv() != 0) {
            return;
        }
        if (!Constants.f2822a) {
            MeshService.b bVar = this.mBinder;
            this.isConnecting = bVar != null && bVar.d() == 1;
            if (this.isConnecting) {
                a.a.a.a.b.m.a.a(TAG, "in connecting");
                publishStatus(8, "connecting...");
                return;
            }
        }
        if (this.mBinder != null) {
            final ExtendedBluetoothDevice extendedBluetoothDevice = new ExtendedBluetoothDevice(unprovisionedBluetoothMeshDevice.getMeshScanResult());
            boolean zIsSupportGatt = unprovisionedBluetoothMeshDevice.isSupportGatt();
            extendedBluetoothDevice.setConfigurationInfo(unprovisionedBluetoothMeshDevice.getmUnprovisionedMeshNodeData().getConfigurationInfo());
            extendedBluetoothDevice.setPk(unprovisionedBluetoothMeshDevice.getPk());
            if (!Constants.f2822a) {
                a.a.a.a.b.m.a.a(TAG, "Connecting...");
                this.mBinder.a(extendedBluetoothDevice, false, map);
                this.isConnecting = true;
                return;
            }
            if (zIsSupportGatt) {
                if (this.fixedThreadPool == null) {
                    int iA = a.a.a.a.b.i.a.a.a(this.mContext, "max_provisioner_number", 3);
                    if (iA > 3 || iA < 1) {
                        iA = 3;
                    }
                    this.fixedThreadPool = Executors.newFixedThreadPool(iA);
                }
                executorService = this.fixedThreadPool;
            } else {
                if (this.mSingleThreadExecutor == null) {
                    this.mSingleThreadExecutor = Executors.newSingleThreadExecutor();
                }
                executorService = this.mSingleThreadExecutor;
            }
            executorService.execute(new Runnable() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.3
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        TgMeshManager.this.mBinder.a(extendedBluetoothDevice, false, map);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            this.isConnecting = true;
            this.mBinder.a(unprovisionedBluetoothMeshDevice.getBluetoothDevice().getAddress());
        }
    }

    public void offerBizRequest(List<SIGMeshBizRequest> list) {
        MeshService.b bVar = this.mBinder;
        if (bVar != null) {
            bVar.b(list);
        }
    }

    public void configModelSubscription(Sigmesh.Device device, final Sigmesh.Action action, IActionListener<Boolean> iActionListener) {
        if (device != null && action != null) {
            SIGMeshBizRequest.Type type = SIGMeshBizRequest.Type.COMMON_REQUEST_RESPONSE;
            type.setOpcode(Integer.parseInt(action.getOpcode(), 16));
            type.setExpectedOpcode(Integer.parseInt(action.getStatusOpcode(), 16));
            u.a aVarD = G.a().d().d();
            SIGMeshBizRequest sIGMeshBizRequestA = new SIGMeshBizRequest.a().a(type).a(aVarD != null ? (ProvisionedMeshNode) aVarD.d(AddressUtils.getUnicastAddressBytes(device.getDestAddr())) : null).a(AddressUtils.getUnicastAddressBytes(device.getDestAddr())).a(new I<Object>() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.28
                @Override // a.a.a.a.b.a.I
                public Pair<Integer, ?> parseResponse(Object obj) {
                    if (obj instanceof CfgMsgModelSubscriptionStatus) {
                        return CfgMsgModelSubscriptionStatus.parseStatus(TgMeshManager.this.mContext, ((CfgMsgModelSubscriptionStatus) obj).getStatus());
                    }
                    boolean z = obj instanceof byte[];
                    return null;
                }
            }).a(true).a(iActionListener).a(new SIGMeshBizRequest.b() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.27
                @Override // com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest.b
                public byte[] getEncodedParameters() {
                    return ConvertUtils.hexString2Bytes(action.getParameters());
                }
            }).a();
            sIGMeshBizRequestA.a(ConvertUtils.hexString2Bytes(device.getDeviceKey()));
            offerBizRequest(sIGMeshBizRequestA);
            return;
        }
        Utils.notifyFailed(iActionListener, -32, "device and action not be empty");
    }

    public void connect(List<Integer> list) {
        a.a.a.a.b.m.a.a(TAG, "Connect to the mesh network");
        if (checkEnv() != 0) {
            return;
        }
        MeshService.b bVar = this.mBinder;
        this.isConnecting = bVar != null && bVar.d() == 1;
        if (this.isConnecting) {
            publishStatus(8, "connecting...");
            return;
        }
        u.a aVarD = G.a().d().d();
        if (aVarD == null) {
            this.isConnected = false;
        } else {
            this.isConnected = aVarD.j();
        }
        if (this.isConnected) {
            publishStatus(2, "connected...");
            return;
        }
        MeshService.b bVar2 = this.mBinder;
        if (bVar2 != null) {
            bVar2.a(list);
            this.isConnecting = true;
        }
        a.a.a.a.b.m.a.a(TAG, "Reconnect to mesh");
    }

    public void sendMessge(final String str, int i, String str2, String str3, IActionListener iActionListener) {
        a.a.a.a.b.m.a.c(TAG, String.format("sendMessageV1, devId:%s, channel:%d, method:%s, params:%s", str, Integer.valueOf(i), str2, str3));
        final UtTraceInfo utTraceInfoA = f.a().a(str);
        a.a.a.a.b.l.c.a("SDKReceiveCmd", utTraceInfoA);
        final JSONObject jSONObject = new JSONObject();
        jSONObject.put("method", (Object) str2);
        jSONObject.put("devId", (Object) str);
        jSONObject.put("msgId", (Object) "");
        jSONObject.put("version", (Object) "1.0");
        Map map = (Map) JSONObject.parseObject(str3, Map.class);
        final IActionListener iActionListener2 = iActionListener == null ? new IActionListener() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.17
            @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
            public void onFailure(int i2, String str4) {
            }

            @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
            public void onSuccess(Object obj) {
            }
        } : iActionListener;
        if ("thing.service.invoke".equals(str2)) {
            String str4 = (String) map.get("serviceName");
            if (TextUtils.isEmpty(str4)) {
                iActionListener2.onFailure(-21, "Missing parameter: serviceName");
                return;
            } else {
                map.remove("serviceName");
                jSONObject.put("serviceName", (Object) str4);
            }
        }
        jSONObject.put("params", (Object) map);
        final HashMap map2 = new HashMap();
        map2.put("pushGenie", i > 0 ? "true" : RequestConstant.FALSE);
        jSONObject.put("extension", (Object) map2);
        if (i <= 0 && "thing.attribute.set".equals(str2)) {
            a.a.a.a.b.m.a.a(TAG, "local control decision making");
            IoTMultiendInOneBridge.getInstance().localControlTranslation(MeshDeviceInfoManager.getInstance().coverIotIdToDevId(str), JSON.toJSONString(map), new IResponseCallback() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.18
                @Override // com.alibaba.android.multiendinonebridge.IResponseCallback
                public void onResponse(int i2, String str5) {
                    a.a.a.a.b.m.a.a(TgMeshManager.TAG, "localControlTranslation code: " + i2 + ", result: " + str5);
                    if (i2 == 0) {
                        JSONObject object = JSON.parseObject(str5);
                        if (object.getInteger("code").intValue() == 200) {
                            List javaList = object.getJSONArray("data").toJavaList(Sigmesh.class);
                            if (!ListUtils.isEmpty(javaList) && javaList.get(0) != null) {
                                Sigmesh sigmesh = (Sigmesh) javaList.get(0);
                                if (sigmesh.getDevice() != null && sigmesh.getAction() != null) {
                                    int destAddr = sigmesh.getDevice().getDestAddr();
                                    int appKeyIndex = sigmesh.getDevice().getAppKeyIndex();
                                    int netKeyIndex = sigmesh.getDevice().getNetKeyIndex();
                                    if (sigmesh.getAction().getOpcode() != null && sigmesh.getAction().getParameters() != null) {
                                        TgMeshManager.this.sendMessge(destAddr, appKeyIndex, netKeyIndex, Utils.byteArray2Int(Utils.getOpCodeBytes(Integer.parseInt(sigmesh.getAction().getOpcode(), 16))), MeshParserUtils.toByteArray(sigmesh.getAction().getParameters()), iActionListener2);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                    if (i2 == -2) {
                        map2.put("pushGenie", "true");
                        jSONObject.put("extension", (Object) map2);
                    }
                    TgMeshManager.this.deviceControlByServer(str, jSONObject.toJSONString(), iActionListener2, utTraceInfoA);
                }
            });
        } else {
            na.a().a(str, "3404", jSONObject.toJSONString(), new MeshConfigCallback<List<Sigmesh>>() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.19
                @Override // datasource.MeshConfigCallback
                public void onFailure(String str5, String str6) {
                    a.a.a.a.b.m.a.a(TgMeshManager.TAG, "errorCode:" + str5 + ", errorMessage:" + str6);
                    UtTraceInfo utTraceInfo = utTraceInfoA;
                    StringBuilder sb = new StringBuilder();
                    sb.append(str5);
                    sb.append(str6);
                    a.a.a.a.b.l.c.a(utTraceInfo, 20005, sb.toString());
                    iActionListener2.onFailure(-20, str6);
                }

                @Override // datasource.MeshConfigCallback
                public void onSuccess(List<Sigmesh> list) {
                    if (!ListUtils.isEmpty(list) && list.get(0) != null) {
                        Sigmesh sigmesh = list.get(0);
                        if (sigmesh.getDevice() != null && sigmesh.getAction() != null) {
                            int destAddr = sigmesh.getDevice().getDestAddr();
                            int appKeyIndex = sigmesh.getDevice().getAppKeyIndex();
                            int netKeyIndex = sigmesh.getDevice().getNetKeyIndex();
                            if (sigmesh.getAction().getOpcode() != null && sigmesh.getAction().getParameters() != null) {
                                byte[] opCodeBytes = Utils.getOpCodeBytes(Integer.parseInt(sigmesh.getAction().getOpcode(), 16));
                                TgMeshManager.this.sendMessge(destAddr, appKeyIndex, netKeyIndex, Utils.byteArray2Int(opCodeBytes), MeshParserUtils.toByteArray(sigmesh.getAction().getParameters()), iActionListener2);
                                return;
                            }
                        }
                    }
                    a.a.a.a.b.l.c.a(utTraceInfoA, 20010, "mtop success while parameter illetal");
                    iActionListener2.onFailure(-21, "Wrong parameter");
                }
            });
        }
    }

    public void connect(ConnectionParams connectionParams) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("connect to mesh with params: ");
        sb.append(connectionParams == null ? TmpConstant.GROUP_ROLE_UNKNOWN : connectionParams.toString());
        a.a.a.a.b.m.a.a(str, sb.toString());
        if (connectionParams != null) {
            connectionParams.setDeviceId(MeshDeviceInfoManager.getInstance().coverIotIdToDevId(connectionParams.getDeviceId()));
        }
        MeshService.b bVar = this.mBinder;
        if (bVar == null) {
            a.a.a.a.b.m.a.d(TAG, "MeshService not bind");
        } else {
            bVar.a(connectionParams);
        }
    }

    public void sendMessge(final String str, int i, String str2, String str3, Map<String, Object> map, final IActionListener iActionListener) {
        a.a.a.a.b.m.a.c(TAG, String.format("sendMessageV1, devId:%s, channel:%d, method:%s, params:%s", str, Integer.valueOf(i), str2, str3));
        final UtTraceInfo utTraceInfoA = f.a().a(str);
        a.a.a.a.b.l.c.a("SDKReceiveCmd", utTraceInfoA);
        final JSONObject jSONObject = new JSONObject();
        if (a.a.a.a.b.d.a.f1315a) {
            jSONObject.put("devId", (Object) str);
            jSONObject.put("msgId", (Object) "");
            HashMap map2 = new HashMap();
            map2.put("pushGenie", i > 0 ? "true" : RequestConstant.FALSE);
            jSONObject.put("extension", (Object) map2);
            if (map.containsKey(AlinkConstants.KEY_CATEGORY_KEY) && "Scene".equals((String) map.get(AlinkConstants.KEY_CATEGORY_KEY))) {
                try {
                    offerBizRequest(SIGMeshBizRequestGenerator.a(str, (short) ((Integer) ((Map) JSONObject.parseObject(str3, Map.class)).values().toArray()[0]).intValue(), (IActionListener<Boolean>) iActionListener));
                    return;
                } catch (Exception e) {
                    a.a.a.a.b.m.a.b(TAG, e.toString());
                    return;
                }
            }
        } else {
            if (map.containsKey("iotId")) {
                jSONObject.put("iotId", map.get("iotId"));
            }
            if (map.containsKey("productKey")) {
                jSONObject.put("productKey", map.get("productKey"));
            }
            if (map.containsKey(AlinkConstants.KEY_CATEGORY_KEY) && "Scene".equals((String) map.get(AlinkConstants.KEY_CATEGORY_KEY))) {
                try {
                    offerBizRequest(SIGMeshBizRequestGenerator.a(str, (short) ((Integer) ((Map) JSONObject.parseObject(str3, Map.class)).values().toArray()[0]).intValue(), (IActionListener<Boolean>) iActionListener));
                    return;
                } catch (Exception e2) {
                    a.a.a.a.b.m.a.b(TAG, e2.toString());
                    return;
                }
            }
        }
        jSONObject.put("method", (Object) str2);
        jSONObject.put("version", (Object) "1.0");
        Map map3 = (Map) JSONObject.parseObject(str3, Map.class);
        final IActionListener iActionListener2 = iActionListener == null ? new IActionListener() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.20
            @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
            public void onFailure(int i2, String str4) {
            }

            @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
            public void onSuccess(Object obj) {
            }
        } : iActionListener;
        if ("thing.service.invoke".equals(str2)) {
            String str4 = (String) map3.get("serviceName");
            if (TextUtils.isEmpty(str4)) {
                iActionListener2.onFailure(-21, "Missing parameter: serviceName");
                return;
            } else {
                map3.remove("serviceName");
                jSONObject.put("serviceName", (Object) str4);
            }
        }
        jSONObject.put("params", (Object) map3);
        if (i <= 0 && "thing.attribute.set".equals(str2)) {
            final String strCoverIotIdToDevId = MeshDeviceInfoManager.getInstance().coverIotIdToDevId(str);
            a.a.a.a.b.m.a.a(TAG, "local control decision making:" + strCoverIotIdToDevId);
            IoTMultiendInOneBridge.getInstance().localControlTranslation(strCoverIotIdToDevId, JSON.toJSONString(map3), new IResponseCallback() { // from class: com.alibaba.ailabs.iot.mesh.TgMeshManager.21
                @Override // com.alibaba.android.multiendinonebridge.IResponseCallback
                public void onResponse(int i2, String str5) {
                    u.a aVarC;
                    a.a.a.a.b.m.a.a(TgMeshManager.TAG, "localControlTranslation code: " + i2 + ", result: " + str5);
                    if (i2 == 0 && TgMeshManager.this.mBinder != null) {
                        JSONObject object = JSON.parseObject(str5);
                        if (object.getInteger("code").intValue() == 200) {
                            List javaList = object.getJSONArray("data").toJavaList(Sigmesh.class);
                            if (!ListUtils.isEmpty(javaList) && javaList.get(0) != null) {
                                Sigmesh sigmesh = (Sigmesh) javaList.get(0);
                                if (sigmesh.getDevice() != null && sigmesh.getAction() != null) {
                                    int destAddr = sigmesh.getDevice().getDestAddr();
                                    int appKeyIndex = sigmesh.getDevice().getAppKeyIndex();
                                    int netKeyIndex = sigmesh.getDevice().getNetKeyIndex();
                                    if (sigmesh.getAction().getOpcode() != null && sigmesh.getAction().getParameters() != null) {
                                        byte[] opCodeBytes = Utils.getOpCodeBytes(Integer.parseInt(sigmesh.getAction().getOpcode(), 16));
                                        if (a.a.a.a.b.d.a.f1316b && (aVarC = G.a().d().c(strCoverIotIdToDevId)) != null && aVarC.i()) {
                                            TgMeshManager.this.offerBizRequest(SIGMeshBizRequestGenerator.a(destAddr, MeshParserUtils.bytesToHex(opCodeBytes, false), sigmesh.getAction().getParameters(), (IActionListener<Boolean>) iActionListener));
                                            return;
                                        } else {
                                            TgMeshManager.this.mBinder.a(G.a().d().b(strCoverIotIdToDevId), appKeyIndex, netKeyIndex, Utils.byteArray2Int(opCodeBytes), MeshParserUtils.toByteArray(sigmesh.getAction().getParameters()), iActionListener2, false);
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    TgMeshManager.this.deviceControlByServer(str, jSONObject.toJSONString(), iActionListener2, utTraceInfoA);
                }
            });
            return;
        }
        deviceControlByServer(str, jSONObject.toJSONString(), iActionListener2, utTraceInfoA);
    }
}
