package datasource.implemention.response;

import datasource.implemention.data.GetProvisionInfoRespData;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes3.dex */
public class GetProvisionInfoResp extends BaseOutDo {
    public GetProvisionInfoRespData data;

    public void setData(GetProvisionInfoRespData getProvisionInfoRespData) {
        this.data = getProvisionInfoRespData;
    }

    /* JADX INFO: renamed from: getData, reason: merged with bridge method [inline-methods] */
    public GetProvisionInfoRespData m785getData() {
        return this.data;
    }
}
