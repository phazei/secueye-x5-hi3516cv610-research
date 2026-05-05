package aisble.data;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import anet.channel.entity.EventType;

/* JADX INFO: loaded from: classes.dex */
public class MutableData extends Data {
    public static final int FLOAT_EXPONENT_MAX = 127;
    public static final int FLOAT_EXPONENT_MIN = -128;
    public static final int FLOAT_MANTISSA_MAX = 8388605;
    public static final int FLOAT_NAN = 8388607;
    public static final int FLOAT_NEGATIVE_INFINITY = 8388610;
    public static final int FLOAT_POSITIVE_INFINITY = 8388606;
    public static final int FLOAT_PRECISION = 10000000;
    public static final int SFLOAT_EXPONENT_MAX = 7;
    public static final int SFLOAT_EXPONENT_MIN = -8;
    public static final int SFLOAT_MANTISSA_MAX = 2045;
    public static final float SFLOAT_MAX = 2.045E10f;
    public static final float SFLOAT_MIN = -2.045E10f;
    public static final int SFLOAT_NAN = 2047;
    public static final int SFLOAT_NEGATIVE_INFINITY = 2050;
    public static final int SFLOAT_POSITIVE_INFINITY = 2046;
    public static final int SFLOAT_PRECISION = 10000;

    public MutableData() {
    }

    public static int floatToInt(float f) {
        if (Float.isNaN(f)) {
            return FLOAT_NAN;
        }
        if (f == Float.POSITIVE_INFINITY) {
            return FLOAT_POSITIVE_INFINITY;
        }
        if (f == Float.NEGATIVE_INFINITY) {
            return FLOAT_NEGATIVE_INFINITY;
        }
        int i = f >= 0.0f ? 1 : -1;
        float fAbs = Math.abs(f);
        int i2 = 0;
        while (fAbs > 8388605.0f) {
            fAbs /= 10.0f;
            i2++;
            if (i2 > 127) {
                return i > 0 ? FLOAT_POSITIVE_INFINITY : FLOAT_NEGATIVE_INFINITY;
            }
        }
        while (fAbs < 1.0f) {
            fAbs *= 10.0f;
            i2--;
            if (i2 < -128) {
                return 0;
            }
        }
        double dAbs = Math.abs(((double) Math.round(fAbs * 1.0E7f)) - ((double) (Math.round(fAbs) * 10000000)));
        while (dAbs > 0.5d && i2 > -128) {
            float f2 = fAbs * 10.0f;
            if (f2 > 8388605.0f) {
                break;
            }
            i2--;
            dAbs = Math.abs(((double) Math.round(f2 * 1.0E7f)) - ((double) (Math.round(f2) * 10000000)));
            fAbs = f2;
        }
        return (Math.round(i * fAbs) & 16777215) | (i2 << 24);
    }

    public static MutableData from(@NonNull BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return new MutableData(bluetoothGattCharacteristic.getValue());
    }

    public static int intToSignedBits(int i, int i2) {
        if (i >= 0) {
            return i;
        }
        int i3 = 1 << (i2 - 1);
        return (i & (i3 - 1)) + i3;
    }

    public static long longToSignedBits(long j, int i) {
        if (j >= 0) {
            return j;
        }
        long j2 = 1 << (i - 1);
        return (j & (j2 - 1)) + j2;
    }

    public static int sfloatToInt(float f) {
        if (Float.isNaN(f)) {
            return SFLOAT_NAN;
        }
        if (f > 2.045E10f) {
            return SFLOAT_POSITIVE_INFINITY;
        }
        if (f < -2.045E10f) {
            return 2050;
        }
        int i = f >= 0.0f ? 1 : -1;
        float fAbs = Math.abs(f);
        int i2 = 0;
        while (fAbs > 2045.0f) {
            fAbs /= 10.0f;
            i2++;
            if (i2 > 7) {
                if (i > 0) {
                    return SFLOAT_POSITIVE_INFINITY;
                }
                return 2050;
            }
        }
        while (fAbs < 1.0f) {
            fAbs *= 10.0f;
            i2--;
            if (i2 < -8) {
                return 0;
            }
        }
        double dAbs = Math.abs(((double) Math.round(fAbs * 10000.0f)) - ((double) (Math.round(fAbs) * 10000)));
        while (dAbs > 0.5d && i2 > -8) {
            float f2 = fAbs * 10.0f;
            if (f2 > 2045.0f) {
                break;
            }
            i2--;
            dAbs = Math.abs(((double) Math.round(f2 * 10000.0f)) - ((double) (Math.round(f2) * 10000)));
            fAbs = f2;
        }
        return (Math.round(i * fAbs) & EventType.ALL) | ((i2 & 15) << 12);
    }

    public boolean setByte(int i, @IntRange(from = 0) int i2) {
        int i3 = i2 + 1;
        if (this.mValue == null) {
            this.mValue = new byte[i3];
        }
        byte[] bArr = this.mValue;
        if (i3 > bArr.length) {
            return false;
        }
        bArr[i2] = (byte) i;
        return true;
    }

    public boolean setValue(@Nullable byte[] bArr) {
        this.mValue = bArr;
        return true;
    }

    public MutableData(@Nullable byte[] bArr) {
        super(bArr);
    }

    public static MutableData from(@NonNull BluetoothGattDescriptor bluetoothGattDescriptor) {
        return new MutableData(bluetoothGattDescriptor.getValue());
    }

    public boolean setValue(int i, int i2, @IntRange(from = 0) int i3) {
        int typeLen = Data.getTypeLen(i2) + i3;
        if (this.mValue == null) {
            this.mValue = new byte[typeLen];
        }
        if (typeLen > this.mValue.length) {
            return false;
        }
        switch (i2) {
            case 17:
                this.mValue[i3] = (byte) (i & 255);
                break;
            case 18:
                byte[] bArr = this.mValue;
                bArr[i3] = (byte) (i & 255);
                bArr[i3 + 1] = (byte) ((i >> 8) & 255);
                break;
            case 19:
                byte[] bArr2 = this.mValue;
                int i4 = i3 + 1;
                bArr2[i3] = (byte) (i & 255);
                bArr2[i4] = (byte) ((i >> 8) & 255);
                bArr2[i4 + 1] = (byte) ((i >> 16) & 255);
                break;
            case 20:
                byte[] bArr3 = this.mValue;
                int i5 = i3 + 1;
                bArr3[i3] = (byte) (i & 255);
                int i6 = i5 + 1;
                bArr3[i5] = (byte) ((i >> 8) & 255);
                bArr3[i6] = (byte) ((i >> 16) & 255);
                bArr3[i6 + 1] = (byte) ((i >> 24) & 255);
                break;
            default:
                switch (i2) {
                    case 33:
                        i = intToSignedBits(i, 8);
                        this.mValue[i3] = (byte) (i & 255);
                        break;
                    case 34:
                        i = intToSignedBits(i, 16);
                        byte[] bArr4 = this.mValue;
                        bArr4[i3] = (byte) (i & 255);
                        bArr4[i3 + 1] = (byte) ((i >> 8) & 255);
                        break;
                    case 35:
                        i = intToSignedBits(i, 24);
                        byte[] bArr22 = this.mValue;
                        int i42 = i3 + 1;
                        bArr22[i3] = (byte) (i & 255);
                        bArr22[i42] = (byte) ((i >> 8) & 255);
                        bArr22[i42 + 1] = (byte) ((i >> 16) & 255);
                        break;
                    case 36:
                        i = intToSignedBits(i, 32);
                        byte[] bArr32 = this.mValue;
                        int i52 = i3 + 1;
                        bArr32[i3] = (byte) (i & 255);
                        int i62 = i52 + 1;
                        bArr32[i52] = (byte) ((i >> 8) & 255);
                        bArr32[i62] = (byte) ((i >> 16) & 255);
                        bArr32[i62 + 1] = (byte) ((i >> 24) & 255);
                        break;
                }
                break;
        }
        return false;
    }

    public boolean setValue(int i, int i2, int i3, @IntRange(from = 0) int i4) {
        int typeLen = Data.getTypeLen(i3) + i4;
        if (this.mValue == null) {
            this.mValue = new byte[typeLen];
        }
        if (typeLen > this.mValue.length) {
            return false;
        }
        if (i3 == 50) {
            int iIntToSignedBits = intToSignedBits(i, 12);
            int iIntToSignedBits2 = intToSignedBits(i2, 4);
            byte[] bArr = this.mValue;
            int i5 = i4 + 1;
            bArr[i4] = (byte) (iIntToSignedBits & 255);
            bArr[i5] = (byte) ((iIntToSignedBits >> 8) & 15);
            bArr[i5] = (byte) (bArr[i5] + ((byte) ((iIntToSignedBits2 & 15) << 4)));
            return true;
        }
        if (i3 != 52) {
            return false;
        }
        int iIntToSignedBits3 = intToSignedBits(i, 24);
        int iIntToSignedBits4 = intToSignedBits(i2, 8);
        byte[] bArr2 = this.mValue;
        int i6 = i4 + 1;
        bArr2[i4] = (byte) (iIntToSignedBits3 & 255);
        int i7 = i6 + 1;
        bArr2[i6] = (byte) ((iIntToSignedBits3 >> 8) & 255);
        int i8 = i7 + 1;
        bArr2[i7] = (byte) ((iIntToSignedBits3 >> 16) & 255);
        bArr2[i8] = (byte) (bArr2[i8] + ((byte) (iIntToSignedBits4 & 255)));
        return true;
    }

    public boolean setValue(long j, int i, @IntRange(from = 0) int i2) {
        int typeLen = Data.getTypeLen(i) + i2;
        if (this.mValue == null) {
            this.mValue = new byte[typeLen];
        }
        if (typeLen > this.mValue.length) {
            return false;
        }
        if (i != 20) {
            if (i != 36) {
                return false;
            }
            j = longToSignedBits(j, 32);
        }
        byte[] bArr = this.mValue;
        int i3 = i2 + 1;
        bArr[i2] = (byte) (j & 255);
        int i4 = i3 + 1;
        bArr[i3] = (byte) ((j >> 8) & 255);
        bArr[i4] = (byte) ((j >> 16) & 255);
        bArr[i4 + 1] = (byte) ((j >> 24) & 255);
        return true;
    }

    public boolean setValue(float f, int i, @IntRange(from = 0) int i2) {
        int typeLen = Data.getTypeLen(i) + i2;
        if (this.mValue == null) {
            this.mValue = new byte[typeLen];
        }
        if (typeLen > this.mValue.length) {
            return false;
        }
        if (i == 50) {
            int iSfloatToInt = sfloatToInt(f);
            byte[] bArr = this.mValue;
            bArr[i2] = (byte) (iSfloatToInt & 255);
            bArr[i2 + 1] = (byte) ((iSfloatToInt >> 8) & 255);
            return true;
        }
        if (i != 52) {
            return false;
        }
        int iFloatToInt = floatToInt(f);
        byte[] bArr2 = this.mValue;
        int i3 = i2 + 1;
        bArr2[i2] = (byte) (iFloatToInt & 255);
        int i4 = i3 + 1;
        bArr2[i3] = (byte) ((iFloatToInt >> 8) & 255);
        int i5 = i4 + 1;
        bArr2[i4] = (byte) ((iFloatToInt >> 16) & 255);
        bArr2[i5] = (byte) (bArr2[i5] + ((byte) ((iFloatToInt >> 24) & 255)));
        return true;
    }
}
