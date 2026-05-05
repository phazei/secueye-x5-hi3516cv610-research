package lvif;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.view.Surface;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iotx.linkvisual.media.audio.AudioParams;
import com.google.android.exoplayer2.util.MimeTypes;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes4.dex */
public class lvif implements lvnew {

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private MediaCodec f8031lvdo;

    /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
    private ByteBuffer[] f8032lvfor;

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    private ByteBuffer[] f8033lvif;

    /* JADX INFO: renamed from: lvint, reason: collision with root package name */
    private AudioParams f8034lvint;

    private lvif(AudioParams audioParams) {
        if (audioParams.getAudioType() != 2) {
            throw new UnsupportedOperationException("Only support AACObjectLC");
        }
        this.f8034lvint = audioParams;
        try {
            MediaCodec mediaCodecCreateEncoderByType = MediaCodec.createEncoderByType(MimeTypes.AUDIO_AAC);
            this.f8031lvdo = mediaCodecCreateEncoderByType;
            mediaCodecCreateEncoderByType.configure(lvdo(2, audioParams.getSampleRate(), audioParams.getChannelCount()), (Surface) null, (MediaCrypto) null, 1);
            this.f8031lvdo.start();
            this.f8033lvif = this.f8031lvdo.getInputBuffers();
            this.f8032lvfor = this.f8031lvdo.getOutputBuffers();
        } catch (Exception e) {
            e.printStackTrace();
            lvfor();
        }
    }

    private MediaFormat lvdo(int i, int i2, int i3) {
        MediaFormat mediaFormatCreateAudioFormat = MediaFormat.createAudioFormat(MimeTypes.AUDIO_AAC, i2, i3);
        mediaFormatCreateAudioFormat.setInteger("aac-profile", 2);
        mediaFormatCreateAudioFormat.setInteger("bitrate", 48000);
        mediaFormatCreateAudioFormat.setInteger("max-input-size", 4096);
        return mediaFormatCreateAudioFormat;
    }

    public static synchronized lvif lvdo(AudioParams audioParams) {
        return new lvif(audioParams);
    }

    @Override // lvif.lvnew
    public String lvdo() {
        return "AACEncoder";
    }

    @Override // lvif.lvnew
    public byte[] lvdo(byte[] bArr, int i, int i2) {
        ALog.d("AACEncoder", "[" + hashCode() + "] offset:" + i + " len:" + i2);
        MediaCodec mediaCodec = this.f8031lvdo;
        if (mediaCodec == null) {
            return null;
        }
        try {
            int iDequeueInputBuffer = mediaCodec.dequeueInputBuffer(2000);
            if (iDequeueInputBuffer >= 0) {
                ByteBuffer byteBuffer = this.f8033lvif[iDequeueInputBuffer];
                byteBuffer.clear();
                byteBuffer.put(bArr, i, i2);
                this.f8031lvdo.queueInputBuffer(iDequeueInputBuffer, 0, i2, 0L, 0);
            }
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int iDequeueOutputBuffer = this.f8031lvdo.dequeueOutputBuffer(bufferInfo, 0L);
            switch (iDequeueOutputBuffer) {
                case -3:
                    ALog.d("AACEncoder", "[" + hashCode() + "] INFO_OUTPUT_BUFFERS_CHANGED");
                    this.f8032lvfor = this.f8031lvdo.getOutputBuffers();
                    break;
                case -2:
                    ALog.d("AACEncoder", "[" + hashCode() + "] NEW FORMAT " + this.f8031lvdo.getOutputFormat());
                    lvif();
                    break;
                case -1:
                    ALog.d("AACEncoder", "[" + hashCode() + "] dequeueOutputBuffer timed out!");
                    break;
                default:
                    ByteBuffer byteBuffer2 = this.f8032lvfor[iDequeueOutputBuffer];
                    byte[] bArr2 = new byte[bufferInfo.size];
                    byteBuffer2.get(bArr2);
                    byteBuffer2.clear();
                    this.f8031lvdo.releaseOutputBuffer(iDequeueOutputBuffer, false);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            lvfor();
            return null;
        }
        return null;
    }

    @Override // lvif.lvnew
    public synchronized void lvfor() {
        MediaCodec mediaCodec = this.f8031lvdo;
        if (mediaCodec != null) {
            try {
                try {
                    mediaCodec.stop();
                    this.f8031lvdo.release();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            } finally {
            }
        }
    }

    @Override // lvif.lvnew
    public void lvif() {
    }

    @Override // lvif.lvnew
    public byte[] lvif(byte[] bArr, int i, int i2) {
        throw new UnsupportedOperationException("not support decode");
    }
}
