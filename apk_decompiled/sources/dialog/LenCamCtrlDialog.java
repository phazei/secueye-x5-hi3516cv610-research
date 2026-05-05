package dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import anet.channel.strategy.dispatch.DispatchConstants;
import com.seculink.app.R;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes3.dex */
public class LenCamCtrlDialog extends Dialog implements View.OnTouchListener {
    private Button bt_focus_add;
    private Button bt_focus_minus;
    private Button bt_guard;
    private Button bt_preset_add;
    private Button bt_preset_delete;
    private Button bt_preset_invoke;
    private Button bt_zoom_add;
    private Button bt_zoom_minus;
    private ConstraintLayout containerFocus;
    private ConstraintLayout containerPreset;
    private ConstraintLayout containerZoom;
    private Activity context;
    private int decorViewVisibleY;
    private EditText et_preset;
    private ImageButton ib_delete;
    private ImageButton ib_follow;
    private boolean isFocusShow;
    private boolean isFolder;
    private boolean isPresetShow;
    private boolean isZoomShow;
    private int nearViewId;
    private OnFuncListener onFuncListener;
    private Timer onTouchTimer;
    private volatile View touchView;
    private TextView tv_focus_minus;
    private TextView tv_preset;
    private TextView tv_zoom_minus;

    public interface OnFuncListener {
        void OnFocusChange(boolean z);

        void OnPresetDelete(String str);

        void OnPresetInvoke(String str);

        void OnPresetSet(String str);

        void OnZoomChange(boolean z);

        void setWatchPos();
    }

    public LenCamCtrlDialog(@NonNull Activity activity2, OnFuncListener onFuncListener) {
        super(activity2);
        this.context = activity2;
        this.onFuncListener = onFuncListener;
    }

    public LenCamCtrlDialog(@NonNull Activity activity2, int i, OnFuncListener onFuncListener) {
        super(activity2, i);
        this.context = activity2;
        this.onFuncListener = onFuncListener;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view2, MotionEvent motionEvent) {
        Timer timer;
        if (motionEvent.getAction() == 0) {
            this.touchView = view2;
            if (this.onTouchTimer != null) {
                return false;
            }
            this.onTouchTimer = new Timer();
            this.onTouchTimer.schedule(new TimerTask() { // from class: dialog.LenCamCtrlDialog.1
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    if (LenCamCtrlDialog.this.touchView.getId() != R.id.bt_zoom_add) {
                        if (LenCamCtrlDialog.this.touchView.getId() == R.id.bt_zoom_minus) {
                            if (LenCamCtrlDialog.this.onFuncListener != null) {
                                LenCamCtrlDialog.this.onFuncListener.OnZoomChange(false);
                                return;
                            }
                            return;
                        } else if (LenCamCtrlDialog.this.touchView.getId() == R.id.bt_focus_add) {
                            if (LenCamCtrlDialog.this.onFuncListener != null) {
                                LenCamCtrlDialog.this.onFuncListener.OnFocusChange(true);
                                return;
                            }
                            return;
                        } else {
                            if (LenCamCtrlDialog.this.touchView.getId() != R.id.bt_focus_minus || LenCamCtrlDialog.this.onFuncListener == null) {
                                return;
                            }
                            LenCamCtrlDialog.this.onFuncListener.OnFocusChange(false);
                            return;
                        }
                    }
                    Log.d("TAG", "run: bt_zoom_add");
                    if (LenCamCtrlDialog.this.onFuncListener != null) {
                        LenCamCtrlDialog.this.onFuncListener.OnZoomChange(true);
                    }
                }
            }, 0L, 50L);
            return false;
        }
        if (motionEvent.getAction() != 1 || (timer = this.onTouchTimer) == null) {
            return false;
        }
        timer.cancel();
        this.onTouchTimer = null;
        return false;
    }

    public LenCamCtrlDialog showZoomCtrl(boolean z) {
        this.isZoomShow = z;
        return this;
    }

    public LenCamCtrlDialog showFocusCtrl(boolean z) {
        this.isFocusShow = z;
        return this;
    }

    public LenCamCtrlDialog showPresetCtrl(boolean z) {
        this.isPresetShow = z;
        return this;
    }

    public LenCamCtrlDialog setNearViewId(int i) {
        this.nearViewId = i;
        return this;
    }

    public LenCamCtrlDialog showDialog() {
        super.show();
        return this;
    }

    private int getStatusBarHeight() {
        int identifier;
        if ((this.context.getWindow().getAttributes().flags & 1024) != 0 || (identifier = this.context.getResources().getIdentifier("status_bar_height", "dimen", DispatchConstants.ANDROID)) <= 0) {
            return 0;
        }
        return this.context.getResources().getDimensionPixelSize(identifier);
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        requestWindowFeature(1);
        setContentView(R.layout.layout_lencam_control2);
        final Window window = getWindow();
        window.setDimAmount(0.0f);
        window.setBackgroundDrawable(new ColorDrawable(0));
        setCancelable(true);
        final WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = -1;
        attributes.height = -2;
        attributes.gravity = 80;
        int i = this.nearViewId;
        if (i > 0) {
            final View viewFindViewById = this.context.findViewById(i);
            if (viewFindViewById != null) {
                new Handler().post(new Runnable() { // from class: dialog.LenCamCtrlDialog.2
                    @Override // java.lang.Runnable
                    public void run() {
                        WindowManager.LayoutParams layoutParams = attributes;
                        layoutParams.gravity = 80;
                        layoutParams.x = 0;
                        layoutParams.y = viewFindViewById.getHeight();
                        window.setAttributes(attributes);
                    }
                });
            }
        } else {
            window.setAttributes(attributes);
        }
        this.containerZoom = (ConstraintLayout) findViewById(R.id.containerZoom);
        this.containerFocus = (ConstraintLayout) findViewById(R.id.containerFocus);
        this.containerPreset = (ConstraintLayout) findViewById(R.id.containerPreset);
        this.et_preset = (EditText) findViewById(R.id.et_preset);
        this.bt_zoom_add = (Button) findViewById(R.id.bt_zoom_add);
        this.bt_zoom_minus = (Button) findViewById(R.id.bt_zoom_minus);
        this.tv_zoom_minus = (TextView) findViewById(R.id.tv_zoom_minus);
        this.bt_focus_add = (Button) findViewById(R.id.bt_focus_add);
        this.bt_focus_minus = (Button) findViewById(R.id.bt_focus_minus);
        this.tv_focus_minus = (TextView) findViewById(R.id.tv_focus_minus);
        this.bt_preset_invoke = (Button) findViewById(R.id.bt_preset_invoke);
        this.bt_preset_add = (Button) findViewById(R.id.bt_preset_add);
        this.bt_preset_delete = (Button) findViewById(R.id.bt_preset_delete);
        this.ib_delete = (ImageButton) findViewById(R.id.ib_delete);
        this.ib_follow = (ImageButton) findViewById(R.id.ib_follow);
        this.tv_preset = (TextView) findViewById(R.id.tv_preset);
        this.et_preset.setText("0");
        this.bt_guard = (Button) findViewById(R.id.bt_guard);
        setFolderShow();
        this.bt_zoom_add.setOnTouchListener(this);
        this.bt_zoom_minus.setOnTouchListener(this);
        this.bt_focus_add.setOnTouchListener(this);
        this.bt_focus_minus.setOnTouchListener(this);
        this.et_preset.requestFocus();
        this.et_preset.addTextChangedListener(new TextWatcher() { // from class: dialog.LenCamCtrlDialog.3
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                int i2;
                LenCamCtrlDialog.this.et_preset.removeTextChangedListener(this);
                if (TextUtils.isEmpty(editable.toString()) || (i2 = Integer.parseInt(editable.toString())) < 0) {
                    LenCamCtrlDialog.this.et_preset.setText(String.valueOf(0));
                } else if (i2 > 255) {
                    LenCamCtrlDialog.this.et_preset.setText(String.valueOf(255));
                } else {
                    LenCamCtrlDialog.this.et_preset.setText(String.valueOf(i2));
                }
                LenCamCtrlDialog.this.et_preset.setSelection(((Editable) Objects.requireNonNull(LenCamCtrlDialog.this.et_preset.getText())).length());
                LenCamCtrlDialog.this.et_preset.addTextChangedListener(this);
            }
        });
        this.bt_preset_invoke.setOnClickListener(new View.OnClickListener() { // from class: dialog.LenCamCtrlDialog.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (LenCamCtrlDialog.this.onFuncListener != null) {
                    LenCamCtrlDialog.this.onFuncListener.OnPresetInvoke(((Editable) Objects.requireNonNull(LenCamCtrlDialog.this.et_preset.getText())).toString());
                }
            }
        });
        this.bt_preset_add.setOnClickListener(new View.OnClickListener() { // from class: dialog.LenCamCtrlDialog.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (LenCamCtrlDialog.this.onFuncListener != null) {
                    LenCamCtrlDialog.this.onFuncListener.OnPresetSet(((Editable) Objects.requireNonNull(LenCamCtrlDialog.this.et_preset.getText())).toString());
                }
            }
        });
        this.bt_preset_delete.setOnClickListener(new View.OnClickListener() { // from class: dialog.LenCamCtrlDialog.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (LenCamCtrlDialog.this.onFuncListener != null) {
                    LenCamCtrlDialog.this.onFuncListener.OnPresetDelete(((Editable) Objects.requireNonNull(LenCamCtrlDialog.this.et_preset.getText())).toString());
                }
            }
        });
        this.ib_delete.setOnClickListener(new View.OnClickListener() { // from class: dialog.LenCamCtrlDialog.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                LenCamCtrlDialog.this.dismiss();
            }
        });
        if ((this.isZoomShow && this.isFocusShow) || ((this.isZoomShow && this.isPresetShow) || (this.isFocusShow && this.isPresetShow))) {
            this.ib_follow.setVisibility(0);
        } else {
            this.ib_follow.setVisibility(4);
        }
        this.ib_follow.setOnClickListener(new View.OnClickListener() { // from class: dialog.LenCamCtrlDialog.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                LenCamCtrlDialog.this.isFolder = !r2.isFolder;
                LenCamCtrlDialog.this.setFolderShow();
            }
        });
        this.bt_guard.setOnClickListener(new View.OnClickListener() { // from class: dialog.LenCamCtrlDialog.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (LenCamCtrlDialog.this.onFuncListener != null) {
                    LenCamCtrlDialog.this.onFuncListener.setWatchPos();
                }
            }
        });
    }

    public void setFolderShow() {
        this.ib_follow.setImageResource(this.isFolder ? R.drawable.ic_up2 : R.drawable.ic_down2);
        if (this.isZoomShow && this.isFocusShow) {
            if (this.isPresetShow) {
                this.containerZoom.setVisibility(this.isFolder ? 8 : 0);
                this.containerFocus.setVisibility(this.isFolder ? 8 : 0);
                this.containerPreset.setVisibility(0);
                return;
            } else {
                this.containerZoom.setVisibility(this.isFolder ? 8 : 0);
                this.containerPreset.setVisibility(8);
                return;
            }
        }
        if (this.isZoomShow && this.isPresetShow) {
            this.containerZoom.setVisibility(this.isFolder ? 8 : 0);
            this.containerPreset.setVisibility(this.isFolder ? 8 : 0);
            this.containerFocus.setVisibility(8);
        } else if (!this.isZoomShow && this.isFocusShow && this.isPresetShow) {
            this.containerFocus.setVisibility(this.isFolder ? 8 : 0);
            this.containerPreset.setVisibility(this.isFolder ? 8 : 0);
            this.containerZoom.setVisibility(8);
        }
    }
}
