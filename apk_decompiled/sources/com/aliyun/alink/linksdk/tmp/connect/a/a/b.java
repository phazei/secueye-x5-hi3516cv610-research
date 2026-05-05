package com.aliyun.alink.linksdk.tmp.connect.a.a;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPConstant;
import com.aliyun.alink.linksdk.alcs.data.ica.ICAAuthParams;
import com.aliyun.alink.linksdk.alcs.data.ica.ICAOptions;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.group.PalGroupInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.group.PalGroupReqMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.data.ica.group.ICAGroupOption;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.api.DeviceManager;
import com.aliyun.alink.linksdk.tmp.connect.d;
import com.aliyun.alink.linksdk.tmp.data.device.Option;
import com.aliyun.alink.linksdk.tmp.device.payload.service.ServiceRequestPayload;
import com.aliyun.alink.linksdk.tmp.device.request.localgroup.QueryLocalGroupDeviceRequest;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;
import java.util.List;

/* JADX INFO: compiled from: TmpGroupServiceRequestBuilder.java */
/* JADX INFO: loaded from: classes2.dex */
public class b extends a<b, ServiceRequestPayload> {
    private String m;
    private String n;
    private String o;
    private String p;
    private Option q;
    private List<DeviceBasicData> r;
    private QueryLocalGroupDeviceRequest.QueryLocalGroupDeviceResData s;

    public b(String str, String str2) {
        this.m = str;
        this.n = str2;
    }

    public static b a(String str, String str2) {
        return new b(str, str2);
    }

    public b b(String str, String str2) {
        this.o = str;
        this.p = str2;
        return this;
    }

    public b a(Option option) {
        this.q = option;
        return this;
    }

    public b a(QueryLocalGroupDeviceRequest.QueryLocalGroupDeviceResData queryLocalGroupDeviceResData) {
        this.s = queryLocalGroupDeviceResData;
        return this;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.a.a.a, com.aliyun.alink.linksdk.tmp.connect.CommonRequestBuilder
    public d c() {
        PalGroupReqMessage palGroupReqMessage = new PalGroupReqMessage();
        palGroupReqMessage.topic = d(this.m);
        palGroupReqMessage.payload = (TextUtils.isEmpty(this.g) ? GsonUtils.toJson(this.l) : this.g).getBytes();
        palGroupReqMessage.groupInfo = new PalGroupInfo();
        palGroupReqMessage.groupInfo.groupId = this.n;
        if (this.s != null) {
            palGroupReqMessage.groupInfo.deviceArray = new PalDeviceInfo[this.s.totalNum];
            for (int i = 0; i < this.s.totalNum; i++) {
                QueryLocalGroupDeviceRequest.QueryLocalGroupDeviceResDataInner queryLocalGroupDeviceResDataInner = this.s.items.get(i);
                DeviceBasicData deviceBasicData = DeviceManager.getInstance().getDeviceBasicData(queryLocalGroupDeviceResDataInner.getDeviceId());
                if (deviceBasicData == null) {
                    palGroupReqMessage.groupInfo.deviceArray[i] = new PalDeviceInfo(queryLocalGroupDeviceResDataInner.productKey, queryLocalGroupDeviceResDataInner.deviceName);
                } else {
                    palGroupReqMessage.groupInfo.deviceArray[i] = new PalDeviceInfo(queryLocalGroupDeviceResDataInner.productKey, queryLocalGroupDeviceResDataInner.deviceName, deviceBasicData.getAddr());
                }
            }
        }
        ICAOptions iCAOptions = new ICAOptions();
        iCAOptions.code = AlcsCoAPConstant.Code.GET.value;
        Option option = this.q;
        iCAOptions.type = (option == null ? TmpEnum.QoSLevel.QoS_CON : option.getQoSLevel()).getValue();
        ICAAuthParams iCAAuthParams = new ICAAuthParams();
        iCAAuthParams.accessKey = this.o;
        iCAAuthParams.accessToken = this.p;
        ICAGroupOption iCAGroupOption = new ICAGroupOption();
        iCAGroupOption.icaOptions = iCAOptions;
        iCAGroupOption.icaAuthParams = iCAAuthParams;
        palGroupReqMessage.palOptions = iCAGroupOption;
        return new d(palGroupReqMessage);
    }
}
