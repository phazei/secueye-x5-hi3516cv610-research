package a.a.a.a.b.i;

import android.text.TextUtils;
import com.alibaba.ailabs.iot.aisbase.spec.TLV;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: renamed from: a.a.a.a.b.i.a, reason: case insensitive filesystem */
/* JADX INFO: compiled from: ComboMeshProvisionHelper.java */
/* JADX INFO: loaded from: classes.dex */
public class C0335a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f1367a = "" + C0335a.class.getSimpleName();

    public static byte[] a(String str, String str2, String str3, byte b2) {
        if (TextUtils.isEmpty(str)) {
            a.a.a.a.b.m.a.b(f1367a, "Illegal parameter: SSID or password can not be null !!!");
            return null;
        }
        ArrayList arrayList = new ArrayList();
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        byte[] bytes2 = TextUtils.isEmpty(str2) ? null : str2.getBytes(StandardCharsets.UTF_8);
        byte[] bytes3 = TextUtils.isEmpty(str3) ? null : str3.getBytes(StandardCharsets.UTF_8);
        int length = bytes.length + 13 + (bytes2 == null ? 0 : bytes2.length);
        if (bytes3 != null) {
            length += bytes3.length + 2;
        }
        byte[] bArr = new byte[length];
        bArr[0] = -69;
        bArr[1] = 6;
        bArr[2] = -16;
        arrayList.add(new TLV((byte) 0, (byte) 1, new byte[]{1}));
        arrayList.add(new TLV((byte) 1, (byte) bytes.length, bytes));
        arrayList.add(new TLV((byte) 2, (byte) (bytes2 == null ? 0 : bytes2.length), bytes2));
        arrayList.add(new TLV((byte) 5, (byte) 1, new byte[]{b2}));
        int length2 = 3;
        if (bytes3 != null) {
            arrayList.add(new TLV((byte) 3, (byte) bytes3.length, bytes3));
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            byte[] bytes4 = ((TLV) it.next()).toBytes();
            System.arraycopy(bytes4, 0, bArr, length2, bytes4.length);
            length2 += bytes4.length;
        }
        a.a.a.a.b.m.a.c(f1367a, "package Wifi config result = " + MeshParserUtils.bytesToHex(bArr, false));
        return bArr;
    }
}
