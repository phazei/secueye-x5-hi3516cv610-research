package datasource.implemention.service;

import com.alibaba.ailabs.tg.mtop.genie.GenieMtopServiceConfig;
import com.alibaba.ailabs.tg.network.Call;
import com.alibaba.ailabs.tg.network.annotation.genie.GenieMtopApiName;
import com.alibaba.ailabs.tg.network.annotation.genie.GenieMtopHost;
import com.alibaba.ailabs.tg.network.annotation.genie.GenieMtopParameters;
import com.alibaba.ailabs.tg.network.annotation.genie.GenieMtopResponse;
import datasource.implemention.data.IoTGatewayInvokeData;
import datasource.implemention.response.IotGatewayInvokeEventResp;

/* JADX INFO: loaded from: classes3.dex */
public interface IoTGetwayService {
    @GenieMtopApiName("IotCommonService.invokeEventMethod")
    @GenieMtopResponse(IotGatewayInvokeEventResp.class)
    @GenieMtopHost(GenieMtopServiceConfig.ServiceType.HSF)
    Call<IoTGatewayInvokeData> invokeEventMethod(@GenieMtopParameters("eventName") String str, @GenieMtopParameters("genericRequest") String str2);
}
