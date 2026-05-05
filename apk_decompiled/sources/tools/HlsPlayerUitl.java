package tools;

import android.content.Context;
import android.view.TextureView;
import com.aliyun.iotx.linkvisual.media.video.player.HlsPlayer;

/* JADX INFO: loaded from: classes4.dex */
public class HlsPlayerUitl {
    Context context;
    HlsPlayer player;
    TextureView textureView;

    public HlsPlayerUitl(Context context, TextureView textureView) {
        this.player = new HlsPlayer(this.context);
        this.textureView = textureView;
        this.context = context;
    }

    public void setPlayer() {
        this.player.setTextureView(this.textureView);
    }
}
