package datasource.implemention.data;

import com.alibaba.ailabs.tg.mtop.data.BaseDataBean;
import datasource.bean.CustomGroupControllModel;
import java.util.List;
import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes3.dex */
public class IotCustomGroupControllRespData extends BaseDataBean implements IMTOPDataObject {
    public List<CustomGroupControllModel> model;

    public List<CustomGroupControllModel> getModel() {
        return this.model;
    }

    public void setModel(List<CustomGroupControllModel> list) {
        this.model = list;
    }
}
