package bean;

/* JADX INFO: loaded from: classes.dex */
public class RecordPlanResponse extends LinkVisionResponseBase {
    Data data;

    public Data getData() {
        return this.data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        String eventTypeList;
        String name;
        String planId;
        int preRecordDuration;
        int recordDuration;
        String templateId;
        TimeTemplateDTO timeTemplateDTO;

        public String getEventTypeList() {
            return this.eventTypeList;
        }

        public void setEventTypeList(String str) {
            this.eventTypeList = str;
        }

        public String getPlanId() {
            return this.planId;
        }

        public void setPlanId(String str) {
            this.planId = str;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String str) {
            this.name = str;
        }

        public int getPreRecordDuration() {
            return this.preRecordDuration;
        }

        public void setPreRecordDuration(int i) {
            this.preRecordDuration = i;
        }

        public int getRecordDuration() {
            return this.recordDuration;
        }

        public void setRecordDuration(int i) {
            this.recordDuration = i;
        }

        public String getTemplateId() {
            return this.templateId;
        }

        public void setTemplateId(String str) {
            this.templateId = str;
        }

        public TimeTemplateDTO getTimeTemplateDTO() {
            return this.timeTemplateDTO;
        }

        public void setTimeTemplateDTO(TimeTemplateDTO timeTemplateDTO) {
            this.timeTemplateDTO = timeTemplateDTO;
        }
    }
}
