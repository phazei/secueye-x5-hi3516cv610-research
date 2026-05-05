package meshprovisioner.utils;

/* JADX INFO: loaded from: classes4.dex */
public class ParseStaticOutputOOBInformation {
    public static final int STATIC_OOB_INFO_AVAILABLE = 1;
    public static final int STATIC_OOB_INFO_UNAVAILABLE = 0;

    public static String parseStaticOOBActionInformation(int i) {
        return i != 0 ? i != 1 ? "Unknown" : "Static OOB Actions available" : "Static OOB Actions unavailable";
    }
}
