package com.aliyun.alink.business.devicecenter.discover.coap;

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
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.base.LocalDevice;
import com.aliyun.alink.business.devicecenter.channel.coap.CoAPClient;
import com.aliyun.alink.business.devicecenter.channel.coap.request.CoapRequestPayload;
import com.aliyun.alink.business.devicecenter.channel.coap.response.CoapResponsePayload;
import com.aliyun.alink.business.devicecenter.config.DeviceCenterBiz;
import com.aliyun.alink.business.devicecenter.config.IDeviceInfoNotifyListener;
import com.aliyun.alink.business.devicecenter.discover.CoAPDeviceInfoNotifyHandler;
import com.aliyun.alink.business.devicecenter.discover.annotation.DeviceDiscovery;
import com.aliyun.alink.business.devicecenter.discover.base.DiscoverChainBase;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.utils.ThreadPool;
import com.aliyun.alink.business.devicecenter.utils.WifiManagerUtil;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPConstant;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPContext;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPRequest;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPResponse;
import com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPReqHandler;
import com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPResHandler;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
@DeviceDiscovery(discoveryType = {DiscoveryType.LOCAL_ONLINE_DEVICE})
public class CoAPDiscoverChain extends DiscoverChainBase {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public Future f3611c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public AlcsCoAPRequest f3612d;
    public IAlcsCoAPResHandler e;
    public WifiManagerUtil f;
    public long g;
    public boolean h;
    public AtomicBoolean i;
    public int j;

    public CoAPDiscoverChain(Context context) {
        super(context);
        this.f3611c = null;
        this.f3612d = null;
        this.e = null;
        this.g = -1L;
        this.h = false;
        this.i = new AtomicBoolean(false);
        this.j = 5;
        try {
            this.f = new WifiManagerUtil(DeviceCenterBiz.getInstance().getAppContext());
        } catch (Exception e) {
            ALog.w("CoAPDiscoverChain", "CoAPDiscoverChain wifiManagerUtil create exception=" + e);
            this.f = null;
        }
        this.h = false;
    }

    @Override // com.aliyun.alink.business.devicecenter.discover.base.AbilityReceiver
    public void onNotify(Intent intent) {
    }

    public void setPeriod(int i) {
        ALog.d("CoAPDiscoverChain", "setPeriod period=" + i);
        if (i < 2) {
            this.j = 2;
        } else {
            this.j = i;
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.discover.IDiscoverChain
    public void startDiscover(final IDeviceDiscoveryListener iDeviceDiscoveryListener) {
        ALog.d("CoAPDiscoverChain", "startDiscover call. listener=" + iDeviceDiscoveryListener);
        stopDiscover();
        WifiManagerUtil wifiManagerUtil = this.f;
        if (wifiManagerUtil != null) {
            wifiManagerUtil.acquireMulticastLock();
        }
        this.i.set(true);
        if (!this.h) {
            this.e = new CoAPDeviceInfoNotifyHandler(new IDeviceInfoNotifyListener() { // from class: com.aliyun.alink.business.devicecenter.discover.coap.CoAPDiscoverChain.1
                @Override // com.aliyun.alink.business.devicecenter.config.IDeviceInfoNotifyListener
                public void onDeviceFound(final DeviceInfo deviceInfo) {
                    if (deviceInfo == null || !CoAPDiscoverChain.this.i.get()) {
                        return;
                    }
                    ALog.d("CoAPDiscoverChain", "onDeviceFound discover found dev=" + deviceInfo);
                    DeviceCenterBiz.getInstance().runOnUIThread(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.discover.coap.CoAPDiscoverChain.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (iDeviceDiscoveryListener != null) {
                                ArrayList arrayList = new ArrayList();
                                arrayList.add(deviceInfo);
                                iDeviceDiscoveryListener.onDeviceFound(DiscoveryType.LOCAL_ONLINE_DEVICE, arrayList);
                            }
                        }
                    });
                }

                @Override // com.aliyun.alink.business.devicecenter.config.IDeviceInfoNotifyListener
                public void onFailure(DCErrorCode dCErrorCode) {
                }
            });
            CoAPClient.getInstance().addNotifyListener(this.e);
        }
        this.f3611c = ThreadPool.scheduleAtFixedRate(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.discover.coap.CoAPDiscoverChain.2
            @Override // java.lang.Runnable
            public void run() {
                if (CoAPDiscoverChain.this.i.get()) {
                    CoAPDiscoverChain.this.a(iDeviceDiscoveryListener);
                }
            }
        }, 0L, this.j, TimeUnit.SECONDS);
    }

    @Override // com.aliyun.alink.business.devicecenter.discover.IDiscoverChain
    public void stopDiscover() {
        ALog.d("CoAPDiscoverChain", "stopDiscover call.");
        CoAPClient.getInstance().removeNotifyListener(this.e);
        a(this.f3612d);
        this.h = false;
        WifiManagerUtil wifiManagerUtil = this.f;
        if (wifiManagerUtil != null) {
            wifiManagerUtil.releaseMulticastLock();
        }
        this.i.set(false);
        try {
            if (this.f3611c != null) {
                this.f3611c.cancel(true);
                this.f3611c = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void a(final IDeviceDiscoveryListener iDeviceDiscoveryListener) {
        ALog.d("CoAPDiscoverChain", "doSendCoAPDiscover() called with: listener = [" + iDeviceDiscoveryListener + "]");
        try {
            CoapRequestPayload coapRequestPayload = new CoapRequestPayload();
            coapRequestPayload.getClass();
            CoapRequestPayload coapRequestPayloadBuild = new CoapRequestPayload.Builder().version("1.0").params(new HashMap()).method(AlinkConstants.COAP_METHOD_GET_DEVICE_INFO).build();
            a(this.f3612d);
            this.f3612d = new AlcsCoAPRequest(AlcsCoAPConstant.Code.GET, AlcsCoAPConstant.Type.NON);
            InetAddress ipAddress = WifiManagerUtil.getIpAddress(WifiManagerUtil.NetworkType.WLAN);
            if (ipAddress == null) {
                ALog.w("CoAPDiscoverChain", "getIpAddress address=null.");
                try {
                    ipAddress = InetAddress.getByName(WifiManagerUtil.getWifiIP(DeviceCenterBiz.getInstance().getAppContext()));
                } catch (UnknownHostException e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("getWifiIP  getByName exception=");
                    sb.append(e);
                    ALog.w("CoAPDiscoverChain", sb.toString());
                }
            }
            InetAddress broadcast = null;
            if (ipAddress != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("address not null, ip=");
                sb2.append(ipAddress.getHostAddress());
                ALog.d("CoAPDiscoverChain", sb2.toString());
                try {
                    broadcast = WifiManagerUtil.getBroadcast(ipAddress);
                } catch (Exception e2) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("getBroadcast exception=");
                    sb3.append(e2);
                    ALog.w("CoAPDiscoverChain", sb3.toString());
                }
            }
            String hostAddress = broadcast == null ? "255.255.255.255" : broadcast.getHostAddress();
            StringBuilder sb4 = new StringBuilder();
            sb4.append(hostAddress);
            sb4.append(":");
            sb4.append(5683);
            sb4.append(AlinkConstants.COAP_PATH_GET_DEVICE_INFO);
            String string = sb4.toString();
            this.f3612d.setPayload(coapRequestPayloadBuild.toString());
            StringBuilder sb5 = new StringBuilder();
            sb5.append("setPayload=");
            sb5.append(coapRequestPayloadBuild.toString());
            sb5.append(",getPayload=");
            sb5.append(this.f3612d.getPayloadString());
            ALog.llog((byte) 3, "CoAPDiscoverChain", sb5.toString());
            this.f3612d.setMulticast(1);
            this.f3612d.setURI(string);
            StringBuilder sb6 = new StringBuilder();
            sb6.append("coapUri=");
            sb6.append(string);
            ALog.d("CoAPDiscoverChain", sb6.toString());
        } catch (Exception e3) {
            ALog.w("CoAPDiscoverChain", "pre sendRequest params exception=" + e3);
        }
        this.g = CoAPClient.getInstance().sendRequest(this.f3612d, new IAlcsCoAPReqHandler() { // from class: com.aliyun.alink.business.devicecenter.discover.coap.CoAPDiscoverChain.3
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPReqHandler
            public void onReqComplete(AlcsCoAPContext alcsCoAPContext, int i, AlcsCoAPResponse alcsCoAPResponse) {
                CoAPClient.getInstance().printResponse(alcsCoAPContext, alcsCoAPResponse);
                if (alcsCoAPResponse == null || TextUtils.isEmpty(alcsCoAPResponse.getPayloadString())) {
                    return;
                }
                ALog.llog((byte) 3, "CoAPDiscoverChain", "responseString=" + alcsCoAPResponse.getPayloadString());
                try {
                    CoapResponsePayload coapResponsePayload = (CoapResponsePayload) JSONObject.parseObject(alcsCoAPResponse.getPayloadString(), new TypeReference<CoapResponsePayload<LocalDevice>>() { // from class: com.aliyun.alink.business.devicecenter.discover.coap.CoAPDiscoverChain.3.1
                    }.getType(), new Feature[0]);
                    if (coapResponsePayload == null || coapResponsePayload.data == 0 || !CoAPDiscoverChain.this.i.get()) {
                        return;
                    }
                    final DeviceInfo deviceInfoConvertLocalDevice = DeviceInfo.convertLocalDevice((LocalDevice) coapResponsePayload.data);
                    DeviceCenterBiz.getInstance().runOnUIThread(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.discover.coap.CoAPDiscoverChain.3.2
                        @Override // java.lang.Runnable
                        public void run() {
                            if (iDeviceDiscoveryListener != null) {
                                ArrayList arrayList = new ArrayList();
                                arrayList.add(deviceInfoConvertLocalDevice);
                                iDeviceDiscoveryListener.onDeviceFound(DiscoveryType.LOCAL_ONLINE_DEVICE, arrayList);
                            }
                        }
                    });
                } catch (Exception e4) {
                    ALog.w("CoAPDiscoverChain", "startDiscovery device.info.get parsePayloadException= " + e4);
                }
            }
        });
    }

    public CoAPDiscoverChain(Context context, boolean z) {
        this(context);
        try {
            this.f = new WifiManagerUtil(DeviceCenterBiz.getInstance().getAppContext());
        } catch (Exception e) {
            ALog.w("CoAPDiscoverChain", "CoAPDiscoverChain wifiManagerUtil create exception=" + e);
            this.f = null;
        }
        this.h = z;
    }

    public final void a(AlcsCoAPRequest alcsCoAPRequest) {
        if (alcsCoAPRequest != null) {
            alcsCoAPRequest.cancel();
        }
        if (this.g != -1) {
            CoAPClient.getInstance().cancelMessage(this.g);
        }
    }
}
