package datasource.bean;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class ConfigResultMap {
    public AddAppKey addAppKey;
    public List<SigmeshKey> sigmeshKeys;
    public List<AddPublish> configModelPublication = null;
    public List<BindModel> bindModel = null;

    @JSONField(alternateNames = {"configModelSubscription", "subscribeGroupAddr"})
    public List<SubscribeGroupAddr> subscribeGroupAddr = null;
    public List<SubscribeGroupAddr> configModelSubscription = null;

    public AddAppKey getAddAppKey() {
        return this.addAppKey;
    }

    public List<BindModel> getBindModel() {
        return this.bindModel;
    }

    public List<AddPublish> getConfigModelPublication() {
        return this.configModelPublication;
    }

    public List<SubscribeGroupAddr> getConfigModelSubscription() {
        return this.configModelSubscription;
    }

    public List<SigmeshKey> getSigmeshKeys() {
        return this.sigmeshKeys;
    }

    public List<SubscribeGroupAddr> getSubscribeGroupAddr() {
        return this.subscribeGroupAddr;
    }

    public void setAddAppKey(AddAppKey addAppKey) {
        this.addAppKey = addAppKey;
    }

    public void setBindModel(List<BindModel> list) {
        this.bindModel = list;
    }

    public void setConfigModelPublication(List<AddPublish> list) {
        this.configModelPublication = list;
    }

    public void setConfigModelSubscription(List<SubscribeGroupAddr> list) {
        this.configModelSubscription = list;
    }

    public void setSigmeshKeys(List<SigmeshKey> list) {
        this.sigmeshKeys = list;
    }

    public void setSubscribeGroupAddr(List<SubscribeGroupAddr> list) {
        this.subscribeGroupAddr = list;
    }
}
