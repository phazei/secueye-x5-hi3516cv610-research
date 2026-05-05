package datasource.bean.local;

import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class DeviceModel extends BaseDeviceModel {
    public PayloadBean payload;

    public static class PayloadBean extends BaseDeviceInfoModel {
        public List<DataBean> data;

        public static class DataBean extends BasePayloadDataBean {
        }

        public List<DataBean> getData() {
            return this.data;
        }

        public void setData(List<DataBean> list) {
            this.data = list;
        }
    }

    public PayloadBean getPayload() {
        return this.payload;
    }

    public void setPayload(PayloadBean payloadBean) {
        this.payload = payloadBean;
    }
}
