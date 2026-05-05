package com.alibaba.sdk.android.push.impl;

import android.content.Intent;
import com.facebook.applinks.AppLinkData;
import com.taobao.accs.utl.ALog;
import com.taobao.agoo.BaseNotifyClickActivity;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: loaded from: classes.dex */
public class HonorParserImpl implements BaseNotifyClickActivity.INotifyListener {
    private static final String TAG = "MPS:HonorParserImpl";

    @Override // com.taobao.agoo.BaseNotifyClickActivity.INotifyListener
    public String getMsgSource() {
        return AgooConstants.MESSAGE_SYSTEM_SOURCE_HONOR;
    }

    @Override // com.taobao.agoo.BaseNotifyClickActivity.INotifyListener
    public String parseMsgFromIntent(Intent intent) {
        if (intent == null) {
            ALog.e(TAG, "parseMsgFromIntent null", new Object[0]);
            return null;
        }
        try {
            return intent.getStringExtra(AppLinkData.ARGUMENTS_EXTRAS_KEY);
        } catch (Throwable th) {
            ALog.e(TAG, "parseMsgFromIntent", th, new Object[0]);
            return null;
        }
    }
}
