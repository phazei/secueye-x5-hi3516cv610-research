package datasource.channel.data;

import com.aliyun.alink.linksdk.connectsdk.BaseApiResponse;
import datasource.bean.ControlProtocol;
import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class DownStreamControlData extends BaseApiResponse {
    public List<ControlProtocol> downControlProtocols;
    public boolean reportResult;

    public List<ControlProtocol> getDownControlProtocols() {
        return this.downControlProtocols;
    }

    public boolean isReportResult() {
        return this.reportResult;
    }

    public void setDownControlProtocols(List<ControlProtocol> list) {
        this.downControlProtocols = list;
    }

    public void setReportResult(boolean z) {
        this.reportResult = z;
    }
}
