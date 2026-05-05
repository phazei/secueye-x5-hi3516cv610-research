package datasource.implemention.response;

import datasource.implemention.data.IotProvisionCompleteRespData;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes3.dex */
public class IotProvisionCompleteResp extends BaseOutDo {
    public IotProvisionCompleteRespData data;

    public void setData(IotProvisionCompleteRespData iotProvisionCompleteRespData) {
        this.data = iotProvisionCompleteRespData;
    }

    /* JADX INFO: renamed from: getData, reason: merged with bridge method [inline-methods] */
    public IotProvisionCompleteRespData m793getData() {
        return this.data;
    }
}
