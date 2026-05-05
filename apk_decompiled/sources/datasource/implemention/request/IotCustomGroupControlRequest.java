package datasource.implemention.request;

import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes3.dex */
public class IotCustomGroupControlRequest implements IMTOPDataObject {
    public long familyId;
    public long groupId;
    public String params;
    public String API_NAME = "mtop.alibaba.ai.IotDeviceGroupService.customGroupControl";
    public String VERSION = "1.0";
    public boolean NEED_ECODE = true;
    public boolean NEED_SESSION = true;
    public String source = "TMALL_GENIE_ANDROID";
    public String authInfo = null;

    public String getAPI_NAME() {
        return this.API_NAME;
    }

    public String getAuthInfo() {
        return this.authInfo;
    }

    public long getFamilyId() {
        return this.familyId;
    }

    public long getGroupId() {
        return this.groupId;
    }

    public String getParams() {
        return this.params;
    }

    public String getSource() {
        return this.source;
    }

    public String getVERSION() {
        return this.VERSION;
    }

    public boolean isNEED_ECODE() {
        return this.NEED_ECODE;
    }

    public boolean isNEED_SESSION() {
        return this.NEED_SESSION;
    }

    public void setAPI_NAME(String str) {
        this.API_NAME = str;
    }

    public void setAuthInfo(String str) {
        this.authInfo = str;
    }

    public void setFamilyId(long j) {
        this.familyId = j;
    }

    public void setGroupId(long j) {
        this.groupId = j;
    }

    public void setNEED_ECODE(boolean z) {
        this.NEED_ECODE = z;
    }

    public void setNEED_SESSION(boolean z) {
        this.NEED_SESSION = z;
    }

    public void setParams(String str) {
        this.params = str;
    }

    public void setSource(String str) {
        this.source = str;
    }

    public void setVERSION(String str) {
        this.VERSION = str;
    }
}
