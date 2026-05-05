package datasource.bean;

import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class ControlProtocol {
    public String devId;
    public String deviceType;
    public boolean operateAll;
    public List<Sigmesh> sigmesh;

    public String getDevId() {
        return this.devId;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public List<Sigmesh> getSigmesh() {
        return this.sigmesh;
    }

    public boolean isOperateAll() {
        return this.operateAll;
    }

    public void setDevId(String str) {
        this.devId = str;
    }

    public void setDeviceType(String str) {
        this.deviceType = str;
    }

    public void setOperateAll(boolean z) {
        this.operateAll = z;
    }

    public void setSigmesh(List<Sigmesh> list) {
        this.sigmesh = list;
    }

    public String toString() {
        return "ControlProtocol{devId='" + this.devId + "', deviceType='" + this.deviceType + "', operateAll=" + this.operateAll + ", sigmesh=" + this.sigmesh + '}';
    }
}
