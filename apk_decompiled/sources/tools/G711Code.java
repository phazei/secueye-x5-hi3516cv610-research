package tools;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import io.netty.handler.codec.http2.Http2CodecUtil;
import java.io.File;
import java.util.Objects;
import kotlin.jvm.internal.ShortCompanionObject;

/* JADX INFO: loaded from: classes4.dex */
public class G711Code {
    public static final int AUDIO_FORMAT = 2;
    public static final int CHANNEL_CONFIG = 16;
    private static final int QUANT_MASK = 15;
    public static final int SAMPLE_RATE_INHZ = 44100;
    public static final int SAMPLE_RATE_INHZ_16000 = 16000;
    public static final int SAMPLE_RATE_INHZ_8000 = 8000;
    private static final int SEG_MASK = 112;
    private static final int SEG_SHIFT = 4;
    private static final int SIGN_BIT = 128;
    static short[] seg_end = {Http2CodecUtil.MAX_UNSIGNED_BYTE, 511, 1023, 2047, 4095, 8191, 16383, ShortCompanionObject.MAX_VALUE};

    static short alaw2linear(byte b2) {
        short s;
        byte b3 = (byte) (b2 ^ 85);
        short s2 = (short) ((b3 & 15) << 4);
        short s3 = (short) ((b3 & 112) >> 4);
        switch (s3) {
            case 0:
                s = (short) (s2 + 8);
                break;
            case 1:
                s = (short) (s2 + 264);
                break;
            default:
                s = (short) (((short) (s2 + 264)) << (s3 - 1));
                break;
        }
        return (b3 & 128) != 0 ? s : (short) (-s);
    }

    static short search(short s, short[] sArr, short s2) {
        for (short s3 = 0; s3 < s2; s3 = (short) (s3 + 1)) {
            if (s <= sArr[s3]) {
                return s3;
            }
        }
        return s2;
    }

    static byte linear2alaw(short s) {
        char c2;
        if (s >= 0) {
            c2 = 213;
        } else {
            c2 = 'U';
            s = (short) ((-s) - 1);
            if (s < 0) {
                s = ShortCompanionObject.MAX_VALUE;
            }
        }
        short sSearch = search(s, seg_end, (short) 8);
        if (sSearch >= 8) {
            return (byte) (c2 ^ 127);
        }
        char c3 = (char) (sSearch << 4);
        return (byte) ((sSearch < 2 ? (char) (((s >> 4) & 15) | c3) : (char) (((s >> (sSearch + 3)) & 15) | c3)) ^ c2);
    }

    public static void G711aEncoder(short[] sArr, byte[] bArr, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            bArr[i2] = linear2alaw(sArr[i2]);
            Log.e("-------------", "数据编码");
        }
    }

    public static void G711aDecoder(short[] sArr, byte[] bArr, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            sArr[i2] = alaw2linear(bArr[i2]);
        }
    }

    public static String getFilesPath(Context context) {
        String path;
        if ("mounted".equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            path = ((File) Objects.requireNonNull(context.getExternalFilesDir(null))).getPath();
        } else {
            path = context.getFilesDir().getPath();
        }
        return path + "/" + Utils.getUserPhone();
    }
}
