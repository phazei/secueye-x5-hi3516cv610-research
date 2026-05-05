package bean;

/* JADX INFO: loaded from: classes.dex */
public class BluetoothDeviceModel {
    public int DeviceModel;
    String Name = "";
    String Address = "";
    String state = "";
    boolean isConnect = false;

    public String getName() {
        return this.Name;
    }

    public void setName(String str) {
        this.Name = str;
    }

    public String getAddress() {
        return this.Address;
    }

    public void setAddress(String str) {
        this.Address = str;
    }

    public boolean isConnect() {
        return this.isConnect;
    }

    public void setConnect(boolean z) {
        this.isConnect = z;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String str) {
        this.state = str;
    }
}
