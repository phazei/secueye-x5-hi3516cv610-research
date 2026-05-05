package view;

/* JADX INFO: loaded from: classes5.dex */
public class RoundCalculator {
    public static double calTwoPointDistant(double d2, double d3, double d4, double d5) {
        return Math.sqrt(Math.pow(d4 - d2, 2.0d) + Math.pow(d5 - d3, 2.0d));
    }

    public static double calTwoPointAngleDegree(double d2, double d3, double d4, double d5) {
        double dAsin = (Math.asin(Math.abs(d5 - d3) / calTwoPointDistant(d2, d3, d4, d5)) * 180.0d) / 3.141592653589793d;
        return (d4 >= d2 || d5 >= d3) ? (d4 >= d2 || d5 < d3) ? (d4 < d2 || d5 < d3) ? dAsin : 360.0d - dAsin : dAsin + 180.0d : 180.0d - dAsin;
    }

    public static double[] calPointLocationByAngle(double d2, double d3, double d4, double d5, double d6) {
        double dCalTwoPointAngleDegree = (calTwoPointAngleDegree(d2, d3, d4, d5) * 3.141592653589793d) / 180.0d;
        return new double[]{(float) (d2 + (Math.cos(dCalTwoPointAngleDegree) * d6)), (float) (d3 - (d6 * Math.sin(dCalTwoPointAngleDegree)))};
    }
}
