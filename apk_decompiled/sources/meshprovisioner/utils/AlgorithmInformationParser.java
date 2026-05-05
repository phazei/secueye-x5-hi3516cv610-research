package meshprovisioner.utils;

/* JADX INFO: loaded from: classes4.dex */
public class AlgorithmInformationParser {
    public static final short FIPS_P_256_ELLIPTIC_CURVE = 1;
    public static final short NONE = 0;

    public static String parseAlgorithm(short s) {
        return s != 1 ? "Unknown" : "FIPS P-256 Elliptic Curve";
    }
}
