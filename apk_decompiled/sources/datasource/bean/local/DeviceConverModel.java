package datasource.bean.local;

import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class DeviceConverModel extends BaseDeviceModel {
    public PayloadBean payload;

    public static class PayloadBean extends BaseDeviceInfoModel {
        public List<DataBean> data;

        public static class DataBean extends BasePayloadDataBean {
            public int skillId = 3034;

            public int getSkillId() {
                return this.skillId;
            }

            public void setSkillId(int i) {
                this.skillId = i;
            }
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
