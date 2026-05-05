package datasource.bean;

import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class AddModelClient {
    public int modelId;
    public List<Integer> statusOpcodes;

    public int getModelId() {
        return this.modelId;
    }

    public List<Integer> getStatusOpcodes() {
        return this.statusOpcodes;
    }

    public void setModelId(int i) {
        this.modelId = i;
    }

    public void setStatusOpcodes(List<Integer> list) {
        this.statusOpcodes = list;
    }
}
