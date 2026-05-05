package com.aliyun.alink.business.devicecenter.provision.core.mesh;

import android.content.Context;
import com.aliyun.alink.business.devicecenter.api.add.LinkType;
import com.aliyun.alink.business.devicecenter.config.annotation.ConfigStrategy;

/* JADX INFO: loaded from: classes2.dex */
@ConfigStrategy(linkType = LinkType.ALI_APP_COMBO_MESH)
public class AppComboMeshStrategy extends AppMeshStrategy {
    public AppComboMeshStrategy() {
    }

    @Override // com.aliyun.alink.business.devicecenter.provision.core.mesh.AppMeshStrategy, com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public boolean needWiFiSsidPwd() {
        return true;
    }

    public AppComboMeshStrategy(Context context) {
        super(context);
    }
}
