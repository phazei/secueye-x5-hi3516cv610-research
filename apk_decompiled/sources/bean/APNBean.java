package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class APNBean implements Serializable {
    List<bean> list = new ArrayList();

    public List<bean> getList() {
        return this.list;
    }

    public void setList(List<bean> list) {
        this.list = list;
    }

    public static class bean implements Serializable {
        private String APN;
        private int AuthType;
        private String Carrier;
        private int Enable;
        private String MCC;
        private String MNC;
        private String Password;
        private int Port;
        private int Protocol;
        private String Proxy;
        private String Server;
        private String Type;
        private String User;

        public int getAuthType() {
            return this.AuthType;
        }

        public void setAuthType(int i) {
            this.AuthType = i;
        }

        public String getType() {
            return this.Type;
        }

        public void setType(String str) {
            this.Type = str;
        }

        public int getProtocol() {
            return this.Protocol;
        }

        public void setProtocol(int i) {
            this.Protocol = i;
        }

        public int getEnable() {
            return this.Enable;
        }

        public void setEnable(int i) {
            this.Enable = i;
        }

        public String getCarrier() {
            return this.Carrier;
        }

        public void setCarrier(String str) {
            this.Carrier = str;
        }

        public String getMCC() {
            return this.MCC;
        }

        public void setMCC(String str) {
            this.MCC = str;
        }

        public String getMNC() {
            return this.MNC;
        }

        public void setMNC(String str) {
            this.MNC = str;
        }

        public String getAPN() {
            return this.APN;
        }

        public void setAPN(String str) {
            this.APN = str;
        }

        public String getServer() {
            return this.Server;
        }

        public void setServer(String str) {
            this.Server = str;
        }

        public String getProxy() {
            return this.Proxy;
        }

        public void setProxy(String str) {
            this.Proxy = str;
        }

        public int getPort() {
            return this.Port;
        }

        public void setPort(int i) {
            this.Port = i;
        }

        public String getUser() {
            return this.User;
        }

        public void setUser(String str) {
            this.User = str;
        }

        public String getPassword() {
            return this.Password;
        }

        public void setPassword(String str) {
            this.Password = str;
        }
    }
}
