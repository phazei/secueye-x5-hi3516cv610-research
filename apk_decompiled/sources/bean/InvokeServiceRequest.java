package bean;

/* JADX INFO: loaded from: classes.dex */
public class InvokeServiceRequest {
    public Object args;
    public String identifier;
    public String iotId;

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

    public Object getArgs() {
        return this.args;
    }

    public void setArgs(Object obj) {
        this.args = obj;
    }
}
