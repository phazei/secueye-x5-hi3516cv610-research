package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.haibin.calendarview.CalendarView;
import com.seculink.app.R;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityCloudStorageBinding extends ViewDataBinding {

    @NonNull
    public final CalendarView calendarView;

    @NonNull
    public final ImageView ivAdvance;

    @NonNull
    public final ImageView ivBack;

    @NonNull
    public final ImageView ivFull;

    @NonNull
    public final ImageView ivLeft;

    @NonNull
    public final ImageView ivPlayState;

    @NonNull
    public final ImageView ivRefund;

    @NonNull
    public final ImageView ivRight;

    @NonNull
    public final ImageView ivShadowBottom;

    @NonNull
    public final ImageView ivShadowTop;

    @NonNull
    public final ImageView ivSound;

    @NonNull
    public final LinearLayout landscapePlayer;

    @NonNull
    public final LinearLayout layoutBottom;

    @NonNull
    public final LinearLayout layoutBottomControl;

    @NonNull
    public final LinearLayout layoutBullet;

    @NonNull
    public final LinearLayout layoutBullet2;

    @NonNull
    public final LinearLayout layoutBullet3;

    @NonNull
    public final LinearLayout layoutCalendarView;

    @NonNull
    public final RelativeLayout layoutControl;

    @NonNull
    public final LinearLayout layoutDate;

    @NonNull
    public final LinearLayout layoutFunction;

    @NonNull
    public final LinearLayout layoutNoData;

    @NonNull
    public final LinearLayout layoutPtz;

    @NonNull
    public final ImageView layoutShot;

    @NonNull
    public final RelativeLayout layoutTime;

    @NonNull
    public final SimpleExoPlayerView play;

    @NonNull
    public final RelativeLayout portraitPlayer;

    @NonNull
    public final RecyclerView rvList;

    @NonNull
    public final SeekBar seekBar;

    @NonNull
    public final TextView tvCancel;

    @NonNull
    public final TextView tvCurdate;

    @NonNull
    public final TextView tvDate;

    @NonNull
    public final TextView tvEndTime;

    @NonNull
    public final TextView tvPlaybackGun;

    @NonNull
    public final TextView tvSpeed;

    @NonNull
    public final TextView tvStartTime;

    @NonNull
    public final TextView tvSuccess;

    @NonNull
    public final TitleView tvTitle;

    protected ActivityCloudStorageBinding(DataBindingComponent dataBindingComponent, View view2, int i, CalendarView calendarView, ImageView imageView, ImageView imageView2, ImageView imageView3, ImageView imageView4, ImageView imageView5, ImageView imageView6, ImageView imageView7, ImageView imageView8, ImageView imageView9, ImageView imageView10, LinearLayout linearLayout, LinearLayout linearLayout2, LinearLayout linearLayout3, LinearLayout linearLayout4, LinearLayout linearLayout5, LinearLayout linearLayout6, LinearLayout linearLayout7, RelativeLayout relativeLayout, LinearLayout linearLayout8, LinearLayout linearLayout9, LinearLayout linearLayout10, LinearLayout linearLayout11, ImageView imageView11, RelativeLayout relativeLayout2, SimpleExoPlayerView simpleExoPlayerView, RelativeLayout relativeLayout3, RecyclerView recyclerView, SeekBar seekBar, TextView textView, TextView textView2, TextView textView3, TextView textView4, TextView textView5, TextView textView6, TextView textView7, TextView textView8, TitleView titleView) {
        super(dataBindingComponent, view2, i);
        this.calendarView = calendarView;
        this.ivAdvance = imageView;
        this.ivBack = imageView2;
        this.ivFull = imageView3;
        this.ivLeft = imageView4;
        this.ivPlayState = imageView5;
        this.ivRefund = imageView6;
        this.ivRight = imageView7;
        this.ivShadowBottom = imageView8;
        this.ivShadowTop = imageView9;
        this.ivSound = imageView10;
        this.landscapePlayer = linearLayout;
        this.layoutBottom = linearLayout2;
        this.layoutBottomControl = linearLayout3;
        this.layoutBullet = linearLayout4;
        this.layoutBullet2 = linearLayout5;
        this.layoutBullet3 = linearLayout6;
        this.layoutCalendarView = linearLayout7;
        this.layoutControl = relativeLayout;
        this.layoutDate = linearLayout8;
        this.layoutFunction = linearLayout9;
        this.layoutNoData = linearLayout10;
        this.layoutPtz = linearLayout11;
        this.layoutShot = imageView11;
        this.layoutTime = relativeLayout2;
        this.play = simpleExoPlayerView;
        this.portraitPlayer = relativeLayout3;
        this.rvList = recyclerView;
        this.seekBar = seekBar;
        this.tvCancel = textView;
        this.tvCurdate = textView2;
        this.tvDate = textView3;
        this.tvEndTime = textView4;
        this.tvPlaybackGun = textView5;
        this.tvSpeed = textView6;
        this.tvStartTime = textView7;
        this.tvSuccess = textView8;
        this.tvTitle = titleView;
    }

    @NonNull
    public static ActivityCloudStorageBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityCloudStorageBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityCloudStorageBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_cloud_storage, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityCloudStorageBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityCloudStorageBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityCloudStorageBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_cloud_storage, null, false, dataBindingComponent);
    }

    public static ActivityCloudStorageBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityCloudStorageBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityCloudStorageBinding) bind(dataBindingComponent, view2, R.layout.activity_cloud_storage);
    }
}
