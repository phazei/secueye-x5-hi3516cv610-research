package config;

import bean.DeviceInfoBean;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class AppConfig {
    public static long INACTIVITY_DELAY = 600000;
    public static long LOW_POWER = 8000;
    public static int MODEL_1 = 1;
    public static final int PTZ_CONTROL_TIME = 100;
    public static int PushInterval = 60;
    public static boolean isChina = false;
    public static boolean isInternal = false;
    public static boolean isRefresh = false;
    public static int type = -1;
    public static int MODEL_0 = 0;
    public static int DEVICES_MODEL = MODEL_0;
    public static String AD_APP_ID = "51375";
    public static String WX_APP_ID = "wx2053683b8d4f3694";
    public static String WX_SECRET = "wx2053683b8d4f3694";
    public static long time = 0;
    public static List<DeviceInfoBean> LOWPOWER_LIST = new ArrayList();
    public static boolean isInitSDK = false;
}
