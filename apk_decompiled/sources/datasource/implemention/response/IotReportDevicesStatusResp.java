package datasource.implemention.response;

import datasource.implemention.data.IotReportDevicesStatusRespData;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes3.dex */
public class IotReportDevicesStatusResp extends BaseOutDo {
    public IotReportDevicesStatusRespData data;

    public void setData(IotReportDevicesStatusRespData iotReportDevicesStatusRespData) {
        this.data = iotReportDevicesStatusRespData;
    }

    /* JADX INFO: renamed from: getData, reason: merged with bridge method [inline-methods] */
    public IotReportDevicesStatusRespData m794getData() {
        return this.data;
    }
}
