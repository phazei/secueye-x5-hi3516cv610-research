package bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class PresetPositionBean {
    private String iotId;
    private List<bitmapBean> list = new ArrayList();
    private HashSet<String> hashSet = new HashSet<>();

    public String getIotId() {
        return this.iotId;
    }

    public void setIotId(String str) {
        this.iotId = str;
    }

    public List<bitmapBean> getList() {
        return this.list;
    }

    public void setList(List<bitmapBean> list) {
        this.list = list;
    }

    public HashSet<String> getHashSet() {
        return this.hashSet;
    }

    public void setHashSet(HashSet<String> hashSet) {
        this.hashSet = hashSet;
    }

    public static class bitmapBean {
        private String bitmap;
        private int presetPositionNum;

        public String getBitmap() {
            return this.bitmap;
        }

        public void setBitmap(String str) {
            this.bitmap = str;
        }

        public int getPresetPositionNum() {
            return this.presetPositionNum;
        }

        public void setPresetPositionNum(int i) {
            this.presetPositionNum = i;
        }
    }
}
