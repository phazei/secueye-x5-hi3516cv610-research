package meshprovisioner;

/* JADX INFO: loaded from: classes4.dex */
public enum ProxyCommunicationQuality {
    QUALITY_LEVE_3(3),
    QUALITY_LEVE_2(2),
    QUALITY_LEVE_1(1),
    QUALITY_LEVE_0(0);

    public int level;

    ProxyCommunicationQuality(int i) {
        this.level = i;
    }

    public static ProxyCommunicationQuality getQualityViaLevel(int i) {
        return i == 3 ? QUALITY_LEVE_3 : i == 2 ? QUALITY_LEVE_2 : i == 1 ? QUALITY_LEVE_1 : QUALITY_LEVE_0;
    }

    public static ProxyCommunicationQuality getQualityViaRssi(int i) {
        return i >= -50 ? QUALITY_LEVE_3 : i >= -70 ? QUALITY_LEVE_2 : i >= -90 ? QUALITY_LEVE_1 : QUALITY_LEVE_0;
    }

    public int getLevel() {
        return this.level;
    }
}
