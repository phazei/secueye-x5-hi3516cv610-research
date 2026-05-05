package datasource.implemention.response;

import datasource.implemention.data.DeviceVersionInfo;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes3.dex */
public class QueryOtaInfoResponse extends BaseOutDo {
    public DeviceVersionInfo data;

    public Object getData() {
        return this.data;
    }

    public void setData(DeviceVersionInfo deviceVersionInfo) {
        this.data = deviceVersionInfo;
    }
}
