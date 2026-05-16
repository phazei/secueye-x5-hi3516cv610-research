#ifndef RTSP_SERVER_API_H
#define RTSP_SERVER_API_H

#include <stdint.h>
#include <stddef.h>

#ifdef __cplusplus
extern "C" {
#endif

/* Initialize RTSP Server. Returns 1 success, 0 failure */
int rtsp_server_start(const char* ip, int port);

/* Create session with video only.
 * index: 0~7  -> live0 ~ live7
 * is_h265: 1 = H265, 0 = H264
 */
int rtsp_session_create(int index, int is_h265);

/* Create session with video + G.711A audio.
 * index: 0~7  -> live0 ~ live7
 * is_h265: 1 = H265, 0 = H264
 */
int rtsp_session_create_with_audio(int index, int is_h265);

/* Push one video frame. is_key: 1 = IDR/I-frame, 0 = P-frame */
int rtsp_session_push_frame(int index, uint8_t* data, size_t len, int is_key);

/* Push one audio frame (G.711A encoded, 8kHz mono).
 * data: G.711A encoded audio samples (no extra headers)
 * len:  number of bytes (= number of samples for G.711A)
 */
int rtsp_session_push_audio(int index, uint8_t* data, size_t len);

/* Stop server (cleans up all sessions) */
void rtsp_server_stop(void);

#ifdef __cplusplus
}
#endif

#endif /* RTSP_SERVER_API_H */
