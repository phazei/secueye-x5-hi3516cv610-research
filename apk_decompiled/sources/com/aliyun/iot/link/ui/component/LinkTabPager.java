package com.aliyun.iot.link.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

/* JADX INFO: loaded from: classes2.dex */
public class LinkTabPager extends LinearLayout {
    private ViewPager mViewPager;
    private TabLayout tableLayout;

    public LinkTabPager(Context context) {
        this(context, null);
    }

    public LinkTabPager(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, R.style.Widget_Design_TabLayout);
    }

    public LinkTabPager(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.link_tab_page, (ViewGroup) this, true);
        this.tableLayout = (TabLayout) findViewById(R.id.tab_layout);
        this.mViewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        this.tableLayout.setupWithViewPager(this.mViewPager);
    }

    public void setAdapter(PagerAdapter pagerAdapter) {
        this.mViewPager.setAdapter(pagerAdapter);
    }

    public void setTabTextColors(int i, int i2) {
        this.tableLayout.setTabTextColors(i, i2);
    }

    public void setTabMode(int i) {
        this.tableLayout.setTabMode(i);
    }

    public int getTabCount() {
        return this.tableLayout.getTabCount();
    }

    public void addOnTabSelectedListener(@NonNull TabLayout.OnTabSelectedListener onTabSelectedListener) {
        this.tableLayout.addOnTabSelectedListener(onTabSelectedListener);
    }

    public void removeOnTabSelectedListener(@NonNull TabLayout.OnTabSelectedListener onTabSelectedListener) {
        this.tableLayout.removeOnTabSelectedListener(onTabSelectedListener);
    }

    public int getSelectedTabPosition() {
        return this.tableLayout.getSelectedTabPosition();
    }

    public int getCurrentItem() {
        return this.mViewPager.getCurrentItem();
    }

    public void setCurrentItem(int i, boolean z) {
        this.mViewPager.setCurrentItem(i, z);
    }

    public void addOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.mViewPager.addOnPageChangeListener(onPageChangeListener);
    }

    public void removeOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.mViewPager.removeOnPageChangeListener(onPageChangeListener);
    }
}
