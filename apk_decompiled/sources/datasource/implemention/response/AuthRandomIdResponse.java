package datasource.implemention.response;

import datasource.implemention.data.AuthRandomIdRespData;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes3.dex */
public class AuthRandomIdResponse extends BaseOutDo {
    public AuthRandomIdRespData data;

    public void setData(AuthRandomIdRespData authRandomIdRespData) {
        this.data = authRandomIdRespData;
    }

    /* JADX INFO: renamed from: getData, reason: merged with bridge method [inline-methods] */
    public AuthRandomIdRespData m782getData() {
        return this.data;
    }
}
