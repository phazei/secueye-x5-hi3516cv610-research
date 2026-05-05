package meshprovisioner.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: loaded from: classes4.dex */
public class AddressUtils {
    public static final String TAG = "AddressUtils";

    public static byte[] getUnicastAddressBytes(int i) {
        return new byte[]{(byte) ((i >> 8) & 255), (byte) (i & 255)};
    }

    public static int getUnicastAddressInt(byte[] bArr) {
        return ByteBuffer.wrap(bArr).order(ByteOrder.BIG_ENDIAN).getShort();
    }

    public static byte[] incrementUnicastAddress(byte[] bArr) {
        int unicastAddressInt = getUnicastAddressInt(bArr) + 1;
        return new byte[]{(byte) ((unicastAddressInt >> 8) & 255), (byte) (unicastAddressInt & 255)};
    }

    public static boolean isValidGroupAddress(byte[] bArr) {
        if (bArr == null || bArr.length != 2) {
            return false;
        }
        return 49152 == (((bArr[1] & 255) | ((bArr[0] & 255) << 8)) & 49152);
    }

    public static boolean isValidUnicastAddress(byte[] bArr) {
        if (bArr == null) {
            return false;
        }
        int i = (bArr[1] & 255) | ((bArr[0] & 255) << 8);
        return i == (i & 32767);
    }
}
