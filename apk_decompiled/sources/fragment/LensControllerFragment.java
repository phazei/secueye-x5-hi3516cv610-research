package fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.seculink.app.R;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes4.dex */
public class LensControllerFragment extends CommonFragment implements View.OnTouchListener {
    ImageView back;
    Button bt_focus_add;
    Button bt_focus_minus;
    Button bt_guard;
    Button bt_preset_add;
    Button bt_preset_invoke;
    Button bt_zoom_add;
    Button bt_zoom_minus;
    EditText et_preset;
    listener listener;
    private Timer onTouchTimer;
    private volatile View touchView;

    public interface listener {
        void OnPresetInvoke(String str);

        void OnPresetSet(String str);

        void addFocus();

        void addZoom();

        void back();

        void reduceFocus();

        void reduceZoom();

        void setWatchPos();
    }

    @Override // fragment.CommonFragment
    protected int getContentLayoutId() {
        return R.layout.lens_controller_fragment_layout;
    }

    @Override // fragment.CommonFragment
    @SuppressLint({"ClickableViewAccessibility"})
    protected void initWidget(View view2) {
        super.initWidget(view2);
        this.bt_zoom_add = (Button) view2.findViewById(R.id.bt_zoom_add);
        this.bt_zoom_minus = (Button) view2.findViewById(R.id.bt_zoom_minus);
        this.bt_focus_add = (Button) view2.findViewById(R.id.bt_focus_add);
        this.bt_focus_minus = (Button) view2.findViewById(R.id.bt_focus_minus);
        this.et_preset = (EditText) view2.findViewById(R.id.et_preset);
        this.bt_preset_invoke = (Button) view2.findViewById(R.id.bt_preset_invoke);
        this.bt_preset_add = (Button) view2.findViewById(R.id.bt_preset_add);
        this.bt_guard = (Button) view2.findViewById(R.id.bt_guard);
        this.back = (ImageView) view2.findViewById(R.id.back);
        this.bt_zoom_add.setOnTouchListener(this);
        this.bt_zoom_minus.setOnTouchListener(this);
        this.bt_focus_add.setOnTouchListener(this);
        this.bt_focus_minus.setOnTouchListener(this);
        this.bt_preset_invoke.setOnClickListener(new View.OnClickListener() { // from class: fragment.LensControllerFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                LensControllerFragment.this.listener.OnPresetInvoke(((Editable) Objects.requireNonNull(LensControllerFragment.this.et_preset.getText())).toString());
            }
        });
        this.bt_preset_add.setOnClickListener(new View.OnClickListener() { // from class: fragment.LensControllerFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                LensControllerFragment.this.listener.OnPresetSet(((Editable) Objects.requireNonNull(LensControllerFragment.this.et_preset.getText())).toString());
            }
        });
        this.bt_guard.setOnClickListener(new View.OnClickListener() { // from class: fragment.LensControllerFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                LensControllerFragment.this.listener.setWatchPos();
            }
        });
        this.back.setOnClickListener(new View.OnClickListener() { // from class: fragment.LensControllerFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                LensControllerFragment.this.listener.back();
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // fragment.CommonFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        this.listener = (listener) context;
        super.onAttach(context);
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
            this.onTouchTimer.schedule(new TimerTask() { // from class: fragment.LensControllerFragment.5
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    if (LensControllerFragment.this.touchView.getId() != R.id.bt_zoom_add) {
                        if (LensControllerFragment.this.touchView.getId() != R.id.bt_zoom_minus) {
                            if (LensControllerFragment.this.touchView.getId() != R.id.bt_focus_add) {
                                if (LensControllerFragment.this.touchView.getId() != R.id.bt_focus_minus || LensControllerFragment.this.listener == null) {
                                    return;
                                }
                                LensControllerFragment.this.listener.reduceFocus();
                                return;
                            }
                            if (LensControllerFragment.this.listener != null) {
                                LensControllerFragment.this.listener.addFocus();
                                return;
                            }
                            return;
                        }
                        if (LensControllerFragment.this.listener != null) {
                            LensControllerFragment.this.listener.reduceZoom();
                            return;
                        }
                        return;
                    }
                    if (LensControllerFragment.this.listener != null) {
                        LensControllerFragment.this.listener.addZoom();
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
}
