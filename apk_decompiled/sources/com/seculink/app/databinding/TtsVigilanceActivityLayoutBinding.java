package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.EditTextView;
import view.ItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class TtsVigilanceActivityLayoutBinding extends ViewDataBinding {

    @NonNull
    public final Button btNext;

    @NonNull
    public final EditTextView editText;

    @NonNull
    public final ConstraintLayout layoutMain;

    @NonNull
    public final View line;

    @NonNull
    public final LinearLayout linearLayout2;

    @NonNull
    public final AppCompatSeekBar soundSpeed;

    @NonNull
    public final ItemView soundType;

    @NonNull
    public final TitleView title;

    protected TtsVigilanceActivityLayoutBinding(DataBindingComponent dataBindingComponent, View view2, int i, Button button, EditTextView editTextView, ConstraintLayout constraintLayout, View view3, LinearLayout linearLayout, AppCompatSeekBar appCompatSeekBar, ItemView itemView, TitleView titleView) {
        super(dataBindingComponent, view2, i);
        this.btNext = button;
        this.editText = editTextView;
        this.layoutMain = constraintLayout;
        this.line = view3;
        this.linearLayout2 = linearLayout;
        this.soundSpeed = appCompatSeekBar;
        this.soundType = itemView;
        this.title = titleView;
    }

    @NonNull
    public static TtsVigilanceActivityLayoutBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static TtsVigilanceActivityLayoutBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (TtsVigilanceActivityLayoutBinding) DataBindingUtil.inflate(layoutInflater, R.layout.tts_vigilance_activity_layout, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static TtsVigilanceActivityLayoutBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static TtsVigilanceActivityLayoutBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (TtsVigilanceActivityLayoutBinding) DataBindingUtil.inflate(layoutInflater, R.layout.tts_vigilance_activity_layout, null, false, dataBindingComponent);
    }

    public static TtsVigilanceActivityLayoutBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static TtsVigilanceActivityLayoutBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (TtsVigilanceActivityLayoutBinding) bind(dataBindingComponent, view2, R.layout.tts_vigilance_activity_layout);
    }
}
