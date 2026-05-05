package bean;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AlertInfoBean {
    private int code;
    private DataBean data;
    private String id;

    public int getCode() {
        return this.code;
    }

    public void setCode(int i) {
        this.code = i;
    }

    public DataBean getData() {
        return this.data;
    }

    public void setData(DataBean dataBean) {
        this.data = dataBean;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public static class DataBean {
        private List<FileListBean> FileList;

        public List<FileListBean> getFileList() {
            return this.FileList;
        }

        public void setFileList(List<FileListBean> list) {
            this.FileList = list;
        }

        public static class FileListBean {
            private int BeginTime;
            private int EndTime;
            private String FileName;
            private int FileType;
            private int Size;

            public int getEndTime() {
                return this.EndTime;
            }

            public void setEndTime(int i) {
                this.EndTime = i;
            }

            public int getFileType() {
                return this.FileType;
            }

            public void setFileType(int i) {
                this.FileType = i;
            }

            public int getSize() {
                return this.Size;
            }

            public void setSize(int i) {
                this.Size = i;
            }

            public String getFileName() {
                return this.FileName;
            }

            public void setFileName(String str) {
                this.FileName = str;
            }

            public int getBeginTime() {
                return this.BeginTime;
            }

            public void setBeginTime(int i) {
                this.BeginTime = i;
            }
        }
    }
}
