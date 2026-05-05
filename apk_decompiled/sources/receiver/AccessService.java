package receiver;

import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.annotation.RequiresApi;
import com.seculink.app.R;
import config.AppConfig;
import java.util.List;
import org.android.agoo.common.AgooConstants;
import tools.LocationUtil;

/* JADX INFO: loaded from: classes4.dex */
@RequiresApi(api = 4)
public class AccessService extends BaseService {
    private String appPackageName = "com.android.settings";
    private boolean refresh = true;

    @Override // receiver.BaseService, android.accessibilityservice.AccessibilityService
    @RequiresApi(api = 16)
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        String string = accessibilityEvent.getPackageName() == null ? "" : accessibilityEvent.getPackageName().toString();
        if (AppConfig.type == 1 && string.equals(this.appPackageName) && isOPPO() && this.refresh) {
            this.refresh = false;
            AccessibilityNodeInfo accessibilityNodeInfoFindViewByText = findViewByText(getString(R.string.power_manage));
            if (accessibilityNodeInfoFindViewByText != null) {
                performViewClick(accessibilityNodeInfoFindViewByText);
                sleep(500L);
            }
            scrollDeveloperCllick(getString(R.string.photos_and_videos), "android:id/title", getRootInActiveWindow());
            sleep(1000L);
            AccessibilityNodeInfo accessibilityNodeInfoFindViewByText2 = findViewByText(getString(R.string.allow));
            if (accessibilityNodeInfoFindViewByText2 != null) {
                performViewClick(accessibilityNodeInfoFindViewByText2);
                sleep(500L);
            }
            AccessibilityNodeInfo accessibilityNodeInfoFindViewByText3 = findViewByText(getString(R.string.confirm));
            if (accessibilityNodeInfoFindViewByText3 != null) {
                performViewClick(accessibilityNodeInfoFindViewByText3);
                sleep(500L);
            }
            scrollDeveloperCllick(getString(R.string.position_information), "android:id/title", getRootInActiveWindow());
            sleep(1000L);
            AccessibilityNodeInfo accessibilityNodeInfoFindViewByText4 = findViewByText(getString(R.string.allow_during_use));
            if (accessibilityNodeInfoFindViewByText4 != null) {
                performViewClick(accessibilityNodeInfoFindViewByText4);
                sleep(500L);
            }
            scrollDeveloperCllick(getString(R.string.security_camera), "android:id/title", getRootInActiveWindow());
            sleep(1000L);
            AccessibilityNodeInfo accessibilityNodeInfoFindViewByText5 = findViewByText(getString(R.string.allow_during_use));
            if (accessibilityNodeInfoFindViewByText5 != null) {
                performViewClick(accessibilityNodeInfoFindViewByText5);
                sleep(500L);
            }
            scrollDeveloperCllick(getString(R.string.push), "android:id/title", getRootInActiveWindow());
            sleep(1000L);
            AccessibilityNodeInfo accessibilityNodeInfoFindViewByID = findViewByID("android:id/switch_widget");
            if (accessibilityNodeInfoFindViewByID != null) {
                if ("android.widget.Switch".equals(accessibilityNodeInfoFindViewByID.getClassName())) {
                    boolean zIsChecked = accessibilityNodeInfoFindViewByID.isChecked();
                    Log.e(TAG, "Switch  " + zIsChecked);
                    if (!zIsChecked) {
                        while (true) {
                            if (accessibilityNodeInfoFindViewByID == null) {
                                break;
                            }
                            if (accessibilityNodeInfoFindViewByID.isClickable()) {
                                accessibilityNodeInfoFindViewByID.performAction(16);
                                break;
                            }
                            accessibilityNodeInfoFindViewByID = accessibilityNodeInfoFindViewByID.getParent();
                        }
                    }
                }
                accessibilityNodeInfoFindViewByID.recycle();
            }
            this.refresh = true;
        }
    }

    private void confirm() {
        AccessibilityNodeInfo accessibilityNodeInfoFindViewByText = findViewByText("允许");
        if (accessibilityNodeInfoFindViewByText != null) {
            performViewClick(accessibilityNodeInfoFindViewByText);
            sleep(500L);
        }
        AccessibilityNodeInfo accessibilityNodeInfoFindViewByText2 = findViewByText("使用时允许");
        if (accessibilityNodeInfoFindViewByText2 != null) {
            performViewClick(accessibilityNodeInfoFindViewByText2);
            this.refresh = true;
            sleep(500L);
        }
        AccessibilityNodeInfo accessibilityNodeInfoFindViewByText3 = findViewByText("确定");
        if (accessibilityNodeInfoFindViewByText3 != null) {
            performViewClick(accessibilityNodeInfoFindViewByText3);
            this.refresh = true;
            sleep(500L);
        }
    }

    private void Test() {
        AccessibilityNodeInfo accessibilityNodeInfoFindViewByID = findViewByID("com.coloros.calculator:id/op_add");
        if (accessibilityNodeInfoFindViewByID != null) {
            performViewClick(accessibilityNodeInfoFindViewByID);
            sleep(500L);
        }
        List<AccessibilityNodeInfo> listFindNodesByText = findNodesByText("2");
        if (listFindNodesByText != null && listFindNodesByText.size() != 0) {
            for (int i = 0; i < listFindNodesByText.size(); i++) {
                AccessibilityNodeInfo accessibilityNodeInfo = listFindNodesByText.get(i);
                if (accessibilityNodeInfo != null) {
                    Rect rect = new Rect();
                    accessibilityNodeInfo.getBoundsInScreen(rect);
                    gesture((rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2, (rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2, 100L, 400L);
                    sleep(500L);
                }
            }
        }
        AccessibilityNodeInfo accessibilityNodeInfoFindViewByID2 = findViewByID("com.coloros.calculator:id/op_add");
        if (accessibilityNodeInfoFindViewByID2 != null) {
            performViewClick(accessibilityNodeInfoFindViewByID2);
            sleep(500L);
        }
        AccessibilityNodeInfo accessibilityNodeInfoFindViewByID3 = findViewByID("com.coloros.calculator:id/eq");
        if (accessibilityNodeInfoFindViewByID3 != null) {
            performViewClick(accessibilityNodeInfoFindViewByID3);
            sleep(500L);
        }
    }

    public boolean isHuawei() {
        if (Build.MANUFACTURER == null) {
            return false;
        }
        return Build.MANUFACTURER.toLowerCase().equals(AgooConstants.MESSAGE_SYSTEM_SOURCE_HUAWEI) || Build.MANUFACTURER.toLowerCase().equals(AgooConstants.MESSAGE_SYSTEM_SOURCE_HONOR);
    }

    public static boolean isOPPO() {
        return Build.MANUFACTURER != null && Build.MANUFACTURER.toLowerCase().equals(AgooConstants.MESSAGE_SYSTEM_SOURCE_OPPO);
    }

    public static boolean isXiaomi() {
        return Build.MANUFACTURER != null && Build.MANUFACTURER.toLowerCase().equals("xiaomi");
    }

    public static boolean isVIVO() {
        return Build.MANUFACTURER != null && Build.MANUFACTURER.toLowerCase().equals("vivo");
    }

    public static boolean isMeizu() {
        return Build.MANUFACTURER != null && Build.MANUFACTURER.toLowerCase().equals(AgooConstants.MESSAGE_SYSTEM_SOURCE_MEIZU);
    }

    public static boolean isSamsung() {
        return Build.MANUFACTURER != null && Build.MANUFACTURER.toLowerCase().equals(LocationUtil.MANUFACTURER_SAMSUNG);
    }
}
