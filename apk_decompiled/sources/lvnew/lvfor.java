package lvnew;

/* JADX INFO: loaded from: classes4.dex */
public enum lvfor {
    EVENT_DISCONNECT(0),
    EVENT_STREAM_ERROR(1),
    EVENT_P2P_CONNECT_OK(2),
    EVENT_RTMP_CONNECT_OK(3),
    EVENT_SEEK_COMPLETE(4),
    EVENT_RECEIVE_FIRST_K_FRAME(5),
    EVENT_VOD_COMPLETE(6),
    EVENT_TALK_READY(7),
    EVENT_SWITCH_TO_P2P_STREAM(8),
    EVENT_DEC_ERROR_FOR_MOMENT(9),
    EVENT_RECV_FIRST_VIDEO_PACKAET(10),
    EVENT_BUFFER_EMPTY_FOR_MOMENT(13),
    EVENT_BUFFERING_END(14),
    EVENT_ENCODE_CHANGE(15),
    EVENT_RESOLUTION_CHANGE(16),
    EVENT_P2P_CHANNEL_ERROR(17),
    EVENT_P2P_EVENT_TRACKING(18),
    EVENT_HLS_SLICE_DOWNLOAD_ERROR(19),
    EVENT_HLS_M3U8_DOWNLOAD_ERROR(20),
    EVENT_RELAY_STARTSTREAMING_TRACKINFO(21),
    EVENT_P2P_STARTSTREAMING_TRACKINFO(22),
    EVENT_HLS_STARTSTREAMING_TRACKINFO(23),
    EVENT_TRANSQUALITY_TRACKINFO(24),
    EVENT_REALPLAY_BUFFER_EMPTY(27),
    EVENT_NEED_REOPEN(29),
    EVENT_P2P_ERROR_TRACKING(30);


    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private int f8063lvdo;

    lvfor(int i) {
        this.f8063lvdo = i;
    }

    public static lvfor lvdo(int i) {
        for (lvfor lvforVar : values()) {
            if (lvforVar.f8063lvdo == i) {
                return lvforVar;
            }
        }
        return null;
    }
}
