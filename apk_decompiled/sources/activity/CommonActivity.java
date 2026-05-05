package activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.alibaba.sdk.android.tbrest.utils.LogUtil;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.seculink.app.R;
import dialog.LoadingDialog;
import fragment.CommonFragment;
import java.util.List;
import tools.ActivityCore;
import tools.ActivityManager;
import tools.ProgressDialogUtil;
import tools.StatusBarUtil;
import view.SwipeBackLayout;

/* JADX INFO: loaded from: classes.dex */
public abstract class CommonActivity extends SwipeBackActivity2 {
    protected final String TAG = getClass().getSimpleName();
    private boolean isForeground = false;
    protected boolean isLand;
    private boolean isMain;
    private LoadingDialog loadingDialog;
    private boolean mAllFinished;
    private long mExitTime;
    protected SwipeBackLayout mSwipeBackLayout;
    private Toast mToast;
    private ProgressDialogUtil progressDialogUtil;
    private Handler setSensorHandler;

    protected void beforeInitWidget() {
    }

    protected Activity getActivity() {
        return this;
    }

    protected abstract int getContentLayoutId();

    protected boolean initArgs(Intent intent) {
        return true;
    }

    protected void initData() {
    }

    protected void initWidget(Bundle bundle) {
    }

    @Override // activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    @RequiresApi(api = 26)
    @SuppressLint({"SourceLockedOrientationActivity"})
    protected void onCreate(@Nullable Bundle bundle) {
        if (Build.VERSION.SDK_INT == 26 && ActivityCore.isTranslucentOrFloating(this)) {
            LogUtil.i("onCreate fixOrientation when Oreo, result = " + ActivityCore.fixOrientation(this));
        }
        super.onCreate(bundle);
        getWindow().addFlags(134217728);
        this.mSwipeBackLayout = getSwipeBackLayout();
        this.mSwipeBackLayout.setEdgeTrackingEnabled(1);
        this.progressDialogUtil = new ProgressDialogUtil();
        ActivityManager.getInstance().push(this);
        initWindows();
        if (initArgs(getIntent())) {
            setContentView(getContentLayoutId());
            beforeInitWidget();
            initWidget(bundle);
            initData();
            return;
        }
        finish();
    }

    public void setEdgeToEdge(View view2) {
        ViewCompat.setOnApplyWindowInsetsListener(view2, new OnApplyWindowInsetsListener() { // from class: activity.-$$Lambda$CommonActivity$zK-fGuV_Ef7-8iXXLd4mueTI2IA
            @Override // androidx.core.view.OnApplyWindowInsetsListener
            public final WindowInsetsCompat onApplyWindowInsets(View view3, WindowInsetsCompat windowInsetsCompat) {
                return CommonActivity.lambda$setEdgeToEdge$0(view3, windowInsetsCompat);
            }
        });
    }

    static /* synthetic */ WindowInsetsCompat lambda$setEdgeToEdge$0(View view2, WindowInsetsCompat windowInsetsCompat) {
        Insets insets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars());
        view2.setPadding(insets.left, view2.getPaddingTop(), insets.right, insets.bottom);
        return WindowInsetsCompat.CONSUMED;
    }

    protected void showProgressDialog(int i) {
        showProgressDialog(getString(i));
    }

    protected void showProgressDialog() {
        showProgressDialog((String) null);
    }

    protected void showProgressDialog(String str) {
        this.progressDialogUtil.showProgressDialog(this, str);
    }

    protected void dismissProgressDialog() {
        this.progressDialogUtil.dismissProgressDialog(this);
    }

    public void Back(View view2) {
        finish();
    }

    protected void setAllFinished(boolean z) {
        this.mAllFinished = z;
    }

    protected void setMain(boolean z) {
        this.isMain = z;
    }

    public static void setViewLayoutParams(View view2, int i, int i2) {
        ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
        if (layoutParams.height == i2 && layoutParams.width == i) {
            return;
        }
        layoutParams.width = i;
        layoutParams.height = i2;
        view2.setLayoutParams(layoutParams);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.isForeground = true;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        this.isForeground = false;
    }

    public boolean isActivityForeground() {
        return this.isForeground;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        ActivityManager.getInstance().pop(this);
        super.onDestroy();
    }

    protected void initWindows() {
        StatusBarUtil.setTranslucentStatus(getActivity());
        StatusBarUtil.setLightStatusBar(getActivity(), true);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.isLand) {
            changeScreenOrientation();
            return;
        }
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment2 : fragments) {
                if ((fragment2 instanceof CommonFragment) && ((CommonFragment) fragment2).onBackPressed()) {
                    return;
                }
            }
        }
        if (!this.mAllFinished) {
            super.onBackPressed();
        } else if (System.currentTimeMillis() - this.mExitTime > AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS) {
            Toast.makeText(this, R.string.exit_press_again, 0).show();
            this.mExitTime = System.currentTimeMillis();
        } else {
            ActivityManager.getInstance().clear();
        }
    }

    public void setBackgroundAlpha(float f) {
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha = f;
        getWindow().setAttributes(attributes);
    }

    protected void hideStateBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(5124);
        }
    }

    protected void showStateBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(1024);
        }
    }

    protected void changeScreenOrientation() {
        if (this.isLand) {
            setRequestedOrientation(1);
        } else {
            setRequestedOrientation(0);
        }
        if (this.setSensorHandler == null) {
            this.setSensorHandler = new Handler();
        }
        this.setSensorHandler.postDelayed(new Runnable() { // from class: activity.CommonActivity.1
            @Override // java.lang.Runnable
            public void run() {
                CommonActivity.this.setRequestedOrientation(4);
            }
        }, 3000L);
    }

    @Override // android.app.Activity
    public void setRequestedOrientation(int i) {
        if (Build.VERSION.SDK_INT == 26 && ActivityCore.isTranslucentOrFloating(this)) {
            LogUtil.i("avoid calling setRequestedOrientation when Oreo.");
        } else {
            super.setRequestedOrientation(i);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        if (configuration.orientation == 2) {
            this.isLand = true;
        } else {
            this.isLand = false;
        }
        super.onConfigurationChanged(configuration);
    }

    public void showLoadingDialog(String str) {
        if (isFinishing()) {
            LoadingDialog loadingDialog = this.loadingDialog;
            if (loadingDialog != null) {
                loadingDialog.dismiss();
                return;
            }
            return;
        }
        if (this.loadingDialog == null) {
            this.loadingDialog = new LoadingDialog(this);
        }
        if (str == null || str.isEmpty()) {
            this.loadingDialog.setMessage("正在请求...");
        } else {
            this.loadingDialog.setMessage("" + str);
        }
        if (this.loadingDialog.isShowing()) {
            return;
        }
        this.loadingDialog.show();
    }

    public void showLoadingSuccessDialog(String str) {
        if (isFinishing()) {
            LoadingDialog loadingDialog = this.loadingDialog;
            if (loadingDialog != null) {
                loadingDialog.dismiss();
                return;
            }
            return;
        }
        if (this.loadingDialog == null) {
            this.loadingDialog = new LoadingDialog(this);
        }
        this.loadingDialog.setMessage("" + str);
        if (!this.loadingDialog.isShowing()) {
            this.loadingDialog.show();
        }
        this.loadingDialog.dismiss();
    }

    public void showLoadingSuccessDialog(String str, int i) {
        if (isFinishing()) {
            LoadingDialog loadingDialog = this.loadingDialog;
            if (loadingDialog != null) {
                loadingDialog.dismiss();
                return;
            }
            return;
        }
        if (this.loadingDialog == null) {
            this.loadingDialog = new LoadingDialog(this);
        }
        this.loadingDialog.setMessage("" + str);
        if (!this.loadingDialog.isShowing()) {
            this.loadingDialog.show();
        }
        new Handler().postDelayed(new Runnable() { // from class: activity.CommonActivity.2
            @Override // java.lang.Runnable
            public void run() {
                if (CommonActivity.this.loadingDialog != null) {
                    CommonActivity.this.loadingDialog.dismiss();
                }
            }
        }, i);
    }

    public void showLoadingLongFailDialog(String str) {
        if (isFinishing()) {
            LoadingDialog loadingDialog = this.loadingDialog;
            if (loadingDialog != null) {
                loadingDialog.dismiss();
                return;
            }
            return;
        }
        if (this.loadingDialog == null) {
            this.loadingDialog = new LoadingDialog(this);
        }
        this.loadingDialog.setMessage("" + str);
        if (!this.loadingDialog.isShowing()) {
            this.loadingDialog.show();
        }
        new Handler().postDelayed(new Runnable() { // from class: activity.CommonActivity.3
            @Override // java.lang.Runnable
            public void run() {
                if (CommonActivity.this.loadingDialog != null) {
                    CommonActivity.this.loadingDialog.dismiss();
                }
            }
        }, 5000L);
    }

    public void showLoadingFailDialog(String str) {
        if (isFinishing()) {
            LoadingDialog loadingDialog = this.loadingDialog;
            if (loadingDialog != null) {
                loadingDialog.dismiss();
                return;
            }
            return;
        }
        if (this.loadingDialog == null) {
            this.loadingDialog = new LoadingDialog(this);
        }
        this.loadingDialog.setMessage("" + str);
        if (!this.loadingDialog.isShowing()) {
            this.loadingDialog.show();
        }
        new Handler().postDelayed(new Runnable() { // from class: activity.CommonActivity.4
            @Override // java.lang.Runnable
            public void run() {
                if (CommonActivity.this.loadingDialog != null) {
                    CommonActivity.this.loadingDialog.dismiss();
                }
            }
        }, 1000L);
    }

    public void showLoadingFailDialog(String str, int i) {
        if (isFinishing()) {
            LoadingDialog loadingDialog = this.loadingDialog;
            if (loadingDialog != null) {
                loadingDialog.dismiss();
                return;
            }
            return;
        }
        if (this.loadingDialog == null) {
            this.loadingDialog = new LoadingDialog(this);
        }
        this.loadingDialog.setMessage("" + str);
        if (!this.loadingDialog.isShowing()) {
            this.loadingDialog.show();
        }
        new Handler().postDelayed(new Runnable() { // from class: activity.CommonActivity.5
            @Override // java.lang.Runnable
            public void run() {
                if (CommonActivity.this.loadingDialog != null) {
                    CommonActivity.this.loadingDialog.dismiss();
                }
            }
        }, i);
    }

    public void hideLoadingDialog() {
        LoadingDialog loadingDialog = this.loadingDialog;
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    public void showToast(String str) {
        Toast toast = this.mToast;
        if (toast == null) {
            this.mToast = Toast.makeText(getApplicationContext(), str, 0);
        } else {
            toast.setText(str);
        }
        this.mToast.show();
    }
}
