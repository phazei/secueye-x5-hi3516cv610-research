package anet.channel.request;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
final class a implements Parcelable.Creator<ByteArrayEntry> {
    a() {
    }

    @Override // android.os.Parcelable.Creator
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public ByteArrayEntry createFromParcel(Parcel parcel) {
        ByteArrayEntry byteArrayEntry = new ByteArrayEntry((a) null);
        byteArrayEntry.bytes = new byte[parcel.readInt()];
        parcel.readByteArray(byteArrayEntry.bytes);
        byteArrayEntry.offset = parcel.readInt();
        byteArrayEntry.count = parcel.readInt();
        byteArrayEntry.contentType = parcel.readString();
        return byteArrayEntry;
    }

    @Override // android.os.Parcelable.Creator
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public ByteArrayEntry[] newArray(int i) {
        return new ByteArrayEntry[i];
    }
}
