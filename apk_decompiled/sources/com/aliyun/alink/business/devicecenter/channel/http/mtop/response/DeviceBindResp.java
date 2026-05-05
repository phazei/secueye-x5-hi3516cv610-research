package com.aliyun.alink.business.devicecenter.channel.http.mtop.response;

import com.aliyun.alink.business.devicecenter.channel.http.mtop.data.DeviceBindRespData;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes.dex */
public class DeviceBindResp extends BaseOutDo {
    public DeviceBindRespData data;

    public void setData(DeviceBindRespData deviceBindRespData) {
        this.data = deviceBindRespData;
    }

    /* JADX INFO: renamed from: getData, reason: merged with bridge method [inline-methods] */
    public DeviceBindRespData m37getData() {
        return this.data;
    }
}
