package lvbyte;

import jp.wasabeef.glide.transformations.BuildConfig;

/* JADX INFO: loaded from: classes4.dex */
public enum lvdo {
    LIVE_QUERY("/vision/customer/stream/query", "2.1.6", "/vision/tmall/stream/query", "1.0.3"),
    CLOUD_VOD_BY_FILENAME("/vision/customer/vod/getbyfilename", "2.0.0", "/vision/tmall/vod/getbyfilename", "1.0.0"),
    LOCAL_VOD_FILE_BY_TIME("/vision/customer/vod/localfile/getbytime", "2.1.7", "/vision/tmall/vod/localfile/getbytime", "1.0.2"),
    LOCAL_VOD_FILE_BY_NAME("/vision/customer/vod/localfile/getbyname", "2.1.4", "/vision/tmall/vod/localfile/getbyname", "1.0.2"),
    VOICE_INTERCOM_START("/vision/customer/voice/intercom/start", BuildConfig.VERSION_NAME, "/vision/tmall/voice/intercom/start", "1.0.0"),
    SLS_TOKEN_QUERY("/vision/customer/sls/token/query", "1.0.0", "/vision/tmall/sls/token/query", "1.0.0");


    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private String f7949lvdo;

    /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
    private String f7950lvfor;

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    private String f7951lvif;

    /* JADX INFO: renamed from: lvint, reason: collision with root package name */
    private String f7952lvint;

    lvdo(String str, String str2, String str3, String str4) {
        this.f7949lvdo = str;
        this.f7950lvfor = str2;
        this.f7951lvif = str3;
        this.f7952lvint = str4;
    }

    public String lvdo() {
        return this.f7949lvdo;
    }

    public String lvfor() {
        return this.f7951lvif;
    }

    public String lvif() {
        return this.f7950lvfor;
    }

    public String lvint() {
        return this.f7952lvint;
    }
}
