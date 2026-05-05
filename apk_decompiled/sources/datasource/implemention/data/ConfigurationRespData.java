package datasource.implemention.data;

import com.alibaba.ailabs.tg.mtop.data.BaseDataBean;
import datasource.bean.ConfigurationData;
import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes3.dex */
public class ConfigurationRespData extends BaseDataBean implements IMTOPDataObject {
    public ConfigurationData model;

    public ConfigurationData getModel() {
        return this.model;
    }

    public void setModel(ConfigurationData configurationData) {
        this.model = configurationData;
    }
}
