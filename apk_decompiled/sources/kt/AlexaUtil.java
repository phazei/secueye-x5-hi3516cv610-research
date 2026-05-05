package kt;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: AlexaUtil.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001:\u0001\u0007B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0002¨\u0006\b"}, d2 = {"Lkt/AlexaUtil;", "", "()V", "getAppToAppIntent", "Landroid/content/Intent;", "appToAppUrl", "", "AlexaAppUtil", "secueye_googleRelease"}, k = 1, mv = {1, 1, 15})
public final class AlexaUtil {
    private final Intent getAppToAppIntent(String appToAppUrl) {
        return new Intent("android.intent.action.VIEW", Uri.parse(appToAppUrl));
    }

    /* JADX INFO: compiled from: AlexaUtil.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lkt/AlexaUtil$AlexaAppUtil;", "", "()V", "ALEXA_APP_TARGET_ACTIVITY_NAME", "", "ALEXA_PACKAGE_NAME", "REQUIRED_MINIMUM_VERSION_CODE", "", "doesAlexaAppSupportAppToApp", "", "context", "Landroid/content/Context;", "secueye_googleRelease"}, k = 1, mv = {1, 1, 15})
    public static final class AlexaAppUtil {
        private static final String ALEXA_APP_TARGET_ACTIVITY_NAME = "com.amazon.dee.app.ui.main.MainActivity";
        private static final String ALEXA_PACKAGE_NAME = "com.amazon.dee.app";
        public static final AlexaAppUtil INSTANCE = new AlexaAppUtil();
        private static final int REQUIRED_MINIMUM_VERSION_CODE = 866607211;

        private AlexaAppUtil() {
        }

        @JvmStatic
        public static final boolean doesAlexaAppSupportAppToApp(@NotNull Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            try {
                PackageManager packageManager = context.getPackageManager();
                Intrinsics.checkExpressionValueIsNotNull(packageManager, "context.packageManager");
                PackageInfo packageInfo = packageManager.getPackageInfo(ALEXA_PACKAGE_NAME, 0);
                if (Build.VERSION.SDK_INT < 28) {
                    return packageInfo != null;
                }
                Intrinsics.checkExpressionValueIsNotNull(packageInfo, "packageInfo");
                return packageInfo.getLongVersionCode() > ((long) REQUIRED_MINIMUM_VERSION_CODE);
            } catch (PackageManager.NameNotFoundException unused) {
                return false;
            }
        }
    }
}
