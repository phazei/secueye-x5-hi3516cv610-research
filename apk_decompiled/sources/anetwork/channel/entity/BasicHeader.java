package anetwork.channel.entity;

import anetwork.channel.Header;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class BasicHeader implements Header {
    private final String name;
    private final String value;

    public BasicHeader(String str, String str2) {
        if (str == null) {
            throw new IllegalArgumentException("Name may not be null");
        }
        this.name = str;
        this.value = str2;
    }

    @Override // anetwork.channel.Header
    public String getName() {
        return this.name;
    }

    @Override // anetwork.channel.Header
    public String getValue() {
        return this.value;
    }
}
