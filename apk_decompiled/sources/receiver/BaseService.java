package receiver;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.annotation.RequiresApi;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: loaded from: classes4.dex */
@RequiresApi(api = 4)
public class BaseService extends AccessibilityService {
    public static String TAG = "自动化";
    private static BaseService mInstance;
    private boolean isOpen = false;
    private AccessibilityManager mAccessibilityManager;
    private Context mContext;

    @Override // android.accessibilityservice.AccessibilityService
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
    }

    @Override // android.accessibilityservice.AccessibilityService
    public void onInterrupt() {
    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
        this.mAccessibilityManager = (AccessibilityManager) this.mContext.getSystemService("accessibility");
    }

    public static BaseService getInstance() {
        if (mInstance == null) {
            mInstance = new BaseService();
        }
        return mInstance;
    }

    public boolean checkAccessibilityEnabled(Context context, String str) {
        List<ActivityManager.RunningServiceInfo> runningServices = ((ActivityManager) context.getSystemService(AgooConstants.OPEN_ACTIIVTY_NAME)).getRunningServices(100);
        if (runningServices.size() < 0) {
            return false;
        }
        for (int i = 0; i < runningServices.size(); i++) {
            if (runningServices.get(i).service.getClassName().contains(str)) {
                return true;
            }
        }
        return false;
    }

    public void goAccess() {
        Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
        intent.setFlags(268435456);
        this.mContext.startActivity(intent);
    }

    public static void toSelfSetting(Context context) {
        Intent intent = new Intent();
        intent.addFlags(268435456);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction("android.intent.action.VIEW");
            intent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }

    public void performViewClick(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo == null) {
            return;
        }
        while (accessibilityNodeInfo != null) {
            if (accessibilityNodeInfo.isClickable()) {
                accessibilityNodeInfo.performAction(16);
                return;
            }
            accessibilityNodeInfo = accessibilityNodeInfo.getParent();
        }
    }

    public void performBackClick() {
        performGlobalAction(1);
    }

    public void performScrollBackward() {
        performGlobalAction(8192);
    }

    public void performScrollForward() {
        performGlobalAction(4096);
    }

    public AccessibilityNodeInfo findViewByText(String str) {
        AccessibilityNodeInfo accessibilityNodeInfoFindViewByText = findViewByText(str, true);
        return accessibilityNodeInfoFindViewByText == null ? findViewByText(str, false) : accessibilityNodeInfoFindViewByText;
    }

    public AccessibilityNodeInfo findViewByText(String str, boolean z) {
        List<AccessibilityNodeInfo> listFindAccessibilityNodeInfosByText;
        AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
        if (rootInActiveWindow != null && (listFindAccessibilityNodeInfosByText = rootInActiveWindow.findAccessibilityNodeInfosByText(str)) != null && !listFindAccessibilityNodeInfosByText.isEmpty()) {
            for (AccessibilityNodeInfo accessibilityNodeInfo : listFindAccessibilityNodeInfosByText) {
                if (accessibilityNodeInfo != null && accessibilityNodeInfo.isClickable() == z) {
                    return accessibilityNodeInfo;
                }
            }
        }
        return null;
    }

    public AccessibilityNodeInfo findViewByID(String str) {
        List<AccessibilityNodeInfo> listFindAccessibilityNodeInfosByViewId;
        AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
        if (rootInActiveWindow != null && (listFindAccessibilityNodeInfosByViewId = rootInActiveWindow.findAccessibilityNodeInfosByViewId(str)) != null && !listFindAccessibilityNodeInfosByViewId.isEmpty()) {
            Log.d("dd", "findViewByID: " + listFindAccessibilityNodeInfosByViewId.size());
            for (AccessibilityNodeInfo accessibilityNodeInfo : listFindAccessibilityNodeInfosByViewId) {
                if (accessibilityNodeInfo != null) {
                    Log.d("dd", "findViewByID: " + accessibilityNodeInfo.toString());
                    return accessibilityNodeInfo;
                }
            }
        }
        return null;
    }

    public void clickViewByText(String str) {
        List<AccessibilityNodeInfo> listFindAccessibilityNodeInfosByText;
        AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
        if (rootInActiveWindow == null || (listFindAccessibilityNodeInfosByText = rootInActiveWindow.findAccessibilityNodeInfosByText(str)) == null || listFindAccessibilityNodeInfosByText.isEmpty()) {
            return;
        }
        for (AccessibilityNodeInfo accessibilityNodeInfo : listFindAccessibilityNodeInfosByText) {
            if (accessibilityNodeInfo != null) {
                performViewClick(accessibilityNodeInfo);
                return;
            }
        }
    }

    public void clickViewByID(String str) {
        List<AccessibilityNodeInfo> listFindAccessibilityNodeInfosByViewId;
        AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
        if (rootInActiveWindow == null || (listFindAccessibilityNodeInfosByViewId = rootInActiveWindow.findAccessibilityNodeInfosByViewId(str)) == null || listFindAccessibilityNodeInfosByViewId.isEmpty()) {
            return;
        }
        for (AccessibilityNodeInfo accessibilityNodeInfo : listFindAccessibilityNodeInfosByViewId) {
            if (accessibilityNodeInfo != null) {
                performViewClick(accessibilityNodeInfo);
                return;
            }
        }
    }

    public void clickNodesByText(String str, AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo == null) {
            return;
        }
        int childCount = accessibilityNodeInfo.getChildCount();
        if (childCount != 0) {
            for (int i = 0; i < childCount; i++) {
                clickNodesByText(str, accessibilityNodeInfo.getChild(i));
            }
            return;
        }
        if (accessibilityNodeInfo.getText() != null && str.equals(accessibilityNodeInfo.getText().toString())) {
            Rect rect = new Rect();
            accessibilityNodeInfo.getBoundsInScreen(rect);
            gesture((rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2, (rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2, 100L, 400L);
        }
    }

    public List<AccessibilityNodeInfo> findNodesByText(String str) {
        ArrayList arrayList = new ArrayList();
        Stack stack = new Stack();
        stack.add(getRootInActiveWindow());
        while (!stack.isEmpty()) {
            AccessibilityNodeInfo accessibilityNodeInfo = (AccessibilityNodeInfo) stack.pop();
            if (accessibilityNodeInfo != null && accessibilityNodeInfo.getText() != null && accessibilityNodeInfo.getText().toString().equals(str)) {
                arrayList.add(accessibilityNodeInfo);
            }
            if (accessibilityNodeInfo != null && accessibilityNodeInfo.getChildCount() != 0) {
                int childCount = accessibilityNodeInfo.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(i);
                    if (child != null) {
                        stack.push(child);
                    }
                }
            }
        }
        if (arrayList.size() > 0) {
            return arrayList;
        }
        return null;
    }

    public void inputText(AccessibilityNodeInfo accessibilityNodeInfo, String str) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, str);
        accessibilityNodeInfo.performAction(2097152, bundle);
    }

    public void gesture(int i, int i2, int i3, int i4, long j, long j2) {
        if (i < 0 || i2 < 0 || i3 < 0 || i4 < 0) {
            Log.e("path", "path nagative");
            return;
        }
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();
        path.moveTo(i, i2);
        path.lineTo(i3, i4);
        dispatchGesture(builder.addStroke(new GestureDescription.StrokeDescription(path, j, j2, false)).build(), new AccessibilityService.GestureResultCallback() { // from class: receiver.BaseService.1
            @Override // android.accessibilityservice.AccessibilityService.GestureResultCallback
            public void onCancelled(GestureDescription gestureDescription) {
            }

            @Override // android.accessibilityservice.AccessibilityService.GestureResultCallback
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
            }
        }, new Handler(Looper.getMainLooper()));
    }

    @RequiresApi(api = 14)
    public void scrollDeveloperCllick(String str, String str2, AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo != null) {
            List<AccessibilityNodeInfo> listFindAccessibilityNodeInfosByText = accessibilityNodeInfo.findAccessibilityNodeInfosByText(str);
            List<AccessibilityNodeInfo> listFindAccessibilityNodeInfosByViewId = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(str2);
            if (listFindAccessibilityNodeInfosByText == null || listFindAccessibilityNodeInfosByText.size() == 0) {
                Log.e(TAG, "不存在 " + str);
                if (listFindAccessibilityNodeInfosByViewId == null || listFindAccessibilityNodeInfosByViewId.size() <= 0) {
                    return;
                }
                Log.e(TAG, "---- [ " + str + " ] 滚动查找中 ----" + listFindAccessibilityNodeInfosByViewId.size());
                AccessibilityNodeInfo parent = listFindAccessibilityNodeInfosByViewId.get(0).getParent().getParent();
                if (parent != null) {
                    Log.e(TAG, "----  滚动 ----" + listFindAccessibilityNodeInfosByViewId.get(0).getParent().getParent().isScrollable());
                    if (listFindAccessibilityNodeInfosByViewId.size() <= 0 || !parent.performAction(4096)) {
                        return;
                    }
                    scrollDeveloperCllick(str, str2, accessibilityNodeInfo);
                    this.isOpen = true;
                    return;
                }
                return;
            }
            Log.e(TAG, "ACTION_CLICK text1=,item=" + listFindAccessibilityNodeInfosByText.size());
            if (listFindAccessibilityNodeInfosByViewId != null) {
                for (int i = 0; i < listFindAccessibilityNodeInfosByViewId.size(); i++) {
                    if (listFindAccessibilityNodeInfosByViewId.get(i).getParent().getChild(0).getText().toString().equals(str)) {
                        AccessibilityNodeInfo parent2 = listFindAccessibilityNodeInfosByViewId.get(i);
                        while (true) {
                            if (parent2 == null) {
                                break;
                            }
                            if (parent2.isClickable()) {
                                parent2.performAction(16);
                                break;
                            }
                            parent2 = parent2.getParent();
                        }
                        this.isOpen = false;
                        return;
                    }
                    if (i == listFindAccessibilityNodeInfosByViewId.size()) {
                        AccessibilityNodeInfo parent3 = listFindAccessibilityNodeInfosByViewId.get(i);
                        while (true) {
                            if (parent3 == null) {
                                break;
                            }
                            if (parent3.isScrollable()) {
                                parent3.performAction(4096);
                                scrollDeveloperCllick(str, str2, accessibilityNodeInfo);
                                break;
                            }
                            parent3 = parent3.getParent();
                        }
                    }
                }
            }
        }
    }

    protected void sleep(long j) {
        try {
            Thread.sleep(j);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // android.accessibilityservice.AccessibilityService
    protected void onServiceConnected() {
        super.onServiceConnected();
    }
}
