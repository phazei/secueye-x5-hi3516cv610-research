package b.a;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import meshprovisioner.control.TransportControlMessage;

/* JADX INFO: compiled from: BlockAcknowledgementMessage.java */
/* JADX INFO: loaded from: classes.dex */
public class a extends TransportControlMessage {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f2124a = "a";

    public a(byte[] bArr) {
    }

    public static Integer a(Integer num, int i) {
        return num == null ? Integer.valueOf((1 << i) | 0) : Integer.valueOf(num.intValue() | (1 << i));
    }

    public static boolean b(Integer num, int i) {
        int iIntValue = num.intValue();
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            if (((iIntValue >> i3) & 1) == 1) {
                i2++;
            }
        }
        return i2 == i;
    }

    @Override // meshprovisioner.control.TransportControlMessage
    public TransportControlMessage.TransportControlMessageState a() {
        return TransportControlMessage.TransportControlMessageState.LOWER_TRANSPORT_BLOCK_ACKNOWLEDGEMENT;
    }

    public static ArrayList<Integer> a(byte[] bArr, int i) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        int i2 = ByteBuffer.wrap(bArr).order(ByteOrder.BIG_ENDIAN).getInt();
        for (int i3 = 0; i3 < i; i3++) {
            if (((i2 >> i3) & 1) == 1) {
                String str = f2124a;
                StringBuilder sb = new StringBuilder();
                sb.append("Segment ");
                sb.append(i3);
                sb.append(" of ");
                sb.append(i - 1);
                sb.append(" received by peer");
                a.a.a.a.b.m.a.a(str, sb.toString());
            } else {
                arrayList.add(Integer.valueOf(i3));
                String str2 = f2124a;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Segment ");
                sb2.append(i3);
                sb2.append(" of ");
                sb2.append(i - 1);
                sb2.append(" not received by peer");
                a.a.a.a.b.m.a.a(str2, sb2.toString());
            }
        }
        return arrayList;
    }
}
