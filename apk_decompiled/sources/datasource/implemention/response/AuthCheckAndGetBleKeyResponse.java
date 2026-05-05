package datasource.implemention.response;

import datasource.implemention.data.AuthCheckAndGetBleKeyRespData;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes3.dex */
public class AuthCheckAndGetBleKeyResponse extends BaseOutDo {
    public AuthCheckAndGetBleKeyRespData data;

    public void setData(AuthCheckAndGetBleKeyRespData authCheckAndGetBleKeyRespData) {
        this.data = authCheckAndGetBleKeyRespData;
    }

    /* JADX INFO: renamed from: getData, reason: merged with bridge method [inline-methods] */
    public AuthCheckAndGetBleKeyRespData m781getData() {
        return this.data;
    }
}
