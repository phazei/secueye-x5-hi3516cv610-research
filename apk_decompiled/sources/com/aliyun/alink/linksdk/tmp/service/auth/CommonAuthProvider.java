package com.aliyun.alink.linksdk.tmp.service.auth;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.alcs.lpbs.api.AlcsPalConst;
import com.aliyun.alink.linksdk.alcs.lpbs.component.auth.IAuthProvider;
import com.aliyun.alink.linksdk.alcs.lpbs.component.auth.IAuthProviderListener;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.breeze.ComboAuthInfo;
import com.aliyun.alink.linksdk.tmp.data.auth.AccessInfo;
import com.aliyun.alink.linksdk.tmp.device.request.GateWayRequest;
import com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener;
import com.aliyun.alink.linksdk.tmp.device.request.auth.NotifyAccessInfoRequest;
import com.aliyun.alink.linksdk.tmp.storage.TmpStorage;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class CommonAuthProvider implements IAuthProvider {
    private static final String TAG = "[Tmp]ComboAuthProvider";

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.auth.IAuthProvider
    public void queryAuthInfo(PalDeviceInfo palDeviceInfo, String str, Object obj, IAuthProviderListener iAuthProviderListener) {
        ComboAuthInfo comboAuthInfo;
        ALog.d(TAG, "queryAuthInfo palDeviceInfo:" + palDeviceInfo + " path:" + str + " data:" + obj + " iAuthProviderListener:" + iAuthProviderListener);
        if (TextUtils.isEmpty(str)) {
            ALog.d(TAG, "queryAuthInfo path empty palDeviceInfo:" + palDeviceInfo + " data:" + obj + " iAuthProviderListener:" + iAuthProviderListener);
            return;
        }
        if (str.equalsIgnoreCase(AlcsPalConst.PATH_COMBO_QUERY_ACCESSINFO)) {
            if (iAuthProviderListener == null) {
                ALog.e(TAG, "queryAuthInfo PATH_COMBO_QUERY_ACCESSINFO iAuthProviderListener empty");
                return;
            }
            AccessInfo comboAccessInfo = TmpStorage.getInstance().getComboAccessInfo(palDeviceInfo.getDevId());
            ComboAuthInfo.SyncAccessInfo syncAccessInfo = TmpStorage.getInstance().getSyncAccessInfo(palDeviceInfo.getDevId());
            if (comboAccessInfo != null) {
                comboAuthInfo = new ComboAuthInfo(comboAccessInfo.mAccessKey, comboAccessInfo.mAccessToken, syncAccessInfo);
            } else {
                comboAuthInfo = new ComboAuthInfo(null, null, syncAccessInfo);
            }
            iAuthProviderListener.onComplete(palDeviceInfo, comboAuthInfo);
            return;
        }
        if (str.equalsIgnoreCase(AlcsPalConst.PATH_COMBO_UPDATE_ACCESSINFO)) {
            if (obj == null || !(obj instanceof ComboAuthInfo)) {
                ALog.e(TAG, "PATH_COMBO_UPDATE_ACCESSINFO data empty");
                return;
            }
            ComboAuthInfo comboAuthInfo2 = (ComboAuthInfo) obj;
            if (comboAuthInfo2.syncAccessInfo != null) {
                TmpStorage.getInstance().saveComboAccessInfo(palDeviceInfo.getDevId(), comboAuthInfo2.syncAccessInfo.accessKey, comboAuthInfo2.syncAccessInfo.accessToken);
            }
            TmpStorage.getInstance().saveSyncAccessInfo(palDeviceInfo.getDevId(), null, null, null);
            GateWayRequest notifyAccessInfoRequest = new NotifyAccessInfoRequest(palDeviceInfo.productModel, palDeviceInfo.deviceId, null);
            notifyAccessInfoRequest.sendRequest(notifyAccessInfoRequest, new IGateWayRequestListener() { // from class: com.aliyun.alink.linksdk.tmp.service.auth.CommonAuthProvider.1
                @Override // com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener
                public void onSuccess(Object obj2, Object obj3) {
                    ALog.d(CommonAuthProvider.TAG, "notifyAccessInfoRequest onSuccess o:" + obj2 + " result:" + obj3);
                }

                @Override // com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener
                public void onFail(Object obj2, AError aError) {
                    ALog.e(CommonAuthProvider.TAG, "notifyAccessInfoRequest onFail o:" + obj2 + " error:" + aError);
                }
            });
            if (iAuthProviderListener != null) {
                iAuthProviderListener.onComplete(palDeviceInfo, null);
            }
        }
    }
}
