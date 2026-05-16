/*
 * rtsp_push.c -- VENC/AENC-to-RTSP bridge using SDK's xop library
 *
 * Wraps the rtsp_server_api.h C API into a convenient interface
 * for pipeline_test.c.  Video NALUs and audio frames are pushed
 * to the xop library, which handles RTP packetization, SDP generation,
 * and client session management internally.
 *
 * Audio: G.711A (PCMA), 8kHz, mono -- matching the camera's internal
 * audio codec and superb's RTSP audio format.
 */

#include "rtsp_push.h"

#include <stdio.h>
#include <string.h>
#include <stdint.h>

/* SDK's xop RTSP C API */
#include "rtsp_server_api.h"

/* Session index (0 = live0, the primary stream) */
#define RTSP_SESSION_IDX  0

static int g_rtsp_running = 0;
static int g_rtsp_has_audio = 0;

static int rtsp_start_internal(const char *bind_ip, int port, int with_audio)
{
    int ret;

    if (g_rtsp_running) {
        printf("[RTSP] Already running\n");
        return 0;
    }

    if (bind_ip == NULL) {
        bind_ip = "0.0.0.0";
    }
    if (port <= 0) {
        port = 554;
    }

    printf("[RTSP] Starting server on %s:%d (audio=%s)...\n",
           bind_ip, port, with_audio ? "yes" : "no");

    ret = rtsp_server_start(bind_ip, port);
    if (!ret) {
        printf("[FAIL] rtsp_server_start returned %d\n", ret);
        return -1;
    }

    /* Create session: video-only or video+audio */
    if (with_audio) {
        ret = rtsp_session_create_with_audio(RTSP_SESSION_IDX, /*is_h265=*/1);
    } else {
        ret = rtsp_session_create(RTSP_SESSION_IDX, /*is_h265=*/1);
    }
    if (!ret) {
        printf("[FAIL] rtsp_session_create returned %d\n", ret);
        rtsp_server_stop();
        return -1;
    }

    g_rtsp_running = 1;
    g_rtsp_has_audio = with_audio;
    printf("[ OK ] RTSP server ready: rtsp://%s:%d/live0%s\n",
           bind_ip, port, with_audio ? " (video+audio)" : " (video only)");
    return 0;
}

int rtsp_start(const char *bind_ip, int port)
{
    return rtsp_start_internal(bind_ip, port, 0);
}

int rtsp_start_with_audio(const char *bind_ip, int port)
{
    return rtsp_start_internal(bind_ip, port, 1);
}

int rtsp_push_venc_stream(const ot_venc_stream *stream)
{
    unsigned int i;
    int pushed = 0;

    if (!g_rtsp_running || stream == NULL) {
        return -1;
    }

    for (i = 0; i < stream->pack_cnt; i++) {
        uint8_t *data;
        uint32_t len;
        int is_key;

        /* Skip SEI NALUs -- xop doesn't need them and they waste bandwidth */
        if (stream->pack[i].data_type.h265_type == OT_VENC_H265_NALU_SEI) {
            continue;
        }

        data = (uint8_t *)stream->pack[i].addr + stream->pack[i].offset;
        len  = stream->pack[i].len - stream->pack[i].offset;

        /* Detect keyframe (IDR or I-slice) */
        is_key = 0;
        if (stream->pack[i].data_type.h265_type == OT_VENC_H265_NALU_IDR_SLICE ||
            stream->pack[i].data_type.h265_type == OT_VENC_H265_NALU_I_SLICE) {
            is_key = 1;
        }

        rtsp_session_push_frame(RTSP_SESSION_IDX, data, len, is_key);
        pushed++;
    }

    return pushed;
}

int rtsp_push_audio_stream(const unsigned char *data, unsigned int len)
{
    if (!g_rtsp_running || !g_rtsp_has_audio || data == NULL || len == 0) {
        return -1;
    }

    rtsp_session_push_audio(RTSP_SESSION_IDX, (uint8_t *)data, len);
    return 0;
}

void rtsp_stop(void)
{
    if (!g_rtsp_running) {
        return;
    }

    printf("[RTSP] Stopping server...\n");
    rtsp_server_stop();
    g_rtsp_running = 0;
    g_rtsp_has_audio = 0;
    printf("[ OK ] RTSP server stopped\n");
}

int rtsp_is_running(void)
{
    return g_rtsp_running;
}
