package tools;

import activity.CountryListActivity;
import android.content.Context;
import android.util.Log;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.config.LanguageCode;
import com.aliyun.iot.aep.oa.OALanguageHelper;
import com.aliyun.iot.aep.sdk.IoTSmart;
import com.seculink.app.R;
import java.util.Locale;

/* JADX INFO: loaded from: classes4.dex */
public class LanguageUtil {
    private static Locale locale;

    public static String getRealLanguage(Context context, String str) {
        for (String str2 : context.getResources().getStringArray(R.array.languages)) {
            if (str2.equalsIgnoreCase(str)) {
                return str2;
            }
        }
        return "en-US";
    }

    public static Context attachBaseContext(Context context) throws Exception {
        setLanguage(context);
        return context;
    }

    private static void setLanguage(Context context) throws Exception {
        String language = IoTSmart.getLanguage();
        Log.d("TAG", "setLanguage: puppet:language====" + language);
        Log.d(CountryListActivity.TAG, "setLanguage: " + language);
        Log.d(CountryListActivity.TAG, "setLanguage: " + locale.getLanguage());
        if (language.equals("zh-CN")) {
            ConfigManager.getInstance().setLanguageCode(LanguageCode.CHINESE);
        } else {
            ConfigManager.getInstance().setLanguageCode(LanguageCode.ENGLISH);
        }
        if (locale.getLanguage().contains("zh")) {
            OALanguageHelper.setLanguageCode(Locale.SIMPLIFIED_CHINESE);
            return;
        }
        if (locale.getLanguage().contains("fr")) {
            OALanguageHelper.setLanguageCode(Locale.FRANCE);
            return;
        }
        if (locale.getLanguage().contains("de")) {
            OALanguageHelper.setLanguageCode(Locale.GERMANY);
            return;
        }
        if (locale.getLanguage().contains("ja")) {
            OALanguageHelper.setLanguageCode(Locale.JAPAN);
            return;
        }
        if (locale.getLanguage().contains("ko")) {
            OALanguageHelper.setLanguageCode(Locale.KOREA);
            return;
        }
        if (locale.getLanguage().contains("pt")) {
            OALanguageHelper.setLanguageCode(new Locale("pt", "PT"));
            return;
        }
        if (locale.getLanguage().contains("pt_BR")) {
            OALanguageHelper.setLanguageCode(new Locale("pt", "BR"));
            return;
        }
        if (locale.getLanguage().contains("th")) {
            OALanguageHelper.setLanguageCode(new Locale("th", "TH"));
            return;
        }
        if (locale.getLanguage().contains("es")) {
            OALanguageHelper.setLanguageCode(new Locale("es", "ES"));
            return;
        }
        if (locale.getLanguage().contains("ru")) {
            OALanguageHelper.setLanguageCode(new Locale("ru", "RU"));
        } else if (locale.getLanguage().contains("vi")) {
            OALanguageHelper.setLanguageCode(new Locale("vi", "VI"));
        } else {
            OALanguageHelper.setLanguageCode(Locale.US);
        }
    }

    public static void setLocale(Locale locale2) {
        locale = locale2;
    }
}
