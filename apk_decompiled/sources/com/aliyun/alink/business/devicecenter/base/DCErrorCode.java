package com.aliyun.alink.business.devicecenter.base;

/* JADX INFO: loaded from: classes.dex */
public class DCErrorCode {
    public static final int BOX_AUTH_INFO_MODEL_ERROR = 3001;
    public static final int BOX_BLUETOOTH_EXCEPTION = 3020;
    public static final int BOX_DEVICE_ALREADY_BINDED = 3010;
    public static final int BOX_DEVICE_RECONFIG_NETWORK_ERROR = 3011;
    public static final int BOX_DHCP_START_FAILED = 3015;
    public static final int BOX_DHCP_TIMEOUT = 3016;
    public static final int BOX_ERROR_AP = 3007;
    public static final int BOX_ERROR_PASSWORD = 3006;
    public static final int BOX_GET_AUTH_CODE_TOO_MANY_TIMES = 3004;
    public static final int BOX_GET_AUTH_INFO_TOO_MANY_TIMES = 3003;
    public static final int BOX_GET_AUTH_RESULT_TIMEOUT = 3005;
    public static final int BOX_GW_AUTH_FAILED = 3018;
    public static final int BOX_GW_CONN_TIMEOUT = 3017;
    public static final int BOX_NETWORK_NOT_FOUND = 3014;
    public static final int BOX_NO_LOCATION_PERMISSION = 3025;
    public static final int BOX_PORTAL = 3026;
    public static final int BOX_PROVISION_EXCEPTION = 3002;
    public static final int BOX_PROVISION_START_ALL_FAILED = 3024;
    public static final int BOX_RECV_ACCS_BUT_QUERY_FAILED = 3021;
    public static final int BOX_SCAN_ERROR = 3008;
    public static final int BOX_SCAN_FAILED = 3013;
    public static final int BOX_USER_CANCEL_ERROR = 3009;
    public static final int BOX_ZCONFIG_TIMEOUT = 3012;
    public static final int CONNECT_AP = 3029;
    public static final int CONNECT_ROUTER = 3030;
    public static final int ERROR_CODE_DIAGNOSE_TIMEOUT = 102001;
    public static final int ESTABLISHED_GW = 3031;
    public static final String PARAM_ERROR_MSG = "ParamsError";
    public static final int PF_ACTIVATION_CLOUD_ERROR = 101623;
    public static final int PF_DEVICE_FAIL = 101616;
    public static final int PF_GET_DEVICE_TOKEN_ERROR = 101609;
    public static final int PF_NETWORK_ERROR = 101601;
    public static final int PF_PARAMS_ERROR = 101602;
    public static final int PF_PROVISION_APP_GENIE_SOUND_BOX_ERROR = 101621;
    public static final int PF_PROVISION_APP_MESH_ERROR = 101620;
    public static final int PF_PROVISION_CLOUD_BIND_ERROR = 101619;
    public static final int PF_PROVISION_CLOUD_SILENT_ERROR = 101622;
    public static final int PF_PROVISION_COMBO_MESH_WIFI_ERROR = 101623;
    public static final int PF_PROVISION_FAIL_FROM_DEVICE = 101618;
    public static final int PF_PROVISION_INSIDE_BIND_ERROR = 101625;
    public static final int PF_PROVISION_SCAN_SAVE_TID = 101624;
    public static final int PF_PROVISION_TIMEOUT = 101617;
    public static final int PF_SDK_ERROR = 101604;
    public static final int PF_SERVER_FAIL = 101606;
    public static final int PF_SYSTEM_ERROR = 101600;
    public static final int PF_UNKNOWN_ERROR = 101603;
    public static final int PF_USER_FAIL = 101605;
    public static final int PF_USER_INVOKE_ERROR = 101608;
    public static final String PROVISION_TIMEOUT_MSG = "ProvisionTimeout";
    public static final int RECEIVE_SSID = 3027;
    public static final int SCANED_AP = 3028;
    public static final String SERVER_ERROR_MSG = "ServerError";
    public static final String SOUND_BOX_PROVISION_MSG = "GenieSoundBoxProvisionError";
    public static final int SUBCODE_APICLIENT_AND_MTOP_DEP_ERROR = 60506;
    public static final int SUBCODE_API_REQUEST_EXCEPTION = 60110;
    public static final int SUBCODE_API_REQUEST_ON_FAILURE = 60109;
    public static final int SUBCODE_BLE_COMBO_CONNECTED_NO_GET_DEVICE_INFO = 61721;
    public static final int SUBCODE_BLE_COMBO_GET_CLOUD_TOKEN_FAILED = 61722;
    public static final int SUBCODE_DF_BLE_AUTH_FAIL = 61612;
    public static final int SUBCODE_DF_BLE_CONNECT_AP_OR_MQTT_FAILED = 61602;
    public static final int SUBCODE_DF_BLE_DISCONNECT = 61601;
    public static final int SUBCODE_DF_BLE_GET_DEVICE_INFO_EMPTY = 61605;
    public static final int SUBCODE_DF_BLE_GET_DEVICE_INFO_INVALID = 61606;
    public static final int SUBCODE_DF_BLE_GET_DEVICE_INFO_NOT_MATCH = 61607;
    public static final int SUBCODE_DF_BLE_NO_CONNECTAP_NOTIFY_AND_CHECK_TOKEN_FAIL = 61609;
    public static final int SUBCODE_DF_BLE_PARSE_RESPONSE_EXCEPTION = 61608;
    public static final int SUBCODE_DF_BLE_RESPONSE_FAIL = 61611;
    public static final int SUBCODE_DF_BLE_RESPONSE_SUCCESS_NO_CONNECT_AP = 61610;
    public static final int SUBCODE_DF_BLE_SEND_DATA_WITH_NO_RESPONSE = 61603;
    public static final int SUBCODE_FDF_CONNECT_AP_FAIL = 61800;
    public static final int SUBCODE_MESH_SDK_BIND_EXCEPTION = 60404;
    public static final int SUBCODE_MESH_SDK_DEVICE_DISCONNECTED = 60406;
    public static final int SUBCODE_MESH_SDK_INIT_EXCEPTION = 60405;
    public static final int SUBCODE_MESH_SDK_PROVISION_EXCEPTION = 60403;
    public static final int SUBCODE_NE_5GWIFI_NOTSUPPORT = 60101;
    public static final int SUBCODE_NE_MOBILE_NOT_ENABLED = 60103;
    public static final int SUBCODE_NE_NETWORK_UNAVAILABLE = 60108;
    public static final int SUBCODE_NE_WIFI_AP_AUTO_ENABL_NOT_SUPPORT = 60105;
    public static final int SUBCODE_NE_WIFI_AP_ENABLE_FAILED = 60106;
    public static final int SUBCODE_NE_WIFI_AP_ENABLE_FAILED_BROADCAST = 60107;
    public static final int SUBCODE_NE_WIFI_AP_NOT_ENABLED = 60104;
    public static final int SUBCODE_NE_WIFI_NOT_CONNECTED = 60102;
    public static final int SUBCODE_PE_BATCH_TO_PROVISION_DEVICE_EMPTY = 60207;
    public static final int SUBCODE_PE_DEVICETYPE_ERROR = 60203;
    public static final int SUBCODE_PE_LINKTYPE_ERROR = 60202;
    public static final int SUBCODE_PE_NON_GENIE_ENV = 60211;
    public static final int SUBCODE_PE_PRODUCTKEY_EMPTY = 60201;
    public static final int SUBCODE_PE_PRODUCT_ID_EMPTY = 60208;
    public static final int SUBCODE_PE_PROVISION_PARAMS_ERROR = 60204;
    public static final int SUBCODE_PE_REG_PK_DN_INVALID = 60209;
    public static final int SUBCODE_PE_SET_INVALID_DEV_AP_SSID = 60210;
    public static final int SUBCODE_PE_SSID_EMPTY = 60205;
    public static final int SUBCODE_PE_VERSION_INVALID = 60220;
    public static final int SUBCODE_PT_BLE_CONNECT_DEV_FAILED = 61703;
    public static final int SUBCODE_PT_BLE_FOUND_DEV_FAILED = 61702;
    public static final int SUBCODE_PT_BLE_GET_DEVICE_NAME_TIMEOUT = 61704;
    public static final int SUBCODE_PT_DEVICE_PROVISION_FAIL_OR_TIMEOUT = 61714;
    public static final int SUBCODE_PT_GET_CIPHER_TIMEOUT = 61713;
    public static final int SUBCODE_PT_LP_USER_CONNECT_DEVICE_AP_TIMEOUT = 61715;
    public static final int SUBCODE_PT_MESH_DEVICE_PROVISION_FAIL_OR_TIMEOUT = 62001;
    public static final int SUBCODE_PT_NO_ACK = 61710;
    public static final int SUBCODE_PT_NO_CONNECTAP_NOTIFY_AND_CHECK_TOKEN_FAIL = 61709;
    public static final int SUBCODE_PT_PAP_DISCOVER_DEVICE_TIMEOUT = 61718;
    public static final int SUBCODE_PT_PAP_NO_CONNECTAP_NOTIFY = 61712;
    public static final int SUBCODE_PT_PAP_NO_DEVICE_CONNECTED = 61705;
    public static final int SUBCODE_PT_PAP_SWITCHAP_NO_ACK = 61710;
    public static final int SUBCODE_PT_PAP_WIFI_NOT_RECOVERED = 61711;
    public static final int SUBCODE_PT_REQUEST_ENROLLEE_TIMEOUT = 61719;
    public static final int SUBCODE_PT_SAP_CONNECT_DEV_AP_FAILED = 61706;
    public static final int SUBCODE_PT_SAP_DEVICE_AP_CONNECTED_NO_SWITCH_AP = 61707;
    public static final int SUBCODE_PT_SAP_NO_SOFTAP = 61701;
    public static final int SUBCODE_PT_SAP_SOME_WIFI_CONNECTED_NO_CONNECTAP_NOTIFY = 61716;
    public static final int SUBCODE_PT_SAP_SWITCHAP_AND_NETWORK_UNAVAILABLE = 61708;
    public static final int SUBCODE_SILENT_DEVICE_BIND_FAIL = 62003;
    public static final int SUBCODE_SILENT_DEVICE_PROVISION_TIMEOUT = 62002;
    public static final int SUBCODE_SKE_DISCOVERY_EXCEPTION = 60401;
    public static final int SUBCODE_SKE_START_CONFIG_EXCEPTION = 60402;
    public static final int SUBCODE_SRE_GET_MESH_PROVISION_RESULT_BIZ_FAIL = 60607;
    public static final int SUBCODE_SRE_GET_MESH_PROVISION_RESULT_IOTID_EMPTY = 60605;
    public static final int SUBCODE_SRE_GET_MESH_PROVISION_RESULT_IOTID_NOT_EQUAL = 60606;
    public static final int SUBCODE_SRE_KEY_EMPTY = 60602;
    public static final int SUBCODE_SRE_MESH_GATEWAY_PROVISION_TRIGGER_RESPONSE_EMPTY = 60604;
    public static final int SUBCODE_SRE_RESPONSE_EMPTY = 60603;
    public static final int SUBCODE_SRE_RESPONSE_FAIL = 60608;
    public static final int SUBCODE_STE_BLE_PERMISSION_LOST = 60001;
    public static final int SUBCODE_STE_ENABLE_AP_FAILED = 60002;
    public static final int SUBCODE_UE_COMMON_CODE = 60301;
    public static final int SUBCODE_UF_DEVICE_AP_CONNECTED = 60504;
    public static final int SUBCODE_UF_LOCATION_SERVICE_DISABLED = 60502;
    public static final int SUBCODE_UF_NO_BLE_DEPENDENCY = 60505;
    public static final int SUBCODE_UF_NO_BLE_PERMISSION = 60503;
    public static final int SUBCODE_UF_NO_LOCATION_PERMISSION = 60501;
    public static final int SUBCODE_UIE_CONCURRENT_MESH_NEED_MAC = 60805;
    public static final int SUBCODE_UIE_GET_DEVICE_TOKEN_PARAMS_INVALID = 60802;
    public static final int SUBCODE_UIE_NEED_BREEZE_BIZ_DEPENDENCY = 60803;
    public static final int SUBCODE_UIE_NEED_MESH_SDK_DEPENDENCY = 60804;
    public static final int SUBCODE_UIE_NEED_USER_LOGIN = 60805;
    public static final int SUBCODE_UIE_PROVISION_RUNNING = 60801;
    public static final int SUBCODE_WRONG_CALL = 60206;
    public static final int WIFI_RSSI = -4000;
    public String code;
    public String codeName;
    public Throwable exception;
    public Object extra;
    public String msg;
    public String subcode;

    public DCErrorCode(String str, int i) {
        this(str, i, 0, null, null);
    }

    public DCErrorCode setExtra(Object obj) {
        this.extra = obj;
        return this;
    }

    public DCErrorCode setMsg(String str) {
        this.msg = str;
        return this;
    }

    public DCErrorCode setSubcode(int i) {
        this.subcode = String.valueOf(i);
        return this;
    }

    public DCErrorCode setSubcodeStr(String str) {
        this.subcode = str;
        return this;
    }

    public String toString() {
        return "DCErrorCode [code:" + this.code + ", subcode:" + this.subcode + ", codeName:" + this.codeName + ", message:" + this.msg + ", extra:" + this.extra + "]";
    }

    public DCErrorCode(String str, int i, String str2) {
        this(str, i, 0, str2, null);
    }

    public DCErrorCode(String str, int i, String str2, Throwable th) {
        this(str, i, 0, str2, null);
        this.exception = th;
    }

    public DCErrorCode(String str, int i, int i2, String str2) {
        this(str, i, i2, str2, null);
    }

    public DCErrorCode(String str, int i, int i2, String str2, Object obj) {
        this.code = String.valueOf(i);
        this.subcode = String.valueOf(i2);
        this.codeName = str;
        this.msg = str2;
        this.extra = obj;
    }
}
