package dialog;

import activity.OAMobileCountrySelectorActivity;
import adapter.ShareHistoryAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bean.AreaCodeModel;
import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.hjq.permissions.Permission;
import com.seculink.app.R;
import dialog.BaseDialog;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import tools.OnMultiClickListener;
import tools.SharePreferenceManager;
import tools.Utils;

/* JADX INFO: loaded from: classes3.dex */
public class ShareDialog extends BaseDialog {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private List<AreaCodeModel> datalist;
    EditText etShareText;
    private Object extra;
    ImageView iv_history;
    private Drawable mDrawable;
    private int mode = 0;
    private OnShareClickListener onShareClickListener;
    private int position;
    RecyclerView rv_history;
    TextView tvDistinct;
    TextView tvShareSwitcher;

    public interface OnShareClickListener {
        void onDistinctChange();

        void onDistinctSelect(AreaCodeModel areaCodeModel);

        void onShareSwitchChange();
    }

    @Override // dialog.BaseDialog, androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(final LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, Bundle bundle) {
        View viewOnCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        this.etShareText = (EditText) viewOnCreateView.findViewById(R.id.et_input);
        this.etShareText.setHintTextColor(ViewCompat.MEASURED_STATE_MASK);
        this.tvShareSwitcher = (TextView) viewOnCreateView.findViewById(R.id.tv_share_tip);
        this.tvDistinct = (TextView) viewOnCreateView.findViewById(R.id.tv_distinct);
        this.rv_history = (RecyclerView) viewOnCreateView.findViewById(R.id.rv_history);
        this.iv_history = (ImageView) viewOnCreateView.findViewById(R.id.iv_history);
        this.tvShareSwitcher.setOnClickListener(new View.OnClickListener() { // from class: dialog.ShareDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (ShareDialog.this.tvDistinct.getVisibility() == 0) {
                    ShareDialog.this.tvDistinct.setVisibility(8);
                    ShareDialog.this.etShareText.setHint(R.string.input_share_email);
                    ShareDialog.this.setInputType(33);
                    ShareDialog shareDialog = ShareDialog.this;
                    shareDialog.mDrawable = shareDialog.getResources().getDrawable(R.drawable.phone);
                    ShareDialog.this.mDrawable.setBounds(0, 0, ShareDialog.this.mDrawable.getIntrinsicWidth(), ShareDialog.this.mDrawable.getMinimumHeight());
                    ShareDialog.this.tvShareSwitcher.setCompoundDrawables(ShareDialog.this.mDrawable, null, null, null);
                    ShareDialog.this.tvShareSwitcher.setText(R.string.phone_share_tip);
                    ShareDialog.this.mode = 1;
                } else {
                    ShareDialog.this.tvDistinct.setVisibility(0);
                    ShareDialog.this.etShareText.setHint(R.string.input_share_phone);
                    ShareDialog.this.setInputType(2);
                    ShareDialog shareDialog2 = ShareDialog.this;
                    shareDialog2.mDrawable = shareDialog2.getResources().getDrawable(R.drawable.email);
                    ShareDialog.this.mDrawable.setBounds(0, 0, ShareDialog.this.mDrawable.getIntrinsicWidth(), ShareDialog.this.mDrawable.getMinimumHeight());
                    ShareDialog.this.tvShareSwitcher.setCompoundDrawables(ShareDialog.this.mDrawable, null, null, null);
                    ShareDialog.this.tvShareSwitcher.setText(R.string.email_share_tip);
                    if (ContextCompat.checkSelfPermission(ShareDialog.this.getContext(), Permission.READ_PHONE_STATE) != 0) {
                        ActivityCompat.requestPermissions(ShareDialog.this.getActivity(), new String[]{Permission.READ_PHONE_STATE, Permission.READ_PHONE_STATE}, 1);
                    }
                    ShareDialog.this.tvDistinct.setText(MqttTopic.SINGLE_LEVEL_WILDCARD + Utils.getAreaCode(ShareDialog.this.getActivity()));
                    ShareDialog.this.mode = 0;
                }
                ShareDialog.this.etShareText.setText("");
                if (ShareDialog.this.onShareClickListener != null) {
                    ShareDialog.this.onShareClickListener.onShareSwitchChange();
                }
            }
        });
        this.tvDistinct.setOnClickListener(new View.OnClickListener() { // from class: dialog.ShareDialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (ShareDialog.this.onShareClickListener != null) {
                    ShareDialog.this.onShareClickListener.onDistinctChange();
                }
                ShareDialog.this.startActivityForResult(new Intent(ShareDialog.this.getActivity(), (Class<?>) OAMobileCountrySelectorActivity.class), 17);
            }
        });
        this.tvDistinct.setText(MqttTopic.SINGLE_LEVEL_WILDCARD + Utils.getAreaCode(getActivity()));
        this.iv_history.setOnClickListener(new OnMultiClickListener() { // from class: dialog.ShareDialog.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (!SharePreferenceManager.getInstance().getShareHistory().isEmpty()) {
                    ShareDialog.this.rv_history.setVisibility(ShareDialog.this.rv_history.getVisibility() != 8 ? 8 : 0);
                } else {
                    Toast.makeText(layoutInflater.getContext(), R.string.no_shared, 0).show();
                }
            }
        });
        ArrayList arrayList = new ArrayList();
        if (!SharePreferenceManager.getInstance().getShareHistory().isEmpty()) {
            arrayList.addAll(JSON.parseArray(SharePreferenceManager.getInstance().getShareHistory(), String.class));
        }
        this.rv_history.setLayoutManager(new LinearLayoutManager(layoutInflater.getContext()));
        ShareHistoryAdapter shareHistoryAdapter = new ShareHistoryAdapter(layoutInflater.getContext(), arrayList);
        this.rv_history.setAdapter(shareHistoryAdapter);
        shareHistoryAdapter.setOnItemClickListener(new ShareHistoryAdapter.OnItemClickListener() { // from class: dialog.ShareDialog.4
            @Override // adapter.ShareHistoryAdapter.OnItemClickListener
            public void onItemClick(String str, int i) {
                ShareDialog.this.etShareText.setText(str);
                ShareDialog.this.rv_history.setVisibility(8);
            }
        });
        return viewOnCreateView;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 17 && i2 == -1 && intent != null) {
            String stringExtra = intent.getStringExtra("countryCode");
            this.tvDistinct.setText(MqttTopic.SINGLE_LEVEL_WILDCARD + stringExtra);
        }
    }

    public void setOnShareClick(OnShareClickListener onShareClickListener) {
        this.onShareClickListener = onShareClickListener;
    }

    public String getContent() {
        return this.etShareText.getText().toString();
    }

    public String getDistinct() {
        return this.tvDistinct.getText().toString();
    }

    public void setInputType(int i) {
        this.etShareText.setInputType(i);
    }

    public void setExtra(Object obj) {
        this.extra = obj;
    }

    public Object getExtra() {
        return this.extra;
    }

    public int getMode() {
        return this.mode;
    }

    @Override // dialog.BaseDialog, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = -2;
        attributes.height = -2;
        window.setAttributes(attributes);
    }

    public void chooseDistinct() {
        ArrayList arrayList = new ArrayList();
        this.datalist = Utils.readAreaCodeList((Context) Objects.requireNonNull(getActivity()));
        int i = this.position;
        for (AreaCodeModel areaCodeModel : this.datalist) {
            arrayList.add(areaCodeModel.getName() + " +" + areaCodeModel.getTel());
        }
        OptionsPickerView optionsPickerViewBuild = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() { // from class: dialog.ShareDialog.5
            @Override // com.bigkoo.pickerview.listener.OnOptionsSelectListener
            public void onOptionsSelect(int i2, int i3, int i4, View view2) {
                if (ShareDialog.this.onShareClickListener != null) {
                    ShareDialog.this.onShareClickListener.onDistinctSelect((AreaCodeModel) ShareDialog.this.datalist.get(i2));
                }
                ShareDialog.this.position = i2;
                ShareDialog.this.tvDistinct.setText(MqttTopic.SINGLE_LEVEL_WILDCARD + ((AreaCodeModel) ShareDialog.this.datalist.get(i2)).getTel());
            }
        }).setSubmitText(getResources().getString(R.string.confirm)).setCancelText(getResources().getString(R.string.cancel)).setSubmitColor(getResources().getColor(R.color.colorAccent)).setCancelColor(getResources().getColor(R.color.colorAccent)).isDialog(true).build();
        Dialog dialog2 = optionsPickerViewBuild.getDialog();
        if (dialog2 != null) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2, 80);
            layoutParams.leftMargin = 0;
            layoutParams.rightMargin = 0;
            optionsPickerViewBuild.getDialogContainerLayout().setLayoutParams(layoutParams);
            Window window = dialog2.getWindow();
            if (window != null) {
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.width = -1;
                attributes.height = -2;
                window.setAttributes(attributes);
                window.setWindowAnimations(R.style.picker_view_slide_anim);
                window.setGravity(80);
            }
        }
        optionsPickerViewBuild.setPicker(arrayList);
        optionsPickerViewBuild.setSelectOptions(this.position);
        optionsPickerViewBuild.show();
    }

    public static class Builder extends BaseDialog.Builder {
        @Override // dialog.BaseDialog.Builder
        public ShareDialog create() {
            return (ShareDialog) super.create(new ShareDialog());
        }
    }
}
