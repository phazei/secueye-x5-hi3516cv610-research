package com.aliyun.alink.linksdk.cmp.connect.alcs;

import com.aliyun.alink.linksdk.cmp.core.base.AConnectOption;

/* JADX INFO: loaded from: classes2.dex */
public class AlcsServerConnectOption extends AConnectOption {
    private String blackClients = null;
    private OptionFlag optionFlag;
    private String prefix;
    private String secrect;

    public enum OptionFlag {
        ADD_PREFIX_SECRET,
        DELETE_PREFIX,
        UPDATE_BLACK_LIST
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String str) {
        this.prefix = str;
    }

    public String getSecrect() {
        return this.secrect;
    }

    public void setSecrect(String str) {
        this.secrect = str;
    }

    public String getBlackClients() {
        return this.blackClients;
    }

    public void setBlackClients(String str) {
        this.blackClients = str;
    }

    public OptionFlag getOptionFlag() {
        return this.optionFlag;
    }

    public void setOptionFlag(OptionFlag optionFlag) {
        this.optionFlag = optionFlag;
    }
}
