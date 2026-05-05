package bean;

/* JADX INFO: loaded from: classes.dex */
public class StatusBean {
    private int code;
    private DataBean data;
    private String id;

    public int getCode() {
        return this.code;
    }

    public void setCode(int i) {
        this.code = i;
    }

    public DataBean getData() {
        return this.data;
    }

    public void setData(DataBean dataBean) {
        this.data = dataBean;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public static class DataBean {
        private int status;
        private long time;

        public long getTime() {
            return this.time;
        }

        public void setTime(long j) {
            this.time = j;
        }

        public int getStatus() {
            return this.status;
        }

        public void setStatus(int i) {
            this.status = i;
        }
    }
}
