package kt;

import activity.CommonActivity;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import bean.AreaPointBean;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.seculink.app.R;
import config.Constants;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sdk.IPCManager;
import view.ItemView;
import view.TitleView;

/* JADX INFO: compiled from: AreaDetectActivity.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u000b\u001a\u00020\fH\u0014J\u0012\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0014J\b\u0010\u0011\u001a\u00020\u000eH\u0002J\b\u0010\u0012\u001a\u00020\u000eH\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lkt/AreaDetectActivity;", "Lactivity/CommonActivity;", "()V", "iotId", "", "getIotId", "()Ljava/lang/String;", "setIotId", "(Ljava/lang/String;)V", "uiHandler", "Landroid/os/Handler;", "getContentLayoutId", "", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "saveBitmap", "saveEdit", "secueye_googleRelease"}, k = 1, mv = {1, 1, 15})
public final class AreaDetectActivity extends CommonActivity {
    private HashMap _$_findViewCache;

    @NotNull
    public String iotId;
    private final Handler uiHandler = new Handler();

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

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_area_detect_layout;
    }

    @NotNull
    public final String getIotId() {
        String str = this.iotId;
        if (str == null) {
            Intrinsics.throwUninitializedPropertyAccessException("iotId");
        }
        return str;
    }

    public final void setIotId(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.iotId = str;
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String stringExtra = getIntent().getStringExtra("iotId");
        if (stringExtra == null) {
            Intrinsics.throwNpe();
        }
        this.iotId = stringExtra;
        setSwipeBackEnable(false);
        AreaView areaView = (AreaView) _$_findCachedViewById(R.id.areaView);
        String str = this.iotId;
        if (str == null) {
            Intrinsics.throwUninitializedPropertyAccessException("iotId");
        }
        areaView.setBackground(str);
        ((TitleView) _$_findCachedViewById(R.id.titleView)).setOnViewClick(new TitleView.OnViewClick() { // from class: kt.AreaDetectActivity.onCreate.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(@Nullable View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(@Nullable View view2) {
                AreaDetectActivity.this.finish();
            }
        });
        ((Button) _$_findCachedViewById(R.id.save)).setOnClickListener(new View.OnClickListener() { // from class: kt.AreaDetectActivity.onCreate.2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                AreaDetectActivity.this.saveEdit();
            }
        });
        ((ItemView) _$_findCachedViewById(R.id.reset_item)).setOnClickListener(new View.OnClickListener() { // from class: kt.AreaDetectActivity.onCreate.3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ((AreaView) AreaDetectActivity.this._$_findCachedViewById(R.id.areaView)).reSet();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void saveEdit() {
        saveBitmap();
        List<PointF> point = ((AreaView) _$_findCachedViewById(R.id.areaView)).getPoint();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        AreaPointBean areaPointBean = new AreaPointBean();
        AreaView areaView = (AreaView) _$_findCachedViewById(R.id.areaView);
        Intrinsics.checkExpressionValueIsNotNull(areaView, "areaView");
        areaView.getWidth();
        int bitmapWidth = ((AreaView) _$_findCachedViewById(R.id.areaView)).getBitmapWidth();
        int bitmapHeight = ((AreaView) _$_findCachedViewById(R.id.areaView)).getBitmapHeight();
        areaPointBean.setEnable(1);
        float f = bitmapWidth;
        float f2 = 640;
        areaPointBean.setLeftTop_X(Integer.valueOf((int) ((point.get(0).x / f) * f2)));
        float f3 = bitmapHeight;
        float f4 = 360;
        areaPointBean.setLeftTop_Y(Integer.valueOf((int) ((point.get(0).y / f3) * f4)));
        areaPointBean.setRightTop_X(Integer.valueOf((int) ((point.get(1).x / f) * f2)));
        areaPointBean.setRightTop_Y(Integer.valueOf((int) ((point.get(1).y / f3) * f4)));
        areaPointBean.setRightBottom_X(Integer.valueOf((int) ((point.get(2).x / f) * f2)));
        areaPointBean.setRightBottom_Y(Integer.valueOf((int) ((point.get(2).y / f3) * f4)));
        areaPointBean.setLeftBottom_X(Integer.valueOf((int) ((point.get(3).x / f) * f2)));
        areaPointBean.setLeftBottom_Y(Integer.valueOf((int) ((point.get(3).y / f3) * f4)));
        linkedHashMap.put(Constants.RegionDetect, areaPointBean);
        IPCManager iPCManager = IPCManager.getInstance();
        String str = this.iotId;
        if (str == null) {
            Intrinsics.throwUninitializedPropertyAccessException("iotId");
        }
        iPCManager.getDevice(str).setProperties(linkedHashMap, new IPanelCallback() { // from class: kt.AreaDetectActivity.saveEdit.1
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public final void onComplete(boolean z, @Nullable Object obj) {
                Log.d(AreaDetectActivity.this.TAG, "onComplete: ----" + z + String.valueOf(obj));
                if (z && obj != null && (!Intrinsics.areEqual("", obj.toString()))) {
                    JSONObject object = JSONObject.parseObject(obj.toString());
                    if (object.containsKey("code")) {
                        Integer integer = object.getInteger("code");
                        if (integer != null && integer.intValue() == 200) {
                            AreaDetectActivity.this.uiHandler.post(new Runnable() { // from class: kt.AreaDetectActivity.saveEdit.1.2
                                @Override // java.lang.Runnable
                                public final void run() {
                                    Toast.makeText(AreaDetectActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                }
                            });
                        } else {
                            AreaDetectActivity.this.uiHandler.post(new Runnable() { // from class: kt.AreaDetectActivity.saveEdit.1.1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    Toast.makeText(AreaDetectActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    private final void saveBitmap() {
        ((AreaView) _$_findCachedViewById(R.id.areaView)).getBitmap();
    }
}
