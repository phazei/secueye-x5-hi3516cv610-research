package view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.seculink.app.R;
import me.imid.swipebacklayout.lib.Utils;
import view.SwipeBackLayout;

/* JADX INFO: loaded from: classes5.dex */
public class SwipeBackActivityHelper {
    private Activity mActivity;
    private SwipeBackLayout mSwipeBackLayout;

    public SwipeBackActivityHelper(Activity activity2) {
        this.mActivity = activity2;
    }

    public void onActivityCreate() {
        this.mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.mActivity.getWindow().getDecorView().setBackgroundDrawable(null);
        this.mSwipeBackLayout = (SwipeBackLayout) LayoutInflater.from(this.mActivity).inflate(R.layout.swipeback_layout2, (ViewGroup) null);
        this.mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() { // from class: view.SwipeBackActivityHelper.1
            @Override // view.SwipeBackLayout.SwipeListener
            public void onScrollOverThreshold() {
            }

            @Override // view.SwipeBackLayout.SwipeListener
            public void onScrollStateChange(int i, float f) {
            }

            @Override // view.SwipeBackLayout.SwipeListener
            public void onEdgeTouch(int i) {
                Utils.convertActivityToTranslucent(SwipeBackActivityHelper.this.mActivity);
            }
        });
    }

    public void onPostCreate() {
        this.mSwipeBackLayout.attachToActivity(this.mActivity);
    }

    public View findViewById(int i) {
        SwipeBackLayout swipeBackLayout = this.mSwipeBackLayout;
        if (swipeBackLayout != null) {
            return swipeBackLayout.findViewById(i);
        }
        return null;
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return this.mSwipeBackLayout;
    }
}
