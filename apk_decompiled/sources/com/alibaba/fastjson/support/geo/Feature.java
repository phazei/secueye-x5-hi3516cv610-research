package com.alibaba.fastjson.support.geo;

import com.alibaba.fastjson.annotation.JSONType;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.util.LinkedHashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
@JSONType(orders = {"type", "id", "bbox", "coordinates", TmpConstant.DEVICE_MODEL_PROPERTIES}, typeName = "Feature")
public class Feature extends Geometry {
    private Geometry geometry;
    private String id;
    private Map<String, String> properties;

    public Feature() {
        super("Feature");
        this.properties = new LinkedHashMap();
    }

    public Geometry getGeometry() {
        return this.geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<String, String> map) {
        this.properties = map;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }
}
