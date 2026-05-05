package bean;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class DeviceTimeModel implements Serializable {

    @JSONField(name = "TZ")
    private String TZ;

    @JSONField(name = "Time")
    private int Time;

    public int getTime() {
        return this.Time;
    }

    public void setTime(int i) {
        this.Time = i;
    }

    public String getTZ() {
        return this.TZ;
    }

    public void setTZ(String str) {
        this.TZ = str;
    }

    public String toString() {
        return "DeviceTimeModel{Time=" + this.Time + ", TZ='" + this.TZ + "'}";
    }
}
