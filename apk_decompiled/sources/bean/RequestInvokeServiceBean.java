package bean;

/* JADX INFO: loaded from: classes.dex */
public class RequestInvokeServiceBean {
    private Args args;
    private String identifier;
    private String iotId;

    public RequestInvokeServiceBean(String str, String str2, Args args) {
        this.iotId = str;
        this.identifier = str2;
        this.args = args;
    }

    public String getIotId() {
        return this.iotId;
    }

    public void setIotId(String str) {
        this.iotId = str;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String str) {
        this.identifier = str;
    }

    public Args getArgs() {
        return this.args;
    }

    public void setArgs(Args args) {
        this.args = args;
    }

    public static class Args {
        private int arg1;

        public Args(int i) {
            this.arg1 = i;
        }
    }
}
