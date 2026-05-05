package tools;

import android.R;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.databinding.BindingAdapter;
import com.alibaba.sdk.android.push.notification.CustomNotificationBuilder;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes4.dex */
public class Drawables {
    private static final int INVALID = 0;
    private static final String TAG = "Drawables";
    private static final int[] tmpPadding = new int[4];

    @Target({ElementType.PARAMETER, ElementType.FIELD})
    @Retention(RetentionPolicy.SOURCE)
    @interface DP {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface GradientType {
        public static final int LINEAR = 0;
        public static final int RADIAL = 1;
        public static final int SWEEP = 2;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {
        public static final int BL_TR = 5;
        public static final int BOTTOM_TOP = 4;
        public static final int BR_TL = 3;
        public static final int LEFT_RIGHT = 6;
        public static final int RIGHT_LEFT = 2;
        public static final int TL_BR = 7;
        public static final int TOP_BOTTOM = 0;
        public static final int TR_BL = 1;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ShapeMode {
        public static final int LINE = 2;
        public static final int OVAL = 1;
        public static final int RECTANGLE = 0;
        public static final int RING = 3;
    }

    private static int validShapeMode(int i) {
        if (i > 3 || i < 0) {
            return 0;
        }
        return i;
    }

    @BindingAdapter(requireAll = false, value = {"drawable_shapeMode", "drawable_solidColor", "drawable_strokeColor", "drawable_strokeWidth", "drawable_strokeDash", "drawable_strokeDashGap", "drawable_radius", "drawable_radiusLT", "drawable_radiusLB", "drawable_radiusRT", "drawable_radiusRB", "drawable_startColor", "drawable_centerColor", "drawable_endColor", "drawable_orientation", "drawable_gradientType", "drawable_radialCenterX", "drawable_radialCenterY", "drawable_radialRadius", "drawable_width", "drawable_height", "drawable_marginLeft", "drawable_marginTop", "drawable_marginRight", "drawable_marginBottom", "drawable_ringThickness", "drawable_ringThicknessRatio", "drawable_ringInnerRadius", "drawable_ringInnerRadiusRatio", "drawable_checked_shapeMode", "drawable_checked_solidColor", "drawable_checked_strokeColor", "drawable_checked_strokeWidth", "drawable_checked_strokeDash", "drawable_checked_strokeDashGap", "drawable_checked_radius", "drawable_checked_radiusLT", "drawable_checked_radiusLB", "drawable_checked_radiusRT", "drawable_checked_radiusRB", "drawable_checked_startColor", "drawable_checked_centerColor", "drawable_checked_endColor", "drawable_checked_orientation", "drawable_checked_gradientType", "drawable_checked_radialCenterX", "drawable_checked_radialCenterY", "drawable_checked_radialRadius", "drawable_checked_width", "drawable_checked_height", "drawable_checked_marginLeft", "drawable_checked_marginTop", "drawable_checked_marginRight", "drawable_checked_marginBottom", "drawable_checked_ringThickness", "drawable_checked_ringThicknessRatio", "drawable_checked_ringInnerRadius", "drawable_checked_ringInnerRadiusRatio", "drawable_checkable_shapeMode", "drawable_checkable_solidColor", "drawable_checkable_strokeColor", "drawable_checkable_strokeWidth", "drawable_checkable_strokeDash", "drawable_checkable_strokeDashGap", "drawable_checkable_radius", "drawable_checkable_radiusLT", "drawable_checkable_radiusLB", "drawable_checkable_radiusRT", "drawable_checkable_radiusRB", "drawable_checkable_startColor", "drawable_checkable_centerColor", "drawable_checkable_endColor", "drawable_checkable_orientation", "drawable_checkable_gradientType", "drawable_checkable_radialCenterX", "drawable_checkable_radialCenterY", "drawable_checkable_radialRadius", "drawable_checkable_width", "drawable_checkable_height", "drawable_checkable_marginLeft", "drawable_checkable_marginTop", "drawable_checkable_marginRight", "drawable_checkable_marginBottom", "drawable_checkable_ringThickness", "drawable_checkable_ringThicknessRatio", "drawable_checkable_ringInnerRadius", "drawable_checkable_ringInnerRadiusRatio", "drawable_enabled_shapeMode", "drawable_enabled_solidColor", "drawable_enabled_strokeColor", "drawable_enabled_strokeWidth", "drawable_enabled_strokeDash", "drawable_enabled_strokeDashGap", "drawable_enabled_radius", "drawable_enabled_radiusLT", "drawable_enabled_radiusLB", "drawable_enabled_radiusRT", "drawable_enabled_radiusRB", "drawable_enabled_startColor", "drawable_enabled_centerColor", "drawable_enabled_endColor", "drawable_enabled_orientation", "drawable_enabled_gradientType", "drawable_enabled_radialCenterX", "drawable_enabled_radialCenterY", "drawable_enabled_radialRadius", "drawable_enabled_width", "drawable_enabled_height", "drawable_enabled_marginLeft", "drawable_enabled_marginTop", "drawable_enabled_marginRight", "drawable_enabled_marginBottom", "drawable_enabled_ringThickness", "drawable_enabled_ringThicknessRatio", "drawable_enabled_ringInnerRadius", "drawable_enabled_ringInnerRadiusRatio", "drawable_focused_shapeMode", "drawable_focused_solidColor", "drawable_focused_strokeColor", "drawable_focused_strokeWidth", "drawable_focused_strokeDash", "drawable_focused_strokeDashGap", "drawable_focused_radius", "drawable_focused_radiusLT", "drawable_focused_radiusLB", "drawable_focused_radiusRT", "drawable_focused_radiusRB", "drawable_focused_startColor", "drawable_focused_centerColor", "drawable_focused_endColor", "drawable_focused_orientation", "drawable_focused_gradientType", "drawable_focused_radialCenterX", "drawable_focused_radialCenterY", "drawable_focused_radialRadius", "drawable_focused_width", "drawable_focused_height", "drawable_focused_marginLeft", "drawable_focused_marginTop", "drawable_focused_marginRight", "drawable_focused_marginBottom", "drawable_focused_ringThickness", "drawable_focused_ringThicknessRatio", "drawable_focused_ringInnerRadius", "drawable_focused_ringInnerRadiusRatio", "drawable_pressed_shapeMode", "drawable_pressed_solidColor", "drawable_pressed_strokeColor", "drawable_pressed_strokeWidth", "drawable_pressed_strokeDash", "drawable_pressed_strokeDashGap", "drawable_pressed_radius", "drawable_pressed_radiusLT", "drawable_pressed_radiusLB", "drawable_pressed_radiusRT", "drawable_pressed_radiusRB", "drawable_pressed_startColor", "drawable_pressed_centerColor", "drawable_pressed_endColor", "drawable_pressed_orientation", "drawable_pressed_gradientType", "drawable_pressed_radialCenterX", "drawable_pressed_radialCenterY", "drawable_pressed_radialRadius", "drawable_pressed_width", "drawable_pressed_height", "drawable_pressed_marginLeft", "drawable_pressed_marginTop", "drawable_pressed_marginRight", "drawable_pressed_marginBottom", "drawable_pressed_ringThickness", "drawable_pressed_ringThicknessRatio", "drawable_pressed_ringInnerRadius", "drawable_pressed_ringInnerRadiusRatio", "drawable_selected_shapeMode", "drawable_selected_solidColor", "drawable_selected_strokeColor", "drawable_selected_strokeWidth", "drawable_selected_strokeDash", "drawable_selected_strokeDashGap", "drawable_selected_radius", "drawable_selected_radiusLT", "drawable_selected_radiusLB", "drawable_selected_radiusRT", "drawable_selected_radiusRB", "drawable_selected_startColor", "drawable_selected_centerColor", "drawable_selected_endColor", "drawable_selected_orientation", "drawable_selected_gradientType", "drawable_selected_radialCenterX", "drawable_selected_radialCenterY", "drawable_selected_radialRadius", "drawable_selected_width", "drawable_selected_height", "drawable_selected_marginLeft", "drawable_selected_marginTop", "drawable_selected_marginRight", "drawable_selected_marginBottom", "drawable_selected_ringThickness", "drawable_selected_ringThicknessRatio", "drawable_selected_ringInnerRadius", "drawable_selected_ringInnerRadiusRatio", CustomNotificationBuilder.NOTIFICATION_ICON_RES_TYPE, "drawable_checked", "drawable_checkable", "drawable_enabled", "drawable_focused", "drawable_pressed", "drawable_selected"})
    public static void setViewBackground(View view2, int i, @ColorInt Integer num, @ColorInt int i2, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, @ColorInt Integer num2, @ColorInt Integer num3, @ColorInt Integer num4, int i3, int i4, Float f9, Float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19, float f20, float f21, int i5, @ColorInt Integer num5, @ColorInt int i6, float f22, float f23, float f24, float f25, float f26, float f27, float f28, float f29, @ColorInt Integer num6, @ColorInt Integer num7, @ColorInt Integer num8, int i7, int i8, Float f30, Float f31, float f32, float f33, float f34, float f35, float f36, float f37, float f38, float f39, float f40, float f41, float f42, int i9, @ColorInt Integer num9, @ColorInt int i10, float f43, float f44, float f45, float f46, float f47, float f48, float f49, float f50, @ColorInt Integer num10, @ColorInt Integer num11, @ColorInt Integer num12, int i11, int i12, Float f51, Float f52, float f53, float f54, float f55, float f56, float f57, float f58, float f59, float f60, float f61, float f62, float f63, int i13, @ColorInt Integer num13, @ColorInt int i14, float f64, float f65, float f66, float f67, float f68, float f69, float f70, float f71, @ColorInt Integer num14, @ColorInt Integer num15, @ColorInt Integer num16, int i15, int i16, Float f72, Float f73, float f74, float f75, float f76, float f77, float f78, float f79, float f80, float f81, float f82, float f83, float f84, int i17, @ColorInt Integer num17, @ColorInt int i18, float f85, float f86, float f87, float f88, float f89, float f90, float f91, float f92, @ColorInt Integer num18, @ColorInt Integer num19, @ColorInt Integer num20, int i19, int i20, Float f93, Float f94, float f95, float f96, float f97, float f98, float f99, float f100, float f101, float f102, float f103, float f104, float f105, int i21, @ColorInt Integer num21, @ColorInt int i22, float f106, float f107, float f108, float f109, float f110, float f111, float f112, float f113, @ColorInt Integer num22, @ColorInt Integer num23, @ColorInt Integer num24, int i23, int i24, Float f114, Float f115, float f116, float f117, float f118, float f119, float f120, float f121, float f122, float f123, float f124, float f125, float f126, int i25, @ColorInt Integer num25, @ColorInt int i26, float f127, float f128, float f129, float f130, float f131, float f132, float f133, float f134, @ColorInt Integer num26, @ColorInt Integer num27, @ColorInt Integer num28, int i27, int i28, Float f135, Float f136, float f137, float f138, float f139, float f140, float f141, float f142, float f143, float f144, float f145, float f146, float f147, Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4, Drawable drawable5, Drawable drawable6, Drawable drawable7) {
        int i29;
        boolean z;
        boolean z2;
        Drawable drawableCreate = drawable != null ? drawable : create(i, num, i2, f, f2, f3, f4, f5, f6, f7, f8, num2, num3, num4, i3, i4, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, f21);
        if (drawableCreate != null) {
            z = false;
            i29 = 1;
        } else {
            i29 = 0;
            z = true;
        }
        Drawable drawableCreate2 = drawable2 != null ? drawable2 : create(i5, num5, i6, f22, f23, f24, f25, f26, f27, f28, f29, num6, num7, num8, i7, i8, f30, f31, f32, f33, f34, f35, f36, f37, f38, f39, f40, f41, f42);
        if (drawableCreate2 != null) {
            i29++;
        }
        Drawable drawableCreate3 = drawable3 != null ? drawable3 : create(i9, num9, i10, f43, f44, f45, f46, f47, f48, f49, f50, num10, num11, num12, i11, i12, f51, f52, f53, f54, f55, f56, f57, f58, f59, f60, f61, f62, f63);
        if (drawableCreate3 != null) {
            i29++;
        }
        Drawable drawableCreate4 = drawable4 != null ? drawable4 : create(i13, num13, i14, f64, f65, f66, f67, f68, f69, f70, f71, num14, num15, num16, i15, i16, f72, f73, f74, f75, f76, f77, f78, f79, f80, f81, f82, f83, f84);
        if (drawableCreate4 != null) {
            i29++;
        }
        Drawable drawableCreate5 = drawable5 != null ? drawable5 : create(i17, num17, i18, f85, f86, f87, f88, f89, f90, f91, f92, num18, num19, num20, i19, i20, f93, f94, f95, f96, f97, f98, f99, f100, f101, f102, f103, f104, f105);
        if (drawableCreate5 != null) {
            i29++;
        }
        Drawable drawableCreate6 = drawable6 != null ? drawable6 : create(i21, num21, i22, f106, f107, f108, f109, f110, f111, f112, f113, num22, num23, num24, i23, i24, f114, f115, f116, f117, f118, f119, f120, f121, f122, f123, f124, f125, f126);
        if (drawableCreate6 != null) {
            i29++;
        }
        Drawable drawableCreate7 = drawable7 != null ? drawable7 : create(i25, num25, i26, f127, f128, f129, f130, f131, f132, f133, f134, num26, num27, num28, i27, i28, f135, f136, f137, f138, f139, f140, f141, f142, f143, f144, f145, f146, f147);
        if (drawableCreate7 != null) {
            i29++;
        }
        if (i29 < 1) {
            return;
        }
        if (z || i29 == 1) {
            tmpPadding[0] = view2.getPaddingLeft();
            tmpPadding[1] = view2.getPaddingTop();
            tmpPadding[2] = view2.getPaddingRight();
            tmpPadding[3] = view2.getPaddingBottom();
            z2 = true;
        } else {
            z2 = false;
        }
        if (i29 == 1 && !z) {
            view2.setBackground(drawableCreate);
        } else {
            ProxyDrawable proxyDrawable = new ProxyDrawable();
            if (drawableCreate2 != null) {
                proxyDrawable.addState(new int[]{R.attr.state_checked}, drawableCreate2);
            }
            if (drawableCreate3 != null) {
                proxyDrawable.addState(new int[]{R.attr.state_checkable}, drawableCreate3);
            }
            if (drawableCreate5 != null) {
                proxyDrawable.addState(new int[]{R.attr.state_focused}, drawableCreate5);
            }
            if (drawableCreate6 != null) {
                proxyDrawable.addState(new int[]{R.attr.state_pressed}, drawableCreate6);
            }
            if (drawableCreate7 != null) {
                proxyDrawable.addState(new int[]{R.attr.state_selected}, drawableCreate7);
            }
            if (drawableCreate4 != null) {
                proxyDrawable.addState(new int[]{R.attr.state_enabled}, drawableCreate4);
            }
            if (drawableCreate != null) {
                proxyDrawable.addState(new int[]{0}, drawableCreate);
            } else {
                Drawable background = view2.getBackground();
                if (background != null) {
                    if (background instanceof ProxyDrawable) {
                        background = ((ProxyDrawable) background).getOriginDrawable();
                    }
                    proxyDrawable.addState(new int[]{0}, background);
                }
            }
            view2.setBackground(proxyDrawable);
        }
        if (z2) {
            int[] iArr = tmpPadding;
            view2.setPadding(iArr[0], iArr[1], iArr[2], iArr[3]);
        }
    }

    public static Drawable create(int i, @ColorInt Integer num, @ColorInt int i2, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, @ColorInt Integer num2, @ColorInt Integer num3, @ColorInt Integer num4, int i3, int i4, Float f9, Float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19, float f20, float f21) {
        int[] iArr;
        if (i == 0 && num == null && i2 == 0 && f == 0.0f && f2 == 0.0f && f3 == 0.0f && f4 == 0.0f && f5 == 0.0f && f6 == 0.0f && f7 == 0.0f && f8 == 0.0f && num2 == null && num3 == null && num4 == null && i3 == 0 && i4 == 0 && f9 == null && f10 == null && f11 == 0.0f && f12 == 0.0f && f13 == 0.0f && f14 == 0.0f && f15 == 0.0f && f16 == 0.0f && f17 == 0.0f) {
            return null;
        }
        GradientDrawable gradientDrawable = new GradientDrawable();
        if (num2 != null && num4 != null) {
            if (num3 != null) {
                iArr = new int[]{num2.intValue(), num3.intValue(), num4.intValue()};
            } else {
                iArr = new int[]{num2.intValue(), num4.intValue()};
            }
            gradientDrawable.setColors(iArr);
            gradientDrawable.setOrientation(mapOrientation(i3));
            gradientDrawable.setGradientType(i4);
            if (i4 == 1) {
                gradientDrawable.setGradientCenter(f9 == null ? 0.5f : f9.floatValue(), f10 != null ? f10.floatValue() : 0.5f);
                gradientDrawable.setGradientRadius(dip2px(f11));
            }
        } else if (num != null) {
            gradientDrawable.setColor(num.intValue());
        }
        gradientDrawable.setShape(validShapeMode(i));
        if (i == 3) {
            setRingValue(gradientDrawable, Float.valueOf(f18), Float.valueOf(f19), Float.valueOf(f20), Float.valueOf(f21));
        }
        if (f > 0.0f) {
            gradientDrawable.setStroke(dip2px(f), i2, dip2px(f2), dip2px(f3));
        }
        if (f4 <= 0.0f) {
            gradientDrawable.setCornerRadii(new float[]{dip2px(f5), dip2px(f5), dip2px(f7), dip2px(f7), dip2px(f8), dip2px(f8), dip2px(f6), dip2px(f6)});
        } else {
            gradientDrawable.setCornerRadius(dip2px(f4));
        }
        if (f12 > 0.0f && f13 > 0.0f) {
            gradientDrawable.setSize(dip2px(f12), dip2px(f13));
        }
        return (f14 == 0.0f && f15 == 0.0f && f16 == 0.0f && f17 == 0.0f) ? gradientDrawable : new InsetDrawable((Drawable) gradientDrawable, dip2px(f14), dip2px(f15), dip2px(f16), dip2px(f17));
    }

    private static GradientDrawable.Orientation mapOrientation(int i) {
        switch (i) {
            case 0:
                return GradientDrawable.Orientation.TOP_BOTTOM;
            case 1:
                return GradientDrawable.Orientation.TR_BL;
            case 2:
                return GradientDrawable.Orientation.RIGHT_LEFT;
            case 3:
                return GradientDrawable.Orientation.BR_TL;
            case 4:
                return GradientDrawable.Orientation.BOTTOM_TOP;
            case 5:
                return GradientDrawable.Orientation.BL_TR;
            case 6:
                return GradientDrawable.Orientation.LEFT_RIGHT;
            case 7:
                return GradientDrawable.Orientation.TL_BR;
            default:
                return GradientDrawable.Orientation.TOP_BOTTOM;
        }
    }

    private static void setRingValue(GradientDrawable gradientDrawable, Float f, Float f2, Float f3, Float f4) {
        try {
            Field declaredField = gradientDrawable.getClass().getDeclaredField("mGradientState");
            declaredField.setAccessible(true);
            Class<?> cls = declaredField.get(gradientDrawable).getClass();
            Field declaredField2 = cls.getDeclaredField("mUseLevelForShape");
            declaredField2.setAccessible(true);
            declaredField2.setBoolean(declaredField.get(gradientDrawable), false);
            if (f != null) {
                Field declaredField3 = cls.getDeclaredField("mThickness");
                declaredField3.setAccessible(true);
                declaredField3.setInt(declaredField.get(gradientDrawable), dip2px(f.floatValue()));
            }
            if (f2 != null) {
                Field declaredField4 = cls.getDeclaredField("mThicknessRatio");
                declaredField4.setAccessible(true);
                declaredField4.setFloat(declaredField.get(gradientDrawable), dip2px(f2.floatValue()));
            }
            if (f3 != null) {
                Field declaredField5 = cls.getDeclaredField("mInnerRadius");
                declaredField5.setAccessible(true);
                declaredField5.setInt(declaredField.get(gradientDrawable), dip2px(f3.floatValue()));
            }
            if (f4 != null) {
                Field declaredField6 = cls.getDeclaredField("mInnerRadiusRatio");
                declaredField6.setAccessible(true);
                declaredField6.setFloat(declaredField.get(gradientDrawable), dip2px(f4.floatValue()));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        }
    }

    private static int dip2px(float f) {
        return (int) ((f * Resources.getSystem().getDisplayMetrics().density) + 0.5f);
    }
}
