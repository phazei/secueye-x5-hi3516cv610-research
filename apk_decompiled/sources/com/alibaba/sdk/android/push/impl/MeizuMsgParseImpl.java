package com.alibaba.sdk.android.push.impl;

import android.content.Intent;
import com.taobao.accs.utl.ALog;
import com.taobao.agoo.BaseNotifyClickActivity;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: loaded from: classes.dex */
public class MeizuMsgParseImpl implements BaseNotifyClickActivity.INotifyListener {
    public static final String TAG = "MPS:MeizuPush";

    @Override // com.taobao.agoo.BaseNotifyClickActivity.INotifyListener
    public String getMsgSource() {
        return AgooConstants.MESSAGE_SYSTEM_SOURCE_MEIZU;
    }

    @Override // com.taobao.agoo.BaseNotifyClickActivity.INotifyListener
    public String parseMsgFromIntent(Intent intent) {
        String stringExtra = null;
        if (intent == null) {
            ALog.e(TAG, "parseMsgFromIntent null", new Object[0]);
            return null;
        }
        try {
            stringExtra = intent.getStringExtra(AgooConstants.MESSAGE_MEIZU_PAYLOAD);
            ALog.i(TAG, "parseMsgFromIntent msg:" + stringExtra, new Object[0]);
            return stringExtra;
        } catch (Throwable th) {
            ALog.e(TAG, "parseMsgFromIntent", th, new Object[0]);
            return stringExtra;
        }
    }
}
