package tools;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;

/* JADX INFO: loaded from: classes4.dex */
public class MediaUtil {
    private static MediaPlayer mMediaPlayer;

    public static void playRing(Context context) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(context, RingtoneManager.getDefaultUri(1));
            mMediaPlayer.setAudioStreamType(2);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopRing() {
        MediaPlayer mediaPlayer = mMediaPlayer;
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                mMediaPlayer.release();
                mMediaPlayer = null;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }
}
