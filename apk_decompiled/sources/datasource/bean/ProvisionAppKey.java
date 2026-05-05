package datasource.bean;

import a.a.a.a.b.d.a;

/* JADX INFO: loaded from: classes3.dex */
public class ProvisionAppKey implements Comparable<ProvisionAppKey> {
    public String appKey;
    public int appKeyIndex;

    public String getAppKey() {
        return this.appKey;
    }

    public int getAppKeyIndex() {
        return this.appKeyIndex;
    }

    public void setAppKey(String str) {
        this.appKey = str;
    }

    public void setAppKeyIndex(int i) {
        this.appKeyIndex = i;
    }

    @Override // java.lang.Comparable
    public int compareTo(ProvisionAppKey provisionAppKey) {
        return a.f1316b ? -(this.appKeyIndex - provisionAppKey.appKeyIndex) : this.appKeyIndex - provisionAppKey.appKeyIndex;
    }
}
