package meshprovisioner.utils;

/* JADX INFO: loaded from: classes4.dex */
public class DeviceFeatureUtils {
    public static final boolean supportsFriendFeature(int i) {
        return (i & 4) > 0;
    }

    public static final boolean supportsLowPowerFeature(int i) {
        return (i & 8) > 0;
    }

    public static final boolean supportsProxyFeature(int i) {
        return (i & 2) > 0;
    }

    public static final boolean supportsRelayFeature(int i) {
        return (i & 1) > 0;
    }
}
