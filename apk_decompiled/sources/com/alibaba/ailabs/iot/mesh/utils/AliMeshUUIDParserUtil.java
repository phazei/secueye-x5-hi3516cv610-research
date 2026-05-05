package com.alibaba.ailabs.iot.mesh.utils;

import a.a.a.a.b.m.a;
import android.os.Build;
import android.text.TextUtils;
import com.alibaba.ailabs.iot.mesh.utils.Constants;
import com.alibaba.ailabs.tg.utils.ConvertUtils;

/* JADX INFO: loaded from: classes.dex */
public class AliMeshUUIDParserUtil {
    public static final int FEATURE_FLAG1_OFFSET = 14;
    public static final int FEATURE_FLAG2_OFFSET = 15;
    public static final int FEATURE_FLAG_OFFSET = 13;
    public static final String TAG = "AliMeshUUIDParserUtil";

    public static String extractMacAddressFromUUID(String str) {
        if (TextUtils.isEmpty(str) || str.length() < 32) {
            return "";
        }
        byte[] bArrHexString2Bytes = ConvertUtils.hexString2Bytes(str.substring(14, 26));
        return String.format("%1$02x:%2$02x:%3$02x:%4$02x:%5$02x:%6$02x", Byte.valueOf(bArrHexString2Bytes[5]), Byte.valueOf(bArrHexString2Bytes[4]), Byte.valueOf(bArrHexString2Bytes[3]), Byte.valueOf(bArrHexString2Bytes[2]), Byte.valueOf(bArrHexString2Bytes[1]), Byte.valueOf(bArrHexString2Bytes[0]));
    }

    public static int extractPIDFromUUID(byte[] bArr) {
        a.a(TAG, String.format("extract PID from UUID: %s", Utils.bytes2HexString(bArr)));
        if (bArr == null || bArr.length < 16) {
            return 0;
        }
        return (bArr[3] & 255) + 0 + ((bArr[4] & 255) << 8) + ((bArr[5] & 255) << 16) + ((bArr[6] & 255) << 24);
    }

    public static Constants.AliMeshSolutionType getAliMeshSolutionTypeFromUUID(byte[] bArr) {
        a.a(TAG, String.format("determine device: %s support large-scale mesh network", Utils.bytes2HexString(bArr)));
        if (bArr == null || bArr.length < 16) {
            return Constants.AliMeshSolutionType.UNKNOWN;
        }
        byte b2 = bArr[13];
        byte b3 = bArr[14];
        return b3 != -125 ? b3 != -109 ? b3 != 3 ? b3 != 7 ? Constants.AliMeshSolutionType.SIG_MESH : Constants.AliMeshSolutionType.TINY_MESH_ADV_V2 : Constants.AliMeshSolutionType.TINY_MESH_ADV_V1 : Constants.AliMeshSolutionType.TINY_MESH_GATT_V2 : Constants.AliMeshSolutionType.TINY_MESH_GATT_V1;
    }

    public static boolean isComboMesh(String str) {
        a.a(TAG, String.format("determine device: %s is comboMesh device", str));
        return (TextUtils.isEmpty(str) || str.length() < 32 || (ConvertUtils.hexString2Bytes(str)[14] & 8) == 0) ? false : true;
    }

    public static boolean isSupportAutomaticallyGenerateShareAppKey(String str) {
        a.a(TAG, String.format("determine device: %s support automatically generate share appKey", str));
        if (!TextUtils.isEmpty(str) && str.length() >= 32) {
            byte[] bArrHexString2Bytes = ConvertUtils.hexString2Bytes(str);
            byte b2 = bArrHexString2Bytes[13];
            byte b3 = bArrHexString2Bytes[15];
            if ((b2 >> 1) > 1) {
                return ((b3 & 8) == 0 && (b3 & 16) == 0) ? false : true;
            }
        }
        return false;
    }

    public static boolean isSupportFastProvision(String str) {
        a.a(TAG, String.format("determine device: %s support fast provision", str));
        if (TextUtils.isEmpty(str) || str.length() < 32) {
            return false;
        }
        byte b2 = ConvertUtils.hexString2Bytes(str)[14];
        if (b2 != -125) {
            return Build.VERSION.SDK_INT >= 21 && b2 == 7;
        }
        return true;
    }

    public static boolean isSupportFastProvisionViaAdv(String str) {
        a.a(TAG, String.format("determine device: %s support fast provision via adv mode", str));
        if (TextUtils.isEmpty(str) || str.length() < 32) {
            return false;
        }
        return Build.VERSION.SDK_INT >= 21 && ConvertUtils.hexString2Bytes(str)[14] == 7;
    }

    public static boolean isSupportFastProvisionViaGatt(String str) {
        a.a(TAG, String.format("determine device: %s support fast provision via gatt mode", str));
        return !TextUtils.isEmpty(str) && str.length() >= 32 && ConvertUtils.hexString2Bytes(str)[14] == -125;
    }

    public static boolean isSupportLargeScaleMeshNetwork(String str) {
        a.a(TAG, String.format("determine device: %s support large-scale mesh network", str));
        if (!TextUtils.isEmpty(str) && str.length() >= 32) {
            byte[] bArrHexString2Bytes = ConvertUtils.hexString2Bytes(str);
            return ((byte) (bArrHexString2Bytes[13] >> 1)) >= 2 && (bArrHexString2Bytes[15] & 1) == 1;
        }
        return false;
    }

    public static boolean isComboMesh(byte[] bArr) {
        return (bArr == null || bArr.length < 16 || (bArr[14] & 8) == 0) ? false : true;
    }
}
