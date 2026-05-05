package meshprovisioner;

/* JADX INFO: loaded from: classes4.dex */
public enum ProxyProtocolMessageType {
    NetworkPDU(0),
    MeshBeacon(1),
    ProxyConfiguration(2),
    ProvisioningPDU(3),
    RFU(4);

    public int type;

    ProxyProtocolMessageType(int i) {
        this.type = i;
    }
}
