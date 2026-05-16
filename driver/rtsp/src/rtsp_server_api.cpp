#include "rtsp_server_api.h"
#include "rtsp_manager.h"

int rtsp_server_start(const char* ip, int port)
{
    if (!ip) {
        return 0;
    }
    return RtspManager::Instance().StartServer(ip, port) ? 1 : 0;
}

int rtsp_session_create(int index, int is_h265)
{
    return RtspManager::Instance().CreateSession(index, is_h265 != 0) ? 1 : 0;
}

int rtsp_session_create_with_audio(int index, int is_h265)
{
    return RtspManager::Instance().CreateSessionWithAudio(index, is_h265 != 0) ? 1 : 0;
}

int rtsp_session_push_frame(int index, uint8_t* data, size_t len, int is_key)
{
    return RtspManager::Instance().PushFrame(index, data, len, is_key != 0) ? 1 : 0;
}

int rtsp_session_push_audio(int index, uint8_t* data, size_t len)
{
    return RtspManager::Instance().PushAudio(index, data, len) ? 1 : 0;
}

void rtsp_server_stop(void)
{
    RtspManager::Instance().StopServer();
}
