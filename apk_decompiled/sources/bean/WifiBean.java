package bean;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class WifiBean implements Parcelable {
    public static final Parcelable.Creator<WifiBean> CREATOR = new Parcelable.Creator<WifiBean>() { // from class: bean.WifiBean.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiBean createFromParcel(Parcel parcel) {
            return new WifiBean(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiBean[] newArray(int i) {
            return new WifiBean[i];
        }
    };
    private boolean currentWifi;
    private String passwd;
    private int quality;
    private String ssid;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    protected WifiBean(Parcel parcel) {
        this.passwd = parcel.readString();
        this.currentWifi = parcel.readByte() != 0;
        this.ssid = parcel.readString();
        this.quality = parcel.readInt();
    }

    public WifiBean() {
    }

    public String getPasswd() {
        return this.passwd;
    }

    public void setPasswd(String str) {
        this.passwd = str;
    }

    public boolean isCurrentWifi() {
        return this.currentWifi;
    }

    public void setCurrentWifi(boolean z) {
        this.currentWifi = z;
    }

    public String getSsid() {
        return this.ssid;
    }

    public void setSsid(String str) {
        this.ssid = str;
    }

    public int getQuality() {
        return this.quality;
    }

    public void setQuality(int i) {
        this.quality = i;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.passwd);
        parcel.writeByte(this.currentWifi ? (byte) 1 : (byte) 0);
        parcel.writeString(this.ssid);
        parcel.writeInt(this.quality);
    }
}
