package com.aliyun.alink.business.devicecenter.channel.ble;

import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class TLV {
    public static final byte TYPE_CHECK_SIGN = 12;
    public static final byte TYPE_CHEC_SECURITY = 9;
    public static final byte TYPE_DEVICE_NAME = 3;
    public static final byte TYPE_PRODUCT_KEY = 2;
    public static final byte TYPE_RANDOM = 4;
    public static final byte TYPE_SDK_VERSION = 1;
    public static final byte TYPE_SIGN = 5;
    public static final byte TYPE_UPDATE_AUTHCODE_SECRET = 11;
    public static final byte TYPE_UPDATE_DEVICE_SECRET = 10;
    public static final byte TYPE_UPDATE_SEQ = 7;

    public static class Element {
        public byte length;
        public byte type;
        public byte[] value;

        public Element() {
        }

        public byte[] toPayload() {
            byte b2 = this.length;
            byte[] bArr = new byte[b2 + 2];
            bArr[0] = this.type;
            bArr[1] = b2;
            if (b2 > 0) {
                System.arraycopy(this.value, 0, bArr, 2, b2);
            }
            return bArr;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("type:");
            sb.append((int) this.type);
            sb.append(" length:");
            sb.append((int) this.length);
            sb.append(" value:");
            sb.append("[");
            byte[] bArr = this.value;
            sb.append(bArr == null ? "emptyvalue" : StringUtils.bytesToHexString(bArr));
            sb.append("] str:");
            byte[] bArr2 = this.value;
            sb.append(bArr2 == null ? TmpConstant.GROUP_ROLE_UNKNOWN : new String(bArr2));
            return sb.toString();
        }

        public Element(byte b2, byte[] bArr) {
            this.type = b2;
            this.value = bArr;
            if (bArr != null) {
                this.length = (byte) bArr.length;
            } else {
                this.length = (byte) 0;
            }
        }
    }

    public static List<Element> parse(byte[] bArr) {
        byte b2;
        ArrayList arrayList = new ArrayList();
        if (bArr == null || bArr.length == 0) {
            ALog.w("TLV", "invalid data. data:" + StringUtils.bytesToHexString(bArr));
        } else {
            Element element = null;
            int i = 0;
            char c2 = 1;
            for (int i2 = 0; i2 < bArr.length; i2++) {
                if (c2 == 1) {
                    Element element2 = new Element();
                    element2.type = bArr[i2];
                    if (i2 == bArr.length - 1) {
                        ALog.e("TLV", "no length data at index:" + i2);
                    }
                    i = 0;
                    element = element2;
                    c2 = 2;
                } else if (c2 == 2) {
                    element.length = bArr[i2];
                    int i3 = element.length;
                    if (i3 == 0) {
                        ALog.e("TLV", "value length error:" + ((int) element.length));
                        return arrayList;
                    }
                    if (i3 < 0) {
                        i3 += 256;
                    }
                    element.value = new byte[i3];
                    if (i2 == bArr.length - 1) {
                        ALog.w("TLV", "no value data at index:" + i2);
                    }
                    c2 = 3;
                } else if (c2 == 3) {
                    element.value[i] = bArr[i2];
                    i++;
                    byte b3 = element.length;
                    if ((b3 >= 0 && i == b3) || ((b2 = element.length) < 0 && i == b2 + 256)) {
                        arrayList.add(element);
                        c2 = 1;
                    } else if (i2 == bArr.length - 1) {
                        if (i < element.length) {
                            ALog.w("TLV", "expected length:" + ((int) element.length) + " real length:" + i);
                        } else {
                            c2 = 1;
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    public static byte[] toPayload(List<Element> list) {
        byte[] bArr = new byte[0];
        if (list.size() > 0) {
            Iterator<Element> it = list.iterator();
            while (it.hasNext()) {
                byte[] payload = it.next().toPayload();
                byte[] bArr2 = new byte[bArr.length + payload.length];
                System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
                System.arraycopy(payload, 0, bArr2, bArr.length, payload.length);
                bArr = bArr2;
            }
        }
        return bArr;
    }
}
