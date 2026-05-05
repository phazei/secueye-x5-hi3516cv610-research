package com.aliyun.alink.linksdk.tmp.data.auth;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class SetupData {
    public String configType = TmpConstant.CONFIG_TYPE_PROVISION_RECEIVER;
    public List<ProvisionAuthData> configValue = new ArrayList();

    public String toString() {
        StringBuilder sb = new StringBuilder(TextUtils.isEmpty(this.configType) ? TmpConstant.GROUP_ROLE_UNKNOWN : this.configType);
        List<ProvisionAuthData> list = this.configValue;
        sb.append(list != null ? list.toString() : TmpConstant.GROUP_ROLE_UNKNOWN);
        sb.append(super.toString());
        return sb.toString();
    }
}
