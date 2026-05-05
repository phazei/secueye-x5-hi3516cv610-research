package datasource.implemention.service;

import com.alibaba.ailabs.tg.mtop.genie.GenieMtopServiceConfig;
import com.alibaba.ailabs.tg.network.Call;
import com.alibaba.ailabs.tg.network.annotation.genie.GenieMtopApiName;
import com.alibaba.ailabs.tg.network.annotation.genie.GenieMtopHost;
import com.alibaba.ailabs.tg.network.annotation.genie.GenieMtopParameters;
import com.alibaba.ailabs.tg.network.annotation.genie.GenieMtopResponse;
import datasource.bean.WakeUpServiceContext;
import datasource.implemention.data.IoTWakeUpData;
import datasource.implemention.response.IoTWakeUpResp;

/* JADX INFO: loaded from: classes3.dex */
public interface IoTOTAService {
    @GenieMtopApiName("OTAService.wakeupDevice")
    @GenieMtopResponse(IoTWakeUpResp.class)
    @GenieMtopHost(GenieMtopServiceConfig.ServiceType.HSF)
    Call<IoTWakeUpData> wakeupDevice(@GenieMtopParameters("devId") String str, @GenieMtopParameters("extParams") WakeUpServiceContext wakeUpServiceContext);
}
