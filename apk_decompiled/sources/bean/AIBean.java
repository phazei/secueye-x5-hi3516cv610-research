package bean;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AIBean {
    private List<AiServiceListBean> aiServiceList;

    public List<AiServiceListBean> getAiServiceList() {
        return this.aiServiceList;
    }

    public void setAiServiceList(List<AiServiceListBean> list) {
        this.aiServiceList = list;
    }

    public static class AiServiceListBean {
        private int serviceCode;
        private String serviceName;

        public int getServiceCode() {
            return this.serviceCode;
        }

        public void setServiceCode(int i) {
            this.serviceCode = i;
        }

        public String getServiceName() {
            return this.serviceName;
        }

        public void setServiceName(String str) {
            this.serviceName = str;
        }
    }
}
