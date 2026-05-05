package com.aliyun.alink.linksdk.id2;

import android.content.Context;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class Id2ItlsSdk {
    public static final int DEBUGLEVEL_ERROR = 1;
    public static final int DEBUGLEVEL_INFOR = 3;
    public static final int DEBUGLEVEL_NODEBUG = 0;
    public static final int DEBUGLEVEL_STATECHANGE = 2;
    public static final int DEBUGLEVEL_VERBOSE = 4;
    private static final String TAG = "Id2ItlsSdk";
    public static Context mContext;
    public static String mSoPath;

    public static void init(Context context) {
        mContext = context;
    }

    public static void init(InitParams initParams) {
        if (initParams == null) {
            ALog.e(TAG, "initParams null error");
        } else {
            mSoPath = initParams.soPath;
            mContext = initParams.context;
        }
    }
}
