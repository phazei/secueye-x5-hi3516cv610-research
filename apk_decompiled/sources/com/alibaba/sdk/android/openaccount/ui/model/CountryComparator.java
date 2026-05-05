package com.alibaba.sdk.android.openaccount.ui.model;

import java.util.Comparator;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* JADX INFO: loaded from: classes.dex */
public class CountryComparator implements Comparator<CountrySort> {
    @Override // java.util.Comparator
    public int compare(CountrySort countrySort, CountrySort countrySort2) {
        if (countrySort.sortLetters.equals("@") || countrySort2.sortLetters.equals(MqttTopic.MULTI_LEVEL_WILDCARD)) {
            return -1;
        }
        if (countrySort.sortLetters.equals(MqttTopic.MULTI_LEVEL_WILDCARD) || countrySort2.sortLetters.equals("@")) {
            return 1;
        }
        return countrySort.sortLetters.compareTo(countrySort2.sortLetters);
    }
}
