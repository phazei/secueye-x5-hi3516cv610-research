package com.alibaba.ailabs.tg.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import anet.channel.strategy.dispatch.DispatchConstants;
import com.alibaba.ailabs.tg.baseutils.R;

/* JADX INFO: loaded from: classes.dex */
public class StatusBarUtil {
    public static final int DEFAULT_STATUS_BAR_ALPHA = 112;
    private static final int FAKE_STATUS_BAR_VIEW_ID = R.id.statusbarutil_fake_status_bar_view;
    private static final int FAKE_TRANSLUCENT_VIEW_ID = R.id.statusbarutil_translucent_view;
    private static final int TAG_KEY_HAVE_SET_OFFSET = -123;

    private static int calculateStatusColor(@ColorInt int i, int i2) {
        if (i2 == 0) {
            return i;
        }
        float f = 1.0f - (i2 / 255.0f);
        return ((int) (((double) ((i & 255) * f)) + 0.5d)) | (((int) (((double) (((i >> 16) & 255) * f)) + 0.5d)) << 16) | ViewCompat.MEASURED_STATE_MASK | (((int) (((double) (((i >> 8) & 255) * f)) + 0.5d)) << 8);
    }

    public static void setColor(Activity activity2, @ColorInt int i) {
        setColor(activity2, i, 112);
    }

    public static void setColor(Activity activity2, @ColorInt int i, int i2) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity2.getWindow().addFlags(Integer.MIN_VALUE);
            activity2.getWindow().clearFlags(67108864);
            activity2.getWindow().setStatusBarColor(calculateStatusColor(i, i2));
        } else if (Build.VERSION.SDK_INT >= 19) {
            activity2.getWindow().addFlags(67108864);
            ViewGroup viewGroup = (ViewGroup) activity2.getWindow().getDecorView();
            View viewFindViewById = viewGroup.findViewById(FAKE_STATUS_BAR_VIEW_ID);
            if (viewFindViewById != null) {
                if (viewFindViewById.getVisibility() == 8) {
                    viewFindViewById.setVisibility(0);
                }
                viewFindViewById.setBackgroundColor(calculateStatusColor(i, i2));
            } else {
                viewGroup.addView(createStatusBarView(activity2, i, i2));
            }
            setRootView(activity2);
        }
    }

    public static void setColorForSwipeBack(Activity activity2, int i) {
        setColorForSwipeBack(activity2, i, 112);
    }

    public static void setColorForSwipeBack(Activity activity2, @ColorInt int i, int i2) {
        if (Build.VERSION.SDK_INT >= 19) {
            ViewGroup viewGroup = (ViewGroup) activity2.findViewById(android.R.id.content);
            View childAt = viewGroup.getChildAt(0);
            int statusBarHeight = getStatusBarHeight(activity2);
            if (childAt != null && (childAt instanceof CoordinatorLayout)) {
                final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) childAt;
                if (Build.VERSION.SDK_INT < 21) {
                    coordinatorLayout.setFitsSystemWindows(false);
                    viewGroup.setBackgroundColor(calculateStatusColor(i, i2));
                    if (viewGroup.getPaddingTop() < statusBarHeight) {
                        viewGroup.setPadding(0, statusBarHeight, 0, 0);
                        coordinatorLayout.post(new Runnable() { // from class: com.alibaba.ailabs.tg.utils.StatusBarUtil.1
                            @Override // java.lang.Runnable
                            public void run() {
                                coordinatorLayout.requestLayout();
                            }
                        });
                    }
                } else {
                    coordinatorLayout.setStatusBarBackgroundColor(calculateStatusColor(i, i2));
                }
            } else {
                viewGroup.setPadding(0, statusBarHeight, 0, 0);
                viewGroup.setBackgroundColor(calculateStatusColor(i, i2));
            }
            setTransparentForWindow(activity2);
        }
    }

    public static void setColorNoTranslucent(Activity activity2, @ColorInt int i) {
        setColor(activity2, i, 0);
    }

    @Deprecated
    public static void setColorDiff(Activity activity2, @ColorInt int i) {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        transparentStatusBar(activity2);
        ViewGroup viewGroup = (ViewGroup) activity2.findViewById(android.R.id.content);
        View viewFindViewById = viewGroup.findViewById(FAKE_STATUS_BAR_VIEW_ID);
        if (viewFindViewById != null) {
            if (viewFindViewById.getVisibility() == 8) {
                viewFindViewById.setVisibility(0);
            }
            viewFindViewById.setBackgroundColor(i);
        } else {
            viewGroup.addView(createStatusBarView(activity2, i));
        }
        setRootView(activity2);
    }

    public static void setTranslucent(Activity activity2) {
        setTranslucent(activity2, 112);
    }

    public static void setTranslucent(Activity activity2, int i) {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        setTransparent(activity2);
        addTranslucentView(activity2, i);
    }

    public static void setTranslucentForCoordinatorLayout(Activity activity2, int i) {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        transparentStatusBar(activity2);
        addTranslucentView(activity2, i);
    }

    public static void setTransparent(Activity activity2) {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        transparentStatusBar(activity2);
        setRootView(activity2);
    }

    @Deprecated
    public static void setTranslucentDiff(Activity activity2) {
        if (Build.VERSION.SDK_INT >= 19) {
            activity2.getWindow().addFlags(67108864);
            setRootView(activity2);
        }
    }

    public static void setColorForDrawerLayout(Activity activity2, DrawerLayout drawerLayout, @ColorInt int i) {
        setColorForDrawerLayout(activity2, drawerLayout, i, 112);
    }

    public static void setColorNoTranslucentForDrawerLayout(Activity activity2, DrawerLayout drawerLayout, @ColorInt int i) {
        setColorForDrawerLayout(activity2, drawerLayout, i, 0);
    }

    public static void setColorForDrawerLayout(Activity activity2, DrawerLayout drawerLayout, @ColorInt int i, int i2) {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            activity2.getWindow().addFlags(Integer.MIN_VALUE);
            activity2.getWindow().clearFlags(67108864);
            activity2.getWindow().setStatusBarColor(0);
        } else {
            activity2.getWindow().addFlags(67108864);
        }
        ViewGroup viewGroup = (ViewGroup) drawerLayout.getChildAt(0);
        View viewFindViewById = viewGroup.findViewById(FAKE_STATUS_BAR_VIEW_ID);
        if (viewFindViewById != null) {
            if (viewFindViewById.getVisibility() == 8) {
                viewFindViewById.setVisibility(0);
            }
            viewFindViewById.setBackgroundColor(i);
        } else {
            viewGroup.addView(createStatusBarView(activity2, i), 0);
        }
        if (!(viewGroup instanceof LinearLayout) && viewGroup.getChildAt(1) != null) {
            viewGroup.getChildAt(1).setPadding(viewGroup.getPaddingLeft(), getStatusBarHeight(activity2) + viewGroup.getPaddingTop(), viewGroup.getPaddingRight(), viewGroup.getPaddingBottom());
        }
        setDrawerLayoutProperty(drawerLayout, viewGroup);
        addTranslucentView(activity2, i2);
    }

    private static void setDrawerLayoutProperty(DrawerLayout drawerLayout, ViewGroup viewGroup) {
        ViewGroup viewGroup2 = (ViewGroup) drawerLayout.getChildAt(1);
        drawerLayout.setFitsSystemWindows(false);
        viewGroup.setFitsSystemWindows(false);
        viewGroup.setClipToPadding(true);
        viewGroup2.setFitsSystemWindows(false);
    }

    @Deprecated
    public static void setColorForDrawerLayoutDiff(Activity activity2, DrawerLayout drawerLayout, @ColorInt int i) {
        if (Build.VERSION.SDK_INT >= 19) {
            activity2.getWindow().addFlags(67108864);
            ViewGroup viewGroup = (ViewGroup) drawerLayout.getChildAt(0);
            View viewFindViewById = viewGroup.findViewById(FAKE_STATUS_BAR_VIEW_ID);
            if (viewFindViewById != null) {
                if (viewFindViewById.getVisibility() == 8) {
                    viewFindViewById.setVisibility(0);
                }
                viewFindViewById.setBackgroundColor(calculateStatusColor(i, 112));
            } else {
                viewGroup.addView(createStatusBarView(activity2, i), 0);
            }
            if (!(viewGroup instanceof LinearLayout) && viewGroup.getChildAt(1) != null) {
                viewGroup.getChildAt(1).setPadding(0, getStatusBarHeight(activity2), 0, 0);
            }
            setDrawerLayoutProperty(drawerLayout, viewGroup);
        }
    }

    public static void setTranslucentForDrawerLayout(Activity activity2, DrawerLayout drawerLayout) {
        setTranslucentForDrawerLayout(activity2, drawerLayout, 112);
    }

    public static void setTranslucentForDrawerLayout(Activity activity2, DrawerLayout drawerLayout, int i) {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        setTransparentForDrawerLayout(activity2, drawerLayout);
        addTranslucentView(activity2, i);
    }

    public static void setTransparentForDrawerLayout(Activity activity2, DrawerLayout drawerLayout) {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            activity2.getWindow().addFlags(Integer.MIN_VALUE);
            activity2.getWindow().clearFlags(67108864);
            activity2.getWindow().setStatusBarColor(0);
        } else {
            activity2.getWindow().addFlags(67108864);
        }
        ViewGroup viewGroup = (ViewGroup) drawerLayout.getChildAt(0);
        if (!(viewGroup instanceof LinearLayout) && viewGroup.getChildAt(1) != null) {
            viewGroup.getChildAt(1).setPadding(0, getStatusBarHeight(activity2), 0, 0);
        }
        setDrawerLayoutProperty(drawerLayout, viewGroup);
    }

    @Deprecated
    public static void setTranslucentForDrawerLayoutDiff(Activity activity2, DrawerLayout drawerLayout) {
        if (Build.VERSION.SDK_INT >= 19) {
            activity2.getWindow().addFlags(67108864);
            ViewGroup viewGroup = (ViewGroup) drawerLayout.getChildAt(0);
            viewGroup.setFitsSystemWindows(true);
            viewGroup.setClipToPadding(true);
            ((ViewGroup) drawerLayout.getChildAt(1)).setFitsSystemWindows(false);
            drawerLayout.setFitsSystemWindows(false);
        }
    }

    public static void setTransparentForImageView(Activity activity2, View view2) {
        setTranslucentForImageView(activity2, 0, view2);
    }

    public static void setTranslucentForImageView(Activity activity2, View view2) {
        setTranslucentForImageView(activity2, 112, view2);
    }

    public static void setTranslucentForImageView(Activity activity2, int i, View view2) {
        if (Build.VERSION.SDK_INT < 19) {
            if (view2 != null) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view2.getLayoutParams();
                marginLayoutParams.topMargin = (marginLayoutParams.topMargin - getStatusBarHeight(activity2)) + 15;
                view2.setLayoutParams(marginLayoutParams);
                return;
            }
            return;
        }
        setTransparentForWindow(activity2);
        addTranslucentView(activity2, i);
        if (view2 != null) {
            Object tag = view2.getTag(TAG_KEY_HAVE_SET_OFFSET);
            if (tag == null || !((Boolean) tag).booleanValue()) {
                ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) view2.getLayoutParams();
                marginLayoutParams2.setMargins(marginLayoutParams2.leftMargin, marginLayoutParams2.topMargin + getStatusBarHeight(activity2), marginLayoutParams2.rightMargin, marginLayoutParams2.bottomMargin);
                view2.setTag(TAG_KEY_HAVE_SET_OFFSET, true);
            }
        }
    }

    public static void setTranslucentForImageViewInFragment(Activity activity2, View view2) {
        setTranslucentForImageViewInFragment(activity2, 112, view2);
    }

    public static void setTranslucentForImageViewInFragment(Activity activity2, View view2, boolean z) {
        if (!z || Build.VERSION.SDK_INT >= 19) {
            return;
        }
        setTranslucentForImageViewInFragment(activity2, 0, view2);
    }

    public static void setTransparentForImageViewInFragment(Activity activity2, View view2) {
        setTranslucentForImageViewInFragment(activity2, 0, view2);
    }

    public static void setTranslucentForImageViewInFragment(Activity activity2, int i, View view2) {
        setTranslucentForImageView(activity2, i, view2);
        if (Build.VERSION.SDK_INT < 19 || Build.VERSION.SDK_INT >= 21) {
            return;
        }
        clearPreviousSetting(activity2);
    }

    public static void hideFakeStatusBarView(Activity activity2) {
        ViewGroup viewGroup = (ViewGroup) activity2.getWindow().getDecorView();
        View viewFindViewById = viewGroup.findViewById(FAKE_STATUS_BAR_VIEW_ID);
        if (viewFindViewById != null) {
            viewFindViewById.setVisibility(8);
        }
        View viewFindViewById2 = viewGroup.findViewById(FAKE_TRANSLUCENT_VIEW_ID);
        if (viewFindViewById2 != null) {
            viewFindViewById2.setVisibility(8);
        }
    }

    @TargetApi(19)
    private static void clearPreviousSetting(Activity activity2) {
        ViewGroup viewGroup = (ViewGroup) activity2.getWindow().getDecorView();
        View viewFindViewById = viewGroup.findViewById(FAKE_STATUS_BAR_VIEW_ID);
        if (viewFindViewById != null) {
            viewGroup.removeView(viewFindViewById);
            ((ViewGroup) ((ViewGroup) activity2.findViewById(android.R.id.content)).getChildAt(0)).setPadding(0, 0, 0, 0);
        }
    }

    private static void addTranslucentView(Activity activity2, int i) {
        ViewGroup viewGroup = (ViewGroup) activity2.findViewById(android.R.id.content);
        View viewFindViewById = viewGroup.findViewById(FAKE_TRANSLUCENT_VIEW_ID);
        if (viewFindViewById != null) {
            if (viewFindViewById.getVisibility() == 8) {
                viewFindViewById.setVisibility(0);
            }
            viewFindViewById.setBackgroundColor(Color.argb(i, 0, 0, 0));
            return;
        }
        viewGroup.addView(createTranslucentStatusBarView(activity2, i));
    }

    private static View createStatusBarView(Activity activity2, @ColorInt int i) {
        return createStatusBarView(activity2, i, 0);
    }

    private static View createStatusBarView(Activity activity2, @ColorInt int i, int i2) {
        View view2 = new View(activity2);
        view2.setLayoutParams(new LinearLayout.LayoutParams(-1, getStatusBarHeight(activity2)));
        view2.setBackgroundColor(calculateStatusColor(i, i2));
        view2.setId(FAKE_STATUS_BAR_VIEW_ID);
        return view2;
    }

    private static void setRootView(Activity activity2) {
        ViewGroup viewGroup = (ViewGroup) activity2.findViewById(android.R.id.content);
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof ViewGroup) {
                childAt.setFitsSystemWindows(true);
                ((ViewGroup) childAt).setClipToPadding(true);
            }
        }
    }

    private static void setTransparentForWindow(Activity activity2) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity2.getWindow().setStatusBarColor(0);
            activity2.getWindow().getDecorView().setSystemUiVisibility(1280);
        } else if (Build.VERSION.SDK_INT >= 19) {
            activity2.getWindow().setFlags(67108864, 67108864);
        }
    }

    @TargetApi(19)
    private static void transparentStatusBar(Activity activity2) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity2.getWindow().addFlags(Integer.MIN_VALUE);
            activity2.getWindow().clearFlags(67108864);
            activity2.getWindow().addFlags(134217728);
            activity2.getWindow().setStatusBarColor(0);
            return;
        }
        activity2.getWindow().addFlags(67108864);
    }

    private static View createTranslucentStatusBarView(Activity activity2, int i) {
        View view2 = new View(activity2);
        view2.setLayoutParams(new LinearLayout.LayoutParams(-1, getStatusBarHeight(activity2)));
        view2.setBackgroundColor(Color.argb(i, 0, 0, 0));
        view2.setId(FAKE_TRANSLUCENT_VIEW_ID);
        return view2;
    }

    public static int getStatusBarHeight(Context context) {
        return context.getResources().getDimensionPixelSize(context.getResources().getIdentifier("status_bar_height", "dimen", DispatchConstants.ANDROID));
    }

    @SuppressLint({"ObsoleteSdkInt"})
    public static void hideSystemNavigationBar(Activity activity2) {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            activity2.getWindow().getDecorView().setSystemUiVisibility(8);
        } else if (Build.VERSION.SDK_INT >= 19) {
            activity2.getWindow().getDecorView().setSystemUiVisibility(4102);
        }
    }

    public static void setStatusBarVisibility(@NonNull Activity activity2, boolean z) {
        setStatusBarVisibility(activity2.getWindow(), z);
    }

    public static void setStatusBarVisibility(@NonNull Window window, boolean z) {
        if (z) {
            window.clearFlags(1024);
        } else {
            window.addFlags(1024);
        }
    }

    static void setStatusBarLightMode(@NonNull Activity activity2, boolean z) {
        setStatusBarLightMode(activity2.getWindow(), z);
    }

    public static void setStatusBarLightMode(@NonNull Window window, boolean z) {
        View decorView;
        int i;
        if (Build.VERSION.SDK_INT < 23 || (decorView = window.getDecorView()) == null) {
            return;
        }
        int systemUiVisibility = decorView.getSystemUiVisibility();
        if (z) {
            window.addFlags(Integer.MIN_VALUE);
            i = systemUiVisibility | 8192;
        } else {
            i = systemUiVisibility & (-8193);
        }
        decorView.setSystemUiVisibility(i);
    }
}
