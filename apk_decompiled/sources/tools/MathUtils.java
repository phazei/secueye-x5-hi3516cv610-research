package tools;

import android.graphics.PointF;
import java.util.List;

/* JADX INFO: loaded from: classes4.dex */
public class MathUtils {
    public static final double CIRCLE_RADIAN = 6.283185307179586d;

    public static double getTanRadian(double d2, int i) {
        if (d2 < 0.0d) {
            d2 += 1.5707963267948966d;
        }
        return d2 + (((double) (i - 1)) * 1.5707963267948966d);
    }

    public static double radianToAngle(double d2) {
        return (d2 / 6.283185307179586d) * 360.0d;
    }

    private MathUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static int getQuadrant(PointF pointF, PointF pointF2) {
        if (pointF.x > pointF2.x) {
            if (pointF.y > pointF2.y) {
                return 4;
            }
            return pointF.y < pointF2.y ? 1 : -1;
        }
        if (pointF.x >= pointF2.x) {
            return -1;
        }
        if (pointF.y > pointF2.y) {
            return 3;
        }
        return pointF.y < pointF2.y ? 2 : -1;
    }

    public static float getPointDistance(PointF pointF, PointF pointF2) {
        return (float) Math.sqrt(Math.pow(pointF.x - pointF2.x, 2.0d) + Math.pow(pointF.y - pointF2.y, 2.0d));
    }

    public static void getInnerTangentPoints(PointF pointF, float f, Double d2, List<PointF> list) {
        float fSin;
        if (d2 != null) {
            double dAtan = (float) Math.atan(d2.doubleValue());
            double d3 = f;
            float fCos = (float) (Math.cos(dAtan) * d3);
            fSin = (float) (Math.sin(dAtan) * d3);
            f = fCos;
        } else {
            fSin = 0.0f;
        }
        list.add(new PointF(pointF.x + f, pointF.y + fSin));
        list.add(new PointF(pointF.x - f, pointF.y - fSin));
    }
}
