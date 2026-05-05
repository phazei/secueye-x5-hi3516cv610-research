package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityFeedbackProblemBinding extends ViewDataBinding {

    @NonNull
    public final Button btNext;

    @NonNull
    public final EditText etContact;

    @NonNull
    public final EditText etContent;

    @NonNull
    public final LinearLayout layoutDevice;

    @NonNull
    public final RelativeLayout layoutMain;

    @NonNull
    public final LinearLayout layoutTop;

    @NonNull
    public final LinearLayout layoutType;

    @NonNull
    public final ImageView leftImg;

    @NonNull
    public final RelativeLayout leftRl;

    @NonNull
    public final TextView tvCount;

    @NonNull
    public final TextView tvName;

    @NonNull
    public final TextView tvType;

    protected ActivityFeedbackProblemBinding(DataBindingComponent dataBindingComponent, View view2, int i, Button button, EditText editText, EditText editText2, LinearLayout linearLayout, RelativeLayout relativeLayout, LinearLayout linearLayout2, LinearLayout linearLayout3, ImageView imageView, RelativeLayout relativeLayout2, TextView textView, TextView textView2, TextView textView3) {
        super(dataBindingComponent, view2, i);
        this.btNext = button;
        this.etContact = editText;
        this.etContent = editText2;
        this.layoutDevice = linearLayout;
        this.layoutMain = relativeLayout;
        this.layoutTop = linearLayout2;
        this.layoutType = linearLayout3;
        this.leftImg = imageView;
        this.leftRl = relativeLayout2;
        this.tvCount = textView;
        this.tvName = textView2;
        this.tvType = textView3;
    }

    @NonNull
    public static ActivityFeedbackProblemBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityFeedbackProblemBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityFeedbackProblemBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_feedback_problem, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityFeedbackProblemBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityFeedbackProblemBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityFeedbackProblemBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_feedback_problem, null, false, dataBindingComponent);
    }

    public static ActivityFeedbackProblemBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityFeedbackProblemBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityFeedbackProblemBinding) bind(dataBindingComponent, view2, R.layout.activity_feedback_problem);
    }
}
