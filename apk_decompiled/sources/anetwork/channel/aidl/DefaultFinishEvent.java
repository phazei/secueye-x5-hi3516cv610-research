package anetwork.channel.aidl;

import android.os.Parcel;
import android.os.Parcelable;
import anet.channel.request.Request;
import anet.channel.statist.RequestStatistic;
import anet.channel.util.ErrorConstant;
import anetwork.channel.NetworkEvent;
import anetwork.channel.statist.StatisticData;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class DefaultFinishEvent implements Parcelable, NetworkEvent.FinishEvent {
    public static final Parcelable.Creator<DefaultFinishEvent> CREATOR = new a();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    Object f1958a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    int f1959b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    String f1960c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    StatisticData f1961d;
    public final Request request;
    public final RequestStatistic rs;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Object getContext() {
        return this.f1958a;
    }

    public void setContext(Object obj) {
        this.f1958a = obj;
    }

    @Override // anetwork.channel.NetworkEvent.FinishEvent
    public int getHttpCode() {
        return this.f1959b;
    }

    @Override // anetwork.channel.NetworkEvent.FinishEvent
    public String getDesc() {
        return this.f1960c;
    }

    @Override // anetwork.channel.NetworkEvent.FinishEvent
    public StatisticData getStatisticData() {
        return this.f1961d;
    }

    public DefaultFinishEvent(int i) {
        this(i, null, null, null);
    }

    public DefaultFinishEvent(int i, String str, RequestStatistic requestStatistic) {
        this(i, str, null, requestStatistic);
    }

    public DefaultFinishEvent(int i, String str, Request request) {
        this(i, str, request, request != null ? request.f1794a : null);
    }

    private DefaultFinishEvent(int i, String str, Request request, RequestStatistic requestStatistic) {
        this.f1961d = new StatisticData();
        this.f1959b = i;
        this.f1960c = str == null ? ErrorConstant.getErrMsg(i) : str;
        this.request = request;
        this.rs = requestStatistic;
    }

    public String toString() {
        return "DefaultFinishEvent [code=" + this.f1959b + ", desc=" + this.f1960c + ", context=" + this.f1958a + ", statisticData=" + this.f1961d + "]";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.f1959b);
        parcel.writeString(this.f1960c);
        StatisticData statisticData = this.f1961d;
        if (statisticData != null) {
            parcel.writeSerializable(statisticData);
        }
    }

    static DefaultFinishEvent a(Parcel parcel) {
        DefaultFinishEvent defaultFinishEvent = new DefaultFinishEvent(0);
        try {
            defaultFinishEvent.f1959b = parcel.readInt();
            defaultFinishEvent.f1960c = parcel.readString();
            defaultFinishEvent.f1961d = (StatisticData) parcel.readSerializable();
        } catch (Throwable unused) {
        }
        return defaultFinishEvent;
    }
}
