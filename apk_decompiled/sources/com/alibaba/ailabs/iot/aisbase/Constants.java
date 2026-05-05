package com.alibaba.ailabs.iot.aisbase;

import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class Constants {
    public static final UUID AIS_DATA_IN = UUID.fromString("0000FED4-0000-1000-8000-00805F9B34FB");
    public static final UUID AIS_COMMAND_OUT = UUID.fromString("0000FED5-0000-1000-8000-00805F9B34FB");
    public static final UUID AIS_COMMAND_RES = UUID.fromString("0000FED6-0000-1000-8000-00805F9B34FB");
    public static final UUID AIS_OTA_OUT = UUID.fromString("0000FED7-0000-1000-8000-00805F9B34FB");
    public static final UUID AIS_OTA_RES = UUID.fromString("0000FED8-0000-1000-8000-00805F9B34FB");
    public static final UUID AIS_PRIMARY_SERVICE = UUID.fromString("0000FEB3-0000-1000-8000-00805F9B34FB");
    public static final String UUID_RFCOMM = "EB3E0AF3-57F4-4789-AB55-86508580296A";
    public static final UUID RFCOMM_UUID = UUID.fromString(UUID_RFCOMM);
    public static final byte[] ALIBABA_CID = {1, -88};

    public interface CMD_TYPE {
        public static final byte CMD_AUDIO_UPSTREAM = 48;
        public static final byte CMD_BUSINESS_DOWNSTREAM = 60;
        public static final byte CMD_BUSINESS_UPSTREAM = 61;
        public static final byte CMD_DELIVERY_RANDOM = 16;
        public static final byte CMD_DELIVERY_RES = 18;
        public static final byte CMD_DEVICE_ABILITY_RES = 51;
        public static final byte CMD_DEV_LOG_NOTIFY = 64;
        public static final byte CMD_ERROR = 15;
        public static final byte CMD_GENERAL_COMMAND = 2;
        public static final byte CMD_GENERAL_NOTIFY = 1;
        public static final byte CMD_GENERAL_RESPONSE = 3;
        public static final byte CMD_GEN_CIPHER = 17;
        public static final byte CMD_GET_FIRMWARE_VERSION = 32;
        public static final byte CMD_GET_FIRMWARE_VERSION_RES = 33;
        public static final byte CMD_GET_FIRMWARE_VERSION_RESEX = 42;
        public static final byte CMD_GET_MANUFACTURER_DATA = 7;
        public static final byte CMD_GET_MANUFACTURER_DATA_RES = 8;
        public static final byte CMD_HANDLE_KEY_RES = 19;
        public static final byte CMD_NOTIFY_STATUS = 54;
        public static final byte CMD_NOTIFY_STATUS_ACK = 55;
        public static final byte CMD_OTA = 47;
        public static final byte CMD_OTA_REQUEST_VERIFY = 37;
        public static final byte CMD_OTA_RES = 36;
        public static final byte CMD_OTA_VERIFY_RES = 38;
        public static final byte CMD_REQUEST_OTA = 34;
        public static final byte CMD_REQUEST_OTA_RES = 35;
        public static final byte CMD_REQUEST_SIGNATURE = 52;
        public static final byte CMD_SEND_DEVICE_INFO = 50;
        public static final byte CMD_SIGNATURE_RES = 53;
        public static final byte CMD_STATUS_REPORT = 49;
    }

    public interface CommandErrorCode {
        public static final int ERROR_BROKEN_CONNECTION = 0;
        public static final int ERROR_COMMAND_TIMEOUT = 2;
        public static final int ERROR_OTHER = 1;
    }

    public interface OTAErrorCode {
        public static final int ERROR_BROKEN_CONNECTION = 0;
        public static final int ERROR_COMMAND_TIMEOUT = 5;
        public static final int ERROR_EXIST_OTA = 4;
        public static final int ERROR_ILLEGAL_COMMAND = 7;
        public static final int ERROR_LOSS_LINK = 6;
        public static final int ERROR_NOT_ALLOW_UPGRADE = 2;
        public static final int ERROR_USER_TERMINATED = 8;
        public static final int ERROR_VERIFY_FAILED = 3;
        public static final int ERROR_WRITE_COMMAND = 1;
    }
}
