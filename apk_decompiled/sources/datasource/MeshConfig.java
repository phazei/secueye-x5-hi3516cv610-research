package datasource;

import androidx.annotation.NonNull;
import com.alibaba.fastjson.JSONArray;
import datasource.bean.ConfigurationData;
import datasource.bean.DeleteDeviceRespDataExtends;
import datasource.bean.DeviceStatus;
import datasource.bean.IoTGatewayEvent;
import datasource.bean.IotDeviceInfo;
import datasource.bean.ProvisionInfo;
import datasource.bean.ProvisionInfo4Master;
import datasource.bean.ServerConfirmation;
import datasource.bean.Sigmesh;
import datasource.bean.WakeUpServiceContext;
import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public interface MeshConfig {
    void batchBindMeshDevices(String str, JSONArray jSONArray, MeshConfigCallback<Object> meshConfigCallback);

    void configuration(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, @NonNull String str5, @NonNull String str6, MeshConfigCallback<ConfigurationData> meshConfigCallback);

    void deleteDevice(@NonNull String str, @NonNull String str2, @NonNull String str3, MeshConfigCallback<DeleteDeviceRespDataExtends> meshConfigCallback);

    void deviceControl(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, MeshConfigCallback<List<Sigmesh>> meshConfigCallback);

    void getInfoByAuthInfo(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, MeshConfigCallback<String> meshConfigCallback);

    void getIotDeviceList(@NonNull String str, MeshConfigCallback<List<IotDeviceInfo>> meshConfigCallback);

    void getProvisionInfo(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, MeshConfigCallback<ProvisionInfo> meshConfigCallback);

    void getProvisionInfo4Master(@NonNull String str, String str2, MeshConfigCallback<ProvisionInfo4Master> meshConfigCallback);

    void groupControl(String str, String str2, String str3, MeshConfigCallback<List<Sigmesh>> meshConfigCallback);

    void provisionAuth(String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, @NonNull String str5, @NonNull String str6, @NonNull String str7, MeshConfigCallback<Boolean> meshConfigCallback);

    void provisionComplete(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, @NonNull String str5, MeshConfigCallback<ConfigurationData> meshConfigCallback);

    void provisionConfirm(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, @NonNull String str5, @NonNull String str6, MeshConfigCallback<ServerConfirmation> meshConfigCallback);

    void reportDevicesStatus(@NonNull String str, @NonNull String str2, @NonNull List<DeviceStatus> list, MeshConfigCallback<String> meshConfigCallback);

    void triggerGatewayEventAccs(String str, IoTGatewayEvent ioTGatewayEvent);

    void wakeUpDevice(String str, WakeUpServiceContext wakeUpServiceContext, MeshConfigCallback<List<Sigmesh>> meshConfigCallback);
}
