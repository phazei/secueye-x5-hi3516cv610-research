package view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.Random;

/* JADX INFO: loaded from: classes5.dex */
public class BadgeAnimator extends ValueAnimator {
    private BitmapFragment[][] mFragments;
    private WeakReference<BadgeView> mWeakBadge;

    public BadgeAnimator(Bitmap bitmap, PointF pointF, BadgeView badgeView) {
        this.mWeakBadge = new WeakReference<>(badgeView);
        setFloatValues(0.0f, 1.0f);
        setDuration(500L);
        this.mFragments = getFragments(bitmap, pointF);
        addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: view.BadgeAnimator.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                BadgeView badgeView2 = (BadgeView) BadgeAnimator.this.mWeakBadge.get();
                if (badgeView2 == null || !badgeView2.isShown()) {
                    BadgeAnimator.this.cancel();
                } else {
                    badgeView2.invalidate();
                }
            }
        });
        addListener(new AnimatorListenerAdapter() { // from class: view.BadgeAnimator.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                BadgeView badgeView2 = (BadgeView) BadgeAnimator.this.mWeakBadge.get();
                if (badgeView2 != null) {
                    badgeView2.reset();
                }
            }
        });
    }

    public void draw(Canvas canvas) {
        for (BitmapFragment[] bitmapFragmentArr : this.mFragments) {
            for (BitmapFragment bitmapFragment : bitmapFragmentArr) {
                bitmapFragment.update(Float.parseFloat(getAnimatedValue().toString()), canvas);
            }
        }
    }

    private BitmapFragment[][] getFragments(Bitmap bitmap, PointF pointF) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float fMin = Math.min(width, height) / 6.0f;
        float width2 = pointF.x - (bitmap.getWidth() / 2.0f);
        float height2 = pointF.y - (bitmap.getHeight() / 2.0f);
        BitmapFragment[][] bitmapFragmentArr = (BitmapFragment[][]) Array.newInstance((Class<?>) BitmapFragment.class, (int) (height / fMin), (int) (width / fMin));
        for (int i = 0; i < bitmapFragmentArr.length; i++) {
            for (int i2 = 0; i2 < bitmapFragmentArr[i].length; i2++) {
                BitmapFragment bitmapFragment = new BitmapFragment();
                float f = i2 * fMin;
                float f2 = i * fMin;
                bitmapFragment.color = bitmap.getPixel((int) f, (int) f2);
                bitmapFragment.x = f + width2;
                bitmapFragment.y = f2 + height2;
                bitmapFragment.size = fMin;
                bitmapFragment.maxSize = Math.max(width, height);
                bitmapFragmentArr[i][i2] = bitmapFragment;
            }
        }
        bitmap.recycle();
        return bitmapFragmentArr;
    }

    private static class BitmapFragment {
        int color;
        int maxSize;
        Paint paint = new Paint();
        Random random;
        float size;
        float x;
        float y;

        public BitmapFragment() {
            this.paint.setAntiAlias(true);
            this.paint.setStyle(Paint.Style.FILL);
            this.random = new Random();
        }

        public void update(float f, Canvas canvas) {
            this.paint.setColor(this.color);
            this.x += this.random.nextInt(this.maxSize) * 0.1f * (this.random.nextFloat() - 0.5f);
            this.y += this.random.nextInt(this.maxSize) * 0.1f * (this.random.nextFloat() - 0.5f);
            float f2 = this.x;
            float f3 = this.y;
            float f4 = this.size;
            canvas.drawCircle(f2, f3, f4 - (f * f4), this.paint);
        }
    }
}
