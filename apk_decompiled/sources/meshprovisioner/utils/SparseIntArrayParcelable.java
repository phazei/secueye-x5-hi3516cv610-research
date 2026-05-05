package meshprovisioner.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseIntArray;

/* JADX INFO: loaded from: classes4.dex */
public class SparseIntArrayParcelable extends SparseIntArray implements Parcelable {
    public static Parcelable.Creator<SparseIntArrayParcelable> CREATOR = new Parcelable.Creator<SparseIntArrayParcelable>() { // from class: meshprovisioner.utils.SparseIntArrayParcelable.1
        @Override // android.os.Parcelable.Creator
        public SparseIntArrayParcelable createFromParcel(Parcel parcel) {
            SparseIntArrayParcelable sparseIntArrayParcelable = new SparseIntArrayParcelable();
            int i = parcel.readInt();
            int[] iArr = new int[i];
            int[] iArr2 = new int[i];
            parcel.readIntArray(iArr);
            parcel.readIntArray(iArr2);
            for (int i2 = 0; i2 < i; i2++) {
                sparseIntArrayParcelable.put(iArr[i2], iArr2[i2]);
            }
            return sparseIntArrayParcelable;
        }

        @Override // android.os.Parcelable.Creator
        public SparseIntArrayParcelable[] newArray(int i) {
            return new SparseIntArrayParcelable[i];
        }
    };

    public SparseIntArrayParcelable() {
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int[] iArr = new int[size()];
        int[] iArr2 = new int[size()];
        for (int i2 = 0; i2 < size(); i2++) {
            iArr[i2] = keyAt(i2);
            iArr2[i2] = valueAt(i2);
        }
        parcel.writeInt(size());
        parcel.writeIntArray(iArr);
        parcel.writeIntArray(iArr2);
    }

    public SparseIntArrayParcelable(SparseIntArray sparseIntArray) {
        for (int i = 0; i < sparseIntArray.size(); i++) {
            put(sparseIntArray.keyAt(i), sparseIntArray.valueAt(i));
        }
    }
}
