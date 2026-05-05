package com.alibaba.ailabs.iot.mesh.bean;

import meshprovisioner.BaseMeshNode;
import meshprovisioner.configuration.ProvisionedMeshNode;

/* JADX INFO: loaded from: classes.dex */
public class ExtendedMeshNode {
    public BaseMeshNode mMeshNode;

    public ExtendedMeshNode(BaseMeshNode baseMeshNode) {
        this.mMeshNode = baseMeshNode;
    }

    public BaseMeshNode getMeshNode() {
        return this.mMeshNode;
    }

    public boolean hasAddedAppKeys() {
        if (this.mMeshNode.isProvisioned()) {
            return !((ProvisionedMeshNode) this.mMeshNode).getAddedAppKeys().isEmpty();
        }
        return false;
    }

    public boolean hasElements() {
        if (this.mMeshNode.isProvisioned()) {
            return !((ProvisionedMeshNode) this.mMeshNode).getElements().isEmpty();
        }
        return false;
    }

    public void setMeshNode(ProvisionedMeshNode provisionedMeshNode) {
        this.mMeshNode = provisionedMeshNode;
    }

    public void updateMeshNode(BaseMeshNode baseMeshNode) {
        this.mMeshNode = baseMeshNode;
    }
}
