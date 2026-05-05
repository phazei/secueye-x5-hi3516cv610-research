package datasource.implemention.response;

import datasource.implemention.data.ProvisionConfirmRespData;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes3.dex */
public class ProvisionConfirmResp extends BaseOutDo {
    public ProvisionConfirmRespData data;

    public void setData(ProvisionConfirmRespData provisionConfirmRespData) {
        this.data = provisionConfirmRespData;
    }

    /* JADX INFO: renamed from: getData, reason: merged with bridge method [inline-methods] */
    public ProvisionConfirmRespData m796getData() {
        return this.data;
    }
}
