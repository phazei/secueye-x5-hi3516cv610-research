package datasource.bean;

import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class BindModel {
    public Integer appKeyIndex;
    public Integer modelElementAddr;
    public List<Integer> modelIds = null;
    public Integer ttl;

    public Integer getAppKeyIndex() {
        return this.appKeyIndex;
    }

    public Integer getModelElementAddr() {
        return this.modelElementAddr;
    }

    public List<Integer> getModelIds() {
        return this.modelIds;
    }

    public Integer getTtl() {
        return this.ttl;
    }

    public void setAppKeyIndex(Integer num) {
        this.appKeyIndex = num;
    }

    public void setModelElementAddr(Integer num) {
        this.modelElementAddr = num;
    }

    public void setModelIds(List<Integer> list) {
        this.modelIds = list;
    }

    public void setTtl(Integer num) {
        this.ttl = num;
    }
}
