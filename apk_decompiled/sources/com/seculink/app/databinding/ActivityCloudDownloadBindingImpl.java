package com.seculink.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.seculink.app.R;
import view.EditViewIpc;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityCloudDownloadBindingImpl extends ActivityCloudDownloadBinding {

    @Nullable
    private static final ViewDataBinding.IncludedLayouts sIncludes = null;

    @Nullable
    private static final SparseIntArray sViewsWithIds = new SparseIntArray();
    private long mDirtyFlags;

    @Override // androidx.databinding.ViewDataBinding
    protected boolean onFieldChange(int i, Object obj, int i2) {
        return false;
    }

    @Override // androidx.databinding.ViewDataBinding
    public boolean setVariable(int i, @Nullable Object obj) {
        return true;
    }

    static {
        sViewsWithIds.put(R.id.title, 1);
        sViewsWithIds.put(R.id.delete, 2);
        sViewsWithIds.put(R.id.calendarLayout, 3);
        sViewsWithIds.put(R.id.calendarView, 4);
        sViewsWithIds.put(R.id.recycler, 5);
        sViewsWithIds.put(R.id.editView, 6);
    }

    public ActivityCloudDownloadBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 7, sIncludes, sViewsWithIds));
    }

    private ActivityCloudDownloadBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (CalendarLayout) objArr[3], (CalendarView) objArr[4], (TextView) objArr[2], (EditViewIpc) objArr[6], (ConstraintLayout) objArr[0], (RecyclerView) objArr[5], (TitleView) objArr[1]);
        this.mDirtyFlags = -1L;
        this.layoutMain.setTag(null);
        setRootTag(view2);
        invalidateAll();
    }

    @Override // androidx.databinding.ViewDataBinding
    public void invalidateAll() {
        synchronized (this) {
            this.mDirtyFlags = 1L;
        }
        requestRebind();
    }

    @Override // androidx.databinding.ViewDataBinding
    public boolean hasPendingBindings() {
        synchronized (this) {
            return this.mDirtyFlags != 0;
        }
    }

    @Override // androidx.databinding.ViewDataBinding
    protected void executeBindings() {
        synchronized (this) {
            long j = this.mDirtyFlags;
            this.mDirtyFlags = 0L;
        }
    }
}
