package com.seculink.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.EditTextView;
import view.ItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public class TtsVigilanceActivityLayoutBindingImpl extends TtsVigilanceActivityLayoutBinding {

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
        sViewsWithIds.put(R.id.line, 2);
        sViewsWithIds.put(R.id.editText, 3);
        sViewsWithIds.put(R.id.sound_type, 4);
        sViewsWithIds.put(R.id.linearLayout2, 5);
        sViewsWithIds.put(R.id.sound_speed, 6);
        sViewsWithIds.put(R.id.bt_next, 7);
    }

    public TtsVigilanceActivityLayoutBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 8, sIncludes, sViewsWithIds));
    }

    private TtsVigilanceActivityLayoutBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (Button) objArr[7], (EditTextView) objArr[3], (ConstraintLayout) objArr[0], (View) objArr[2], (LinearLayout) objArr[5], (AppCompatSeekBar) objArr[6], (ItemView) objArr[4], (TitleView) objArr[1]);
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
