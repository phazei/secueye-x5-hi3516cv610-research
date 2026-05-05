package com.aliyun.alink.business.devicecenter.discover.cloud;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.discovery.DiscoveryType;
import com.aliyun.alink.business.devicecenter.api.discovery.IDeviceDiscoveryListener;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.cache.CacheCenter;
import com.aliyun.alink.business.devicecenter.cache.CacheType;
import com.aliyun.alink.business.devicecenter.channel.http.CloudResponse;
import com.aliyun.alink.business.devicecenter.channel.http.TransitoryClient;
import com.aliyun.alink.business.devicecenter.config.DeviceCenterBiz;
import com.aliyun.alink.business.devicecenter.discover.CloudEnrolleeDeviceHelper;
import com.aliyun.alink.business.devicecenter.discover.CloudEnrolleeDeviceWrapper;
import com.aliyun.alink.business.devicecenter.discover.annotation.DeviceDiscovery;
import com.aliyun.alink.business.devicecenter.discover.base.DiscoverChainBase;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.utils.ThreadPool;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
@DeviceDiscovery(discoveryType = {DiscoveryType.CLOUD_ENROLLEE_DEVICE})
public class CloudDiscoverChain extends DiscoverChainBase {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public ScheduledFuture f3603c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public Map<String, Object> f3604d;
    public boolean e;

    public CloudDiscoverChain(Context context, Map<String, Object> map) {
        super(context);
        this.f3603c = null;
        this.f3604d = null;
        this.e = false;
        this.f3604d = map;
        if (this.f3604d == null) {
            this.f3604d = new HashMap();
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.discover.base.AbilityReceiver
    public void onNotify(Intent intent) {
    }

    public final void resetFutureTask() {
        ScheduledFuture scheduledFuture = this.f3603c;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            this.f3603c = null;
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.discover.IDiscoverChain
    public void startDiscover(final IDeviceDiscoveryListener iDeviceDiscoveryListener) {
        ALog.d("CloudDiscoverChain", "startDiscover");
        resetFutureTask();
        this.f3603c = ThreadPool.scheduleAtFixedRate(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.discover.cloud.CloudDiscoverChain.1
            @Override // java.lang.Runnable
            public void run() {
                TransitoryClient.getInstance().asynRequest(new IoTRequestBuilder().setPath(AlinkConstants.HTTP_PATH_CLOUD_ENROLLEE_DISCOVER).setScheme(Scheme.HTTPS).setApiVersion("1.0.7").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(CloudDiscoverChain.this.f3604d).addParam(AlinkConstants.KEY_PAGE_SIZE, 100).addParam("pageNum", 1).build(), new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.discover.cloud.CloudDiscoverChain.1.1
                    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                    public void onFailure(IoTRequest ioTRequest, Exception exc) {
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                    public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                        CloudResponse cloudResponse;
                        if (ioTResponse != null) {
                            try {
                                if (ioTResponse.getCode() == 200) {
                                    String str = new String(ioTResponse.getRawData());
                                    if (TextUtils.isEmpty(str) || (cloudResponse = (CloudResponse) JSONObject.parseObject(str, new TypeReference<CloudResponse<CloudEnrolleeDeviceWrapper>>() { // from class: com.aliyun.alink.business.devicecenter.discover.cloud.CloudDiscoverChain.1.1.1
                                    }.getType(), new Feature[0])) == null || cloudResponse.data == 0 || ((CloudEnrolleeDeviceWrapper) cloudResponse.data).items == null) {
                                        return;
                                    }
                                    if (CloudDiscoverChain.this.e) {
                                        CacheCenter.getInstance().updateCache(CacheType.BATCH_CLOUD_ENROLLEE, (List) CloudEnrolleeDeviceHelper.getFilteredEnrolleeDevices(((CloudEnrolleeDeviceWrapper) cloudResponse.data).items, CloudDiscoverChain.this.e));
                                    } else {
                                        CacheCenter.getInstance().updateCache(CacheType.CLOUD_ENROLLEE, (List) CloudEnrolleeDeviceHelper.getFilteredEnrolleeDevices(((CloudEnrolleeDeviceWrapper) cloudResponse.data).items, CloudDiscoverChain.this.e));
                                    }
                                    a(CloudEnrolleeDeviceHelper.convertCloudEnrolleeDevice(((CloudEnrolleeDeviceWrapper) cloudResponse.data).items, CloudDiscoverChain.this.e));
                                    return;
                                }
                            } catch (Exception e) {
                                ALog.w("CloudDiscoverChain", "startDiscover getEnrolleeList onResponse parse exception=" + e);
                                return;
                            }
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("getEnrollee device list failed.request=");
                        sb.append(TransitoryClient.getInstance().requestToStr(ioTRequest));
                        sb.append(",response=");
                        sb.append(TransitoryClient.getInstance().responseToStr(ioTResponse));
                        ALog.w("CloudDiscoverChain", sb.toString());
                    }
                });
            }

            public final void a(final List<DeviceInfo> list) {
                DeviceCenterBiz.getInstance().runOnUIThread(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.discover.cloud.CloudDiscoverChain.1.2
                    @Override // java.lang.Runnable
                    public void run() {
                        iDeviceDiscoveryListener.onDeviceFound(DiscoveryType.CLOUD_ENROLLEE_DEVICE, list);
                    }
                });
            }
        }, 0L, this.e ? 3L : 5L, TimeUnit.SECONDS);
    }

    @Override // com.aliyun.alink.business.devicecenter.discover.IDiscoverChain
    public void stopDiscover() {
        ALog.d("CloudDiscoverChain", "stopDiscover");
        resetFutureTask();
    }

    public CloudDiscoverChain(Context context, Map<String, Object> map, boolean z) {
        this(context, map);
        this.e = z;
    }
}
