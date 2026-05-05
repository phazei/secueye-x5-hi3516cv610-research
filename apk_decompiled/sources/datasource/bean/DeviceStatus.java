package datasource.bean;

import java.io.Serializable;

/* JADX INFO: loaded from: classes3.dex */
public class DeviceStatus implements Serializable {
    public String iotId;
    public int sequenceNo;
    public String status;
    public int unicastAddress;
    public String userId;
    public String uuid;

    public static class Status {
        public String opcode;
        public String parameters;

        public String getOpcode() {
            return this.opcode;
        }

        public String getParameters() {
            return this.parameters;
        }

        public void setOpcode(String str) {
            this.opcode = str;
        }

        public void setParameters(String str) {
            this.parameters = str;
        }
    }

    public String getIotId() {
        return this.iotId;
    }

    public int getSequenceNo() {
        return this.sequenceNo;
    }

    public String getStatus() {
        return this.status;
    }

    public int getUnicastAddress() {
        return this.unicastAddress;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setIotId(String str) {
        this.iotId = str;
    }

    public void setSequenceNo(int i) {
        this.sequenceNo = i;
    }

    public void setStatus(String str) {
        this.status = str;
    }

    public void setUnicastAddress(int i) {
        this.unicastAddress = i;
    }

    public void setUserId(String str) {
        this.userId = str;
    }

    public void setUuid(String str) {
        this.uuid = str;
    }
}
