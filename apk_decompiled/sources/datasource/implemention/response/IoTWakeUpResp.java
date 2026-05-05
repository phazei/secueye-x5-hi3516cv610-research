package datasource.implemention.response;

import datasource.implemention.data.IoTWakeUpData;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes3.dex */
public class IoTWakeUpResp extends BaseOutDo {
    public IoTWakeUpData data;

    public void setData(IoTWakeUpData ioTWakeUpData) {
        this.data = ioTWakeUpData;
    }

    /* JADX INFO: renamed from: getData, reason: merged with bridge method [inline-methods] */
    public IoTWakeUpData m786getData() {
        return this.data;
    }
}
