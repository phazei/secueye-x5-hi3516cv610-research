package com.aliyun.alink.business.devicecenter.provision.core;

import android.text.TextUtils;
import com.alibaba.ailabs.iot.mesh.TgScanManager;
import com.alibaba.ailabs.iot.mesh.UnprovisionedBluetoothMeshDevice;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.add.LinkType;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkHelper;
import com.aliyun.alink.business.devicecenter.biz.MeshDiscoverCallback;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.mesh.AppMeshStrategy;
import java.util.ArrayList;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.y, reason: case insensitive filesystem */
/* JADX INFO: compiled from: AppMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class C0491y implements MeshDiscoverCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ UnprovisionedBluetoothMeshDevice f3725a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ AppMeshStrategy f3726b;

    public C0491y(AppMeshStrategy appMeshStrategy, UnprovisionedBluetoothMeshDevice unprovisionedBluetoothMeshDevice) {
        this.f3726b = appMeshStrategy;
        this.f3725a = unprovisionedBluetoothMeshDevice;
    }

    @Override // com.aliyun.alink.business.devicecenter.biz.MeshDiscoverCallback
    public void onFailure(String str) {
        ALog.e(AppMeshStrategy.TAG, "Cloud Filter, error: " + str);
        this.f3726b.isScanStartedFirstQuery.set(false);
        this.f3726b.mInFilterProcess.set(false);
    }

    @Override // com.aliyun.alink.business.devicecenter.biz.MeshDiscoverCallback
    public void onSuccess(JSONArray jSONArray) {
        try {
            this.f3726b.isScanStartedFirstQuery.set(false);
        } catch (Exception e) {
            ALog.w(AppMeshStrategy.TAG, "getDiscoveredMeshDevice parse exception. " + e);
        }
        if (jSONArray != null && !jSONArray.isEmpty()) {
            TgScanManager.getInstance().stopGetRemoteSpecifiedPIDUnprovisionedSigMeshDeviceWithScan();
            this.f3726b.unprovisionedBluetoothMeshDevice = this.f3725a;
            this.f3726b.unprovisionedDeviceFound.set(true);
            ArrayList arrayList = new ArrayList();
            String str = AppMeshStrategy.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Cloud Filter, response size: ");
            sb.append(jSONArray.size());
            sb.append(", chain: ");
            sb.append(this.f3726b);
            ALog.d(str, sb.toString());
            for (int i = 0; i < jSONArray.size(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                if (jSONObject != null) {
                    this.f3726b.mDeviceInfo = new DeviceInfo();
                    this.f3726b.mDeviceInfo.deviceName = jSONObject.getString("deviceName");
                    this.f3726b.mDeviceInfo.productKey = jSONObject.getString("productKey");
                    this.f3726b.mDeviceInfo.productId = jSONObject.getString(AlinkConstants.KEY_PRODUCT_ID);
                    this.f3726b.mDeviceInfo.mac = jSONObject.getString(AlinkConstants.KEY_MAC);
                    this.f3726b.mDeviceInfo.deviceId = jSONObject.getString(AlinkConstants.KEY_SUB_DEVICE_ID);
                    this.f3726b.mDeviceInfo.linkType = LinkType.ALI_APP_MESH.getName();
                    this.f3726b.mDeviceInfo.authFlag = jSONObject.getBooleanValue(AlinkConstants.KEY_AUTH_FLAG);
                    this.f3726b.mDeviceInfo.confirmCloud = jSONObject.getString(AlinkConstants.KEY_CONFIRM_CLOUD);
                    this.f3726b.mDeviceInfo.subDeviceId = jSONObject.getString(AlinkConstants.KEY_SUB_DEVICE_ID);
                    if (this.f3726b.mDeviceInfo.authFlag) {
                        this.f3726b.mDeviceInfo.random = jSONObject.getString(AlinkConstants.KEY_RANDOM);
                        this.f3726b.mDeviceInfo.authDevice = jSONObject.getString(AlinkConstants.KEY_AUTH_DEVICE);
                    }
                    if (!TextUtils.isEmpty(this.f3726b.mDeviceInfo.mac) && !this.f3726b.mDeviceInfo.mac.contains(":")) {
                        this.f3726b.mDeviceInfo.mac = AlinkHelper.getMacFromSimpleMac(this.f3726b.mDeviceInfo.mac);
                    }
                    arrayList.add(this.f3726b.mDeviceInfo);
                    this.f3726b.startConcurrentAddDevice(arrayList);
                }
            }
            this.f3726b.mInFilterProcess.set(false);
            return;
        }
        this.f3726b.mInFilterProcess.set(false);
    }
}
