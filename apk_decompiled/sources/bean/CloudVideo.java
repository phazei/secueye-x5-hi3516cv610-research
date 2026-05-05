package bean;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class CloudVideo {
    public String beginTime;
    private int nextBeginTime;
    private boolean nextValid;
    private List<RecordFileListBean> recordFileList = new ArrayList();

    public boolean isNextValid() {
        return this.nextValid;
    }

    public void setNextValid(boolean z) {
        this.nextValid = z;
    }

    public int getNextBeginTime() {
        return this.nextBeginTime;
    }

    public void setNextBeginTime(int i) {
        this.nextBeginTime = i;
    }

    public List<RecordFileListBean> getRecordFileList() {
        return this.recordFileList;
    }

    public void setRecordFileList(List<RecordFileListBean> list) {
        this.recordFileList = list;
    }

    public static class RecordFileListBean {
        private String beginTime;
        private String beginTimeUTC;
        private String endTime;
        private String endTimeUTC;
        private String fileName;
        private int fileSize;
        private String intelligentTypeList;
        private boolean isSelected;
        private int recordType;
        private String snapshotUrl;
        private int streamType;

        public String getFileName() {
            return this.fileName;
        }

        public void setFileName(String str) {
            this.fileName = str;
        }

        public int getStreamType() {
            return this.streamType;
        }

        public void setStreamType(int i) {
            this.streamType = i;
        }

        public int getFileSize() {
            return this.fileSize;
        }

        public void setFileSize(int i) {
            this.fileSize = i;
        }

        public int getRecordType() {
            return this.recordType;
        }

        public void setRecordType(int i) {
            this.recordType = i;
        }

        public String getIntelligentTypeList() {
            return this.intelligentTypeList;
        }

        public void setIntelligentTypeList(String str) {
            this.intelligentTypeList = str;
        }

        public String getBeginTime() {
            return this.beginTime;
        }

        public void setBeginTime(String str) {
            this.beginTime = str;
        }

        public String getEndTime() {
            return this.endTime;
        }

        public void setEndTime(String str) {
            this.endTime = str;
        }

        public String getBeginTimeUTC() {
            return this.beginTimeUTC;
        }

        public void setBeginTimeUTC(String str) {
            this.beginTimeUTC = str;
        }

        public String getEndTimeUTC() {
            return this.endTimeUTC;
        }

        public void setEndTimeUTC(String str) {
            this.endTimeUTC = str;
        }

        public String getSnapshotUrl() {
            return this.snapshotUrl;
        }

        public void setSnapshotUrl(String str) {
            this.snapshotUrl = str;
        }

        public boolean getIsSelected() {
            return this.isSelected;
        }

        public void setIsSelected(boolean z) {
            this.isSelected = z;
        }
    }
}
