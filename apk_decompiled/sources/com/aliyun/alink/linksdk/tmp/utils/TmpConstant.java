package com.aliyun.alink.linksdk.tmp.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes2.dex */
public class TmpConstant {

    @Deprecated
    public static final String ALGECC = "ecc";

    @Deprecated
    public static final String ALGHMACMD5 = "hmacmd5";

    @Deprecated
    public static final String ALGNONE = "none";
    public static final String ALINK_SECURE_URI_SCHEME = "coaps";
    public static final String ALINK_URI_SCHEME = "coap";
    public static final String AUTH_VER = "1.0";
    public static final String CONFIG_TYPE_CLIENT = "ClientAuthInfo";
    public static final String CONFIG_TYPE_PROVISION_RECEIVER = "SetupData";
    public static final String CONFIG_TYPE_SERVER = "ServerAuthInfo";
    public static final String CONNECT_WIFI_EVENT = "connect_wifi_event";
    public static final String DATA_KEY_DEVICENAME = "MAC";
    public static final String DATA_KEY_DEVICE_WIFI_STATUS = "wifiEnabled";
    public static final int DEFAULT_COAP_SECURE_PORT = 5684;
    public static final int DEFAULT_COAP_UDP_PORT = 5683;
    public static final String DEFAULT_MULTI_CAST_ADDRESS = "224.0.1.187";
    public static final String DEVICES = "devices";
    public static final String DEVICE_DATA_FORMAT_ALINK = "ALINK_FORMAT";
    public static final String DEVICE_IOTID = "iotId";
    public static final String DEVICE_MODEL = "deviceModel";
    public static final String DEVICE_MODEL_EVENTS = "events";
    public static final String DEVICE_MODEL_PROPERTIES = "properties";
    public static final String DEVICE_MODEL_SERVICES = "services";
    public static final String DEVICE_MODEL_TYPE = "model";
    public static final String DEVICE_NAME = "deviceName";
    public static final int DEVICE_NAME_POSITION = 2;
    public static final String DEVICE_PRODUCT_KEY = "productKey";
    public static final String DEVICE_PROFILE = "profile";
    public static final String EVENT_PROPERTY_URI_POST = "post";
    public static final String EVENT_PROPERTY_URI_POST_REPLY = "post_reply";
    public static final String EVENT_PROPERTY_URI_PRE = "thing/event/property";

    @Deprecated
    public static final int EVENT_PUB_CODE = 3;

    @Deprecated
    public static final int EVENT_SUB_CODE = 1;

    @Deprecated
    public static final int EVENT_UNSUB_CODE = 2;
    public static final String EVENT_URI_POST = "post";
    public static final String EVENT_URI_PRE = "thing/event";
    public static final String EXPAND_SPLITE = ".";
    public static final String GROUP_CLOUD_ROLE_COTROLLER = "COTROLLER";
    public static final String GROUP_CLOUD_ROLE_DEVICE = "DEVICE";
    public static final int GROUP_DEVICE_LOCAL_STATUS_OFFLINE = 3;
    public static final int GROUP_DEVICE_LOCAL_STATUS_ONLINE = 1;
    public static final int GROUP_LOCAL_SATUS_ALLOFFLINE = 2;
    public static final int GROUP_LOCAL_SATUS_ALLONLINE = 0;
    public static final int GROUP_LOCAL_SATUS_PARTONLINE = 1;
    public static final String GROUP_OP_ADD = "add";
    public static final String GROUP_OP_DEL = "del";
    public static final String GROUP_ROLE_CONTROLLER = "cloud_ctl";
    public static final String GROUP_ROLE_DEVICE = "cloud_dev";
    public static final String GROUP_ROLE_UNKNOWN = "null";
    public static final int HEARTBEAT_DEFAULT_TIME = 600000;
    public static final int HEARTBEAT_MAX_RETRY_TIME = 3;

    @Deprecated
    public static final String IDENTIFIER_DEVICENOTIFY = "devnotify";
    public static final String IDENTIFIER_RAW_DATA_DOWN = "raw_data_down";
    public static final String IDENTIFIER_SETUP = "setup";
    public static final String IDENTITY_ADMIN = "0";
    public static final String IDENTITY_GUEST = "1";
    public static final String IDENTITY_PROVISION = "2";
    public static final String KEY_CLIENT_ID = "clientId";
    public static final String KEY_IOT_NeedRsp = "NeedRsp";
    public static final String KEY_IOT_NeedRsp_EX = "NeedRspEx";
    public static final String KEY_IOT_PERFORMANCE_EVENT_REQ = "req";
    public static final String KEY_IOT_PERFORMANCE_EVENT_RES = "res";
    public static final String KEY_IOT_PERFORMANCE_ID = "IotPerformanceId";
    public static final String KEY_IOT_QOS = "QosLevel";
    public static final String KEY_IOT_QOS_EX = "QosLevelEx";
    public static final String KEY_SIGN_METHOD = "signMethod";
    public static final String KEY_SIGN_VALUE = "sign";
    public static final String LINK_DEFAULT_CONNECT_ID = "LINK_DEAFULT";
    public static final int MAX_BLOCK_SIZE = 3072;
    public static final int MAX_DEVICE_MODEL_LENGTH = 3072;
    public static final String METHOD_AUTH_USER = "core.service.user";
    public static final String METHOD_DEVICE_NOTIFY = "core.service.notify";
    public static final String METHOD_HEART_BEAT = "core.service.heartBeat";
    public static final String METHOD_IDENTIFIER_DEV = "dev";
    public static final String METHOD_PROPERTY_GET = "thing.service.property.get";
    public static final String METHOD_PROPERTY_POST = "thing.event.property.post";
    public static final String METHOD_PROPERTY_SET = "thing.service.property.set";
    public static final String METHOD_SERVICE_AUTH = "core.service.auth";
    public static final String METHOD_SERVICE_AUTH_INFO = "core.service.auth_info";
    public static final String METHOD_SERVICE_DISCOVERY = "core.service.dev";
    public static final String METHOD_SERVICE_PRE = "thing.service.";
    public static final String METHOD_SETUP = "core.service.setup";
    public static final String METHOD_TSL_TEMPLATE = "thing.dsltemplate.get";
    public static final String METHOD_URI_PRE = "thing/service";
    public static final String METHOD_USER_SERVICE = "core.service.user";
    public static final String MODEL_TYPE_ALI_LCA_CLOUD = "4";
    public static final String MODE_VALUE_RANDW = "rw";
    public static final String MODE_VALUE_READ = "r";
    public static final String MODE_VALUE_WRITE = "w";
    public static final String MQTT_NOTIFY_TOPIC_PREFIX = "/app/down/_thing";
    public static final String MQTT_TOPIC_EVENTS = "/app/down/thing/events";
    public static final String MQTT_TOPIC_NOTIFY = "/app/down/_thing/event/notify";
    public static final String MQTT_TOPIC_PREFIX = "/app/down/thing";
    public static final String MQTT_TOPIC_PROPERTIES = "/app/down/thing/properties";
    public static final String MQTT_TOPIC_SERVICE_REPLY = "/app/down/thing/service/invoke/reply";
    public static final String MQTT_TOPIC_STATUS = "/app/down/thing/status";

    @Deprecated
    public static final int OPTIONNUMREGISTRY_ACCESSKEY = 36;
    public static final String PATH_DISCOVERY = "/dev/core/service/dev";
    public static final String PATH_GROUP_PRE = "/thing/service/";
    public static final String PATH_NOTIFY = "/dev/core/service/notify";
    public static final String PATH_SETUP = "/core/service/setup";
    public static final String PAYLOAD_CODE = "code";
    public static final int PRODUCT_KEY_POSITION = 1;
    public static final String PROPERTY_IDENTIFIER = "identifier";
    public static final String PROPERTY_IDENTIFIER_GET = "get";
    public static final String PROPERTY_IDENTIFIER_SET = "set";
    public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_TIME = "time";
    public static final String PROPERTY_URI_PRE = "thing/service/property";
    public static final String PROPERTY_VALUE = "value";
    public static final String REQUEST_DELAYTIME = "delayTime";
    public static final String REQUEST_ID = "id";
    public static final String REQUEST_METHOD = "method";
    public static final String REQUEST_PARAMS = "params";
    public static final String REQUEST_SESSIONKEY = "sessionKey";
    public static final String REQUEST_VERSION = "version";
    public static final String RESULT_FAIL = "Failed";
    public static final String RESULT_OK = "Success";
    public static final String RRESPONSE_DATA = "data";
    public static final String RRESPONSE_MESSAGEID = "messageId";
    public static final String SERVICE_CALLTYPE = "callType";
    public static final String SERVICE_DESC = "desc";
    public static final String SERVICE_IDENTIFIER = "identifier";
    public static final String SERVICE_INPUTDATA = "inputData";
    public static final String SERVICE_METHOD = "method";
    public static final String SERVICE_NAME = "name";
    public static final String SERVICE_OUTPUTDATA = "outputData";

    @Deprecated
    public static final int SESSION_VALID_TIME = 86400;
    public static final String TAG = "[Tmp]";
    public static final String THING_WIFI_RECONNECT_NOTIFY = "thing/wifi/connect/event/notify";
    public static final String THING_WIFI_STATUS_NOTIFY = "thing/wifi/status/notify";
    public static final String TMP_LOCAL_STATUS = "tmp_local_status";
    public static final String TMP_MODEL_TYPE_ALI_BREEZE = "breeze";
    public static final String TMP_MODEL_TYPE_ALI_LCA_CLOUD = "cloudlca";
    public static final String TMP_MODEL_TYPE_ALI_THIRD_PART = "third";
    public static final String TMP_MODEL_TYPE_ALI_WIFI = "wifi";
    public static final String TYPE_VALUE_ARRAY = "array";
    public static final String TYPE_VALUE_BOOLEAN = "bool";
    public static final String TYPE_VALUE_DATE = "date";
    public static final String TYPE_VALUE_DOUBLE = "double";
    public static final String TYPE_VALUE_ENUM = "enum";
    public static final String TYPE_VALUE_FLOAT = "float";
    public static final String TYPE_VALUE_INTEGER = "int";
    public static final String TYPE_VALUE_STRING = "string";
    public static final String TYPE_VALUE_STRUCT = "struct";
    public static final String TYPE_VALUE_TEXT = "text";

    @Deprecated
    public static final int UNKNOWN_CODE = 0;

    @Deprecated
    public static final String URI_ALINK_DEVICE_DATA = "alink/deviceStatus";

    @Deprecated
    public static final String URI_ALINK_SERVICE = "alink/service";

    @Deprecated
    public static final String URI_AUTH = "/auth";
    public static final String URI_AUTHEN_REGISTER = "/sys/{productKey}/{deviceName}/thing/authen/sub/register";
    public static final String URI_AUTHEN_REGISTER_REPLY = "/sys/{productKey}/{deviceName}/thing/authen/sub/register_reply";
    public static final String URI_BLACKLIST_UPDATE_POST = "/thing/lan/blacklist/update";
    public static final String URI_BLACKLIST_UPDATE_REPLY_POST = "/thing/lan/blacklist/update_reply";

    @Deprecated
    public static final String URI_DEVICE = "/device/core/dev";
    public static final String URI_DEV_PATH_PRE = "dev";

    @Deprecated
    public static final String URI_METHOD = "/device/core/method";
    public static final String URI_MODEL = "/model";
    public static final String URI_PATH_SPLITER = "/";

    @Deprecated
    public static final String URI_PERMISSION = "/permission";
    public static final String URI_PREFIX_ALCS_SERVICE = "core/service";
    public static final String URI_PREFIX_UPDATE_POST = "/thing/lan/prefix/update";
    public static final String URI_PREFIX_UPDATE_REPLY_POST = "/thing/lan/prefix/update_reply";
    public static final String URI_PRODUCT_DEVICE_REPLACE = "{deviceName}";
    public static final String URI_PRODUCT_PRODUCT_REPLACE = "{productKey}";
    public static final String URI_PROPERTY_SET = "/thing/service/property/set";
    public static final String URI_SYS_PATH_PRE = "sys";
    public static final String URI_THING = "/thing";
    public static final String URI_TOPIC_LOCALDEVICE_STATECHANGE = "group/localstatechange";
    public static final String URI_TOPIC_REPLY_POST = "_reply";
    public static final String URI_UPDATE_DEVICE_INFO = "/sys/{productKey}/{deviceName}/thing/deviceinfo/update";
    public static final String URI_UPDATE_DEVICE_INFO_REPLY = "/sys/{productKey}/{deviceName}/thing/deviceinfo/update_reply";

    @Deprecated
    public static final String URI_USER = "/user";
    public static final String VALUE_SHA256 = "sha256";
    public static final String VERSION = "1.0";
    public static final String WIFI_CONNECT_FAIL_CODE = "wifiConnectFailCode";
    public static final String WIFI_STATUS_KEY = "status";
    public static final String WIFI_STATUS_NOTIFY_PARAMS = "params";

    @Retention(RetentionPolicy.SOURCE)
    public @interface NotifyType {
        public static final int CONNECTING = 2;
        public static final int CONNECT_FAIL = -1;
        public static final int OPEN_WIFI_INPUT = 0;
        public static final int RECONNECTING = 1;
    }
}
