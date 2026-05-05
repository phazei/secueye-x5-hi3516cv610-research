package com.aliyun.iotx.linkvisual.media.video;

/* JADX INFO: loaded from: classes2.dex */
public class PlayerException extends Exception {
    public static final int RENDER_ERROR = 7;
    public static final int SOURCE_ERROR = 6;
    public static final int SUB_CODE_RENDER_DECODE_ERROR = 1000;
    public static final int SUB_CODE_SOURCE_INVALID_DECRYPTE_KEY = 1006;
    public static final int SUB_CODE_SOURCE_INVALID_HLS_URL = 1101;
    public static final int SUB_CODE_SOURCE_INVALID_RTMP_URL = 1007;
    public static final int SUB_CODE_SOURCE_PARAMETER_ERROR = 1008;
    public static final int SUB_CODE_SOURCE_QUERY_URL_FAILED = 1009;
    public static final int SUB_CODE_SOURCE_STREAM_CONNECT_ERROR = 1005;
    public static final int SUB_CODE_UNEXPECTED_PULL_STREAM_ERROR = 1100;
    public static final int UNEXPECTED_ERROR = 8;
    private int code;
    private int subCode;

    public PlayerException(int i, int i2, String str) {
        super(str);
        this.code = i;
        this.subCode = i2;
    }

    public PlayerException(int i, String str) {
        super(str);
        this.code = i;
    }

    public int getCode() {
        return this.code;
    }

    public int getSubCode() {
        return this.subCode;
    }
}
