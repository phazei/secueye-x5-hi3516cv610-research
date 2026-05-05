package com.aliyun.alink.business.devicecenter.provision.other.zero;

import android.content.Context;
import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.api.add.LinkType;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.cache.CacheCenter;
import com.aliyun.alink.business.devicecenter.cache.CacheType;
import com.aliyun.alink.business.devicecenter.config.BaseProvisionStrategy;
import com.aliyun.alink.business.devicecenter.config.IConfigCallback;
import com.aliyun.alink.business.devicecenter.config.IConfigExtraCallback;
import com.aliyun.alink.business.devicecenter.config.IConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.annotation.ConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.model.DCAlibabaConfigParams;
import com.aliyun.alink.business.devicecenter.config.model.DCConfigParams;
import com.aliyun.alink.business.devicecenter.config.model.DeviceReportTokenType;
import com.aliyun.alink.business.devicecenter.discover.CloudEnrolleeDeviceModel;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.other.q;
import com.aliyun.alink.business.devicecenter.provision.other.s;
import com.aliyun.alink.business.devicecenter.track.DCUserTrack;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;
import com.aliyun.alink.business.devicecenter.utils.ThreadPool;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes2.dex */
@ConfigStrategy(linkType = LinkType.ALI_ZERO_IN_BATCHES)
public class BatchZeroConfigStrategy extends BaseProvisionStrategy implements IConfigStrategy {
    public static String TAG = "BatchZeroConfigStrategy";
    public Future futureTask = null;
    public List<CloudEnrolleeDeviceModel> batchEnrolleeDeviceList = null;
    public ConcurrentHashMap<String, Boolean> cacheCallbackMap = new ConcurrentHashMap<>();
    public DeviceReportTokenType deviceReportTokenType = DeviceReportTokenType.UNKNOWN;

    public BatchZeroConfigStrategy() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean batchDeviceSuccess(String str, String str2) {
        ALog.d(TAG, "batchDeviceSuccess() called with: pk = [" + str + "], dn = [" + str2 + "]");
        List<CloudEnrolleeDeviceModel> list = this.batchEnrolleeDeviceList;
        if (list != null && !list.isEmpty() && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            int size = this.batchEnrolleeDeviceList.size();
            for (int i = 0; i < size; i++) {
                CloudEnrolleeDeviceModel cloudEnrolleeDeviceModel = this.batchEnrolleeDeviceList.get(i);
                if (cloudEnrolleeDeviceModel != null && str.equals(cloudEnrolleeDeviceModel.enrolleeProductKey) && str2.equals(cloudEnrolleeDeviceModel.enrolleeDeviceName)) {
                    return true;
                }
            }
        }
        return false;
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
        return LinkType.ALI_ZERO_IN_BATCHES.getName();
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
        ALog.d(TAG, "startConfig() called with: callback = [" + iConfigCallback + "], configParams = [" + dCConfigParams + "]");
        resetFutureTask();
        this.cacheCallbackMap.clear();
        this.mConfigParams = (DCAlibabaConfigParams) dCConfigParams;
        this.mConfigCallback = iConfigCallback;
        if (TextUtils.isEmpty(this.mConfigParams.productKey) || TextUtils.isEmpty(this.mConfigParams.regProductKey) || TextUtils.isEmpty(this.mConfigParams.regDeviceName)) {
            this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setMsg("pk or regPk or regDn is empty").setSubcode(DCErrorCode.SUBCODE_PE_PRODUCTKEY_EMPTY);
            provisionResultCallback(null);
            return;
        }
        DCUserTrack.addTrackData(AlinkConstants.KEY_PROVISION_STARTED, "true");
        setCallbackOnce(false);
        CacheCenter cacheCenter = CacheCenter.getInstance();
        CacheType cacheType = CacheType.BATCH_CLOUD_ENROLLEE;
        DCAlibabaConfigParams dCAlibabaConfigParams = this.mConfigParams;
        List<CloudEnrolleeDeviceModel> cachedModel = cacheCenter.getCachedModel(cacheType, dCAlibabaConfigParams.productKey, null, dCAlibabaConfigParams.regProductKey, dCAlibabaConfigParams.regDeviceName);
        if (cachedModel == null || cachedModel.isEmpty() || !(cachedModel.get(0) instanceof CloudEnrolleeDeviceModel)) {
            this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setMsg("batch zero provision device list is empty.").setSubcode(DCErrorCode.SUBCODE_PE_BATCH_TO_PROVISION_DEVICE_EMPTY);
            provisionResultCallback(null);
            return;
        }
        this.batchEnrolleeDeviceList = cachedModel;
        ALog.d(TAG, " batchEnrolleeDeviceList=" + this.batchEnrolleeDeviceList);
        this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PROVISION_TIMEOUT_MSG, DCErrorCode.PF_PROVISION_TIMEOUT);
        startProvisionTimer();
        this.mConfigParams.bindToken = StringUtils.getHexString(32);
        addProvisionOverListener(new q(this));
        this.futureTask = ThreadPool.submit(new s(this));
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

    public BatchZeroConfigStrategy(Context context) {
    }
}
