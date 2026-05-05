package view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.seculink.app.R;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes5.dex */
public class CircleTooView extends RelativeLayout {
    private static int direction = 180;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    Bitmap f8106b;
    private Canvas canvas;
    private Context context;
    private String iotId;
    private boolean isFirst;
    private setOnLister mListener;
    private int moveX;
    private int moveY;
    private List<PointF> originalList;
    Paint paint;
    private PointF sp;
    private ImageView start;
    private ImageView test;
    private double x;
    private double y;

    public interface setOnLister {
        void setPointControl();
    }

    public CircleTooView(Context context) {
        super(context);
        this.isFirst = true;
        this.originalList = new ArrayList();
        initView(context);
        this.context = context;
    }

    public CircleTooView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.isFirst = true;
        this.originalList = new ArrayList();
        initView(context);
        this.context = context;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @SuppressLint({"ClickableViewAccessibility"})
    private void initView(Context context) {
        if (this.mListener != null) {
            this.mListener = (setOnLister) context;
        }
        LayoutInflater.from(context).inflate(R.layout.circle_control_view, (ViewGroup) this, true);
        this.sp = new PointF();
        this.paint = new Paint();
        this.paint.setColor(getResources().getColor(R.color.colorAccent));
        this.paint.setStrokeWidth(5.0f);
        this.start = (ImageView) findViewById(R.id.start);
        this.test = (ImageView) findViewById(R.id.test);
        this.f8106b = Bitmap.createBitmap(1024, 800, Bitmap.Config.ARGB_8888);
        this.canvas = new Canvas(this.f8106b);
        this.test.setImageBitmap(this.f8106b);
        this.start.setOnTouchListener(new View.OnTouchListener() { // from class: view.CircleTooView.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                switch (motionEvent.getAction()) {
                    case 0:
                        CircleTooView.this.moveX = x;
                        CircleTooView.this.moveY = y;
                        break;
                    case 1:
                        if (CircleTooView.this.mListener != null) {
                            CircleTooView.this.mListener.setPointControl();
                        }
                        break;
                    case 2:
                        int i = x - CircleTooView.this.moveX;
                        int i2 = y - CircleTooView.this.moveY;
                        CircleTooView.this.start.offsetLeftAndRight(i);
                        CircleTooView.this.start.offsetTopAndBottom(i2);
                        CircleTooView.this.invalidate();
                        if (CircleTooView.this.start.getX() + (CircleTooView.this.start.getWidth() / 4) < 0.0f || CircleTooView.this.start.getX() > CircleTooView.this.getWidth() - ((CircleTooView.this.start.getWidth() / 4) * 3) || CircleTooView.this.start.getY() + (CircleTooView.this.start.getHeight() / 4) < 0.0f || CircleTooView.this.start.getY() > CircleTooView.this.getHeight() - ((CircleTooView.this.start.getHeight() / 4) * 3)) {
                            CircleTooView.this.start.offsetLeftAndRight(-i);
                            CircleTooView.this.start.offsetTopAndBottom(-i2);
                            CircleTooView.this.moveX = x;
                            CircleTooView.this.moveY = y;
                        }
                        CircleTooView.this.draw();
                        break;
                }
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
            this.isFirst = false;
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
        this.canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public static int dip2px(Context context, float f) {
        float f2 = context.getResources().getDisplayMetrics().density;
        Log.d("ContentValues", "dip2px: " + ((f / f2) + 0.5f));
        return (int) ((f * f2) + 0.5f);
    }

    public static int px2dip(Context context, int i) {
        return (int) TypedValue.applyDimension(1, i, context.getResources().getDisplayMetrics());
    }

    public PointF getSp() {
        return this.sp;
    }

    public void clearCanvas() {
        this.canvas.drawColor(0, PorterDuff.Mode.CLEAR);
    }

    public void hide() {
        this.start.setVisibility(8);
        this.canvas.drawColor(0, PorterDuff.Mode.CLEAR);
    }

    public void display() {
        this.start.setVisibility(0);
        direction = 180;
        post(new Runnable() { // from class: view.CircleTooView.2
            @Override // java.lang.Runnable
            public void run() {
                CircleTooView.this.start.setX(((PointF) CircleTooView.this.originalList.get(0)).x);
                CircleTooView.this.start.setY(((PointF) CircleTooView.this.originalList.get(0)).y);
                CircleTooView.this.draw();
            }
        });
    }

    public void setOnMotionEventListener(setOnLister setonlister) {
        this.mListener = setonlister;
    }
}
