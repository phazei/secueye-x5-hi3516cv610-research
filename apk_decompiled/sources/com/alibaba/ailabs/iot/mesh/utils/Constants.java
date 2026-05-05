package com.alibaba.ailabs.iot.mesh.utils;

/* JADX INFO: loaded from: classes.dex */
public class Constants {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static boolean f2822a = true;

    public enum AliMeshSolutionType {
        SIG_MESH(0),
        TINY_MESH_ADV_V1(1),
        TINY_MESH_ADV_V2(2),
        TINY_MESH_GATT_V1(3),
        TINY_MESH_GATT_V2(4),
        UNKNOWN(5);

        public int solutionType;

        AliMeshSolutionType(int i) {
            this.solutionType = i;
        }

        public int getSolutionType() {
            return this.solutionType;
        }
    }
}
