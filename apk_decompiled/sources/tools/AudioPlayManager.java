package tools;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import java.io.IOException;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes4.dex */
public class AudioPlayManager implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private static volatile AudioPlayManager manager;
    private MediaPlayerBuilder builder;
    private boolean isPausing;
    private MediaPlayer mediaPlayer;
    public long playerId;

    @Override // android.media.MediaPlayer.OnBufferingUpdateListener
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mediaPlayer) {
    }

    @Override // android.media.MediaPlayer.OnPreparedListener
    public void onPrepared(MediaPlayer mediaPlayer) {
    }

    private AudioPlayManager() {
    }

    public synchronized void play() {
        if (this.mediaPlayer != null && !this.mediaPlayer.isPlaying()) {
            if (this.isPausing) {
                this.isPausing = false;
                this.mediaPlayer.start();
            } else {
                try {
                    try {
                        this.mediaPlayer.prepare();
                        this.mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IllegalStateException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public synchronized void pause() {
        if (this.mediaPlayer != null && this.mediaPlayer.isPlaying()) {
            this.isPausing = true;
            this.mediaPlayer.pause();
        }
    }

    public synchronized void release() {
        if (this.mediaPlayer != null) {
            this.isPausing = false;
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
    }

    public synchronized void release(long j) {
        if (this.playerId == j) {
            release();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x000f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized boolean isPlaying() {
        /*
            r1 = this;
            monitor-enter(r1)
            android.media.MediaPlayer r0 = r1.mediaPlayer     // Catch: java.lang.Throwable -> L12
            if (r0 == 0) goto Lf
            android.media.MediaPlayer r0 = r1.mediaPlayer     // Catch: java.lang.Throwable -> L12
            boolean r0 = r0.isPlaying()     // Catch: java.lang.Throwable -> L12
            if (r0 == 0) goto Lf
            r0 = 1
            goto L10
        Lf:
            r0 = 0
        L10:
            monitor-exit(r1)
            return r0
        L12:
            r0 = move-exception
            monitor-exit(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: tools.AudioPlayManager.isPlaying():boolean");
    }

    public static AudioPlayManager with() {
        return with(null);
    }

    public static AudioPlayManager with(MediaPlayerBuilder mediaPlayerBuilder) {
        initialize();
        if (mediaPlayerBuilder != null) {
            manager.initAudio(mediaPlayerBuilder);
        }
        return manager;
    }

    private void initAudio(MediaPlayerBuilder mediaPlayerBuilder) {
        AssetFileDescriptor assetFileDescriptorOpenRawResourceFd;
        if (mediaPlayerBuilder == null) {
            return;
        }
        if (this.mediaPlayer == null) {
            this.mediaPlayer = new MediaPlayer();
        }
        this.mediaPlayer.reset();
        this.mediaPlayer.setLooping(mediaPlayerBuilder.isLooping);
        this.playerId++;
        if (mediaPlayerBuilder.type == MediaPlayerBuilder.TYPE.URI) {
            if (mediaPlayerBuilder.ref.get() == null) {
                return;
            }
            try {
                this.mediaPlayer.setDataSource((Context) mediaPlayerBuilder.ref.get(), (Uri) mediaPlayerBuilder.obj);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (mediaPlayerBuilder.type == MediaPlayerBuilder.TYPE.ASSET) {
            if (mediaPlayerBuilder.ref.get() == null) {
                return;
            }
            AssetFileDescriptor assetFileDescriptorOpenFd = null;
            try {
                assetFileDescriptorOpenFd = ((Context) mediaPlayerBuilder.ref.get()).getAssets().openFd((String) mediaPlayerBuilder.obj);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            if (assetFileDescriptorOpenFd == null) {
                return;
            }
            try {
                this.mediaPlayer.setDataSource(assetFileDescriptorOpenFd.getFileDescriptor(), assetFileDescriptorOpenFd.getStartOffset(), assetFileDescriptorOpenFd.getLength());
                assetFileDescriptorOpenFd.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        } else if (mediaPlayerBuilder.type == MediaPlayerBuilder.TYPE.RAW) {
            if (mediaPlayerBuilder.ref.get() == null || (assetFileDescriptorOpenRawResourceFd = ((Context) mediaPlayerBuilder.ref.get()).getResources().openRawResourceFd(((Integer) mediaPlayerBuilder.obj).intValue())) == null) {
                return;
            }
            try {
                this.mediaPlayer.setDataSource(assetFileDescriptorOpenRawResourceFd.getFileDescriptor(), assetFileDescriptorOpenRawResourceFd.getStartOffset(), assetFileDescriptorOpenRawResourceFd.getLength());
                assetFileDescriptorOpenRawResourceFd.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        this.mediaPlayer.setAudioStreamType(3);
        this.mediaPlayer.setOnBufferingUpdateListener(this);
        this.mediaPlayer.setOnPreparedListener(this);
        this.mediaPlayer.setOnCompletionListener(this);
        this.builder = mediaPlayerBuilder;
    }

    private static void initialize() {
        if (manager == null) {
            manager = new AudioPlayManager();
        }
    }

    public static synchronized MediaPlayerBuilder obtainBuilder() {
        if (manager != null && manager.builder != null) {
            return manager.builder;
        }
        return newBuilder();
    }

    public static MediaPlayerBuilder newBuilder() {
        return new MediaPlayerBuilder();
    }

    public static class MediaPlayerBuilder {
        private boolean isLooping;
        private Object obj;
        private WeakReference<Context> ref;
        private int streamType;
        private TYPE type;

        public enum TYPE {
            RAW,
            ASSET,
            URI
        }

        public synchronized MediaPlayerBuilder loop() {
            this.isLooping = true;
            return this;
        }

        public synchronized MediaPlayerBuilder streamType(int i) {
            this.streamType = i;
            return this;
        }

        public synchronized MediaPlayerBuilder load(Context context, Object obj, TYPE type) {
            this.ref = new WeakReference<>(context);
            this.obj = obj;
            this.type = type;
            return this;
        }

        public synchronized MediaPlayerBuilder showTip() {
            Log.i(MediaPlayerBuilder.class.getSimpleName(), "raw use RAW, assets use ASSET, file/http/raw use URI");
            return this;
        }

        public synchronized AudioPlayManager build() {
            return AudioPlayManager.with(this);
        }
    }
}
