package kt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import bean.AreaPointBean;
import com.aliyun.alink.linksdk.alcs.coap.resources.LinkFormat;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.google.gson.Gson;
import com.seculink.app.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tools.SharePreferenceManager;
import tools.SpUtil;
import tools.Utils;

/* JADX INFO: compiled from: AreaView.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0010\b\u0007\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J \u0010\u0019\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\b2\u0006\u0010\u001b\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00020\nH\u0002J\b\u0010\u001d\u001a\u00020\u000fH\u0002J\u0006\u0010\u001e\u001a\u00020\u001fJ\u0006\u0010 \u001a\u00020\nJ\u0006\u0010!\u001a\u00020\nJ\u000e\u0010\"\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\u0003J\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00150$J\u0010\u0010%\u001a\u00020\u001f2\u0006\u0010&\u001a\u00020'H\u0014J0\u0010(\u001a\u00020\u001f2\u0006\u0010)\u001a\u00020\u000f2\u0006\u0010*\u001a\u00020\n2\u0006\u0010+\u001a\u00020\n2\u0006\u0010,\u001a\u00020\n2\u0006\u0010-\u001a\u00020\nH\u0014J(\u0010.\u001a\u00020\u001f2\u0006\u0010/\u001a\u00020\n2\u0006\u00100\u001a\u00020\n2\u0006\u00101\u001a\u00020\n2\u0006\u00102\u001a\u00020\nH\u0014J\u0006\u00103\u001a\u00020\u001fJ\u0010\u00104\u001a\u00020\u001f2\u0006\u00105\u001a\u00020\bH\u0002J\u000e\u00106\u001a\u00020\u001f2\u0006\u0010\f\u001a\u00020\rR\u000e\u0010\u0007\u001a\u00020\bX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00150\u0014X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00150\u0014X\u0082\u0004¢\u0006\u0002\n\u0000¨\u00067"}, d2 = {"Lkt/AreaView;", "Landroidx/constraintlayout/widget/ConstraintLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "bitmap", "Landroid/graphics/Bitmap;", "bitmapHeight", "", "bitmapWidth", "iotId", "", "isReset", "", "moveX", "", "moveY", "originalList", "", "Landroid/graphics/PointF;", "paint", "Landroid/graphics/Paint;", "pointList", "bit", "bm", "newWidth", "newHeight", "check", "getBitmap", "", "getBitmapHeight", "getBitmapWidth", "getFilesPath", "getPoint", "", "onDraw", "canvas", "Landroid/graphics/Canvas;", "onLayout", "changed", TtmlNode.LEFT, "top", TtmlNode.RIGHT, "bottom", "onSizeChanged", "w", LinkFormat.HOST, "oldw", "oldh", "reSet", "scanFile", "bmp", "setBackground", "secueye_googleRelease"}, k = 1, mv = {1, 1, 15})
@SuppressLint({"ClickableViewAccessibility"})
public final class AreaView extends ConstraintLayout {
    private HashMap _$_findViewCache;
    private Bitmap bitmap;
    private int bitmapHeight;
    private int bitmapWidth;
    private String iotId;
    private boolean isReset;
    private float moveX;
    private float moveY;
    private final List<PointF> originalList;
    private final Paint paint;
    private final List<PointF> pointList;

    public void _$_clearFindViewByIdCache() {
        HashMap map = this._$_findViewCache;
        if (map != null) {
            map.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view2 = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view2 != null) {
            return view2;
        }
        View viewFindViewById = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), viewFindViewById);
        return viewFindViewById;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AreaView(@NotNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkParameterIsNotNull(context, "context");
        Paint paint = new Paint(1);
        paint.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        paint.setStrokeWidth(UtilsKt.getDp(2));
        this.paint = paint;
        this.pointList = new ArrayList();
        this.originalList = new ArrayList();
        this.iotId = "";
        this.isReset = true;
        LayoutInflater.from(context).inflate(R.layout.area_view_layout, (ViewGroup) this, true);
        setWillNotDraw(false);
        ConstraintLayout my_parent = (ConstraintLayout) _$_findCachedViewById(R.id.my_parent);
        Intrinsics.checkExpressionValueIsNotNull(my_parent, "my_parent");
        int childCount = my_parent.getChildCount();
        for (final int i = 0; i < childCount; i++) {
            this.pointList.add(new PointF());
            this.originalList.add(new PointF());
            ((ConstraintLayout) _$_findCachedViewById(R.id.my_parent)).getChildAt(i).setOnTouchListener(new View.OnTouchListener() { // from class: kt.AreaView.1
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View v, MotionEvent event2) {
                    Intrinsics.checkExpressionValueIsNotNull(event2, "event");
                    float x = event2.getX();
                    float y = event2.getY();
                    int action = event2.getAction();
                    if (action == 0) {
                        AreaView.this.moveX = x;
                        AreaView.this.moveY = y;
                        return true;
                    }
                    if (action != 2) {
                        return true;
                    }
                    float f = x - AreaView.this.moveX;
                    float f2 = y - AreaView.this.moveY;
                    int i2 = (int) f;
                    v.offsetLeftAndRight(i2);
                    int i3 = (int) f2;
                    v.offsetTopAndBottom(i3);
                    float f3 = ((PointF) AreaView.this.pointList.get(i)).x;
                    float f4 = ((PointF) AreaView.this.pointList.get(i)).y;
                    PointF pointF = (PointF) AreaView.this.pointList.get(i);
                    Intrinsics.checkExpressionValueIsNotNull(v, "v");
                    pointF.x = v.getX() + (v.getWidth() / 2);
                    ((PointF) AreaView.this.pointList.get(i)).y = v.getY() + (v.getHeight() / 2);
                    AreaView.this.invalidate();
                    if (AreaView.this.check()) {
                        float f5 = 0;
                        if (v.getX() + (v.getWidth() / 4) >= f5 && v.getX() <= (AreaView.this.getWidth() - v.getWidth()) + (v.getWidth() / 4) && v.getY() + (v.getHeight() / 4) >= f5 && v.getY() <= (AreaView.this.getHeight() - v.getHeight()) + (v.getHeight() / 4)) {
                            return true;
                        }
                    }
                    v.offsetLeftAndRight(-i2);
                    v.offsetTopAndBottom(-i3);
                    ((PointF) AreaView.this.pointList.get(i)).x = f3;
                    ((PointF) AreaView.this.pointList.get(i)).y = f4;
                    AreaView.this.moveX = x;
                    AreaView.this.moveY = y;
                    return true;
                }
            });
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.isReset) {
            int size = this.pointList.size();
            for (int i = 0; i < size; i++) {
                View temp = ((ConstraintLayout) _$_findCachedViewById(R.id.my_parent)).getChildAt(i);
                PointF pointF = this.originalList.get(i);
                Intrinsics.checkExpressionValueIsNotNull(temp, "temp");
                pointF.x = temp.getX() + (temp.getWidth() / 2.0f);
                this.originalList.get(i).y = temp.getY() + (temp.getHeight() / 2.0f);
            }
            this.isReset = false;
        }
        try {
            AreaPointBean areaPointBean = (AreaPointBean) new Gson().fromJson(SharePreferenceManager.getInstance().getRegionDetectPoint(this.iotId), AreaPointBean.class);
            if (areaPointBean != null) {
                ConstraintLayout my_parent = (ConstraintLayout) _$_findCachedViewById(R.id.my_parent);
                Intrinsics.checkExpressionValueIsNotNull(my_parent, "my_parent");
                int childCount = my_parent.getChildCount();
                for (int i2 = 0; i2 < childCount; i2++) {
                    View childAt = ((ConstraintLayout) _$_findCachedViewById(R.id.my_parent)).getChildAt(i2);
                    if (areaPointBean.getLeftTop_X() == null) {
                        return;
                    }
                    areaPointBean.getLeftTop_X().intValue();
                    getBitmapWidth();
                    getBitmapWidth();
                    if (childAt == null) {
                        return;
                    }
                    childAt.getWidth();
                    switch (i2) {
                        case 0:
                            childAt.setX(((areaPointBean.getLeftTop_X().intValue() * getBitmapWidth()) / 640) - (childAt.getWidth() / 2));
                            childAt.setY(((areaPointBean.getLeftTop_Y().intValue() * getBitmapHeight()) / 360) - (childAt.getHeight() / 2));
                            break;
                        case 1:
                            childAt.setX(((areaPointBean.getRightTop_X().intValue() * getBitmapWidth()) / 640) - (childAt.getWidth() / 2));
                            childAt.setY(((areaPointBean.getRightTop_Y().intValue() * getBitmapHeight()) / 360) - (childAt.getHeight() / 2));
                            break;
                        case 2:
                            childAt.setX(((areaPointBean.getRightBottom_X().intValue() * getBitmapWidth()) / 640) - (childAt.getWidth() / 2));
                            childAt.setY(((areaPointBean.getRightBottom_Y().intValue() * getBitmapHeight()) / 360) - (childAt.getHeight() / 2));
                            break;
                        case 3:
                            childAt.setX(((areaPointBean.getLeftBottom_X().intValue() * getBitmapWidth()) / 640) - (childAt.getWidth() / 2));
                            childAt.setY(((areaPointBean.getLeftBottom_Y().intValue() * getBitmapHeight()) / 360) - (childAt.getHeight() / 2));
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int size2 = this.pointList.size();
        for (int i3 = 0; i3 < size2; i3++) {
            View temp2 = ((ConstraintLayout) _$_findCachedViewById(R.id.my_parent)).getChildAt(i3);
            PointF pointF2 = this.pointList.get(i3);
            Intrinsics.checkExpressionValueIsNotNull(temp2, "temp");
            pointF2.x = temp2.getX() + (temp2.getWidth() / 2.0f);
            this.pointList.get(i3).y = temp2.getY() + (temp2.getHeight() / 2.0f);
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(SpUtil.getString(getContext(), Utils.getDevSnapKey(this.iotId), ""), options);
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        if (Intrinsics.areEqual("", SpUtil.getString(getContext(), Utils.getDevSnapKey(this.iotId), "")) || SpUtil.getString(getContext(), Utils.getDevSnapKey(this.iotId), "") == null) {
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Intrinsics.checkExpressionValueIsNotNull(bitmapCreateBitmap, "Bitmap.createBitmap(widt… Bitmap.Config.ARGB_8888)");
            this.bitmap = bitmapCreateBitmap;
            Bitmap bitmap = this.bitmap;
            if (bitmap == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bitmap");
            }
            bitmap.eraseColor(-1);
            Bitmap bitmap2 = this.bitmap;
            if (bitmap2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bitmap");
            }
            this.bitmapWidth = bitmap2.getWidth();
            Bitmap bitmap3 = this.bitmap;
            if (bitmap3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bitmap");
            }
            this.bitmapHeight = bitmap3.getHeight();
            return;
        }
        if (getContext() != null) {
            Bitmap bitmapDecodeFile = BitmapFactory.decodeFile(SpUtil.getString(getContext(), Utils.getDevSnapKey(this.iotId), ""), options);
            Intrinsics.checkExpressionValueIsNotNull(bitmapDecodeFile, "BitmapFactory.decodeFile…options\n                )");
            this.bitmap = bitmapDecodeFile;
            Bitmap bitmap4 = this.bitmap;
            if (bitmap4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bitmap");
            }
            this.bitmap = bit(bitmap4, getWidth(), (getWidth() / 16) * 9);
            Bitmap bitmap5 = this.bitmap;
            if (bitmap5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bitmap");
            }
            this.bitmapWidth = bitmap5.getWidth();
            Bitmap bitmap6 = this.bitmap;
            if (bitmap6 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bitmap");
            }
            this.bitmapHeight = bitmap6.getHeight();
        }
    }

    @Override // android.view.View
    protected void onDraw(@NotNull Canvas canvas) {
        Intrinsics.checkParameterIsNotNull(canvas, "canvas");
        super.onDraw(canvas);
        Bitmap bitmap = this.bitmap;
        if (bitmap == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bitmap");
        }
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, this.paint);
        canvas.drawLine(this.pointList.get(0).x, this.pointList.get(0).y, this.pointList.get(1).x, this.pointList.get(1).y, this.paint);
        canvas.drawLine(this.pointList.get(1).x, this.pointList.get(1).y, this.pointList.get(2).x, this.pointList.get(2).y, this.paint);
        canvas.drawLine(this.pointList.get(2).x, this.pointList.get(2).y, this.pointList.get(3).x, this.pointList.get(3).y, this.paint);
        canvas.drawLine(this.pointList.get(3).x, this.pointList.get(3).y, this.pointList.get(0).x, this.pointList.get(0).y, this.paint);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean check() {
        PointF pointF = this.pointList.get(0);
        PointF pointF2 = this.pointList.get(1);
        PointF pointF3 = this.pointList.get(2);
        PointF pointF4 = this.pointList.get(3);
        return (((((pointF4.x - pointF.x) * (pointF2.y - pointF.y)) - ((pointF4.y - pointF.y) * (pointF2.x - pointF.x))) * (((pointF.x - pointF2.x) * (pointF3.y - pointF2.y)) - ((pointF.y - pointF2.y) * (pointF3.x - pointF2.x)))) * (((pointF2.x - pointF3.x) * (pointF4.y - pointF3.y)) - ((pointF2.y - pointF3.y) * (pointF4.x - pointF3.x)))) * (((pointF3.x - pointF4.x) * (pointF.y - pointF4.y)) - ((pointF3.y - pointF4.y) * (pointF.x - pointF4.x))) > ((float) 0);
    }

    @NotNull
    public final List<PointF> getPoint() {
        return this.pointList;
    }

    public final void setBackground(@NotNull String iotId) {
        Intrinsics.checkParameterIsNotNull(iotId, "iotId");
        this.iotId = iotId;
    }

    public final void reSet() {
        int size = this.pointList.size();
        for (int i = 0; i < size; i++) {
            this.pointList.get(i).x = this.originalList.get(i).x;
            this.pointList.get(i).y = this.originalList.get(i).y;
            View temp = ((ConstraintLayout) _$_findCachedViewById(R.id.my_parent)).getChildAt(i);
            Intrinsics.checkExpressionValueIsNotNull(temp, "temp");
            temp.setX(this.pointList.get(i).x - (temp.getWidth() / 2));
            temp.setY(this.pointList.get(i).y - (temp.getHeight() / 2));
        }
        invalidate();
    }

    public final int getBitmapWidth() {
        return this.bitmapWidth;
    }

    public final int getBitmapHeight() {
        return this.bitmapHeight;
    }

    public final void getBitmap() {
        Bitmap bitmap = this.bitmap;
        if (bitmap == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bitmap");
        }
        scanFile(bitmap);
    }

    private final void scanFile(Bitmap bmp) {
        StringBuilder sb = new StringBuilder();
        Context context = getContext();
        Intrinsics.checkExpressionValueIsNotNull(context, "context");
        sb.append(getFilesPath(context));
        sb.append("/area/");
        File file = new File(sb.toString());
        if (file.exists() || file.mkdirs()) {
            String str = this.iotId + ".jpg";
            File file2 = new File(file, str);
            if (file2.exists()) {
                file2.delete();
                file2 = new File(file, str);
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private final Bitmap bit(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(newWidth / width, newHeight / height);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        Intrinsics.checkExpressionValueIsNotNull(bitmapCreateBitmap, "Bitmap.createBitmap(bm, …th, height, matrix, true)");
        return bitmapCreateBitmap;
    }

    @NotNull
    public final String getFilesPath(@NotNull Context context) {
        String path;
        Intrinsics.checkParameterIsNotNull(context, "context");
        if (Intrinsics.areEqual("mounted", Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            File externalFilesDir = context.getExternalFilesDir(null);
            if (externalFilesDir == null) {
                Intrinsics.throwNpe();
            }
            Intrinsics.checkExpressionValueIsNotNull(externalFilesDir, "context.getExternalFilesDir(null)!!");
            path = externalFilesDir.getPath();
            Intrinsics.checkExpressionValueIsNotNull(path, "context.getExternalFilesDir(null)!!.path");
        } else {
            File filesDir = context.getFilesDir();
            Intrinsics.checkExpressionValueIsNotNull(filesDir, "context.filesDir");
            path = filesDir.getPath();
            Intrinsics.checkExpressionValueIsNotNull(path, "context.filesDir.path");
        }
        return path + "//" + Utils.getUserPhone();
    }
}
