package com.seculink.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.adapters.TextViewBindingAdapter;
import bean.CloudVideo;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class ItemCloudImgBindingImpl extends ItemCloudImgBinding {

    @Nullable
    private static final ViewDataBinding.IncludedLayouts sIncludes = null;

    @Nullable
    private static final SparseIntArray sViewsWithIds = new SparseIntArray();
    private long mDirtyFlags;

    @Override // androidx.databinding.ViewDataBinding
    protected boolean onFieldChange(int i, Object obj, int i2) {
        return false;
    }

    static {
        sViewsWithIds.put(R.id.top, 2);
        sViewsWithIds.put(R.id.iv_center, 3);
        sViewsWithIds.put(R.id.bottom, 4);
        sViewsWithIds.put(R.id.tv_type, 5);
        sViewsWithIds.put(R.id.tv_time_size, 6);
        sViewsWithIds.put(R.id.iv_img, 7);
        sViewsWithIds.put(R.id.iv_playing, 8);
    }

    public ItemCloudImgBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 9, sIncludes, sViewsWithIds));
    }

    private ItemCloudImgBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (View) objArr[4], (ImageView) objArr[3], (ImageView) objArr[7], (ImageView) objArr[8], (RelativeLayout) objArr[0], (View) objArr[2], (TextView) objArr[1], (TextView) objArr[6], (TextView) objArr[5]);
        this.mDirtyFlags = -1L;
        this.layoutItem.setTag(null);
        this.tvTime.setTag(null);
        setRootTag(view2);
        invalidateAll();
    }

    @Override // androidx.databinding.ViewDataBinding
    public void invalidateAll() {
        synchronized (this) {
            this.mDirtyFlags = 2L;
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
    public boolean setVariable(int i, @Nullable Object obj) {
        if (2 != i) {
            return false;
        }
        setModel((CloudVideo.RecordFileListBean) obj);
        return true;
    }

    @Override // com.seculink.app.databinding.ItemCloudImgBinding
    public void setModel(@Nullable CloudVideo.RecordFileListBean recordFileListBean) {
        this.mModel = recordFileListBean;
        synchronized (this) {
            this.mDirtyFlags |= 1;
        }
        notifyPropertyChanged(2);
        super.requestRebind();
    }

    @Override // androidx.databinding.ViewDataBinding
    protected void executeBindings() {
        long j;
        synchronized (this) {
            j = this.mDirtyFlags;
            this.mDirtyFlags = 0L;
        }
        String beginTime = null;
        CloudVideo.RecordFileListBean recordFileListBean = this.mModel;
        long j2 = j & 3;
        if (j2 != 0 && recordFileListBean != null) {
            beginTime = recordFileListBean.getBeginTime();
        }
        if (j2 != 0) {
            TextViewBindingAdapter.setText(this.tvTime, beginTime);
        }
    }
}
