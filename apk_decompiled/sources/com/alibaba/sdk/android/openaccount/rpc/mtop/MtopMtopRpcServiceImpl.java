package com.alibaba.sdk.android.openaccount.rpc.mtop;

import com.alibaba.sdk.android.openaccount.OpenAccountConfigs;
import com.alibaba.sdk.android.openaccount.rpc.CommMtopRpcServiceImpl;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.features.MtopFeatureManager;
import mtopsdk.mtop.intf.MtopSetting;
import mtopsdk.xstate.XState;

/* JADX INFO: loaded from: classes.dex */
public class MtopMtopRpcServiceImpl extends CommMtopRpcServiceImpl {
    @Override // com.alibaba.sdk.android.openaccount.rpc.CommMtopRpcServiceImpl, com.alibaba.sdk.android.openaccount.rpc.RpcService
    public String getSource() {
        return "mtop2";
    }

    @Override // com.alibaba.sdk.android.openaccount.rpc.CommMtopRpcServiceImpl
    protected void enableSupportOpenAccount() {
        MtopSetting.setMtopFeatureFlag(MtopFeatureManager.MtopFeatureEnum.SUPPORT_OPEN_ACCOUNT, true);
    }

    @Override // com.alibaba.sdk.android.openaccount.rpc.CommMtopRpcServiceImpl
    protected void enableLog() {
        TBSdkLog.setLogEnable(TBSdkLog.LogEnable.VerboseEnable);
    }

    @Override // com.alibaba.sdk.android.openaccount.rpc.CommMtopRpcServiceImpl, com.alibaba.sdk.android.openaccount.rpc.RpcService
    public void registerSessionInfo(String str) {
        if (OpenAccountConfigs.enableOpenAccountMtopSession) {
            XState.setValue("sid", str);
        }
    }
}
