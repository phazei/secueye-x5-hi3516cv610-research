package com.aliyun.alink.linksdk.tmp.device.panel.data;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class BoneDisFilter {
    private static final String TAG = "BoneDisFilter";
    public List<String> deviceType;
    public String gatewayIotId;
    public String groupId;
    public String productKey;

    public boolean doFilter(DeviceBasicData deviceBasicData) {
        if (deviceBasicData == null) {
            ALog.d(TAG, "basicData empty");
            return false;
        }
        List<String> list = this.deviceType;
        if ((list == null || list.isEmpty()) && TextUtils.isEmpty(this.productKey)) {
            ALog.d(TAG, "doFilter deviceType productKey empty");
            return true;
        }
        String modelType = deviceBasicData.getModelType();
        if ("2".equalsIgnoreCase(deviceBasicData.getModelType())) {
            modelType = TmpConstant.TMP_MODEL_TYPE_ALI_BREEZE;
        } else if ("1".equalsIgnoreCase(deviceBasicData.getModelType())) {
            modelType = "wifi";
        } else if ("3".equalsIgnoreCase(deviceBasicData.getModelType())) {
            modelType = TmpConstant.TMP_MODEL_TYPE_ALI_THIRD_PART;
        } else if ("4".equalsIgnoreCase(deviceBasicData.getModelType())) {
            modelType = TmpConstant.TMP_MODEL_TYPE_ALI_LCA_CLOUD;
        }
        ALog.d(TAG, "doFilter strModelType:" + modelType + " modelType:" + deviceBasicData.getModelType() + " productKey:" + this.productKey + " deviceType:" + this.deviceType);
        if (!TextUtils.isEmpty(this.productKey) && this.productKey.equalsIgnoreCase(deviceBasicData.getProductKey())) {
            return true;
        }
        List<String> list2 = this.deviceType;
        return list2 != null && list2.contains(modelType);
    }
}
