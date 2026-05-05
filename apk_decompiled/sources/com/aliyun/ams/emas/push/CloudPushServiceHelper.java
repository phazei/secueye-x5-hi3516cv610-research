package com.aliyun.ams.emas.push;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.aliyun.ams.emas.push.notification.CPushMessage;
import com.taobao.accs.utl.ALog;
import com.taobao.agoo.AgooErrorCode;
import com.xiaomi.mipush.sdk.Constants;
import java.util.Calendar;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: loaded from: classes2.dex */
public class CloudPushServiceHelper {
    private static final String TAG = "MPS:CloudPushService";
    private Context context;
    private int mDoNotDisturbEndHour;
    private int mDoNotDisturbEndMinute;
    private int mDoNotDisturbStartHour;
    private int mDoNotDisturbStartMinute;
    private boolean mIsDoNotDisturbMode = false;

    public CloudPushServiceHelper(Context context) {
        this.context = context;
    }

    public void setDoNotDisturbMode(boolean z) {
        this.mIsDoNotDisturbMode = z;
    }

    public void setDoNotDisturb(int i, int i2, int i3, int i4, CommonCallback commonCallback) {
        ALog.d(TAG, "setDoNotDisturb " + i + ":" + i2 + Constants.ACCEPT_TIME_SEPARATOR_SERVER + i3 + ":" + i4, new Object[0]);
        if (i < 0 || i > 23 || i3 < 0 || i3 > 23 || i2 < 0 || i2 > 59 || i4 < 0 || i4 > 59) {
            if (commonCallback != null) {
                commonCallback.onFailed(AgooErrorCode.INVALID_ARG.getCode(), AgooErrorCode.INVALID_ARG.getMsg());
                return;
            }
            return;
        }
        this.mIsDoNotDisturbMode = true;
        this.mDoNotDisturbStartHour = i;
        this.mDoNotDisturbStartMinute = i2;
        this.mDoNotDisturbEndHour = i3;
        this.mDoNotDisturbEndMinute = i4;
        if (commonCallback != null) {
            commonCallback.onSuccess("");
        }
    }

    public boolean isInDoNotDisturbTimeWindow() {
        if (!this.mIsDoNotDisturbMode) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        int i = (this.mDoNotDisturbStartHour * 60) + this.mDoNotDisturbStartMinute;
        int i2 = (this.mDoNotDisturbEndHour * 60) + this.mDoNotDisturbEndMinute;
        int i3 = (calendar.get(11) * 60) + calendar.get(12);
        return i <= i2 ? i3 >= i && i3 <= i2 : i3 >= i || i3 <= i2;
    }

    public void clickMessage(CPushMessage cPushMessage) {
        if (cPushMessage == null || TextUtils.isEmpty(cPushMessage.getMessageId())) {
            ALog.e(TAG, "message is null", new Object[0]);
            return;
        }
        if (this.context == null) {
            ALog.e(TAG, "context is null", new Object[0]);
            return;
        }
        try {
            Intent intent = new Intent();
            intent.setAction(PushConfigHolder.SERVICE_CONTAINER_ACTION);
            intent.setClassName(this.context.getPackageName(), MsgService.class.getName());
            intent.putExtra("action_type", AgooConstants.MESSAGE_TYPE_OPEN);
            intent.putExtra("msgId", cPushMessage.getMessageId());
            intent.putExtra("extData", cPushMessage.getTraceInfo());
            this.context.startService(intent);
        } catch (Throwable th) {
            ALog.e(TAG, "Click message event upload failed.", th, new Object[0]);
        }
    }

    public void dismissMessage(CPushMessage cPushMessage) {
        if (cPushMessage == null || TextUtils.isEmpty(cPushMessage.getMessageId())) {
            ALog.e(TAG, "message is null", new Object[0]);
            return;
        }
        if (this.context == null) {
            ALog.e(TAG, "context is null", new Object[0]);
            return;
        }
        try {
            Intent intent = new Intent();
            intent.setAction(PushConfigHolder.SERVICE_CONTAINER_ACTION);
            intent.setClassName(this.context.getPackageName(), MsgService.class.getName());
            intent.putExtra("action_type", AgooConstants.MESSAGE_TYPE_DELETE);
            intent.putExtra("msgId", cPushMessage.getMessageId());
            intent.putExtra("extData", cPushMessage.getTraceInfo());
            this.context.startService(intent);
        } catch (Throwable th) {
            ALog.e(TAG, "Dismiss message event upload failed.", th, new Object[0]);
        }
    }
}
