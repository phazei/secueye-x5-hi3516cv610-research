package com.taobao.agoo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.alibaba.sdk.android.push.PushInitStatus;
import com.taobao.accs.utl.ALog;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes3.dex */
public class BaseNotifyClickActivity extends Activity implements PushInitStatus.IPushInitListener {
    private static final String TAG = "Naccs.BaseNotifyClickActivity";
    public static Set<INotifyListener> notifyListeners;
    private BaseNotifyClick baseNotifyClick = new BaseNotifyClick() { // from class: com.taobao.agoo.BaseNotifyClickActivity.1
        @Override // com.taobao.agoo.BaseNotifyClick
        public void onMessage(Intent intent) {
            BaseNotifyClickActivity.this.onMessage(intent);
        }
    };

    public interface INotifyListener {
        String getMsgSource();

        String parseMsgFromIntent(Intent intent);
    }

    public void onMessage(Intent intent) {
    }

    public static void addNotifyListener(INotifyListener iNotifyListener) {
        if (notifyListeners == null) {
            notifyListeners = new HashSet();
        }
        notifyListeners.add(iNotifyListener);
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ALog.i(TAG, "onCreate", new Object[0]);
        if (!PushInitStatus.getInstance().isInitPush) {
            PushInitStatus.getInstance().listener = this;
        }
        this.baseNotifyClick.onCreate(this, getIntent());
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ALog.i(TAG, "onNewIntent", new Object[0]);
        this.baseNotifyClick.onNewIntent(intent);
    }

    @Override // com.alibaba.sdk.android.push.PushInitStatus.IPushInitListener
    public void onInitPush(boolean z) {
        ALog.i(TAG, "onInitPush init push success isInit = " + z, new Object[0]);
        PushInitStatus.getInstance().isInitPush = z;
        if (z) {
            this.baseNotifyClick.onInitPush();
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        if (PushInitStatus.getInstance().listener != null) {
            PushInitStatus.getInstance().listener = null;
        }
    }
}
