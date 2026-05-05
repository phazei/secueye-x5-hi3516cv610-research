package aisble;

import aisble.BleManagerCallbacks;
import aisble.Request;
import aisble.callback.DataReceivedCallback;
import aisble.callback.SuccessCallback;
import aisble.data.Data;
import aisble.error.GattError;
import aisble.utils.ILogger;
import aisble.utils.ParserUtils;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.media.MediaDescriptionCompat;
import android.util.Log;
import androidx.annotation.IntRange;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import com.alibaba.ailabs.tg.utils.LogUtils;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.huawei.hms.framework.network.grs.GrsBaseInfo;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;
import tools.BleClient;
import view.CustomFinderView;

/* JADX INFO: loaded from: classes.dex */
public abstract class BleManager<E extends BleManagerCallbacks> extends TimeoutHandler implements ILogger {
    public static final long CONNECTION_TIMEOUT_THRESHOLD = 20000;
    public static final int PAIRING_VARIANT_CONSENT = 3;
    public static final int PAIRING_VARIANT_DISPLAY_PASSKEY = 4;
    public static final int PAIRING_VARIANT_DISPLAY_PIN = 5;
    public static final int PAIRING_VARIANT_OOB_CONSENT = 6;
    public static final int PAIRING_VARIANT_PASSKEY = 1;
    public static final int PAIRING_VARIANT_PASSKEY_CONFIRMATION = 2;
    public static final int PAIRING_VARIANT_PIN = 0;
    public String TAG;

    @Deprecated
    public ValueChangedCallback mBatteryLevelNotificationCallback;

    @IntRange(from = -1, to = 100)
    @Deprecated
    public int mBatteryValue;
    public BluetoothDevice mBluetoothDevice;
    public BluetoothGatt mBluetoothGatt;
    public final BroadcastReceiver mBluetoothStateBroadcastReceiver;
    public BroadcastReceiver mBondingBroadcastReceiver;
    public E mCallbacks;
    public ConnectRequest mConnectRequest;
    public Runnable mConnectTimeoutTask;
    public boolean mConnected;
    public int mConnectionCount;
    public int mConnectionState;
    public long mConnectionTime;
    public final Context mContext;
    public boolean mDeviceNotSupported;
    public BleManager<E>.BleManagerGattCallback mGattCallback;
    public final Handler mHandler;
    public boolean mInitialConnection;
    public final Object mLock;
    public int mMtu;
    public final HashMap<BluetoothGattCharacteristic, ValueChangedCallback> mNotificationCallbacks;
    public final BroadcastReceiver mPairingRequestBroadcastReceiver;
    public boolean mReady;
    public boolean mReliableWriteInProgress;
    public Request mRequest;
    public RequestQueue mRequestQueue;
    public int mRetryDiscoveryServiceCount;
    public boolean mServiceDiscoveryRequested;
    public Runnable mServiceDiscoveryRunnable;
    public boolean mServicesDiscovered;
    public boolean mUserDisconnected;
    public WaitForValueChangedRequest mValueChangedRequest;
    public Runnable mWriteOpTimeoutRunable;
    public static final UUID CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID = UUID.fromString(BleClient.READ_DEDSCRIPTION_UUID);
    public static final UUID BATTERY_SERVICE = UUID.fromString("0000180F-0000-1000-8000-00805f9b34fb");
    public static final UUID BATTERY_LEVEL_CHARACTERISTIC = UUID.fromString("00002A19-0000-1000-8000-00805f9b34fb");
    public static final UUID GENERIC_ATTRIBUTE_SERVICE = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb");
    public static final UUID SERVICE_CHANGED_CHARACTERISTIC = UUID.fromString("00002A05-0000-1000-8000-00805f9b34fb");

    /* JADX INFO: renamed from: aisble.BleManager$10, reason: invalid class name */
    static /* synthetic */ class AnonymousClass10 {
        public static final /* synthetic */ int[] $SwitchMap$aisble$Request$Type = new int[Request.Type.values().length];

        static {
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.WAIT_FOR_INDICATION.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.WAIT_FOR_NOTIFICATION.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.CONNECT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.DISCONNECT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.CREATE_BOND.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.REMOVE_BOND.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.SET.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.READ.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.WRITE.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.READ_DESCRIPTOR.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.WRITE_DESCRIPTOR.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.BEGIN_RELIABLE_WRITE.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.EXECUTE_RELIABLE_WRITE.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.ABORT_RELIABLE_WRITE.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.ENABLE_NOTIFICATIONS.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.ENABLE_INDICATIONS.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.DISABLE_NOTIFICATIONS.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.DISABLE_INDICATIONS.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.READ_BATTERY_LEVEL.ordinal()] = 19;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.ENABLE_BATTERY_LEVEL_NOTIFICATIONS.ordinal()] = 20;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.DISABLE_BATTERY_LEVEL_NOTIFICATIONS.ordinal()] = 21;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.ENABLE_SERVICE_CHANGED_INDICATIONS.ordinal()] = 22;
            } catch (NoSuchFieldError unused22) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.REQUEST_MTU.ordinal()] = 23;
            } catch (NoSuchFieldError unused23) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.REQUEST_CONNECTION_PRIORITY.ordinal()] = 24;
            } catch (NoSuchFieldError unused24) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.SET_PREFERRED_PHY.ordinal()] = 25;
            } catch (NoSuchFieldError unused25) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.READ_PHY.ordinal()] = 26;
            } catch (NoSuchFieldError unused26) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.READ_RSSI.ordinal()] = 27;
            } catch (NoSuchFieldError unused27) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.REFRESH_CACHE.ordinal()] = 28;
            } catch (NoSuchFieldError unused28) {
            }
            try {
                $SwitchMap$aisble$Request$Type[Request.Type.SLEEP.ordinal()] = 29;
            } catch (NoSuchFieldError unused29) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract class BleManagerGattCallback extends MainThreadBluetoothGattCallback {
        public static final String ERROR_AUTH_ERROR_WHILE_BONDED = "Phone has lost bonding information";
        public static final String ERROR_CONNECTION_PRIORITY_REQUEST = "Error on connection priority request";
        public static final String ERROR_CONNECTION_STATE_CHANGE = "Error on connection state change";
        public static final String ERROR_DISCOVERY_SERVICE = "Error on discovering services";
        public static final String ERROR_MTU_REQUEST = "Error on mtu request";
        public static final String ERROR_PHY_UPDATE = "Error on PHY update";
        public static final String ERROR_READ_CHARACTERISTIC = "Error on reading characteristic";
        public static final String ERROR_READ_DESCRIPTOR = "Error on reading descriptor";
        public static final String ERROR_READ_PHY = "Error on PHY read";
        public static final String ERROR_READ_RSSI = "Error on RSSI read";
        public static final String ERROR_RELIABLE_WRITE = "Error on Execute Reliable Write";
        public static final String ERROR_WRITE_CHARACTERISTIC = "Error on writing characteristic";
        public static final String ERROR_WRITE_DESCRIPTOR = "Error on writing descriptor";
        public boolean mInitInProgress;
        public Deque<Request> mInitQueue;
        public boolean mOperationInProgress;
        public final Deque<Request> mTaskQueue = new LinkedList();
        public boolean mConnectionPriorityOperationInProgress = false;

        public BleManagerGattCallback() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void enqueue(@NonNull Request request) {
            (this.mInitInProgress ? this.mInitQueue : this.mTaskQueue).add(request);
            request.enqueued = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void enqueueFirst(@NonNull Request request) {
            (this.mInitInProgress ? this.mInitQueue : this.mTaskQueue).addFirst(request);
            request.enqueued = true;
        }

        @Deprecated
        private boolean isBatteryLevelCharacteristic(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            return bluetoothGattCharacteristic != null && BleManager.BATTERY_LEVEL_CHARACTERISTIC.equals(bluetoothGattCharacteristic.getUuid());
        }

        private boolean isCCCD(BluetoothGattDescriptor bluetoothGattDescriptor) {
            return bluetoothGattDescriptor != null && BleManager.CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID.equals(bluetoothGattDescriptor.getUuid());
        }

        private boolean isServiceChangedCCCD(@Nullable BluetoothGattDescriptor bluetoothGattDescriptor) {
            return bluetoothGattDescriptor != null && BleManager.SERVICE_CHANGED_CHARACTERISTIC.equals(bluetoothGattDescriptor.getCharacteristic().getUuid());
        }

        private boolean isServiceChangedCharacteristic(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            return bluetoothGattCharacteristic != null && BleManager.SERVICE_CHANGED_CHARACTERISTIC.equals(bluetoothGattCharacteristic.getUuid());
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:101:0x0247 A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:111:0x0287 A[Catch: all -> 0x03e5, TRY_ENTER, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:112:0x0290 A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:113:0x0299 A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:114:0x02a2 A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:115:0x02ab A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:116:0x02b6 A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:117:0x02c1 A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:118:0x02cc A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:119:0x02d7 A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:120:0x02e0 A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:121:0x02e9 A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:126:0x0305 A[Catch: all -> 0x03e5, TRY_ENTER, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:130:0x0320 A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:131:0x032b A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:135:0x035e A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:136:0x0368 A[Catch: all -> 0x03e5, TRY_LEAVE, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:139:0x0374 A[Catch: all -> 0x03e5, TRY_ENTER, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:140:0x037c A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:141:0x0384 A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:142:0x038c A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:144:0x03a5 A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:27:0x005f A[Catch: Exception -> 0x006c, all -> 0x03e5, TryCatch #0 {Exception -> 0x006c, blocks: (B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:24:0x0048), top: B:161:0x0023 }] */
        /* JADX WARN: Removed duplicated region for block: B:33:0x006f A[Catch: all -> 0x03e5, TRY_ENTER, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:45:0x00c8  */
        /* JADX WARN: Removed duplicated region for block: B:48:0x00cd  */
        /* JADX WARN: Removed duplicated region for block: B:51:0x00da A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:56:0x00ec  */
        /* JADX WARN: Removed duplicated region for block: B:58:0x00ef A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:62:0x010d A[Catch: all -> 0x03e5, PHI: r2 r3
  0x010d: PHI (r2v63 aisble.Request) = (r2v1 aisble.Request), (r2v2 aisble.Request), (r2v1 aisble.Request) binds: [B:57:0x00ed, B:61:0x0104, B:47:0x00cb] A[DONT_GENERATE, DONT_INLINE]
  0x010d: PHI (r3v73 boolean) = (r3v5 boolean), (r3v5 boolean), (r3v74 boolean) binds: [B:57:0x00ed, B:61:0x0104, B:47:0x00cb] A[DONT_GENERATE, DONT_INLINE], TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:64:0x0113 A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:65:0x011e A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:70:0x0140  */
        /* JADX WARN: Removed duplicated region for block: B:71:0x0142 A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:74:0x017f A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:77:0x019d A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:78:0x01a6 A[Catch: all -> 0x03e5, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:86:0x01d4 A[Catch: all -> 0x03e5, TRY_ENTER, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:94:0x020e A[Catch: all -> 0x03e5, TRY_ENTER, TryCatch #1 {, blocks: (B:3:0x0001, B:9:0x000f, B:13:0x001a, B:14:0x001c, B:19:0x0023, B:21:0x002b, B:23:0x0037, B:27:0x005f, B:29:0x0063, B:33:0x006f, B:35:0x0073, B:37:0x0086, B:38:0x009e, B:40:0x00a7, B:43:0x00b3, B:62:0x010d, B:64:0x0113, B:68:0x012f, B:69:0x013d, B:144:0x03a5, B:151:0x03c9, B:147:0x03bb, B:71:0x0142, B:73:0x014a, B:74:0x017f, B:76:0x0187, B:77:0x019d, B:78:0x01a6, B:80:0x01ac, B:81:0x01b5, B:83:0x01bd, B:86:0x01d4, B:88:0x01da, B:89:0x01ef, B:91:0x01f7, B:94:0x020e, B:96:0x0214, B:98:0x0225, B:100:0x0231, B:101:0x0247, B:103:0x0253, B:105:0x0257, B:106:0x0264, B:108:0x026c, B:111:0x0287, B:112:0x0290, B:113:0x0299, B:114:0x02a2, B:115:0x02ab, B:116:0x02b6, B:117:0x02c1, B:118:0x02cc, B:119:0x02d7, B:120:0x02e0, B:121:0x02e9, B:123:0x02f1, B:126:0x0305, B:128:0x030c, B:129:0x0317, B:130:0x0320, B:131:0x032b, B:133:0x0332, B:134:0x0344, B:135:0x035e, B:136:0x0368, B:139:0x0374, B:140:0x037c, B:141:0x0384, B:142:0x038c, B:65:0x011e, B:67:0x0126, B:154:0x03d8, B:49:0x00cf, B:51:0x00da, B:53:0x00e2, B:58:0x00ef, B:61:0x0104, B:24:0x0048), top: B:163:0x0001, inners: #2 }] */
        @androidx.annotation.MainThread
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public synchronized void nextRequest(boolean r10) {
            /*
                Method dump skipped, instruction units count: 1058
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: aisble.BleManager.BleManagerGattCallback.nextRequest(boolean):void");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifyDeviceDisconnected(@NonNull BluetoothDevice bluetoothDevice) {
            boolean z = BleManager.this.mConnected;
            BleManager.this.mConnected = false;
            BleManager bleManager = BleManager.this;
            bleManager.mServicesDiscovered = false;
            bleManager.mServiceDiscoveryRequested = false;
            this.mInitInProgress = false;
            BleManager.this.mConnectionState = 0;
            if (!z) {
                BleManager.this.log(5, "Connection attempt timed out");
                BleManager.this.close();
                BleManager.this.mCallbacks.onDeviceDisconnected(bluetoothDevice);
            } else if (BleManager.this.mUserDisconnected) {
                BleManager.this.log(4, "Disconnected");
                BleManager.this.close();
                Request request = BleManager.this.mRequest;
                BleManager.this.mCallbacks.onDeviceDisconnected(bluetoothDevice);
                if (request != null && request.type == Request.Type.DISCONNECT) {
                    request.notifySuccess(bluetoothDevice);
                }
            } else {
                BleManager.this.log(5, "Connection lost");
                BleManager.this.mCallbacks.onLinkLossOccurred(bluetoothDevice);
            }
            if (BleManager.this.mUserDisconnected) {
                BleManager.this.mHandler.postDelayed(new Runnable() { // from class: aisble.BleManager.BleManagerGattCallback.1
                    @Override // java.lang.Runnable
                    public void run() {
                        BleManagerGattCallback.this.onDeviceDisconnected();
                    }
                }, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
            } else {
                onDeviceDisconnected();
            }
        }

        private void onError(BluetoothDevice bluetoothDevice, String str, int i) {
            BleManager.this.log(6, "Error (0x" + Integer.toHexString(i) + "): " + GattError.parse(i));
            BleManager.this.mCallbacks.onError(bluetoothDevice, str, i);
        }

        public void cancelQueue() {
            this.mTaskQueue.clear();
        }

        @Deprecated
        public Deque<Request> initGatt(@NonNull BluetoothGatt bluetoothGatt) {
            return null;
        }

        public void initialize() {
        }

        public boolean isOptionalServiceSupported(@NonNull BluetoothGatt bluetoothGatt) {
            return false;
        }

        public abstract boolean isRequiredServiceSupported(@NonNull BluetoothGatt bluetoothGatt);

        @Deprecated
        public void onBatteryValueReceived(@NonNull BluetoothGatt bluetoothGatt, @IntRange(from = 0, to = 100) int i) {
        }

        @Override // aisble.MainThreadBluetoothGattCallback
        public final void onCharacteristicChangedSafe(@NonNull BluetoothGatt bluetoothGatt, @NonNull BluetoothGattCharacteristic bluetoothGattCharacteristic, @Nullable byte[] bArr) {
            if (isServiceChangedCharacteristic(bluetoothGattCharacteristic)) {
                this.mOperationInProgress = true;
                cancelQueue();
                this.mInitQueue = null;
                BleManager.this.log(4, "Service Changed indication received");
                BleManager.this.log(2, "Discovering Services...");
                BleManager.this.log(3, "gatt.discoverServices()");
                bluetoothGatt.discoverServices();
                return;
            }
            BluetoothGattDescriptor descriptor = bluetoothGattCharacteristic.getDescriptor(BleManager.CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
            boolean z = descriptor == null || descriptor.getValue() == null || descriptor.getValue().length != 2 || descriptor.getValue()[0] == 1;
            String str = ParserUtils.parse(bArr);
            if (z) {
                BleManager.this.log(4, "Notification received from " + bluetoothGattCharacteristic.getUuid() + ", value: " + str);
                onCharacteristicNotified(bluetoothGatt, bluetoothGattCharacteristic);
            } else {
                BleManager.this.log(4, "Indication received from " + bluetoothGattCharacteristic.getUuid() + ", value: " + str);
                onCharacteristicIndicated(bluetoothGatt, bluetoothGattCharacteristic);
            }
            if (BleManager.this.mBatteryLevelNotificationCallback != null && isBatteryLevelCharacteristic(bluetoothGattCharacteristic)) {
                BleManager.this.mBatteryLevelNotificationCallback.notifyValueChanged(bluetoothGatt.getDevice(), bArr);
            }
            ValueChangedCallback valueChangedCallback = (ValueChangedCallback) BleManager.this.mNotificationCallbacks.get(bluetoothGattCharacteristic);
            if (valueChangedCallback != null && valueChangedCallback.matches(bArr)) {
                valueChangedCallback.notifyValueChanged(bluetoothGatt.getDevice(), bArr);
            }
            WaitForValueChangedRequest waitForValueChangedRequest = BleManager.this.mValueChangedRequest;
            if (waitForValueChangedRequest == null || waitForValueChangedRequest.characteristic != bluetoothGattCharacteristic || waitForValueChangedRequest.isTriggerPending() || !waitForValueChangedRequest.matches(bArr)) {
                return;
            }
            waitForValueChangedRequest.notifyValueChanged(bluetoothGatt.getDevice(), bArr);
            if (waitForValueChangedRequest.hasMore()) {
                return;
            }
            waitForValueChangedRequest.notifySuccess(bluetoothGatt.getDevice());
            BleManager.this.mValueChangedRequest = null;
            if (waitForValueChangedRequest.isTriggerCompleteOrNull()) {
                nextRequest(true);
            }
        }

        @Deprecated
        public void onCharacteristicIndicated(@NonNull BluetoothGatt bluetoothGatt, @NonNull BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        }

        @Deprecated
        public void onCharacteristicNotified(@NonNull BluetoothGatt bluetoothGatt, @NonNull BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        }

        @Deprecated
        public void onCharacteristicRead(@NonNull BluetoothGatt bluetoothGatt, @NonNull BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        }

        @Override // aisble.MainThreadBluetoothGattCallback
        public final void onCharacteristicReadSafe(@NonNull BluetoothGatt bluetoothGatt, @NonNull BluetoothGattCharacteristic bluetoothGattCharacteristic, @Nullable byte[] bArr, int i) {
            if (i == 0) {
                BleManager.this.log(4, "Read Response received from " + bluetoothGattCharacteristic.getUuid() + ", value: " + ParserUtils.parse(bArr));
                onCharacteristicRead(bluetoothGatt, bluetoothGattCharacteristic);
                if (BleManager.this.mRequest instanceof ReadRequest) {
                    ReadRequest readRequest = (ReadRequest) BleManager.this.mRequest;
                    boolean zMatches = readRequest.matches(bArr);
                    if (zMatches) {
                        readRequest.notifyValueChanged(bluetoothGatt.getDevice(), bArr);
                    }
                    if (!zMatches || readRequest.hasMore()) {
                        enqueueFirst(readRequest);
                    } else {
                        readRequest.notifySuccess(bluetoothGatt.getDevice());
                    }
                }
            } else {
                if (i == 5 || i == 8 || i == 137) {
                    BleManager.this.log(5, "Authentication required (" + i + ")");
                    if (bluetoothGatt.getDevice().getBondState() != 10) {
                        Log.w(BleManager.this.TAG, ERROR_AUTH_ERROR_WHILE_BONDED);
                        BleManager.this.mCallbacks.onError(bluetoothGatt.getDevice(), ERROR_AUTH_ERROR_WHILE_BONDED, i);
                        return;
                    }
                    return;
                }
                Log.e(BleManager.this.TAG, "onCharacteristicRead error " + i);
                if (BleManager.this.mRequest instanceof ReadRequest) {
                    BleManager.this.mRequest.notifyFail(bluetoothGatt.getDevice(), i);
                }
                BleManager.this.mValueChangedRequest = null;
                onError(bluetoothGatt.getDevice(), ERROR_READ_CHARACTERISTIC, i);
            }
            nextRequest(true);
        }

        @Deprecated
        public void onCharacteristicWrite(@NonNull BluetoothGatt bluetoothGatt, @NonNull BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        }

        @Override // aisble.MainThreadBluetoothGattCallback
        public final void onCharacteristicWriteSafe(@NonNull BluetoothGatt bluetoothGatt, @NonNull BluetoothGattCharacteristic bluetoothGattCharacteristic, @Nullable byte[] bArr, int i) {
            if (BleManager.this.mRequest instanceof WriteRequest) {
                if (BleManager.this.mWriteOpTimeoutRunable != null) {
                    BleManager bleManager = BleManager.this;
                    bleManager.mHandler.removeCallbacks(bleManager.mWriteOpTimeoutRunable);
                }
                ((WriteRequest) BleManager.this.mRequest).onWriteFinished();
            }
            if (i == 0) {
                BleManager.this.log(4, "Data written to " + bluetoothGattCharacteristic.getUuid() + ", value: " + ParserUtils.parse(bArr));
                onCharacteristicWrite(bluetoothGatt, bluetoothGattCharacteristic);
                if (BleManager.this.mRequest instanceof WriteRequest) {
                    WriteRequest writeRequest = (WriteRequest) BleManager.this.mRequest;
                    if (!writeRequest.notifyPacketSent(bluetoothGatt.getDevice(), bArr) && (BleManager.this.mRequestQueue instanceof ReliableWriteRequest)) {
                        writeRequest.notifyFail(bluetoothGatt.getDevice(), -6);
                        BleManager.this.mRequestQueue.cancelQueue();
                    } else if (writeRequest.hasMore()) {
                        enqueueFirst(writeRequest);
                    } else {
                        writeRequest.notifySuccess(bluetoothGatt.getDevice());
                    }
                }
            } else {
                if (i == 5 || i == 8 || i == 137) {
                    BleManager.this.log(5, "Authentication required (" + i + ")");
                    if (bluetoothGatt.getDevice().getBondState() != 10) {
                        Log.w(BleManager.this.TAG, ERROR_AUTH_ERROR_WHILE_BONDED);
                        BleManager.this.mCallbacks.onError(bluetoothGatt.getDevice(), ERROR_AUTH_ERROR_WHILE_BONDED, i);
                        return;
                    }
                    return;
                }
                Log.e(BleManager.this.TAG, "onCharacteristicWrite error " + i);
                if (BleManager.this.mRequest instanceof WriteRequest) {
                    BleManager.this.mRequest.notifyFail(bluetoothGatt.getDevice(), i);
                    if (BleManager.this.mRequestQueue instanceof ReliableWriteRequest) {
                        BleManager.this.mRequestQueue.cancelQueue();
                    }
                }
                BleManager.this.mValueChangedRequest = null;
                onError(bluetoothGatt.getDevice(), ERROR_WRITE_CHARACTERISTIC, i);
            }
            nextRequest(true);
        }

        @Override // aisble.MainThreadBluetoothGattCallback
        public final void onConnectionStateChangeSafe(@NonNull final BluetoothGatt bluetoothGatt, int i, int i2) {
            BleManager.this.log(3, "[Callback] Connection state changed with status: " + i + " and new state: " + i2 + " (" + BleManager.this.stateToString(i2) + ")");
            BleManager.this.cancelConnectTimeoutTask();
            if (i == 0 && i2 == 2) {
                if (BleManager.this.mBluetoothDevice == null) {
                    Log.e(BleManager.this.TAG, "Device received notification after disconnection.");
                    BleManager.this.log(3, "gatt.close()");
                    bluetoothGatt.close();
                    return;
                }
                BleManager.this.log(4, "Connected to " + bluetoothGatt.getDevice().getAddress());
                BleManager.this.mConnected = true;
                BleManager.this.mConnectionState = 2;
                BleManager.this.mCallbacks.onDeviceConnected(bluetoothGatt.getDevice());
                if (BleManager.this.mServiceDiscoveryRequested) {
                    return;
                }
                int serviceDiscoveryDelay = BleManager.this.getServiceDiscoveryDelay(bluetoothGatt.getDevice().getBondState() == 12);
                if (serviceDiscoveryDelay > 0) {
                    BleManager.this.log(3, "wait(" + serviceDiscoveryDelay + ")");
                }
                final int iAccess$1804 = BleManager.access$1804(BleManager.this);
                BleManager bleManager = BleManager.this;
                bleManager.mHandler.postDelayed(bleManager.mServiceDiscoveryRunnable = new Runnable() { // from class: aisble.BleManager.BleManagerGattCallback.2
                    @Override // java.lang.Runnable
                    public void run() {
                        if (iAccess$1804 == BleManager.this.mConnectionCount && BleManager.this.mConnected && bluetoothGatt.getDevice().getBondState() != 11) {
                            BleManager.this.mServiceDiscoveryRequested = true;
                            BleManager bleManager2 = BleManager.this;
                            bleManager2.mRetryDiscoveryServiceCount = 0;
                            bleManager2.log(2, "Discovering services...");
                            BleManager.this.log(3, "gatt.discoverServices()");
                            bluetoothGatt.discoverServices();
                        }
                    }
                }, serviceDiscoveryDelay);
                return;
            }
            if (i2 == 0) {
                long jElapsedRealtime = SystemClock.elapsedRealtime();
                boolean z = BleManager.this.mConnectionTime > 0;
                boolean z2 = z && jElapsedRealtime > BleManager.this.mConnectionTime + BleManager.CONNECTION_TIMEOUT_THRESHOLD;
                BleManager.this.mConnectionState = 0;
                if (i != 0) {
                    BleManager.this.log(5, "Error: (0x" + Integer.toHexString(i) + "): " + GattError.parseConnectionError(i));
                }
                BleManager.this.log(3, "canTimeout: " + z + ", timeout: " + z2 + ", mConnectRequest: " + BleManager.this.mConnectRequest);
                BleManager bleManager2 = BleManager.this;
                bleManager2.mHandler.removeCallbacks(bleManager2.mServiceDiscoveryRunnable);
                BleManager bleManager3 = BleManager.this;
                if (!bleManager3.mDeviceNotSupported && z && !z2 && bleManager3.mConnectRequest != null && BleManager.this.mConnectRequest.canRetry()) {
                    int retryDelay = BleManager.this.mConnectRequest.getRetryDelay();
                    if (retryDelay > 0) {
                        BleManager.this.log(3, "wait(" + retryDelay + ")");
                    }
                    BleManager.this.mHandler.postDelayed(new Runnable() { // from class: aisble.BleManager.BleManagerGattCallback.3
                        @Override // java.lang.Runnable
                        public void run() {
                            BleManager bleManager4 = BleManager.this;
                            if (bleManager4.mBluetoothGatt == null) {
                                bluetoothGatt.close();
                                return;
                            }
                            try {
                                bleManager4.internalReconnect(bluetoothGatt.getDevice(), BleManager.this.mConnectRequest);
                            } catch (Exception e) {
                                Log.e(BleManager.this.TAG, e.toString());
                            }
                        }
                    }, retryDelay);
                    return;
                }
                this.mOperationInProgress = true;
                cancelQueue();
                this.mInitQueue = null;
                BleManager bleManager4 = BleManager.this;
                bleManager4.mReady = false;
                boolean z3 = bleManager4.mConnected;
                notifyDeviceDisconnected(bluetoothGatt.getDevice());
                if (BleManager.this.mRequest != null && BleManager.this.mRequest.type != Request.Type.DISCONNECT && BleManager.this.mRequest.type != Request.Type.REMOVE_BOND) {
                    BleManager.this.mRequest.notifyFail(bluetoothGatt.getDevice(), i == 0 ? -1 : i);
                    BleManager.this.mRequest = null;
                }
                if (BleManager.this.mValueChangedRequest != null) {
                    BleManager.this.mValueChangedRequest.notifyFail(BleManager.this.mBluetoothDevice, -1);
                    BleManager.this.mValueChangedRequest = null;
                }
                if (BleManager.this.mConnectRequest != null) {
                    BleManager.this.mConnectRequest.notifyFail(bluetoothGatt.getDevice(), BleManager.this.mDeviceNotSupported ? -2 : i == 0 ? -1 : (i == 133 && z2) ? -5 : i);
                    BleManager.this.mConnectRequest = null;
                }
                this.mOperationInProgress = false;
                if (z3 && BleManager.this.mInitialConnection) {
                    BleManager.this.internalConnect(bluetoothGatt.getDevice(), null);
                } else {
                    BleManager.this.mInitialConnection = false;
                    nextRequest(false);
                }
                if (z3 || i == 0) {
                    return;
                }
            } else if (i != 0) {
                BleManager.this.log(6, "Error (0x" + Integer.toHexString(i) + "): " + GattError.parseConnectionError(i));
            }
            BleManager.this.mCallbacks.onError(bluetoothGatt.getDevice(), ERROR_CONNECTION_STATE_CHANGE, i);
        }

        @TargetApi(26)
        @Deprecated
        public void onConnectionUpdated(@NonNull BluetoothGatt bluetoothGatt, @IntRange(from = MediaDescriptionCompat.BT_FOLDER_TYPE_YEARS, to = 3200) int i, @IntRange(from = 0, to = 499) int i2, @IntRange(from = CustomFinderView.CUSTOME_ANIMATION_DELAY, to = 3200) int i3) {
        }

        @Override // aisble.MainThreadBluetoothGattCallback
        @RequiresApi(api = 26)
        public final void onConnectionUpdatedSafe(@NonNull BluetoothGatt bluetoothGatt, @IntRange(from = MediaDescriptionCompat.BT_FOLDER_TYPE_YEARS, to = 3200) int i, @IntRange(from = 0, to = 499) int i2, @IntRange(from = CustomFinderView.CUSTOME_ANIMATION_DELAY, to = 3200) int i3, int i4) {
            if (i4 == 0) {
                BleManager.this.log(4, "Connection parameters updated (interval: " + (((double) i) * 1.25d) + "ms, latency: " + i2 + ", timeout: " + (i3 * 10) + "ms)");
                onConnectionUpdated(bluetoothGatt, i, i2, i3);
                if (BleManager.this.mRequest instanceof ConnectionPriorityRequest) {
                    ((ConnectionPriorityRequest) BleManager.this.mRequest).notifyConnectionPriorityChanged(bluetoothGatt.getDevice(), i, i2, i3);
                    BleManager.this.mRequest.notifySuccess(bluetoothGatt.getDevice());
                }
            } else if (i4 == 59) {
                Log.e(BleManager.this.TAG, "onConnectionUpdated received status: Unacceptable connection interval, interval: " + i + ", latency: " + i2 + ", timeout: " + i3);
                BleManager.this.log(5, "Connection parameters update failed with status: UNACCEPT CONN INTERVAL (0x3b) (interval: " + (((double) i) * 1.25d) + "ms, latency: " + i2 + ", timeout: " + (i3 * 10) + "ms)");
                if (BleManager.this.mRequest instanceof ConnectionPriorityRequest) {
                    BleManager.this.mRequest.notifyFail(bluetoothGatt.getDevice(), i4);
                    BleManager.this.mValueChangedRequest = null;
                }
            } else {
                Log.e(BleManager.this.TAG, "onConnectionUpdated received status: " + i4 + ", interval: " + i + ", latency: " + i2 + ", timeout: " + i3);
                BleManager.this.log(5, "Connection parameters update failed with status " + i4 + " (interval: " + (((double) i) * 1.25d) + "ms, latency: " + i2 + ", timeout: " + (i3 * 10) + "ms)");
                if (BleManager.this.mRequest instanceof ConnectionPriorityRequest) {
                    BleManager.this.mRequest.notifyFail(bluetoothGatt.getDevice(), i4);
                    BleManager.this.mValueChangedRequest = null;
                }
            }
            if (this.mConnectionPriorityOperationInProgress) {
                this.mConnectionPriorityOperationInProgress = false;
                nextRequest(true);
            }
        }

        @Deprecated
        public void onDescriptorRead(@NonNull BluetoothGatt bluetoothGatt, @NonNull BluetoothGattDescriptor bluetoothGattDescriptor) {
        }

        @Override // aisble.MainThreadBluetoothGattCallback
        public void onDescriptorReadSafe(@NonNull BluetoothGatt bluetoothGatt, @NonNull BluetoothGattDescriptor bluetoothGattDescriptor, @Nullable byte[] bArr, int i) {
            if (i == 0) {
                BleManager.this.log(4, "Read Response received from descr. " + bluetoothGattDescriptor.getUuid() + ", value: " + ParserUtils.parse(bArr));
                onDescriptorRead(bluetoothGatt, bluetoothGattDescriptor);
                if (BleManager.this.mRequest instanceof ReadRequest) {
                    ReadRequest readRequest = (ReadRequest) BleManager.this.mRequest;
                    readRequest.notifyValueChanged(bluetoothGatt.getDevice(), bArr);
                    if (readRequest.hasMore()) {
                        enqueueFirst(readRequest);
                    } else {
                        readRequest.notifySuccess(bluetoothGatt.getDevice());
                    }
                }
            } else {
                if (i == 5 || i == 8 || i == 137) {
                    BleManager.this.log(5, "Authentication required (" + i + ")");
                    if (bluetoothGatt.getDevice().getBondState() != 10) {
                        Log.w(BleManager.this.TAG, ERROR_AUTH_ERROR_WHILE_BONDED);
                        BleManager.this.mCallbacks.onError(bluetoothGatt.getDevice(), ERROR_AUTH_ERROR_WHILE_BONDED, i);
                        return;
                    }
                    return;
                }
                Log.e(BleManager.this.TAG, "onDescriptorRead error " + i);
                if (BleManager.this.mRequest instanceof ReadRequest) {
                    BleManager.this.mRequest.notifyFail(bluetoothGatt.getDevice(), i);
                }
                BleManager.this.mValueChangedRequest = null;
                onError(bluetoothGatt.getDevice(), ERROR_READ_DESCRIPTOR, i);
            }
            nextRequest(true);
        }

        @Deprecated
        public void onDescriptorWrite(@NonNull BluetoothGatt bluetoothGatt, @NonNull BluetoothGattDescriptor bluetoothGattDescriptor) {
        }

        @Override // aisble.MainThreadBluetoothGattCallback
        public final void onDescriptorWriteSafe(@NonNull BluetoothGatt bluetoothGatt, @NonNull BluetoothGattDescriptor bluetoothGattDescriptor, @Nullable byte[] bArr, int i) {
            if (i == 0) {
                BleManager.this.log(4, "Data written to descr. " + bluetoothGattDescriptor.getUuid() + ", value: " + ParserUtils.parse(bArr));
                if (isServiceChangedCCCD(bluetoothGattDescriptor)) {
                    BleManager.this.log(4, "Service Changed notifications enabled");
                } else if (!isCCCD(bluetoothGattDescriptor)) {
                    onDescriptorWrite(bluetoothGatt, bluetoothGattDescriptor);
                } else if (bArr != null && bArr.length == 2 && bArr[1] == 0) {
                    byte b2 = bArr[0];
                    if (b2 == 0) {
                        BleManager.this.mNotificationCallbacks.remove(bluetoothGattDescriptor.getCharacteristic());
                        BleManager.this.log(4, "Notifications and indications disabled");
                    } else if (b2 == 1) {
                        BleManager.this.log(4, "Notifications enabled");
                    } else if (b2 == 2) {
                        BleManager.this.log(4, "Indications enabled");
                    }
                    onDescriptorWrite(bluetoothGatt, bluetoothGattDescriptor);
                }
                if (BleManager.this.mRequest instanceof WriteRequest) {
                    WriteRequest writeRequest = (WriteRequest) BleManager.this.mRequest;
                    if (!writeRequest.notifyPacketSent(bluetoothGatt.getDevice(), bArr) && (BleManager.this.mRequestQueue instanceof ReliableWriteRequest)) {
                        writeRequest.notifyFail(bluetoothGatt.getDevice(), -6);
                        BleManager.this.mRequestQueue.cancelQueue();
                    } else if (writeRequest.hasMore()) {
                        enqueueFirst(writeRequest);
                    } else {
                        writeRequest.notifySuccess(bluetoothGatt.getDevice());
                    }
                }
            } else {
                if (i == 5 || i == 8 || i == 137) {
                    BleManager.this.log(5, "Authentication required (" + i + ")");
                    if (bluetoothGatt.getDevice().getBondState() != 10) {
                        Log.w(BleManager.this.TAG, ERROR_AUTH_ERROR_WHILE_BONDED);
                        BleManager.this.mCallbacks.onError(bluetoothGatt.getDevice(), ERROR_AUTH_ERROR_WHILE_BONDED, i);
                        return;
                    }
                    return;
                }
                Log.e(BleManager.this.TAG, "onDescriptorWrite error " + i);
                if (BleManager.this.mRequest instanceof WriteRequest) {
                    BleManager.this.mRequest.notifyFail(bluetoothGatt.getDevice(), i);
                    if (BleManager.this.mRequestQueue instanceof ReliableWriteRequest) {
                        BleManager.this.mRequestQueue.cancelQueue();
                    }
                }
                BleManager.this.mValueChangedRequest = null;
                onError(bluetoothGatt.getDevice(), ERROR_WRITE_DESCRIPTOR, i);
            }
            nextRequest(true);
        }

        public abstract void onDeviceDisconnected();

        public void onDeviceReady() {
            BleManager bleManager = BleManager.this;
            BluetoothGatt bluetoothGatt = bleManager.mBluetoothGatt;
            if (bluetoothGatt == null) {
                return;
            }
            bleManager.mCallbacks.onDeviceReady(bluetoothGatt.getDevice());
        }

        public void onManagerReady() {
        }

        @Deprecated
        public void onMtuChanged(@NonNull BluetoothGatt bluetoothGatt, @IntRange(from = 23, to = 517) int i) {
        }

        @Override // aisble.MainThreadBluetoothGattCallback
        @RequiresApi(api = 21)
        public final void onMtuChangedSafe(@NonNull BluetoothGatt bluetoothGatt, @IntRange(from = 23, to = 517) int i, int i2) {
            if (i2 == 0) {
                BleManager.this.log(4, "MTU changed to: " + i);
                BleManager.this.mMtu = i;
                onMtuChanged(bluetoothGatt, i);
                if (BleManager.this.mRequest instanceof MtuRequest) {
                    ((MtuRequest) BleManager.this.mRequest).notifyMtuChanged(bluetoothGatt.getDevice(), i);
                    BleManager.this.mRequest.notifySuccess(bluetoothGatt.getDevice());
                }
            } else {
                Log.e(BleManager.this.TAG, "onMtuChanged error: " + i2 + ", mtu: " + i);
                if (BleManager.this.mRequest instanceof MtuRequest) {
                    BleManager.this.mRequest.notifyFail(bluetoothGatt.getDevice(), i2);
                    BleManager.this.mValueChangedRequest = null;
                }
            }
            nextRequest(true);
        }

        @Override // aisble.MainThreadBluetoothGattCallback
        @RequiresApi(api = 26)
        public final void onPhyReadSafe(@NonNull BluetoothGatt bluetoothGatt, int i, int i2, int i3) {
            if (i3 == 0) {
                BleManager.this.log(4, "PHY read (TX: " + BleManager.this.phyToString(i) + ", RX: " + BleManager.this.phyToString(i2) + ")");
                if (BleManager.this.mRequest instanceof PhyRequest) {
                    ((PhyRequest) BleManager.this.mRequest).notifyPhyChanged(bluetoothGatt.getDevice(), i, i2);
                    BleManager.this.mRequest.notifySuccess(bluetoothGatt.getDevice());
                }
            } else {
                BleManager.this.log(5, "PHY read failed with status " + i3);
                if (BleManager.this.mRequest instanceof PhyRequest) {
                    BleManager.this.mRequest.notifyFail(bluetoothGatt.getDevice(), i3);
                }
                BleManager.this.mValueChangedRequest = null;
                BleManager.this.mCallbacks.onError(bluetoothGatt.getDevice(), ERROR_READ_PHY, i3);
            }
            nextRequest(true);
        }

        @Override // aisble.MainThreadBluetoothGattCallback
        @RequiresApi(api = 26)
        public final void onPhyUpdateSafe(@NonNull BluetoothGatt bluetoothGatt, int i, int i2, int i3) {
            if (i3 == 0) {
                BleManager.this.log(4, "PHY updated (TX: " + BleManager.this.phyToString(i) + ", RX: " + BleManager.this.phyToString(i2) + ")");
                if (BleManager.this.mRequest instanceof PhyRequest) {
                    ((PhyRequest) BleManager.this.mRequest).notifyPhyChanged(bluetoothGatt.getDevice(), i, i2);
                    BleManager.this.mRequest.notifySuccess(bluetoothGatt.getDevice());
                }
            } else {
                BleManager.this.log(5, "PHY updated failed with status " + i3);
                if (BleManager.this.mRequest instanceof PhyRequest) {
                    BleManager.this.mRequest.notifyFail(bluetoothGatt.getDevice(), i3);
                    BleManager.this.mValueChangedRequest = null;
                }
                BleManager.this.mCallbacks.onError(bluetoothGatt.getDevice(), ERROR_PHY_UPDATE, i3);
            }
            if (BleManager.this.mRequest instanceof PhyRequest) {
                nextRequest(true);
            }
        }

        @Override // aisble.MainThreadBluetoothGattCallback
        public final void onReadRemoteRssiSafe(@NonNull BluetoothGatt bluetoothGatt, @IntRange(from = -128, to = 20) int i, int i2) {
            if (i2 == 0) {
                BleManager.this.log(4, "Remote RSSI received: " + i + " dBm");
                if (BleManager.this.mRequest instanceof ReadRssiRequest) {
                    ((ReadRssiRequest) BleManager.this.mRequest).notifyRssiRead(bluetoothGatt.getDevice(), i);
                    BleManager.this.mRequest.notifySuccess(bluetoothGatt.getDevice());
                }
            } else {
                BleManager.this.log(5, "Reading remote RSSI failed with status " + i2);
                if (BleManager.this.mRequest instanceof ReadRssiRequest) {
                    BleManager.this.mRequest.notifyFail(bluetoothGatt.getDevice(), i2);
                }
                BleManager.this.mValueChangedRequest = null;
                BleManager.this.mCallbacks.onError(bluetoothGatt.getDevice(), ERROR_READ_RSSI, i2);
            }
            nextRequest(true);
        }

        @Override // aisble.MainThreadBluetoothGattCallback
        public final void onReliableWriteCompletedSafe(@NonNull BluetoothGatt bluetoothGatt, int i) {
            boolean z = BleManager.this.mRequest.type == Request.Type.EXECUTE_RELIABLE_WRITE;
            BleManager.this.mReliableWriteInProgress = false;
            if (i != 0) {
                Log.e(BleManager.this.TAG, "onReliableWriteCompleted execute " + z + ", error " + i);
                BleManager.this.mRequest.notifyFail(bluetoothGatt.getDevice(), i);
                onError(bluetoothGatt.getDevice(), ERROR_RELIABLE_WRITE, i);
            } else if (z) {
                BleManager.this.log(4, "Reliable Write executed");
                BleManager.this.mRequest.notifySuccess(bluetoothGatt.getDevice());
            } else {
                BleManager.this.log(5, "Reliable Write aborted");
                BleManager.this.mRequest.notifySuccess(bluetoothGatt.getDevice());
                BleManager.this.mRequestQueue.notifyFail(bluetoothGatt.getDevice(), -4);
            }
            nextRequest(true);
        }

        @Override // aisble.MainThreadBluetoothGattCallback
        public final void onServicesDiscoveredSafe(@NonNull final BluetoothGatt bluetoothGatt, int i) {
            BleManager.this.mServiceDiscoveryRequested = false;
            if (i != 0) {
                Log.e(BleManager.this.TAG, "onServicesDiscovered error " + i);
                onError(bluetoothGatt.getDevice(), ERROR_DISCOVERY_SERVICE, i);
                if (BleManager.this.mConnectRequest != null) {
                    BleManager.this.mConnectRequest.notifyFail(bluetoothGatt.getDevice(), -4);
                    BleManager.this.mConnectRequest = null;
                }
                BleManager.this.internalDisconnect();
                return;
            }
            BleManager.this.log(4, "Services discovered");
            BleManager.this.mServicesDiscovered = true;
            if (!isRequiredServiceSupported(bluetoothGatt)) {
                BleManager.this.log(5, "Device is not supported");
                BleManager bleManager = BleManager.this;
                int i2 = bleManager.mRetryDiscoveryServiceCount + 1;
                bleManager.mRetryDiscoveryServiceCount = i2;
                if (!bleManager.shouldRetryDiscoveryServiceWhenDeviceNotSupported(i2)) {
                    BleManager bleManager2 = BleManager.this;
                    bleManager2.mDeviceNotSupported = true;
                    bleManager2.internalDisconnect();
                    return;
                }
                int serviceDiscoveryDelay = BleManager.this.getServiceDiscoveryDelay(bluetoothGatt.getDevice().getBondState() == 12);
                if (serviceDiscoveryDelay > 0) {
                    BleManager.this.log(3, "wait(" + serviceDiscoveryDelay + ") try again");
                }
                BleManager.this.mHandler.postDelayed(new Runnable() { // from class: aisble.BleManager.BleManagerGattCallback.4
                    @Override // java.lang.Runnable
                    public void run() {
                        if (!BleManager.this.mConnected || bluetoothGatt.getDevice().getBondState() == 11) {
                            return;
                        }
                        BleManager.this.mServiceDiscoveryRequested = true;
                        BleManager.this.log(2, "Retry discovering services...");
                        BleManager.this.log(3, "gatt.discoverServices()");
                        BleManager.this.internalRefreshDeviceCache();
                        bluetoothGatt.discoverServices();
                    }
                }, serviceDiscoveryDelay);
                return;
            }
            BleManager.this.log(2, "Primary service found");
            boolean zIsOptionalServiceSupported = isOptionalServiceSupported(bluetoothGatt);
            if (zIsOptionalServiceSupported) {
                BleManager.this.log(2, "Secondary service found");
            }
            BleManager.this.mCallbacks.onServicesDiscovered(bluetoothGatt.getDevice(), zIsOptionalServiceSupported);
            this.mInitInProgress = true;
            this.mOperationInProgress = true;
            this.mInitQueue = initGatt(bluetoothGatt);
            boolean z = this.mInitQueue != null;
            if (z) {
                Iterator<Request> it = this.mInitQueue.iterator();
                while (it.hasNext()) {
                    it.next().enqueued = true;
                }
            }
            if (this.mInitQueue == null) {
                this.mInitQueue = new LinkedList();
            }
            int i3 = Build.VERSION.SDK_INT;
            if (i3 < 23 || i3 == 26 || i3 == 27 || i3 == 28) {
                enqueueFirst(Request.newEnableServiceChangedIndicationsRequest().setManager(BleManager.this));
            }
            if (z) {
                BleManager.this.readBatteryLevel();
                if (BleManager.this.mCallbacks.shouldEnableBatteryLevelNotifications(bluetoothGatt.getDevice())) {
                    BleManager.this.enableBatteryLevelNotifications();
                }
            }
            initialize();
            this.mInitInProgress = false;
            nextRequest(true);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PairingVariant {
    }

    public BleManager(@NonNull Context context, @NonNull String str) {
        this(context);
        this.TAG += str;
    }

    public static /* synthetic */ int access$1804(BleManager bleManager) {
        int i = bleManager.mConnectionCount + 1;
        bleManager.mConnectionCount = i;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelConnectTimeoutTask() {
        Runnable runnable = this.mConnectTimeoutTask;
        if (runnable != null) {
            this.mHandler.removeCallbacks(runnable);
            this.mConnectTimeoutTask = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean ensureServiceChangedEnabled() {
        BluetoothGattService service;
        BluetoothGattCharacteristic characteristic;
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt == null || !this.mConnected || bluetoothGatt.getDevice().getBondState() != 12 || (service = bluetoothGatt.getService(GENERIC_ATTRIBUTE_SERVICE)) == null || (characteristic = service.getCharacteristic(SERVICE_CHANGED_CHARACTERISTIC)) == null) {
            return false;
        }
        log(4, "Service Changed characteristic found on a bonded device");
        return internalEnableIndications(characteristic);
    }

    public static BluetoothGattDescriptor getCccd(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
        if (bluetoothGattCharacteristic == null || (i & bluetoothGattCharacteristic.getProperties()) == 0) {
            return null;
        }
        return bluetoothGattCharacteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
    }

    private void installConnectTimeoutTask(long j) {
        Handler handler = this.mHandler;
        Runnable runnable = new Runnable() { // from class: aisble.BleManager.9
            @Override // java.lang.Runnable
            public void run() {
                if (BleManager.this.mConnected) {
                    return;
                }
                BleManager.this.close();
            }
        };
        this.mConnectTimeoutTask = runnable;
        handler.postDelayed(runnable, j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @MainThread
    public boolean internalAbortReliableWrite() {
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt == null || !this.mConnected || !this.mReliableWriteInProgress) {
            return false;
        }
        log(2, "Aborting reliable write...");
        if (Build.VERSION.SDK_INT >= 19) {
            log(3, "gatt.abortReliableWrite()");
            bluetoothGatt.abortReliableWrite();
            return true;
        }
        log(3, "gatt.abortReliableWrite(device)");
        bluetoothGatt.abortReliableWrite(this.mBluetoothDevice);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @MainThread
    public boolean internalBeginReliableWrite() {
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt == null || !this.mConnected) {
            return false;
        }
        if (this.mReliableWriteInProgress) {
            return true;
        }
        log(2, "Beginning reliable write...");
        log(3, "gatt.beginReliableWrite()");
        boolean zBeginReliableWrite = bluetoothGatt.beginReliableWrite();
        this.mReliableWriteInProgress = zBeginReliableWrite;
        return zBeginReliableWrite;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @MainThread
    public boolean internalConnect(@NonNull BluetoothDevice bluetoothDevice, @Nullable ConnectRequest connectRequest) {
        this.mDeviceNotSupported = false;
        this.mServiceDiscoveryRequested = false;
        boolean zIsEnabled = BluetoothAdapter.getDefaultAdapter().isEnabled();
        if (this.mConnected || !zIsEnabled) {
            BluetoothDevice bluetoothDevice2 = this.mBluetoothDevice;
            if (zIsEnabled && bluetoothDevice2 != null && bluetoothDevice2.equals(bluetoothDevice)) {
                this.mConnectRequest.notifySuccess(bluetoothDevice);
            } else {
                ConnectRequest connectRequest2 = this.mConnectRequest;
                if (connectRequest2 != null) {
                    connectRequest2.notifyFail(bluetoothDevice, zIsEnabled ? -4 : -100);
                }
            }
            this.mConnectRequest = null;
            BleManager<E>.BleManagerGattCallback bleManagerGattCallback = this.mGattCallback;
            if (bleManagerGattCallback != null) {
                bleManagerGattCallback.nextRequest(true);
            }
            return true;
        }
        synchronized (this.mLock) {
            if (this.mBluetoothGatt == null) {
                this.mContext.registerReceiver(this.mBluetoothStateBroadcastReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
                this.mContext.registerReceiver(this.mBondingBroadcastReceiver, new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED"));
                this.mContext.registerReceiver(this.mPairingRequestBroadcastReceiver, new IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST"));
            } else {
                if (this.mInitialConnection) {
                    this.mInitialConnection = false;
                    this.mConnectionTime = 0L;
                    this.mConnectionState = 1;
                    log(2, "Connecting...");
                    this.mCallbacks.onDeviceConnecting(bluetoothDevice);
                    log(3, "gatt.connect()");
                    this.mBluetoothGatt.connect();
                    return true;
                }
                log(3, "gatt.close()");
                this.mBluetoothGatt.close();
                this.mBluetoothGatt = null;
                try {
                    log(3, "wait(200)");
                    Thread.sleep(200L);
                } catch (InterruptedException unused) {
                }
            }
            if (connectRequest == null) {
                return false;
            }
            boolean zShouldAutoConnect = connectRequest.shouldAutoConnect();
            this.mUserDisconnected = !zShouldAutoConnect;
            if (zShouldAutoConnect) {
                this.mInitialConnection = true;
            }
            this.mBluetoothDevice = bluetoothDevice;
            this.mGattCallback.setHandler(this.mHandler);
            log(2, connectRequest.isFirstAttempt() ? "Connecting..." : "Retrying...");
            this.mConnectionState = 1;
            this.mCallbacks.onDeviceConnecting(bluetoothDevice);
            this.mConnectionTime = SystemClock.elapsedRealtime();
            String str = Build.MODEL;
            int i = Build.VERSION.SDK_INT;
            if (i >= 26) {
                int preferredPhy = connectRequest.getPreferredPhy();
                log(3, "gatt = device.connectGatt(autoConnect = false, TRANSPORT_LE, " + phyMaskToString(preferredPhy) + ")");
                this.mBluetoothGatt = bluetoothDevice.connectGatt(this.mContext, false, this.mGattCallback, 2, preferredPhy);
            } else if (i >= 23) {
                log(3, "gatt = device.connectGatt(autoConnect = false, TRANSPORT_LE)");
                this.mBluetoothGatt = bluetoothDevice.connectGatt(this.mContext, false, this.mGattCallback, 2);
            } else {
                log(3, "gatt = device.connectGatt(autoConnect = false)");
                this.mBluetoothGatt = bluetoothDevice.connectGatt(this.mContext, false, this.mGattCallback);
            }
            long connectionTimeout = getConnectionTimeout();
            if (connectionTimeout > 0) {
                installConnectTimeoutTask(connectionTimeout);
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @MainThread
    public boolean internalCreateBond() {
        BluetoothDevice bluetoothDevice = this.mBluetoothDevice;
        if (bluetoothDevice == null) {
            return false;
        }
        log(2, "Starting pairing...");
        if (bluetoothDevice.getBondState() == 12) {
            log(5, "Device already bonded");
            this.mRequest.notifySuccess(bluetoothDevice);
            this.mGattCallback.nextRequest(true);
            return true;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            log(3, "device.createBond()");
            return bluetoothDevice.createBond();
        }
        try {
            Method method = bluetoothDevice.getClass().getMethod("createBond", new Class[0]);
            log(3, "device.createBond() (hidden)");
            return ((Boolean) method.invoke(bluetoothDevice, new Object[0])).booleanValue();
        } catch (Exception e) {
            Log.w(this.TAG, "An exception occurred while creating bond", e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @MainThread
    public boolean internalDisableIndications(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return internalDisableNotifications(bluetoothGattCharacteristic);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @MainThread
    public boolean internalDisableNotifications(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothGattDescriptor cccd;
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt == null || bluetoothGattCharacteristic == null || !this.mConnected || (cccd = getCccd(bluetoothGattCharacteristic, 16)) == null) {
            return false;
        }
        log(3, "gatt.setCharacteristicNotification(" + bluetoothGattCharacteristic.getUuid() + ", false)");
        bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, false);
        cccd.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        log(2, "Disabling notifications and indications for " + bluetoothGattCharacteristic.getUuid());
        log(3, "gatt.writeDescriptor(" + CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID + ", value=0x00-00)");
        return internalWriteDescriptorWorkaround(cccd);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @MainThread
    public boolean internalDisconnect() {
        this.mUserDisconnected = true;
        this.mInitialConnection = false;
        this.mReady = false;
        if (this.mBluetoothGatt != null) {
            this.mConnectionState = 3;
            log(2, this.mConnected ? "Disconnecting..." : "Cancelling connection...");
            this.mCallbacks.onDeviceDisconnecting(this.mBluetoothGatt.getDevice());
            boolean z = this.mConnected;
            log(3, "gatt.disconnect()");
            this.mBluetoothGatt.disconnect();
            if (z) {
                return true;
            }
            this.mConnectionState = 0;
            log(4, "Disconnected");
            this.mCallbacks.onDeviceDisconnected(this.mBluetoothGatt.getDevice());
        }
        Request request = this.mRequest;
        if (request != null && request.type == Request.Type.DISCONNECT) {
            BluetoothDevice bluetoothDevice = this.mBluetoothDevice;
            if (bluetoothDevice != null) {
                request.notifySuccess(bluetoothDevice);
            } else {
                request.notifyInvalidRequest();
            }
        }
        BleManager<E>.BleManagerGattCallback bleManagerGattCallback = this.mGattCallback;
        if (bleManagerGattCallback != null) {
            bleManagerGattCallback.nextRequest(true);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @MainThread
    public boolean internalEnableIndications(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt != null && bluetoothGattCharacteristic != null && this.mConnected) {
            bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, true);
            BluetoothGattDescriptor cccd = getCccd(bluetoothGattCharacteristic, 32);
            if (cccd != null) {
                log(3, "gatt.setCharacteristicNotification(" + bluetoothGattCharacteristic.getUuid() + ", true)");
                cccd.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                log(2, "Enabling indications for " + bluetoothGattCharacteristic.getUuid());
                log(3, "gatt.writeDescriptor(" + CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID + ", value=0x02-00)");
                return internalWriteDescriptorWorkaround(cccd);
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @MainThread
    public boolean internalEnableNotifications(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt != null && bluetoothGattCharacteristic != null && this.mConnected) {
            bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, true);
            BluetoothGattDescriptor cccd = getCccd(bluetoothGattCharacteristic, 16);
            if (cccd != null) {
                log(3, "gatt.setCharacteristicNotification(" + bluetoothGattCharacteristic.getUuid() + ", true)");
                cccd.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                log(2, "Enabling notifications for " + bluetoothGattCharacteristic.getUuid());
                log(3, "gatt.writeDescriptor(" + CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID + ", value=0x01-00)");
                return internalWriteDescriptorWorkaround(cccd);
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @MainThread
    public boolean internalExecuteReliableWrite() {
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt == null || !this.mConnected || !this.mReliableWriteInProgress) {
            return false;
        }
        log(2, "Executing reliable write...");
        log(3, "gatt.executeReliableWrite()");
        return bluetoothGatt.executeReliableWrite();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @MainThread
    @Deprecated
    public boolean internalReadBatteryLevel() {
        BluetoothGattService service;
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt == null || !this.mConnected || (service = bluetoothGatt.getService(BATTERY_SERVICE)) == null) {
            return false;
        }
        return internalReadCharacteristic(service.getCharacteristic(BATTERY_LEVEL_CHARACTERISTIC));
    }

    /* JADX INFO: Access modifiers changed from: private */
    @MainThread
    public boolean internalReadCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt == null || bluetoothGattCharacteristic == null || !this.mConnected) {
            return false;
        }
        if ((bluetoothGattCharacteristic.getProperties() & 2) == 0) {
            log(5, "Reading characteristic failed");
            return false;
        }
        log(2, "Reading characteristic " + bluetoothGattCharacteristic.getUuid());
        log(3, "gatt.readCharacteristic(" + bluetoothGattCharacteristic.getUuid() + ")");
        return bluetoothGatt.readCharacteristic(bluetoothGattCharacteristic);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @MainThread
    public boolean internalReadDescriptor(BluetoothGattDescriptor bluetoothGattDescriptor) {
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt == null || bluetoothGattDescriptor == null || !this.mConnected) {
            return false;
        }
        log(2, "Reading descriptor " + bluetoothGattDescriptor.getUuid());
        log(3, "gatt.readDescriptor(" + bluetoothGattDescriptor.getUuid() + ")");
        return bluetoothGatt.readDescriptor(bluetoothGattDescriptor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @RequiresApi(api = 26)
    @MainThread
    public boolean internalReadPhy() {
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt == null || !this.mConnected) {
            return false;
        }
        log(2, "Reading PHY...");
        log(3, "gatt.readPhy()");
        bluetoothGatt.readPhy();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @MainThread
    public boolean internalReadRssi() {
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt == null || !this.mConnected) {
            return false;
        }
        log(2, "Reading remote RSSI...");
        log(3, "gatt.readRemoteRssi()");
        return bluetoothGatt.readRemoteRssi();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @MainThread
    public boolean internalReconnect(@NonNull BluetoothDevice bluetoothDevice, @Nullable ConnectRequest connectRequest) {
        this.mDeviceNotSupported = false;
        this.mServiceDiscoveryRequested = false;
        if (this.mGattCallback == null) {
            return true;
        }
        boolean zIsEnabled = BluetoothAdapter.getDefaultAdapter().isEnabled();
        if (!zIsEnabled) {
            BluetoothDevice bluetoothDevice2 = this.mBluetoothDevice;
            ConnectRequest connectRequest2 = this.mConnectRequest;
            if (connectRequest2 != null) {
                connectRequest2.notifyFail(bluetoothDevice, zIsEnabled ? -4 : -100);
            }
            this.mConnectRequest = null;
            BleManager<E>.BleManagerGattCallback bleManagerGattCallback = this.mGattCallback;
            if (bleManagerGattCallback != null) {
                bleManagerGattCallback.nextRequest(true);
            }
            return true;
        }
        synchronized (this.mLock) {
            if (this.mBluetoothGatt == null) {
                this.mContext.registerReceiver(this.mBluetoothStateBroadcastReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
                this.mContext.registerReceiver(this.mBondingBroadcastReceiver, new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED"));
                this.mContext.registerReceiver(this.mPairingRequestBroadcastReceiver, new IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST"));
            } else {
                if (this.mInitialConnection) {
                    this.mInitialConnection = false;
                    this.mConnectionTime = 0L;
                    this.mConnectionState = 1;
                    log(2, "Connecting...");
                    this.mCallbacks.onDeviceConnecting(bluetoothDevice);
                    log(3, "gatt.connect()");
                    this.mBluetoothGatt.connect();
                    return true;
                }
                log(3, "gatt.close()");
                this.mBluetoothGatt.close();
                this.mBluetoothGatt = null;
                try {
                    log(3, "wait(200)");
                    Thread.sleep(200L);
                } catch (InterruptedException unused) {
                }
            }
            if (connectRequest == null || this.mGattCallback == null) {
                return false;
            }
            boolean zShouldAutoConnect = connectRequest.shouldAutoConnect();
            this.mUserDisconnected = !zShouldAutoConnect;
            if (zShouldAutoConnect) {
                this.mInitialConnection = true;
            }
            this.mBluetoothDevice = bluetoothDevice;
            this.mGattCallback.setHandler(this.mHandler);
            log(2, connectRequest.isFirstAttempt() ? "Connecting..." : "Retrying...");
            this.mConnectionState = 1;
            this.mCallbacks.onDeviceConnecting(bluetoothDevice);
            this.mConnectionTime = SystemClock.elapsedRealtime();
            int i = Build.VERSION.SDK_INT;
            if (i >= 26) {
                int preferredPhy = connectRequest.getPreferredPhy();
                log(3, "gatt = device.connectGatt(autoConnect = false, TRANSPORT_LE, " + phyMaskToString(preferredPhy) + ")");
                this.mBluetoothGatt = bluetoothDevice.connectGatt(this.mContext, false, this.mGattCallback, 2, preferredPhy);
            } else if (i >= 23) {
                log(3, "gatt = device.connectGatt(autoConnect = false, TRANSPORT_LE)");
                this.mBluetoothGatt = bluetoothDevice.connectGatt(this.mContext, false, this.mGattCallback, 2);
            } else {
                log(3, "gatt = device.connectGatt(autoConnect = false)");
                this.mBluetoothGatt = bluetoothDevice.connectGatt(this.mContext, false, this.mGattCallback);
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @MainThread
    public boolean internalRefreshDeviceCache() {
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt == null) {
            return false;
        }
        log(2, "Refreshing device cache...");
        log(3, "gatt.refresh() (hidden)");
        try {
            return ((Boolean) bluetoothGatt.getClass().getMethod("refresh", new Class[0]).invoke(bluetoothGatt, new Object[0])).booleanValue();
        } catch (Exception e) {
            Log.w(this.TAG, "An exception occurred while refreshing device", e);
            log(5, "gatt.refresh() method not found");
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @MainThread
    public boolean internalRemoveBond() {
        BluetoothDevice bluetoothDevice = this.mBluetoothDevice;
        if (bluetoothDevice == null) {
            return false;
        }
        log(2, "Removing bond information...");
        if (bluetoothDevice.getBondState() == 10) {
            log(5, "Device is not bonded");
            this.mRequest.notifySuccess(bluetoothDevice);
            this.mGattCallback.nextRequest(true);
            return true;
        }
        try {
            Method method = bluetoothDevice.getClass().getMethod("removeBond", new Class[0]);
            log(3, "device.removeBond() (hidden)");
            return ((Boolean) method.invoke(bluetoothDevice, new Object[0])).booleanValue();
        } catch (Exception e) {
            Log.w(this.TAG, "An exception occurred while removing bond", e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @RequiresApi(api = 21)
    @MainThread
    public boolean internalRequestConnectionPriority(int i) {
        String str;
        String str2;
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt == null || !this.mConnected) {
            return false;
        }
        if (i == 1) {
            str = Build.VERSION.SDK_INT >= 23 ? "HIGH (11.25–15ms, 0, 20s)" : "HIGH (7.5–10ms, 0, 20s)";
            str2 = "HIGH";
        } else if (i != 2) {
            str = "BALANCED (30–50ms, 0, 20s)";
            str2 = "BALANCED";
        } else {
            str = "LOW POWER (100–125ms, 2, 20s)";
            str2 = "LOW POWER";
        }
        log(2, "Requesting connection priority: " + str + "...");
        log(3, "gatt.requestConnectionPriority(" + str2 + ")");
        return bluetoothGatt.requestConnectionPriority(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @RequiresApi(api = 21)
    @MainThread
    public boolean internalRequestMtu(@IntRange(from = 23, to = 517) int i) {
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt == null || !this.mConnected) {
            return false;
        }
        log(2, "Requesting new MTU...");
        log(3, "gatt.requestMtu(" + i + ")");
        return bluetoothGatt.requestMtu(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @MainThread
    @Deprecated
    public boolean internalSetBatteryNotifications(boolean z) {
        BluetoothGattService service;
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt == null || !this.mConnected || (service = bluetoothGatt.getService(BATTERY_SERVICE)) == null) {
            return false;
        }
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(BATTERY_LEVEL_CHARACTERISTIC);
        return z ? internalEnableNotifications(characteristic) : internalDisableNotifications(characteristic);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @RequiresApi(api = 26)
    @MainThread
    public boolean internalSetPreferredPhy(int i, int i2, int i3) {
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt == null || !this.mConnected) {
            return false;
        }
        log(2, "Requesting preferred PHYs...");
        log(3, "gatt.setPreferredPhy(" + phyMaskToString(i) + ", " + phyMaskToString(i2) + ", coding option = " + phyCodedOptionToString(i3) + ")");
        bluetoothGatt.setPreferredPhy(i, i2, i3);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @MainThread
    public boolean internalWriteCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt == null || bluetoothGattCharacteristic == null || !this.mConnected || (bluetoothGattCharacteristic.getProperties() & 12) == 0) {
            return false;
        }
        log(2, "Writing characteristic " + bluetoothGattCharacteristic.getUuid() + " (" + writeTypeToString(bluetoothGattCharacteristic.getWriteType()) + ")");
        StringBuilder sb = new StringBuilder();
        sb.append("gatt.writeCharacteristic(");
        sb.append(bluetoothGattCharacteristic.getUuid());
        sb.append(")");
        log(3, sb.toString());
        return bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @MainThread
    public boolean internalWriteDescriptor(BluetoothGattDescriptor bluetoothGattDescriptor) {
        if (this.mBluetoothGatt == null || bluetoothGattDescriptor == null || !this.mConnected) {
            return false;
        }
        log(2, "Writing descriptor " + bluetoothGattDescriptor.getUuid());
        log(3, "gatt.writeDescriptor(" + bluetoothGattDescriptor.getUuid() + ")");
        return internalWriteDescriptorWorkaround(bluetoothGattDescriptor);
    }

    @MainThread
    private boolean internalWriteDescriptorWorkaround(BluetoothGattDescriptor bluetoothGattDescriptor) {
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt == null || bluetoothGattDescriptor == null || !this.mConnected) {
            return false;
        }
        BluetoothGattCharacteristic characteristic = bluetoothGattDescriptor.getCharacteristic();
        int writeType = characteristic.getWriteType();
        characteristic.setWriteType(2);
        boolean zWriteDescriptor = bluetoothGatt.writeDescriptor(bluetoothGattDescriptor);
        characteristic.setWriteType(writeType);
        return zWriteDescriptor;
    }

    @RequiresApi(26)
    private String phyCodedOptionToString(int i) {
        if (i == 0) {
            return "No preferred";
        }
        if (i == 1) {
            return "S2";
        }
        if (i == 2) {
            return "S8";
        }
        return "UNKNOWN (" + i + ")";
    }

    @RequiresApi(26)
    private String phyMaskToString(int i) {
        switch (i) {
            case 1:
                return "LE 1M";
            case 2:
                return "LE 2M";
            case 3:
                return "LE 1M or LE 2M";
            case 4:
                return "LE Coded";
            case 5:
                return "LE 1M or LE Coded";
            case 6:
                return "LE 2M or LE Coded";
            case 7:
                return "LE 1M, LE 2M or LE Coded";
            default:
                return "UNKNOWN (" + i + ")";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @RequiresApi(26)
    public String phyToString(int i) {
        if (i == 1) {
            return "LE 1M";
        }
        if (i == 2) {
            return "LE 2M";
        }
        if (i == 3) {
            return "LE Coded";
        }
        return "UNKNOWN (" + i + ")";
    }

    private void runOnUiThread(Runnable runnable) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            this.mHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    @NonNull
    public RequestQueue beginAtomicRequestQueue() {
        return new RequestQueue().setManager((BleManager) this);
    }

    @NonNull
    public ReliableWriteRequest beginReliableWrite() {
        return Request.newReliableWriteRequest().setManager((BleManager) this);
    }

    public String bondStateToString(int i) {
        switch (i) {
            case 10:
                return "BOND_NONE";
            case 11:
                return "BOND_BONDING";
            case 12:
                return "BOND_BONDED";
            default:
                return GrsBaseInfo.CountryCodeSource.UNKNOWN;
        }
    }

    public void clearQueue() {
        BleManager<E>.BleManagerGattCallback bleManagerGattCallback = this.mGattCallback;
        if (bleManagerGattCallback != null) {
            bleManagerGattCallback.cancelQueue();
        }
    }

    public void close() {
        try {
            this.mContext.unregisterReceiver(this.mBluetoothStateBroadcastReceiver);
            this.mContext.unregisterReceiver(this.mBondingBroadcastReceiver);
            this.mContext.unregisterReceiver(this.mPairingRequestBroadcastReceiver);
        } catch (Exception unused) {
        }
        synchronized (this.mLock) {
            if (this.mBluetoothGatt != null) {
                if (shouldClearCacheWhenDisconnected()) {
                    if (internalRefreshDeviceCache()) {
                        log(4, "Cache refreshed");
                    } else {
                        log(5, "Refreshing failed");
                    }
                }
                log(3, "gatt.close()");
                this.mBluetoothGatt.close();
                this.mBluetoothGatt = null;
            }
            this.mConnected = false;
            this.mInitialConnection = false;
            this.mReliableWriteInProgress = false;
            this.mNotificationCallbacks.clear();
            this.mConnectionState = 0;
            if (this.mGattCallback != null) {
                this.mGattCallback.cancelQueue();
                this.mGattCallback.mInitQueue = null;
            }
            this.mGattCallback = null;
            this.mBluetoothDevice = null;
        }
    }

    @NonNull
    public final ConnectRequest connect(@NonNull BluetoothDevice bluetoothDevice) {
        if (this.mCallbacks == null) {
            throw new NullPointerException("Set callbacks using setGattCallbacks(E callbacks) before connecting");
        }
        if (bluetoothDevice != null) {
            return Request.connect(bluetoothDevice).useAutoConnect(shouldAutoConnect()).setManager((BleManager) this);
        }
        throw new NullPointerException("Bluetooth device not specified");
    }

    @NonNull
    public Request createBond() {
        return Request.createBond().setManager(this);
    }

    @Deprecated
    public void disableBatteryLevelNotifications() {
        Request.newDisableBatteryLevelNotificationsRequest().setManager((BleManager) this).done(new SuccessCallback() { // from class: aisble.BleManager.7
            @Override // aisble.callback.SuccessCallback
            public void onRequestCompleted(@NonNull BluetoothDevice bluetoothDevice) {
                BleManager.this.log(4, "Battery Level notifications disabled");
            }
        }).enqueue();
    }

    @NonNull
    public WriteRequest disableIndications(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return Request.newDisableIndicationsRequest(bluetoothGattCharacteristic).setManager((BleManager) this);
    }

    @NonNull
    public WriteRequest disableNotifications(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return Request.newDisableNotificationsRequest(bluetoothGattCharacteristic).setManager((BleManager) this);
    }

    @NonNull
    public final DisconnectRequest disconnect() {
        return Request.disconnect().setManager((BleManager) this);
    }

    public final void disconnectImmediately() {
        internalDisconnect();
    }

    @Deprecated
    public void enableBatteryLevelNotifications() {
        if (this.mBatteryLevelNotificationCallback == null) {
            this.mBatteryLevelNotificationCallback = new ValueChangedCallback().with(new DataReceivedCallback() { // from class: aisble.BleManager.5
                @Override // aisble.callback.DataReceivedCallback
                public void onDataReceived(@NonNull BluetoothDevice bluetoothDevice, @NonNull Data data) {
                    if (data.size() == 1) {
                        int iIntValue = data.getIntValue(17, 0).intValue();
                        BleManager.this.mBatteryValue = iIntValue;
                        BleManagerGattCallback bleManagerGattCallback = BleManager.this.mGattCallback;
                        if (bleManagerGattCallback != null) {
                            bleManagerGattCallback.onBatteryValueReceived(BleManager.this.mBluetoothGatt, iIntValue);
                        }
                        BleManager.this.mCallbacks.onBatteryValueReceived(bluetoothDevice, iIntValue);
                    }
                }
            });
        }
        Request.newEnableBatteryLevelNotificationsRequest().setManager((BleManager) this).done(new SuccessCallback() { // from class: aisble.BleManager.6
            @Override // aisble.callback.SuccessCallback
            public void onRequestCompleted(@NonNull BluetoothDevice bluetoothDevice) {
                BleManager.this.log(4, "Battery Level notifications enabled");
            }
        }).enqueue();
    }

    @NonNull
    public WriteRequest enableIndications(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return Request.newEnableIndicationsRequest(bluetoothGattCharacteristic).setManager((BleManager) this);
    }

    @NonNull
    public WriteRequest enableNotifications(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return Request.newEnableNotificationsRequest(bluetoothGattCharacteristic).setManager((BleManager) this);
    }

    @Deprecated
    public final void enqueue(@NonNull Request request) {
        final BleManager<E>.BleManagerGattCallback gattCallback = this.mGattCallback;
        if (gattCallback == null) {
            gattCallback = getGattCallback();
            this.mGattCallback = gattCallback;
        }
        gattCallback.enqueue(request);
        runOnUiThread(new Runnable() { // from class: aisble.BleManager.8
            @Override // java.lang.Runnable
            public void run() {
                gattCallback.nextRequest(false);
            }
        });
    }

    @IntRange(from = -1, to = 100)
    @Deprecated
    public final int getBatteryValue() {
        return this.mBatteryValue;
    }

    @Nullable
    public BluetoothDevice getBluetoothDevice() {
        return this.mBluetoothDevice;
    }

    public final int getConnectState() {
        return this.mConnectionState;
    }

    public long getConnectionTimeout() {
        return -1L;
    }

    @NonNull
    public final Context getContext() {
        return this.mContext;
    }

    @NonNull
    public abstract BleManager<E>.BleManagerGattCallback getGattCallback();

    @IntRange(from = 23, to = 517)
    public int getMtu() {
        return this.mMtu;
    }

    @IntRange(from = 0)
    public int getServiceDiscoveryDelay(boolean z) {
        return z ? 1600 : 100;
    }

    public final boolean isBonded() {
        BluetoothDevice bluetoothDevice = this.mBluetoothDevice;
        return bluetoothDevice != null && bluetoothDevice.getBondState() == 12;
    }

    public final boolean isConnected() {
        return this.mConnected;
    }

    public final boolean isReady() {
        return this.mReady;
    }

    public final boolean isReliableWriteInProgress() {
        return this.mReliableWriteInProgress;
    }

    @Override // aisble.utils.ILogger
    public void log(int i, @NonNull String str) {
        switch (i) {
            case 2:
                LogUtils.v(this.TAG, str);
                break;
            case 3:
                LogUtils.d(this.TAG, str);
                break;
            case 4:
                LogUtils.i(this.TAG, str);
                break;
            case 5:
                LogUtils.w(this.TAG, str);
                break;
            case 6:
                LogUtils.e(this.TAG, str);
                break;
        }
    }

    public void onPairingRequestReceived(@NonNull BluetoothDevice bluetoothDevice, int i) {
    }

    @Override // aisble.TimeoutHandler
    @MainThread
    public void onRequestTimeout(@NonNull TimeoutableRequest timeoutableRequest) {
        this.mRequest = null;
        this.mValueChangedRequest = null;
        Request.Type type = timeoutableRequest.type;
        if (type == Request.Type.CONNECT) {
            this.mConnectRequest = null;
            internalDisconnect();
        } else {
            if (type == Request.Type.DISCONNECT) {
                close();
                return;
            }
            BleManager<E>.BleManagerGattCallback bleManagerGattCallback = this.mGattCallback;
            if (bleManagerGattCallback != null) {
                bleManagerGattCallback.nextRequest(true);
            }
        }
    }

    public void overrideMtu(@IntRange(from = 23, to = 517) int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.mMtu = i;
        }
    }

    public String pairingVariantToString(int i) {
        switch (i) {
            case 0:
                return "PAIRING_VARIANT_PIN";
            case 1:
                return "PAIRING_VARIANT_PASSKEY";
            case 2:
                return "PAIRING_VARIANT_PASSKEY_CONFIRMATION";
            case 3:
                return "PAIRING_VARIANT_CONSENT";
            case 4:
                return "PAIRING_VARIANT_DISPLAY_PASSKEY";
            case 5:
                return "PAIRING_VARIANT_DISPLAY_PIN";
            case 6:
                return "PAIRING_VARIANT_OOB_CONSENT";
            default:
                return GrsBaseInfo.CountryCodeSource.UNKNOWN;
        }
    }

    @Deprecated
    public void readBatteryLevel() {
        Request.newReadBatteryLevelRequest().setManager((BleManager) this).with(new DataReceivedCallback() { // from class: aisble.BleManager.4
            @Override // aisble.callback.DataReceivedCallback
            public void onDataReceived(@NonNull BluetoothDevice bluetoothDevice, @NonNull Data data) {
                if (data.size() == 1) {
                    int iIntValue = data.getIntValue(17, 0).intValue();
                    BleManager.this.log(4, "Battery Level received: " + iIntValue + "%");
                    BleManager.this.mBatteryValue = iIntValue;
                    BleManagerGattCallback bleManagerGattCallback = BleManager.this.mGattCallback;
                    if (bleManagerGattCallback != null) {
                        bleManagerGattCallback.onBatteryValueReceived(BleManager.this.mBluetoothGatt, iIntValue);
                    }
                    BleManager.this.mCallbacks.onBatteryValueReceived(bluetoothDevice, iIntValue);
                }
            }
        }).enqueue();
    }

    @NonNull
    public ReadRequest readCharacteristic(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return Request.newReadRequest(bluetoothGattCharacteristic).setManager((BleManager) this);
    }

    @NonNull
    public ReadRequest readDescriptor(@Nullable BluetoothGattDescriptor bluetoothGattDescriptor) {
        return Request.newReadRequest(bluetoothGattDescriptor).setManager((BleManager) this);
    }

    public PhyRequest readPhy() {
        return Request.newReadPhyRequest().setManager((BleManager) this);
    }

    public ReadRssiRequest readRssi() {
        return Request.newReadRssiRequest().setManager((BleManager) this);
    }

    public Request refreshDeviceCache() {
        return Request.newRefreshCacheRequest().setManager(this);
    }

    @NonNull
    public Request removeBond() {
        return Request.removeBond().setManager(this);
    }

    @RequiresApi(api = 21)
    public ConnectionPriorityRequest requestConnectionPriority(int i) {
        return Request.newConnectionPriorityRequest(i).setManager((BleManager) this);
    }

    public MtuRequest requestMtu(@IntRange(from = 23, to = 517) int i) {
        return Request.newMtuRequest(i).setManager((BleManager) this);
    }

    public void setGattCallbacks(@NonNull E e) {
        this.mCallbacks = e;
    }

    @NonNull
    public ValueChangedCallback setIndicationCallback(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return setNotificationCallback(bluetoothGattCharacteristic);
    }

    @NonNull
    @MainThread
    public ValueChangedCallback setNotificationCallback(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        ValueChangedCallback valueChangedCallback = this.mNotificationCallbacks.get(bluetoothGattCharacteristic);
        if (valueChangedCallback == null) {
            valueChangedCallback = new ValueChangedCallback();
            if (bluetoothGattCharacteristic != null) {
                this.mNotificationCallbacks.put(bluetoothGattCharacteristic, valueChangedCallback);
            }
        }
        return valueChangedCallback.free();
    }

    public PhyRequest setPreferredPhy(int i, int i2, int i3) {
        return Request.newSetPreferredPhyRequest(i, i2, i3).setManager((BleManager) this);
    }

    @Deprecated
    public boolean shouldAutoConnect() {
        return false;
    }

    public boolean shouldClearCacheWhenDisconnected() {
        return false;
    }

    public boolean shouldRetryDiscoveryServiceWhenDeviceNotSupported(int i) {
        return false;
    }

    public SleepRequest sleep(@IntRange(from = 0) long j) {
        return Request.newSleepRequest(j).setManager((BleManager) this);
    }

    public String stateToString(int i) {
        return i != 1 ? i != 2 ? i != 3 ? "DISCONNECTED" : "DISCONNECTING" : "CONNECTED" : "CONNECTING";
    }

    @NonNull
    public WaitForValueChangedRequest waitForIndication(@NonNull BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return Request.newWaitForIndicationRequest(bluetoothGattCharacteristic).setManager((BleManager) this);
    }

    @NonNull
    public WaitForValueChangedRequest waitForNotification(@NonNull BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return Request.newWaitForNotificationRequest(bluetoothGattCharacteristic).setManager((BleManager) this);
    }

    @NonNull
    public WriteRequest writeCharacteristic(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic, @Nullable Data data) {
        return Request.newWriteRequest(bluetoothGattCharacteristic, data != null ? data.getValue() : null).setManager((BleManager) this);
    }

    @NonNull
    public WriteRequest writeDescriptor(@Nullable BluetoothGattDescriptor bluetoothGattDescriptor, @Nullable Data data) {
        return Request.newWriteRequest(bluetoothGattDescriptor, data != null ? data.getValue() : null).setManager((BleManager) this);
    }

    public String writeTypeToString(int i) {
        if (i == 1) {
            return "WRITE COMMAND";
        }
        if (i == 2) {
            return "WRITE REQUEST";
        }
        if (i == 4) {
            return "WRITE SIGNED";
        }
        return "UNKNOWN: " + i;
    }

    public BleManager(@NonNull Context context) {
        this.TAG = "BleManager";
        this.mLock = new Object();
        this.mConnectionCount = 0;
        this.mConnectionState = 0;
        this.mBatteryValue = -1;
        this.mMtu = 23;
        this.mNotificationCallbacks = new HashMap<>();
        this.mDeviceNotSupported = false;
        this.mRetryDiscoveryServiceCount = 0;
        this.mConnectTimeoutTask = null;
        this.mServiceDiscoveryRunnable = null;
        this.mWriteOpTimeoutRunable = null;
        this.mBluetoothStateBroadcastReceiver = new BroadcastReceiver() { // from class: aisble.BleManager.1
            private String state2String(int i) {
                switch (i) {
                    case 10:
                        return "OFF";
                    case 11:
                        return "TURNING ON";
                    case 12:
                        return "ON";
                    case 13:
                        return "TURNING OFF";
                    default:
                        return "UNKNOWN (" + i + ")";
                }
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", 10);
                int intExtra2 = intent.getIntExtra("android.bluetooth.adapter.extra.PREVIOUS_STATE", 10);
                BleManager.this.log(3, "[Broadcast] Action received: android.bluetooth.adapter.action.STATE_CHANGED, state changed to " + state2String(intExtra));
                if (intExtra == 10 || intExtra == 13) {
                    if (intExtra2 == 13 || intExtra2 == 10) {
                        BleManager.this.close();
                        return;
                    }
                    BleManagerGattCallback bleManagerGattCallback = BleManager.this.mGattCallback;
                    if (bleManagerGattCallback != null) {
                        bleManagerGattCallback.mOperationInProgress = true;
                        bleManagerGattCallback.cancelQueue();
                        bleManagerGattCallback.mInitQueue = null;
                    }
                    BluetoothDevice bluetoothDevice = BleManager.this.mBluetoothDevice;
                    if (bluetoothDevice != null) {
                        if (BleManager.this.mRequest != null && BleManager.this.mRequest.type != Request.Type.DISCONNECT) {
                            BleManager.this.mRequest.notifyFail(bluetoothDevice, -100);
                            BleManager.this.mRequest = null;
                        }
                        if (BleManager.this.mValueChangedRequest != null) {
                            BleManager.this.mValueChangedRequest.notifyFail(bluetoothDevice, -100);
                            BleManager.this.mValueChangedRequest = null;
                        }
                        if (BleManager.this.mConnectRequest != null) {
                            BleManager.this.mConnectRequest.notifyFail(bluetoothDevice, -100);
                            BleManager.this.mConnectRequest = null;
                        }
                    }
                    BleManager.this.mUserDisconnected = true;
                    if (bleManagerGattCallback != null) {
                        bleManagerGattCallback.mOperationInProgress = false;
                        if (bluetoothDevice != null) {
                            bleManagerGattCallback.notifyDeviceDisconnected(bluetoothDevice);
                        }
                    }
                }
            }
        };
        this.mBondingBroadcastReceiver = new BroadcastReceiver() { // from class: aisble.BleManager.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                int intExtra = intent.getIntExtra("android.bluetooth.device.extra.BOND_STATE", -1);
                int intExtra2 = intent.getIntExtra("android.bluetooth.device.extra.PREVIOUS_BOND_STATE", -1);
                if (BleManager.this.mBluetoothDevice == null || !bluetoothDevice.getAddress().equals(BleManager.this.mBluetoothDevice.getAddress())) {
                    return;
                }
                BleManager.this.log(3, "[Broadcast] Action received: android.bluetooth.device.action.BOND_STATE_CHANGED, bond state changed to: " + BleManager.this.bondStateToString(intExtra) + " (" + intExtra + ")");
                switch (intExtra) {
                    case 10:
                        if (intExtra2 == 11) {
                            BleManager.this.mCallbacks.onBondingFailed(bluetoothDevice);
                            BleManager.this.log(5, "Bonding failed");
                            if (BleManager.this.mRequest != null) {
                                BleManager.this.mRequest.notifyFail(bluetoothDevice, -4);
                                BleManager.this.mRequest = null;
                            }
                        } else if (intExtra2 == 12 && BleManager.this.mRequest != null && BleManager.this.mRequest.type == Request.Type.REMOVE_BOND) {
                            BleManager.this.log(4, "Bond information removed");
                            BleManager.this.mRequest.notifySuccess(bluetoothDevice);
                            BleManager.this.mRequest = null;
                        }
                        break;
                    case 11:
                        BleManager.this.mCallbacks.onBondingRequired(bluetoothDevice);
                        return;
                    case 12:
                        BleManager.this.log(4, "Device bonded");
                        BleManager.this.mCallbacks.onBonded(bluetoothDevice);
                        if (BleManager.this.mRequest != null && BleManager.this.mRequest.type == Request.Type.CREATE_BOND) {
                            BleManager.this.mRequest.notifySuccess(bluetoothDevice);
                            BleManager.this.mRequest = null;
                        } else {
                            BleManager bleManager = BleManager.this;
                            if (!bleManager.mServicesDiscovered && !bleManager.mServiceDiscoveryRequested) {
                                BleManager.this.mServiceDiscoveryRequested = true;
                                BleManager.this.mHandler.post(new Runnable() { // from class: aisble.BleManager.2.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        BleManager.this.log(2, "Discovering services...");
                                        BleManager.this.log(3, "gatt.discoverServices()");
                                        BleManager.this.mBluetoothGatt.discoverServices();
                                    }
                                });
                                return;
                            } else if (Build.VERSION.SDK_INT >= 26 || BleManager.this.mRequest == null || BleManager.this.mRequest.type == Request.Type.CREATE_BOND) {
                                return;
                            } else {
                                BleManager.this.mGattCallback.enqueueFirst(BleManager.this.mRequest);
                            }
                        }
                        break;
                }
                BleManager.this.mGattCallback.nextRequest(true);
            }
        };
        this.mPairingRequestBroadcastReceiver = new BroadcastReceiver() { // from class: aisble.BleManager.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if (BleManager.this.mBluetoothDevice == null || bluetoothDevice == null || !bluetoothDevice.getAddress().equals(BleManager.this.mBluetoothDevice.getAddress())) {
                    return;
                }
                int intExtra = intent.getIntExtra("android.bluetooth.device.extra.PAIRING_VARIANT", 0);
                BleManager.this.log(3, "[Broadcast] Action received: android.bluetooth.device.action.PAIRING_REQUEST, pairing variant: " + BleManager.this.pairingVariantToString(intExtra) + " (" + intExtra + ")");
                BleManager.this.onPairingRequestReceived(bluetoothDevice, intExtra);
            }
        };
        this.mContext = context.getApplicationContext();
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    @NonNull
    public WriteRequest writeCharacteristic(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic, @Nullable byte[] bArr) {
        return Request.newWriteRequest(bluetoothGattCharacteristic, bArr).setManager((BleManager) this);
    }

    @NonNull
    public WriteRequest writeDescriptor(@Nullable BluetoothGattDescriptor bluetoothGattDescriptor, @Nullable byte[] bArr) {
        return Request.newWriteRequest(bluetoothGattDescriptor, bArr).setManager((BleManager) this);
    }

    @NonNull
    public WriteRequest writeCharacteristic(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic, @Nullable byte[] bArr, int i, int i2) {
        return Request.newWriteRequest(bluetoothGattCharacteristic, bArr, i, i2).setManager((BleManager) this);
    }

    @NonNull
    public WriteRequest writeDescriptor(@Nullable BluetoothGattDescriptor bluetoothGattDescriptor, @Nullable byte[] bArr, int i, int i2) {
        return Request.newWriteRequest(bluetoothGattDescriptor, bArr, i, i2).setManager((BleManager) this);
    }

    @Override // aisble.utils.ILogger
    public void log(int i, @StringRes int i2, @Nullable Object... objArr) {
        log(i, this.mContext.getString(i2, objArr));
    }

    @NonNull
    @Deprecated
    public final ConnectRequest connect(@NonNull BluetoothDevice bluetoothDevice, int i) {
        if (this.mCallbacks == null) {
            throw new NullPointerException("Set callbacks using setGattCallbacks(E callbacks) before connecting");
        }
        if (bluetoothDevice != null) {
            return Request.connect(bluetoothDevice).usePreferredPhy(i).useAutoConnect(shouldAutoConnect()).setManager((BleManager) this);
        }
        throw new NullPointerException("Bluetooth device not specified");
    }
}
