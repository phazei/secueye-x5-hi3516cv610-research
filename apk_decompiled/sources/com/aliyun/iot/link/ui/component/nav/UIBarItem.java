package com.aliyun.iot.link.ui.component.nav;

import android.graphics.drawable.Drawable;
import android.view.View;

/* JADX INFO: loaded from: classes2.dex */
public abstract class UIBarItem {
    protected Action action;
    protected Drawable icon;
    protected boolean isEnable;
    protected int tag;
    protected String title;

    public interface Action {
        void invoke(View view2);
    }

    public int getTag() {
        return this.tag;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public Drawable getIcon() {
        return this.icon;
    }

    public void setIcon(Drawable drawable) {
        this.icon = drawable;
    }

    public boolean isEnable() {
        return this.isEnable;
    }

    public void setEnable(boolean z) {
        this.isEnable = z;
    }
}
