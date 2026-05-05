package com.alibaba.ailabs.iot.mesh.provision;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.spec.TLV;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class WiFiConfigReplyParser {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final String f2813a = "wifi_config_" + WiFiConfigReplyParser.class.getSimpleName();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public int f2814b = 0;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public int f2815c = 0;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public a<Object> f2816d;

    public enum Status {
        START_CONNECT_AP(1, "start connect ap"),
        START_OBTAIN_IP(2, "start obtain ip"),
        START_CONNECT_MQTT(3, "start connect mqtt"),
        CONNECT_AP_SUCCESS(4, "on successful connect ap"),
        OBTAIN_IP_SUCCESS(5, "on successful obtain ip"),
        CONNECT_MQTT_SUCCESS(6, "on successful connect mqtt"),
        UNDEFINED(0, "undefined status");

        public int code;
        public String desc;

        Status(int i, String str) {
            this.code = i;
            this.desc = str;
        }

        public static Status parseFromValue(int i) {
            switch (i) {
                case 1:
                    return START_CONNECT_AP;
                case 2:
                    return START_OBTAIN_IP;
                case 3:
                    return START_CONNECT_MQTT;
                case 4:
                    return CONNECT_AP_SUCCESS;
                case 5:
                    return OBTAIN_IP_SUCCESS;
                case 6:
                    return CONNECT_MQTT_SUCCESS;
                default:
                    return UNDEFINED;
            }
        }

        public int getCode() {
            return this.code;
        }

        public String getDesc() {
            return this.desc;
        }
    }

    public interface a<T> extends IActionListener<T> {
        void a(int i, int i2, String str);

        void a(Status status);
    }

    public WiFiConfigReplyParser(a<Object> aVar) {
        this.f2816d = null;
        this.f2816d = aVar;
    }

    public void a(byte[] bArr) {
        List<TLV> multiFromBytes = (bArr == null || bArr.length < 3) ? null : TLV.parseMultiFromBytes(bArr);
        if (multiFromBytes == null || multiFromBytes.size() == 0) {
            return;
        }
        for (TLV tlv : multiFromBytes) {
            byte[] value = tlv.getValue();
            if (value != null && value.length != 0) {
                switch (tlv.getType()) {
                    case 1:
                        if (value[0] == 2) {
                            a.a.a.a.b.m.a.d(this.f2813a, "onMessage device connect ap or connect mqtt failed.");
                            this.f2815c = -1;
                        } else if (value[0] == 1) {
                            a.a.a.a.b.m.a.c(this.f2813a, "onMessage connect ap success.");
                            this.f2815c = 1;
                            a<Object> aVar = this.f2816d;
                            if (aVar != null) {
                                aVar.onSuccess("");
                                return;
                            }
                        } else if (value[0] == 3) {
                            a.a.a.a.b.m.a.c(this.f2813a, "onMessage token report success. params=");
                            this.f2815c = 2;
                        }
                        break;
                    case 2:
                        continue;
                    case 3:
                        if (value.length == 2) {
                            this.f2814b = ((value[1] & 255) << 8) | (value[0] & 255);
                        }
                        int i = this.f2814b;
                        if (i >= 50404) {
                            int i2 = this.f2815c;
                            if (i2 == -1) {
                                a.a.a.a.b.m.a.c(this.f2813a, "onMessage device connect provision fail, wait for device to retry until timeout.");
                                return;
                            }
                            if (i2 == 1) {
                                a.a.a.a.b.m.a.c(this.f2813a, "onMessage device connect ap success, device connect cloud failed, wait until timeout.");
                                return;
                            } else if (i2 == 2) {
                                a.a.a.a.b.m.a.c(this.f2813a, "onMessage device connect ap success, reportToken success, wait until loop cloud check.");
                                return;
                            } else {
                                a.a.a.a.b.m.a.c(this.f2813a, "onMessage device unexpected state returned, device connect cloud failed, wait until timeout.");
                                return;
                            }
                        }
                        a(-71, i, "wifi config fail. error code from node: " + this.f2814b);
                        break;
                        break;
                    case 4:
                        int i3 = ((value[1] & 255) << 8) | (value[0] & 255);
                        Status fromValue = Status.parseFromValue(i3);
                        a.a.a.a.b.m.a.a(this.f2813a, String.format(Locale.getDefault(), "device notifies of the status change to: %s, value: %d", fromValue, Integer.valueOf(i3)));
                        a<Object> aVar2 = this.f2816d;
                        if (aVar2 != null) {
                            aVar2.a(Status.parseFromValue(i3));
                            if (fromValue == Status.CONNECT_MQTT_SUCCESS) {
                                this.f2816d.onSuccess("");
                            } else if (fromValue == Status.UNDEFINED) {
                                a(-71, i3, "wifi config fail.");
                            }
                        }
                        break;
                    default:
                        a.a.a.a.b.m.a.d(this.f2813a, String.format(Locale.getDefault(), "Unknown type: %d", Byte.valueOf(tlv.getType())));
                        break;
                }
            }
        }
    }

    public final void a(int i, int i2, String str) {
        a<Object> aVar = this.f2816d;
        if (aVar != null) {
            aVar.a(i, i2, str);
        }
    }
}
