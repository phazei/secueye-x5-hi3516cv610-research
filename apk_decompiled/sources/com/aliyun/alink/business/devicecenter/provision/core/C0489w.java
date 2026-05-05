package com.aliyun.alink.business.devicecenter.provision.core;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.UnprovisionedBluetoothMeshDevice;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.provision.core.mesh.AppMeshStrategy;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.w, reason: case insensitive filesystem */
/* JADX INFO: compiled from: AppMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class C0489w implements IActionListener<UnprovisionedBluetoothMeshDevice> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AppMeshStrategy f3722a;

    public C0489w(AppMeshStrategy appMeshStrategy) {
        this.f3722a = appMeshStrategy;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(UnprovisionedBluetoothMeshDevice unprovisionedBluetoothMeshDevice) {
        this.f3722a.startSupportDeviceProvision(unprovisionedBluetoothMeshDevice);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onFailure(int i, String str) {
        this.f3722a.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setMsg("app mesh provision, no discovered object, please discover before provisioning.").setSubcode(DCErrorCode.SUBCODE_PE_PROVISION_PARAMS_ERROR);
        this.f3722a.provisionResultCallback(null);
    }
}
