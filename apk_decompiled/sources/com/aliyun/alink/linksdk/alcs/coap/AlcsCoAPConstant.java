package com.aliyun.alink.linksdk.alcs.coap;

import com.aliyun.alink.linksdk.tools.ALog;
import java.nio.charset.Charset;

/* JADX INFO: loaded from: classes2.dex */
public final class AlcsCoAPConstant {
    public static final int COAP_RECV_RESP_TIMEOUT = 1;
    public static final int COAP_REQUEST_SUCCESS = 0;
    public static final String COAP_SECURE_TCP_URI_SCHEME = "coaps+tcp";
    public static final String COAP_SECURE_URI_SCHEME = "coaps";
    public static final String COAP_TCP_URI_SCHEME = "coap+tcp";
    public static final String COAP_URI_SCHEME = "coap";
    public static final int DEFAULT_COAP_PORT = 5683;
    public static final int DEFAULT_COAP_SECURE_PORT = 5684;
    private static final String TAG = "AlcsCoAPConstant";
    public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    public static final int VERSION = 1;

    public static int getCodeClass(int i) {
        return (i & 224) >> 5;
    }

    public static int getCodeDetail(int i) {
        return i & 31;
    }

    public static boolean isEmptyMessage(int i) {
        return i == 0;
    }

    public static boolean isRequest(int i) {
        return i >= 1 && i <= 31;
    }

    public static boolean isResponse(int i) {
        return i >= 64 && i <= 191;
    }

    private AlcsCoAPConstant() {
    }

    public static String formatCode(int i) {
        return formatCode(getCodeClass(i), getCodeDetail(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String formatCode(int i, int i2) {
        return String.format("%d.%02d", Integer.valueOf(i), Integer.valueOf(i2));
    }

    public static boolean isTcpScheme(String str) {
        return COAP_TCP_URI_SCHEME.equalsIgnoreCase(str) || COAP_SECURE_TCP_URI_SCHEME.equalsIgnoreCase(str);
    }

    public static boolean isSecureScheme(String str) {
        return "coaps".equalsIgnoreCase(str) || COAP_SECURE_TCP_URI_SCHEME.equalsIgnoreCase(str);
    }

    public static boolean isSupportedScheme(String str) {
        return "coap".equalsIgnoreCase(str) || COAP_TCP_URI_SCHEME.equalsIgnoreCase(str) || "coaps".equalsIgnoreCase(str) || COAP_SECURE_TCP_URI_SCHEME.equalsIgnoreCase(str);
    }

    public static int getDefaultPort(String str) {
        if ("coap".equalsIgnoreCase(str)) {
            return 5683;
        }
        if ("coaps".equalsIgnoreCase(str)) {
            return 5684;
        }
        if (COAP_TCP_URI_SCHEME.equalsIgnoreCase(str)) {
            return 5683;
        }
        return COAP_SECURE_TCP_URI_SCHEME.equalsIgnoreCase(str) ? 5684 : 0;
    }

    public enum Type {
        CON(0),
        NON(1),
        ACK(2),
        RST(3);

        public final int value;

        Type(int i) {
            this.value = i;
        }

        public static Type valueOf(int i) {
            switch (i) {
                case 0:
                    return CON;
                case 1:
                    return NON;
                case 2:
                    return ACK;
                case 3:
                    return RST;
                default:
                    throw new IllegalArgumentException("Unknown CoAP type " + i);
            }
        }
    }

    public enum CodeClass {
        REQUEST(0),
        SUCCESS_RESPONSE(2),
        ERROR_RESPONSE(4),
        SERVER_ERROR_RESPONSE(5),
        SIGNAL(7);

        public final int value;

        CodeClass(int i) {
            this.value = i;
        }

        public static CodeClass valueOf(int i) {
            if (i == 0) {
                return REQUEST;
            }
            if (i == 2) {
                return SUCCESS_RESPONSE;
            }
            if (i != 7) {
                switch (i) {
                    case 4:
                        return ERROR_RESPONSE;
                    case 5:
                        return SERVER_ERROR_RESPONSE;
                    default:
                        throw new MessageFormatException(String.format("Unknown CoAP class code: %d", Integer.valueOf(i)));
                }
            }
            return SIGNAL;
        }
    }

    public enum Code {
        GET(1),
        POST(2),
        PUT(3),
        DELETE(4);

        public final int value;

        Code(int i) {
            this.value = i;
        }

        public static Code valueOf(int i) {
            int codeClass = AlcsCoAPConstant.getCodeClass(i);
            int codeDetail = AlcsCoAPConstant.getCodeDetail(i);
            if (codeClass > 0) {
                ALog.e(AlcsCoAPConstant.TAG, "Not a CoAP request code: %s", AlcsCoAPConstant.formatCode(codeClass, codeDetail));
                return null;
            }
            switch (codeDetail) {
                case 1:
                    return GET;
                case 2:
                    return POST;
                case 3:
                    return PUT;
                case 4:
                    return DELETE;
                default:
                    throw new MessageFormatException(String.format("Unknown CoAP request code: %s", AlcsCoAPConstant.formatCode(codeClass, codeDetail)));
            }
        }
    }

    public enum ResponseCode {
        _UNKNOWN_SUCCESS_CODE(CodeClass.SUCCESS_RESPONSE, 0),
        CREATED(CodeClass.SUCCESS_RESPONSE, 1),
        DELETED(CodeClass.SUCCESS_RESPONSE, 2),
        VALID(CodeClass.SUCCESS_RESPONSE, 3),
        CHANGED(CodeClass.SUCCESS_RESPONSE, 4),
        CONTENT(CodeClass.SUCCESS_RESPONSE, 5),
        CONTINUE(CodeClass.SUCCESS_RESPONSE, 31),
        BAD_REQUEST(CodeClass.ERROR_RESPONSE, 0),
        UNAUTHORIZED(CodeClass.ERROR_RESPONSE, 1),
        BAD_OPTION(CodeClass.ERROR_RESPONSE, 2),
        FORBIDDEN(CodeClass.ERROR_RESPONSE, 3),
        NOT_FOUND(CodeClass.ERROR_RESPONSE, 4),
        METHOD_NOT_ALLOWED(CodeClass.ERROR_RESPONSE, 5),
        NOT_ACCEPTABLE(CodeClass.ERROR_RESPONSE, 6),
        REQUEST_ENTITY_INCOMPLETE(CodeClass.ERROR_RESPONSE, 8),
        PRECONDITION_FAILED(CodeClass.ERROR_RESPONSE, 12),
        REQUEST_ENTITY_TOO_LARGE(CodeClass.ERROR_RESPONSE, 13),
        UNSUPPORTED_CONTENT_FORMAT(CodeClass.ERROR_RESPONSE, 15),
        INTERNAL_SERVER_ERROR(CodeClass.SERVER_ERROR_RESPONSE, 0),
        NOT_IMPLEMENTED(CodeClass.SERVER_ERROR_RESPONSE, 1),
        BAD_GATEWAY(CodeClass.SERVER_ERROR_RESPONSE, 2),
        SERVICE_UNAVAILABLE(CodeClass.SERVER_ERROR_RESPONSE, 3),
        GATEWAY_TIMEOUT(CodeClass.SERVER_ERROR_RESPONSE, 4),
        PROXY_NOT_SUPPORTED(CodeClass.SERVER_ERROR_RESPONSE, 5);

        public final int codeClass;
        public final int codeDetail;
        public final int value;

        ResponseCode(CodeClass codeClass, int i) {
            this.codeClass = codeClass.value;
            this.codeDetail = i;
            this.value = (codeClass.value << 5) | i;
        }

        public static ResponseCode valueOf(int i) {
            int codeClass = AlcsCoAPConstant.getCodeClass(i);
            int codeDetail = AlcsCoAPConstant.getCodeDetail(i);
            if (codeClass == 2) {
                return valueOfSuccessCode(codeDetail);
            }
            switch (codeClass) {
                case 4:
                    break;
                case 5:
                    break;
                default:
                    ALog.e(AlcsCoAPConstant.TAG, "Not a CoAP response code:" + AlcsCoAPConstant.formatCode(codeClass, codeDetail));
                    break;
            }
            return valueOfServerErrorCode(codeDetail);
        }

        private static ResponseCode valueOfSuccessCode(int i) {
            if (i != 31) {
                switch (i) {
                    case 1:
                        return CREATED;
                    case 2:
                        return DELETED;
                    case 3:
                        return VALID;
                    case 4:
                        return CHANGED;
                    case 5:
                        return CONTENT;
                    default:
                        return _UNKNOWN_SUCCESS_CODE;
                }
            }
            return CONTINUE;
        }

        private static ResponseCode valueOfClientErrorCode(int i) {
            switch (i) {
                case 0:
                    return BAD_REQUEST;
                case 1:
                    return UNAUTHORIZED;
                case 2:
                    return BAD_OPTION;
                case 3:
                    return FORBIDDEN;
                case 4:
                    return NOT_FOUND;
                case 5:
                    return METHOD_NOT_ALLOWED;
                case 6:
                    return NOT_ACCEPTABLE;
                case 7:
                case 9:
                case 10:
                case 11:
                case 14:
                default:
                    return BAD_REQUEST;
                case 8:
                    return REQUEST_ENTITY_INCOMPLETE;
                case 12:
                    return PRECONDITION_FAILED;
                case 13:
                    return REQUEST_ENTITY_TOO_LARGE;
                case 15:
                    return UNSUPPORTED_CONTENT_FORMAT;
            }
        }

        private static ResponseCode valueOfServerErrorCode(int i) {
            switch (i) {
                case 0:
                    return INTERNAL_SERVER_ERROR;
                case 1:
                    return NOT_IMPLEMENTED;
                case 2:
                    return BAD_GATEWAY;
                case 3:
                    return SERVICE_UNAVAILABLE;
                case 4:
                    return GATEWAY_TIMEOUT;
                case 5:
                    return PROXY_NOT_SUPPORTED;
                default:
                    return INTERNAL_SERVER_ERROR;
            }
        }

        @Override // java.lang.Enum
        public String toString() {
            return AlcsCoAPConstant.formatCode(this.codeClass, this.codeDetail);
        }

        public static boolean isSuccess(ResponseCode responseCode) {
            if (responseCode != null) {
                return responseCode.codeClass == CodeClass.SUCCESS_RESPONSE.value;
            }
            ALog.e(AlcsCoAPConstant.TAG, "ResponseCode must not be null!");
            return false;
        }

        public static boolean isClientError(ResponseCode responseCode) {
            if (responseCode != null) {
                return responseCode.codeClass == CodeClass.ERROR_RESPONSE.value;
            }
            throw new NullPointerException("ResponseCode must not be null!");
        }

        public static boolean isServerError(ResponseCode responseCode) {
            if (responseCode != null) {
                return responseCode.codeClass == CodeClass.SERVER_ERROR_RESPONSE.value;
            }
            throw new NullPointerException("ResponseCode must not be null!");
        }
    }

    public static final class MessageFormat {
        public static final int CODE_BITS = 8;
        public static final int EMPTY_CODE = 0;
        public static final int LENGTH_NIBBLE_BITS = 4;
        public static final int MESSAGE_ID_BITS = 16;
        public static final int OPTION_DELTA_BITS = 4;
        public static final int OPTION_LENGTH_BITS = 4;
        public static final byte PAYLOAD_MARKER = -1;
        public static final int REQUEST_CODE_LOWER_BOUND = 1;
        public static final int REQUEST_CODE_UPPER_BOUND = 31;
        public static final int RESPONSE_CODE_LOWER_BOUND = 64;
        public static final int RESPONSE_CODE_UPPER_BOUND = 191;
        public static final int TOKEN_LENGTH_BITS = 4;
        public static final int TYPE_BITS = 2;
        public static final int VERSION = 1;
        public static final int VERSION_BITS = 2;

        private MessageFormat() {
        }
    }
}
