package com.aliyun.alink.business.devicecenter.provision.other;

import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.channel.http.TransitoryClient;
import com.aliyun.alink.business.devicecenter.config.model.DeviceReportTokenType;
import com.aliyun.alink.business.devicecenter.discover.CloudEnrolleeDeviceModel;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.log.PerformanceLog;
import com.aliyun.alink.business.devicecenter.provision.other.zero.BatchZeroConfigStrategy;
import com.aliyun.alink.business.devicecenter.track.DCUserTrack;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: compiled from: BatchZeroConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class s implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BatchZeroConfigStrategy f3748a;

    public s(BatchZeroConfigStrategy batchZeroConfigStrategy) {
        this.f3748a = batchZeroConfigStrategy;
    }

    public final void a() {
        ALog.d(BatchZeroConfigStrategy.TAG, "startConfig requestEnrollee data=" + this.f3748a.mConfigParams);
        IoTRequestBuilder ioTRequestBuilderAddParam = new IoTRequestBuilder().setApiVersion("1.0.0").setPath(AlinkConstants.HTTP_PATH_ENROLLEES_CONNECT).setScheme(Scheme.HTTPS).setAuthType(AlinkConstants.KEY_IOT_AUTH).addParam("token", this.f3748a.mConfigParams.bindToken).addParam("regDeviceName", this.f3748a.mConfigParams.regDeviceName).addParam("regProductKey", this.f3748a.mConfigParams.regProductKey);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.f3748a.batchEnrolleeDeviceList.size(); i++) {
            CloudEnrolleeDeviceModel cloudEnrolleeDeviceModel = (CloudEnrolleeDeviceModel) this.f3748a.batchEnrolleeDeviceList.get(i);
            if (cloudEnrolleeDeviceModel != null) {
                HashMap map = new HashMap();
                map.put("enrolleeProductKey", cloudEnrolleeDeviceModel.enrolleeProductKey);
                map.put("enrolleeDeviceName", cloudEnrolleeDeviceModel.enrolleeDeviceName);
                arrayList.add(map);
            }
        }
        ioTRequestBuilderAddParam.addParam("enrolleeDeviceList", (List) arrayList);
        IoTRequest ioTRequestBuild = ioTRequestBuilderAddParam.build();
        PerformanceLog.trace(BatchZeroConfigStrategy.TAG, "reqEnrollee");
        BatchZeroConfigStrategy batchZeroConfigStrategy = this.f3748a;
        batchZeroConfigStrategy.cancelRequest(batchZeroConfigStrategy.retryTransitoryClient);
        this.f3748a.retryTransitoryClient = TransitoryClient.getInstance().asynRequest(ioTRequestBuild, new C0493r(this));
    }

    @Override // java.lang.Runnable
    public void run() {
        DCUserTrack.addTrackData(AlinkConstants.KEY_START_TIME_REQUEST_ENROLLEE, String.valueOf(System.currentTimeMillis()));
        if (this.f3748a.provisionErrorInfo != null) {
            this.f3748a.provisionErrorInfo.setSubcode(DCErrorCode.SUBCODE_PT_GET_CIPHER_TIMEOUT).setMsg("getCipherTimeout");
        }
        this.f3748a.deviceReportTokenType = DeviceReportTokenType.APP_TOKEN;
        a();
    }
}
