package datasource.channel.data;

import com.aliyun.alink.linksdk.connectsdk.BaseApiResponse;
import datasource.bean.Sigmesh;
import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class FeiyanControlData extends BaseApiResponse {
    public String devId;
    public String deviceType;
    public boolean operateAll;
    public List<Sigmesh> sigmesh;

    public String getDevId() {
        return this.devId;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public List<Sigmesh> getSigmesh() {
        List<Sigmesh> list = this.sigmesh;
        if (list != null && list.size() > 0) {
            this.sigmesh.get(0).setDevId(this.devId);
        }
        return this.sigmesh;
    }

    public boolean isOperateAll() {
        return this.operateAll;
    }

    public void setDevId(String str) {
        this.devId = str;
    }

    public void setDeviceType(String str) {
        this.deviceType = str;
    }

    public void setOperateAll(boolean z) {
        this.operateAll = z;
    }

    public void setSigmesh(List<Sigmesh> list) {
        this.sigmesh = list;
    }
}
