package datasource.implemention.response;

import datasource.implemention.data.IotCustomGroupControllRespData;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes3.dex */
public class IotCustomGroupControlResp extends BaseOutDo {
    public IotCustomGroupControllRespData data;

    public void setData(IotCustomGroupControllRespData iotCustomGroupControllRespData) {
        this.data = iotCustomGroupControllRespData;
    }

    /* JADX INFO: renamed from: getData, reason: merged with bridge method [inline-methods] */
    public IotCustomGroupControllRespData m787getData() {
        return this.data;
    }
}
