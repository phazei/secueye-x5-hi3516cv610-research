package datasource.channel.data;

import com.aliyun.alink.linksdk.connectsdk.BaseApiResponse;
import datasource.bean.Sigmesh;
import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class FeiyanGroupControlData extends BaseApiResponse {
    public String deviceType;
    public boolean operateAll;
    public List<Sigmesh> sigmesh;

    public String getDeviceType() {
        return this.deviceType;
    }

    public List<Sigmesh> getSigmesh() {
        return this.sigmesh;
    }

    public boolean isOperateAll() {
        return this.operateAll;
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
