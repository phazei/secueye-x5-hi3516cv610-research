package datasource.implemention;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.alibaba.ailabs.tg.network.Call;
import com.alibaba.ailabs.tg.network.annotation.Parameters;
import com.alibaba.ailabs.tg.network.annotation.Request;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.DeviceCommonConstants;
import datasource.implemention.data.AuthCheckAndGetBleKeyRespData;
import datasource.implemention.data.AuthRandomIdRespData;
import datasource.implemention.data.DeviceVersionInfo;
import datasource.implemention.data.GetDeviceUUIDRespData;
import datasource.implemention.data.OtaProgressRespData;
import datasource.implemention.data.UpdateDeviceVersionRespData;
import datasource.implemention.request.AuthCheckAndGetBleKeyForBLEDeviceRequest;
import datasource.implemention.request.AuthCheckAndGetBleKeyRequest;
import datasource.implemention.request.AuthRandomIdForBLEDeviceRequest;
import datasource.implemention.request.AuthRandomIdRequest;
import datasource.implemention.request.GetDeviceUUIDRequest;
import datasource.implemention.request.OtaProgressReportRequest;
import datasource.implemention.request.QueryOtaInfoRequest;
import datasource.implemention.request.UpdateDeviceVersionRequest;
import datasource.implemention.response.AuthCheckAndGetBleKeyResponse;
import datasource.implemention.response.AuthRandomIdResponse;
import datasource.implemention.response.GetDeviceUUIDResponse;
import datasource.implemention.response.OtaProgressReportResponse;
import datasource.implemention.response.QueryOtaInfoResponse;
import datasource.implemention.response.UpdateDeviceVersionResponse;

/* JADX INFO: loaded from: classes3.dex */
public interface AuthRequestService {
    @Request({AuthCheckAndGetBleKeyRequest.class, AuthCheckAndGetBleKeyResponse.class})
    @Parameters({"authInfo", AlinkConstants.KEY_PRODUCT_ID, "deviceId", "digest"})
    Call<AuthCheckAndGetBleKeyRespData> authCheckAndGetBleKey(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4);

    @Request({AuthCheckAndGetBleKeyForBLEDeviceRequest.class, AuthCheckAndGetBleKeyResponse.class})
    @Parameters({"authInfo", AlinkConstants.KEY_PRODUCT_ID, "deviceId", "digest", "deviceType"})
    Call<AuthCheckAndGetBleKeyRespData> authCheckAndGetBleKeyForBLEDevice(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, @NonNull String str5);

    @Request({AuthRandomIdRequest.class, AuthRandomIdResponse.class})
    @Parameters({"authInfo", AlinkConstants.KEY_PRODUCT_ID, "deviceId"})
    Call<AuthRandomIdRespData> getAuthRandomId(@NonNull String str, @NonNull String str2, @NonNull String str3);

    @Request({AuthRandomIdForBLEDeviceRequest.class, AuthRandomIdResponse.class})
    @Parameters({"authInfo", AlinkConstants.KEY_PRODUCT_ID, "deviceId", "deviceType"})
    Call<AuthRandomIdRespData> getAuthRandomIdForBLEDevice(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4);

    @Request({GetDeviceUUIDRequest.class, GetDeviceUUIDResponse.class})
    @Parameters({"authInfo", AlinkConstants.KEY_PRODUCT_ID, "macAddress"})
    Call<GetDeviceUUIDRespData> getDeviceUUID(@NonNull String str, @NonNull String str2, @NonNull String str3);

    @Request({OtaProgressReportRequest.class, OtaProgressReportResponse.class})
    @Parameters({"authInfo", "deviceId", "appVersion", NotificationCompat.CATEGORY_PROGRESS})
    Call<OtaProgressRespData> otaProgressReport(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4);

    @Request({QueryOtaInfoRequest.class, QueryOtaInfoResponse.class})
    @Parameters({"authInfo", DeviceCommonConstants.KEY_DEVICE_ID, "appVersion"})
    Call<DeviceVersionInfo> queryOtaInfo(@NonNull String str, @NonNull String str2, @NonNull String str3);

    @Request({UpdateDeviceVersionRequest.class, UpdateDeviceVersionResponse.class})
    @Parameters({"authInfo", DeviceCommonConstants.KEY_DEVICE_ID, "softwareVersion"})
    Call<UpdateDeviceVersionRespData> updateDeviceVersion(@NonNull String str, @NonNull String str2, @NonNull String str3);
}
