package datasource;

import androidx.annotation.NonNull;
import datasource.implemention.data.DeviceVersionInfo;
import datasource.implemention.data.GetDeviceUUIDRespData;
import datasource.implemention.data.OtaProgressRespData;
import datasource.implemention.data.UpdateDeviceVersionRespData;

/* JADX INFO: loaded from: classes3.dex */
public interface AuthManager {
    void authCheckAndGetBleKey(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, NetworkCallback<String> networkCallback);

    void authCipherCheckThenGetKeyForBLEDevice(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, NetworkCallback<String> networkCallback);

    void getAuthRandomId(@NonNull String str, @NonNull String str2, @NonNull String str3, NetworkCallback<String> networkCallback);

    void getAuthRandomIdForBLEDevice(@NonNull String str, @NonNull String str2, @NonNull String str3, NetworkCallback<String> networkCallback);

    void getDeviceUUID(@NonNull String str, @NonNull String str2, @NonNull String str3, NetworkCallback<GetDeviceUUIDRespData> networkCallback);

    void gmaOtaProgressReport(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, @NonNull NetworkCallback<OtaProgressRespData> networkCallback);

    void queryOtaInfo(@NonNull String str, @NonNull String str2, @NonNull String str3, NetworkCallback<DeviceVersionInfo> networkCallback);

    void updateDeviceVersion(@NonNull String str, @NonNull String str2, @NonNull String str3, NetworkCallback<UpdateDeviceVersionRespData> networkCallback);
}
