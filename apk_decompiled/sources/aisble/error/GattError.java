package aisble.error;

import anet.channel.util.HttpConstant;

/* JADX INFO: loaded from: classes.dex */
public class GattError {
    public static final int GATT_AUTH_FAIL = 137;
    public static final int GATT_BUSY = 132;
    public static final int GATT_CCCD_CFG_ERROR = 253;
    public static final int GATT_CMD_STARTED = 134;
    public static final int GATT_CONGESTED = 143;
    public static final int GATT_CONN_CANCEL = 256;
    public static final int GATT_CONN_FAIL_ESTABLISH = 62;
    public static final int GATT_CONN_L2C_FAILURE = 1;
    public static final int GATT_CONN_LMP_TIMEOUT = 34;
    public static final int GATT_CONN_TERMINATE_LOCAL_HOST = 22;
    public static final int GATT_CONN_TERMINATE_PEER_USER = 19;
    public static final int GATT_CONN_TIMEOUT = 8;
    public static final int GATT_CONTROLLER_BUSY = 58;
    public static final int GATT_DB_FULL = 131;
    public static final int GATT_ENCRYPTED_NO_MITM = 141;
    public static final int GATT_ERROR = 133;
    public static final int GATT_ERR_UNLIKELY = 14;
    public static final int GATT_ILLEGAL_PARAMETER = 135;
    public static final int GATT_INSUF_AUTHENTICATION = 5;
    public static final int GATT_INSUF_AUTHORIZATION = 8;
    public static final int GATT_INSUF_ENCRYPTION = 15;
    public static final int GATT_INSUF_KEY_SIZE = 12;
    public static final int GATT_INSUF_RESOURCE = 17;
    public static final int GATT_INTERNAL_ERROR = 129;
    public static final int GATT_INVALID_ATTR_LEN = 13;
    public static final int GATT_INVALID_CFG = 139;
    public static final int GATT_INVALID_HANDLE = 1;
    public static final int GATT_INVALID_OFFSET = 7;
    public static final int GATT_INVALID_PDU = 4;
    public static final int GATT_MORE = 138;
    public static final int GATT_NOT_ENCRYPTED = 142;
    public static final int GATT_NOT_FOUND = 10;
    public static final int GATT_NOT_LONG = 11;
    public static final int GATT_NO_RESOURCES = 128;
    public static final int GATT_PENDING = 136;
    public static final int GATT_PREPARE_Q_FULL = 9;
    public static final int GATT_PROCEDURE_IN_PROGRESS = 254;
    public static final int GATT_READ_NOT_PERMIT = 2;
    public static final int GATT_REQ_NOT_SUPPORTED = 6;
    public static final int GATT_SERVICE_STARTED = 140;
    public static final int GATT_SUCCESS = 0;
    public static final int GATT_UNACCEPT_CONN_INTERVAL = 59;
    public static final int GATT_UNSUPPORT_GRP_TYPE = 16;
    public static final int GATT_VALUE_OUT_OF_RANGE = 255;
    public static final int GATT_WRITE_NOT_PERMIT = 3;
    public static final int GATT_WRONG_STATE = 130;
    public static final int TOO_MANY_OPEN_CONNECTIONS = 257;

    public static String parse(int i) {
        if (i == 34) {
            return "GATT CONN LMP TIMEOUT";
        }
        if (i == 257) {
            return "TOO MANY OPEN CONNECTIONS";
        }
        if (i == 58) {
            return "GATT CONTROLLER BUSY";
        }
        if (i == 59) {
            return "GATT UNACCEPT CONN INTERVAL";
        }
        switch (i) {
            case 1:
                return "GATT INVALID HANDLE";
            case 2:
                return "GATT READ NOT PERMIT";
            case 3:
                return "GATT WRITE NOT PERMIT";
            case 4:
                return "GATT INVALID PDU";
            case 5:
                return "GATT INSUF AUTHENTICATION";
            case 6:
                return "GATT REQ NOT SUPPORTED";
            case 7:
                return "GATT INVALID OFFSET";
            case 8:
                return "GATT INSUF AUTHORIZATION";
            case 9:
                return "GATT PREPARE Q FULL";
            case 10:
                return "GATT NOT FOUND";
            case 11:
                return "GATT NOT LONG";
            case 12:
                return "GATT INSUF KEY SIZE";
            case 13:
                return "GATT INVALID ATTR LEN";
            case 14:
                return "GATT ERR UNLIKELY";
            case 15:
                return "GATT INSUF ENCRYPTION";
            case 16:
                return "GATT UNSUPPORT GRP TYPE";
            case 17:
                return "GATT INSUF RESOURCE";
            default:
                switch (i) {
                    case 128:
                        return "GATT NO RESOURCES";
                    case 129:
                        return "GATT INTERNAL ERROR";
                    case 130:
                        return "GATT WRONG STATE";
                    case 131:
                        return "GATT DB FULL";
                    case 132:
                        return "GATT BUSY";
                    case 133:
                        return "GATT ERROR";
                    case 134:
                        return "GATT CMD STARTED";
                    case 135:
                        return "GATT ILLEGAL PARAMETER";
                    case 136:
                        return "GATT PENDING";
                    case 137:
                        return "GATT AUTH FAIL";
                    case 138:
                        return "GATT MORE";
                    case 139:
                        return "GATT INVALID CFG";
                    case 140:
                        return "GATT SERVICE STARTED";
                    case 141:
                        return "GATT ENCRYPTED NO MITM";
                    case 142:
                        return "GATT NOT ENCRYPTED";
                    case 143:
                        return "GATT CONGESTED";
                    default:
                        switch (i) {
                            case GATT_CCCD_CFG_ERROR /* 253 */:
                                return "GATT CCCD CFG ERROR";
                            case 254:
                                return "GATT PROCEDURE IN PROGRESS";
                            case 255:
                                return "GATT VALUE OUT OF RANGE";
                            default:
                                return "UNKNOWN (" + i + ")";
                        }
                }
        }
    }

    public static String parseConnectionError(int i) {
        if (i == 8) {
            return "GATT CONN TIMEOUT";
        }
        if (i == 19) {
            return "GATT CONN TERMINATE PEER USER";
        }
        if (i == 22) {
            return "GATT CONN TERMINATE LOCAL HOST";
        }
        if (i == 34) {
            return "GATT CONN LMP TIMEOUT";
        }
        if (i == 62) {
            return "GATT CONN FAIL ESTABLISH";
        }
        if (i == 133) {
            return "GATT ERROR";
        }
        if (i == 256) {
            return "GATT CONN CANCEL ";
        }
        switch (i) {
            case 0:
                return HttpConstant.SUCCESS;
            case 1:
                return "GATT CONN L2C FAILURE";
            default:
                return "UNKNOWN (" + i + ")";
        }
    }
}
