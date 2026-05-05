package com.aliyun.alink.business.devicecenter.channel.http;

import com.alibaba.ailabs.tg.network.Call;
import com.alibaba.ailabs.tg.network.annotation.CommonParameters;
import com.alibaba.ailabs.tg.network.annotation.Parameters;
import com.alibaba.ailabs.tg.network.annotation.Request;
import com.aliyun.alink.business.devicecenter.biz.model.CheckBindTokenMtopResponse;
import com.aliyun.alink.business.devicecenter.biz.model.CheckBindTokenRequest;
import com.aliyun.alink.business.devicecenter.biz.model.GetBindTokenMtopResponse;
import com.aliyun.alink.business.devicecenter.biz.model.GetBindTokenRequest;

/* JADX INFO: loaded from: classes.dex */
public interface IMtopRequestService {
    @CommonParameters({"authInfo"})
    @Request({CheckBindTokenRequest.class, CheckBindTokenMtopResponse.class})
    @Parameters({"productKey", "deviceName", "token"})
    Call<CheckBindTokenMtopResponse.Data> checkBindToken(String str, String str2, String str3);

    @CommonParameters({"authInfo"})
    @Request({GetBindTokenRequest.class, GetBindTokenMtopResponse.class})
    @Parameters({"bssid"})
    Call<GetBindTokenMtopResponse.Data> getBindToken(String str);
}
