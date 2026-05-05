package datasource.implemention.response;

import datasource.implemention.data.IoTGatewayInvokeData;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes3.dex */
public class IotGatewayInvokeEventResp extends BaseOutDo {
    public IoTGatewayInvokeData data;

    public void setData(IoTGatewayInvokeData ioTGatewayInvokeData) {
        this.data = ioTGatewayInvokeData;
    }

    /* JADX INFO: renamed from: getData, reason: merged with bridge method [inline-methods] */
    public IoTGatewayInvokeData m790getData() {
        return this.data;
    }
}
