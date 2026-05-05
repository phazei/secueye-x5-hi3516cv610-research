package view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import bean.CrossLineDetectBean;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.seculink.app.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import tools.SharePreferenceManager;
import tools.SpUtil;
import tools.Utils;

/* JADX INFO: loaded from: classes5.dex */
public class LineView extends RelativeLayout {
    private static int direction = 180;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    Bitmap f8110b;
    private Canvas canvas;
    private Context context;
    private ImageView end;
    private PointF ep;
    private String iotId;
    private boolean isFirst;
    private int moveX;
    private int moveY;
    private List<PointF> originalList;
    Paint paint;
    private ImageView pic;
    private int picNum;
    private PointF sp;
    private ImageView start;
    private ImageView test;
    private double x;
    private double y;

    interface setOnLister {
    }

    public LineView(Context context) {
        super(context);
        this.isFirst = true;
        this.originalList = new ArrayList();
        initView(context);
        this.context = context;
    }

    public LineView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.isFirst = true;
        this.originalList = new ArrayList();
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.LineView);
        this.picNum = typedArrayObtainStyledAttributes.getResourceId(0, 0);
        typedArrayObtainStyledAttributes.recycle();
        initView(context);
        this.context = context;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.line_control_view_, (ViewGroup) this, true);
        this.sp = new PointF();
        this.ep = new PointF();
        this.paint = new Paint();
        this.paint.setColor(getResources().getColor(R.color.colorAccent));
        this.paint.setStrokeWidth(5.0f);
        this.start = (ImageView) findViewById(R.id.start);
        this.end = (ImageView) findViewById(R.id.end);
        this.test = (ImageView) findViewById(R.id.test);
        this.pic = (ImageView) findViewById(R.id.pic);
        this.f8110b = Bitmap.createBitmap(1024, 576, Bitmap.Config.ARGB_8888);
        this.canvas = new Canvas(this.f8110b);
        this.test.setImageBitmap(this.f8110b);
        this.start.setOnTouchListener(new View.OnTouchListener() { // from class: view.LineView.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                int action = motionEvent.getAction();
                if (action == 0) {
                    LineView.this.moveX = x;
                    LineView.this.moveY = y;
                    return true;
                }
                if (action != 2) {
                    return true;
                }
                int i = x - LineView.this.moveX;
                int i2 = y - LineView.this.moveY;
                LineView.this.start.offsetLeftAndRight(i);
                LineView.this.start.offsetTopAndBottom(i2);
                LineView.this.invalidate();
                if (LineView.this.start.getX() + (LineView.this.start.getWidth() / 4) < 0.0f || LineView.this.start.getX() > LineView.this.getWidth() - ((LineView.this.start.getWidth() / 4) * 3) || LineView.this.start.getY() + (LineView.this.start.getHeight() / 4) < 0.0f || LineView.this.start.getY() > LineView.this.getHeight() - ((LineView.this.start.getHeight() / 4) * 3)) {
                    LineView.this.start.offsetLeftAndRight(-i);
                    LineView.this.start.offsetTopAndBottom(-i2);
                    LineView.this.moveX = x;
                    LineView.this.moveY = y;
                }
                LineView.this.draw();
                return true;
            }
        });
        this.end.setOnTouchListener(new View.OnTouchListener() { // from class: view.LineView.2
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                int action = motionEvent.getAction();
                if (action == 0) {
                    LineView.this.moveX = x;
                    LineView.this.moveY = y;
                    return true;
                }
                if (action != 2) {
                    return true;
                }
                int i = x - LineView.this.moveX;
                int i2 = y - LineView.this.moveY;
                LineView.this.end.offsetLeftAndRight(i);
                LineView.this.end.offsetTopAndBottom(i2);
                LineView.this.invalidate();
                if (LineView.this.end.getX() + (LineView.this.end.getWidth() / 4) < 0.0f || LineView.this.end.getX() > LineView.this.getWidth() - ((LineView.this.end.getWidth() / 4) * 3) || LineView.this.end.getY() + (LineView.this.end.getHeight() / 4) < 0.0f || LineView.this.end.getY() > LineView.this.getHeight() - ((LineView.this.end.getHeight() / 4) * 3)) {
                    LineView.this.end.offsetLeftAndRight(-i);
                    LineView.this.end.offsetTopAndBottom(-i2);
                    LineView.this.moveX = x;
                    LineView.this.moveY = y;
                }
                LineView.this.draw();
                return true;
            }
        });
    }

    @Override // android.widget.RelativeLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.x = this.start.getWidth() / 2.0f;
        this.y = this.start.getHeight() / 2.0f;
        if (this.isFirst) {
            this.originalList.add(new PointF(this.start.getX() + ((float) this.x), this.start.getY() + ((float) this.y)));
            this.originalList.add(new PointF(this.end.getX() + ((float) this.x), this.end.getY() + ((float) this.y)));
            this.isFirst = false;
        }
        try {
            if (((CrossLineDetectBean) new Gson().fromJson(SharePreferenceManager.getInstance().getCrossLineDetect(this.iotId), CrossLineDetectBean.class)) != null) {
                this.start.setX(((r5.getEndX() * 1024.0f) / 640.0f) - ((float) this.x));
                this.start.setY(((r5.getEndY() * 576.0f) / 360.0f) - ((float) this.y));
                this.end.setX(((r5.getStartX() * 1024.0f) / 640.0f) - ((float) this.x));
                this.end.setY(((r5.getStartY() * 576.0f) / 360.0f) - ((float) this.y));
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        draw();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void draw() {
        float x = (float) (((double) this.start.getX()) + this.x);
        float y = (float) (((double) this.start.getY()) + this.y);
        PointF pointF = this.sp;
        pointF.x = x;
        pointF.y = y;
        float x2 = (float) (((double) this.end.getX()) + this.x);
        float y2 = (float) (((double) this.end.getY()) + this.y);
        PointF pointF2 = this.ep;
        pointF2.x = x2;
        pointF2.y = y2;
        this.canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        this.canvas.drawLine(x, y, x2, y2, this.paint);
        drawDirection(x, y, x2, y2);
        invalidate();
    }

    public void SaveBitmap(String str) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(SpUtil.getString(this.context, Utils.getDevSnapKey(str), ""), options);
        options.inJustDecodeBounds = false;
        options.inDensity = 440;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmapDecodeFile = BitmapFactory.decodeFile(SpUtil.getString(this.context, Utils.getDevSnapKey(str), ""), options);
        if (bitmapDecodeFile == null) {
            return;
        }
        Bitmap bitmapBit = bit(bitmapDecodeFile, 1024, 576);
        new Canvas(bitmapBit).drawBitmap(this.f8110b, 0.0f, 0.0f, this.paint);
        scanFile(bitmapBit, str);
    }

    private Bitmap bit(Bitmap bitmap, int i, int i2) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(i / width, i2 / height);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    private void scanFile(Bitmap bitmap, String str) {
        File file = new File(getFilesPath(this.context) + "/line/");
        if (file.exists() || file.mkdirs()) {
            String str2 = str + ".jpg";
            File file2 = new File(file, str2);
            if (file2.exists()) {
                file2.delete();
                file2 = new File(file, str2);
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFilesPath(Context context) {
        String path;
        if ("mounted".equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            path = context.getExternalFilesDir(null).getPath();
        } else {
            path = context.getFilesDir().getPath();
        }
        return path + "//" + Utils.getUserPhone();
    }

    private static double getDistanceBetween2Points(ImageView imageView, ImageView imageView2) {
        return Math.sqrt(Math.abs(((imageView.getX() - imageView2.getX()) * (imageView.getX() - imageView2.getX())) + ((imageView.getY() - imageView2.getY()) * (imageView.getY() - imageView2.getY()))));
    }

    private void drawDirection(float f, float f2, float f3, float f4) {
        Paint paint = new Paint();
        this.canvas.save();
        this.canvas.rotate(getRotationBetweenLines(f, f2, f3, f4), center(this.sp, this.ep).x, center(this.sp, this.ep).y);
        this.canvas.drawBitmap(zoomImg(BitmapFactory.decodeResource(getResources(), R.drawable.course, null), 50, 50), center(this.sp, this.ep).x - 25.0f, center(this.sp, this.ep).y - 25.0f, paint);
        this.canvas.restore();
    }

    public void changeDirection() {
        float x = this.start.getX();
        float y = this.start.getY();
        float x2 = this.end.getX();
        float y2 = this.end.getY();
        this.start.setX(x2);
        this.start.setY(y2);
        this.end.setX(x);
        this.end.setY(y);
        draw();
        invalidate();
    }

    public static PointF center(PointF pointF, PointF pointF2) {
        return new PointF((pointF.x + pointF2.x) / 2.0f, (pointF.y + pointF2.y) / 2.0f);
    }

    public static Bitmap zoomImg(Bitmap bitmap, int i, int i2) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(i / width, i2 / height);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static int getRotationBetweenLines(float f, float f2, float f3, float f4) {
        double d2 = ((double) (f2 - f2)) / ((double) ((2.0f * f) - f));
        double d3 = ((double) (f4 - f2)) / ((double) (f3 - f));
        double dAtan = Math.atan(Math.abs(d2 - d3) / ((d2 * d3) + 1.0d)) / 3.141592653589793d;
        double d4 = 180.0d;
        double d5 = dAtan * 180.0d;
        if (f3 > f && f4 < f2) {
            d4 = 90.0d - d5;
        } else if (f3 > f && f4 > f2) {
            d4 = d5 + 90.0d;
        } else if (f3 < f && f4 > f2) {
            d4 = 270.0d - d5;
        } else if (f3 < f && f4 < f2) {
            d4 = d5 + 270.0d;
        } else if ((f3 == f && f4 < f2) || f3 != f || f4 <= f2) {
            d4 = 0.0d;
        }
        return ((int) d4) - direction;
    }

    public void setIotId(String str) {
        this.iotId = str;
    }

    public static int dip2px(Context context, float f) {
        float f2 = context.getResources().getDisplayMetrics().density;
        Log.d("ContentValues", "dip2px: " + ((f / f2) + 0.5f));
        return (int) ((f * f2) + 0.5f);
    }

    public static int px2dip(Context context, int i) {
        return (int) TypedValue.applyDimension(1, i, context.getResources().getDisplayMetrics());
    }

    public ImageView getPic() {
        return this.pic;
    }

    public PointF getEp() {
        return this.ep;
    }

    public PointF getSp() {
        return this.sp;
    }

    public void clearCanvas() {
        this.canvas.drawColor(0, PorterDuff.Mode.CLEAR);
    }

    public void hide() {
        this.start.setVisibility(8);
        this.end.setVisibility(8);
        this.canvas.drawColor(0, PorterDuff.Mode.CLEAR);
    }

    public void display() {
        this.start.setVisibility(0);
        this.end.setVisibility(0);
        direction = 180;
        post(new Runnable() { // from class: view.LineView.3
            @Override // java.lang.Runnable
            public void run() {
                LineView.this.start.setX(((PointF) LineView.this.originalList.get(0)).x);
                LineView.this.start.setY(((PointF) LineView.this.originalList.get(0)).y);
                LineView.this.end.setX(((PointF) LineView.this.originalList.get(1)).x);
                LineView.this.end.setY(((PointF) LineView.this.originalList.get(1)).y);
                LineView.this.draw();
            }
        });
    }
}
