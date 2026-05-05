package com.alibaba.fastjson.support.geo;

import com.alibaba.fastjson.annotation.JSONType;

/* JADX INFO: loaded from: classes.dex */
@JSONType(orders = {"type", "bbox", "coordinates"}, typeName = "MultiPolygon")
public class MultiPolygon extends Geometry {
    private double[][][][] coordinates;

    public MultiPolygon() {
        super("MultiPolygon");
    }

    public double[][][][] getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(double[][][][] dArr) {
        this.coordinates = dArr;
    }
}
