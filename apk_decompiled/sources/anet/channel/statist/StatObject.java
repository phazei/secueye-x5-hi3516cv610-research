package anet.channel.statist;

import java.io.Serializable;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public abstract class StatObject implements Serializable {
    public boolean beforeCommit() {
        return true;
    }
}
