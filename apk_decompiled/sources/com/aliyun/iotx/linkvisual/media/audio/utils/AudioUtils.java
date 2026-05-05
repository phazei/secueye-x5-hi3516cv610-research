package com.aliyun.iotx.linkvisual.media.audio.utils;

import aisble.data.MutableData;
import android.bluetooth.BluetoothAdapter;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iotx.linkvisual.media.audio.AudioParams;
import java.util.Iterator;
import java.util.Queue;

/* JADX INFO: loaded from: classes2.dex */
public class AudioUtils {
    public static final String TAG = "linksdk_lv_AudioUtils";

    public static void addADTStoPacket(byte[] bArr, int i) {
        bArr[0] = -1;
        bArr[1] = -7;
        bArr[2] = (byte) 96;
        bArr[3] = (byte) ((i >> 11) + 64);
        bArr[4] = (byte) ((i & MutableData.SFLOAT_NAN) >> 3);
        bArr[5] = (byte) (((i & 7) << 5) + 31);
        bArr[6] = -4;
    }

    public static int calculateAudioDataSizeInByte(int i, AudioParams audioParams) {
        int audioType = audioParams.getAudioType();
        if (audioType == 0) {
            return ((((audioParams.getChannelCount() * audioParams.getSampleRate()) * audioParams.getBitsPerSample()) / 8) / 1000) * i;
        }
        if (audioType == 1) {
            return (((((audioParams.getChannelCount() * audioParams.getSampleRate()) * audioParams.getBitsPerSample()) / 8) / 1000) * i) / 2;
        }
        if (audioType == 2) {
            return (((((audioParams.getChannelCount() * audioParams.getSampleRate()) * audioParams.getBitsPerSample()) / 8) / 1000) * i) / 8;
        }
        throw new RuntimeException("不支持的音频格式");
    }

    public static int calculateAudioTrackBuffer(AudioParams audioParams) {
        int minBufferSize = AudioTrack.getMinBufferSize(audioParams.getSampleRate(), getChannelOutConfig(audioParams.getChannelCount()), audioParams.getAudioEncoding());
        ALog.i(TAG, "AudioTrack minBufferSize: " + minBufferSize + "\t usingSize:" + minBufferSize);
        return minBufferSize;
    }

    public static int calculateCumulativeDelayTimeInMs(Queue<byte[]> queue, AudioParams audioParams) {
        Iterator<byte[]> it = queue.iterator();
        int length = 0;
        while (it.hasNext()) {
            length += it.next().length;
        }
        return (((length * 1000) / audioParams.getSampleRate()) / audioParams.getChannelCount()) / (audioParams.getBitsPerSample() / 8);
    }

    public static int getChannelInConfig(int i) {
        return i != 2 ? 1 : 12;
    }

    public static int getChannelOutConfig(int i) {
        return i != 2 ? 4 : 12;
    }

    public static boolean isBlueToothHeadsetConnected() {
        try {
            return BluetoothAdapter.getDefaultAdapter().getProfileConnectionState(1) != 0;
        } catch (Exception unused) {
            return true;
        }
    }

    public static boolean isBtHeadsetScoOn(AudioManager audioManager) {
        if (Build.VERSION.SDK_INT < 23) {
            return audioManager.isBluetoothScoOn();
        }
        for (AudioDeviceInfo audioDeviceInfo : audioManager.getDevices(2)) {
            if (audioDeviceInfo.getType() == 7) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWiredHeadsetOn(AudioManager audioManager) {
        if (Build.VERSION.SDK_INT < 23) {
            return audioManager.isWiredHeadsetOn();
        }
        for (AudioDeviceInfo audioDeviceInfo : audioManager.getDevices(2)) {
            if (audioDeviceInfo.getType() == 3 || audioDeviceInfo.getType() == 4) {
                return true;
            }
        }
        return false;
    }

    public static void useSpeaker(AudioManager audioManager) {
        audioManager.stopBluetoothSco();
        audioManager.setBluetoothScoOn(false);
        audioManager.setSpeakerphoneOn(true);
    }

    public static void useWiredHeadset(AudioManager audioManager) {
        audioManager.stopBluetoothSco();
        audioManager.setBluetoothScoOn(false);
        audioManager.setSpeakerphoneOn(false);
    }
}
