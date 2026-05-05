package com.aliyun.iotx.linkvisual.media;

import android.view.Surface;
import com.aliyun.iotx.linkvisual.media.audio.ILVOssUploadCallback;
import com.aliyun.iotx.linkvisual.media.video.ILvStreamCallback;
import com.aliyun.iotx.linkvisual.media.video.beans.FrameColor;
import com.aliyun.iotx.linkvisual.media.video.beans.PlayInfo;
import com.aliyun.iotx.linkvisual.media.video.beans.PreConnectType;
import com.aliyun.iotx.linkvisual.media.video.beans.Yuv420pFrame;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes2.dex */
public class LinkVisual {
    static {
        System.loadLibrary("linkvisual");
    }

    public static native int close_stream(int i);

    public static native void destroy_textureview_opengl(int i);

    public static native void draw_surfaceview_frame_externally(int i, ByteBuffer byteBuffer, int i2, int i3);

    public static native void draw_surfaceview_frame_internally(int i);

    public static native void draw_textureview_frame_externally(int i, int i2, ByteBuffer byteBuffer, int i3, int i4);

    public static native void draw_textureview_frame_internally(int i, int i2);

    public static native boolean frame_by_frame_stream(int i);

    public static native long get_current_duration(int i);

    public static native void get_current_play_info(int i, PlayInfo playInfo);

    public static native long get_current_recording_content_duration(int i);

    public static native int get_decoder_type(int i);

    public static native long get_duration(int i);

    public static native boolean get_stream_color(int i, FrameColor frameColor);

    public static native int get_stream_connect_type(int i);

    public static native boolean get_yuv420p_frame(Yuv420pFrame yuv420pFrame, ByteBuffer byteBuffer, int i);

    public static native boolean get_yuv420p_frame_data(int i, byte[] bArr, int i2, int i3);

    public static native void init_glsurfaceview_opengl();

    public static native void init_textureview_opengl(Surface surface, int i);

    public static native void native_close_oss_upload(long j);

    public static native int native_open_amr(String str, boolean z, ILvStreamCallback iLvStreamCallback, ByteBuffer byteBuffer, int i);

    public static native long native_oss_upload_amr(String str, String str2, String str3, String str4, ILVOssUploadCallback iLVOssUploadCallback);

    public static native long native_start_amr_writer(int i, int i2, int i3, String str);

    public static native boolean native_stop_writer(long j);

    public static native boolean native_write_pcm_to_amr(long j, byte[] bArr, int i);

    public static native void on_surfaceview_viewport_changed(int i, int i2, int i3, int i4);

    public static native void on_textureview_viewport_changed(int i, int i2, int i3, int i4, int i5);

    public static native int open_hls_stream(String str, long j, ILvStreamCallback iLvStreamCallback, ByteBuffer byteBuffer, int i);

    public static native int open_p2p_stream(String str, int i, int i2, boolean z, byte[] bArr, byte[] bArr2, int i3, String str2, String str3, String str4, int i4, String str5, String str6, ILvStreamCallback iLvStreamCallback, ByteBuffer byteBuffer, int i5, ByteBuffer byteBuffer2, int i6);

    public static native int open_rtmp_stream(String str, int i, boolean z, int i2, boolean z2, byte[] bArr, byte[] bArr2, ILvStreamCallback iLvStreamCallback, ByteBuffer byteBuffer, int i3, ByteBuffer byteBuffer2, int i4);

    public static native boolean pause_stream(int i, boolean z);

    public static native int pre_create_p2p(int i, int i2, String str, String str2, int i3, String str3, String str4);

    public static native int pre_create_stream(String str, int i, int i2, boolean z, boolean z2, String str2, boolean z3, byte[] bArr, byte[] bArr2, String str3, String str4, int i3, String str5, String str6);

    public static native int query_and_play(String str, int i, int i2, ILvStreamCallback iLvStreamCallback, ByteBuffer byteBuffer, int i3, ByteBuffer byteBuffer2, int i4);

    public static native int query_connected_channel(String str, int i, PreConnectType preConnectType);

    public static native int reopen_p2p_stream(int i, String str, int i2, boolean z, byte[] bArr, byte[] bArr2, boolean z2, String str2, String str3, int i3, String str4, String str5);

    public static native boolean seek_stream(int i, int i2);

    public static native boolean send_talk_data(int i, byte[] bArr, int i2, int i3, long j);

    public static native boolean set_decoder_strategy(int i);

    public static native boolean set_display_buffer_size(int i);

    public static native boolean set_log_level(int i);

    public static native void set_lv_dump_dir(String str);

    public static native boolean set_max_jitter_buffer_size_in_ms(int i);

    public static native boolean set_speed_rate(int i, int i2);

    public static native boolean set_stream_color(int i, int i2, int i3, int i4, int i5);

    public static native boolean set_talk_format(int i, int i2, int i3, int i4, int i5);

    public static native boolean snapshot_yuv_to_jpeg(int i, String str, ByteBuffer byteBuffer, int i2, int i3);

    public static native boolean start_convert_mp4(int i, String str);

    public static native boolean stop_convert_mp4(int i);

    public static native int stream_p2p_exit();

    public static native void stream_p2p_init();
}
