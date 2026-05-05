package activity;

import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.FragmentActivity;
import me.imid.swipebacklayout.lib.Utils;
import view.SwipeBackActivityBase;
import view.SwipeBackActivityHelper;
import view.SwipeBackLayout;

/* JADX INFO: loaded from: classes.dex */
public class SwipeBackActivity2 extends FragmentActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mHelper = new SwipeBackActivityHelper(this);
        this.mHelper.onActivityCreate();
    }

    @Override // android.app.Activity
    protected void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        this.mHelper.onPostCreate();
    }

    @Override // android.app.Activity
    public View findViewById(int i) {
        SwipeBackActivityHelper swipeBackActivityHelper;
        View viewFindViewById = super.findViewById(i);
        return (viewFindViewById != null || (swipeBackActivityHelper = this.mHelper) == null) ? viewFindViewById : swipeBackActivityHelper.findViewById(i);
    }

    @Override // view.SwipeBackActivityBase
    public SwipeBackLayout getSwipeBackLayout() {
        return this.mHelper.getSwipeBackLayout();
    }

    @Override // view.SwipeBackActivityBase
    public void setSwipeBackEnable(boolean z) {
        getSwipeBackLayout().setEnableGesture(z);
    }

    @Override // view.SwipeBackActivityBase
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
}
