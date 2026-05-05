package com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants;

/* JADX INFO: loaded from: classes.dex */
public class WifiProvisionUtConst {
    public static final String ARG_CONNECTION = "provision";
    public static final int BOX_AUTH_INFO_MODEL_ERROR = 3001;
    public static final int BOX_BLUETOOTH_DISCONNECT = -3026;
    public static final int BOX_BLUETOOTH_EXCEPTION = 3020;
    public static final int BOX_CONFIG_SUCCESS = 4001;
    public static final int BOX_CONNECT_AP = 3029;
    public static final int BOX_CONNECT_ROUTER = 3030;
    public static final int BOX_CONNECT_SUCCESS = 3032;
    public static final int BOX_DEVICE_ALREADY_BINDED = 3010;
    public static final int BOX_DEVICE_RECONFIG_NETWORK_ERROR = 3011;
    public static final int BOX_DHCP_START_FAILED = 3015;
    public static final int BOX_DHCP_TIMEOUT = 3016;
    public static final int BOX_ERROR_AP = 3007;
    public static final int BOX_ERROR_PASSWORD = 3006;
    public static final int BOX_ESTABLISHED_GW = 3031;
    public static final int BOX_GET_AUTH_CODE_TOO_MANY_TIMES = 3004;
    public static final int BOX_GET_AUTH_INFO_TOO_MANY_TIMES = 3003;
    public static final int BOX_GET_AUTH_RESULT_TIMEOUT = 3005;
    public static final int BOX_GW_AUTH_FAILED = 3018;
    public static final int BOX_GW_CONN_TIMEOUT = 3017;
    public static final int BOX_INTERNAL_MSG = 4002;
    public static final int BOX_NETWORK_NOT_FOUND = 3014;
    public static final int BOX_NO_LOCATION_PERMISSION = 3025;
    public static final int BOX_PORTAL_HOT = 3026;
    public static final int BOX_PROVISION_EXCEPTION = 3002;
    public static final int BOX_PROVISION_START_ALL_FAILED = 3024;
    public static final int BOX_RECEIVE_SSID = 3027;
    public static final int BOX_RECV_ACCS_BUT_QUERY_FAILED = 3021;
    public static final int BOX_SCANED_AP = 3028;
    public static final int BOX_SCAN_ERROR = 3008;
    public static final int BOX_SCAN_FAILED = 3013;
    public static final int BOX_USER_CANCEL_ERROR = 3009;
    public static final int BOX_WIFI_RSSI = -4000;
    public static final int BOX_ZCONFIG_TIMEOUT = 3012;
    public static final int COMBO_FOR_WROD_ERROR = 1013;
    public static final int COMBO_FOR_WROD_FAIL = 1012;
    public static final int COMBO_PROVISION_FAIL_HEADER = 2020;
    public static final int FEIYAN_ADD_DEVICE_ERROR = 2003;
    public static final int FEIYAN_BROADCAST_MSG_ERROR = 2002;
    public static final int FEIYAN_REQUEST_ENCRYPTION_KEY_ERROR = 2001;
    public static final String KEY_BUSIZ_INFO = "bus_info";
    public static final String KEY_CHANNEL = "channel";
    public static final String KEY_COST_TIME = "costTime";
    public static final String KEY_ERROR_CODE = "errorCode";
    public static final String KEY_ERROR_MSG = "errorMsg";
    public static final String KEY_PK = "productKey";
    public static final String KEY_STEP = "step";
    public static final int QUERY_BIND_RESULT_EXCEPTION = 1007;
    public static final int REQUEST_PRODUCT_INFO_ERROR = 1004;
    public static final int SSID_EMTPY_ERROR = 1006;
    public static final int SUCCESS = 0;
    public static final int TG_IOT_BROADCAST_MSG_ERROR = 1002;
    public static final int TG_IOT_REQUEST_PROVISION_TOKEN_ERROR = 1001;
    public static final int TG_IOT_TIMEOUT = 1009;
    public static final int TG_QUERY_RESULT_TIMEOUT = 1003;
    public static final int UNKNOW_ERROR = 1008;
    public static final int USER_CANCEL_PROVISION_ERROR = 1005;
    public static final String VALUE_ERROR = "error";
    public static final String VALUE_START = "start";
    public static final String VALUE_SUCCESS = "success";

    public enum ChannelType {
        TG_IOT_WIFI("wifi"),
        TG_IOT_BLE("ble"),
        FEIYAN_IOT_WIFI("feiyan_iot_wifi"),
        TG_BOX_WIFI("box_wifi"),
        FEIYAN_IOT_CLOUD("feiyan_iot_cloud"),
        FEIYAN_IOT_BLE_COMBO("feiyan_iot_ble_combo");

        public String provisionType;

        ChannelType(String str) {
            this.provisionType = str;
        }

        public String getProvisionType() {
            return this.provisionType;
        }
    }

    public enum ProvisionPage {
        BLE_PAGE("ALSBluetooth"),
        TG_BOX("ALSGenie"),
        TG_WIFI_PAGE("ALSWiFi"),
        ALSFEIYAN_COMBO_PAGE("ALSFeiyanCombo"),
        FEIYAN_WIFI_PAGE("ALSFeiyanWiFi"),
        FEIYAN_CLOUD_PAGE("ALSFeiyanCloud"),
        GMA("ALSGMA"),
        MESH("ALSMesh");

        public String pageName;

        ProvisionPage(String str) {
            this.pageName = str;
        }

        public String getPageName() {
            return this.pageName;
        }
    }
}
