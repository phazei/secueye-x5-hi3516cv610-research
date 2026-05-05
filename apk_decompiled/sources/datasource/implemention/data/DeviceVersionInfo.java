package datasource.implemention.data;

import com.alibaba.ailabs.tg.mtop.data.BaseDataBean;
import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes3.dex */
public class DeviceVersionInfo extends BaseDataBean implements IMTOPDataObject {
    public DeviceInfoModel model;

    public static class DeviceInfoModel {
        public String canOta;
        public String md5;
        public String minimumAppVersion;
        public String otaUrl;
        public String size;
        public String version;

        public String getCanOta() {
            return this.canOta;
        }

        public String getMd5() {
            return this.md5;
        }

        public String getMinimumAppVersion() {
            return this.minimumAppVersion;
        }

        public String getOtaUrl() {
            return this.otaUrl;
        }

        public String getSize() {
            return this.size;
        }

        public String getVersion() {
            return this.version;
        }

        public void setCanOta(String str) {
            this.canOta = str;
        }

        public void setMd5(String str) {
            this.md5 = str;
        }

        public void setMinimumAppVersion(String str) {
            this.minimumAppVersion = str;
        }

        public void setOtaUrl(String str) {
            this.otaUrl = str;
        }

        public void setSize(String str) {
            this.size = str;
        }

        public void setVersion(String str) {
            this.version = str;
        }

        public String toString() {
            return String.format("DeviceVersionInfo(otaUrl: %s, version: %s, md5: %s, size: %s, minimumAppVersion: %s, canOta: %s)", this.otaUrl, this.version, this.md5, this.size, this.minimumAppVersion, this.canOta);
        }
    }

    public DeviceInfoModel getModel() {
        return this.model;
    }

    public void setModel(DeviceInfoModel deviceInfoModel) {
        this.model = deviceInfoModel;
    }

    public String toString() {
        return String.format("DeviceVersionInfo {%s}", this.model);
    }
}
