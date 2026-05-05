package com.aliyun.iot.aep.sdk.page;

import com.alibaba.sdk.android.openaccount.ui.model.CountrySort;
import com.alibaba.sdk.android.openaccount.ui.util.CharacterParserUtil;
import com.alibaba.sdk.android.openaccount.ui.util.LocaleUtils;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* JADX INFO: loaded from: classes2.dex */
public class GetCountryNameSort {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    CharacterParserUtil f4835a = CharacterParserUtil.getInstance();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    String f4836b = "[\\u4E00-\\u9FA5]+";

    public String getSortLetter(String str) {
        if (str == null) {
            return MqttTopic.MULTI_LEVEL_WILDCARD;
        }
        String upperCase = this.f4835a.getSelling(str).substring(0, 1).toUpperCase(Locale.CHINESE);
        return upperCase.matches("[A-Z]") ? upperCase.toUpperCase(Locale.CHINESE) : MqttTopic.MULTI_LEVEL_WILDCARD;
    }

    public String getSortLetterBySortKey(String str) {
        if (str == null || "".equals(str.trim())) {
            return null;
        }
        String upperCase = str.trim().substring(0, 1).toUpperCase(Locale.CHINESE);
        return upperCase.matches("[A-Z]") ? upperCase.toUpperCase(Locale.CHINESE) : MqttTopic.MULTI_LEVEL_WILDCARD;
    }

    public List<CountrySort> search(String str, List<CountrySort> list) {
        if (list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        if (str.matches("^([0-9]|[/+]).*")) {
            String strReplaceAll = str.replaceAll("\\-|\\s", "");
            for (CountrySort countrySort : list) {
                if (countrySort.name != null && countrySort.code != null && (countrySort.code.contains(strReplaceAll) || countrySort.displayName.contains(str))) {
                    if (!WebSocketServerHandshaker.SUB_PROTOCOL_WILDCARD.equals(countrySort.sortLetters)) {
                        arrayList.add(countrySort);
                    }
                }
            }
            return arrayList;
        }
        String currentLocale = LocaleUtils.getCurrentLocale();
        for (CountrySort countrySort2 : list) {
            if (countrySort2.code != null && countrySort2.name != null) {
                String lowerCase = str.toLowerCase(Locale.CHINESE);
                if ((countrySort2.name.toLowerCase(Locale.CHINESE).contains(lowerCase) || countrySort2.sortWeightKey.toLowerCase(Locale.CHINESE).replace(" ", "").contains(lowerCase) || countrySort2.englishName.toLowerCase(Locale.CHINESE).contains(lowerCase) || countrySort2.domain.toLowerCase(Locale.CHINESE).contains(lowerCase)) && !WebSocketServerHandshaker.SUB_PROTOCOL_WILDCARD.equals(countrySort2.sortLetters)) {
                    arrayList.add(countrySort2);
                }
                if (LocaleUtils.isZHLocale(currentLocale) && countrySort2.pinyin.toLowerCase(Locale.CHINESE).contains(lowerCase)) {
                    arrayList.add(countrySort2);
                }
            }
        }
        return arrayList;
    }
}
