package meshprovisioner.utils;

import a.a.a.a.b.m.a;

/* JADX INFO: loaded from: classes4.dex */
public class ParseInputOOBActions {
    public static final short INPUT_ALPHA_NUMBERIC = 8;
    public static final short INPUT_NUMBER = 4;
    public static final short NO_INPUT = 0;
    public static final short PUSH = 1;
    public static final String TAG = "ParseInputOOBActions";
    public static final short TWIST = 2;

    public static String getInputOOBActionDescription(short s) {
        return s != 0 ? s != 1 ? s != 2 ? s != 4 ? s != 8 ? "Unknown" : "Input Alpha Numeric" : "Input Number" : "Twist" : "Push" : "Not supported";
    }

    public static int getOuputOOBActionValue(short s) {
        if (s == 1) {
            return 0;
        }
        if (s == 2) {
            return 1;
        }
        if (s != 4) {
            return s != 8 ? -1 : 3;
        }
        return 2;
    }

    public static void parseInputActionsFromBitMask(int i) {
        for (byte b2 : new byte[]{1, 2, 4, 8}) {
            if ((i & b2) == b2) {
                a.a(TAG, "Input oob action type value: " + getInputOOBActionDescription(b2));
            }
        }
    }

    public static int parseInputOOBActionValue(int i) {
        if (i == 0) {
            return 0;
        }
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                i2 = 4;
                if (i != 4) {
                    i2 = 8;
                    if (i != 8) {
                        return -1;
                    }
                }
            }
        }
        return i2;
    }
}
