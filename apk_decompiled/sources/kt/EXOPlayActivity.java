package kt;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.seculink.app.R;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: EXOPlayActivity.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0002J\b\u0010\t\u001a\u00020\bH\u0002J\u0012\u0010\n\u001a\u00020\b2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0014J\b\u0010\r\u001a\u00020\bH\u0002J\b\u0010\u000e\u001a\u00020\bH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lkt/EXOPlayActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "exoPlayer", "Lcom/google/android/exoplayer2/SimpleExoPlayer;", "playerView", "Lcom/google/android/exoplayer2/ui/PlayerView;", "bindPlayerToView", "", "initializePlayer", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "prepareMediaSource", "startPlaying", "secueye_googleRelease"}, k = 1, mv = {1, 1, 15})
public final class EXOPlayActivity extends AppCompatActivity {
    private HashMap _$_findViewCache;
    private SimpleExoPlayer exoPlayer;
    private PlayerView playerView;

    public void _$_clearFindViewByIdCache() {
        HashMap map = this._$_findViewCache;
        if (map != null) {
            map.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view2 = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view2 != null) {
            return view2;
        }
        View viewFindViewById = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), viewFindViewById);
        return viewFindViewById;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exoplay);
        initializePlayer();
        prepareMediaSource();
        bindPlayerToView();
        startPlaying();
    }

    private final void initializePlayer() {
        SimpleExoPlayer simpleExoPlayerNewSimpleInstance = ExoPlayerFactory.newSimpleInstance(this);
        Intrinsics.checkExpressionValueIsNotNull(simpleExoPlayerNewSimpleInstance, "ExoPlayerFactory.newSimpleInstance(this)");
        this.exoPlayer = simpleExoPlayerNewSimpleInstance;
    }

    private final void prepareMediaSource() {
        ExtractorMediaSource extractorMediaSourceCreateMediaSource = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(this, "exoplayer")).createMediaSource(Uri.parse("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear2/prog_index.m3u8"));
        SimpleExoPlayer simpleExoPlayer = this.exoPlayer;
        if (simpleExoPlayer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("exoPlayer");
        }
        simpleExoPlayer.prepare(extractorMediaSourceCreateMediaSource);
    }

    private final void bindPlayerToView() {
        View viewFindViewById = findViewById(R.id.playerView);
        Intrinsics.checkExpressionValueIsNotNull(viewFindViewById, "findViewById(R.id.playerView)");
        this.playerView = (PlayerView) viewFindViewById;
        PlayerView playerView = this.playerView;
        if (playerView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("playerView");
        }
        SimpleExoPlayer simpleExoPlayer = this.exoPlayer;
        if (simpleExoPlayer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("exoPlayer");
        }
        playerView.setPlayer(simpleExoPlayer);
    }

    private final void startPlaying() {
        SimpleExoPlayer simpleExoPlayer = this.exoPlayer;
        if (simpleExoPlayer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("exoPlayer");
        }
        simpleExoPlayer.setPlayWhenReady(true);
    }
}
