package com.aliyun.alink.linksdk.tmp.component.pkdnconvert;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IDevInfoTrans;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDiscoveryDeviceInfo;
import com.aliyun.alink.linksdk.tmp.TmpSdk;
import com.aliyun.alink.linksdk.tmp.component.cloud.ICloudProxyListener;
import com.aliyun.alink.linksdk.tmp.data.auth.ProductKeyResult;
import com.aliyun.alink.linksdk.tmp.error.ParamsError;
import com.aliyun.alink.linksdk.tmp.storage.TmpStorage;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes2.dex */
public class DefaultDevInfoTrans implements IDevInfoTrans {
    private static final String TAG = "[Tmp]DefaultDevInfoTrans";
    private Map<String, String> mProductIdToKeyList;
    private Map<String, PalDeviceInfo> mToAliIoTPkDnList;
    private Map<String, PalDeviceInfo> mToPrivatePkDnList;

    private DefaultDevInfoTrans() {
        this.mToPrivatePkDnList = new ConcurrentHashMap();
        this.mToAliIoTPkDnList = new ConcurrentHashMap();
        this.mProductIdToKeyList = new ConcurrentHashMap();
    }

    public static DefaultDevInfoTrans getInstance() {
        return SingleInstanceHolder.mInstance;
    }

    private static class SingleInstanceHolder {
        private static DefaultDevInfoTrans mInstance = new DefaultDevInfoTrans();

        private SingleInstanceHolder() {
        }
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IDevInfoTrans
    public void init(final PalDiscoveryDeviceInfo palDiscoveryDeviceInfo, final IDevInfoTrans.IDevInfoTransListener iDevInfoTransListener) {
        if (palDiscoveryDeviceInfo == null || iDevInfoTransListener == null || palDiscoveryDeviceInfo.deviceInfo == null) {
            ALog.e(TAG, "init DefaultDevInfoTrans error privateDeviceInfo or listener null");
            return;
        }
        if ("3".equalsIgnoreCase(palDiscoveryDeviceInfo.modelType) && palDiscoveryDeviceInfo.deviceInfo.productModel.endsWith("-model")) {
            insertConvertData(palDiscoveryDeviceInfo.deviceInfo, palDiscoveryDeviceInfo.pluginId, new PalDeviceInfo(palDiscoveryDeviceInfo.deviceInfo.productModel.replace("-model", ""), palDiscoveryDeviceInfo.deviceInfo.deviceId));
            iDevInfoTransListener.onComplete(true, null);
            ALog.d(TAG, "MODEL_TYPE_ALI_THIRD_PART local found");
            return;
        }
        if ("2".equalsIgnoreCase(palDiscoveryDeviceInfo.modelType)) {
            String str = this.mProductIdToKeyList.get(palDiscoveryDeviceInfo.deviceInfo.productModel);
            ALog.d(TAG, "MODEL_TYPE_ALI_BREEZE pubProductKey:" + str + " productModel:" + palDiscoveryDeviceInfo.deviceInfo.productModel);
            if (!TextUtils.isEmpty(str)) {
                breezeTrans(palDiscoveryDeviceInfo, str, iDevInfoTransListener);
                return;
            }
            String pkByPid = TmpStorage.getInstance().getPkByPid(palDiscoveryDeviceInfo.deviceInfo.productModel);
            ALog.d(TAG, "MODEL_TYPE_ALI_BREEZE getPkByPid pubProductKey:" + pkByPid + " productModel:" + palDiscoveryDeviceInfo.deviceInfo.productModel);
            if (!TextUtils.isEmpty(pkByPid)) {
                breezeTrans(palDiscoveryDeviceInfo, pkByPid, iDevInfoTransListener);
                return;
            } else {
                TmpSdk.getCloudProxy().queryProdtKeyFromProdtId(palDiscoveryDeviceInfo.deviceInfo.productModel, new ICloudProxyListener() { // from class: com.aliyun.alink.linksdk.tmp.component.pkdnconvert.DefaultDevInfoTrans.1
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // com.aliyun.alink.linksdk.tmp.component.cloud.ICloudProxyListener
                    public void onResponse(String str2, Object obj) {
                        ProductKeyResult.ProductKeyPayload productKeyPayload;
                        ALog.d(DefaultDevInfoTrans.TAG, "queryProdtKeyFromProdtId onResponse id:" + str2);
                        if (obj == null) {
                            ALog.e(DefaultDevInfoTrans.TAG, "queryProdtKeyFromProdtId onResponse rawPayload empty");
                            iDevInfoTransListener.onComplete(false, new ParamsError());
                            return;
                        }
                        try {
                            productKeyPayload = (ProductKeyResult.ProductKeyPayload) JSON.parseObject(String.valueOf(obj), ProductKeyResult.ProductKeyPayload.class);
                        } catch (Exception e) {
                            ALog.e(DefaultDevInfoTrans.TAG, "queryProdtKeyFromProdtId onResponse parseObject e:" + e.toString());
                            productKeyPayload = null;
                        }
                        ProductKeyResult productKeyResult = productKeyPayload != null ? (ProductKeyResult) productKeyPayload.data : null;
                        if (productKeyResult == null || TextUtils.isEmpty(productKeyResult.productKey)) {
                            ALog.e(DefaultDevInfoTrans.TAG, "queryProdtKeyFromProdtId onResponse productKeyResult or productKey empty");
                            iDevInfoTransListener.onComplete(false, new ParamsError());
                        } else {
                            TmpStorage.getInstance().savePkByPid(palDiscoveryDeviceInfo.deviceInfo.productModel, productKeyResult.productKey);
                            DefaultDevInfoTrans.this.mProductIdToKeyList.put(palDiscoveryDeviceInfo.deviceInfo.productModel, productKeyResult.productKey);
                            DefaultDevInfoTrans.this.breezeTrans(palDiscoveryDeviceInfo, productKeyResult.productKey, iDevInfoTransListener);
                        }
                    }

                    @Override // com.aliyun.alink.linksdk.tmp.component.cloud.ICloudProxyListener
                    public void onFailure(String str2, AError aError) {
                        ALog.e(DefaultDevInfoTrans.TAG, "queryProdtKeyFromProdtId onFailure id:" + str2);
                        iDevInfoTransListener.onComplete(false, aError);
                    }
                });
                return;
            }
        }
        ALog.d(TAG, "init don't need translate modelType:" + palDiscoveryDeviceInfo.modelType + " pk:" + palDiscoveryDeviceInfo.deviceInfo.productModel + " dn:" + palDiscoveryDeviceInfo.deviceInfo.deviceId);
        iDevInfoTransListener.onComplete(true, null);
    }

    protected void breezeTrans(PalDiscoveryDeviceInfo palDiscoveryDeviceInfo, String str, IDevInfoTrans.IDevInfoTransListener iDevInfoTransListener) {
        insertConvertData(palDiscoveryDeviceInfo.deviceInfo, palDiscoveryDeviceInfo.pluginId, new PalDeviceInfo(str, palDiscoveryDeviceInfo.deviceInfo.deviceId, palDiscoveryDeviceInfo.deviceInfo.ip, palDiscoveryDeviceInfo.deviceInfo.mac));
        iDevInfoTransListener.onComplete(true, null);
    }

    public void insertConvertData(PalDeviceInfo palDeviceInfo, String str, PalDeviceInfo palDeviceInfo2) {
        if (palDeviceInfo == null || palDeviceInfo2 == null) {
            ALog.e(TAG, "insertConvertData privateDeviceInfo or aliIoTDeviceInfo null");
            return;
        }
        ALog.d(TAG, "insertConvertData privateDeviceInfo:" + palDeviceInfo.toString() + " aliIoTDeviceInfo:" + palDeviceInfo2);
        this.mToPrivatePkDnList.put(palDeviceInfo2.getPkDnChangeDevId(str), palDeviceInfo);
        this.mToAliIoTPkDnList.put(palDeviceInfo.getPkDnChangeDevId(str), palDeviceInfo2);
    }

    public void updatePubDevInfo(String str, String str2, String str3, String str4) {
        ALog.d(TAG, "updatePubDevInfo oldId:" + str + " productKey:" + str2 + " deviceName:" + str3);
        PalDeviceInfo palDeviceInfo = new PalDeviceInfo(str2, str3);
        Map<String, PalDeviceInfo> map = this.mToPrivatePkDnList;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str4);
        PalDeviceInfo palDeviceInfo2 = map.get(sb.toString());
        if (palDeviceInfo2 != null) {
            this.mToPrivatePkDnList.remove(str + str4);
            this.mToPrivatePkDnList.put(palDeviceInfo.getPkDnChangeDevId(str4), palDeviceInfo2);
            this.mToAliIoTPkDnList.put(palDeviceInfo2.getPkDnChangeDevId(str4), palDeviceInfo);
        }
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IDevInfoTrans
    public PalDeviceInfo toAliIoTDeviceInfo(PalDeviceInfo palDeviceInfo, String str) {
        if (palDeviceInfo == null) {
            ALog.e(TAG, "toAliIoTDeviceInfo deviceInfo null");
            return palDeviceInfo;
        }
        PalDeviceInfo palDeviceInfo2 = this.mToAliIoTPkDnList.get(palDeviceInfo.getPkDnChangeDevId(str));
        if (palDeviceInfo2 == null) {
            palDeviceInfo2 = palDeviceInfo;
        }
        ALog.d(TAG, "toAliIoTDeviceInfo deviceInfo :" + palDeviceInfo.toString() + " pluginId:" + str + " retDeviceInfo:" + palDeviceInfo2.toString());
        return palDeviceInfo2;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IDevInfoTrans
    public PalDeviceInfo toPrivateDeviceInfo(PalDeviceInfo palDeviceInfo, String str) {
        if (palDeviceInfo == null) {
            ALog.e(TAG, "toPrivateDeviceInfo deviceInfo null");
            return palDeviceInfo;
        }
        PalDeviceInfo palDeviceInfo2 = this.mToPrivatePkDnList.get(palDeviceInfo.getPkDnChangeDevId(str));
        if (palDeviceInfo2 == null) {
            palDeviceInfo2 = palDeviceInfo;
        }
        ALog.d(TAG, "toPrivateDeviceInfo deviceInfo :" + palDeviceInfo.toString() + " pluginId:" + str + " retDeviceInfo:" + palDeviceInfo2.toString());
        return palDeviceInfo2;
    }
}
