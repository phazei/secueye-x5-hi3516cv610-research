package datasource.bean;

import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class ProvisionInfo {
    public Integer primaryUnicastAddress;
    public List<Integer> netKeyIndexes = null;
    public List<Integer> appKeyIndexes = null;
    public List<String> netKeys = null;
    public List<String> appKeys = null;
    public String serverConfirmation = null;

    public List<Integer> getAppKeyIndexes() {
        return this.appKeyIndexes;
    }

    public List<String> getAppKeys() {
        return this.appKeys;
    }

    public List<Integer> getNetKeyIndexes() {
        return this.netKeyIndexes;
    }

    public List<String> getNetKeys() {
        return this.netKeys;
    }

    public Integer getPrimaryUnicastAddress() {
        return this.primaryUnicastAddress;
    }

    public String getServerConfirmation() {
        return this.serverConfirmation;
    }

    public void setAppKeyIndexes(List<Integer> list) {
        this.appKeyIndexes = list;
    }

    public void setAppKeys(List<String> list) {
        this.appKeys = list;
    }

    public void setNetKeyIndexes(List<Integer> list) {
        this.netKeyIndexes = list;
    }

    public void setNetKeys(List<String> list) {
        this.netKeys = list;
    }

    public void setPrimaryUnicastAddress(Integer num) {
        this.primaryUnicastAddress = num;
    }

    public void setServerConfirmation(String str) {
        this.serverConfirmation = str;
    }
}
