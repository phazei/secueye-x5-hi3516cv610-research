package aisscanner;

import android.os.ParcelUuid;
import android.util.SparseArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.ailabs.iot.aisbase.C0453s;
import com.alibaba.ailabs.iot.aisbase.C0455t;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class ScanRecord {
    public static final int DATA_TYPE_FLAGS = 1;
    public static final int DATA_TYPE_LOCAL_NAME_COMPLETE = 9;
    public static final int DATA_TYPE_LOCAL_NAME_SHORT = 8;
    public static final int DATA_TYPE_MANUFACTURER_SPECIFIC_DATA = 255;
    public static final int DATA_TYPE_MESH_BEACON = 43;
    public static final int DATA_TYPE_MESH_MESSAGE = 42;
    public static final int DATA_TYPE_SERVICE_DATA_128_BIT = 33;
    public static final int DATA_TYPE_SERVICE_DATA_16_BIT = 22;
    public static final int DATA_TYPE_SERVICE_DATA_32_BIT = 32;
    public static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_COMPLETE = 7;
    public static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_PARTIAL = 6;
    public static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_COMPLETE = 3;
    public static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_PARTIAL = 2;
    public static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_COMPLETE = 5;
    public static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_PARTIAL = 4;
    public static final int DATA_TYPE_TX_POWER_LEVEL = 10;
    public static final int DATA_TYPE_UNPROVISIONED_DEVICE_BEACON = 0;
    public static final String MESH_PROVISIONING_UUID = "00001827-0000-1000-8000-00805F9B34FB";
    public static final String TAG = "ScanRecord";
    public final int advertiseFlags;
    public final byte[] bytes;
    public final String deviceName;

    @Nullable
    public final SparseArray<byte[]> manufacturerSpecificData;
    public final byte[] meshNetworkPUD;

    @Nullable
    public final Map<ParcelUuid, byte[]> serviceData;

    @Nullable
    public final List<ParcelUuid> serviceUuids;
    public final int txPowerLevel;

    public ScanRecord(@Nullable List<ParcelUuid> list, @Nullable SparseArray<byte[]> sparseArray, @Nullable Map<ParcelUuid, byte[]> map, int i, int i2, String str, byte[] bArr, byte[] bArr2) {
        this.serviceUuids = list;
        this.manufacturerSpecificData = sparseArray;
        this.serviceData = map;
        this.deviceName = str;
        this.advertiseFlags = i;
        this.txPowerLevel = i2;
        this.bytes = bArr2;
        this.meshNetworkPUD = bArr;
    }

    public static byte[] extractBytes(@NonNull byte[] bArr, int i, int i2) {
        byte[] bArr2 = new byte[i2];
        System.arraycopy(bArr, i, bArr2, 0, i2);
        return bArr2;
    }

    /* JADX WARN: Removed duplicated region for block: B:52:0x00e3  */
    @androidx.annotation.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static aisscanner.ScanRecord parseFromBytes(@androidx.annotation.Nullable byte[] r17) {
        /*
            Method dump skipped, instruction units count: 350
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: aisscanner.ScanRecord.parseFromBytes(byte[]):aisscanner.ScanRecord");
    }

    public static int parseServiceUuid(@NonNull byte[] bArr, int i, int i2, int i3, @NonNull List<ParcelUuid> list) {
        while (i2 > 0) {
            list.add(C0455t.a(extractBytes(bArr, i, i3)));
            i2 -= i3;
            i += i3;
        }
        return i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || ScanRecord.class != obj.getClass()) {
            return false;
        }
        return Arrays.equals(this.bytes, ((ScanRecord) obj).bytes);
    }

    public int getAdvertiseFlags() {
        return this.advertiseFlags;
    }

    @Nullable
    public byte[] getBytes() {
        return this.bytes;
    }

    @Nullable
    public String getDeviceName() {
        return this.deviceName;
    }

    @Nullable
    public SparseArray<byte[]> getManufacturerSpecificData() {
        return this.manufacturerSpecificData;
    }

    public byte[] getMeshNetworkPUD() {
        return this.meshNetworkPUD;
    }

    @Nullable
    public Map<ParcelUuid, byte[]> getServiceData() {
        return this.serviceData;
    }

    @Nullable
    public List<ParcelUuid> getServiceUuids() {
        return this.serviceUuids;
    }

    public int getTxPowerLevel() {
        return this.txPowerLevel;
    }

    public String toString() {
        return "ScanRecord [advertiseFlags=" + this.advertiseFlags + ", serviceUuids=" + this.serviceUuids + ", manufacturerSpecificData=" + C0453s.a(this.manufacturerSpecificData) + ", serviceData=" + C0453s.a(this.serviceData) + ", txPowerLevel=" + this.txPowerLevel + ", deviceName=" + this.deviceName + "]";
    }

    @Nullable
    public byte[] getManufacturerSpecificData(int i) {
        SparseArray<byte[]> sparseArray = this.manufacturerSpecificData;
        if (sparseArray == null) {
            return null;
        }
        return sparseArray.get(i);
    }

    @Nullable
    public byte[] getServiceData(@NonNull ParcelUuid parcelUuid) {
        Map<ParcelUuid, byte[]> map;
        if (parcelUuid == null || (map = this.serviceData) == null) {
            return null;
        }
        return map.get(parcelUuid);
    }
}
