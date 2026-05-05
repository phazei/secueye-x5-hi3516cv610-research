package com.alibaba.sdk.android.openaccount.ui.model;

import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

/* JADX INFO: loaded from: classes.dex */
public class CountrySort extends Country {
    private static final long serialVersionUID = 3674828616454899037L;
    public String displayName;
    public String sortLetters;

    public static CountrySort getCountryHot(CountrySort countrySort) {
        CountrySort countrySort2 = new CountrySort();
        countrySort2.displayName = countrySort.displayName;
        countrySort2.name = countrySort.name;
        countrySort2.code = countrySort.code;
        countrySort2.englishName = countrySort.englishName;
        countrySort2.tranditionalName = countrySort.tranditionalName;
        countrySort2.pinyin = countrySort.pinyin;
        countrySort2.domain = countrySort.domain;
        countrySort2.checkKey = countrySort.checkKey;
        countrySort2.sortWeightKey = countrySort.sortWeightKey;
        countrySort2.version = countrySort.version;
        countrySort2.sortLetters = WebSocketServerHandshaker.SUB_PROTOCOL_WILDCARD;
        return countrySort2;
    }
}
