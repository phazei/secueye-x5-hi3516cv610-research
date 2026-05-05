package com.alibaba.ailabs.iot.aisbase.env;

/* JADX INFO: loaded from: classes.dex */
public class AppEnv {
    public static final boolean IS_GENIE_ENV = isGenieAppEnv();
    public static final String MTOP_CLASS_NAME = "mtopsdk.mtop.domain.MtopRequest";

    public static boolean isGenieAppEnv() {
        try {
            Class.forName(MTOP_CLASS_NAME);
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
