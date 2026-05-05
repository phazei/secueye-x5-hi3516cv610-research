package com.aliyun.iotx.linkvisual.media.audio;

/* JADX INFO: loaded from: classes2.dex */
public class LiveIntercomException extends Exception {
    public static final int CONNECTION_STREAM_FAILED = 3;
    public static final int INIT_AUDIO_PLAYER_FAILED = 16;
    public static final int INIT_RECORD_FAILED = 7;
    public static final int INVALID_AUDIO_PARAMS = 1;
    public static final int READ_RECORD_BUFFER_FAILED = 9;
    public static final int RECEIVE_STREAM_DATA_FAILED = 6;
    public static final int SEND_STREAM_DATA_FAILED = 5;
    public static final int START_LIVE_INTERCOM_REQUEST_FAILED = 2;
    public static final int START_RECORD_FAILED = 8;
    private int code;
    private int subCode;

    public LiveIntercomException(int i, int i2, String str) {
        super(str);
        this.code = i;
        this.subCode = i2;
    }

    public LiveIntercomException(int i, int i2, Throwable th) {
        super(th);
        this.subCode = i2;
        this.code = i;
    }

    public LiveIntercomException(int i, String str) {
        super(str);
        this.code = i;
    }

    public LiveIntercomException(int i, String str, Throwable th) {
        super(str, th);
        this.code = i;
    }

    public LiveIntercomException(int i, Throwable th) {
        super(th);
        this.code = i;
    }

    public int getCode() {
        return this.code;
    }

    public int getSubCode() {
        return this.subCode;
    }
}
