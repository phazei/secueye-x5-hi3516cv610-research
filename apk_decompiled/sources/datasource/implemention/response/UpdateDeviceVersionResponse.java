package datasource.implemention.response;

import datasource.implemention.data.UpdateDeviceVersionRespData;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes3.dex */
public class UpdateDeviceVersionResponse extends BaseOutDo {
    public UpdateDeviceVersionRespData data;

    public Object getData() {
        return this.data;
    }

    public void setData(UpdateDeviceVersionRespData updateDeviceVersionRespData) {
        this.data = updateDeviceVersionRespData;
    }
}
