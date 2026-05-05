package com.alibaba.sdk.android.openaccount.ui.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes.dex */
public class SpecialLoginConstants {

    public interface FromScene {
        public static final int MOBILE_LOGIN = 0;
        public static final int MOBILE_REGISTER = 2;
        public static final int MOBILE_RESET_PWD = 1;

        @Retention(RetentionPolicy.SOURCE)
        public @interface FromSceneCode {
        }
    }
}
