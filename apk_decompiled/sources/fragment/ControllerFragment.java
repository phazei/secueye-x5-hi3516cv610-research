package fragment;

import activity.IPCameraActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.seculink.app.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import sdk.IPCManager;
import tools.SharePreferenceManager;
import view.DialogView;
import view.ShadowButton;

/* JADX INFO: loaded from: classes4.dex */
public class ControllerFragment extends CommonFragment implements View.OnClickListener, View.OnLongClickListener {
    LinearLayout back;
    TextView door_text;
    private String iotId;
    private Mylistener listener;
    TextView tips;
    private List<Integer> list = new ArrayList();
    private int Page = 0;
    private int controllerSize = 5;
    private List<ShadowButton> buttonList = new ArrayList();
    private List<ImageView> doorList = new ArrayList();
    private List<TextView> doorTextList = new ArrayList();
    private MutableLiveData<Integer> pageSelect = new MutableLiveData<>();
    boolean flag = false;
    int QueryRFKeyStatusTimes = 0;
    long lastOnclickTime = 0;
    int statusFlag = 2;

    public interface Mylistener {
        void thanks();
    }

    @Override // fragment.CommonFragment
    protected int getContentLayoutId() {
        return R.layout.controller_fragment;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // fragment.CommonFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        this.listener = (Mylistener) context;
        super.onAttach(context);
    }

    @Override // fragment.CommonFragment
    @SuppressLint({"ClickableViewAccessibility"})
    protected void initWidget(View view2) {
        super.initWidget(view2);
        this.back = (LinearLayout) view2.findViewById(R.id.back_ll);
        this.back.setOnClickListener(new View.OnClickListener() { // from class: fragment.ControllerFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                ControllerFragment.this.listener.thanks();
            }
        });
        final ShadowButton shadowButton = (ShadowButton) view2.findViewById(R.id.button1);
        shadowButton.setOnClickListener(this);
        shadowButton.setOnLongClickListener(this);
        shadowButton.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.ControllerFragment.2
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    shadowButton.setBackground(ControllerFragment.this.getResources().getDrawable(R.drawable.controller_up_down));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                ControllerFragment.this.refreshButton();
                return false;
            }
        });
        final ShadowButton shadowButton2 = (ShadowButton) view2.findViewById(R.id.button2);
        shadowButton2.setOnClickListener(this);
        shadowButton2.setOnLongClickListener(this);
        shadowButton2.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.ControllerFragment.3
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    shadowButton2.setBackground(ControllerFragment.this.getResources().getDrawable(R.drawable.controller_lock_down));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                ControllerFragment.this.refreshButton();
                return false;
            }
        });
        final ShadowButton shadowButton3 = (ShadowButton) view2.findViewById(R.id.button3);
        shadowButton3.setOnClickListener(this);
        shadowButton3.setOnLongClickListener(this);
        shadowButton3.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.ControllerFragment.4
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    shadowButton3.setBackground(ControllerFragment.this.getResources().getDrawable(R.drawable.controller_stop_down));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                ControllerFragment.this.refreshButton();
                return false;
            }
        });
        final ShadowButton shadowButton4 = (ShadowButton) view2.findViewById(R.id.button4);
        shadowButton4.setOnClickListener(this);
        shadowButton4.setOnLongClickListener(this);
        shadowButton4.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.ControllerFragment.5
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    shadowButton4.setBackground(ControllerFragment.this.getResources().getDrawable(R.drawable.controller_down_down));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                ControllerFragment.this.refreshButton();
                return false;
            }
        });
        final ShadowButton shadowButton5 = (ShadowButton) view2.findViewById(R.id.doorbell);
        shadowButton5.setOnLongClickListener(this);
        shadowButton5.setOnClickListener(this);
        shadowButton5.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.ControllerFragment.6
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    shadowButton5.setBackground(ControllerFragment.this.getResources().getDrawable(R.drawable.doorbell_down));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                ControllerFragment.this.refreshButton();
                return false;
            }
        });
        this.buttonList.add(shadowButton);
        this.buttonList.add(shadowButton2);
        this.buttonList.add(shadowButton3);
        this.buttonList.add(shadowButton4);
        this.buttonList.add(shadowButton5);
        this.controllerSize = this.buttonList.size();
        ((LinearLayout) view2.findViewById(R.id.ll1)).setOnClickListener(this);
        ((LinearLayout) view2.findViewById(R.id.ll2)).setOnClickListener(this);
        ((LinearLayout) view2.findViewById(R.id.ll3)).setOnClickListener(this);
        ((LinearLayout) view2.findViewById(R.id.ll4)).setOnClickListener(this);
        ImageView imageView = (ImageView) view2.findViewById(R.id.image1);
        ImageView imageView2 = (ImageView) view2.findViewById(R.id.image2);
        ImageView imageView3 = (ImageView) view2.findViewById(R.id.image3);
        ImageView imageView4 = (ImageView) view2.findViewById(R.id.image4);
        TextView textView = (TextView) view2.findViewById(R.id.text1);
        TextView textView2 = (TextView) view2.findViewById(R.id.text2);
        TextView textView3 = (TextView) view2.findViewById(R.id.text3);
        TextView textView4 = (TextView) view2.findViewById(R.id.text4);
        this.doorList.add(imageView);
        this.doorList.add(imageView2);
        this.doorList.add(imageView3);
        this.doorList.add(imageView4);
        this.doorTextList.add(textView);
        this.doorTextList.add(textView2);
        this.doorTextList.add(textView3);
        this.doorTextList.add(textView4);
        this.door_text = (TextView) view2.findViewById(R.id.door_text);
        ImageView imageView5 = (ImageView) view2.findViewById(R.id.controller_edit);
        this.tips = (TextView) view2.findViewById(R.id.tips);
        imageView5.setOnClickListener(new View.OnClickListener() { // from class: fragment.ControllerFragment.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                ControllerFragment.this.showTips();
            }
        });
        this.pageSelect.observe(this, new Observer<Integer>() { // from class: fragment.ControllerFragment.8
            @Override // androidx.lifecycle.Observer
            public void onChanged(@Nullable Integer num) {
                if (num != null) {
                    ((ImageView) ControllerFragment.this.doorList.get(ControllerFragment.this.Page)).setImageResource(R.drawable.computer_ipc);
                    ((TextView) ControllerFragment.this.doorTextList.get(ControllerFragment.this.Page)).setTextColor(ControllerFragment.this.getResources().getColor(R.color.color_black));
                    ControllerFragment.this.Page = num.intValue();
                    ((ImageView) ControllerFragment.this.doorList.get(num.intValue())).setImageResource(R.drawable.computer_ipc_light);
                    ((TextView) ControllerFragment.this.doorTextList.get(num.intValue())).setTextColor(ControllerFragment.this.getResources().getColor(R.color.colorAccent));
                    final int iIntValue = num.intValue();
                    ((FragmentActivity) Objects.requireNonNull(ControllerFragment.this.getActivity())).runOnUiThread(new Runnable() { // from class: fragment.ControllerFragment.8.1
                        /* JADX WARN: Removed duplicated region for block: B:13:0x0062  */
                        @Override // java.lang.Runnable
                        /*
                            Code decompiled incorrectly, please refer to instructions dump.
                            To view partially-correct add '--show-bad-code' argument
                        */
                        public void run() {
                            /*
                                r4 = this;
                                tools.SharePreferenceManager r0 = tools.SharePreferenceManager.getInstance()
                                fragment.ControllerFragment$8 r1 = fragment.ControllerFragment.AnonymousClass8.this
                                fragment.ControllerFragment r1 = fragment.ControllerFragment.this
                                java.lang.String r1 = fragment.ControllerFragment.access$500(r1)
                                fragment.ControllerFragment$8 r2 = fragment.ControllerFragment.AnonymousClass8.this
                                fragment.ControllerFragment r2 = fragment.ControllerFragment.this
                                int r2 = fragment.ControllerFragment.access$200(r2)
                                java.lang.String r0 = r0.getAutoName(r1, r2)
                                fragment.ControllerFragment$8 r1 = fragment.ControllerFragment.AnonymousClass8.this
                                fragment.ControllerFragment r1 = fragment.ControllerFragment.this
                                android.widget.TextView r1 = r1.door_text
                                if (r0 == 0) goto L2a
                                java.lang.String r2 = ""
                                boolean r2 = r0.equals(r2)
                                if (r2 != 0) goto L2a
                                r2 = r0
                                goto L3e
                            L2a:
                                fragment.ControllerFragment$8 r2 = fragment.ControllerFragment.AnonymousClass8.this
                                fragment.ControllerFragment r2 = fragment.ControllerFragment.this
                                java.util.List r2 = fragment.ControllerFragment.access$400(r2)
                                int r3 = r2
                                java.lang.Object r2 = r2.get(r3)
                                android.widget.TextView r2 = (android.widget.TextView) r2
                                java.lang.CharSequence r2 = r2.getText()
                            L3e:
                                r1.setText(r2)
                                fragment.ControllerFragment$8 r1 = fragment.ControllerFragment.AnonymousClass8.this
                                fragment.ControllerFragment r1 = fragment.ControllerFragment.this
                                java.util.List r1 = fragment.ControllerFragment.access$400(r1)
                                fragment.ControllerFragment$8 r2 = fragment.ControllerFragment.AnonymousClass8.this
                                fragment.ControllerFragment r2 = fragment.ControllerFragment.this
                                int r2 = fragment.ControllerFragment.access$200(r2)
                                java.lang.Object r1 = r1.get(r2)
                                android.widget.TextView r1 = (android.widget.TextView) r1
                                if (r0 == 0) goto L62
                                java.lang.String r2 = ""
                                boolean r2 = r0.equals(r2)
                                if (r2 != 0) goto L62
                                goto L76
                            L62:
                                fragment.ControllerFragment$8 r0 = fragment.ControllerFragment.AnonymousClass8.this
                                fragment.ControllerFragment r0 = fragment.ControllerFragment.this
                                java.util.List r0 = fragment.ControllerFragment.access$400(r0)
                                int r2 = r2
                                java.lang.Object r0 = r0.get(r2)
                                android.widget.TextView r0 = (android.widget.TextView) r0
                                java.lang.CharSequence r0 = r0.getText()
                            L76:
                                r1.setText(r0)
                                return
                            */
                            throw new UnsupportedOperationException("Method not decompiled: fragment.ControllerFragment.AnonymousClass8.AnonymousClass1.run():void");
                        }
                    });
                }
            }
        });
        this.pageSelect.postValue(0);
        refreshButtonViewText();
    }

    @Override // fragment.CommonFragment
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        this.iotId = bundle.getString("iotId");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v3, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v7, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r1v8, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r2v3, types: [android.widget.TextView] */
    public void refreshButtonViewText() {
        for (int i = 0; i < this.doorList.size(); i++) {
            ?? autoName = SharePreferenceManager.getInstance().getAutoName(this.iotId, i);
            TextView textView = this.doorTextList.get(i);
            if (autoName == 0 || autoName.equals("")) {
                autoName = this.doorTextList.get(i).getText();
            }
            textView.setText((CharSequence) autoName);
        }
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    @Override // androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (z) {
            return;
        }
        refreshButton();
    }

    public void refreshButton() {
        Log.d(this.TAG, "refreshButton: ------------" + this.Page);
        try {
            this.tips.setVisibility(0);
            boolean z = false;
            for (int i = 0; i < this.buttonList.size(); i++) {
                if (this.list.get((this.Page * this.controllerSize) + i).intValue() == 1 && i == 0) {
                    this.buttonList.get(i).setBackground(getResources().getDrawable(R.drawable.controller_up));
                } else if (this.list.get((this.Page * this.controllerSize) + i).intValue() == 0 && i == 0) {
                    this.buttonList.get(i).setBackground(getResources().getDrawable(R.drawable.controller_up_gray));
                }
                if (this.list.get((this.Page * this.controllerSize) + i).intValue() == 1 && i == 1) {
                    this.buttonList.get(i).setBackground(getResources().getDrawable(R.drawable.controller_lock));
                } else if (this.list.get((this.Page * this.controllerSize) + i).intValue() == 0 && i == 1) {
                    this.buttonList.get(i).setBackground(getResources().getDrawable(R.drawable.controller_lock_gray));
                }
                if (this.list.get((this.Page * this.controllerSize) + i).intValue() == 1 && i == 2) {
                    this.buttonList.get(i).setBackground(getResources().getDrawable(R.drawable.controller_stop));
                } else if (this.list.get((this.Page * this.controllerSize) + i).intValue() == 0 && i == 2) {
                    this.buttonList.get(i).setBackground(getResources().getDrawable(R.drawable.controller_stop_gray));
                }
                if (this.list.get((this.Page * this.controllerSize) + i).intValue() == 1 && i == 3) {
                    this.buttonList.get(i).setBackground(getResources().getDrawable(R.drawable.controller_down));
                } else if (this.list.get((this.Page * this.controllerSize) + i).intValue() == 0 && i == 3) {
                    this.buttonList.get(i).setBackground(getResources().getDrawable(R.drawable.controller_down_gray));
                }
                if (this.list.get((this.Page * this.controllerSize) + i).intValue() == 1 && i == 4) {
                    this.buttonList.get(i).setBackground(getResources().getDrawable(R.drawable.doorbell));
                } else if (this.list.get((this.Page * this.controllerSize) + i).intValue() == 0 && i == 4) {
                    this.buttonList.get(i).setBackground(getResources().getDrawable(R.drawable.doorbell_gray));
                }
                if (this.list.get((this.Page * this.controllerSize) + i).intValue() == 1 && !z) {
                    this.tips.setVisibility(8);
                    z = true;
                }
            }
        } catch (Exception unused) {
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        if (view2.getId() == R.id.button1) {
            act((this.Page * this.controllerSize) + 0);
            return;
        }
        if (view2.getId() == R.id.button2) {
            act((this.Page * this.controllerSize) + 1);
            return;
        }
        if (view2.getId() == R.id.button3) {
            act((this.Page * this.controllerSize) + 2);
            return;
        }
        if (view2.getId() == R.id.button4) {
            act((this.Page * this.controllerSize) + 3);
            return;
        }
        if (view2.getId() == R.id.ll1) {
            this.pageSelect.setValue(0);
            refreshButton();
            return;
        }
        if (view2.getId() == R.id.ll2) {
            this.pageSelect.setValue(1);
            refreshButton();
        } else if (view2.getId() == R.id.ll3) {
            this.pageSelect.setValue(2);
            refreshButton();
        } else if (view2.getId() == R.id.ll4) {
            this.pageSelect.setValue(3);
            refreshButton();
        }
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view2) {
        if (view2.getId() == R.id.button1) {
            if (this.list.get(this.Page * this.controllerSize).intValue() != 0) {
                return false;
            }
            showDialog(this.Page * this.controllerSize);
            return false;
        }
        if (view2.getId() == R.id.button2) {
            if (this.list.get((this.Page * this.controllerSize) + 1).intValue() != 0) {
                return false;
            }
            showDialog((this.Page * this.controllerSize) + 1);
            return false;
        }
        if (view2.getId() == R.id.button3) {
            if (this.list.get((this.Page * this.controllerSize) + 2).intValue() != 0) {
                return false;
            }
            showDialog((this.Page * this.controllerSize) + 2);
            return false;
        }
        if (view2.getId() == R.id.button4) {
            if (this.list.get((this.Page * this.controllerSize) + 3).intValue() != 0) {
                return false;
            }
            showDialog((this.Page * this.controllerSize) + 3);
            return false;
        }
        if (view2.getId() != R.id.doorbell || this.list.get((this.Page * this.controllerSize) + 4).intValue() != 0) {
            return false;
        }
        showDialog((this.Page * this.controllerSize) + 4);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addController(final int i, final DialogView dialogView) {
        IPCManager.getInstance().getDevice(this.iotId).AddController(i, (i + 1) % 5 == 0 ? 1 : 0, new IPanelCallback() { // from class: fragment.ControllerFragment.9
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (z) {
                    Log.d(ControllerFragment.this.TAG, "onComplete: -------" + z);
                    if (JSONObject.parseObject(String.valueOf(obj)).getIntValue("code") == 200) {
                        ControllerFragment.this.QueryRFKeyStatus(i, dialogView);
                    }
                }
            }
        });
    }

    private void showDialog(final int i) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() { // from class: fragment.ControllerFragment.10
                @Override // java.lang.Runnable
                public void run() {
                    ControllerFragment.this.showTest(i);
                }
            });
        }
    }

    private void act(int i) {
        IPCManager.getInstance().getDevice(this.iotId).RFActionControl(i, new IPanelCallback() { // from class: fragment.ControllerFragment.11
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                Log.d(ControllerFragment.this.TAG, "onComplete: -------" + z);
            }
        });
    }

    private void delete() {
        for (int i = 0; i < this.controllerSize; i++) {
            IPCManager.getInstance().getDevice(this.iotId).deleteController((this.Page * this.controllerSize) + i, new IPanelCallback() { // from class: fragment.ControllerFragment.12
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (!z || ControllerFragment.this.getActivity() == null) {
                        return;
                    }
                    ControllerFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: fragment.ControllerFragment.12.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ((IPCameraActivity) ControllerFragment.this.getActivity()).getControllerList();
                        }
                    });
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTest(final int i) {
        new DialogView.Builder(getContext()).setContent(getResources().getString(R.string.learn_mode_tips)).setPositiveClickListener(getResources().getString(R.string.confirm), new DialogView.OnPositiveClickListener() { // from class: fragment.ControllerFragment.14
            @Override // view.DialogView.OnPositiveClickListener
            public void onPositiveClick(DialogView dialogView) {
                TextView tvConfirm = dialogView.getTvConfirm();
                TextView tvDes = dialogView.getTvDes();
                ProgressBar loading = dialogView.getLoading();
                int statusNum = dialogView.getStatusNum();
                if (statusNum != 0) {
                    if (statusNum == 1) {
                        dialogView.dismiss();
                        return;
                    }
                    return;
                }
                loading.setVisibility(0);
                tvConfirm.setText(ControllerFragment.this.getResources().getString(R.string.finish));
                tvDes.setText(ControllerFragment.this.getResources().getString(R.string.remote_control_tips));
                tvDes.setGravity(17);
                dialogView.setStatusNum(1);
                dialogView.hideConfirmButton();
                ControllerFragment.this.addController(i, dialogView);
            }
        }).setNegativeClickListener(getResources().getString(R.string.cancel), new DialogView.OnNegativeClickListener() { // from class: fragment.ControllerFragment.13
            @Override // view.DialogView.OnNegativeClickListener
            public void onNegativeClick(DialogView dialogView) {
                dialogView.dismiss();
            }
        }).build().show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void QueryRFKeyStatus(final int i, final DialogView dialogView) {
        new Thread(new Runnable() { // from class: fragment.ControllerFragment.15
            @Override // java.lang.Runnable
            public void run() {
                ControllerFragment controllerFragment = ControllerFragment.this;
                controllerFragment.QueryRFKeyStatusTimes = 0;
                controllerFragment.flag = false;
                controllerFragment.statusFlag = 2;
                while (ControllerFragment.this.QueryRFKeyStatusTimes < 140 && ControllerFragment.this.statusFlag == 2) {
                    long jUptimeMillis = SystemClock.uptimeMillis();
                    if (jUptimeMillis - ControllerFragment.this.lastOnclickTime >= 500) {
                        ControllerFragment.this.lastOnclickTime = jUptimeMillis;
                        IPCManager.getInstance().getDevice(ControllerFragment.this.iotId).QueryRFKeyStatus(i, new IPanelCallback() { // from class: fragment.ControllerFragment.15.1
                            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                            public void onComplete(boolean z, @Nullable Object obj) {
                                if (z) {
                                    Log.d(ControllerFragment.this.TAG, "onComplete: -------" + z);
                                    int iIntValue = JSONObject.parseObject(JSONObject.parseObject(String.valueOf(obj)).getString("data")).getInteger("Status").intValue();
                                    if (iIntValue == 1) {
                                        ControllerFragment.this.statusFlag = iIntValue;
                                        ControllerFragment.this.flag = true;
                                    } else if (iIntValue == 0) {
                                        ControllerFragment.this.statusFlag = iIntValue;
                                        ControllerFragment.this.flag = false;
                                    }
                                }
                            }
                        });
                        ControllerFragment.this.QueryRFKeyStatusTimes++;
                    }
                }
                if (ControllerFragment.this.getActivity() != null) {
                    ControllerFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: fragment.ControllerFragment.15.2
                        @Override // java.lang.Runnable
                        public void run() {
                            if (ControllerFragment.this.flag) {
                                dialogView.getStatus().setImageResource(R.drawable.success);
                                dialogView.hideConfirmButton();
                                dialogView.getTvDes().setText(R.string.learn_success);
                                dialogView.getTvCancel().setText(R.string.known);
                                ((IPCameraActivity) ControllerFragment.this.getActivity()).getControllerList();
                            } else {
                                dialogView.getTvDes().setText(R.string.learn_fail);
                                dialogView.getStatus().setImageResource(R.drawable.error);
                                dialogView.getTvConfirm().setText(R.string.relearn);
                            }
                            dialogView.getLoading().setVisibility(8);
                            dialogView.getStatus().setVisibility(0);
                        }
                    });
                }
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTips() {
        Context context = getContext();
        TextView textView = new TextView(context);
        textView.setText(R.string.autor_door_name);
        textView.setTextSize(20.0f);
        textView.setGravity(17);
        textView.setPadding(0, 50, 0, 0);
        textView.setTextColor(context.getResources().getColor(R.color.color_black));
        View viewInflate = View.inflate(context, R.layout.alertdialog_edittext_view, null);
        final EditText editText = (EditText) viewInflate.findViewById(R.id.editText);
        editText.setHint(R.string.rename);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        editText.setBackground(context.getResources().getDrawable(R.drawable.edittext_style));
        AlertDialog alertDialogCreate = new AlertDialog.Builder(context).setCustomTitle(textView).setView(viewInflate).setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() { // from class: fragment.ControllerFragment.17
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                ((FragmentActivity) Objects.requireNonNull(ControllerFragment.this.getActivity())).runOnUiThread(new Runnable() { // from class: fragment.ControllerFragment.17.1
                    /* JADX WARN: Removed duplicated region for block: B:7:0x004c  */
                    @Override // java.lang.Runnable
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                        To view partially-correct add '--show-bad-code' argument
                    */
                    public void run() {
                        /*
                            r4 = this;
                            tools.SharePreferenceManager r0 = tools.SharePreferenceManager.getInstance()
                            fragment.ControllerFragment$17 r1 = fragment.ControllerFragment.AnonymousClass17.this
                            fragment.ControllerFragment r1 = fragment.ControllerFragment.this
                            java.lang.String r1 = fragment.ControllerFragment.access$500(r1)
                            fragment.ControllerFragment$17 r2 = fragment.ControllerFragment.AnonymousClass17.this
                            android.widget.EditText r2 = r2
                            android.text.Editable r2 = r2.getText()
                            java.lang.String r2 = r2.toString()
                            fragment.ControllerFragment$17 r3 = fragment.ControllerFragment.AnonymousClass17.this
                            fragment.ControllerFragment r3 = fragment.ControllerFragment.this
                            int r3 = fragment.ControllerFragment.access$200(r3)
                            r0.saveAutoName(r1, r2, r3)
                            tools.SharePreferenceManager r0 = tools.SharePreferenceManager.getInstance()
                            fragment.ControllerFragment$17 r1 = fragment.ControllerFragment.AnonymousClass17.this
                            fragment.ControllerFragment r1 = fragment.ControllerFragment.this
                            java.lang.String r1 = fragment.ControllerFragment.access$500(r1)
                            fragment.ControllerFragment$17 r2 = fragment.ControllerFragment.AnonymousClass17.this
                            fragment.ControllerFragment r2 = fragment.ControllerFragment.this
                            int r2 = fragment.ControllerFragment.access$200(r2)
                            java.lang.String r0 = r0.getAutoName(r1, r2)
                            fragment.ControllerFragment$17 r1 = fragment.ControllerFragment.AnonymousClass17.this
                            fragment.ControllerFragment r1 = fragment.ControllerFragment.this
                            android.widget.TextView r1 = r1.door_text
                            if (r0 == 0) goto L4c
                            java.lang.String r2 = ""
                            boolean r2 = r0.equals(r2)
                            if (r2 != 0) goto L4c
                            goto L66
                        L4c:
                            fragment.ControllerFragment$17 r0 = fragment.ControllerFragment.AnonymousClass17.this
                            fragment.ControllerFragment r0 = fragment.ControllerFragment.this
                            java.util.List r0 = fragment.ControllerFragment.access$400(r0)
                            fragment.ControllerFragment$17 r2 = fragment.ControllerFragment.AnonymousClass17.this
                            fragment.ControllerFragment r2 = fragment.ControllerFragment.this
                            int r2 = fragment.ControllerFragment.access$200(r2)
                            java.lang.Object r0 = r0.get(r2)
                            android.widget.TextView r0 = (android.widget.TextView) r0
                            java.lang.CharSequence r0 = r0.getText()
                        L66:
                            r1.setText(r0)
                            fragment.ControllerFragment$17 r0 = fragment.ControllerFragment.AnonymousClass17.this
                            fragment.ControllerFragment r0 = fragment.ControllerFragment.this
                            fragment.ControllerFragment.access$900(r0)
                            return
                        */
                        throw new UnsupportedOperationException("Method not decompiled: fragment.ControllerFragment.AnonymousClass17.AnonymousClass1.run():void");
                    }
                });
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: fragment.ControllerFragment.16
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
        alertDialogCreate.setCanceledOnTouchOutside(false);
        alertDialogCreate.show();
        alertDialogCreate.getButton(-1).setTextColor(Color.parseColor("#2c99fd"));
        alertDialogCreate.getButton(-2).setTextColor(Color.parseColor("#E92E2E"));
        final Button button = alertDialogCreate.getButton(-1);
        Button button2 = alertDialogCreate.getButton(-2);
        button.setEnabled(false);
        button.setTextSize(18.0f);
        button2.setTextSize(18.0f);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
        layoutParams.weight = 13.0f;
        button.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) button2.getLayoutParams();
        layoutParams2.weight = 13.0f;
        button.setLayoutParams(layoutParams2);
        editText.addTextChangedListener(new TextWatcher() { // from class: fragment.ControllerFragment.18
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (editable != null && !editable.toString().equals("")) {
                    button.setEnabled(true);
                } else {
                    button.setEnabled(false);
                }
            }
        });
    }
}
