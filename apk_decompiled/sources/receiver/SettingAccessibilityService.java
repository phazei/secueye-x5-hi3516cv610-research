package receiver;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.annotation.RequiresApi;

/* JADX INFO: loaded from: classes4.dex */
@RequiresApi(api = 4)
public class SettingAccessibilityService extends AccessibilityService {
    @Override // android.accessibilityservice.AccessibilityService
    public void onInterrupt() {
    }

    @Override // android.accessibilityservice.AccessibilityService
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override // android.accessibilityservice.AccessibilityService
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Log.e("无障碍监听", "" + accessibilityEvent.toString());
        Log.e("无障碍文本", "" + accessibilityEvent.getText());
        if (accessibilityEvent.getEventType() == 32 && accessibilityEvent.getPackageName().equals("com.android.packageinstaller")) {
            Log.e("无障碍监听", "监听到安装服务");
        }
    }

    @RequiresApi(api = 14)
    private boolean performClickNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo == null) {
            return false;
        }
        if (accessibilityNodeInfo.isClickable()) {
            accessibilityNodeInfo.performAction(16);
            return true;
        }
        AccessibilityNodeInfo parent = accessibilityNodeInfo.getParent();
        if (parent == null) {
            return false;
        }
        boolean zPerformClickNodeInfo = performClickNodeInfo(parent);
        parent.recycle();
        return zPerformClickNodeInfo;
    }
}
