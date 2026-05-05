package kt;

import activity.CommonActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.seculink.app.R;
import java.util.ArrayList;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import kt.HelpAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import view.TitleView;

/* JADX INFO: compiled from: HelpActivity.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0014J\u0012\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0015J\b\u0010\r\u001a\u00020\nH\u0014J\b\u0010\u000e\u001a\u00020\nH\u0014J\b\u0010\u000f\u001a\u00020\nH\u0014J\u0010\u0010\u0010\u001a\u00020\n2\u0006\u0010\u0011\u001a\u00020\u0005H\u0002R\u001e\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lkt/HelpActivity;", "Lactivity/CommonActivity;", "()V", "settingList", "Ljava/util/ArrayList;", "", "Lkotlin/collections/ArrayList;", "getContentLayoutId", "", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onPause", "onResume", "openWebView", "url", "secueye_googleRelease"}, k = 1, mv = {1, 1, 15})
public final class HelpActivity extends CommonActivity {
    private HashMap _$_findViewCache;
    private ArrayList<String> settingList = CollectionsKt.arrayListOf("二维码联网", "智能联网", "4G联网", "网线连接");

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
        return R.layout.activity_help_layout;
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    @SuppressLint({"SetJavaScriptEnabled"})
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TitleView) _$_findCachedViewById(R.id.titleView)).setOnViewClick(new TitleView.OnViewClick() { // from class: kt.HelpActivity.onCreate.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(@Nullable View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(@Nullable View view2) {
                WebView webView = (WebView) HelpActivity.this._$_findCachedViewById(R.id.webView);
                Intrinsics.checkExpressionValueIsNotNull(webView, "webView");
                if (webView.getVisibility() == 8) {
                    HelpActivity.this.finish();
                    return;
                }
                WebView webView2 = (WebView) HelpActivity.this._$_findCachedViewById(R.id.webView);
                Intrinsics.checkExpressionValueIsNotNull(webView2, "webView");
                webView2.setVisibility(8);
                ((WebView) HelpActivity.this._$_findCachedViewById(R.id.webView)).onPause();
            }
        });
        RecyclerView recycler_help_one = (RecyclerView) _$_findCachedViewById(R.id.recycler_help_one);
        Intrinsics.checkExpressionValueIsNotNull(recycler_help_one, "recycler_help_one");
        HelpActivity helpActivity = this;
        recycler_help_one.setLayoutManager(new GridLayoutManager(helpActivity, 2));
        RecyclerView recycler_help_one2 = (RecyclerView) _$_findCachedViewById(R.id.recycler_help_one);
        Intrinsics.checkExpressionValueIsNotNull(recycler_help_one2, "recycler_help_one");
        recycler_help_one2.setAdapter(new HelpAdapter(this.settingList, new HelpAdapter.ImageViewClick() { // from class: kt.HelpActivity.onCreate.2
            @Override // kt.HelpAdapter.ImageViewClick
            public void onClick(@NotNull String string) {
                Intrinsics.checkParameterIsNotNull(string, "string");
                HelpActivity.this.openWebView(string);
            }
        }));
        RecyclerView recycler_help_two = (RecyclerView) _$_findCachedViewById(R.id.recycler_help_two);
        Intrinsics.checkExpressionValueIsNotNull(recycler_help_two, "recycler_help_two");
        recycler_help_two.setLayoutManager(new GridLayoutManager(helpActivity, 2));
        RecyclerView recycler_help_two2 = (RecyclerView) _$_findCachedViewById(R.id.recycler_help_two);
        Intrinsics.checkExpressionValueIsNotNull(recycler_help_two2, "recycler_help_two");
        recycler_help_two2.setAdapter(new HelpAdapter(this.settingList, new HelpAdapter.ImageViewClick() { // from class: kt.HelpActivity.onCreate.3
            @Override // kt.HelpAdapter.ImageViewClick
            public void onClick(@NotNull String string) {
                Intrinsics.checkParameterIsNotNull(string, "string");
                HelpActivity.this.openWebView(string);
            }
        }));
        WebView webView = (WebView) _$_findCachedViewById(R.id.webView);
        Intrinsics.checkExpressionValueIsNotNull(webView, "webView");
        webView.setWebViewClient(new WebViewClient() { // from class: kt.HelpActivity.onCreate.4
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(@NotNull WebView view2, @NotNull String url) {
                Intrinsics.checkParameterIsNotNull(view2, "view");
                Intrinsics.checkParameterIsNotNull(url, "url");
                try {
                    if (StringsKt.startsWith$default(url, "http:", false, 2, (Object) null) || StringsKt.startsWith$default(url, "https:", false, 2, (Object) null)) {
                        view2.loadUrl(url);
                    } else {
                        HelpActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                    }
                    return true;
                } catch (Exception unused) {
                    return false;
                }
            }
        });
        WebView webView2 = (WebView) _$_findCachedViewById(R.id.webView);
        Intrinsics.checkExpressionValueIsNotNull(webView2, "webView");
        WebSettings settings = webView2.getSettings();
        Intrinsics.checkExpressionValueIsNotNull(settings, "webView.settings");
        settings.setJavaScriptEnabled(true);
        WebView webView3 = (WebView) _$_findCachedViewById(R.id.webView);
        Intrinsics.checkExpressionValueIsNotNull(webView3, "webView");
        webView3.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void openWebView(String url) {
        WebView webView = (WebView) _$_findCachedViewById(R.id.webView);
        Intrinsics.checkExpressionValueIsNotNull(webView, "webView");
        webView.setVisibility(0);
        ((WebView) _$_findCachedViewById(R.id.webView)).loadUrl(url);
        ((WebView) _$_findCachedViewById(R.id.webView)).onResume();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        ((WebView) _$_findCachedViewById(R.id.webView)).onPause();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        ((WebView) _$_findCachedViewById(R.id.webView)).onResume();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        ((WebView) _$_findCachedViewById(R.id.webView)).destroy();
    }
}
