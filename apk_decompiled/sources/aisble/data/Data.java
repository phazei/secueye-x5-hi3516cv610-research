package aisble.data;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.ailabs.iot.aisbase.C0417a;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.apache.commons.codec.language.Soundex;

/* JADX INFO: loaded from: classes.dex */
public class Data implements Parcelable {
    public static final int FORMAT_FLOAT = 52;
    public static final int FORMAT_SFLOAT = 50;
    public static final int FORMAT_SINT16 = 34;
    public static final int FORMAT_SINT24 = 35;
    public static final int FORMAT_SINT32 = 36;
    public static final int FORMAT_SINT8 = 33;
    public static final int FORMAT_UINT16 = 18;
    public static final int FORMAT_UINT24 = 19;
    public static final int FORMAT_UINT32 = 20;
    public static final int FORMAT_UINT8 = 17;
    public byte[] mValue;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static char[] f1588a = "0123456789ABCDEF".toCharArray();
    public static final Parcelable.Creator<Data> CREATOR = new C0417a();

    @Retention(RetentionPolicy.SOURCE)
    public @interface FloatFormat {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface IntFormat {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LongFormat {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ValueFormat {
    }

    public Data() {
        this.mValue = null;
    }

    public static float a(byte b2, byte b3) {
        return (float) (((double) a(a(b2) + ((a(b3) & 15) << 8), 12)) * Math.pow(10.0d, a(a(b3) >> 4, 4)));
    }

    public static int a(byte b2) {
        return b2 & 255;
    }

    public static int a(int i, int i2) {
        int i3 = 1 << (i2 - 1);
        return (i & i3) != 0 ? (i3 - (i & (i3 - 1))) * (-1) : i;
    }

    public static long a(long j, int i) {
        int i2 = 1 << (i - 1);
        long j2 = i2;
        return (j & j2) != 0 ? (-1) * (j2 - (j & ((long) (i2 - 1)))) : j;
    }

    public static int b(byte b2, byte b3) {
        return a(b2) + (a(b3) << 8);
    }

    public static long b(byte b2) {
        return ((long) b2) & 255;
    }

    public static long c(byte b2, byte b3, byte b4, byte b5) {
        return b(b2) + (b(b3) << 8) + (b(b4) << 16) + (b(b5) << 24);
    }

    public static Data from(@NonNull String str) {
        return new Data(str.getBytes());
    }

    public static int getTypeLen(int i) {
        return i & 15;
    }

    public static Data opCode(byte b2) {
        return new Data(new byte[]{b2});
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Nullable
    public Byte getByte(@IntRange(from = 0) int i) {
        if (i + 1 > size()) {
            return null;
        }
        return Byte.valueOf(this.mValue[i]);
    }

    @Nullable
    public Float getFloatValue(int i, @IntRange(from = 0) int i2) {
        if (getTypeLen(i) + i2 > size()) {
            return null;
        }
        if (i == 50) {
            byte[] bArr = this.mValue;
            int i3 = i2 + 1;
            if (bArr[i3] == 7 && bArr[i2] == -2) {
                return Float.valueOf(Float.POSITIVE_INFINITY);
            }
            byte[] bArr2 = this.mValue;
            if (bArr2[i3] != 7 || bArr2[i2] != -1) {
                byte[] bArr3 = this.mValue;
                if (bArr3[i3] != 8 || bArr3[i2] != 0) {
                    byte[] bArr4 = this.mValue;
                    if (bArr4[i3] != 8 || bArr4[i2] != 1) {
                        byte[] bArr5 = this.mValue;
                        if (bArr5[i3] == 8 && bArr5[i2] == 2) {
                            return Float.valueOf(Float.NEGATIVE_INFINITY);
                        }
                        byte[] bArr6 = this.mValue;
                        return Float.valueOf(a(bArr6[i2], bArr6[i3]));
                    }
                }
            }
            return Float.valueOf(Float.NaN);
        }
        if (i != 52) {
            return null;
        }
        byte[] bArr7 = this.mValue;
        int i4 = i2 + 3;
        if (bArr7[i4] == 0) {
            int i5 = i2 + 2;
            if (bArr7[i5] != 127 || bArr7[i2 + 1] != -1) {
                byte[] bArr8 = this.mValue;
                if (bArr8[i5] == -128 && bArr8[i2 + 1] == 0) {
                    if (bArr8[i2] == 0 || bArr8[i2] == 1) {
                        return Float.valueOf(Float.NaN);
                    }
                    if (bArr8[i2] == 2) {
                        return Float.valueOf(Float.NEGATIVE_INFINITY);
                    }
                }
            } else {
                if (bArr7[i2] == -2) {
                    return Float.valueOf(Float.POSITIVE_INFINITY);
                }
                if (bArr7[i2] == -1) {
                    return Float.valueOf(Float.NaN);
                }
            }
        }
        byte[] bArr9 = this.mValue;
        return Float.valueOf(a(bArr9[i2], bArr9[i2 + 1], bArr9[i2 + 2], bArr9[i4]));
    }

    @Nullable
    public Integer getIntValue(int i, @IntRange(from = 0) int i2) {
        if (getTypeLen(i) + i2 > size()) {
            return null;
        }
        switch (i) {
            case 17:
                break;
            case 18:
                byte[] bArr = this.mValue;
                break;
            case 19:
                byte[] bArr2 = this.mValue;
                break;
            case 20:
                byte[] bArr3 = this.mValue;
                break;
            default:
                switch (i) {
                    case 34:
                        byte[] bArr4 = this.mValue;
                        break;
                    case 35:
                        byte[] bArr5 = this.mValue;
                        break;
                    case 36:
                        byte[] bArr6 = this.mValue;
                        break;
                }
                break;
        }
        return null;
    }

    @Nullable
    public Long getLongValue(int i, @IntRange(from = 0) int i2) {
        if (getTypeLen(i) + i2 > size()) {
            return null;
        }
        if (i == 20) {
            byte[] bArr = this.mValue;
            return Long.valueOf(c(bArr[i2], bArr[i2 + 1], bArr[i2 + 2], bArr[i2 + 3]));
        }
        if (i != 36) {
            return null;
        }
        byte[] bArr2 = this.mValue;
        return Long.valueOf(a(c(bArr2[i2], bArr2[i2 + 1], bArr2[i2 + 2], bArr2[i2 + 3]), 32));
    }

    @Nullable
    public String getStringValue(@IntRange(from = 0) int i) {
        byte[] bArr = this.mValue;
        if (bArr == null || i > bArr.length) {
            return null;
        }
        byte[] bArr2 = new byte[bArr.length - i];
        int i2 = 0;
        while (true) {
            byte[] bArr3 = this.mValue;
            if (i2 == bArr3.length - i) {
                return new String(bArr2);
            }
            bArr2[i2] = bArr3[i + i2];
            i2++;
        }
    }

    @Nullable
    public byte[] getValue() {
        return this.mValue;
    }

    public int size() {
        byte[] bArr = this.mValue;
        if (bArr != null) {
            return bArr.length;
        }
        return 0;
    }

    public String toString() {
        if (size() == 0) {
            return "";
        }
        char[] cArr = new char[(this.mValue.length * 3) - 1];
        int i = 0;
        while (true) {
            byte[] bArr = this.mValue;
            if (i >= bArr.length) {
                return "(0x) " + new String(cArr);
            }
            int i2 = bArr[i] & 255;
            int i3 = i * 3;
            char[] cArr2 = f1588a;
            cArr[i3] = cArr2[i2 >>> 4];
            cArr[i3 + 1] = cArr2[i2 & 15];
            if (i != bArr.length - 1) {
                cArr[i3 + 2] = Soundex.SILENT_MARKER;
            }
            i++;
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByteArray(this.mValue);
    }

    public static int b(byte b2, byte b3, byte b4, byte b5) {
        return a(b2) + (a(b3) << 8) + (a(b4) << 16) + (a(b5) << 24);
    }

    public static Data from(@NonNull BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return new Data(bluetoothGattCharacteristic.getValue());
    }

    public static Data opCode(byte b2, byte b3) {
        return new Data(new byte[]{b2, b3});
    }

    public Data(@Nullable byte[] bArr) {
        this.mValue = bArr;
    }

    public static Data from(@NonNull BluetoothGattDescriptor bluetoothGattDescriptor) {
        return new Data(bluetoothGattDescriptor.getValue());
    }

    public Data(Parcel parcel) {
        this.mValue = parcel.createByteArray();
    }

    public static float a(byte b2, byte b3, byte b4, byte b5) {
        return (float) (((double) a(a(b2) + (a(b3) << 8) + (a(b4) << 16), 24)) * Math.pow(10.0d, b5));
    }
}
