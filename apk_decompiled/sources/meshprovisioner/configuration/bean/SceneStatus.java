package meshprovisioner.configuration.bean;

import android.content.Context;
import android.util.Pair;
import com.alibaba.ailabs.iot.mesh.R;

/* JADX INFO: loaded from: classes4.dex */
public class SceneStatus {
    public short currentScene;
    public int status;
    public Short targetScene;

    public SceneStatus(int i, short s, Short sh) {
        this.status = i;
        this.currentScene = s;
        this.targetScene = sh;
    }

    public static String parseStatusMessage(Context context, int i) {
        return i != 0 ? i != 1 ? i != 2 ? context.getString(R.string.status_scene_reserved) : context.getString(R.string.status_scene_not_found) : context.getString(R.string.status_scene_register_full) : context.getString(R.string.model_subscription_success);
    }

    public short getCurrentScene() {
        return this.currentScene;
    }

    public int getStatus() {
        return this.status;
    }

    public Short getTargetScene() {
        return this.targetScene;
    }

    public Pair<Integer, Object> parseStatus(Context context) {
        int i = this.status;
        return i != 0 ? i != 1 ? i != 2 ? new Pair<>(-52, context.getString(R.string.status_scene_reserved)) : new Pair<>(-51, context.getString(R.string.status_scene_not_found)) : new Pair<>(-50, context.getString(R.string.status_scene_register_full)) : new Pair<>(0, true);
    }

    public void setCurrentScene(short s) {
        this.currentScene = s;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public void setTargetScene(Short sh) {
        this.targetScene = sh;
    }
}
