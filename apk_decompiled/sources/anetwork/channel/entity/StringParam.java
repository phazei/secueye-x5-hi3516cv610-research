package anetwork.channel.entity;

import anetwork.channel.Param;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class StringParam implements Param {
    private String key;
    private String value;

    @Override // anetwork.channel.Param
    public String getKey() {
        return this.key;
    }

    @Override // anetwork.channel.Param
    public String getValue() {
        return this.value;
    }

    public StringParam(String str, String str2) {
        this.key = str;
        this.value = str2;
    }
}
