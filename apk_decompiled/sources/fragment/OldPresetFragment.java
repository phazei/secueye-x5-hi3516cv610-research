package fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.seculink.app.R;
import java.util.Objects;
import sdk.IPCManager;

/* JADX INFO: loaded from: classes4.dex */
public class OldPresetFragment extends CommonFragment {
    private EditText appCompatEditText;
    private LinearLayout back_ll;
    private String iotId = "";
    private OldPresetBackListener mListener;
    private Button presetAddButton;
    private Button presetInvokeButton;

    public interface OldPresetBackListener {
        void oldPresetBack();
    }

    @Override // fragment.CommonFragment
    protected int getContentLayoutId() {
        return R.layout.old_preset_ipc_fragment_layout;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // fragment.CommonFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mListener = (OldPresetBackListener) context;
    }

    @Override // fragment.CommonFragment
    protected void initWidget(View view2) {
        super.initWidget(view2);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.iotId = arguments.getString("iotId");
        }
        this.appCompatEditText = (EditText) view2.findViewById(R.id.et_preset);
        this.presetInvokeButton = (Button) view2.findViewById(R.id.bt_preset_invoke);
        this.presetInvokeButton.setOnClickListener(new View.OnClickListener() { // from class: fragment.OldPresetFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                if (OldPresetFragment.this.getActivity() == null) {
                    return;
                }
                OldPresetFragment oldPresetFragment = OldPresetFragment.this;
                oldPresetFragment.hideKeyboard(oldPresetFragment.getActivity());
                if ("".equals(((Editable) Objects.requireNonNull(OldPresetFragment.this.appCompatEditText.getText())).toString())) {
                    return;
                }
                IPCManager.getInstance().getDevice(OldPresetFragment.this.iotId).changePresetLocation(Integer.parseInt(OldPresetFragment.this.appCompatEditText.getText().toString()), new IPanelCallback() { // from class: fragment.OldPresetFragment.1.1
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, @Nullable Object obj) {
                        if (z) {
                            ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode();
                        }
                    }
                });
            }
        });
        this.presetAddButton = (Button) view2.findViewById(R.id.bt_preset_add);
        this.presetAddButton.setOnClickListener(new View.OnClickListener() { // from class: fragment.OldPresetFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                if (OldPresetFragment.this.getActivity() == null) {
                    return;
                }
                OldPresetFragment oldPresetFragment = OldPresetFragment.this;
                oldPresetFragment.hideKeyboard(oldPresetFragment.getActivity());
                if ("".equals(((Editable) Objects.requireNonNull(OldPresetFragment.this.appCompatEditText.getText())).toString())) {
                    return;
                }
                IPCManager.getInstance().getDevice(OldPresetFragment.this.iotId).addPresetLocation(Integer.parseInt(OldPresetFragment.this.appCompatEditText.getText().toString()), new IPanelCallback() { // from class: fragment.OldPresetFragment.2.1
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, @Nullable Object obj) {
                        if (z) {
                            ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode();
                        }
                    }
                });
            }
        });
        this.back_ll = (LinearLayout) view2.findViewById(R.id.back_ll);
        this.back_ll.setOnClickListener(new View.OnClickListener() { // from class: fragment.OldPresetFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                OldPresetFragment.this.mListener.oldPresetBack();
            }
        });
    }

    public void hideKeyboard(Activity activity2) {
        ((InputMethodManager) activity2.getSystemService("input_method")).hideSoftInputFromWindow(activity2.getWindow().getDecorView().getWindowToken(), 0);
    }
}
