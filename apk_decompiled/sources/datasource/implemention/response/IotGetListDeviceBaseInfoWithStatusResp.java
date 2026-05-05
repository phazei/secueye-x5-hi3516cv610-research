package datasource.implemention.response;

import datasource.implemention.data.IotGetListDeviceBaseInfoWithStatusRespData;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes3.dex */
public class IotGetListDeviceBaseInfoWithStatusResp extends BaseOutDo {
    public IotGetListDeviceBaseInfoWithStatusRespData data;

    public void setData(IotGetListDeviceBaseInfoWithStatusRespData iotGetListDeviceBaseInfoWithStatusRespData) {
        this.data = iotGetListDeviceBaseInfoWithStatusRespData;
    }

    /* JADX INFO: renamed from: getData, reason: merged with bridge method [inline-methods] */
    public IotGetListDeviceBaseInfoWithStatusRespData m791getData() {
        return this.data;
    }
}
