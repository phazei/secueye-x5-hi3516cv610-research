package com.taobao.accs.client;

import android.content.Context;
import com.taobao.accs.ChannelService;
import com.taobao.accs.IProcessName;
import com.taobao.accs.data.Message;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.OrangeAdapter;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class GlobalConfig {
    public static boolean enableCookie = true;

    public static void setControlFrameMaxRetry(int i) {
        Message.f6297a = i;
    }

    public static void setMainProcessName(String str) {
        AdapterGlobalClientInfo.mMainProcessName = str;
    }

    public static void setChannelProcessName(String str) {
        AdapterGlobalClientInfo.mChannelProcessName = str;
    }

    public static void setCurrProcessNameImpl(IProcessName iProcessName) {
        AdapterGlobalClientInfo.mProcessNameImpl = iProcessName;
    }

    public static void setEnableForground(Context context, boolean z) {
        ALog.i("GlobalConfig", "setEnableForground", "enable", Boolean.valueOf(z));
        OrangeAdapter.saveConfigToSP(context, ChannelService.SUPPORT_FOREGROUND_VERSION_KEY, z ? 24 : 0);
    }
}
