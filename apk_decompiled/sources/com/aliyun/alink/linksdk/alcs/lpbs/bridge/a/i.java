package com.aliyun.alink.linksdk.alcs.lpbs.bridge.a;

import com.aliyun.alink.linksdk.alcs.data.ica.ICADeviceInfo;
import com.aliyun.alink.linksdk.alcs.data.ica.ICADiscoveryDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.api.AlcsPalConst;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.ica.ICAPalDiscoveryDevInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDiscoveryListener;
import com.aliyun.alink.linksdk.alcs.pal.ica.ICADiscoveryListener;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: ICADisHandlerWrapper.java */
/* JADX INFO: loaded from: classes2.dex */
public class i implements ICADiscoveryListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f4072a = "[AlcsLPBS]ICADisHandlerWrapper";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private PalDiscoveryListener f4073b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private c f4074c;

    public i(c cVar, PalDiscoveryListener palDiscoveryListener) {
        this.f4073b = palDiscoveryListener;
        this.f4074c = cVar;
    }

    @Override // com.aliyun.alink.linksdk.alcs.pal.ica.ICADiscoveryListener
    public void onDiscoveryDevice(String str, int i, String str2, ICADeviceInfo iCADeviceInfo) {
        if (iCADeviceInfo == null) {
            ALog.e(f4072a, "onDiscoveryDevice deviceInfo null");
            return;
        }
        ALog.d(f4072a, "onDiscoveryDevice addr:" + str + " port:" + i + " deviceInfo:" + iCADeviceInfo.toString() + " pal:" + str2);
        ICADiscoveryDeviceInfo iCADiscoveryDeviceInfo = new ICADiscoveryDeviceInfo(iCADeviceInfo, str, i, str2);
        this.f4074c.a(iCADeviceInfo.getDevId(), iCADiscoveryDeviceInfo);
        ICAPalDiscoveryDevInfo iCAPalDiscoveryDevInfo = new ICAPalDiscoveryDevInfo(new PalDeviceInfo(iCADiscoveryDeviceInfo.deviceInfo.productKey, iCADiscoveryDeviceInfo.deviceInfo.deviceName), iCADiscoveryDeviceInfo.isPkDnNeedConvert());
        iCAPalDiscoveryDevInfo.modelType = b(iCADiscoveryDeviceInfo);
        iCAPalDiscoveryDevInfo.dataFormat = a(iCADiscoveryDeviceInfo);
        iCAPalDiscoveryDevInfo.isDataToCloud = iCADiscoveryDeviceInfo.isDataToCloud();
        iCAPalDiscoveryDevInfo.tslData = iCADiscoveryDeviceInfo.tslData;
        iCAPalDiscoveryDevInfo.deviceInfo.ip = iCADiscoveryDeviceInfo.addr;
        PalDiscoveryListener palDiscoveryListener = this.f4073b;
        if (palDiscoveryListener != null) {
            palDiscoveryListener.onDiscoveryDevice(iCAPalDiscoveryDevInfo);
        }
    }

    @Override // com.aliyun.alink.linksdk.alcs.pal.ica.ICADiscoveryListener
    public void onDiscoveryFinish() {
        ALog.d(f4072a, "onDiscoveryFinish mListener:" + this.f4073b);
        PalDiscoveryListener palDiscoveryListener = this.f4073b;
        if (palDiscoveryListener != null) {
            palDiscoveryListener.onDiscoveryFinish();
        }
    }

    public String a(ICADiscoveryDeviceInfo iCADiscoveryDeviceInfo) {
        return iCADiscoveryDeviceInfo.isDataNeedConvert() ? AlcsPalConst.DATA_FORMAT_CUNSTOM : "ALINK_FORMAT";
    }

    public String b(ICADiscoveryDeviceInfo iCADiscoveryDeviceInfo) {
        return (ICADiscoveryListener.PAL_LINKKIT_RAW.equalsIgnoreCase(iCADiscoveryDeviceInfo.pal) || ICADiscoveryListener.PAL_LINKKIT_ICA.equalsIgnoreCase(iCADiscoveryDeviceInfo.pal) || !ICADiscoveryListener.PAL_DLCP_RAW.equalsIgnoreCase(iCADiscoveryDeviceInfo.pal)) ? "1" : "3";
    }
}
