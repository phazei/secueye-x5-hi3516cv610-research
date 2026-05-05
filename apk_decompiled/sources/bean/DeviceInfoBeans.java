package bean;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DeviceInfoBeans {
    private DeviceInfoBean cellDeviceInfoBean;
    private List<DeviceInfoBean> data;
    public String iccid;
    public String id;
    public String imei;
    public String name;
    private String nvrIot;
    private int pos;
    private int type;

    public String getNvrIot() {
        return this.nvrIot;
    }

    public void setNvrIot(String str) {
        this.nvrIot = str;
    }

    public DeviceInfoBeans() {
        this.data = new ArrayList();
        this.pos = -1;
        this.nvrIot = "";
        this.imei = "";
        this.iccid = "";
        this.name = "";
        this.id = "";
        this.type = 0;
    }

    public DeviceInfoBeans(List<DeviceInfoBean> list) {
        this.data = new ArrayList();
        this.pos = -1;
        this.nvrIot = "";
        this.imei = "";
        this.iccid = "";
        this.name = "";
        this.id = "";
        this.type = 0;
        this.data = list;
    }

    public DeviceInfoBeans(List<DeviceInfoBean> list, int i) {
        this.data = new ArrayList();
        this.pos = -1;
        this.nvrIot = "";
        this.imei = "";
        this.iccid = "";
        this.name = "";
        this.id = "";
        this.type = 0;
        this.data = list;
        this.pos = i;
    }

    public List<DeviceInfoBean> getData() {
        return this.data;
    }

    public int getPos() {
        return this.pos;
    }

    public void setPos(int i) {
        this.pos = i;
    }

    public DeviceInfoBean getCellDeviceInfoBean() {
        return this.cellDeviceInfoBean;
    }

    public void setCellDeviceInfoBean(DeviceInfoBean deviceInfoBean) {
        this.cellDeviceInfoBean = deviceInfoBean;
    }
}
