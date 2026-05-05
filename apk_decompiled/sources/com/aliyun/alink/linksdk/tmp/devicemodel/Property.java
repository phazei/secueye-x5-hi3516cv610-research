package com.aliyun.alink.linksdk.tmp.devicemodel;

/* JADX INFO: loaded from: classes2.dex */
public class Property {
    private String accessMode;
    private DataType dataType;
    private String desc;
    private String identifier;
    private String name;
    private boolean optional;
    private boolean required;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getAccessMode() {
        return this.accessMode;
    }

    public void setAccessMode(String str) {
        this.accessMode = str;
    }

    @Deprecated
    public boolean isOptional() {
        return this.optional;
    }

    @Deprecated
    public void setOptional(boolean z) {
        this.optional = z;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String str) {
        this.desc = str;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String str) {
        this.identifier = str;
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(boolean z) {
        this.required = z;
    }

    public DataType getDataType() {
        return this.dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
}
