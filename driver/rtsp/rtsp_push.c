/*
 * rtsp_push.c -- VENC-to-RTSP bridge using SDK's xop library
 *
 * Wraps the 4-function rtsp_server_api.h into a convenient interface
 * for pipeline_test.c.  Each VENC pack (NALU) is pushed individually
 * to the xop library, which handles RTP packetization, SDP generation,
 * and client session management internally.
 *
 * Reference: Hi3516CV610 SDK sample_comm_venc.c:sample_comm_push_frame_to_rtsp()
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

int rtsp_start(const char *bind_ip, int port)
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

    printf("[RTSP] Starting server on %s:%d ...\n", bind_ip, port);

    ret = rtsp_server_start(bind_ip, port);
    if (!ret) {
        printf("[FAIL] rtsp_server_start returned %d\n", ret);
        return -1;
    }

    /* Create H.265 session at index 0 -> URL: rtsp://ip:port/live0 */
    ret = rtsp_session_create(RTSP_SESSION_IDX, /*is_h265=*/1);
    if (!ret) {
        printf("[FAIL] rtsp_session_create returned %d\n", ret);
        rtsp_server_stop();
        return -1;
    }

    g_rtsp_running = 1;
    printf("[ OK ] RTSP server ready: rtsp://%s:%d/live0\n", bind_ip, port);
    return 0;
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

void rtsp_stop(void)
{
    if (!g_rtsp_running) {
        return;
    }

    printf("[RTSP] Stopping server...\n");
    rtsp_server_stop();
    g_rtsp_running = 0;
    printf("[ OK ] RTSP server stopped\n");
}

int rtsp_is_running(void)
{
    return g_rtsp_running;
}
