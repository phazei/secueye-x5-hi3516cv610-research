package com.taobao.agoo;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.alibaba.sdk.android.push.PushInitStatus;
import com.alibaba.sdk.android.push.impl.HuaweiMsgParseImpl;
import com.alibaba.sdk.android.push.impl.MeizuMsgParseImpl;
import com.alibaba.sdk.android.push.impl.OppoMsgParseImpl;
import com.alibaba.sdk.android.push.impl.VivoMsgParseImpl;
import com.alibaba.sdk.android.push.impl.XiaoMiMsgParseImpl;
import com.alibaba.sdk.android.push.register.ReporterFactory;
import com.alibaba.sdk.android.push.utils.ThreadUtil;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AppMonitorAdapter;
import com.taobao.agoo.BaseNotifyClickActivity;
import java.util.Iterator;
import org.android.agoo.common.AgooConstants;
import org.android.agoo.common.MsgDO;

/* JADX INFO: loaded from: classes3.dex */
public abstract class BaseNotifyClick {
    private static final String TAG = "MPS.BaseNotifyClick";
    private Context context;
    private Intent mIntent = null;
    private String msgSource;

    public abstract void onMessage(Intent intent);

    public void onNotPushData(Intent intent) {
    }

    public void onParseFailed(Intent intent) {
    }

    public void onCreate(Context context, Intent intent) {
        ALog.i(TAG, "onCreate", new Object[0]);
        this.context = context;
        this.mIntent = intent;
        if (PushInitStatus.getInstance().isInitPush) {
            buildMessage(this.mIntent);
        }
    }

    public void onNewIntent(Intent intent) {
        ALog.i(TAG, "onNewIntent", new Object[0]);
        this.mIntent = intent;
        if (PushInitStatus.getInstance().isInitPush) {
            buildMessage(this.mIntent);
        }
    }

    public void onInitPush() {
        ALog.i(TAG, "onInitPush init push success mIntent = " + this.mIntent, new Object[0]);
        Intent intent = this.mIntent;
        if (intent != null) {
            buildMessage(intent);
        }
    }

    private void buildMessage(final Intent intent) {
        ThreadUtil.getExecutor().execute(new Runnable() { // from class: com.taobao.agoo.BaseNotifyClick.1
            @Override // java.lang.Runnable
            public void run() {
                String msgByThirdPush;
                Intent msg = null;
                try {
                    try {
                        try {
                            if (intent != null) {
                                try {
                                    msgByThirdPush = BaseNotifyClick.this.parseMsgByThirdPush(intent);
                                } catch (Throwable unused) {
                                    msgByThirdPush = null;
                                }
                                if (!TextUtils.isEmpty(msgByThirdPush) && !TextUtils.isEmpty(BaseNotifyClick.this.msgSource)) {
                                    try {
                                        msg = ReporterFactory.getPushParser().parseMsg(BaseNotifyClick.this.context, msgByThirdPush, BaseNotifyClick.this.msgSource);
                                    } catch (Throwable unused2) {
                                    }
                                    if (msg != null) {
                                        BaseNotifyClick.this.reportClickNotifyMsg(msg);
                                    } else {
                                        BaseNotifyClick.this.onParseFailed(intent);
                                    }
                                } else {
                                    BaseNotifyClick.this.onNotPushData(intent);
                                    ALog.w(BaseNotifyClick.TAG, "parseMsgFromNotifyListener null!!", "source", BaseNotifyClick.this.msgSource);
                                }
                            }
                            if (msg != null) {
                                BaseNotifyClick.this.onMessage(msg);
                            }
                        } catch (Throwable th) {
                            ALog.e(BaseNotifyClick.TAG, "onMessage", th, new Object[0]);
                        }
                    } catch (Throwable th2) {
                        ALog.e(BaseNotifyClick.TAG, "buildMessage", th2, new Object[0]);
                        if (0 != 0) {
                            BaseNotifyClick.this.onMessage(null);
                        }
                    }
                } catch (Throwable th3) {
                    if (0 != 0) {
                        try {
                            BaseNotifyClick.this.onMessage(null);
                        } catch (Throwable th4) {
                            ALog.e(BaseNotifyClick.TAG, "onMessage", th4, new Object[0]);
                        }
                    }
                    throw th3;
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String parseMsgByThirdPush(Intent intent) {
        String msgFromIntent;
        if (BaseNotifyClickActivity.notifyListeners != null && BaseNotifyClickActivity.notifyListeners.size() > 0) {
            Iterator<BaseNotifyClickActivity.INotifyListener> it = BaseNotifyClickActivity.notifyListeners.iterator();
            msgFromIntent = null;
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                BaseNotifyClickActivity.INotifyListener next = it.next();
                String msgFromIntent2 = next.parseMsgFromIntent(intent);
                if (!TextUtils.isEmpty(msgFromIntent2)) {
                    this.msgSource = next.getMsgSource();
                    msgFromIntent = msgFromIntent2;
                    break;
                }
                msgFromIntent = msgFromIntent2;
            }
        } else {
            ALog.w(TAG, "no impl, try use default impl to parse intent!", new Object[0]);
            BaseNotifyClickActivity.INotifyListener huaweiMsgParseImpl = new HuaweiMsgParseImpl();
            msgFromIntent = huaweiMsgParseImpl.parseMsgFromIntent(intent);
            if (TextUtils.isEmpty(msgFromIntent)) {
                huaweiMsgParseImpl = new XiaoMiMsgParseImpl();
                msgFromIntent = huaweiMsgParseImpl.parseMsgFromIntent(intent);
            }
            if (TextUtils.isEmpty(msgFromIntent)) {
                huaweiMsgParseImpl = new OppoMsgParseImpl();
                msgFromIntent = huaweiMsgParseImpl.parseMsgFromIntent(intent);
            }
            if (TextUtils.isEmpty(msgFromIntent)) {
                huaweiMsgParseImpl = new VivoMsgParseImpl();
                ((VivoMsgParseImpl) huaweiMsgParseImpl).setContext(this.context);
                msgFromIntent = huaweiMsgParseImpl.parseMsgFromIntent(intent);
            }
            if (TextUtils.isEmpty(msgFromIntent)) {
                huaweiMsgParseImpl = new MeizuMsgParseImpl();
                msgFromIntent = huaweiMsgParseImpl.parseMsgFromIntent(intent);
            }
            if (TextUtils.isEmpty(msgFromIntent)) {
                AppMonitorAdapter.commitCount("accs", "error", "parse 3push error", 0.0d);
            } else {
                this.msgSource = huaweiMsgParseImpl.getMsgSource();
                AppMonitorAdapter.commitCount("accs", "error", "parse 3push default " + this.msgSource, 0.0d);
            }
        }
        ALog.i(TAG, "parseMsgByThirdPush", "result", msgFromIntent, "msgSource", this.msgSource);
        return msgFromIntent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportClickNotifyMsg(Intent intent) {
        try {
            String stringExtra = intent.getStringExtra("id");
            String stringExtra2 = intent.getStringExtra(AgooConstants.MESSAGE_SOURCE);
            String stringExtra3 = intent.getStringExtra(AgooConstants.MESSAGE_REPORT);
            String stringExtra4 = intent.getStringExtra("extData");
            MsgDO msgDO = new MsgDO();
            msgDO.msgIds = stringExtra;
            msgDO.extData = stringExtra4;
            msgDO.messageSource = stringExtra2;
            msgDO.reportStr = stringExtra3;
            msgDO.msgStatus = "8";
            ALog.i(TAG, "reportClickNotifyMsg messageId:" + stringExtra + " source:" + stringExtra2 + " reportStr:" + stringExtra3 + " status:" + msgDO.msgStatus, new Object[0]);
            ReporterFactory.getPushReporter().reportPushClick(this.context, msgDO);
        } catch (Exception e) {
            ALog.e(TAG, "reportClickNotifyMsg exception: " + e, new Object[0]);
        }
    }
}
