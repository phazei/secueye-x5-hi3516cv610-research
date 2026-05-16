#ifndef RTSP_MANAGER_H
#define RTSP_MANAGER_H

#include "RtspServer.h"
#include "H264Source.h"
#include "H265Source.h"
#include "G711ASource.h"
#include "NetInterface.h"
#include <memory>
#include <mutex>
#include <string>
#include <array>
#include <cstdio>
#include <cstring>

#define MAX_RTSP_SESSION 8

class RtspManager {
public:
    static RtspManager& Instance();

    bool StartServer(const std::string& ip, int port);
    bool CreateSession(int index, bool is_h265);
    bool CreateSessionWithAudio(int index, bool is_h265);
    bool PushFrame(int index, uint8_t* data, size_t len, bool is_key);
    bool PushAudio(int index, uint8_t* data, size_t len);
    void StopServer();

private:
    RtspManager() = default;
    ~RtspManager() = default;

    std::string m_server_ip;
    int         m_port;

    struct SessionCtx {
        bool used = false;
        bool is_h265 = false;
        bool has_audio = false;
        xop::MediaSessionId session_id = 0;
    };

    uint32_t GetTimestamp(bool is_h265);

private:
    std::shared_ptr<xop::EventLoop>  m_loop;
    std::shared_ptr<xop::RtspServer> m_server;
    std::array<SessionCtx, MAX_RTSP_SESSION> m_sessions;
    std::mutex m_mutex;
};

#endif /* RTSP_MANAGER_H */