package com.aliyun.iotx.linkvisual.media.audio.utils;

import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iotx.linkvisual.media.audio.AudioParams;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/* JADX INFO: loaded from: classes2.dex */
public class WavFileWriter {
    public static final String TAG = "linksdk_lv_WavFileWriter";
    private AudioParams audioParams;
    private RandomAccessFile file;

    private WavFileWriter(String str, AudioParams audioParams) throws IOException {
        this.audioParams = audioParams;
        File file = new File(str);
        if (file.exists()) {
            file.delete();
        } else {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
        }
        file.createNewFile();
        this.file = new RandomAccessFile(file, "rw");
        writeWaveHeader();
    }

    public static WavFileWriter create(String str, AudioParams audioParams) throws IOException {
        return new WavFileWriter(str, audioParams);
    }

    private void writeWaveHeader() throws IOException {
        this.file.writeBytes("RIFF");
        this.file.writeInt(0);
        this.file.writeBytes("WAVE");
        this.file.writeBytes("fmt ");
        this.file.writeInt(Integer.reverseBytes(16));
        this.file.writeShort(Short.reverseBytes((short) (this.audioParams.getAudioType() == 1 ? 6 : 1)));
        this.file.writeShort(Short.reverseBytes((short) this.audioParams.getChannelCount()));
        this.file.writeInt(Integer.reverseBytes(this.audioParams.getSampleRate()));
        this.file.writeInt(Integer.reverseBytes(this.audioParams.getChannelCount() * this.audioParams.getSampleRate() * (this.audioParams.getBitsPerSample() / 8)));
        this.file.writeShort(Short.reverseBytes((short) (this.audioParams.getChannelCount() * (this.audioParams.getBitsPerSample() / 8))));
        this.file.writeShort(Short.reverseBytes((short) this.audioParams.getBitsPerSample()));
        this.file.writeBytes("data");
        this.file.writeInt(0);
    }

    public void close() throws IOException {
        try {
            this.file.seek(4L);
            RandomAccessFile randomAccessFile = this.file;
            randomAccessFile.writeInt(Integer.reverseBytes((int) (randomAccessFile.length() - 8)));
            this.file.seek(40L);
            RandomAccessFile randomAccessFile2 = this.file;
            randomAccessFile2.writeInt(Integer.reverseBytes((int) (randomAccessFile2.length() - 44)));
            ALog.d(TAG, "wav size: " + this.file.length());
        } finally {
            this.file.close();
        }
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        this.file.write(bArr, i, i2);
    }
}
