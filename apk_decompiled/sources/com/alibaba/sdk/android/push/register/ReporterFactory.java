package com.alibaba.sdk.android.push.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.alibaba.sdk.android.push.register.ThirdPushManager;
import com.taobao.accs.utl.ALog;
import org.android.agoo.common.MsgDO;
import org.android.agoo.control.AgooFactory;

/* JADX INFO: loaded from: classes.dex */
public class ReporterFactory {
    private static final String TAG = "MPS:" + ReporterFactory.class.getCanonicalName();
    private static volatile IMsgReporter mMsgReporter;
    private static volatile ITokenReporter mTokenReporter;
    private static volatile IPushParser pushParser;
    private static volatile IPushReporter pushReporter;

    public interface IMsgReporter {
        void sendMsgToDecrypt(Context context, String str, byte[] bArr, String str2);
    }

    public interface IPushParser {
        Intent parseMsg(Context context, String str, String str2);
    }

    public interface IPushReporter {
        void reportPushClick(Context context, MsgDO msgDO);
    }

    @Deprecated
    public interface ITokenReporter {
        @Deprecated
        void reportToken(Context context, String str, String str2, String str3);
    }

    public interface ITokenReporterV2 extends ITokenReporter {
        void reportToken(Context context, String str, String str2, String str3, String str4);
    }

    private ReporterFactory() {
    }

    public static ITokenReporter getTokenReporter() {
        if (mTokenReporter == null) {
            synchronized (ReporterFactory.class) {
                if (mTokenReporter == null) {
                    mTokenReporter = new ITokenReporterV2() { // from class: com.alibaba.sdk.android.push.register.ReporterFactory.1
                        @Override // com.alibaba.sdk.android.push.register.ReporterFactory.ITokenReporterV2
                        public void reportToken(Context context, String str, String str2, String str3, String str4) {
                            if (TextUtils.isEmpty(str2) || context == null) {
                                return;
                            }
                            try {
                                ALog.i(ReporterFactory.TAG, "report " + str2 + " ThirdToken: " + str4 + ", version: " + str3, new Object[0]);
                                AgooFactory.getInstance(context).getNotifyManager().reportThirdPushToken(str, str4, str2, str3, true);
                            } catch (Throwable th) {
                                ALog.e(ReporterFactory.TAG, "agoo factory could not report third push token", new Object[0]);
                                th.printStackTrace();
                            }
                        }

                        @Override // com.alibaba.sdk.android.push.register.ReporterFactory.ITokenReporter
                        public void reportToken(Context context, String str, String str2, String str3) {
                            reportToken(context, "-SNAPSHOT", str, str2, str3);
                        }
                    };
                }
            }
        }
        return mTokenReporter;
    }

    public static String addPrefix(String str, String str2) {
        for (int i = 0; i < ThirdPushManager.ThirdPushReportKeyword.values().length; i++) {
            if (str.equals(ThirdPushManager.ThirdPushReportKeyword.values()[i].thirdTokenKeyword)) {
                return ThirdPushManager.ThirdPushReportKeyword.values()[i].thirdSdkVersionPrefix + str2;
            }
        }
        return str + str2;
    }

    public static void setTokenReporter(ITokenReporter iTokenReporter) {
        synchronized (ReporterFactory.class) {
            mTokenReporter = iTokenReporter;
        }
    }

    public static void setMsgReporter(IMsgReporter iMsgReporter) {
        synchronized (ReporterFactory.class) {
            mMsgReporter = iMsgReporter;
        }
    }

    public static IMsgReporter getMsgReporter() {
        if (mMsgReporter == null) {
            synchronized (ReporterFactory.class) {
                if (mMsgReporter == null) {
                    mMsgReporter = new IMsgReporter() { // from class: com.alibaba.sdk.android.push.register.ReporterFactory.2
                        @Override // com.alibaba.sdk.android.push.register.ReporterFactory.IMsgReporter
                        public void sendMsgToDecrypt(Context context, String str, byte[] bArr, String str2) {
                            try {
                                AgooFactory.getInstance(context).msgReceive(bArr, str, null);
                            } catch (Throwable th) {
                                ALog.e(ReporterFactory.TAG, "agoo factory could not report msg", new Object[0]);
                                th.printStackTrace();
                            }
                        }
                    };
                }
            }
        }
        return mMsgReporter;
    }

    public static void setPushParser(IPushParser iPushParser) {
        synchronized (ReporterFactory.class) {
            pushParser = iPushParser;
        }
    }

    public static IPushParser getPushParser() {
        if (pushParser == null) {
            synchronized (ReporterFactory.class) {
                if (pushParser == null) {
                    pushParser = new IPushParser() { // from class: com.alibaba.sdk.android.push.register.ReporterFactory.3
                        @Override // com.alibaba.sdk.android.push.register.ReporterFactory.IPushParser
                        public Intent parseMsg(Context context, String str, String str2) {
                            try {
                                Bundle bundleMsgReceiverPreHandler = AgooFactory.getInstance(context).msgReceiverPreHandler(str.getBytes("UTF-8"), str2, null, false);
                                String string = bundleMsgReceiverPreHandler.getString("body");
                                ALog.i(ReporterFactory.TAG, "begin parse EncryptedMsg", new Object[0]);
                                String encryptedMsg = AgooFactory.parseEncryptedMsg(string);
                                if (TextUtils.isEmpty(encryptedMsg)) {
                                    ALog.e(ReporterFactory.TAG, "parse EncryptedMsg fail, empty", new Object[0]);
                                } else {
                                    bundleMsgReceiverPreHandler.putString("body", encryptedMsg);
                                }
                                Intent intent = new Intent();
                                intent.putExtras(bundleMsgReceiverPreHandler);
                                AgooFactory.getInstance(context).saveMsg(str.getBytes("UTF-8"), "2");
                                if (TextUtils.isEmpty(encryptedMsg)) {
                                    return null;
                                }
                                return intent;
                            } catch (Throwable th) {
                                ALog.e(ReporterFactory.TAG, "agoo factory parse EncryptedMsg fail", th, new Object[0]);
                                return null;
                            }
                        }
                    };
                }
            }
        }
        return pushParser;
    }

    public static void setPushReporter(IPushReporter iPushReporter) {
        synchronized (ReporterFactory.class) {
            pushReporter = iPushReporter;
        }
    }

    public static IPushReporter getPushReporter() {
        if (pushReporter == null) {
            synchronized (ReporterFactory.class) {
                if (pushReporter == null) {
                    pushReporter = new IPushReporter() { // from class: com.alibaba.sdk.android.push.register.ReporterFactory.4
                        @Override // com.alibaba.sdk.android.push.register.ReporterFactory.IPushReporter
                        public void reportPushClick(Context context, MsgDO msgDO) {
                            AgooFactory.getInstance(context).getNotifyManager().report(msgDO, null);
                        }
                    };
                }
            }
        }
        return pushReporter;
    }
}
