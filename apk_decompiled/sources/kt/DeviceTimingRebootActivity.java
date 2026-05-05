package kt;

import activity.CommonActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.core.view.ViewCompat;
import bean.DeviceInfoBean;
import bean.RebootBean;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.seculink.app.R;
import config.Constants;
import dialog.BaseDialog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sdk.IPCManager;
import tools.SharePreferenceManager;
import view.ItemView;

/* JADX INFO: compiled from: DeviceTimingRebootActivity.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010$\u001a\u00020\u0014H\u0014J\u0012\u0010%\u001a\u00020&2\b\u0010'\u001a\u0004\u0018\u00010(H\u0015J\b\u0010)\u001a\u00020&H\u0002J\b\u0010*\u001a\u00020&H\u0002R \u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR$\u0010\n\u001a\n\u0012\u0004\u0012\u00020\f\u0018\u00010\u000bX\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u0011\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0015\u001a\u00020\u0014X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001c\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u0011\u0010 \u001a\u00020!¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010#¨\u0006+"}, d2 = {"Lkt/DeviceTimingRebootActivity;", "Lactivity/CommonActivity;", "()V", "checkBoxList", "", "Landroid/widget/CheckBox;", "getCheckBoxList", "()Ljava/util/List;", "setCheckBoxList", "(Ljava/util/List;)V", "days", "", "", "getDays", "()[Ljava/lang/String;", "setDays", "([Ljava/lang/String;)V", "[Ljava/lang/String;", "iotId", "mHour", "", "mMinute", "getMMinute", "()I", "setMMinute", "(I)V", "rebootBean", "Lbean/RebootBean;", "getRebootBean", "()Lbean/RebootBean;", "setRebootBean", "(Lbean/RebootBean;)V", "uiHandler", "Landroid/os/Handler;", "getUiHandler", "()Landroid/os/Handler;", "getContentLayoutId", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "setItemDay", "updateDevParam", "secueye_googleRelease"}, k = 1, mv = {1, 1, 15})
public final class DeviceTimingRebootActivity extends CommonActivity {
    private HashMap _$_findViewCache;

    @Nullable
    private String[] days;
    private String iotId;
    private int mHour;
    private int mMinute;

    @Nullable
    private RebootBean rebootBean;

    @NotNull
    private List<CheckBox> checkBoxList = new ArrayList();

    @NotNull
    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    public void _$_clearFindViewByIdCache() {
        HashMap map = this._$_findViewCache;
        if (map != null) {
            map.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view2 = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view2 != null) {
            return view2;
        }
        View viewFindViewById = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), viewFindViewById);
        return viewFindViewById;
    }

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_device_timing_reset_layout;
    }

    @NotNull
    public final List<CheckBox> getCheckBoxList() {
        return this.checkBoxList;
    }

    public final void setCheckBoxList(@NotNull List<CheckBox> list) {
        Intrinsics.checkParameterIsNotNull(list, "<set-?>");
        this.checkBoxList = list;
    }

    @Nullable
    public final String[] getDays() {
        return this.days;
    }

    public final void setDays(@Nullable String[] strArr) {
        this.days = strArr;
    }

    public final int getMMinute() {
        return this.mMinute;
    }

    public final void setMMinute(int i) {
        this.mMinute = i;
    }

    @Nullable
    public final RebootBean getRebootBean() {
        return this.rebootBean;
    }

    public final void setRebootBean(@Nullable RebootBean rebootBean) {
        this.rebootBean = rebootBean;
    }

    @NotNull
    public final Handler getUiHandler() {
        return this.uiHandler;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x00a2  */
    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    @android.annotation.SuppressLint({"InflateParams", "NewApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void onCreate(@org.jetbrains.annotations.Nullable android.os.Bundle r10) {
        /*
            Method dump skipped, instruction units count: 879
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kt.DeviceTimingRebootActivity.onCreate(android.os.Bundle):void");
    }

    /* JADX INFO: renamed from: kt.DeviceTimingRebootActivity$onCreate$1, reason: invalid class name */
    /* JADX INFO: compiled from: DeviceTimingRebootActivity.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/view/View;", "kotlin.jvm.PlatformType", "onClick"}, k = 3, mv = {1, 1, 15})
    static final class AnonymousClass1 implements View.OnClickListener {
        final /* synthetic */ DeviceInfoBean $deviceInfoBean;
        final /* synthetic */ Handler $mHandler;

        AnonymousClass1(DeviceInfoBean deviceInfoBean, Handler handler) {
            this.$deviceInfoBean = deviceInfoBean;
            this.$mHandler = handler;
        }

        @Override // android.view.View.OnClickListener
        public final void onClick(View view2) {
            new BaseDialog.Builder().view(R.layout.dialog_delete_camera).content(DeviceTimingRebootActivity.this.getString(R.string.sure_restart_camera)).leftBtnText(DeviceTimingRebootActivity.this.getString(R.string.sure_restart)).rightBtnText(DeviceTimingRebootActivity.this.getString(R.string.cancel)).clickLeft(new ViewOnClickListenerC03001()).canCancel(false).create().show(DeviceTimingRebootActivity.this.getSupportFragmentManager(), "");
        }

        /* JADX INFO: renamed from: kt.DeviceTimingRebootActivity$onCreate$1$1, reason: invalid class name and collision with other inner class name */
        /* JADX INFO: compiled from: DeviceTimingRebootActivity.kt */
        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/view/View;", "kotlin.jvm.PlatformType", "onClick"}, k = 3, mv = {1, 1, 15})
        static final class ViewOnClickListenerC03001 implements View.OnClickListener {
            ViewOnClickListenerC03001() {
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                IPCManager.getInstance().getDevice(AnonymousClass1.this.$deviceInfoBean.getIotId()).reboot(new IPanelCallback() { // from class: kt.DeviceTimingRebootActivity.onCreate.1.1.1
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public final void onComplete(boolean z, @Nullable Object obj) {
                        Log.e(DeviceTimingRebootActivity.this.TAG, "reboot onComplete: " + z);
                        if (z && obj != null && (!Intrinsics.areEqual("", obj.toString()))) {
                            JSONObject object = JSONObject.parseObject(obj.toString());
                            if (object.containsKey("code")) {
                                Integer integer = object.getInteger("code");
                                if (integer == null || integer.intValue() != 200) {
                                    AnonymousClass1.this.$mHandler.post(new Runnable() { // from class: kt.DeviceTimingRebootActivity.onCreate.1.1.1.1
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            Toast.makeText(DeviceTimingRebootActivity.this.getActivity(), R.string.restart_dev_failed, 0).show();
                                        }
                                    });
                                } else {
                                    AnonymousClass1.this.$mHandler.post(new Runnable() { // from class: kt.DeviceTimingRebootActivity.onCreate.1.1.1.2
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            Toast.makeText(DeviceTimingRebootActivity.this.getActivity(), R.string.restart_dev_succeed, 0).show();
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    /* JADX INFO: renamed from: kt.DeviceTimingRebootActivity$onCreate$7, reason: invalid class name */
    /* JADX INFO: compiled from: DeviceTimingRebootActivity.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/view/View;", "kotlin.jvm.PlatformType", "onClick"}, k = 3, mv = {1, 1, 15})
    static final class AnonymousClass7 implements View.OnClickListener {
        AnonymousClass7() {
        }

        @Override // android.view.View.OnClickListener
        public final void onClick(View view2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DeviceTimingRebootActivity.this);
            View viewInflate = DeviceTimingRebootActivity.this.getLayoutInflater().inflate(R.layout.time_pick_dialog, (ViewGroup) null);
            if (viewInflate == null) {
                throw new TypeCastException("null cannot be cast to non-null type android.view.View");
            }
            final TimePicker timePicker = (TimePicker) viewInflate.findViewById(R.id.time_picker);
            if (timePicker == null) {
                throw new TypeCastException("null cannot be cast to non-null type android.widget.TimePicker");
            }
            timePicker.setIs24HourView(true);
            RebootBean rebootBean = DeviceTimingRebootActivity.this.getRebootBean();
            if (rebootBean == null) {
                Intrinsics.throwNpe();
            }
            timePicker.setHour(rebootBean.getTime().intValue() / 3600);
            RebootBean rebootBean2 = DeviceTimingRebootActivity.this.getRebootBean();
            if (rebootBean2 == null) {
                Intrinsics.throwNpe();
            }
            timePicker.setMinute((rebootBean2.getTime().intValue() / 60) % 60);
            builder.setView(viewInflate);
            builder.setTitle(DeviceTimingRebootActivity.this.getString(R.string.set_timing_time));
            AlertDialog alertDialogCreate = builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() { // from class: kt.DeviceTimingRebootActivity$onCreate$7$dialog$1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    Object objValueOf;
                    Object objValueOf2;
                    DeviceTimingRebootActivity.this.mHour = timePicker.getHour();
                    DeviceTimingRebootActivity.this.setMMinute(timePicker.getMinute());
                    ItemView setting_reboot_time = (ItemView) DeviceTimingRebootActivity.this._$_findCachedViewById(R.id.setting_reboot_time);
                    Intrinsics.checkExpressionValueIsNotNull(setting_reboot_time, "setting_reboot_time");
                    StringBuilder sb = new StringBuilder();
                    if (DeviceTimingRebootActivity.this.mHour < 10) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append('0');
                        sb2.append(DeviceTimingRebootActivity.this.mHour);
                        objValueOf = sb2.toString();
                    } else {
                        objValueOf = Integer.valueOf(DeviceTimingRebootActivity.this.mHour);
                    }
                    sb.append(objValueOf);
                    sb.append(":");
                    if (DeviceTimingRebootActivity.this.getMMinute() < 10) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append('0');
                        sb3.append(DeviceTimingRebootActivity.this.getMMinute());
                        objValueOf2 = sb3.toString();
                    } else {
                        objValueOf2 = Integer.valueOf(DeviceTimingRebootActivity.this.getMMinute());
                    }
                    sb.append(objValueOf2);
                    sb.append(":00");
                    setting_reboot_time.setRightText(sb.toString());
                    dialogInterface.cancel();
                    RebootBean rebootBean3 = DeviceTimingRebootActivity.this.getRebootBean();
                    if (rebootBean3 == null) {
                        Intrinsics.throwNpe();
                    }
                    rebootBean3.setTime(Integer.valueOf((DeviceTimingRebootActivity.this.mHour * 3600) + (DeviceTimingRebootActivity.this.getMMinute() * 60)));
                    DeviceTimingRebootActivity.this.updateDevParam();
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: kt.DeviceTimingRebootActivity$onCreate$7$dialog$2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            }).create();
            alertDialogCreate.show();
            alertDialogCreate.getButton(-1).setTextColor(Color.parseColor("#2c99fd"));
            alertDialogCreate.getButton(-2).setTextColor(ViewCompat.MEASURED_STATE_MASK);
            Button button = alertDialogCreate.getButton(-1);
            Intrinsics.checkExpressionValueIsNotNull(button, "dialog.getButton(AlertDialog.BUTTON_POSITIVE)");
            Button button2 = alertDialogCreate.getButton(-2);
            Intrinsics.checkExpressionValueIsNotNull(button2, "dialog.getButton(AlertDialog.BUTTON_NEGATIVE)");
            ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
            if (layoutParams == null) {
                throw new TypeCastException("null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
            }
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
            layoutParams2.weight = 10.0f;
            LinearLayout.LayoutParams layoutParams3 = layoutParams2;
            button.setLayoutParams(layoutParams3);
            button2.setLayoutParams(layoutParams3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setItemDay() {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        int size = this.checkBoxList.size();
        for (int i = 0; i < size; i++) {
            if (this.checkBoxList.get(i).isChecked()) {
                String[] strArr = this.days;
                sb.append(strArr != null ? strArr[i] : null);
                sb.append("、");
                sb2.append("1");
            } else {
                sb2.append("0");
            }
        }
        if (sb.length() == 0) {
            ItemView repeat = (ItemView) _$_findCachedViewById(R.id.repeat);
            Intrinsics.checkExpressionValueIsNotNull(repeat, "repeat");
            repeat.setRightText(getString(R.string.not_set));
        } else if (sb.length() == 21) {
            ItemView repeat2 = (ItemView) _$_findCachedViewById(R.id.repeat);
            Intrinsics.checkExpressionValueIsNotNull(repeat2, "repeat");
            repeat2.setRightText(getString(R.string.everyday));
        } else {
            ItemView repeat3 = (ItemView) _$_findCachedViewById(R.id.repeat);
            Intrinsics.checkExpressionValueIsNotNull(repeat3, "repeat");
            repeat3.setRightText(sb.substring(0, sb.length() - 1));
        }
        String string = sb2.reverse().toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "set.reverse().toString()");
        int i2 = Integer.parseInt(string, CharsKt.checkRadix(2));
        RebootBean rebootBean = this.rebootBean;
        if (rebootBean == null) {
            Intrinsics.throwNpe();
        }
        rebootBean.setWeekMask(Integer.valueOf(i2));
        updateDevParam();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateDevParam() {
        HashMap map = new HashMap();
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = jSONObject;
        RebootBean rebootBean = this.rebootBean;
        if (rebootBean == null) {
            Intrinsics.throwNpe();
        }
        jSONObject2.put(Constants.Enable, rebootBean.getEnable());
        RebootBean rebootBean2 = this.rebootBean;
        if (rebootBean2 == null) {
            Intrinsics.throwNpe();
        }
        jSONObject2.put("Time", rebootBean2.getTime());
        RebootBean rebootBean3 = this.rebootBean;
        if (rebootBean3 == null) {
            Intrinsics.throwNpe();
        }
        jSONObject2.put("WeekMask", rebootBean3.getWeekMask());
        map.put(Constants.RebootSchedule, jSONObject);
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: kt.DeviceTimingRebootActivity.updateDevParam.1
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public final void onComplete(boolean z, @Nullable Object obj) {
                if (z && obj != null && (!Intrinsics.areEqual("", obj.toString()))) {
                    JSONObject object = JSONObject.parseObject(obj.toString());
                    if (object.containsKey("code")) {
                        Integer integer = object.getInteger("code");
                        if (integer != null && integer.intValue() == 200) {
                            SharePreferenceManager.getInstance().setRebootSchedule(DeviceTimingRebootActivity.this.iotId, String.valueOf(DeviceTimingRebootActivity.this.getRebootBean()));
                            DeviceTimingRebootActivity.this.getUiHandler().post(new Runnable() { // from class: kt.DeviceTimingRebootActivity.updateDevParam.1.2
                                @Override // java.lang.Runnable
                                public final void run() {
                                    Toast.makeText(DeviceTimingRebootActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                }
                            });
                        } else {
                            DeviceTimingRebootActivity.this.getUiHandler().post(new Runnable() { // from class: kt.DeviceTimingRebootActivity.updateDevParam.1.1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    Toast.makeText(DeviceTimingRebootActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        }
                    }
                }
            }
        });
    }
}
