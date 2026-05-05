package tools;

import android.app.Application;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.WifiProvisionUtConst;
import com.aliyun.iot.aep.component.router.Router;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes4.dex */
public class FloatWindowHelper {
    private static final int DOWN = 0;
    private static FloatWindowHelper floatWindowHelper;
    private Application application;
    private float mTouchStartX;
    private float mTouchStartY;
    private WindowManager.LayoutParams params;
    private WindowManager windowManager;
    private View windowView;
    private boolean needShowFloatWindowFlag = false;
    private volatile boolean isInit = false;
    private volatile boolean isAddWindow = false;
    private int mode = -1;

    private FloatWindowHelper(Application application) {
        this.application = application;
        initWindow();
    }

    public static FloatWindowHelper getInstance(Application application) {
        if (floatWindowHelper == null) {
            synchronized (FloatWindowHelper.class) {
                if (floatWindowHelper == null) {
                    floatWindowHelper = new FloatWindowHelper(application);
                }
            }
        }
        return floatWindowHelper;
    }

    private void initWindow() {
        this.windowManager = (WindowManager) this.application.getSystemService("window");
        LayoutInflater layoutInflaterFrom = LayoutInflater.from(this.application);
        this.params = getWindowParams();
        this.windowView = layoutInflaterFrom.inflate(R.layout.float_window, (ViewGroup) null);
        this.windowView.setOnTouchListener(new WindowTouchListener());
        this.windowView.findViewById(R.id.window_home_view).setOnClickListener(new View.OnClickListener() { // from class: tools.FloatWindowHelper.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Router.getInstance().toUrl(FloatWindowHelper.this.application, "page/main");
            }
        });
        this.windowView.findViewById(R.id.window_about_view).setOnClickListener(new View.OnClickListener() { // from class: tools.FloatWindowHelper.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Router.getInstance().toUrl(FloatWindowHelper.this.application, "page/about");
            }
        });
        this.windowView.findViewById(R.id.window_log_view).setOnClickListener(new View.OnClickListener() { // from class: tools.FloatWindowHelper.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Router.getInstance().toUrl(FloatWindowHelper.this.application, "page/alog");
            }
        });
        this.windowView.findViewById(R.id.window_scan_view).setOnClickListener(new View.OnClickListener() { // from class: tools.FloatWindowHelper.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Router.getInstance().toUrl(FloatWindowHelper.this.application, "page/scan");
            }
        });
        this.isInit = true;
    }

    public void setNeedShowFloatWindowFlag(boolean z) {
        this.needShowFloatWindowFlag = z;
        showFloatWindow();
    }

    public void showFloatWindow() {
        if (!this.isInit) {
            initWindow();
        }
        if (!this.needShowFloatWindowFlag) {
            if (this.isAddWindow) {
                removeFloatWindow();
            }
        } else {
            if (this.isAddWindow) {
                return;
            }
            this.isAddWindow = true;
            this.windowManager.addView(this.windowView, this.params);
        }
    }

    public void removeFloatWindow() {
        if (this.isAddWindow) {
            this.windowManager.removeView(this.windowView);
        }
        this.isAddWindow = false;
    }

    private WindowManager.LayoutParams getWindowParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.format = 1;
        layoutParams.width = (int) ScreenTools.convertDp2Px(this.application, 50.0f);
        layoutParams.height = (int) ScreenTools.convertDp2Px(this.application, 180.0f);
        if (Build.VERSION.SDK_INT < 19 || Build.VERSION.SDK_INT > 24) {
            layoutParams.type = WifiProvisionUtConst.FEIYAN_BROADCAST_MSG_ERROR;
        } else {
            layoutParams.type = WifiProvisionUtConst.FEIYAN_ADD_DEVICE_ERROR;
        }
        layoutParams.flags = 40;
        layoutParams.gravity = 51;
        layoutParams.x = (int) (ScreenTools.getScreenWidth(this.application) - ScreenTools.convertDp2Px(this.application, 68.0f));
        layoutParams.y = (int) (ScreenTools.getScreenHeight(this.application) - ScreenTools.convertDp2Px(this.application, 207.0f));
        return layoutParams;
    }

    private class WindowTouchListener implements View.OnTouchListener {
        private WindowTouchListener() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view2, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case 0:
                    FloatWindowHelper.this.mode = 0;
                    FloatWindowHelper.this.mTouchStartX = motionEvent.getRawX();
                    FloatWindowHelper.this.mTouchStartY = motionEvent.getRawY();
                    return false;
                case 1:
                    FloatWindowHelper.this.mode = -1;
                    return false;
                case 2:
                    if (FloatWindowHelper.this.mode == 0) {
                        float rawX = motionEvent.getRawX();
                        float rawY = motionEvent.getRawY();
                        FloatWindowHelper.this.params.x = (int) (r1.x + (rawX - FloatWindowHelper.this.mTouchStartX));
                        FloatWindowHelper.this.params.y = (int) (r1.y + (rawY - FloatWindowHelper.this.mTouchStartY));
                        FloatWindowHelper.this.windowManager.updateViewLayout(FloatWindowHelper.this.windowView, FloatWindowHelper.this.params);
                        FloatWindowHelper.this.mTouchStartX = rawX;
                        FloatWindowHelper.this.mTouchStartY = rawY;
                    }
                    return false;
                default:
                    return false;
            }
        }
    }
}
