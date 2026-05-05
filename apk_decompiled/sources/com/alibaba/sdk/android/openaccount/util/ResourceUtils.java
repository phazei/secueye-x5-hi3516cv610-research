package com.alibaba.sdk.android.openaccount.util;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.OpenAccountConfigs;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.message.Message;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.alibaba.sdk.android.push.notification.CustomNotificationBuilder;
import com.google.android.exoplayer2.text.ttml.TtmlNode;

/* JADX INFO: loaded from: classes.dex */
public class ResourceUtils {
    public static final String TAG = "oa_Resource";

    public static String getString(Context context, String str) {
        int identifier;
        if (context == null) {
            context = OpenAccountSDK.getAndroidContext();
        }
        if (OpenAccountConfigs.stringResourceSuffix != null) {
            identifier = getIdentifier(context, "string", str + OpenAccountUIConstants.UNDER_LINE + OpenAccountConfigs.stringResourceSuffix);
        } else {
            identifier = 0;
        }
        if (identifier == 0) {
            identifier = getIdentifier(context, "string", str);
        }
        if (identifier == 0) {
            Message messageCreateMessage = MessageUtils.createMessage(12, str);
            AliSDKLogger.log(TAG, messageCreateMessage);
            return messageCreateMessage.message;
        }
        return context.getResources().getString(identifier);
    }

    public static int getStringIdentifier(Context context, String str) {
        int identifier;
        if (context == null) {
            context = OpenAccountSDK.getAndroidContext();
        }
        if (OpenAccountConfigs.stringResourceSuffix != null) {
            identifier = getIdentifier(context, "string", str + OpenAccountUIConstants.UNDER_LINE + OpenAccountConfigs.stringResourceSuffix);
        } else {
            identifier = 0;
        }
        return identifier == 0 ? getIdentifier(context, "string", str) : identifier;
    }

    public static int getRLayout(Context context, String str) {
        return getIdentifier(context, TtmlNode.TAG_LAYOUT, str);
    }

    public static int getRDrawable(Context context, String str) {
        return getIdentifier(context, CustomNotificationBuilder.NOTIFICATION_ICON_RES_TYPE, str);
    }

    public static String getString(String str) {
        return getString(OpenAccountSDK.getAndroidContext(), str);
    }

    public static int getRLayout(String str) {
        return getIdentifier(OpenAccountSDK.getAndroidContext(), TtmlNode.TAG_LAYOUT, str);
    }

    public static int getRId(Context context, String str) {
        return getIdentifier(context, "id", str);
    }

    public static int getRStyleable(Context context, String str) {
        Object rStyleableField = getRStyleableField(context, str);
        if (rStyleableField == null) {
            return 0;
        }
        return ((Integer) rStyleableField).intValue();
    }

    public static int getRId(String str) {
        return getIdentifier(OpenAccountSDK.getAndroidContext(), "id", str);
    }

    public static int getRDrawable(String str) {
        return getIdentifier(OpenAccountSDK.getAndroidContext(), CustomNotificationBuilder.NOTIFICATION_ICON_RES_TYPE, str);
    }

    public static int getIdentifier(String str, String str2) {
        return getIdentifier(OpenAccountSDK.getAndroidContext(), str, str2);
    }

    public static int getIdentifier(Context context, String str, String str2) {
        if (context == null) {
            return 0;
        }
        int identifier = context.getResources().getIdentifier(str2, str, context.getPackageName());
        if (identifier != 0) {
            return identifier;
        }
        String bundleName = ConfigManager.getInstance().getBundleName();
        if (TextUtils.isEmpty(bundleName)) {
            return 0;
        }
        return context.getResources().getIdentifier(str2, str, bundleName);
    }

    public static final int[] getRStyleableIntArray(Context context, String str) {
        return (int[]) getRStyleableField(context, str);
    }

    private static Object getRStyleableField(Context context, String str) {
        try {
            return Class.forName(context.getPackageName() + ".R$styleable").getField(str).get(null);
        } catch (Throwable unused) {
            AliSDKLogger.log(TAG, MessageUtils.createMessage(12, str));
            String bundleName = ConfigManager.getInstance().getBundleName();
            if (!TextUtils.isEmpty(bundleName)) {
                try {
                    return Class.forName(bundleName + ".R$styleable").getField(str).get(null);
                } catch (Throwable th) {
                    th.printStackTrace();
                    return null;
                }
            }
            return null;
        }
    }
}
