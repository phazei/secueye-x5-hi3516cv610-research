package datasource.implemention;

import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.Wa;
import com.alibaba.ailabs.iot.aisbase.Xa;
import com.alibaba.ailabs.tg.utils.LogUtils;
import com.aliyun.alink.linksdk.connectsdk.ApiHelper;
import datasource.AuthManager;
import datasource.NetworkCallback;
import datasource.implemention.data.DeviceVersionInfo;
import datasource.implemention.data.GetDeviceUUIDRespData;
import datasource.implemention.data.OtaProgressRespData;
import datasource.implemention.data.UpdateDeviceVersionRespData;
import datasource.implemention.feiyan.request.FeiyanAuthCipherCheckThenGetKeyForBLEDeviceRequest;
import datasource.implemention.feiyan.request.FeiyanGetAuthRandomIdForBLEDeviceRequest;

/* JADX INFO: loaded from: classes3.dex */
public class FeiyanAuthManager implements AuthManager {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f7861a = "FeiyanAuthManager";

    @Override // datasource.AuthManager
    public void authCheckAndGetBleKey(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, NetworkCallback<String> networkCallback) {
        networkCallback.onFailure("-1", "method not support");
    }

    @Override // datasource.AuthManager
    public void authCipherCheckThenGetKeyForBLEDevice(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, NetworkCallback<String> networkCallback) {
        LogUtils.i(f7861a, "authCipherCheckThenGetKeyForBLEDevice:-- " + str2 + ", " + str3);
        ApiHelper.getInstance().send(new FeiyanAuthCipherCheckThenGetKeyForBLEDeviceRequest(str2, str3, str4), new Xa(this, networkCallback));
    }

    @Override // datasource.AuthManager
    public void getAuthRandomId(@NonNull String str, @NonNull String str2, @NonNull String str3, NetworkCallback<String> networkCallback) {
        networkCallback.onFailure("-1", "method not support");
    }

    @Override // datasource.AuthManager
    public void getAuthRandomIdForBLEDevice(@NonNull String str, @NonNull String str2, @NonNull String str3, NetworkCallback<String> networkCallback) {
        LogUtils.i(f7861a, "getAuthRandomIdForBLEDevice: " + str2 + ", " + str3);
        ApiHelper.getInstance().send(new FeiyanGetAuthRandomIdForBLEDeviceRequest(str2, str3), new Wa(this, networkCallback));
    }

    @Override // datasource.AuthManager
    public void getDeviceUUID(@NonNull String str, @NonNull String str2, @NonNull String str3, NetworkCallback<GetDeviceUUIDRespData> networkCallback) {
        networkCallback.onFailure("-1", "method not support");
    }

    @Override // datasource.AuthManager
    public void gmaOtaProgressReport(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, @NonNull NetworkCallback<OtaProgressRespData> networkCallback) {
        networkCallback.onFailure("-1", "method not support");
    }

    @Override // datasource.AuthManager
    public void queryOtaInfo(@NonNull String str, @NonNull String str2, @NonNull String str3, NetworkCallback<DeviceVersionInfo> networkCallback) {
        networkCallback.onFailure("-1", "method not support");
    }

    @Override // datasource.AuthManager
    public void updateDeviceVersion(@NonNull String str, @NonNull String str2, @NonNull String str3, NetworkCallback<UpdateDeviceVersionRespData> networkCallback) {
        networkCallback.onFailure("-1", "method not support");
    }
}
