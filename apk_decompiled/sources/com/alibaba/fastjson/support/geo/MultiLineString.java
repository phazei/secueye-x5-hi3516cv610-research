package com.alibaba.fastjson.support.geo;

import com.alibaba.fastjson.annotation.JSONType;

/* JADX INFO: loaded from: classes.dex */
@JSONType(orders = {"type", "bbox", "coordinates"}, typeName = "MultiLineString")
public class MultiLineString extends Geometry {
    private double[][][] coordinates;

    public MultiLineString() {
        super("MultiLineString");
    }

    public double[][][] getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(double[][][] dArr) {
        this.coordinates = dArr;
    }
}
