package activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.seculink.app.R;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import view.TitleView;

/* JADX INFO: compiled from: ShopActivity.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0014J\u0012\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0014J\u0012\u0010\t\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0014¨\u0006\n"}, d2 = {"Lactivity/ShopActivity;", "Lactivity/CommonActivity;", "()V", "getContentLayoutId", "", "initWidget", "", "savedInstanceState", "Landroid/os/Bundle;", "onCreate", "secueye_googleRelease"}, k = 1, mv = {1, 1, 15})
public final class ShopActivity extends CommonActivity {
    private HashMap _$_findViewCache;

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
        return R.layout.shop_activity_layout;
    }

    @Override // activity.CommonActivity
    protected void initWidget(@Nullable Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        ((TitleView) _$_findCachedViewById(R.id.fl_titlebar)).setOnViewClick(new TitleView.OnViewClick() { // from class: activity.ShopActivity.initWidget.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(@Nullable View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(@Nullable View view2) {
                ShopActivity.this.finish();
            }
        });
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView shop_web = (WebView) _$_findCachedViewById(R.id.shop_web);
        Intrinsics.checkExpressionValueIsNotNull(shop_web, "shop_web");
        WebSettings settings = shop_web.getSettings();
        Intrinsics.checkExpressionValueIsNotNull(settings, "shop_web.settings");
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        WebView shop_web2 = (WebView) _$_findCachedViewById(R.id.shop_web);
        Intrinsics.checkExpressionValueIsNotNull(shop_web2, "shop_web");
        WebSettings settings2 = shop_web2.getSettings();
        Intrinsics.checkExpressionValueIsNotNull(settings2, "shop_web.settings");
        settings2.setJavaScriptEnabled(true);
        WebView webView = (WebView) _$_findCachedViewById(R.id.shop_web);
        String stringExtra = getIntent().getStringExtra("url");
        if (stringExtra == null) {
            Intrinsics.throwNpe();
        }
        webView.loadUrl(stringExtra);
    }
}
