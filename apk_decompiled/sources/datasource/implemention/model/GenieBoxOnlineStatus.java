package datasource.implemention.model;

import java.io.Serializable;

/* JADX INFO: loaded from: classes3.dex */
public class GenieBoxOnlineStatus implements Serializable {
    public static final int ONLINE = 1;
    public int deviceProductId;
    public int id;
    public int online;
    public String sn;
    public String state;
    public String uuid;

    public int getDeviceProductId() {
        return this.deviceProductId;
    }

    public int getId() {
        return this.id;
    }

    public int getOnline() {
        return this.online;
    }

    public String getSn() {
        return this.sn;
    }

    public String getState() {
        return this.state;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setDeviceProductId(int i) {
        this.deviceProductId = i;
    }

    public void setId(int i) {
        this.id = i;
    }

    public void setOnline(int i) {
        this.online = i;
    }

    public void setSn(String str) {
        this.sn = str;
    }

    public void setState(String str) {
        this.state = str;
    }

    public void setUuid(String str) {
        this.uuid = str;
    }
}
