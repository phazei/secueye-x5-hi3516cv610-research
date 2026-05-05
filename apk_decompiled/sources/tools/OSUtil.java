package tools;

import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/* JADX INFO: loaded from: classes4.dex */
public class OSUtil {
    private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
    private static final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";
    private static final String KEY_EMUI_VERSION_CODE = "ro.build.version.emui";
    private static final String KEY_FLYME_ICON_FALG = "persist.sys.use.flyme.icon";
    private static final String KEY_FLYME_ID_FALG_KEY = "ro.build.display.id";
    private static final String KEY_FLYME_ID_FALG_VALUE_KEYWORD = "Flyme";
    private static final String KEY_FLYME_PUBLISH_FALG = "ro.flyme.published";
    private static final String KEY_FLYME_SETUP_FALG = "ro.meizu.setupwizard.flyme";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";

    public static boolean isFlyme() {
        if (isPropertiesExist(KEY_FLYME_ICON_FALG, KEY_FLYME_SETUP_FALG, KEY_FLYME_PUBLISH_FALG)) {
            return true;
        }
        try {
            BuildProperties buildPropertiesNewInstance = BuildProperties.newInstance();
            if (!buildPropertiesNewInstance.containsKey(KEY_FLYME_ID_FALG_KEY)) {
                return false;
            }
            String property = buildPropertiesNewInstance.getProperty(KEY_FLYME_ID_FALG_KEY);
            if (TextUtils.isEmpty(property)) {
                return false;
            }
            return property.contains(KEY_FLYME_ID_FALG_VALUE_KEYWORD);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isEMUI() {
        return isPropertiesExist(KEY_EMUI_VERSION_CODE, KEY_EMUI_API_LEVEL, KEY_EMUI_CONFIG_HW_SYS_VERSION);
    }

    public static boolean isMIUI() {
        return isPropertiesExist(KEY_MIUI_VERSION_CODE, KEY_MIUI_VERSION_NAME, KEY_MIUI_INTERNAL_STORAGE);
    }

    private static boolean isPropertiesExist(String... strArr) {
        if (strArr == null || strArr.length == 0) {
            return false;
        }
        try {
            BuildProperties buildPropertiesNewInstance = BuildProperties.newInstance();
            for (String str : strArr) {
                if (buildPropertiesNewInstance.getProperty(str) != null) {
                    return true;
                }
            }
            return false;
        } catch (IOException unused) {
            return false;
        }
    }

    private static final class BuildProperties {
        private final Properties properties = new Properties();

        private BuildProperties() throws IOException {
            this.properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        }

        public boolean containsKey(Object obj) {
            return this.properties.containsKey(obj);
        }

        public boolean containsValue(Object obj) {
            return this.properties.containsValue(obj);
        }

        public Set<Map.Entry<Object, Object>> entrySet() {
            return this.properties.entrySet();
        }

        public String getProperty(String str) {
            return this.properties.getProperty(str);
        }

        public String getProperty(String str, String str2) {
            return this.properties.getProperty(str, str2);
        }

        public boolean isEmpty() {
            return this.properties.isEmpty();
        }

        public Enumeration<Object> keys() {
            return this.properties.keys();
        }

        public Set<Object> keySet() {
            return this.properties.keySet();
        }

        public int size() {
            return this.properties.size();
        }

        public Collection<Object> values() {
            return this.properties.values();
        }

        public static BuildProperties newInstance() throws IOException {
            return new BuildProperties();
        }
    }

    class AvailableRomType {
        public static final int ANDROID_NATIVE = 3;
        public static final int FLYME = 2;
        public static final int MIUI = 1;
        public static final int NA = 4;

        AvailableRomType() {
        }
    }

    public static int getLightStatusBarAvailableRomType() {
        if (isMiUIV7OrAbove()) {
            return 3;
        }
        if (isMiUIV6OrAbove()) {
            return 1;
        }
        if (isFlymeV4OrAbove()) {
            return 2;
        }
        return isAndroidMOrAbove() ? 3 : 4;
    }

    private static boolean isFlymeV4OrAbove() {
        String str = Build.DISPLAY;
        if (!TextUtils.isEmpty(str) && str.contains(KEY_FLYME_ID_FALG_VALUE_KEYWORD)) {
            for (String str2 : str.split(" ")) {
                if (str2.matches("^[4-9]\\.(\\d+\\.)+\\S*")) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isAndroidMOrAbove() {
        return Build.VERSION.SDK_INT >= 23;
    }

    private static boolean isMiUIV6OrAbove() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            String property = properties.getProperty(KEY_MIUI_VERSION_CODE, null);
            if (property != null) {
                return Integer.parseInt(property) >= 4;
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean isMiUIV7OrAbove() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            String property = properties.getProperty(KEY_MIUI_VERSION_CODE, null);
            if (property != null) {
                return Integer.parseInt(property) >= 5;
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }
}
