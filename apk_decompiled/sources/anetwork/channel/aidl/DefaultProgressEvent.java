package anetwork.channel.aidl;

import android.os.Parcel;
import android.os.Parcelable;
import anetwork.channel.NetworkEvent;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class DefaultProgressEvent implements Parcelable, NetworkEvent.ProgressEvent {
    public static final Parcelable.Creator<DefaultProgressEvent> CREATOR = new b();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    int f1962a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    int f1963b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    int f1964c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    Object f1965d;
    byte[] e;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // anetwork.channel.NetworkEvent.ProgressEvent
    public String getDesc() {
        return "";
    }

    public DefaultProgressEvent() {
    }

    public DefaultProgressEvent(int i, int i2, int i3, byte[] bArr) {
        this.f1962a = i;
        this.f1963b = i2;
        this.f1964c = i3;
        this.e = bArr;
    }

    @Override // anetwork.channel.NetworkEvent.ProgressEvent
    public int getSize() {
        return this.f1963b;
    }

    @Override // anetwork.channel.NetworkEvent.ProgressEvent
    public int getTotal() {
        return this.f1964c;
    }

    public Object getContext() {
        return this.f1965d;
    }

    public void setContext(Object obj) {
        this.f1965d = obj;
    }

    @Override // anetwork.channel.NetworkEvent.ProgressEvent
    public byte[] getBytedata() {
        return this.e;
    }

    @Override // anetwork.channel.NetworkEvent.ProgressEvent
    public int getIndex() {
        return this.f1962a;
    }

    public String toString() {
        return "DefaultProgressEvent [index=" + this.f1962a + ", size=" + this.f1963b + ", total=" + this.f1964c + "]";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.f1962a);
        parcel.writeInt(this.f1963b);
        parcel.writeInt(this.f1964c);
        byte[] bArr = this.e;
        parcel.writeInt(bArr != null ? bArr.length : 0);
        parcel.writeByteArray(this.e);
    }

    public static DefaultProgressEvent readFromParcel(Parcel parcel) {
        DefaultProgressEvent defaultProgressEvent = new DefaultProgressEvent();
        try {
            defaultProgressEvent.f1962a = parcel.readInt();
            defaultProgressEvent.f1963b = parcel.readInt();
            defaultProgressEvent.f1964c = parcel.readInt();
            int i = parcel.readInt();
            if (i > 0) {
                byte[] bArr = new byte[i];
                parcel.readByteArray(bArr);
                defaultProgressEvent.e = bArr;
            }
        } catch (Exception unused) {
        }
        return defaultProgressEvent;
    }
}
