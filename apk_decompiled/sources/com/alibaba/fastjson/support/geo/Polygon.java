package com.alibaba.fastjson.support.geo;

import com.alibaba.fastjson.annotation.JSONType;

/* JADX INFO: loaded from: classes.dex */
@JSONType(orders = {"type", "bbox", "coordinates"}, typeName = "Polygon")
public class Polygon extends Geometry {
    private double[][][] coordinates;

    public Polygon() {
        super("Polygon");
    }

    public double[][][] getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(double[][][] dArr) {
        this.coordinates = dArr;
    }
}
