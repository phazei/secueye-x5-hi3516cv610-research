package a.a.a.a.b.i;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.alibaba.ailabs.iot.mesh.bean.MeshNodeStatus;
import com.alibaba.ailabs.iot.mesh.provision.FastProvisionManager;
import com.alibaba.ailabs.iot.mesh.utils.Utils;

/* JADX INFO: renamed from: a.a.a.a.b.i.e, reason: case insensitive filesystem */
/* JADX INFO: compiled from: FastProvisionManager.java */
/* JADX INFO: loaded from: classes.dex */
public class C0339e extends BroadcastReceiver {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ FastProvisionManager f1418a;

    public C0339e(FastProvisionManager fastProvisionManager) {
        this.f1418a = fastProvisionManager;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (Utils.ACTION_PROVISIONING_STATE.equals(intent.getAction())) {
            if (intent.getIntExtra(Utils.EXTRA_PROVISIONING_STATE, -1) == MeshNodeStatus.PROVISIONING_FAILED.getState()) {
                this.f1418a.onProvisionProgressFinished();
            }
        } else if (Utils.ACTION_BIND_STATE.equals(intent.getAction())) {
            intent.getIntExtra(Utils.EXTRA_BIND_CODE, -1);
            this.f1418a.onProvisionProgressFinished();
        }
    }
}
