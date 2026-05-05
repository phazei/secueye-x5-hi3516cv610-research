package meshprovisioner.utils;

import a.a.a.a.b.m.a;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes4.dex */
public class ParseOutputOOBActions {
    public static final short BEEP = 2;
    public static final short BLINK = 1;
    public static final short NO_OUTPUT = 0;
    public static final short OUTPUT_ALPHA_NUMERIC = 16;
    public static final short OUTPUT_NUMERIC = 8;
    public static final String TAG = "ParseOutputOOBActions";
    public static final short VIBRATE = 4;

    public static String getOuputOOBActionDescription(short s) {
        return s != 0 ? s != 1 ? s != 2 ? s != 4 ? s != 8 ? s != 16 ? "Unknown" : "Output Alpha Numeric" : "Output Numeric" : "Vibrate" : "Beep" : "Blink" : "Not Supported";
    }

    public static int getOuputOOBActionValue(short s) {
        if (s == 1) {
            return 0;
        }
        if (s == 2) {
            return 1;
        }
        if (s == 4) {
            return 2;
        }
        if (s != 8) {
            return s != 16 ? 0 : 4;
        }
        return 3;
    }

    public static int parseOuputOOBActionValue(int i) {
        if (i == 0) {
            return 0;
        }
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                if (i == 4) {
                    return 3;
                }
                if (i != 8) {
                    return i != 16 ? -1 : 10;
                }
                return 4;
            }
        }
        return i2;
    }

    public static void parseOutputActionsFromBitMask(int i) {
        ArrayList arrayList = new ArrayList();
        for (byte b2 : new byte[]{1, 2, 4, 8, 16}) {
            if ((i & b2) == b2) {
                arrayList.add(Byte.valueOf(b2));
                a.a(TAG, "Supported output oob action type: " + getOuputOOBActionDescription(b2));
            }
        }
    }

    public static short selectOutputActionsFromBitMask(int i) {
        ArrayList arrayList = new ArrayList();
        for (byte b2 : new byte[]{1, 2, 4, 8, 16}) {
            if ((i & b2) == b2) {
                arrayList.add(Byte.valueOf(b2));
                a.a(TAG, "Supported output oob action type: " + getOuputOOBActionDescription(b2));
            }
        }
        if (arrayList.isEmpty()) {
            return (short) 0;
        }
        return ((Byte) arrayList.get(0)).byteValue();
    }
}
