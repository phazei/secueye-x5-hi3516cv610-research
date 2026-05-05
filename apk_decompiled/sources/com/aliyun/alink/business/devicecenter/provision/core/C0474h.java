package com.aliyun.alink.business.devicecenter.provision.core;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.base.DCEnvHelper;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.biz.model.GetBindTokenMtopResponse;
import com.aliyun.alink.business.devicecenter.channel.http.DCError;
import com.aliyun.alink.business.devicecenter.channel.http.IRequestCallback;
import com.aliyun.alink.business.devicecenter.config.model.DeviceReportTokenType;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.ble.BreezeConfigStrategy;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.h, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BreezeConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class C0474h implements IRequestCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BreezeConfigStrategy f3699a;

    public C0474h(BreezeConfigStrategy breezeConfigStrategy) {
        this.f3699a = breezeConfigStrategy;
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.http.IRequestCallback
    public void onFail(DCError dCError, Object obj) {
        if (!this.f3699a.provisionHasStopped.get() && this.f3699a.needRetryGetCloudTokenAB.get()) {
            if (!DCEnvHelper.isTgEnv()) {
                if (DCEnvHelper.isILopEnv()) {
                    this.f3699a.connectBreDevice();
                    return;
                }
                return;
            }
            ALog.w(BreezeConfigStrategy.TAG, "getCloudProvisionToken onFail dcError=" + dCError + ", response=" + obj);
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.http.IRequestCallback
    public void onSuccess(Object obj) {
        ALog.d(BreezeConfigStrategy.TAG, "getCloudToken requestCallback onSuccess() called with: data = [" + obj + "]");
        if (!this.f3699a.provisionHasStopped.get() && this.f3699a.needRetryGetCloudTokenAB.get()) {
            if (!DCEnvHelper.isTgEnv()) {
                if (DCEnvHelper.isILopEnv()) {
                    if (!(obj instanceof IoTResponse)) {
                        ALog.w(BreezeConfigStrategy.TAG, "getCloudProvisionToken sth wrong with apiclient & mtop dep.");
                        this.f3699a.connectBreDevice();
                        return;
                    }
                    IoTResponse ioTResponse = (IoTResponse) obj;
                    if (ioTResponse == null || ioTResponse.getCode() != 200) {
                        ALog.w(BreezeConfigStrategy.TAG, "getCloudProvisionToken onResponse response error.");
                        this.f3699a.connectBreDevice();
                        return;
                    }
                    if (ioTResponse.getData() == null) {
                        ALog.w(BreezeConfigStrategy.TAG, "getCloudProvisionToken onResponse data null.");
                        this.f3699a.connectBreDevice();
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(ioTResponse.getData().toString());
                    if (!TextUtils.isEmpty(object.getString("token"))) {
                        this.f3699a.sendAppToken2DeviceAB.set(false);
                        this.f3699a.deviceReportTokenType = DeviceReportTokenType.UNKNOWN;
                        this.f3699a.mConfigParams.bindToken = object.getString("token");
                        this.f3699a.connectBreDevice();
                        return;
                    }
                    ALog.w(BreezeConfigStrategy.TAG, "getCloudProvisionToken onResponse token null.");
                    if (!this.f3699a.mConfigParams.isInSide) {
                        this.f3699a.connectBreDevice();
                        return;
                    }
                    ALog.w(BreezeConfigStrategy.TAG, "getCloudProvisionToken onResponse token null. :inside");
                    this.f3699a.provisionErrorInfo = new DCErrorCode(DCErrorCode.SERVER_ERROR_MSG, DCErrorCode.PF_DEVICE_FAIL).setMsg("combo+inside get cloud token error").setSubcode(DCErrorCode.SUBCODE_DF_BLE_NO_CONNECTAP_NOTIFY_AND_CHECK_TOKEN_FAIL);
                    this.f3699a.provisionResultCallback(null);
                    this.f3699a.stopConfig();
                    return;
                }
                return;
            }
            if (!(obj instanceof GetBindTokenMtopResponse)) {
                this.f3699a.provisionErrorInfo = new DCErrorCode("UserFail", DCErrorCode.PF_USER_FAIL).setMsg("sth wrong with mtop & apiclient dep.").setSubcode(DCErrorCode.SUBCODE_APICLIENT_AND_MTOP_DEP_ERROR);
                this.f3699a.provisionResultCallback(null);
                this.f3699a.stopConfig();
                return;
            }
            GetBindTokenMtopResponse getBindTokenMtopResponse = (GetBindTokenMtopResponse) obj;
            if (getBindTokenMtopResponse.m35getData() == null) {
                ALog.w(BreezeConfigStrategy.TAG, "getCloudProvisionToken onResponse response error.");
                this.f3699a.provisionErrorInfo = new DCErrorCode(DCErrorCode.SERVER_ERROR_MSG, DCErrorCode.PF_SERVER_FAIL).setMsg("getCloudProvisionToken failed. data=null.").setSubcode(DCErrorCode.SUBCODE_SRE_RESPONSE_FAIL);
                this.f3699a.provisionResultCallback(null);
                this.f3699a.stopConfig();
                return;
            }
            if (!getBindTokenMtopResponse.m35getData().isSuccess()) {
                this.f3699a.provisionErrorInfo = new DCErrorCode(DCErrorCode.SERVER_ERROR_MSG, DCErrorCode.PF_SERVER_FAIL).setMsg("getCloudProvisionToken. success=false." + getBindTokenMtopResponse.m35getData().getMsgInfo()).setSubcodeStr(getBindTokenMtopResponse.m35getData().getMsgCode());
                this.f3699a.provisionResultCallback(null);
                this.f3699a.stopConfig();
                return;
            }
            if (getBindTokenMtopResponse.m35getData().getModel() == null) {
                ALog.w(BreezeConfigStrategy.TAG, "getCloudProvisionToken success, but model is empty.");
                this.f3699a.provisionErrorInfo = new DCErrorCode(DCErrorCode.SERVER_ERROR_MSG, DCErrorCode.PF_SERVER_FAIL).setMsg("getCloudProvisionToken failed. model is empty.").setSubcode(DCErrorCode.SUBCODE_SRE_RESPONSE_FAIL);
                this.f3699a.provisionResultCallback(null);
                this.f3699a.stopConfig();
                return;
            }
            ALog.d(BreezeConfigStrategy.TAG, "getCloudProvisionToken success, to connect ble device.");
            this.f3699a.sendAppToken2DeviceAB.set(false);
            this.f3699a.deviceReportTokenType = DeviceReportTokenType.UNKNOWN;
            this.f3699a.mConfigParams.bindToken = getBindTokenMtopResponse.m35getData().getModel().getToken();
            this.f3699a.connectBreDevice();
        }
    }
}
