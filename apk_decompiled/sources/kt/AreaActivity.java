package kt;

import activity.CommonActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.widget.SwitchCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.seculink.app.R;
import java.io.File;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tools.SharePreferenceManager;
import tools.Utils;
import view.ItemView;
import view.TitleView;

/* JADX INFO: compiled from: AreaActivity.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0014J\u000e\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bJ\u0012\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0014J\b\u0010\u0010\u001a\u00020\rH\u0014J\b\u0010\u0011\u001a\u00020\rH\u0015R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lkt/AreaActivity;", "Lactivity/CommonActivity;", "()V", "iotId", "", "swArea", "Landroidx/appcompat/widget/SwitchCompat;", "getContentLayoutId", "", "getFilesPath", "context", "Landroid/content/Context;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onPause", "onResume", "secueye_googleRelease"}, k = 1, mv = {1, 1, 15})
public final class AreaActivity extends CommonActivity {
    private HashMap _$_findViewCache;
    private String iotId;
    private SwitchCompat swArea;

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
        return R.layout.activity_area_layout;
    }

    public static final /* synthetic */ String access$getIotId$p(AreaActivity areaActivity) {
        String str = areaActivity.iotId;
        if (str == null) {
            Intrinsics.throwUninitializedPropertyAccessException("iotId");
        }
        return str;
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TitleView) _$_findCachedViewById(R.id.titleView)).setOnViewClick(new TitleView.OnViewClick() { // from class: kt.AreaActivity.onCreate.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(@Nullable View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(@Nullable View view2) {
                AreaActivity.this.finish();
            }
        });
        String stringExtra = getIntent().getStringExtra("iotId");
        if (stringExtra == null) {
            Intrinsics.throwNpe();
        }
        this.iotId = stringExtra;
        ((ItemView) _$_findCachedViewById(R.id.area_edit)).setOnClickListener(new View.OnClickListener() { // from class: kt.AreaActivity.onCreate.2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                Intent intent = new Intent(AreaActivity.this.getActivity(), (Class<?>) AreaDetectActivity.class);
                intent.putExtra("iotId", AreaActivity.access$getIotId$p(AreaActivity.this));
                AreaActivity.this.startActivity(intent);
            }
        });
        ((ItemView) _$_findCachedViewById(R.id.display_lineArea)).addRightView(new SwitchCompat(this));
        ItemView display_lineArea = (ItemView) _$_findCachedViewById(R.id.display_lineArea);
        Intrinsics.checkExpressionValueIsNotNull(display_lineArea, "display_lineArea");
        View rightView = display_lineArea.getRightView();
        if (rightView == null) {
            throw new TypeCastException("null cannot be cast to non-null type androidx.appcompat.widget.SwitchCompat");
        }
        this.swArea = (SwitchCompat) rightView;
        SwitchCompat switchCompat = this.swArea;
        if (switchCompat == null) {
            Intrinsics.throwUninitializedPropertyAccessException("swArea");
        }
        switchCompat.setTextOff("");
        SwitchCompat switchCompat2 = this.swArea;
        if (switchCompat2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("swArea");
        }
        switchCompat2.setTextOn("");
        SwitchCompat switchCompat3 = this.swArea;
        if (switchCompat3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("swArea");
        }
        switchCompat3.setText("");
        SwitchCompat switchCompat4 = this.swArea;
        if (switchCompat4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("swArea");
        }
        switchCompat4.setThumbDrawable((Drawable) null);
        SwitchCompat switchCompat5 = this.swArea;
        if (switchCompat5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("swArea");
        }
        switchCompat5.setBackgroundResource(R.drawable.sel_switch);
        SwitchCompat switchCompat6 = this.swArea;
        if (switchCompat6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("swArea");
        }
        SharePreferenceManager sharePreferenceManager = SharePreferenceManager.getInstance();
        String str = this.iotId;
        if (str == null) {
            Intrinsics.throwUninitializedPropertyAccessException("iotId");
        }
        switchCompat6.setChecked((sharePreferenceManager.getAreaDetectEnable(str).intValue() >> 1) == 1);
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

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    @SuppressLint({"CheckResult"})
    protected void onResume() {
        super.onResume();
        StringBuilder sb = new StringBuilder();
        sb.append(getFilesPath(this));
        sb.append("/area/");
        String str = this.iotId;
        if (str == null) {
            Intrinsics.throwUninitializedPropertyAccessException("iotId");
        }
        sb.append(str);
        sb.append(".jpg");
        String string = sb.toString();
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true);
        Glide.with(getApplicationContext()).load(string).apply(requestOptions).into((ImageView) _$_findCachedViewById(R.id.image));
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
    }
}
