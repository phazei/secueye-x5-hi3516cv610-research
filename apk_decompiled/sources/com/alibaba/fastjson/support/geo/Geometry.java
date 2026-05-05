package com.alibaba.fastjson.support.geo;

import com.alibaba.fastjson.annotation.JSONType;

/* JADX INFO: loaded from: classes.dex */
@JSONType(seeAlso = {GeometryCollection.class, LineString.class, MultiLineString.class, Point.class, MultiPoint.class, Polygon.class, MultiPolygon.class, Feature.class, FeatureCollection.class}, typeKey = "type")
public abstract class Geometry {
    private double[] bbox;
    private final String type;

    protected Geometry(String str) {
        this.type = str;
    }

    public String getType() {
        return this.type;
    }

    public double[] getBbox() {
        return this.bbox;
    }

    public void setBbox(double[] dArr) {
        this.bbox = dArr;
    }
}
