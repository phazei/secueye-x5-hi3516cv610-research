package tools;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: Extensions.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u001e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00018F¢\u0006\u0006\u001a\u0004\b\u0002\u0010\u0003\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0002\u0010\u0005¨\u0006\f"}, d2 = {"dp", "", "getDp", "(F)F", "", "(I)F", "getAvatar", "Landroid/graphics/Bitmap;", "res", "Landroid/content/res/Resources;", "width", "id", "secueye_googleRelease"}, k = 2, mv = {1, 1, 15})
public final class ExtensionsKt {
    public static final float getDp(float f) {
        Resources system = Resources.getSystem();
        Intrinsics.checkExpressionValueIsNotNull(system, "Resources.getSystem()");
        return TypedValue.applyDimension(1, f, system.getDisplayMetrics());
    }

    public static final float getDp(int i) {
        return getDp(i);
    }

    @NotNull
    public static final Bitmap getAvatar(@NotNull Resources res, int i, int i2) {
        Intrinsics.checkParameterIsNotNull(res, "res");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, i2, options);
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        options.inTargetDensity = i;
        Bitmap bitmapDecodeResource = BitmapFactory.decodeResource(res, i2, options);
        Intrinsics.checkExpressionValueIsNotNull(bitmapDecodeResource, "BitmapFactory.decodeResource(res, id, options)");
        return bitmapDecodeResource;
    }
}
