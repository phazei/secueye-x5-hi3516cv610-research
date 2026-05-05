package lvif;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.view.Surface;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iotx.linkvisual.media.audio.AudioParams;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.gms.safetynet.SafetyNetStatusCodes;
import java.nio.ByteBuffer;
import tools.G711Code;

/* JADX INFO: loaded from: classes4.dex */
public class lvdo implements lvnew {

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private MediaCodec f8024lvdo;

    /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
    private ByteBuffer[] f8025lvfor;

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    private ByteBuffer[] f8026lvif;

    /* JADX INFO: renamed from: lvint, reason: collision with root package name */
    private AudioParams f8027lvint;

    private lvdo(AudioParams audioParams) {
        if (audioParams.getAudioType() != 2) {
            throw new UnsupportedOperationException("Only support AACObjectLC");
        }
        this.f8027lvint = audioParams;
        try {
            MediaCodec mediaCodecCreateDecoderByType = MediaCodec.createDecoderByType(MimeTypes.AUDIO_AAC);
            this.f8024lvdo = mediaCodecCreateDecoderByType;
            mediaCodecCreateDecoderByType.configure(lvdo(2, audioParams.getSampleRate(), audioParams.getChannelCount()), (Surface) null, (MediaCrypto) null, 0);
            this.f8024lvdo.start();
            this.f8026lvif = this.f8024lvdo.getInputBuffers();
            this.f8025lvfor = this.f8024lvdo.getOutputBuffers();
        } catch (Exception e) {
            e.printStackTrace();
            lvfor();
        }
    }

    private MediaFormat lvdo(int i, int i2, int i3) {
        MediaFormat mediaFormatCreateAudioFormat = MediaFormat.createAudioFormat(MimeTypes.AUDIO_AAC, i2, i3);
        mediaFormatCreateAudioFormat.setInteger("is-adts", 0);
        int[] iArr = {96000, 88200, 64000, 48000, G711Code.SAMPLE_RATE_INHZ, 32000, 24000, 22050, G711Code.SAMPLE_RATE_INHZ_16000, SafetyNetStatusCodes.SAFE_BROWSING_UNSUPPORTED_THREAT_TYPES, 11025, 8000};
        int i4 = -1;
        for (int i5 = 0; i5 < 12; i5++) {
            if (iArr[i5] == i2) {
                ALog.d("AACDecoder", "[" + hashCode() + "] kSamplingFreq " + iArr[i5] + " i : " + i5);
                i4 = i5;
            }
        }
        if (i4 == -1) {
            return null;
        }
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(2);
        byteBufferAllocate.put((byte) ((i << 3) | (i4 >> 1)));
        byteBufferAllocate.position(1);
        byteBufferAllocate.put((byte) (((byte) ((i4 << 7) & 128)) | (i3 << 3)));
        byteBufferAllocate.flip();
        mediaFormatCreateAudioFormat.setByteBuffer("csd-0", byteBufferAllocate);
        for (int i6 = 0; i6 < byteBufferAllocate.capacity(); i6++) {
            ALog.i("AACDecoder", "[" + hashCode() + "] csd : " + ((int) byteBufferAllocate.array()[i6]));
        }
        return mediaFormatCreateAudioFormat;
    }

    public static synchronized lvdo lvdo(AudioParams audioParams) {
        return new lvdo(audioParams);
    }

    @Override // lvif.lvnew
    public String lvdo() {
        return "AACDecoder";
    }

    @Override // lvif.lvnew
    public byte[] lvdo(byte[] bArr, int i, int i2) {
        throw new UnsupportedOperationException("not support encode");
    }

    @Override // lvif.lvnew
    public synchronized void lvfor() {
        MediaCodec mediaCodec = this.f8024lvdo;
        if (mediaCodec != null) {
            try {
                try {
                    mediaCodec.stop();
                    this.f8024lvdo.release();
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
        MediaCodec mediaCodec = this.f8024lvdo;
        byte[] bArr2 = null;
        if (mediaCodec == null) {
            return null;
        }
        long j = 2000;
        try {
            int iDequeueInputBuffer = mediaCodec.dequeueInputBuffer(j);
            if (iDequeueInputBuffer >= 0) {
                ByteBuffer byteBuffer = this.f8026lvif[iDequeueInputBuffer];
                byteBuffer.clear();
                byteBuffer.put(bArr, i, i2);
                this.f8024lvdo.queueInputBuffer(iDequeueInputBuffer, 0, i2, 0L, 0);
            }
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int iDequeueOutputBuffer = this.f8024lvdo.dequeueOutputBuffer(bufferInfo, j);
            switch (iDequeueOutputBuffer) {
                case -3:
                    ALog.d("AACDecoder", "[" + hashCode() + "] INFO_OUTPUT_BUFFERS_CHANGED");
                    this.f8025lvfor = this.f8024lvdo.getOutputBuffers();
                    break;
                case -2:
                    ALog.d("AACDecoder", "[" + hashCode() + "] NEW FORMAT " + this.f8024lvdo.getOutputFormat());
                    lvif();
                    break;
                case -1:
                    ALog.d("AACDecoder", "[" + hashCode() + "] dequeueOutputBuffer timed out!");
                    break;
                default:
                    ByteBuffer byteBuffer2 = this.f8025lvfor[iDequeueOutputBuffer];
                    bArr2 = new byte[bufferInfo.size];
                    byteBuffer2.get(bArr2);
                    byteBuffer2.clear();
                    this.f8024lvdo.releaseOutputBuffer(iDequeueOutputBuffer, false);
                    break;
            }
            if ((bufferInfo.flags & 4) != 0) {
                ALog.d("AACDecoder", "[" + hashCode() + "] OutputBuffer BUFFER_FLAG_END_OF_STREAM");
            }
        } catch (Exception e) {
            e.printStackTrace();
            lvfor();
        }
        return bArr2;
    }
}
