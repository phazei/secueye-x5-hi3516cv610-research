package com.aliyun.iot.aep.sdk.credential.listener;

import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.credential.data.CompanyData;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface OnReqCompanyCallback {
    void onFailure(IoTRequest ioTRequest, Exception exc);

    void onSuccess(int i, List<CompanyData> list);
}
