package datasource.implemention.response;

import datasource.implemention.data.GetDeviceUUIDRespData;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes3.dex */
public class GetDeviceUUIDResponse extends BaseOutDo {
    public GetDeviceUUIDRespData data;

    public Object getData() {
        return this.data;
    }

    public void setData(GetDeviceUUIDRespData getDeviceUUIDRespData) {
        this.data = getDeviceUUIDRespData;
    }
}
