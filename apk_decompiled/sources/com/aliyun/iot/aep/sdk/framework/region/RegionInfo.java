package com.aliyun.iot.aep.sdk.framework.region;

import android.text.TextUtils;
import com.aliyun.iot.aep.sdk.framework.network.BaseResponse;

/* JADX INFO: loaded from: classes2.dex */
public class RegionInfo extends BaseResponse {
    public String apiGatewayEndpoint;
    public String mqttEndpoint;
    public String oaApiGatewayEndpoint;
    public String pushChannelEndpoint;
    public String regionEnglishName;
    public String regionId;

    public boolean isEqual(RegionInfo regionInfo) {
        return regionInfo != null && TextUtils.equals(this.regionId, regionInfo.regionId) && TextUtils.equals(this.mqttEndpoint, regionInfo.mqttEndpoint) && TextUtils.equals(this.apiGatewayEndpoint, regionInfo.apiGatewayEndpoint) && TextUtils.equals(this.oaApiGatewayEndpoint, regionInfo.oaApiGatewayEndpoint) && TextUtils.equals(this.regionEnglishName, regionInfo.regionEnglishName) && TextUtils.equals(this.pushChannelEndpoint, regionInfo.pushChannelEndpoint);
    }
}
