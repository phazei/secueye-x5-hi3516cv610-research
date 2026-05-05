package com.seculink.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityFeedbackProblemBindingImpl extends ActivityFeedbackProblemBinding {

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
        sViewsWithIds.put(R.id.left_rl, 1);
        sViewsWithIds.put(R.id.left_img, 2);
        sViewsWithIds.put(R.id.layout_top, 3);
        sViewsWithIds.put(R.id.layout_type, 4);
        sViewsWithIds.put(R.id.tv_type, 5);
        sViewsWithIds.put(R.id.layout_device, 6);
        sViewsWithIds.put(R.id.tv_name, 7);
        sViewsWithIds.put(R.id.et_contact, 8);
        sViewsWithIds.put(R.id.et_content, 9);
        sViewsWithIds.put(R.id.tv_count, 10);
        sViewsWithIds.put(R.id.bt_next, 11);
    }

    public ActivityFeedbackProblemBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 12, sIncludes, sViewsWithIds));
    }

    private ActivityFeedbackProblemBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (Button) objArr[11], (EditText) objArr[8], (EditText) objArr[9], (LinearLayout) objArr[6], (RelativeLayout) objArr[0], (LinearLayout) objArr[3], (LinearLayout) objArr[4], (ImageView) objArr[2], (RelativeLayout) objArr[1], (TextView) objArr[10], (TextView) objArr[7], (TextView) objArr[5]);
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
