package com.aliyun.alink.business.devicecenter.api.add;

import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public enum LinkType {
    ALI_BROADCAST("ForceAliLinkTypeBroadcast"),
    ALI_BROADCAST_IN_BATCHES("ForceAliLinkTypeBroadcastInBatches"),
    ALI_ZERO_AP("ForceAliLinkTypeZeroAP"),
    ALI_PHONE_AP("ForceAliLinkTypePhoneAP"),
    ALI_BLE("ForceAliLinkTypeBLE"),
    ALI_SOFT_AP("ForceAliLinkTypeSoftAP"),
    ALI_QR("ForceAliLinkTypeQR"),
    ALI_ZERO_IN_BATCHES("ForceAliLinkTypeZeroInBatches"),
    ALI_APP_MESH("ForceAliLinkTypeAppMesh"),
    ALI_GATEWAY_MESH("ForceAliLinkTypeGatewayMesh"),
    ALI_APP_COMBO_MESH("ForceAliLinkTypeAppComboMesh"),
    ALI_GENIE_SOUND_BOX("ForceAliLinkTypeGenieSoundBox"),
    ALI_GENIE_QR("ForceAliLinkTypeGenieQR"),
    ALI_GENIE_STATIC_QR("ForceAliLinkTypeGenieStaticQR"),
    ALI_CLOUD_GENIE_SILENT("ForceAliLinkTypeGenieSilent"),
    ALI_WIFI_SMART_CONFIG("ForceAliLinkTypeGenieEncryptSmartConfig"),
    ALI_BATCH_APP_MESH("ForceAliLinkTypeBatchAppMesh"),
    ALI_BATCH_GATEWAY_MESH("ForceAliLinkTypeBatchGatewayMesh"),
    ALI_DEFAULT("ForceAliLinkTypeNone");

    public String name;

    LinkType(String str) {
        this.name = null;
        this.name = str;
    }

    public static LinkType getLinkType(String str) {
        if (TextUtils.isEmpty(str)) {
            return ALI_DEFAULT;
        }
        for (LinkType linkType : values()) {
            if (linkType != null && str.equals(linkType.getName())) {
                return linkType;
            }
        }
        return ALI_DEFAULT;
    }

    public String getName() {
        return this.name;
    }
}
