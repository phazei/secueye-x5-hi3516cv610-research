package datasource.implemention.response;

import datasource.implemention.data.ConfigurationRespData;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes3.dex */
public class ConfigurationResp extends BaseOutDo {
    public ConfigurationRespData data;

    public void setData(ConfigurationRespData configurationRespData) {
        this.data = configurationRespData;
    }

    /* JADX INFO: renamed from: getData, reason: merged with bridge method [inline-methods] */
    public ConfigurationRespData m783getData() {
        return this.data;
    }
}
