/*
 * rtsp_push.h -- Thin RTSP streaming wrapper for pipeline_test
 *
 * Uses the SDK's xop RTSP library (libxoprtsp.a) via its C API
 * (rtsp_server_api.h) to serve a live H.265 stream from VENC output.
 *
 * Usage from pipeline_test.c:
 *   1. After VENC is started, call rtsp_start()
 *   2. In the VENC get_stream loop, call rtsp_push_venc_stream()
 *   3. On shutdown, call rtsp_stop()
 *
 * Clients connect to: rtsp://<camera_ip>:554/live0
 */

#ifndef RTSP_PUSH_H
#define RTSP_PUSH_H

#include "ot_common_venc.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * rtsp_start - Initialize RTSP server and create H.265 session
 *
 * @bind_ip:  IP address to bind (e.g. "192.168.1.153"), or NULL for auto
 * @port:     RTSP port (typically 554; use 8554 if 554 is in use)
 *
 * Returns 0 on success, -1 on failure.
 * Stream URL will be: rtsp://<bind_ip>:<port>/live0
 */
int rtsp_start(const char *bind_ip, int port);

/*
 * rtsp_push_venc_stream - Push one VENC frame to connected RTSP clients
 *
 * Call this for each frame returned by ss_mpi_venc_get_stream().
 * Iterates over all packs, skips SEI NALUs, detects keyframes,
 * and pushes each NALU to the xop RTSP library.
 *
 * @stream:  The VENC stream obtained from ss_mpi_venc_get_stream()
 *
 * Returns number of NALUs pushed, or -1 on error.
 */
int rtsp_push_venc_stream(const ot_venc_stream *stream);

/*
 * rtsp_stop - Shut down RTSP server and clean up
 */
void rtsp_stop(void);

/*
 * rtsp_is_running - Check if RTSP server is active
 *
 * Returns 1 if running, 0 if not.
 */
int rtsp_is_running(void);

#ifdef __cplusplus
}
#endif

#endif /* RTSP_PUSH_H */
