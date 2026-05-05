package datasource.bean;

/* JADX INFO: loaded from: classes3.dex */
public class ActivePayload {
    public int groupAddr;
    public String opcode;
    public String parameters;
    public int retryCount;

    public int getGroupAddr() {
        return this.groupAddr;
    }

    public String getOpcode() {
        return this.opcode;
    }

    public String getParameters() {
        return this.parameters;
    }

    public int getRetryCount() {
        return this.retryCount;
    }

    public void setGroupAddr(int i) {
        this.groupAddr = i;
    }

    public void setOpcode(String str) {
        this.opcode = str;
    }

    public void setParameters(String str) {
        this.parameters = str;
    }

    public void setRetryCount(int i) {
        this.retryCount = i;
    }
}
