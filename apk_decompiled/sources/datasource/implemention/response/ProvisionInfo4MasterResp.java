package datasource.implemention.response;

import datasource.implemention.data.ProvisionInfo4MasterRespData;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes3.dex */
public class ProvisionInfo4MasterResp extends BaseOutDo {
    public ProvisionInfo4MasterRespData data;

    public void setData(ProvisionInfo4MasterRespData provisionInfo4MasterRespData) {
        this.data = provisionInfo4MasterRespData;
    }

    /* JADX INFO: renamed from: getData, reason: merged with bridge method [inline-methods] */
    public ProvisionInfo4MasterRespData m797getData() {
        return this.data;
    }
}
