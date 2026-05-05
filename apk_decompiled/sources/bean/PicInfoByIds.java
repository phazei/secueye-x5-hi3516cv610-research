package bean;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class PicInfoByIds {
    public List<Picture> pictureList = new ArrayList();

    public static class Picture {
        public String iotId;
        public String pictureId;
        public String pictureTime;
        public String pictureTimeUTC;
        public String pictureUrl;
        public String thumbUrl;
    }
}
