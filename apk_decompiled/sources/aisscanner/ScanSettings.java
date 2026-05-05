package aisscanner;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.C0463x;

/* JADX INFO: loaded from: classes.dex */
public final class ScanSettings implements Parcelable {
    public static final int CALLBACK_TYPE_ALL_MATCHES = 1;
    public static final int CALLBACK_TYPE_FIRST_MATCH = 2;
    public static final int CALLBACK_TYPE_MATCH_LOST = 4;
    public static final Parcelable.Creator<ScanSettings> CREATOR = new C0463x();
    public static final long MATCH_LOST_DEVICE_TIMEOUT_DEFAULT = 10000;
    public static final long MATCH_LOST_TASK_INTERVAL_DEFAULT = 10000;
    public static final int MATCH_MODE_AGGRESSIVE = 1;
    public static final int MATCH_MODE_STICKY = 2;
    public static final int MATCH_NUM_FEW_ADVERTISEMENT = 2;
    public static final int MATCH_NUM_MAX_ADVERTISEMENT = 3;
    public static final int MATCH_NUM_ONE_ADVERTISEMENT = 1;
    public static final int PHY_LE_ALL_SUPPORTED = 255;
    public static final int SCAN_MODE_BALANCED = 1;
    public static final int SCAN_MODE_LOW_LATENCY = 2;
    public static final int SCAN_MODE_LOW_POWER = 0;
    public static final int SCAN_MODE_OPPORTUNISTIC = -1;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final long f1599a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final long f1600b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public int f1601c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public int f1602d;
    public long e;
    public int f;
    public int g;
    public boolean h;
    public boolean i;
    public boolean j;
    public long k;
    public long l;
    public boolean m;
    public int n;

    public static final class Builder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public int f1603a = 0;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public int f1604b = 1;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public long f1605c = 0;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public int f1606d = 1;
        public int e = 3;
        public boolean f = true;
        public int g = 255;
        public boolean h = true;
        public boolean i = true;
        public boolean j = true;
        public long k = 10000;
        public long l = 10000;
        public long m = 0;
        public long n = 0;

        public final boolean a(int i) {
            return i == 1 || i == 2 || i == 4 || i == 6;
        }

        @NonNull
        public ScanSettings build() {
            return new ScanSettings(this.f1603a, this.f1604b, this.f1605c, this.f1606d, this.e, this.f, this.g, this.h, this.i, this.j, this.k, this.l, this.n, this.m, null);
        }

        @NonNull
        public Builder setCallbackType(int i) {
            if (a(i)) {
                this.f1604b = i;
                return this;
            }
            throw new IllegalArgumentException("invalid callback type - " + i);
        }

        @NonNull
        public Builder setLegacy(boolean z) {
            this.f = z;
            return this;
        }

        @NonNull
        public Builder setMatchMode(int i) {
            if (i >= 1 && i <= 2) {
                this.f1606d = i;
                return this;
            }
            throw new IllegalArgumentException("invalid matchMode " + i);
        }

        @NonNull
        public Builder setMatchOptions(long j, long j2) {
            if (j <= 0 || j2 <= 0) {
                throw new IllegalArgumentException("maxDeviceAgeMillis and taskIntervalMillis must be > 0");
            }
            this.k = j;
            this.l = j2;
            return this;
        }

        @NonNull
        public Builder setNumOfMatches(int i) {
            if (i >= 1 && i <= 3) {
                this.e = i;
                return this;
            }
            throw new IllegalArgumentException("invalid numOfMatches " + i);
        }

        @NonNull
        public Builder setPhy(int i) {
            this.g = i;
            return this;
        }

        @NonNull
        public Builder setPowerSave(long j, long j2) {
            if (j <= 0 || j2 <= 0) {
                throw new IllegalArgumentException("scanInterval and restInterval must be > 0");
            }
            this.n = j;
            this.m = j2;
            return this;
        }

        @NonNull
        public Builder setReportDelay(long j) {
            if (j < 0) {
                throw new IllegalArgumentException("reportDelay must be > 0");
            }
            this.f1605c = j;
            return this;
        }

        @NonNull
        public Builder setScanMode(int i) {
            if (i >= -1 && i <= 2) {
                this.f1603a = i;
                return this;
            }
            throw new IllegalArgumentException("invalid scan mode " + i);
        }

        @NonNull
        public Builder setUseHardwareBatchingIfSupported(boolean z) {
            this.i = z;
            return this;
        }

        @NonNull
        public Builder setUseHardwareCallbackTypesIfSupported(boolean z) {
            this.j = z;
            return this;
        }

        @NonNull
        public Builder setUseHardwareFilteringIfSupported(boolean z) {
            this.h = z;
            return this;
        }
    }

    public /* synthetic */ ScanSettings(int i, int i2, long j, int i3, int i4, boolean z, int i5, boolean z2, boolean z3, boolean z4, long j2, long j3, long j4, long j5, C0463x c0463x) {
        this(i, i2, j, i3, i4, z, i5, z2, z3, z4, j2, j3, j4, j5);
    }

    public void a() {
        this.j = false;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getCallbackType() {
        return this.f1602d;
    }

    public boolean getLegacy() {
        return this.m;
    }

    public long getMatchLostDeviceTimeout() {
        return this.k;
    }

    public long getMatchLostTaskInterval() {
        return this.l;
    }

    public int getMatchMode() {
        return this.f;
    }

    public int getNumOfMatches() {
        return this.g;
    }

    public int getPhy() {
        return this.n;
    }

    public long getPowerSaveRest() {
        return this.f1600b;
    }

    public long getPowerSaveScan() {
        return this.f1599a;
    }

    public long getReportDelayMillis() {
        return this.e;
    }

    public int getScanMode() {
        return this.f1601c;
    }

    public boolean getUseHardwareBatchingIfSupported() {
        return this.i;
    }

    public boolean getUseHardwareCallbackTypesIfSupported() {
        return this.j;
    }

    public boolean getUseHardwareFilteringIfSupported() {
        return this.h;
    }

    public boolean hasPowerSaveMode() {
        return this.f1600b > 0 && this.f1599a > 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.f1601c);
        parcel.writeInt(this.f1602d);
        parcel.writeLong(this.e);
        parcel.writeInt(this.f);
        parcel.writeInt(this.g);
        parcel.writeInt(this.m ? 1 : 0);
        parcel.writeInt(this.n);
        parcel.writeInt(this.h ? 1 : 0);
        parcel.writeInt(this.i ? 1 : 0);
        parcel.writeLong(this.f1599a);
        parcel.writeLong(this.f1600b);
    }

    public /* synthetic */ ScanSettings(Parcel parcel, C0463x c0463x) {
        this(parcel);
    }

    public ScanSettings(int i, int i2, long j, int i3, int i4, boolean z, int i5, boolean z2, boolean z3, boolean z4, long j2, long j3, long j4, long j5) {
        this.f1601c = i;
        this.f1602d = i2;
        this.e = j;
        this.g = i4;
        this.f = i3;
        this.m = z;
        this.n = i5;
        this.h = z2;
        this.i = z3;
        this.j = z4;
        this.k = 1000000 * j2;
        this.l = j3;
        this.f1599a = j4;
        this.f1600b = j5;
    }

    public ScanSettings(Parcel parcel) {
        this.f1601c = parcel.readInt();
        this.f1602d = parcel.readInt();
        this.e = parcel.readLong();
        this.f = parcel.readInt();
        this.g = parcel.readInt();
        this.m = parcel.readInt() != 0;
        this.n = parcel.readInt();
        this.h = parcel.readInt() == 1;
        this.i = parcel.readInt() == 1;
        this.f1599a = parcel.readLong();
        this.f1600b = parcel.readLong();
    }
}
