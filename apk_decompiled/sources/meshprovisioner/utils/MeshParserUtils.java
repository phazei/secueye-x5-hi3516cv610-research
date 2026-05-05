package meshprovisioner.utils;

import android.content.Context;
import android.text.TextUtils;
import anet.channel.entity.EventType;
import com.alibaba.ailabs.iot.mesh.R;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;

/* JADX INFO: loaded from: classes4.dex */
public class MeshParserUtils {
    public static final int DEFAULT_TTL = 255;
    public static final int GENERIC_ON_OFF_5_MS = 5;
    public static final int IV_ADDRESS_MAX = 4096;
    public static final int IV_ADDRESS_MIN = 0;
    public static final String PATTERN_NETWORK_KEY = "[0-9a-fA-F]{32}";
    public static final int PROHIBITED_DEFAULT_TTL_STATE_MAX = 255;
    public static final int PROHIBITED_DEFAULT_TTL_STATE_MID = 128;
    public static final int PROHIBITED_DEFAULT_TTL_STATE_MIN = 1;
    public static final int PROHIBITED_PUBLISH_TTL_MAX = 254;
    public static final int PROHIBITED_PUBLISH_TTL_MIN = 128;
    public static final int RESOLUTION_100_MS = 0;
    public static final int RESOLUTION_10_M = 3;
    public static final int RESOLUTION_10_S = 2;
    public static final int RESOLUTION_1_S = 1;
    public static final int UNICAST_ADDRESS_MIN = 0;
    public static final char[] HEX_ARRAY = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    public static final byte[] DISABLED_PUBLICATION_ADDRESS = {0, 0};

    public static byte[] addKeyIndexPadding(Integer num) {
        return ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort((short) (num.intValue() & EventType.ALL)).array();
    }

    public static String bytesToHex(byte[] bArr, boolean z) {
        return bArr == null ? "" : bytesToHex(bArr, 0, bArr.length, z);
    }

    public static int calculateSeqZero(byte[] bArr) {
        return (bArr[2] & 255) | ((bArr[1] & 31) << 8);
    }

    public static byte[] concatenateSegmentedMessages(HashMap<Integer, byte[]> map) {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(getSegmentedMessageLength(map));
        byteBufferAllocate.order(ByteOrder.BIG_ENDIAN);
        for (int i = 0; i < map.size(); i++) {
            byteBufferAllocate.put(map.get(Integer.valueOf(i)));
        }
        return byteBufferAllocate.array();
    }

    public static byte[] createVendorOpCode(int i, int i2) {
        if (i2 != 65535) {
            return new byte[]{(byte) ((i & 63) | 192), (byte) (i2 & 255), (byte) ((i2 >> 8) & 255)};
        }
        return null;
    }

    public static ArrayList<Integer> decode(byte[] bArr, int i, int i2) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        int i3 = i - i2;
        if (i3 == 0) {
            return arrayList;
        }
        if (i3 == 2) {
            arrayList.add(Integer.valueOf(encode(new byte[]{(byte) (bArr[i2 + 1] & 15), bArr[i2]})));
        } else {
            int i4 = i2 + 1;
            int iEncode = encode(new byte[]{(byte) (bArr[i4] & 15), bArr[i2]});
            int i5 = i2 + 2;
            int iEncode2 = encode(new byte[]{(byte) ((bArr[i5] & 240) >> 4), (byte) ((bArr[i5] << 4) | ((bArr[i4] & 240) >> 4))});
            arrayList.add(Integer.valueOf(iEncode));
            arrayList.add(Integer.valueOf(iEncode2));
            arrayList.addAll(decode(bArr, i, i2 + 3));
        }
        return arrayList;
    }

    public static int encode(byte[] bArr) {
        return ByteBuffer.wrap(bArr).order(ByteOrder.BIG_ENDIAN).getShort();
    }

    public static int getBitValue(int i, int i2) {
        return (i >> i2) & 1;
    }

    public static byte[] getDstAddress(byte[] bArr) {
        return ByteBuffer.allocate(2).put(bArr, 8, 2).array();
    }

    public static String getMeshNodeCacheKey(String str, byte[] bArr) {
        return String.format("%s-%d", str, Integer.valueOf((bArr[1] & 255) | ((bArr[0] & 255) << 8)));
    }

    public static int getOpCode(byte[] bArr, int i) {
        if (i == 1) {
            return bArr[0];
        }
        if (i != 2) {
            if (i != 3) {
                return -1;
            }
            return (bArr[2] & 255) | ((bArr[0] & 255) << 16) | ((bArr[1] & 255) << 8);
        }
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder.put(bArr, 0, 2);
        byteBufferOrder.rewind();
        return byteBufferOrder.getShort();
    }

    public static byte[] getOpCodes(int i) {
        return (i & Utils.MASK_3_OCTET_OPCODE) == 12582912 ? new byte[]{(byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)} : (16744448 & i) == 32768 ? new byte[]{(byte) ((i >> 8) & 255), (byte) (i & 255)} : new byte[]{(byte) i};
    }

    public static int getRemainingTime(int i, int i2) {
        switch (i) {
            case 0:
                return i2 * 100;
            case 1:
                return i2 * 1000;
            case 2:
                return i2 * 10 * 1000;
            case 3:
                return i2 * 10 * 1000 * 60;
            default:
                return 0;
        }
    }

    public static String getRemainingTime(int i) {
        int i2 = i >> 6;
        int i3 = i & 63;
        switch (i2) {
            case 0:
                return (i3 * 100) + " milliseconds";
            case 1:
                return i3 + " seconds";
            case 2:
                return (i3 * 10) + " seconds";
            case 3:
                return (i3 * 10) + " minutes";
            default:
                return "Unknown";
        }
    }

    public static String getRemainingTransitionTime(int i, int i2) {
        switch (i) {
            case 0:
                return (i2 * 100) + " ms";
            case 1:
                return i2 + " s";
            case 2:
                return (i2 * 10) + " s";
            case 3:
                return (i2 * 10) + " min.";
            default:
                return "Unknown";
        }
    }

    public static int getSegmentedMessageLength(HashMap<Integer, byte[]> map) {
        int length = 0;
        for (int i = 0; i < map.size(); i++) {
            length += map.get(Integer.valueOf(i)).length;
        }
        return length;
    }

    public static int getSequenceNumber(byte[] bArr) {
        return (bArr[2] & 255) | ((bArr[0] & 255) << 16) | ((bArr[1] & 255) << 8);
    }

    public static byte[] getSequenceNumberBytes(int i) {
        if (isValidSequenceNumber(Integer.valueOf(i))) {
            return new byte[]{(byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)};
        }
        return null;
    }

    public static int getSequenceNumberFromPDU(byte[] bArr) {
        return (bArr[5] & 255) | ((bArr[3] & 255) << 16) | ((bArr[4] & 255) << 8);
    }

    public static byte[] getSrcAddress(byte[] bArr) {
        return ByteBuffer.allocate(2).put(bArr, 6, 2).array();
    }

    public static int getValue(byte[] bArr) {
        if (bArr == null || bArr.length != 2) {
            return 0;
        }
        return unsignedToSigned(unsignedBytesToInt(bArr[0], bArr[1]), 16);
    }

    public static boolean isValidIvIndex(Integer num) {
        return num != null && num.intValue() >= 0 && num.intValue() <= Integer.MAX_VALUE;
    }

    public static boolean isValidKeyIndex(Integer num) {
        return num == null || num.intValue() != (num.intValue() & EventType.ALL);
    }

    public static final boolean isValidOpcode(int i) {
        if (i == (16777215 & i)) {
            return true;
        }
        throw new IllegalArgumentException("Invalid opcode, opcode must be 1-3 octets");
    }

    public static final boolean isValidParameters(byte[] bArr) {
        if (bArr == null || bArr.length <= 379) {
            return true;
        }
        throw new IllegalArgumentException("Invalid parameters, parameters must be 0-379 octets");
    }

    public static boolean isValidSequenceNumber(Integer num) {
        boolean z = num != null && num.intValue() == (num.intValue() & 16777215);
        if (num.intValue() == 16777215) {
            return false;
        }
        return z;
    }

    public static boolean isValidUnicastAddress(Integer num) {
        return num != null && num.intValue() == (num.intValue() & 32767);
    }

    public static byte parseUpdateFlags(int i, int i2) {
        byte b2 = i == 1 ? (byte) 1 : (byte) 0;
        return (byte) (i2 == 1 ? b2 | 2 : b2 & (-2));
    }

    public static int setByteArrayValue(byte[] bArr, int i, String str) {
        if (str == null) {
            return i;
        }
        for (int i2 = 0; i2 < str.length(); i2 += 2) {
            bArr[(i2 / 2) + i] = (byte) ((Character.digit(str.charAt(i2), 16) << 244) + Character.digit(str.charAt(i2 + 1), 16));
        }
        return i + (str.length() / 2);
    }

    public static byte[] toByteArray(String str) {
        int length = str.length();
        byte[] bArr = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }

    public static int unsignedByteToInt(byte b2) {
        return b2 & 255;
    }

    public static int unsignedBytesToInt(byte b2, byte b3) {
        return unsignedByteToInt(b2) + (unsignedByteToInt(b3) << 8);
    }

    public static int unsignedToSigned(int i, int i2) {
        int i3 = 1 << (i2 - 1);
        return (i & i3) != 0 ? (i3 - (i & (i3 - 1))) * (-1) : i;
    }

    public static boolean validateAppKeyInput(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException(context.getString(R.string.error_empty_app_key));
        }
        if (str.matches(PATTERN_NETWORK_KEY)) {
            return true;
        }
        throw new IllegalArgumentException(context.getString(R.string.error_invalid_app_key));
    }

    public static boolean validateIvIndexInput(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException(context.getString(R.string.error_empty_iv_index));
        }
        try {
            Integer numValueOf = Integer.valueOf(Integer.parseInt(str, 16));
            if (!isValidIvIndex(numValueOf)) {
                throw new IllegalArgumentException(context.getString(R.string.error_invalid_iv_index));
            }
            if (numValueOf.intValue() >= 0 || numValueOf.intValue() <= 4096) {
                return true;
            }
            throw new IllegalArgumentException(context.getString(R.string.error_invalid_iv_index));
        } catch (NumberFormatException unused) {
            throw new IllegalArgumentException(context.getString(R.string.error_invalid_iv_index));
        }
    }

    public static boolean validateKeyIndexInput(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException(context.getString(R.string.error_empty_key_index));
        }
        try {
            if (isValidKeyIndex(Integer.valueOf(Integer.parseInt(str)))) {
                throw new IllegalArgumentException(context.getString(R.string.error_invalid_key_index));
            }
            return true;
        } catch (NumberFormatException unused) {
            throw new IllegalArgumentException(context.getString(R.string.error_invalid_key_index));
        }
    }

    public static boolean validateNetworkKeyInput(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException(context.getString(R.string.error_empty_network_key));
        }
        if (str.matches(PATTERN_NETWORK_KEY)) {
            return true;
        }
        throw new IllegalArgumentException(context.getString(R.string.error_invalid_network_key));
    }

    public static boolean validatePublishRetransmitIntervalSteps(int i) {
        return i == (i & 31);
    }

    public static boolean validatePublishTtl(int i) {
        return i < 128 || i > 254 || i == 255;
    }

    public static boolean validateRetransmitCount(int i) {
        return i == (i & 7);
    }

    public static boolean validateTtlInput(Context context, Integer num) {
        if (num == null) {
            throw new IllegalArgumentException(context.getString(R.string.error_empty_global_ttl));
        }
        if (num.intValue() == 1 || (num.intValue() >= 128 && num.intValue() <= 255)) {
            throw new IllegalArgumentException(context.getString(R.string.error_invalid_global_ttl));
        }
        return true;
    }

    public static boolean validateUnicastAddressInput(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException(context.getString(R.string.error_empty_unicast_address));
        }
        try {
            Integer numValueOf = Integer.valueOf(Integer.parseInt(str, 16));
            if (!isValidUnicastAddress(numValueOf)) {
                throw new IllegalArgumentException(context.getString(R.string.error_invalid_unicast_address));
            }
            if (numValueOf.intValue() != 0) {
                return true;
            }
            throw new IllegalArgumentException(context.getString(R.string.error_invalid_unicast_address));
        } catch (NumberFormatException unused) {
            throw new IllegalArgumentException(context.getString(R.string.error_invalid_unicast_address));
        }
    }

    public static String bytesToHex(byte[] bArr, int i, int i2, boolean z) {
        if (bArr == null || bArr.length <= i || i2 <= 0) {
            return "";
        }
        int iMin = Math.min(i2, bArr.length - i);
        char[] cArr = new char[iMin * 2];
        for (int i3 = 0; i3 < iMin; i3++) {
            int i4 = bArr[i + i3] & 255;
            int i5 = i3 * 2;
            char[] cArr2 = HEX_ARRAY;
            cArr[i5] = cArr2[i4 >>> 4];
            cArr[i5 + 1] = cArr2[i4 & 15];
        }
        if (!z) {
            return new String(cArr);
        }
        return "0x" + new String(cArr);
    }

    public static boolean isValidUnicastAddress(byte[] bArr) {
        if (bArr == null) {
            return false;
        }
        int i = (bArr[1] & 255) | ((bArr[0] & 255) << 8);
        return i == (i & 32767);
    }

    public static boolean validateKeyIndexInput(Context context, Integer num) {
        if (num != null) {
            if (isValidKeyIndex(num)) {
                throw new IllegalArgumentException(context.getString(R.string.error_invalid_key_index));
            }
            return true;
        }
        throw new IllegalArgumentException(context.getString(R.string.error_empty_key_index));
    }

    public static boolean validateIvIndexInput(Context context, Integer num) {
        if (num != null) {
            if (isValidIvIndex(num)) {
                if (num.intValue() >= 0 || num.intValue() <= 4096) {
                    return true;
                }
                throw new IllegalArgumentException(context.getString(R.string.error_invalid_iv_index));
            }
            throw new IllegalArgumentException(context.getString(R.string.error_invalid_iv_index));
        }
        throw new IllegalArgumentException(context.getString(R.string.error_empty_iv_index));
    }

    public static boolean validateUnicastAddressInput(Context context, Integer num) {
        if (num != null) {
            if (isValidUnicastAddress(num)) {
                if (num.intValue() != 0) {
                    return true;
                }
                throw new IllegalArgumentException(context.getString(R.string.error_invalid_unicast_address));
            }
            throw new IllegalArgumentException(context.getString(R.string.error_invalid_unicast_address));
        }
        throw new IllegalArgumentException(context.getString(R.string.error_empty_unicast_address));
    }
}
