package meshprovisioner.utils;

import androidx.annotation.NonNull;
import meshprovisioner.configuration.ProvisionedMeshNode;

/* JADX INFO: loaded from: classes4.dex */
public class ConfigModelPublicationSetParams {
    public final int appKeyIndex;
    public int aszmic;
    public boolean credentialFlag;
    public final byte[] elementAddress;
    public final ProvisionedMeshNode meshNode;
    public int modelIdentifier;
    public int publicationResolution;
    public int publicationSteps;
    public final byte[] publishAddress;
    public int publishRetransmitCount;
    public int publishRetransmitIntervalSteps;
    public int publishTtl;
    public final byte[] src;

    public ConfigModelPublicationSetParams(@NonNull ProvisionedMeshNode provisionedMeshNode, @NonNull byte[] bArr, @NonNull int i, @NonNull byte[] bArr2, @NonNull int i2) {
        this.meshNode = provisionedMeshNode;
        this.src = provisionedMeshNode.getConfigurationSrc();
        this.elementAddress = bArr;
        this.modelIdentifier = i;
        this.publishAddress = bArr2;
        this.appKeyIndex = i2;
    }

    public int getAppKeyIndex() {
        return this.appKeyIndex;
    }

    public int getAszmic() {
        return this.aszmic;
    }

    public boolean getCredentialFlag() {
        return this.credentialFlag;
    }

    public byte[] getElementAddress() {
        return this.elementAddress;
    }

    public ProvisionedMeshNode getMeshNode() {
        return this.meshNode;
    }

    public int getModelIdentifier() {
        return this.modelIdentifier;
    }

    public int getPublicationResolution() {
        return this.publicationResolution;
    }

    public int getPublicationSteps() {
        return this.publicationSteps;
    }

    public byte[] getPublishAddress() {
        return this.publishAddress;
    }

    public int getPublishRetransmitCount() {
        return this.publishRetransmitCount;
    }

    public int getPublishRetransmitIntervalSteps() {
        return this.publishRetransmitIntervalSteps;
    }

    public int getPublishTtl() {
        return this.publishTtl;
    }

    public void setCredentialFlag(boolean z) {
        this.credentialFlag = z;
    }

    public void setPublicationResolution(int i) {
        this.publicationResolution = i;
    }

    public void setPublicationSteps(int i) {
        this.publicationSteps = i;
    }

    public void setPublishRetransmitCount(int i) {
        this.publishRetransmitCount = i;
    }

    public void setPublishRetransmitIntervalSteps(int i) {
        this.publishRetransmitIntervalSteps = i;
    }

    public void setPublishTtl(int i) {
        this.publishTtl = i;
    }
}
