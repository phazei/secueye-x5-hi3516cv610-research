package a.a.a.a.b;

import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.ailabs.iot.mesh.bean.MeshNodeStatus;
import com.alibaba.ailabs.iot.mesh.contant.MeshUtConst$MeshErrorEnum;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import datasource.MeshConfigCallback;
import datasource.bean.ProvisionAppKey;
import datasource.bean.ProvisionInfo4Master;
import datasource.bean.SigmeshKey;
import java.util.List;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class ca implements MeshConfigCallback<ProvisionInfo4Master> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ MeshService f1312a;

    public ca(MeshService meshService) {
        this.f1312a = meshService;
    }

    @Override // datasource.MeshConfigCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(ProvisionInfo4Master provisionInfo4Master) {
        a.a.a.a.b.m.a.a(MeshService.TAG, "getProvisionInfo4Master request success");
        if (provisionInfo4Master != null) {
            try {
                G.a().b(provisionInfo4Master);
            } catch (Exception unused) {
            }
            Integer numValueOf = Integer.valueOf(provisionInfo4Master.getProvisionerAddr());
            this.f1312a.mMeshManagerApi.h(new byte[]{(byte) ((numValueOf.intValue() >> 8) & 255), (byte) (numValueOf.intValue() & 255)});
            MeshService meshService = this.f1312a;
            meshService.mProvisioningSettings = meshService.mMeshManagerApi.d();
            List<SigmeshKey> sigmeshKeys = provisionInfo4Master.getSigmeshKeys();
            provisionInfo4Master.getAddModelClient();
            provisionInfo4Master.getSubscribeGroupAddr();
            if (sigmeshKeys != null) {
                for (SigmeshKey sigmeshKey : sigmeshKeys) {
                    if (sigmeshKey != null && sigmeshKey.getProvisionNetKey() != null) {
                        this.f1312a.mSigmeshKeys.put(sigmeshKey.getProvisionNetKey().getNetKeyIndex(), sigmeshKey);
                    }
                }
            }
            if (this.f1312a.mSigmeshKeys.get(0) != null) {
                SigmeshKey sigmeshKey2 = (SigmeshKey) this.f1312a.mSigmeshKeys.get(0);
                if (sigmeshKey2.getProvisionNetKey() != null && sigmeshKey2.getProvisionNetKey().getNetKey() != null) {
                    String netKey = sigmeshKey2.getProvisionNetKey().getNetKey();
                    int netKeyIndex = sigmeshKey2.getProvisionNetKey().getNetKeyIndex();
                    this.f1312a.mProvisioningSettings.a(netKey);
                    this.f1312a.mProvisioningSettings.d(netKeyIndex);
                }
                if (sigmeshKey2.getProvisionAppKeys() != null && sigmeshKey2.getProvisionAppKeys().get(0) != null) {
                    ProvisionAppKey provisionAppKey = sigmeshKey2.getProvisionAppKeys().get(0);
                    int appKeyIndex = provisionAppKey.getAppKeyIndex();
                    String appKey = provisionAppKey.getAppKey();
                    if (this.f1312a.mProvisioningSettings.c().get(appKeyIndex) == null) {
                        this.f1312a.mProvisioningSettings.a(appKeyIndex, appKey);
                    } else if (!TextUtils.equals(this.f1312a.mProvisioningSettings.c().get(appKeyIndex), appKey)) {
                        this.f1312a.mProvisioningSettings.b(appKeyIndex, appKey);
                    }
                    b.e.i.c().a(new C0326aa(this));
                }
            }
            this.f1312a.mProvisioningSettings.c(0);
            this.f1312a.mProvisioningSettings.b(10);
            this.f1312a.mProvisioningSettings.a(0);
            this.f1312a.mProvisioningSettings.j();
            this.f1312a.mProvisioningSettings.p();
            if (Build.VERSION.SDK_INT >= 21 && this.f1312a.mFastProvisionWorker != null) {
                a.a.a.a.b.i.J j = this.f1312a.mFastProvisionWorker;
                MeshService meshService2 = this.f1312a;
                b.s sVar = meshService2.mProvisioningSettings;
                MeshService meshService3 = this.f1312a;
                j.a(meshService2, meshService2, sVar, meshService3, meshService3, meshService3);
            }
            this.f1312a.isInitialized = true;
            LocalBroadcastManager.getInstance(this.f1312a).sendBroadcast(new Intent(Utils.ACTION_INIT_SUCCESS));
            a.a.a.a.b.m.a.a(MeshService.TAG, "ProvisioningSettings updated");
        }
    }

    @Override // datasource.MeshConfigCallback
    public void onFailure(String str, String str2) {
        String str3;
        a.a.a.a.b.m.a.b(MeshService.TAG, "getProvisionInfo4Master request failed, errorMessage: " + str2);
        if (!this.f1312a.isRetry) {
            this.f1312a.mHandler.postDelayed(new ba(this), 500L);
            return;
        }
        this.f1312a.mIsProvisioningComplete = false;
        this.f1312a.isInitialized = false;
        Intent intent = new Intent(Utils.ACTION_PROVISIONING_STATE);
        intent.putExtra(Utils.EXTRA_PROVISIONING_STATE, MeshNodeStatus.REQUEST_FAILED.getState());
        MeshUtConst$MeshErrorEnum meshUtConst$MeshErrorEnum = MeshUtConst$MeshErrorEnum.GET_PROVISION_4_MASTER_REQUEST_ERROR;
        intent.putExtra(Utils.EXTRA_REQUEST_FAIL_MSG, meshUtConst$MeshErrorEnum.getErrorMsg() + " : " + str2);
        LocalBroadcastManager.getInstance(this.f1312a).sendBroadcast(intent);
        if (this.f1312a.mUnprovisionedMeshNodeData == null) {
            str3 = "";
        } else {
            str3 = this.f1312a.mUnprovisionedMeshNodeData.getProductId() + "";
        }
        a.a.a.a.b.m.b.a("ALSMesh", "ble", str3, false, null, "", 0L, meshUtConst$MeshErrorEnum.getErrorCode(), meshUtConst$MeshErrorEnum.getErrorMsg(), str, str2);
        LocalBroadcastManager.getInstance(this.f1312a).sendBroadcast(new Intent(Utils.ACTION_INIT_FAILED));
    }
}
