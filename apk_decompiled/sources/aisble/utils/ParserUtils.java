package aisble.utils;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import org.apache.commons.codec.language.Soundex;

/* JADX INFO: loaded from: classes.dex */
public class ParserUtils {
    public static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String parse(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return parse(bluetoothGattCharacteristic.getValue());
    }

    public static String parse(BluetoothGattDescriptor bluetoothGattDescriptor) {
        return parse(bluetoothGattDescriptor.getValue());
    }

    public static String parse(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return "";
        }
        char[] cArr = new char[(bArr.length * 3) - 1];
        for (int i = 0; i < bArr.length; i++) {
            int i2 = bArr[i] & 255;
            int i3 = i * 3;
            char[] cArr2 = HEX_ARRAY;
            cArr[i3] = cArr2[i2 >>> 4];
            cArr[i3 + 1] = cArr2[i2 & 15];
            if (i != bArr.length - 1) {
                cArr[i3 + 2] = Soundex.SILENT_MARKER;
            }
        }
        return "(0x) " + new String(cArr);
    }
}
