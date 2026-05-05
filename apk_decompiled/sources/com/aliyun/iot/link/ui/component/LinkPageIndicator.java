package com.aliyun.iot.link.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;

/* JADX INFO: loaded from: classes2.dex */
public class LinkPageIndicator extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "PageIndicator";
    private OnTabClickListener onTabClickListener;
    private TabAdapter tabAdapter;

    public interface OnTabClickListener {
        void onTabClick(View view2, BaseTabViewHolder baseTabViewHolder);
    }

    private void init() {
    }

    public void setNeedShowNumPoint() {
    }

    public LinkPageIndicator(Context context) {
        this(context, null);
    }

    public LinkPageIndicator(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LinkPageIndicator(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public void setTabAdapter(TabAdapter tabAdapter) {
        this.tabAdapter = tabAdapter;
        tabAdapter.setLinkPageIndicator(this);
        buildView();
        requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void buildView() {
        LinearLayout.LayoutParams layoutParams;
        removeAllViews();
        setOrientation(this.tabAdapter.getOrientation());
        if (this.tabAdapter != null) {
            for (int i = 0; i < this.tabAdapter.getTabCount(); i++) {
                BaseTabViewHolder baseTabViewHolderOnCreatTabViewHolder = this.tabAdapter.onCreatTabViewHolder(getContext(), this, i, this.tabAdapter.getTabType(i));
                baseTabViewHolderOnCreatTabViewHolder.setPosition(i);
                if (getOrientation() == 0) {
                    layoutParams = new LinearLayout.LayoutParams(0, -1, 1.0f);
                } else {
                    layoutParams = new LinearLayout.LayoutParams(-1, 0, 1.0f);
                }
                baseTabViewHolderOnCreatTabViewHolder.getView().setLayoutParams(layoutParams);
                baseTabViewHolderOnCreatTabViewHolder.getView().setOnClickListener(this);
                this.tabAdapter.onBindTabViewHolder(baseTabViewHolderOnCreatTabViewHolder, i);
                addView(baseTabViewHolderOnCreatTabViewHolder.getView());
            }
        }
    }

    public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
        this.onTabClickListener = onTabClickListener;
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        refreshTabState(view2);
        OnTabClickListener onTabClickListener = this.onTabClickListener;
        if (onTabClickListener != null) {
            onTabClickListener.onTabClick(view2, (BaseTabViewHolder) view2.getTag());
        } else {
            Log.d(TAG, "there is no OnTabCLickListener");
        }
    }

    private void refreshTabState(View view2) {
        if (view2.getTag() != null && (view2.getTag() instanceof BaseTabViewHolder)) {
            ((BaseTabViewHolder) view2.getTag()).onTabChecked(true);
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getTag() != null && (childAt.getTag() instanceof BaseTabViewHolder)) {
                BaseTabViewHolder baseTabViewHolder = (BaseTabViewHolder) childAt.getTag();
                if (childAt == view2) {
                    baseTabViewHolder.onTabChecked(true);
                } else {
                    baseTabViewHolder.onTabChecked(false);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshIndicator() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getTag() != null && (childAt.getTag() instanceof BaseTabViewHolder)) {
                BaseTabViewHolder baseTabViewHolder = (BaseTabViewHolder) childAt.getTag();
                this.tabAdapter.onBindTabViewHolder(baseTabViewHolder, baseTabViewHolder.getPosition());
            }
        }
    }

    public static abstract class TabAdapter {
        private LinkPageIndicator mLinkPageIndicator;
        private int orientation;

        public int getTabCount() {
            return 0;
        }

        public abstract int getTabType(int i);

        public abstract void onBindTabViewHolder(BaseTabViewHolder baseTabViewHolder, int i);

        public abstract BaseTabViewHolder onCreatTabViewHolder(Context context, ViewGroup viewGroup, int i, int i2);

        public TabAdapter(int i) {
            this.orientation = i;
        }

        public TabAdapter() {
            this(0);
        }

        public int getOrientation() {
            return this.orientation;
        }

        public void setLinkPageIndicator(LinkPageIndicator linkPageIndicator) {
            this.mLinkPageIndicator = linkPageIndicator;
        }

        public void notifyDataSetChanged() {
            this.mLinkPageIndicator.buildView();
            this.mLinkPageIndicator.requestLayout();
        }

        public void notifyDataSetRefresh() {
            this.mLinkPageIndicator.refreshIndicator();
        }
    }

    public static abstract class BaseTabViewHolder<T> {
        public static final int LEFT_EDGE_TAB = 1;
        public static final int MIDDLE_TAB = 2;
        public static final int ONLY_ONE_TAB = 4;
        public static final int RIGHT_EDGE_TAB = 3;
        private int mPosition;
        private T mTabData;
        private int mTabType;
        public View mView;

        public abstract void initView();

        public abstract void onTabChecked(boolean z);

        public abstract void upDateData(T t);

        public BaseTabViewHolder(View view2) {
            this.mView = view2;
            this.mView.setTag(this);
            initView();
        }

        public void setTabData(T t) {
            this.mTabData = t;
            try {
                upDateData(t);
            } catch (Exception e) {
                Log.e(LinkPageIndicator.TAG, "ilop pageindicator bind data error!");
                e.printStackTrace();
            }
        }

        public View getView() {
            return this.mView;
        }

        public void setPosition(int i) {
            this.mPosition = i;
        }

        public int getPosition() {
            return this.mPosition;
        }

        public void setType(int i) {
            this.mTabType = i;
        }

        public int getTabType() {
            return this.mTabType;
        }

        public T getTabData() {
            return this.mTabData;
        }
    }
}
