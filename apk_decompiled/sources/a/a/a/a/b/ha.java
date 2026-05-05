package a.a.a.a.b;

import android.content.Context;
import android.os.Looper;
import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;
import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.ailabs.iot.mesh.bean.ExtendedBluetoothDevice;
import java.util.ArrayList;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class ha implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ExtendedBluetoothDevice f1335a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ boolean f1336b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ MeshService f1337c;

    public ha(MeshService meshService, ExtendedBluetoothDevice extendedBluetoothDevice, boolean z) {
        this.f1337c = meshService;
        this.f1335a = extendedBluetoothDevice;
        this.f1336b = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        Looper.prepare();
        if (this.f1337c.deviceProvisioningWorkerArray == null) {
            this.f1337c.deviceProvisioningWorkerArray = new ArrayList();
        }
        Context applicationContext = this.f1337c.getApplicationContext();
        MeshService meshService = this.f1337c;
        DeviceProvisioningWorker deviceProvisioningWorker = new DeviceProvisioningWorker(applicationContext, meshService, meshService.mSigmeshKeys, this.f1337c.mMeshManagerApi.b(), this.f1337c.mOnReadyToBindHandler, this.f1337c.mConcurrentProvisionContext);
        deviceProvisioningWorker.a(this.f1337c.mGlobalProvisionFinishedListener);
        deviceProvisioningWorker.a(this.f1335a, this.f1336b, this.f1337c.mProvisioningExtensionsParams);
        this.f1337c.deviceProvisioningWorkerArray.add(deviceProvisioningWorker);
    }
}
