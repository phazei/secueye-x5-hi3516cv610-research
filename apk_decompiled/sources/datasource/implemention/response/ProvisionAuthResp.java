package datasource.implemention.response;

import datasource.implemention.data.ProvisionAuthRespData;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes3.dex */
public class ProvisionAuthResp extends BaseOutDo {
    public ProvisionAuthRespData data;

    public void setData(ProvisionAuthRespData provisionAuthRespData) {
        this.data = provisionAuthRespData;
    }

    /* JADX INFO: renamed from: getData, reason: merged with bridge method [inline-methods] */
    public ProvisionAuthRespData m795getData() {
        return this.data;
    }
}
