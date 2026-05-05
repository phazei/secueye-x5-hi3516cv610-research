package com.alibaba.sdk.android.push.impl;

import android.content.Intent;
import com.taobao.accs.utl.ALog;
import com.taobao.agoo.BaseNotifyClickActivity;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageHelper;

/* JADX INFO: loaded from: classes.dex */
public class XiaoMiMsgParseImpl implements BaseNotifyClickActivity.INotifyListener {
    private static final String TAG = "MPS:MiPushRegister";

    @Override // com.taobao.agoo.BaseNotifyClickActivity.INotifyListener
    public String getMsgSource() {
        return "xiaomi";
    }

    @Override // com.taobao.agoo.BaseNotifyClickActivity.INotifyListener
    public String parseMsgFromIntent(Intent intent) {
        String content;
        try {
            content = ((MiPushMessage) intent.getSerializableExtra(PushMessageHelper.KEY_MESSAGE)).getContent();
        } catch (Exception unused) {
            content = null;
        }
        ALog.i(TAG, "parseMsgFromIntent msg:" + content, new Object[0]);
        return content;
    }

    public String toString() {
        return "INotifyListener: " + getMsgSource();
    }
}
