package com.aliyun.alink.linksdk.tmp.utils;

import com.alibaba.ailabs.iot.mesh.managers.MeshDeviceInfoManager;
import com.aliyun.alink.linksdk.tmp.device.panel.data.ProductInfoPayload;

/* JADX INFO: loaded from: classes2.dex */
public class DeviceClassificationUtil {
    public static final String PROTOCOL_BLE_MESH = "ble-mesh";
    public static final String PROTOCOL_COMBO_MESH = "combo-mesh";
    private static final String TAG = "[Tmp]DeviceClassificationUtil";

    public static boolean isBleMeshDeviceViaProductInfo(ProductInfoPayload.ProductInfo productInfo) {
        return productInfo != null && (PROTOCOL_BLE_MESH.equalsIgnoreCase(productInfo.protocol) || PROTOCOL_COMBO_MESH.equalsIgnoreCase(productInfo.protocol)) && "NET_BT".equalsIgnoreCase(productInfo.netType);
    }

    public static boolean isComboMeshDeviceViaProductInfo(ProductInfoPayload.ProductInfo productInfo) {
        return productInfo != null && PROTOCOL_COMBO_MESH.equalsIgnoreCase(productInfo.protocol) && "NET_BT".equalsIgnoreCase(productInfo.netType);
    }

    public static boolean isComboMeshDeviceViaIotID(String str) {
        return MeshDeviceInfoManager.getInstance().isComboMeshDevice(str);
    }
}
