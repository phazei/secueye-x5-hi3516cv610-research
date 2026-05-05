package com.aliyun.iot.aep.sdk.framework.region;

import android.text.TextUtils;
import com.aliyun.iot.aep.sdk.framework.network.BaseResponse;

/* JADX INFO: loaded from: classes2.dex */
public class RegionInfo2 extends BaseResponse {
    public String apiGatewayEndpoint;
    public String mqttEndpoint;
    public String oaApiGatewayEndpoint;
    public String pushChannelEndpoint;
    public String regionEnglishName;
    public String regionId;
    public String shortRegionId;

    public boolean isEqual(RegionInfo2 regionInfo2) {
        return regionInfo2 != null && TextUtils.equals(this.regionId, regionInfo2.regionId) && TextUtils.equals(this.mqttEndpoint, regionInfo2.mqttEndpoint) && TextUtils.equals(this.apiGatewayEndpoint, regionInfo2.apiGatewayEndpoint) && TextUtils.equals(this.oaApiGatewayEndpoint, regionInfo2.oaApiGatewayEndpoint) && TextUtils.equals(this.regionEnglishName, regionInfo2.regionEnglishName) && TextUtils.equals(this.shortRegionId, regionInfo2.shortRegionId) && TextUtils.equals(this.pushChannelEndpoint, regionInfo2.pushChannelEndpoint);
    }
}
