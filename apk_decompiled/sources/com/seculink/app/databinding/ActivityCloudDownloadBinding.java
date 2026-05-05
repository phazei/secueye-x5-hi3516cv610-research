package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.seculink.app.R;
import view.EditViewIpc;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityCloudDownloadBinding extends ViewDataBinding {

    @NonNull
    public final CalendarLayout calendarLayout;

    @NonNull
    public final CalendarView calendarView;

    @NonNull
    public final TextView delete;

    @NonNull
    public final EditViewIpc editView;

    @NonNull
    public final ConstraintLayout layoutMain;

    @NonNull
    public final RecyclerView recycler;

    @NonNull
    public final TitleView title;

    protected ActivityCloudDownloadBinding(DataBindingComponent dataBindingComponent, View view2, int i, CalendarLayout calendarLayout, CalendarView calendarView, TextView textView, EditViewIpc editViewIpc, ConstraintLayout constraintLayout, RecyclerView recyclerView, TitleView titleView) {
        super(dataBindingComponent, view2, i);
        this.calendarLayout = calendarLayout;
        this.calendarView = calendarView;
        this.delete = textView;
        this.editView = editViewIpc;
        this.layoutMain = constraintLayout;
        this.recycler = recyclerView;
        this.title = titleView;
    }

    @NonNull
    public static ActivityCloudDownloadBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityCloudDownloadBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityCloudDownloadBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_cloud_download, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityCloudDownloadBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityCloudDownloadBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityCloudDownloadBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_cloud_download, null, false, dataBindingComponent);
    }

    public static ActivityCloudDownloadBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityCloudDownloadBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityCloudDownloadBinding) bind(dataBindingComponent, view2, R.layout.activity_cloud_download);
    }
}
