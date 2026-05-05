package com.alibaba.ailabs.iot.mesh.bean;

import java.io.Serializable;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DeviceStatusesPayload implements Serializable {
    public List<SubDeviceStatuses> subDeviceStatuses;

    public static class SubDeviceStatuses implements Serializable {
        public String linkQualityMetric;
        public String status;
        public int unicastAddress;

        public SubDeviceStatuses() {
        }

        public String getLinkQualityMetric() {
            return this.linkQualityMetric;
        }

        public String getStatus() {
            return this.status;
        }

        public int getUnicastAddress() {
            return this.unicastAddress;
        }

        public void setLinkQualityMetric(String str) {
            this.linkQualityMetric = str;
        }

        public void setStatus(String str) {
            this.status = str;
        }

        public void setUnicastAddress(int i) {
            this.unicastAddress = i;
        }

        public SubDeviceStatuses(int i, String str) {
            this.unicastAddress = i;
            this.status = str;
        }
    }

    public List<SubDeviceStatuses> getSubDeviceStatuses() {
        return this.subDeviceStatuses;
    }

    public void setSubDeviceStatuses(List<SubDeviceStatuses> list) {
        this.subDeviceStatuses = list;
    }
}
