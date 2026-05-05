package datasource.implemention.response;

import datasource.implemention.data.GetInfoByAuthInfoRespData;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes3.dex */
public class GetInfoByAuthInfoResp extends BaseOutDo {
    public GetInfoByAuthInfoRespData data;

    public void setData(GetInfoByAuthInfoRespData getInfoByAuthInfoRespData) {
        this.data = getInfoByAuthInfoRespData;
    }

    /* JADX INFO: renamed from: getData, reason: merged with bridge method [inline-methods] */
    public GetInfoByAuthInfoRespData m784getData() {
        return this.data;
    }
}
