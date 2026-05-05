package meshprovisioner.utils;

/* JADX INFO: loaded from: classes4.dex */
public class ParseProvisioningAlgorithm {
    public static final int FIPS_P_256_ELLIPTIC_CURVE = 1;

    public static String getAlgorithmType(int i) {
        return i != 1 ? "NONE" : "FIPS P-256 ELLIPTIC CURVE";
    }

    public static byte getAlgorithmValue(int i) {
        return i != 1 ? (byte) 1 : (byte) 0;
    }
}
