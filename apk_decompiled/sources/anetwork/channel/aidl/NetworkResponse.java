package anetwork.channel.aidl;

import android.os.Parcel;
import android.os.Parcelable;
import anet.channel.util.ALog;
import anet.channel.util.ErrorConstant;
import anetwork.channel.Response;
import anetwork.channel.statist.StatisticData;
import java.util.List;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class NetworkResponse implements Parcelable, Response {
    public static final Parcelable.Creator<NetworkResponse> CREATOR = new c();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    int f1967a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    byte[] f1968b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private String f1969c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Map<String, List<String>> f1970d;
    private Throwable e;
    private StatisticData f;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public void setStatusCode(int i) {
        this.f1967a = i;
        this.f1969c = ErrorConstant.getErrMsg(i);
    }

    @Override // anetwork.channel.Response
    public byte[] getBytedata() {
        return this.f1968b;
    }

    public void setBytedata(byte[] bArr) {
        this.f1968b = bArr;
    }

    public void setConnHeadFields(Map<String, List<String>> map) {
        this.f1970d = map;
    }

    @Override // anetwork.channel.Response
    public Map<String, List<String>> getConnHeadFields() {
        return this.f1970d;
    }

    public void setDesc(String str) {
        this.f1969c = str;
    }

    @Override // anetwork.channel.Response
    public String getDesc() {
        return this.f1969c;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("NetworkResponse [");
        sb.append("statusCode=");
        sb.append(this.f1967a);
        sb.append(", desc=");
        sb.append(this.f1969c);
        sb.append(", connHeadFields=");
        sb.append(this.f1970d);
        sb.append(", bytedata=");
        byte[] bArr = this.f1968b;
        sb.append(bArr != null ? new String(bArr) : "");
        sb.append(", error=");
        sb.append(this.e);
        sb.append(", statisticData=");
        sb.append(this.f);
        sb.append("]");
        return sb.toString();
    }

    public NetworkResponse() {
    }

    public NetworkResponse(int i) {
        this.f1967a = i;
        this.f1969c = ErrorConstant.getErrMsg(i);
    }

    @Override // anetwork.channel.Response
    public int getStatusCode() {
        return this.f1967a;
    }

    @Override // anetwork.channel.Response
    public Throwable getError() {
        return this.e;
    }

    public void setError(Throwable th) {
        this.e = th;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.f1967a);
        parcel.writeString(this.f1969c);
        byte[] bArr = this.f1968b;
        int length = bArr != null ? bArr.length : 0;
        parcel.writeInt(length);
        if (length > 0) {
            parcel.writeByteArray(this.f1968b);
        }
        parcel.writeMap(this.f1970d);
        StatisticData statisticData = this.f;
        if (statisticData != null) {
            parcel.writeSerializable(statisticData);
        }
    }

    public static NetworkResponse readFromParcel(Parcel parcel) {
        NetworkResponse networkResponse = new NetworkResponse();
        try {
            networkResponse.f1967a = parcel.readInt();
            networkResponse.f1969c = parcel.readString();
            int i = parcel.readInt();
            if (i > 0) {
                networkResponse.f1968b = new byte[i];
                parcel.readByteArray(networkResponse.f1968b);
            }
            networkResponse.f1970d = parcel.readHashMap(NetworkResponse.class.getClassLoader());
            try {
                networkResponse.f = (StatisticData) parcel.readSerializable();
            } catch (Throwable unused) {
                ALog.i("anet.NetworkResponse", "[readFromParcel] source.readSerializable() error", null, new Object[0]);
            }
        } catch (Exception e) {
            ALog.w("anet.NetworkResponse", "[readFromParcel]", null, e, new Object[0]);
        }
        return networkResponse;
    }

    public void setStatisticData(StatisticData statisticData) {
        this.f = statisticData;
    }

    @Override // anetwork.channel.Response
    public StatisticData getStatisticData() {
        return this.f;
    }
}
