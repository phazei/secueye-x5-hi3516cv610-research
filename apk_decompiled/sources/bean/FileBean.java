package bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class FileBean implements MultiItemEntity {
    public static final int PHOTO = 1;
    public static final int PIC = 1;
    public static final int TEXT = 0;
    public static final int VIDEO = 2;
    long createTime;
    int day;
    boolean isEdit;
    boolean isSelected;
    int itemType;
    int mediaType;
    int month;
    String name;
    public FileBean parent;
    long size;
    String url;
    int year;
    public ArrayList<FileBean> photos = new ArrayList<>();
    public ArrayList<FileBean> videos = new ArrayList<>();

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int i) {
        this.year = i;
    }

    public int getMonth() {
        return this.month;
    }

    public void setMonth(int i) {
        this.month = i;
    }

    public int getDay() {
        return this.day;
    }

    public void setDay(int i) {
        this.day = i;
    }

    public int getMediaType() {
        return this.mediaType;
    }

    public void setMediaType(int i) {
        this.mediaType = i;
    }

    public boolean isEdit() {
        return this.isEdit;
    }

    public void setEdit(boolean z) {
        this.isEdit = z;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long j) {
        this.createTime = j;
    }

    public void setItemType(int i) {
        this.itemType = i;
    }

    @Override // com.chad.library.adapter.base.entity.MultiItemEntity
    public int getItemType() {
        return this.itemType;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long j) {
        this.size = j;
    }
}
