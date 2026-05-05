package tools;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import bean.AreaCodeModel;
import bean.TimeZoneCodeModel;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.aliyun.iot.aep.sdk.login.data.UserInfo;
import com.github.promeg.pinyinhelper.Pinyin;
import com.seculink.app.R;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* JADX INFO: loaded from: classes4.dex */
public class Utils {
    public static String getUserPhone() {
        UserInfo userInfo = LoginBusiness.getUserInfo();
        if (userInfo == null) {
            return null;
        }
        if (userInfo.userPhone.equals("")) {
            return userInfo.userEmail;
        }
        return userInfo.userPhone;
    }

    public static String getDevSnapKey(String str) {
        UserInfo userInfo = LoginBusiness.getUserInfo();
        if (userInfo == null) {
            return str;
        }
        return userInfo.userPhone + str;
    }

    public static String getDevSnapDir(Context context, String str) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getPackageName() + "/" + getUserPhone() + "/snap/" + str + "/";
    }

    public static String getDevSnapTempDir(Context context, String str) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getPackageName() + "/" + getUserPhone() + "/snap/" + str + "/tempDir/";
    }

    public static List<TimeZoneCodeModel> readTimeZoneList(Context context) {
        String[] stringArray = context.getResources().getStringArray(R.array.time_zone_name);
        ArrayList arrayList = new ArrayList();
        for (String str : stringArray) {
            TimeZoneCodeModel timeZoneCodeModel = new TimeZoneCodeModel();
            String[] strArrSplit = str.split("\\s+");
            timeZoneCodeModel.setCode(strArrSplit[0]);
            timeZoneCodeModel.setName(strArrSplit[1]);
            arrayList.add(timeZoneCodeModel);
        }
        return arrayList;
    }

    public static List<AreaCodeModel> readAreaCodeList(Context context) {
        String[] stringArray = context.getResources().getStringArray(R.array.phone_area_code);
        ArrayList arrayList = new ArrayList();
        for (String str : stringArray) {
            AreaCodeModel areaCodeModel = new AreaCodeModel();
            String[] strArrSplit = str.split("\\s+");
            areaCodeModel.setTel(strArrSplit[2]);
            areaCodeModel.setShortName(strArrSplit[1]);
            areaCodeModel.setName(strArrSplit[0]);
            arrayList.add(areaCodeModel);
        }
        return arrayList;
    }

    public static String getFirstPinYin(String str) {
        if (TextUtils.isEmpty(str) || str.length() <= 0) {
            return MqttTopic.MULTI_LEVEL_WILDCARD;
        }
        if (Pinyin.isChinese(str.charAt(0))) {
            return Pinyin.toPinyin(str.charAt(0)).substring(0, 1);
        }
        return str.substring(0, 1).matches("^[a-zA-Z]*") ? str.substring(0, 1) : MqttTopic.MULTI_LEVEL_WILDCARD;
    }

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isOwner(String str) {
        String deviceOwner = SharePreferenceManager.getInstance().getDeviceOwner(str);
        String userPhone = getUserPhone();
        return !TextUtils.isEmpty(userPhone) && userPhone.equals(deviceOwner);
    }

    public static int getCurrentTimeZone() {
        String displayName = TimeZone.getDefault().getDisplayName(false, 0);
        if ("GMT".equalsIgnoreCase(displayName)) {
            return 0;
        }
        return Integer.parseInt(displayName.replace("GMT", "").split(":")[0]);
    }

    public static String getAreaCode(Context context) {
        String simCountryIso = ((TelephonyManager) context.getSystemService("phone")).getSimCountryIso();
        if (TextUtils.isEmpty(simCountryIso)) {
            simCountryIso = context.getResources().getConfiguration().locale.getCountry();
        }
        for (AreaCodeModel areaCodeModel : readAreaCodeList(context)) {
            if (areaCodeModel.getShortName().equalsIgnoreCase(simCountryIso)) {
                return areaCodeModel.getTel();
            }
        }
        return "86";
    }
}
