package com.aliyun.alink.business.devicecenter.provision.core.mesh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.api.add.DeviceBindResultInfo;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.add.LinkType;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.biz.ProvisionRepository;
import com.aliyun.alink.business.devicecenter.config.BaseProvisionStrategy;
import com.aliyun.alink.business.devicecenter.config.IConfigCallback;
import com.aliyun.alink.business.devicecenter.config.IConfigExtraCallback;
import com.aliyun.alink.business.devicecenter.config.IConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.annotation.ConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.model.DCAlibabaConfigParams;
import com.aliyun.alink.business.devicecenter.config.model.DCConfigParams;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.P;
import com.aliyun.alink.business.devicecenter.provision.core.Q;
import com.aliyun.alink.business.devicecenter.provision.core.S;
import com.aliyun.alink.business.devicecenter.provision.core.U;
import com.aliyun.alink.business.devicecenter.track.DCUserTrack;
import com.aliyun.alink.business.devicecenter.utils.AppUtils;
import com.aliyun.alink.business.devicecenter.utils.NetworkTypeUtils;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;
import com.aliyun.alink.business.devicecenter.utils.ThreadPool;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes2.dex */
@ConfigStrategy(linkType = LinkType.ALI_GATEWAY_MESH)
public class GatewayMeshStrategy extends BaseProvisionStrategy implements IConfigStrategy {
    public static String TAG = "GatewayMeshStrategy";
    public a mBindReceiver;
    public Context mContext;
    public Future futureTask = null;
    public String cloudMeshProvisionState = null;
    public String localMeshProvisionState = null;
    public boolean registerReceiver = false;

    private class a extends BroadcastReceiver {
        public a() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            JSONObject object = JSONObject.parseObject(intent.getStringExtra("bindData"));
            if (object == null || GatewayMeshStrategy.this.mConfigParams == null) {
                ALog.d(GatewayMeshStrategy.TAG, "onReceive: data is null return ");
                return;
            }
            boolean z = object.getIntValue("owned") == 1;
            String string = object.getString("gatewayIotId");
            String string2 = object.getString("iotId");
            String string3 = object.getString("deviceName");
            String string4 = object.getString("productKey");
            String string5 = object.getString(AlinkConstants.KEY_PAGE_ROUTER_URL);
            String string6 = object.getString("code");
            int intFromString = StringUtils.getIntFromString(string6);
            String string7 = object.getString(AlinkConstants.KEY_LOCALIZED_MSG);
            if (!TextUtils.equals(string3, GatewayMeshStrategy.this.mConfigParams.deviceName)) {
                ALog.d(GatewayMeshStrategy.TAG, "onReceive: deviceName is wrong return ");
                return;
            }
            ALog.d(GatewayMeshStrategy.TAG, "mesh device provisioning state=FINISH, isSuccess=" + z);
            GatewayMeshStrategy.this.cloudMeshProvisionState = "FINISH";
            if (z) {
                if (GatewayMeshStrategy.this.mConfigParams == null || GatewayMeshStrategy.this.provisionHasStopped.get()) {
                    ALog.d(GatewayMeshStrategy.TAG, "provision has stopped, return.");
                    return;
                }
                if (!TextUtils.isEmpty(string4)) {
                    GatewayMeshStrategy.this.mConfigParams.productKey = string4;
                }
                if (!TextUtils.isEmpty(string3)) {
                    GatewayMeshStrategy.this.mConfigParams.deviceName = string3;
                }
                if (GatewayMeshStrategy.this.mNotifyListner != null) {
                    DeviceInfo deviceInfo = new DeviceInfo();
                    deviceInfo.regIotId = string;
                    deviceInfo.productKey = GatewayMeshStrategy.this.mConfigParams.productKey;
                    deviceInfo.deviceName = GatewayMeshStrategy.this.mConfigParams.deviceName;
                    deviceInfo.bindResultInfo = new DeviceBindResultInfo();
                    deviceInfo.bindResultInfo.productKey = GatewayMeshStrategy.this.mConfigParams.productKey;
                    deviceInfo.bindResultInfo.deviceName = GatewayMeshStrategy.this.mConfigParams.deviceName;
                    DeviceBindResultInfo deviceBindResultInfo = deviceInfo.bindResultInfo;
                    deviceBindResultInfo.iotId = string2;
                    deviceBindResultInfo.bindResult = 1;
                    deviceBindResultInfo.pageRouterUrl = string5;
                    deviceBindResultInfo.iotId = string2;
                    deviceBindResultInfo.errorCode = string6;
                    deviceBindResultInfo.localizedMsg = string7;
                    ALog.d(GatewayMeshStrategy.TAG, "push bind device success");
                    GatewayMeshStrategy.this.mNotifyListner.onDeviceFound(deviceInfo);
                    return;
                }
            }
            if (z) {
                return;
            }
            GatewayMeshStrategy gatewayMeshStrategy = GatewayMeshStrategy.this;
            DCErrorCode msg = new DCErrorCode(DCErrorCode.SERVER_ERROR_MSG, DCErrorCode.PF_SERVER_FAIL).setMsg("mesh/gateway/discovery/device/query provision failed state=FINISH, msg=" + string7);
            if (intFromString == 0) {
                intFromString = DCErrorCode.SUBCODE_SRE_GET_MESH_PROVISION_RESULT_BIZ_FAIL;
            }
            gatewayMeshStrategy.provisionErrorInfo = msg.setSubcode(intFromString);
            GatewayMeshStrategy.this.provisionResultCallback(null);
        }

        public /* synthetic */ a(GatewayMeshStrategy gatewayMeshStrategy, P p) {
            this();
        }
    }

    public GatewayMeshStrategy() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getProvisionTimeoutErrorInfo() {
        this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PROVISION_TIMEOUT_MSG, DCErrorCode.PF_PROVISION_TIMEOUT);
        String str = "[mobile=" + NetworkTypeUtils.isMobileNetwork(this.mContext) + ", WiFi=" + NetworkTypeUtils.isWiFi(this.mContext) + ", state= " + this.cloudMeshProvisionState + ", localMeshProvisionState=" + this.localMeshProvisionState + "].";
        if (NetworkTypeUtils.isMobileNetwork(this.mContext)) {
            this.provisionErrorInfo.setSubcode(DCErrorCode.SUBCODE_NE_NETWORK_UNAVAILABLE).setMsg("provision timeout, network unavailable " + str);
            return;
        }
        this.provisionErrorInfo.setSubcode(DCErrorCode.SUBCODE_PT_DEVICE_PROVISION_FAIL_OR_TIMEOUT).setMsg("provision timeout, device not success until now,  " + str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loopQueryMeshProvisionResult(String str) {
        resetFutureTask();
        if (this.provisionHasStopped.get()) {
            return;
        }
        this.localMeshProvisionState = "queryProvisionResult";
        this.futureTask = ThreadPool.scheduleAtFixedRate(new U(this, str), 0L, 3L, TimeUnit.SECONDS);
    }

    private void registerReceiver() {
        try {
            if (this.registerReceiver || this.mBindReceiver != null) {
                return;
            }
            this.mBindReceiver = new a(this, null);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("iLop.bind.cloud.mesh");
            AppUtils.getContext().registerReceiver(this.mBindReceiver, intentFilter);
            this.registerReceiver = true;
        } catch (Exception unused) {
        }
    }

    private void resetFutureTask() {
        Future future = this.futureTask;
        if (future != null && !future.isDone()) {
            this.futureTask.cancel(true);
        }
        this.futureTask = null;
    }

    private void triggerGateway2Provision() {
        DCAlibabaConfigParams dCAlibabaConfigParams = this.mConfigParams;
        if (dCAlibabaConfigParams == null) {
            ALog.w(TAG, "provision has stopped, return.");
        } else {
            this.localMeshProvisionState = "trigger2Provision";
            ProvisionRepository.meshDeviceProvisionTrigger(dCAlibabaConfigParams.regIoTId, dCAlibabaConfigParams.deviceId, new S(this));
        }
    }

    private void unregisterReceiver() {
        try {
            if (this.mBindReceiver != null) {
                AppUtils.getContext().unregisterReceiver(this.mBindReceiver);
                this.registerReceiver = false;
                this.mBindReceiver = null;
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void continueConfig(Map map) {
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void doExtraPrepareWork(IConfigExtraCallback iConfigExtraCallback) {
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public String getProvisionType() {
        return LinkType.ALI_GATEWAY_MESH.getName();
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public boolean hasExtraPrepareWork() {
        return false;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public boolean isSupport() {
        return true;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public boolean needWiFiSsidPwd() {
        return false;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void preConfig(IConfigCallback iConfigCallback, DCConfigParams dCConfigParams) {
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void startConfig(IConfigCallback iConfigCallback, DCConfigParams dCConfigParams) throws Exception {
        this.mConfigCallback = iConfigCallback;
        registerReceiver();
        if (!(dCConfigParams instanceof DCAlibabaConfigParams)) {
            ALog.w(TAG, "startConfig params error.");
            this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR);
            this.provisionErrorInfo.setMsg("configParams error:").setSubcode(DCErrorCode.SUBCODE_PE_PROVISION_PARAMS_ERROR);
            provisionResultCallback(null);
            return;
        }
        this.mConfigParams = (DCAlibabaConfigParams) dCConfigParams;
        if (TextUtils.isEmpty(this.mConfigParams.iotId) || TextUtils.isEmpty(this.mConfigParams.deviceId)) {
            this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setMsg("iotId or regIotID is empty").setSubcode(DCErrorCode.SUBCODE_PE_PROVISION_PARAMS_ERROR);
            provisionResultCallback(null);
            return;
        }
        DCUserTrack.addTrackData(AlinkConstants.KEY_PROVISION_STARTED, "true");
        this.mConfigParams.timeout = 30;
        startProvisionTimer(new P(this));
        addProvisionOverListener(new Q(this));
        triggerGateway2Provision();
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void stopConfig() {
        this.provisionHasStopped.set(true);
        this.mConfigParams = null;
        this.cloudMeshProvisionState = null;
        this.localMeshProvisionState = null;
        resetFutureTask();
        stopProvisionTimer();
        unregisterReceiver();
    }

    public GatewayMeshStrategy(Context context) {
        this.mContext = context;
    }
}
