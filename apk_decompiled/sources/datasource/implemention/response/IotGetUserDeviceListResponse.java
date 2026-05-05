package datasource.implemention.response;

import datasource.implemention.data.IotGetUserDeviceListRespData;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes3.dex */
public class IotGetUserDeviceListResponse extends BaseOutDo {
    public IotGetUserDeviceListRespData data;

    public void setData(IotGetUserDeviceListRespData iotGetUserDeviceListRespData) {
        this.data = iotGetUserDeviceListRespData;
    }

    /* JADX INFO: renamed from: getData, reason: merged with bridge method [inline-methods] */
    public IotGetUserDeviceListRespData m792getData() {
        return this.data;
    }
}
