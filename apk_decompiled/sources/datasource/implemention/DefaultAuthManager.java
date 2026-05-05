package datasource.implemention;

import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.Oa;
import com.alibaba.ailabs.iot.aisbase.Pa;
import com.alibaba.ailabs.iot.aisbase.Qa;
import com.alibaba.ailabs.iot.aisbase.Ra;
import com.alibaba.ailabs.iot.aisbase.Sa;
import com.alibaba.ailabs.iot.aisbase.Ta;
import com.alibaba.ailabs.iot.aisbase.Ua;
import com.alibaba.ailabs.iot.aisbase.Va;
import com.alibaba.ailabs.tg.network.NetworkLifecycle;
import com.alibaba.ailabs.tg.network.NetworkLifecycleOwner;
import datasource.AuthManager;
import datasource.NetworkCallback;
import datasource.implemention.data.DeviceVersionInfo;
import datasource.implemention.data.GetDeviceUUIDRespData;
import datasource.implemention.data.OtaProgressRespData;
import datasource.implemention.data.UpdateDeviceVersionRespData;

/* JADX INFO: loaded from: classes3.dex */
public class DefaultAuthManager implements AuthManager, NetworkLifecycleOwner {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public AuthRequestService f7860a = (AuthRequestService) NetworkManager.getService(AuthRequestService.class);

    @Override // datasource.AuthManager
    public void authCheckAndGetBleKey(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, NetworkCallback<String> networkCallback) {
        this.f7860a.authCheckAndGetBleKey(str, str2, str3, str4).bindTo(this).enqueue(new Pa(this, networkCallback));
    }

    @Override // datasource.AuthManager
    public void authCipherCheckThenGetKeyForBLEDevice(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, NetworkCallback<String> networkCallback) {
        this.f7860a.authCheckAndGetBleKeyForBLEDevice(str, str2, str3, str4, "iot").bindTo(this).enqueue(new Ua(this, networkCallback));
    }

    @Override // datasource.AuthManager
    public void getAuthRandomId(@NonNull String str, @NonNull String str2, @NonNull String str3, NetworkCallback<String> networkCallback) {
        this.f7860a.getAuthRandomId(str, str2, str3).bindTo(this).enqueue(new Oa(this, networkCallback));
    }

    @Override // datasource.AuthManager
    public void getAuthRandomIdForBLEDevice(@NonNull String str, @NonNull String str2, @NonNull String str3, NetworkCallback<String> networkCallback) {
        this.f7860a.getAuthRandomIdForBLEDevice(str, str2, str3, "iot").bindTo(this).enqueue(new Ta(this, networkCallback));
    }

    @Override // datasource.AuthManager
    public void getDeviceUUID(@NonNull String str, @NonNull String str2, @NonNull String str3, NetworkCallback<GetDeviceUUIDRespData> networkCallback) {
        this.f7860a.getDeviceUUID(str, str2, str3).enqueue(new Qa(this, networkCallback));
    }

    public NetworkLifecycle getNetworkLifecycle() {
        return new NetworkLifecycle();
    }

    @Override // datasource.AuthManager
    public void gmaOtaProgressReport(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, NetworkCallback<OtaProgressRespData> networkCallback) {
        this.f7860a.otaProgressReport(str, str2, str3, str4).enqueue(new Va(this, networkCallback));
    }

    @Override // datasource.AuthManager
    public void queryOtaInfo(@NonNull String str, @NonNull String str2, @NonNull String str3, NetworkCallback<DeviceVersionInfo> networkCallback) {
        this.f7860a.queryOtaInfo(str, str2, str3).enqueue(new Ra(this, networkCallback));
    }

    @Override // datasource.AuthManager
    public void updateDeviceVersion(@NonNull String str, @NonNull String str2, @NonNull String str3, NetworkCallback<UpdateDeviceVersionRespData> networkCallback) {
        this.f7860a.updateDeviceVersion(str, str2, str3).enqueue(new Sa(this, networkCallback));
    }
}
