package meshprovisioner.utils;

import java.util.Locale;

/* JADX INFO: loaded from: classes4.dex */
public class CompositionDataParser {
    public static String formatCompanyIdentifier(int i, boolean z) {
        if (!z) {
            return String.format(Locale.US, "%04X", Integer.valueOf(i));
        }
        return "0x" + String.format(Locale.US, "%04X", Integer.valueOf(i));
    }

    public static String formatFeatures(int i, boolean z) {
        if (!z) {
            return String.format(Locale.US, "%04X", Integer.valueOf(i));
        }
        return "0x" + String.format(Locale.US, "%04X", Integer.valueOf(i));
    }

    public static String formatModelIdentifier(int i, boolean z) {
        if (i < -32768 || i > 32767) {
            if (!z) {
                return String.format(Locale.US, "%08X", Integer.valueOf(i));
            }
            return "0x" + String.format(Locale.US, "%08X", Integer.valueOf(i));
        }
        if (!z) {
            return String.format(Locale.US, "%04X", Integer.valueOf(i));
        }
        return "0x" + String.format(Locale.US, "%04X", Integer.valueOf(i));
    }

    public static String formatProductIdentifier(int i, boolean z) {
        if (!z) {
            return String.format(Locale.US, "%04X", Integer.valueOf(i));
        }
        return "0x" + String.format(Locale.US, "%04X", Integer.valueOf(i));
    }

    public static String formatReplayProtectionCount(int i, boolean z) {
        if (!z) {
            return String.format(Locale.US, "%04X", Integer.valueOf(i));
        }
        return "0x" + String.format(Locale.US, "%04X", Integer.valueOf(i));
    }

    public static String formatVersionIdentifier(int i, boolean z) {
        if (!z) {
            return String.format(Locale.US, "%04X", Integer.valueOf(i));
        }
        return "0x" + String.format(Locale.US, "%04X", Integer.valueOf(i));
    }
}
