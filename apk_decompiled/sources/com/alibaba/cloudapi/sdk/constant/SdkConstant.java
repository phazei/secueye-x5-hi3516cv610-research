package com.alibaba.cloudapi.sdk.constant;

import java.nio.charset.Charset;
import org.apache.commons.codec.CharEncoding;

/* JADX INFO: loaded from: classes.dex */
public class SdkConstant {
    public static final String CLOUDAPI_CA_HEADER_TO_SIGN_PREFIX_SYSTEM = "x-ca-";
    public static final String CLOUDAPI_CA_VERSION_VALUE = "1";
    public static final String CLOUDAPI_COMMAND_CONNECTION_RUNS_OUT = "CR";
    public static final String CLOUDAPI_COMMAND_HEART_BEAT_REQUEST = "H1";
    public static final String CLOUDAPI_COMMAND_HEART_BEAT_RESPONSE = "HO";
    public static final String CLOUDAPI_COMMAND_NOTIFY_REQUEST = "NF";
    public static final String CLOUDAPI_COMMAND_NOTIFY_RESPONSE = "NO";
    public static final String CLOUDAPI_COMMAND_OVER_FLOW_BY_SECOND = "OS";
    public static final String CLOUDAPI_COMMAND_REGISTER_FAIL_REQUEST = "RF";
    public static final String CLOUDAPI_COMMAND_REGISTER_REQUEST = "RG";
    public static final String CLOUDAPI_COMMAND_REGISTER_SUCCESS_RESPONSE = "RO";
    public static final Charset CLOUDAPI_ENCODING = Charset.forName("UTF-8");
    public static final Charset CLOUDAPI_HEADER_ENCODING = Charset.forName(CharEncoding.ISO_8859_1);
    public static final String CLOUDAPI_LF = "\n";
    public static final String CLOUDAPI_USER_AGENT = "ALIYUN-ANDROID-DEMO";
    public static final String CLOUDAPI_X_CA_ERROR_MESSAGE = "x-ca-error-message";
    public static final String CLOUDAPI_X_CA_KEY = "x-ca-key";
    public static final String CLOUDAPI_X_CA_NONCE = "x-ca-nonce";
    public static final String CLOUDAPI_X_CA_SEQ = "x-ca-seq";
    public static final String CLOUDAPI_X_CA_SIGNATURE = "x-ca-signature";
    public static final String CLOUDAPI_X_CA_SIGNATURE_HEADERS = "x-ca-signature-headers";
    public static final String CLOUDAPI_X_CA_SIGNATURE_METHOD = "X-Ca-Signature-Method";
    public static final String CLOUDAPI_X_CA_TIMESTAMP = "x-ca-timestamp";
    public static final String CLOUDAPI_X_CA_VERSION = "CA_VERSION";
    public static final String CLOUDAPI_X_CA_WEBSOCKET_API_TYPE = "x-ca-websocket_api_type";
}
