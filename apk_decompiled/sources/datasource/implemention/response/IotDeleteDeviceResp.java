package datasource.implemention.response;

import datasource.implemention.data.IotDeleteDeviceRespData;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes3.dex */
public class IotDeleteDeviceResp extends BaseOutDo {
    public IotDeleteDeviceRespData data;

    public void setData(IotDeleteDeviceRespData iotDeleteDeviceRespData) {
        this.data = iotDeleteDeviceRespData;
    }

    /* JADX INFO: renamed from: getData, reason: merged with bridge method [inline-methods] */
    public IotDeleteDeviceRespData m788getData() {
        return this.data;
    }
}
