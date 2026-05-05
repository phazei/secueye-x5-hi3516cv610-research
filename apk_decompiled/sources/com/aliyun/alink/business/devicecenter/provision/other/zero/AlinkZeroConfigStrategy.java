package com.aliyun.alink.business.devicecenter.provision.other.zero;

import android.content.Context;
import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.api.add.LinkType;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.config.BaseProvisionStrategy;
import com.aliyun.alink.business.devicecenter.config.IConfigCallback;
import com.aliyun.alink.business.devicecenter.config.IConfigExtraCallback;
import com.aliyun.alink.business.devicecenter.config.IConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.annotation.ConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.model.DCAlibabaConfigParams;
import com.aliyun.alink.business.devicecenter.config.model.DCConfigParams;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.other.n;
import com.aliyun.alink.business.devicecenter.provision.other.p;
import com.aliyun.alink.business.devicecenter.track.DCUserTrack;
import com.aliyun.alink.business.devicecenter.utils.ThreadPool;
import java.util.Map;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes2.dex */
@ConfigStrategy(linkType = LinkType.ALI_ZERO_AP)
public class AlinkZeroConfigStrategy extends BaseProvisionStrategy implements IConfigStrategy {
    public static String TAG = "AlinkZeroConfigStrategy";
    public Future futureTask = null;

    public AlinkZeroConfigStrategy() {
    }

    private void resetFutureTask() {
        Future future = this.futureTask;
        if (future != null && !future.isDone()) {
            this.futureTask.cancel(true);
        }
        this.futureTask = null;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void continueConfig(Map map) {
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void doExtraPrepareWork(IConfigExtraCallback iConfigExtraCallback) {
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public String getProvisionType() {
        return LinkType.ALI_ZERO_AP.getName();
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
        ALog.d(TAG, "startConfig");
        resetFutureTask();
        this.mConfigCallback = iConfigCallback;
        if (!(dCConfigParams instanceof DCAlibabaConfigParams)) {
            ALog.w(TAG, "startConfig params error.");
            this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR);
            this.provisionErrorInfo.setMsg("configParams error").setSubcode(DCErrorCode.SUBCODE_PE_PROVISION_PARAMS_ERROR);
            provisionResultCallback(null);
            return;
        }
        this.mConfigParams = (DCAlibabaConfigParams) dCConfigParams;
        DCUserTrack.addTrackData(AlinkConstants.KEY_PROVISION_STARTED, "true");
        if (TextUtils.isEmpty(this.mConfigParams.regProductKey) || TextUtils.isEmpty(this.mConfigParams.regDeviceName)) {
            ALog.e(TAG, "startAddDevice, linkType zero with empty rpk or rdn.");
            this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR);
            this.provisionErrorInfo.setMsg("ZeroWithRPkOrDnNull").setSubcode(DCErrorCode.SUBCODE_PE_PROVISION_PARAMS_ERROR);
            provisionResultCallback(null);
            return;
        }
        if (TextUtils.isEmpty(this.mConfigParams.productKey)) {
            this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setMsg("productKey=empty").setSubcode(DCErrorCode.SUBCODE_PE_PRODUCTKEY_EMPTY);
            provisionResultCallback(null);
        } else {
            this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PROVISION_TIMEOUT_MSG, DCErrorCode.PF_PROVISION_TIMEOUT);
            startProvisionTimer();
            addProvisionOverListener(new n(this));
            this.futureTask = ThreadPool.submit(new p(this));
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void stopConfig() {
        ALog.d(TAG, "stopConfig");
        cancelRequest(this.retryTransitoryClient);
        removeProvisionOverListener();
        resetFutureTask();
        this.provisionErrorInfo = null;
        stopProvisionTimer();
        stopBackupCheck();
    }

    public AlinkZeroConfigStrategy(Context context) {
    }
}
