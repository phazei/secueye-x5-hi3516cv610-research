package com.aliyun.alink.business.devicecenter.provision.core;

import com.alibaba.ailabs.iot.mesh.MeshStatusCallback;
import com.alibaba.ailabs.iot.mesh.TgMeshManager;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.business.devicecenter.api.add.LinkType;
import com.aliyun.alink.business.devicecenter.api.add.ProvisionStatus;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.mesh.AppMeshStrategy;
import java.util.Map;

/* JADX INFO: compiled from: AppMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class C implements MeshStatusCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AppMeshStrategy f3658a;

    public C(AppMeshStrategy appMeshStrategy) {
        this.f3658a = appMeshStrategy;
    }

    @Override // com.alibaba.ailabs.iot.mesh.StatusCallback
    public void onStatus(int i, String str) {
        ALog.d(AppMeshStrategy.TAG, "mesh sdk onStatus() called with: statusCode = [" + i + "], statusMsg = [" + str + "]");
        this.f3658a.meshProvisionStatus = i;
        this.f3658a.meshProvisionErrorMessage = str;
        if (this.f3658a.provisionHasStopped.get()) {
            ALog.d(AppMeshStrategy.TAG, "provisionHasStopped = true, return.");
            return;
        }
        if (i == 1) {
            ALog.d(AppMeshStrategy.TAG, "mesh sdk init success.");
            this.f3658a.startMeshDeviceProvision();
        }
        if (i == -1) {
            this.f3658a.provisionErrorInfo = new DCErrorCode("SdkError", DCErrorCode.PF_PROVISION_APP_MESH_ERROR).setMsg("mesh sdk returned bind fail, state=" + i + ", msg=" + str).setSubcode(DCErrorCode.SUBCODE_MESH_SDK_INIT_EXCEPTION);
            this.f3658a.provisionResultCallback(null);
            return;
        }
        if (i != -3) {
            if (i == 20) {
                ProvisionStatus provisionStatus = ProvisionStatus.MESH_COMBO_WIFI_CONNECT_CLOUD_STATUS;
                provisionStatus.setExtraParams((Map) JSON.parseObject(str, Map.class));
                this.f3658a.provisionStatusCallback(provisionStatus);
                TgMeshManager.getInstance().stopAddNode();
                return;
            }
            return;
        }
        if (LinkType.ALI_APP_COMBO_MESH.equals(this.f3658a.mConfigParams.linkType) && this.f3658a.mCurrentRetryCount < 3) {
            this.f3658a.mCurrentRetryCount++;
            this.f3658a.provisionHasStarted.set(false);
            this.f3658a.provisionHasStarted.set(false);
            this.f3658a.stopProvisionTimer();
            this.f3658a.startProvision();
            return;
        }
        this.f3658a.provisionErrorInfo = new DCErrorCode("SdkError", DCErrorCode.PF_PROVISION_APP_MESH_ERROR).setMsg("mesh sdk returned provision fail, state=" + i + ", msg=" + str).setSubcode(DCErrorCode.SUBCODE_MESH_SDK_PROVISION_EXCEPTION);
        this.f3658a.provisionResultCallback(null);
        this.f3658a.mConfigCallback = null;
    }
}
