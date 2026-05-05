package meshprovisioner.utils;

/* JADX INFO: loaded from: classes4.dex */
public class ParsePublicKeyInformation {
    public static final int PUBLIC_KEY_INFORMATION_AVAILABLE = 1;
    public static final int PUBLIC_KEY_INFORMATION_UNAVAILABLE = 0;

    public static String parsePublicKeyInformation(int i) {
        return i != 0 ? i != 1 ? "Unknown" : "Public key information available" : "Public key information unavailable";
    }
}
