package anetwork.channel.cache;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public interface Cache {
    void clear();

    Entry get(String str);

    void put(String str, Entry entry);

    void remove(String str);

    /* JADX INFO: compiled from: Taobao */
    public static class Entry implements Serializable {
        public byte[] data;
        public String etag;
        public long lastModified;
        public Map<String, List<String>> responseHeaders = Collections.EMPTY_MAP;
        public long serverDate;
        public long ttl;

        public boolean isFresh() {
            return System.currentTimeMillis() <= this.ttl;
        }
    }
}
