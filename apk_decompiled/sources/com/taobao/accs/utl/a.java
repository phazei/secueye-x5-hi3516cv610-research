package com.taobao.accs.utl;

import com.alibaba.sdk.android.error.ErrorCode;
import com.taobao.accs.AccsErrorCode;
import com.taobao.accs.IAppReceiver;
import com.taobao.accs.IAppReceiverV1;
import com.taobao.accs.IAppReceiverV2;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class a extends IAppReceiverV2 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private IAppReceiver f6451a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f6452b = false;

    public static IAppReceiver a(IAppReceiver iAppReceiver) {
        if (iAppReceiver == null) {
            return null;
        }
        return new a(iAppReceiver);
    }

    private a(IAppReceiver iAppReceiver) {
        this.f6451a = iAppReceiver;
    }

    @Override // com.taobao.accs.IAppReceiverV1, com.taobao.accs.IAppReceiver
    public void onData(String str, String str2, byte[] bArr) {
        this.f6451a.onData(str, str2, bArr);
    }

    @Override // com.taobao.accs.IAppReceiverV1, com.taobao.accs.IAppReceiver
    public void onBindApp(int i) {
        if (this.f6452b) {
            return;
        }
        this.f6452b = true;
        this.f6451a.onBindApp(i);
    }

    @Override // com.taobao.accs.IAppReceiverV2, com.taobao.accs.IAppReceiverV1
    public void onBindApp(int i, String str) {
        if (this.f6452b) {
            return;
        }
        this.f6452b = true;
        IAppReceiver iAppReceiver = this.f6451a;
        if (iAppReceiver instanceof IAppReceiverV1) {
            ((IAppReceiverV1) iAppReceiver).onBindApp(i, str);
        } else {
            iAppReceiver.onBindApp(i);
        }
    }

    @Override // com.taobao.accs.IAppReceiverV2
    public void onBindApp(int i, String str, String str2) {
        if (this.f6452b) {
            return;
        }
        if (i == AccsErrorCode.SUCCESS.getCodeInt()) {
            this.f6452b = true;
        }
        IAppReceiver iAppReceiver = this.f6451a;
        if (iAppReceiver instanceof IAppReceiverV2) {
            ((IAppReceiverV2) iAppReceiver).onBindApp(i, str, str2);
        } else if (iAppReceiver instanceof IAppReceiverV1) {
            ((IAppReceiverV1) iAppReceiver).onBindApp(i, str2);
        } else {
            iAppReceiver.onBindApp(i);
        }
    }

    @Override // com.taobao.accs.IAppReceiverV2, com.taobao.accs.IAppReceiverV1, com.taobao.accs.IAppReceiver
    public void onUnbindApp(int i) {
        if (this.f6452b) {
            this.f6452b = false;
            this.f6451a.onUnbindApp(i);
        }
    }

    @Override // com.taobao.accs.IAppReceiverV2
    public void onUnbindApp(int i, String str) {
        if (this.f6452b) {
            this.f6452b = false;
            IAppReceiver iAppReceiver = this.f6451a;
            if (iAppReceiver instanceof IAppReceiverV2) {
                ((IAppReceiverV2) iAppReceiver).onUnbindApp(i, str);
            } else {
                iAppReceiver.onUnbindApp(i);
            }
        }
    }

    @Override // com.taobao.accs.IAppReceiverV2, com.taobao.accs.IAppReceiverV1, com.taobao.accs.IAppReceiver
    public void onBindUser(String str, int i) {
        this.f6451a.onBindUser(str, i);
    }

    @Override // com.taobao.accs.IAppReceiverV2
    public void onBindUser(String str, int i, String str2) {
        IAppReceiver iAppReceiver = this.f6451a;
        if (iAppReceiver instanceof IAppReceiverV2) {
            ((IAppReceiverV2) iAppReceiver).onBindUser(str, i, str2);
        } else {
            iAppReceiver.onBindUser(str, i);
        }
    }

    @Override // com.taobao.accs.IAppReceiverV2, com.taobao.accs.IAppReceiverV1, com.taobao.accs.IAppReceiver
    public void onUnbindUser(int i) {
        this.f6451a.onUnbindUser(i);
    }

    @Override // com.taobao.accs.IAppReceiverV2
    public void onUnbindUser(int i, String str) {
        IAppReceiver iAppReceiver = this.f6451a;
        if (iAppReceiver instanceof IAppReceiverV2) {
            ((IAppReceiverV2) iAppReceiver).onUnbindUser(i, str);
        } else {
            iAppReceiver.onUnbindUser(i);
        }
    }

    @Override // com.taobao.accs.IAppReceiverV1, com.taobao.accs.IAppReceiver
    public void onSendData(String str, int i) {
        this.f6451a.onSendData(str, i);
    }

    @Override // com.taobao.accs.IAppReceiverV1, com.taobao.accs.IAppReceiver
    public String getService(String str) {
        return this.f6451a.getService(str);
    }

    @Override // com.taobao.accs.IAppReceiverV1, com.taobao.accs.IAppReceiver
    public Map<String, String> getAllServices() {
        return this.f6451a.getAllServices();
    }

    public boolean equals(Object obj) {
        if (obj instanceof a) {
            return this.f6451a.equals(((a) obj).f6451a);
        }
        return this.f6451a.equals(obj);
    }

    public int hashCode() {
        return this.f6451a.hashCode();
    }

    public static void a(ErrorCode errorCode, IAppReceiver iAppReceiver, String str) {
        if (iAppReceiver instanceof IAppReceiverV2) {
            ((IAppReceiverV2) iAppReceiver).onBindApp(errorCode.getCodeInt(), errorCode.getMsg(), str);
        } else if (iAppReceiver instanceof IAppReceiverV1) {
            ((IAppReceiverV1) iAppReceiver).onBindApp(errorCode.getCodeInt(), str);
        } else {
            iAppReceiver.onBindApp(errorCode.getCodeInt());
        }
    }

    public static void a(ErrorCode errorCode, IAppReceiver iAppReceiver) {
        if (iAppReceiver instanceof IAppReceiverV2) {
            ((IAppReceiverV2) iAppReceiver).onUnbindApp(errorCode.getCodeInt(), errorCode.getMsg());
        } else {
            iAppReceiver.onUnbindApp(errorCode.getCodeInt());
        }
    }

    public static void b(ErrorCode errorCode, IAppReceiver iAppReceiver, String str) {
        if (iAppReceiver instanceof IAppReceiverV2) {
            ((IAppReceiverV2) iAppReceiver).onBindUser(str, errorCode.getCodeInt(), errorCode.getMsg());
        } else {
            iAppReceiver.onBindUser(str, errorCode.getCodeInt());
        }
    }

    public static void b(ErrorCode errorCode, IAppReceiver iAppReceiver) {
        if (iAppReceiver instanceof IAppReceiverV2) {
            ((IAppReceiverV2) iAppReceiver).onUnbindUser(errorCode.getCodeInt(), errorCode.getMsg());
        } else {
            iAppReceiver.onUnbindUser(errorCode.getCodeInt());
        }
    }
}
