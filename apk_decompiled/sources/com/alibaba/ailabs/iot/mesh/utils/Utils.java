package com.alibaba.ailabs.iot.mesh.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Process;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.core.view.InputDeviceCompat;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.hjq.permissions.Permission;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class Utils {
    public static String ACTION_ADD_APP_KEY = "ACTION_ADD_APP_KEY";
    public static final String ACTION_AGREE_SHARE_DEVICE = "com.aliyun.iot.ilop.agree.share.device";
    public static final String ACTION_BIND_STATE = "ACTION_BIND_STATE";
    public static final String ACTION_COMMON_MESSAGE_STATUS_RECEIVED = "ACTION_COMMON_MESSAGE_STATUS_RECEIVED";
    public static final String ACTION_CONFIGURATION_STATE = "ACTION_CONFIGURATION_STATE";
    public static final String ACTION_CONNECTION_STATE = "ACTION_CONNECTION_STATE";
    public static String ACTION_DELETE_APP_KEY = "ACTION_DELETE_APP_KEY";
    public static final String ACTION_GENERIC_LEVEL_STATE = "ACTION_GENERIC_LEVEL_STATE";
    public static final String ACTION_GENERIC_ON_OFF_STATE = "ACTION_GENERIC_ON_OFF_STATE";
    public static final String ACTION_GENERIC_STATE = "ACTION_GENERIC_STATE";
    public static final String ACTION_INIT_FAILED = "ACTION_INIT_FAILED";
    public static final String ACTION_INIT_SUCCESS = "ACTION_INIT_SUCCESS";
    public static final String ACTION_IS_CONNECTED = "ACTION_IS_CONNECTED";
    public static final String ACTION_IS_RECONNECTING = "ACTION_IS_RECONNECTING";
    public static final String ACTION_MQTT_CONNECTED = "ACTION_MQTT_CONNECTED";
    public static final String ACTION_MQTT_RECEIVED = "ACTION_MQTT_RECEIVED";
    public static final String ACTION_MQTT_SHARE_DEVICE_UNBIND = "ACTION_MQTT_SHARE_DEVICE_UNBIND";
    public static final String ACTION_ON_DEVICE_READY = "ACTION_ON_DEVICE_READY";
    public static final String ACTION_OPERATE_SUCCESS = "ACTION_OPERATE_SUCCESS";
    public static final String ACTION_PROVISIONED_NODE_FOUND = "ACTION_PROVISIONED_NODE_FOUND";
    public static final String ACTION_PROVISIONING_STATE = "ACTION_PROVISIONING_STATE";
    public static final String ACTION_RESET_MESH_NETWORK = "ACTION_RESET_MESH_NETWORK";
    public static final String ACTION_TRANSACTION_STATE = "ACTION_TRANSACTION_STATE";
    public static final String ACTION_UNBIND_SHARE_DEVICE = "com.aliyun.iot.ilop.unbind.share.device";
    public static final String ACTION_UPDATE_PROVISIONED_NODES = "ACTION_UPDATE_PROVISIONED_NODES";
    public static final String ACTION_VENDOR_MODEL_MESSAGE = "ACTION_VENDOR_MODEL_MESSAGE";
    public static final String ACTION_VENDOR_MODEL_MESSAGE_STATE = "ACTION_VENDOR_MODEL_MESSAGE_STATE";
    public static String ACTION_VIEW_APP_KEY = "ACTION_VIEW_APP_KEY";
    public static final String ACTIVITY_RESULT = "RESULT_APP_KEY";
    public static final String APPLICATION_KEYS = "APPLICATION_KEYS";
    public static final String EXTRA_APP_KEY_INDEX = "EXTRA_APP_KEY_INDEX";
    public static final String EXTRA_BIND_CODE = "EXTRA_BIND_CODE";
    public static final String EXTRA_BIND_STATE_MSG = "EXTRA_BIND_STATE_MSG";
    public static final String EXTRA_CONFIGURATION_FAIL_MSG = "EXTRA_CONFIGURATION_FAIL_MSG";
    public static final String EXTRA_CONFIGURATION_STATE = "EXTRA_CONFIGURATION_STATE";
    public static final String EXTRA_CONNECT_FAIL_MSG = "EXTRA_CONNECT_FAIL_MSG";
    public static final String EXTRA_DATA = "EXTRA_DATA";
    public static final String EXTRA_DATA_MODEL_NAME = "EXTRA_DATA_MODEL_NAME";
    public static final String EXTRA_DATA_NODE_RESET = "EXTRA_DATA_NODE_RESET";
    public static final String EXTRA_DATA_NODE_RESET_STATUS = "EXTRA_DATA_NODE_RESET_STATUS";
    public static final String EXTRA_DEVICE = "EXTRA_DEVICE";
    public static final String EXTRA_ELEMENT_ADDRESS = "EXTRA_ELEMENT_ADDRESS";
    public static final String EXTRA_GENERIC_ON_OFF_GET = "EXTRA_GENERIC_ON_OFF_GET";
    public static final String EXTRA_GENERIC_ON_OFF_SET = "EXTRA_GENERIC_ON_OFF_SET";
    public static final String EXTRA_GENERIC_ON_OFF_SET_UNACK = "EXTRA_GENERIC_ON_OFF_SET_UNACK";
    public static final String EXTRA_GENERIC_ON_OFF_STATE = "EXTRA_GENERIC_ON_OFF_STATE";
    public static final String EXTRA_GENERIC_PRESENT_STATE = "EXTRA_GENERIC_PRESENT_STATE";
    public static final String EXTRA_GENERIC_TARGET_STATE = "EXTRA_GENERIC_TARGET_STATE";
    public static final String EXTRA_GENERIC_TRANSITION_RES = "EXTRA_GENERIC_TRANSITION_RES";
    public static final String EXTRA_GENERIC_TRANSITION_STEPS = "EXTRA_GENERIC_TRANSITION_STEPS";
    public static final String EXTRA_IS_SUCCESS = "EXTRA_IS_SUCCESS";
    public static final String EXTRA_MODEL_ID = "EXTRA_MODEL_ID";
    public static final String EXTRA_MQTT_DATA = "EXTRA_MQTT_DATA";
    public static final String EXTRA_NET_KEY_INDEX = "EXTRA_APP_KEY_INDEX";
    public static final String EXTRA_PROVISIONING_DEVICE_MAC = "EXTRA_PROVISIONING_DEVICE_MAC";
    public static final String EXTRA_PROVISIONING_FAIL_MSG = "EXTRA_PROVISIONING_FAIL_MSG";
    public static final String EXTRA_PROVISIONING_STATE = "EXTRA_PROVISIONING_STATE";
    public static final String EXTRA_PUBLISH_ADDRESS = "EXTRA_PUBLISH_ADDRESS";
    public static final String EXTRA_REQUEST_FAIL_MSG = "EXTRA_REQUEST_FAIL_MSG";
    public static final String EXTRA_STATUS = "EXTRA_STATUS";
    public static final String EXTRA_SUBSCRIPTION_ADDRESS = "EXTRA_SUBSCRIPTION_ADDRESS";
    public static String EXTRA_UNICAST_ADDRESS = "EXTRA_UNICAST_ADDRESS";
    public static final int FLAG_2_OCTET_OPCODE = 32768;
    public static final String HARMONY_OS = "harmony";
    public static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static final String HEX_PATTERN = "^[0-9a-fA-F]+$";
    public static final int MASK_2_OCTET_OPCODE = 16744448;
    public static final int MASK_3_OCTET_OPCODE = 12582912;
    public static final String PREFS_LOCATION_NOT_REQUIRED = "location_not_required";
    public static final String PREFS_PERMISSION_REQUESTED = "permission_requested";
    public static final String PROVISIONED_NODES_FILE = "PROVISIONED_FILES";
    public static final int PROVISIONING_SUCCESS = 2112;

    public static int byteArray2Int(byte[] bArr) {
        int length = 4 - (bArr == null ? 0 : bArr.length);
        byte[] bArr2 = new byte[length];
        for (int i = 0; i < length; i++) {
            bArr2[i] = 0;
        }
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(4);
        byteBufferAllocate.put(bArr2);
        byteBufferAllocate.put(bArr);
        byteBufferAllocate.order(ByteOrder.BIG_ENDIAN);
        return byteBufferAllocate.getInt(0);
    }

    public static String bytes2HexString(byte[] bArr) {
        int length;
        if (bArr == null || (length = bArr.length) <= 0) {
            return null;
        }
        char[] cArr = new char[length << 1];
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i + 1;
            char[] cArr2 = HEX_DIGITS;
            cArr[i] = cArr2[(bArr[i2] >>> 4) & 15];
            i = i3 + 1;
            cArr[i3] = cArr2[bArr[i2] & 15];
        }
        return new String(cArr);
    }

    public static boolean checkBlePermission(Context context) {
        return (Build.VERSION.SDK_INT < 23 || Build.VERSION.SDK_INT >= 31) ? PermissionChecker.checkPermission(context, Permission.BLUETOOTH_SCAN, Process.myPid(), Process.myUid(), context.getPackageName()) == 0 && PermissionChecker.checkPermission(context, Permission.BLUETOOTH_CONNECT, Process.myPid(), Process.myUid(), context.getPackageName()) == 0 : ActivityCompat.checkSelfPermission(context, Permission.BLUETOOTH_SCAN) == 0 && ActivityCompat.checkSelfPermission(context, Permission.BLUETOOTH_CONNECT) == 0;
    }

    public static boolean checkIfVersionIsOreoOrAbove() {
        return Build.VERSION.SDK_INT >= 26;
    }

    public static IntentFilter createIntentFilters() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_CONNECTION_STATE);
        intentFilter.addAction(ACTION_IS_CONNECTED);
        intentFilter.addAction(ACTION_IS_RECONNECTING);
        intentFilter.addAction(ACTION_ON_DEVICE_READY);
        intentFilter.addAction(ACTION_PROVISIONING_STATE);
        intentFilter.addAction(ACTION_CONFIGURATION_STATE);
        intentFilter.addAction(ACTION_TRANSACTION_STATE);
        intentFilter.addAction(ACTION_GENERIC_ON_OFF_STATE);
        intentFilter.addAction(ACTION_GENERIC_LEVEL_STATE);
        intentFilter.addAction(ACTION_VENDOR_MODEL_MESSAGE_STATE);
        intentFilter.addAction(ACTION_BIND_STATE);
        intentFilter.addAction(ACTION_INIT_SUCCESS);
        intentFilter.addAction(ACTION_INIT_FAILED);
        intentFilter.addAction(ACTION_OPERATE_SUCCESS);
        intentFilter.addAction(ACTION_COMMON_MESSAGE_STATUS_RECEIVED);
        intentFilter.addAction(ACTION_MQTT_CONNECTED);
        intentFilter.addAction(ACTION_MQTT_RECEIVED);
        intentFilter.addAction(ACTION_MQTT_SHARE_DEVICE_UNBIND);
        intentFilter.addAction(ACTION_AGREE_SHARE_DEVICE);
        intentFilter.addAction(ACTION_UNBIND_SHARE_DEVICE);
        return intentFilter;
    }

    public static String deviceId2Mac(String str) {
        if (TextUtils.isEmpty(str) || str.length() < 26) {
            return "";
        }
        byte[] bArrHexString2Bytes = ConvertUtils.hexString2Bytes(str.substring(14, 26));
        return String.format("%1$02x:%2$02x:%3$02x:%4$02x:%5$02x:%6$02x", Byte.valueOf(bArrHexString2Bytes[5]), Byte.valueOf(bArrHexString2Bytes[4]), Byte.valueOf(bArrHexString2Bytes[3]), Byte.valueOf(bArrHexString2Bytes[2]), Byte.valueOf(bArrHexString2Bytes[1]), Byte.valueOf(bArrHexString2Bytes[0]));
    }

    public static int getKey(Map<Integer, String> map, String str) {
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (entry.getValue().equals(str)) {
                return entry.getKey().intValue();
            }
        }
        return -1;
    }

    public static byte[] getOpCodeBytes(int i) {
        return (i & MASK_3_OCTET_OPCODE) == 12582912 ? new byte[]{(byte) ((i >> 16) & 255), (byte) (i & 255), (byte) ((i >> 8) & 255)} : (16744448 & i) == 32768 ? new byte[]{(byte) ((i >> 8) & 255), (byte) (i & 255)} : new byte[]{(byte) i};
    }

    public static boolean isBleEnabled() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        return defaultAdapter != null && defaultAdapter.isEnabled();
    }

    public static boolean isHarmonyOS() {
        try {
            Class<?> cls = Class.forName("com.huawei.system.BuildEx");
            Method method = cls.getMethod("getOsBrand", new Class[0]);
            ClassLoader classLoader = cls.getClassLoader();
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            sb.append("classLoader: ");
            sb.append(classLoader);
            printStream.println(sb.toString());
            if (classLoader != null && classLoader.getParent() == null) {
                return HARMONY_OS.equals(method.invoke(cls, new Object[0]));
            }
        } catch (ClassNotFoundException | NoSuchMethodException | Exception unused) {
        }
        return false;
    }

    public static boolean isLocationEnabled(Context context) {
        int i;
        if (!isMarshmallowOrAbove()) {
            return true;
        }
        try {
            i = Settings.Secure.getInt(context.getContentResolver(), "location_mode");
        } catch (Settings.SettingNotFoundException unused) {
            i = 0;
        }
        return i != 0;
    }

    public static boolean isLocationPermissionDeniedForever(Activity activity2) {
        return (isLocationPermissionsGranted(activity2) || !PreferenceManager.getDefaultSharedPreferences(activity2).getBoolean(PREFS_PERMISSION_REQUESTED, false) || ActivityCompat.shouldShowRequestPermissionRationale(activity2, Permission.ACCESS_COARSE_LOCATION)) ? false : true;
    }

    public static boolean isLocationPermissionsGranted(Context context) {
        return Build.VERSION.SDK_INT >= 33 ? (ContextCompat.checkSelfPermission(context, Permission.ACCESS_COARSE_LOCATION) == 0) || ContextCompat.checkSelfPermission(context, Permission.NEARBY_WIFI_DEVICES) == 0 : ContextCompat.checkSelfPermission(context, Permission.ACCESS_COARSE_LOCATION) == 0;
    }

    public static boolean isLocationRequired(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREFS_LOCATION_NOT_REQUIRED, isMarshmallowOrAbove());
    }

    public static boolean isLollipopOrAbove() {
        return Build.VERSION.SDK_INT >= 21;
    }

    public static boolean isMarshmallowOrAbove() {
        return Build.VERSION.SDK_INT >= 23;
    }

    public static boolean isValidUint8(int i) {
        int i2 = i & InputDeviceCompat.SOURCE_ANY;
        return i2 == 0 || i2 == -256;
    }

    public static void markLocationNotRequired(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREFS_LOCATION_NOT_REQUIRED, false).apply();
    }

    public static void markLocationPermissionRequested(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREFS_PERMISSION_REQUESTED, true).apply();
    }

    public static void notifyFailed(IActionListener iActionListener, int i, String str) {
        if (iActionListener != null) {
            try {
                iActionListener.onFailure(i, str);
            } catch (Exception unused) {
            }
        }
    }

    public static <T> void notifySuccess(IActionListener<T> iActionListener, T t) {
        if (iActionListener != null) {
            try {
                iActionListener.onSuccess(t);
            } catch (Exception unused) {
            }
        }
    }

    public static void saveApplicationKeys(Context context, Map<Integer, String> map) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences(APPLICATION_KEYS, 0).edit();
        for (int i = 0; i < map.size(); i++) {
            editorEdit.putString(String.valueOf(i), map.get(Integer.valueOf(i)));
        }
        editorEdit.apply();
    }

    public static Integer shortToInteger(Short sh) {
        return sh.shortValue() < 0 ? Integer.valueOf(sh.shortValue() + 65536) : Integer.valueOf(new BigDecimal((int) sh.shortValue()).intValue());
    }

    public static void showToast(Context context, String str) {
        Toast.makeText(context, str, 0).show();
    }

    public static void notifyFailed(com.alibaba.ailabs.iot.aisbase.callback.IActionListener iActionListener, int i, String str) {
        if (iActionListener != null) {
            try {
                iActionListener.onFailure(i, str);
            } catch (Exception unused) {
            }
        }
    }

    public static <T> void notifySuccess(com.alibaba.ailabs.iot.aisbase.callback.IActionListener<T> iActionListener, T t) {
        if (iActionListener != null) {
            try {
                iActionListener.onSuccess(t);
            } catch (Exception unused) {
            }
        }
    }
}
