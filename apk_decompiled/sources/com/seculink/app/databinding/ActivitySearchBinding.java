package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.haibin.calendarview.CalendarView;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivitySearchBinding extends ViewDataBinding {

    @NonNull
    public final CalendarView calendarView;

    @NonNull
    public final EditText editSearch;

    @NonNull
    public final LinearLayout layoutDate;

    @NonNull
    public final RelativeLayout layoutEnd;

    @NonNull
    public final LinearLayout layoutMain;

    @NonNull
    public final RelativeLayout layoutStart;

    @NonNull
    public final ImageView leftImg;

    @NonNull
    public final RelativeLayout leftRl;

    @NonNull
    public final RecyclerView rvList;

    @NonNull
    public final TextView tvCurdate;

    @NonNull
    public final TextView tvEnd;

    @NonNull
    public final TextView tvMonth;

    @NonNull
    public final TextView tvNoData;

    @NonNull
    public final TextView tvSearch;

    @NonNull
    public final TextView tvStart;

    @NonNull
    public final TextView tvToday;

    @NonNull
    public final TextView tvYear;

    protected ActivitySearchBinding(DataBindingComponent dataBindingComponent, View view2, int i, CalendarView calendarView, EditText editText, LinearLayout linearLayout, RelativeLayout relativeLayout, LinearLayout linearLayout2, RelativeLayout relativeLayout2, ImageView imageView, RelativeLayout relativeLayout3, RecyclerView recyclerView, TextView textView, TextView textView2, TextView textView3, TextView textView4, TextView textView5, TextView textView6, TextView textView7, TextView textView8) {
        super(dataBindingComponent, view2, i);
        this.calendarView = calendarView;
        this.editSearch = editText;
        this.layoutDate = linearLayout;
        this.layoutEnd = relativeLayout;
        this.layoutMain = linearLayout2;
        this.layoutStart = relativeLayout2;
        this.leftImg = imageView;
        this.leftRl = relativeLayout3;
        this.rvList = recyclerView;
        this.tvCurdate = textView;
        this.tvEnd = textView2;
        this.tvMonth = textView3;
        this.tvNoData = textView4;
        this.tvSearch = textView5;
        this.tvStart = textView6;
        this.tvToday = textView7;
        this.tvYear = textView8;
    }

    @NonNull
    public static ActivitySearchBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivitySearchBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivitySearchBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_search, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivitySearchBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivitySearchBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivitySearchBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_search, null, false, dataBindingComponent);
    }

    public static ActivitySearchBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivitySearchBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivitySearchBinding) bind(dataBindingComponent, view2, R.layout.activity_search);
    }
}
