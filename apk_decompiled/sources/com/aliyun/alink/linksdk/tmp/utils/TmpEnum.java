package com.aliyun.alink.linksdk.tmp.utils;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class TmpEnum {
    private static final String QOS_CON_STRING = "Qos_CON";
    private static final String QOS_NON_STRING = "Qos_NON";
    private static final String TAG = "[Tmp]TmpEnum";

    public enum ChannelStrategy {
        LOCAL_CHANNEL_FIRST,
        LOCAL_CHANNEL_ONLY,
        CLOUD_CHANNEL_ONLY
    }

    public enum DiscoveryDeviceState {
        DISCOVERY_STATE_ONLINE,
        DISCOVERY_STATE_OFFLINE
    }

    public enum GroupRoleType {
        UNKNOWN(TmpConstant.GROUP_ROLE_UNKNOWN),
        CONTROLLER(TmpConstant.GROUP_ROLE_CONTROLLER),
        DEVICE(TmpConstant.GROUP_ROLE_DEVICE);

        private String value;

        GroupRoleType(String str) {
            this.value = str;
        }

        public String getValue() {
            return this.value;
        }
    }

    public enum DeviceState {
        UNKNOW(0),
        CONNECTED(1),
        DISCONNECTED(2),
        CONNECTING(3);

        private int value;

        DeviceState(int i) {
            this.value = i;
        }

        public int getValue() {
            return this.value;
        }

        public static DeviceState createConnectState(ConnectState connectState) {
            if (ConnectState.CONNECTED == connectState) {
                return CONNECTED;
            }
            if (ConnectState.DISCONNECTED == connectState || ConnectState.CONNECTFAIL == connectState) {
                return DISCONNECTED;
            }
            if (ConnectState.CONNECTING == connectState) {
                return CONNECTING;
            }
            ALog.e(TmpEnum.TAG, "createConnectState state unknown state:" + connectState);
            return UNKNOW;
        }
    }

    public enum QoSLevel {
        QoS_CON(0),
        QoS_NON(1);

        int value;

        QoSLevel(int i) {
            this.value = i;
        }

        public int getValue() {
            return this.value;
        }

        public static QoSLevel getQoSLevel(int i) {
            QoSLevel qoSLevel = QoS_CON;
            switch (i) {
                case 0:
                default:
                    return qoSLevel;
                case 1:
                    return QoS_NON;
            }
        }

        public static QoSLevel getQoSLevel(String str) {
            QoSLevel qoSLevel = QoS_CON;
            if (TextUtils.isEmpty(str)) {
                return qoSLevel;
            }
            if (TmpEnum.QOS_CON_STRING.equalsIgnoreCase(str)) {
                return QoS_CON;
            }
            return TmpEnum.QOS_NON_STRING.equalsIgnoreCase(str) ? QoS_NON : qoSLevel;
        }
    }

    public enum DeviceShadowUpdateType {
        UPDATE_OPTION_TSL(1),
        UPDATE_OPTION_DEVICE_DETAIL_INFO(2),
        UPDATE_OPTION_PROPERTIES(4),
        UPDATE_OPTION_STATUS(8),
        UPDATE_OPTION_DEVICE_WIFI_STATUS(16),
        UPDATE_OPTION_DEVICE_NET_TYPE(32),
        UPDATE_TYPE_DEVICE_DYNAMIC_TYPE(UPDATE_OPTION_PROPERTIES.value | UPDATE_OPTION_STATUS.value),
        UPDATE_OPTION_ALL_TYPE_EXCEPT_WIFISTATUS((((UPDATE_OPTION_TSL.value | UPDATE_OPTION_DEVICE_DETAIL_INFO.value) | UPDATE_OPTION_PROPERTIES.value) | UPDATE_OPTION_STATUS.value) | UPDATE_OPTION_DEVICE_NET_TYPE.value),
        UPDATE_OPTION_ALL(-1);

        private int value;

        DeviceShadowUpdateType(int i) {
            this.value = i;
        }

        public int getValue() {
            return this.value;
        }
    }

    public enum DeviceNetType {
        NET_UNKNOWN(0),
        NET_WIFI(1),
        NET_BT(2),
        NET_COMBOMESH(256),
        NET_LORA(4),
        NET_CELLULAR(8),
        NET_ZIGBEE(16),
        NET_ETHERNET(32),
        NET_OTHER(4096),
        NET_BLE_MESH(128);

        private int value;

        DeviceNetType(int i) {
            this.value = i;
        }

        public int getValue() {
            return this.value;
        }

        public static boolean isWifiBtCombo(int i) {
            return (NET_WIFI.getValue() & i) > 0 && (i & NET_BT.getValue()) > 0;
        }

        public static int formatDeviceNetType(List<String> list) {
            if (list == null) {
                return 0;
            }
            int i = list.contains("NET_LORA") ? 0 | NET_LORA.value : 0;
            if (list.contains("NET_CELLULAR")) {
                i |= NET_CELLULAR.value;
            }
            if (list.contains("NET_WIFI")) {
                i |= NET_WIFI.value;
            }
            if (list.contains("NET_ZIGBEE")) {
                i |= NET_ZIGBEE.value;
            }
            if (list.contains("NET_ETHERNET")) {
                i |= NET_ETHERNET.value;
            }
            if (list.contains("NET_BT")) {
                i |= NET_BT.value;
            }
            if (list.contains("NET_OTHER")) {
                i |= NET_OTHER.value;
            }
            return list.contains("NET_COMBOMESH") ? i | NET_COMBOMESH.value : i;
        }
    }

    public enum DeviceWifiStatus {
        DeviceWifiStatus_NotSupport(-1),
        DeviceWifiStatus_NotSet(0),
        DeviceWifiStatus_Set(1),
        DeviceWifiStatus_Setting(2);

        private int value;

        DeviceWifiStatus(int i) {
            this.value = i;
        }

        public int getValue() {
            return this.value;
        }

        public static DeviceWifiStatus formatDeviceWifiStatus(int i) {
            switch (i) {
                case -1:
                    return DeviceWifiStatus_NotSupport;
                case 0:
                    return DeviceWifiStatus_NotSet;
                case 1:
                    return DeviceWifiStatus_Set;
                case 2:
                    return DeviceWifiStatus_Setting;
                default:
                    return DeviceWifiStatus_NotSupport;
            }
        }
    }

    public enum ConnectType {
        CONNECT_TYPE_UNKNOWN(0),
        CONNECT_TYPE_COAP(1),
        CONNECT_TYPE_MQTT(2),
        CONNECT_TYPE_APIGATE(3),
        CONNECT_TYPE_BLE(4),
        CONNECT_TYPE_TGMESH(5);

        public int value;

        ConnectType(int i) {
            this.value = i;
        }
    }
}
