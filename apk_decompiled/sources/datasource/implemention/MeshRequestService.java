package datasource.implemention;

import androidx.annotation.NonNull;
import com.alibaba.ailabs.tg.network.Call;
import com.alibaba.ailabs.tg.network.annotation.CommonParameters;
import com.alibaba.ailabs.tg.network.annotation.Parameters;
import com.alibaba.ailabs.tg.network.annotation.Request;
import com.alibaba.fastjson.JSONArray;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.DeviceCommonConstants;
import com.aliyun.alink.linksdk.tools.ut.AUserTrack;
import datasource.implemention.data.ConfigurationRespData;
import datasource.implemention.data.GetInfoByAuthInfoRespData;
import datasource.implemention.data.GetProvisionInfoRespData;
import datasource.implemention.data.IotCustomGroupControllRespData;
import datasource.implemention.data.IotDeleteDeviceRespData;
import datasource.implemention.data.IotDeviceControlRespData;
import datasource.implemention.data.IotGetListDeviceBaseInfoWithStatusRespData;
import datasource.implemention.data.IotGetUserDeviceListRespData;
import datasource.implemention.data.IotProvisionCompleteRespData;
import datasource.implemention.data.IotReportDevicesStatusRespData;
import datasource.implemention.data.ProvisionAuthRespData;
import datasource.implemention.data.ProvisionConfirmRespData;
import datasource.implemention.data.ProvisionInfo4MasterRespData;
import datasource.implemention.request.BatchBindMeshDevicesRequest;
import datasource.implemention.request.ConfigurationRequest;
import datasource.implemention.request.GetInfoByAuthInfoRequest;
import datasource.implemention.request.GetProvisionInfoRequest;
import datasource.implemention.request.IotCustomGroupControlRequest;
import datasource.implemention.request.IotDeleteDeviceRequest;
import datasource.implemention.request.IotDeviceControlRequest;
import datasource.implemention.request.IotGetListDeviceBaseInfoWithStatusRequest;
import datasource.implemention.request.IotGetUserDeviceListRequest;
import datasource.implemention.request.IotProvisionCompleteRequest;
import datasource.implemention.request.IotReportDevicesStatusRequest;
import datasource.implemention.request.ProvisionAuthRequest;
import datasource.implemention.request.ProvisionConfirmRequest;
import datasource.implemention.request.ProvisionInfo4MasterRequest;
import datasource.implemention.response.BatchBindMeshDevicesResp;
import datasource.implemention.response.ConfigurationResp;
import datasource.implemention.response.GetInfoByAuthInfoResp;
import datasource.implemention.response.GetProvisionInfoResp;
import datasource.implemention.response.IotCustomGroupControlResp;
import datasource.implemention.response.IotDeleteDeviceResp;
import datasource.implemention.response.IotDeviceControlResp;
import datasource.implemention.response.IotGetListDeviceBaseInfoWithStatusResp;
import datasource.implemention.response.IotGetUserDeviceListResponse;
import datasource.implemention.response.IotProvisionCompleteResp;
import datasource.implemention.response.IotReportDevicesStatusResp;
import datasource.implemention.response.ProvisionAuthResp;
import datasource.implemention.response.ProvisionConfirmResp;
import datasource.implemention.response.ProvisionInfo4MasterResp;

/* JADX INFO: loaded from: classes3.dex */
public interface MeshRequestService {
    @Request({ConfigurationRequest.class, ConfigurationResp.class})
    @Parameters({"authInfo", "unicastAddresses", "configurationReq"})
    Call<ConfigurationRespData> configuration(@NonNull String str, @NonNull String str2, @NonNull String str3);

    @Request({IotCustomGroupControlRequest.class, IotCustomGroupControlResp.class})
    @Parameters({"familyId", DeviceCommonConstants.KEY_MESSAGE_GROUP, "params"})
    Call<IotCustomGroupControllRespData> customGroupControl(@NonNull long j, @NonNull long j2, @NonNull String str);

    @Request({IotDeleteDeviceRequest.class, IotDeleteDeviceResp.class})
    @Parameters({"authInfo", AlinkConstants.KEY_DEVICE_INFO, "unbindRequestJSONString"})
    Call<IotDeleteDeviceRespData> deleteDevice(@NonNull String str, @NonNull String str2, @NonNull String str3);

    @Request({IotDeviceControlRequest.class, IotDeviceControlResp.class})
    @Parameters({"authInfo", "devId", "skillId", "params"})
    Call<IotDeviceControlRespData> deviceControl(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4);

    @CommonParameters({"authInfo"})
    @Request({BatchBindMeshDevicesRequest.class, BatchBindMeshDevicesResp.class})
    Call<Object> getBatchBindMeshDevices(@NonNull String str, JSONArray jSONArray);

    @Request({GetInfoByAuthInfoRequest.class, GetInfoByAuthInfoResp.class})
    @Parameters({"authInfo", AUserTrack.UTKEY_MODULE, "func", "args"})
    Call<GetInfoByAuthInfoRespData> getInfoByAuthInfo(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4);

    @Request({IotGetUserDeviceListRequest.class, IotGetUserDeviceListResponse.class})
    @Parameters({"authInfo"})
    Call<IotGetUserDeviceListRespData> getIotDeviceList(@NonNull String str);

    @CommonParameters({"authInfo"})
    @Request({IotGetListDeviceBaseInfoWithStatusRequest.class, IotGetListDeviceBaseInfoWithStatusResp.class})
    Call<IotGetListDeviceBaseInfoWithStatusRespData> getListDeviceBaseInfoWithStatus();

    @Request({GetProvisionInfoRequest.class, GetProvisionInfoResp.class})
    @Parameters({"authInfo", "provisionBaseReq"})
    Call<GetProvisionInfoRespData> getProvisionInfo(@NonNull String str, @NonNull String str2);

    @Request({ProvisionInfo4MasterRequest.class, ProvisionInfo4MasterResp.class})
    @Parameters({"authInfo", DeviceCommonConstants.KEY_DEVICE_ID})
    Call<ProvisionInfo4MasterRespData> getProvisionInfo4Master(@NonNull String str, String str2);

    @Request({ProvisionAuthRequest.class, ProvisionAuthResp.class})
    @Parameters({"authInfo", "provisionAuthorizationReq"})
    Call<ProvisionAuthRespData> provisionAuth(@NonNull String str, @NonNull String str2);

    @Request({IotProvisionCompleteRequest.class, IotProvisionCompleteResp.class})
    @Parameters({"authInfo", "provisionCompleteReq"})
    Call<IotProvisionCompleteRespData> provisionComplete(@NonNull String str, @NonNull String str2);

    @Request({ProvisionConfirmRequest.class, ProvisionConfirmResp.class})
    @Parameters({"authInfo", "provisionConfirmationReq"})
    Call<ProvisionConfirmRespData> provisionConfirm(@NonNull String str, @NonNull String str2);

    @Request({IotReportDevicesStatusRequest.class, IotReportDevicesStatusResp.class})
    @Parameters({"authInfo", DeviceCommonConstants.KEY_DEVICE_ID, "devicesStatusList"})
    Call<IotReportDevicesStatusRespData> reportDevicesStatus(@NonNull String str, @NonNull String str2, @NonNull String str3);
}
