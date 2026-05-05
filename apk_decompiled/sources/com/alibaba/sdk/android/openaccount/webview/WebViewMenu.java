package com.alibaba.sdk.android.openaccount.webview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class WebViewMenu extends PopupWindow {
    private Context context;
    private WebViewMenuAdapter menuAdapter;
    private List<MenuItem> menuItemList;
    private ListView menuListView;

    public WebViewMenu(Activity activity2) {
        super(activity2);
        this.context = activity2;
        this.menuListView = (ListView) ((LayoutInflater) activity2.getSystemService("layout_inflater")).inflate(ResourceUtils.getRLayout(activity2, "com_taobao_tae_sdk_web_view_menu"), (ViewGroup) null);
        this.menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.alibaba.sdk.android.openaccount.webview.WebViewMenu.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
                ((MenuItem) WebViewMenu.this.menuItemList.get(i)).onClickListener.onClick(view2);
                WebViewMenu.this.dismiss();
            }
        });
        setContentView(this.menuListView);
        setWidth(CommonUtils.dp2px(activity2, 200.0f));
        setHeight(-2);
        setFocusable(true);
        setTouchable(true);
        this.menuListView.setFocusable(true);
        this.menuListView.setFocusableInTouchMode(true);
        setBackgroundDrawable(new PaintDrawable());
        setOutsideTouchable(true);
        this.menuListView.setOnKeyListener(new View.OnKeyListener() { // from class: com.alibaba.sdk.android.openaccount.webview.WebViewMenu.2
            @Override // android.view.View.OnKeyListener
            public boolean onKey(View view2, int i, KeyEvent keyEvent) {
                if (i != 4) {
                    return false;
                }
                WebViewMenu.this.dismiss();
                return false;
            }
        });
    }

    public void setAdapter(List<MenuItem> list) {
        this.menuItemList = list;
        if (list == null || list.size() <= 0) {
            return;
        }
        this.menuAdapter = new WebViewMenuAdapter(this.context, list);
        this.menuListView.setAdapter((ListAdapter) this.menuAdapter);
    }
}
