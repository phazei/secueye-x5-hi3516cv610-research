package com.aliyun.alink.linksdk.tmp.resource;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.devicemodel.DeviceModel;
import com.aliyun.alink.linksdk.tmp.devicemodel.Profile;
import com.aliyun.alink.linksdk.tmp.listener.ITResRequestHandler;
import com.aliyun.alink.linksdk.tmp.listener.ITResResponseCallback;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tmp.utils.WifiManagerUtil;
import com.aliyun.alink.linksdk.tools.ALog;
import java.net.InetAddress;
import java.util.HashMap;

/* JADX INFO: compiled from: DiscoveryResHander.java */
/* JADX INFO: loaded from: classes2.dex */
public class a implements ITResRequestHandler {
    private static final String e = "[Tmp]DiscoveryResHander";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected String f4410a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected String f4411b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected DeviceModel f4412c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected int f4413d;

    public a(String str, String str2, DeviceModel deviceModel) {
        a(str, str2, deviceModel);
    }

    public void a(String str, String str2, DeviceModel deviceModel) {
        this.f4410a = str;
        this.f4411b = str2;
        this.f4412c = deviceModel;
        if (deviceModel != null) {
            String json = GsonUtils.toJson(this.f4412c);
            if (TextUtils.isEmpty(json)) {
                return;
            }
            this.f4413d = json.length();
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.listener.ITResRequestHandler
    public void onProcess(String str, Object obj, ITResResponseCallback iTResResponseCallback) {
        Profile profile = new Profile();
        profile.setProdKey(this.f4410a);
        profile.setName(this.f4411b);
        profile.port = 5683;
        InetAddress ipAddress = WifiManagerUtil.getIpAddress(WifiManagerUtil.NetworkType.WLAN);
        profile.addr = ipAddress == null ? "" : ipAddress.getHostAddress();
        ValueWrapper valueWrapper = new ValueWrapper();
        ALog.d(e, "onProcess identifier mDeviceModelLength:" + this.f4413d + " mDeviceModel:" + this.f4412c);
        DeviceModel deviceModel = this.f4412c;
        if (deviceModel != null && this.f4413d <= 3072) {
            deviceModel.setProfile(profile);
            valueWrapper.setValue(this.f4412c);
        } else {
            HashMap map = new HashMap();
            map.put("profile", profile);
            valueWrapper.setValue(map);
        }
        iTResResponseCallback.onComplete("dev", null, new OutputParams("deviceModel", valueWrapper));
    }

    @Override // com.aliyun.alink.linksdk.tmp.listener.IDevListener
    public void onSuccess(Object obj, OutputParams outputParams) {
        ALog.d(e, "onSuccess returnValue:" + outputParams);
    }

    @Override // com.aliyun.alink.linksdk.tmp.listener.IDevListener
    public void onFail(Object obj, ErrorInfo errorInfo) {
        ALog.d(e, "onFail errorInfo:" + errorInfo);
    }
}
