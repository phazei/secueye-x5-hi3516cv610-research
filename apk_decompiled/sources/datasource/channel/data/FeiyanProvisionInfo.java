package datasource.channel.data;

import com.aliyun.alink.linksdk.connectsdk.BaseApiResponse;
import datasource.bean.ProvisionInfo;
import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class FeiyanProvisionInfo extends BaseApiResponse {
    public Integer primaryUnicastAddress;
    public List<Integer> netKeyIndexes = null;
    public List<Integer> appKeyIndexes = null;

    public ProvisionInfo convert2ProvisionInfo() {
        ProvisionInfo provisionInfo = new ProvisionInfo();
        provisionInfo.setAppKeyIndexes(getAppKeyIndexes());
        provisionInfo.setNetKeyIndexes(getNetKeyIndexes());
        provisionInfo.setPrimaryUnicastAddress(getPrimaryUnicastAddress());
        return provisionInfo;
    }

    public List<Integer> getAppKeyIndexes() {
        return this.appKeyIndexes;
    }

    public List<Integer> getNetKeyIndexes() {
        return this.netKeyIndexes;
    }

    public Integer getPrimaryUnicastAddress() {
        return this.primaryUnicastAddress;
    }

    public void setAppKeyIndexes(List<Integer> list) {
        this.appKeyIndexes = list;
    }

    public void setNetKeyIndexes(List<Integer> list) {
        this.netKeyIndexes = list;
    }

    public void setPrimaryUnicastAddress(Integer num) {
        this.primaryUnicastAddress = num;
    }
}
