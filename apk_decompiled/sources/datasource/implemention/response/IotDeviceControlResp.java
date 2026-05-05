package datasource.implemention.response;

import datasource.implemention.data.IotDeviceControlRespData;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes3.dex */
public class IotDeviceControlResp extends BaseOutDo {
    public IotDeviceControlRespData data;

    public void setData(IotDeviceControlRespData iotDeviceControlRespData) {
        this.data = iotDeviceControlRespData;
    }

    /* JADX INFO: renamed from: getData, reason: merged with bridge method [inline-methods] */
    public IotDeviceControlRespData m789getData() {
        return this.data;
    }
}
