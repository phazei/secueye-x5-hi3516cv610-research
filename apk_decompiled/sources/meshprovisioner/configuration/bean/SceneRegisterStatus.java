package meshprovisioner.configuration.bean;

import android.content.Context;
import android.util.Pair;
import com.alibaba.ailabs.iot.mesh.R;

/* JADX INFO: loaded from: classes4.dex */
public class SceneRegisterStatus {
    public short currentScene;
    public byte[] scenes;
    public int status;

    public SceneRegisterStatus(int i, short s, byte[] bArr) {
        this.status = i;
        this.currentScene = s;
        this.scenes = bArr;
    }

    public short getCurrentScene() {
        return this.currentScene;
    }

    public byte[] getScenes() {
        return this.scenes;
    }

    public int getStatus() {
        return this.status;
    }

    public Pair<Integer, Object> parseStatus(Context context) {
        int i = this.status;
        return i != 0 ? i != 1 ? i != 2 ? new Pair<>(-52, context.getString(R.string.status_scene_reserved)) : new Pair<>(-51, context.getString(R.string.status_scene_not_found)) : new Pair<>(-50, context.getString(R.string.status_scene_register_full)) : new Pair<>(0, true);
    }

    public void setCurrentScene(short s) {
        this.currentScene = s;
    }

    public void setScenes(byte[] bArr) {
        this.scenes = bArr;
    }

    public void setStatus(int i) {
        this.status = i;
    }
}
