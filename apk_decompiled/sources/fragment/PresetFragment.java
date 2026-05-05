package fragment;

import adapter.PresetAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bean.PresetBean;
import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.openaccount.ui.util.ToastUtils;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.seculink.app.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import sdk.IPCManager;
import tools.PresetGridSpaceItemDecoration;
import tools.SpUtil;
import tools.Utils;

/* JADX INFO: loaded from: classes4.dex */
public class PresetFragment extends CommonFragment implements PresetAdapter.OnItemClickListener {
    private EditText appCompatEditText;
    private LinearLayout back_ll;
    private GridLayoutManager gridLayoutManager;
    private List<Integer> intList;
    private String iotId;
    private boolean isEdit;
    private boolean isOwner;
    private PresetDataChange listener;
    private PresetBackListener mListener;
    private List<Integer> positionList;
    private List<PresetBean> positionPresetList;
    private PresetAdapter presetAdapter;
    private Button presetAddButton;
    private List<PresetBean> presetBeanList;
    private Button presetInvokeButton;
    private List<Integer> presetList;
    private RecyclerView recyclerView;
    private int result = 0;
    private TextView textView;
    private ViewTreeObserver viewTreeObserver;

    public interface PresetBackListener {
        void presetBack();
    }

    public interface PresetDataChange {
        void deletePicture(PresetBean presetBean, RecyclerView.Adapter adapter2, int i);

        void go2Delete();

        void outDelete();

        void snapPicture(PresetBean presetBean, RecyclerView.Adapter adapter2, int i);
    }

    @Override // fragment.CommonFragment
    protected int getContentLayoutId() {
        return R.layout.preset_ipc_fragment_layout;
    }

    static /* synthetic */ int access$908(PresetFragment presetFragment) {
        int i = presetFragment.result;
        presetFragment.result = i + 1;
        return i;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // fragment.CommonFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mListener = (PresetBackListener) context;
        this.listener = (PresetDataChange) context;
    }

    @Override // fragment.CommonFragment
    protected void initWidget(View view2) {
        super.initWidget(view2);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.iotId = arguments.getString("iotId");
            this.isOwner = arguments.getBoolean("isOwner");
        }
        this.recyclerView = (RecyclerView) view2.findViewById(R.id.recyclerView);
        this.textView = (TextView) view2.findViewById(R.id.edit);
        this.textView.setOnClickListener(new View.OnClickListener() { // from class: fragment.PresetFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                PresetFragment.this.textView.post(new Runnable() { // from class: fragment.PresetFragment.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        PresetFragment.this.isEdit = !PresetFragment.this.isEdit;
                        if (PresetFragment.this.isEdit) {
                            if (PresetFragment.this.positionList != null) {
                                PresetFragment.this.positionList.clear();
                            } else {
                                PresetFragment.this.positionList = new ArrayList();
                            }
                            if (PresetFragment.this.positionPresetList != null) {
                                PresetFragment.this.positionPresetList.clear();
                            } else {
                                PresetFragment.this.positionPresetList = new ArrayList();
                            }
                            PresetFragment.this.listener.go2Delete();
                            PresetFragment.this.textView.setText(PresetFragment.this.getResources().getString(R.string.finish));
                            for (PresetBean presetBean : PresetFragment.this.presetAdapter.getmData()) {
                                presetBean.setEdit(true);
                                presetBean.setSelected(false);
                            }
                            PresetFragment.this.presetAdapter.notifyDataSetChanged();
                            return;
                        }
                        PresetFragment.this.listener.outDelete();
                        PresetFragment.this.textView.setText(PresetFragment.this.getResources().getString(R.string.edit));
                        for (PresetBean presetBean2 : PresetFragment.this.presetAdapter.getmData()) {
                            presetBean2.setEdit(false);
                            presetBean2.setSelected(false);
                        }
                        PresetFragment.this.presetAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        this.back_ll = (LinearLayout) view2.findViewById(R.id.back_ll);
        this.back_ll.setOnClickListener(new View.OnClickListener() { // from class: fragment.PresetFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                PresetFragment.this.isEdit = false;
                PresetFragment.this.textView.setText(PresetFragment.this.getResources().getString(R.string.edit));
                for (PresetBean presetBean : PresetFragment.this.presetAdapter.getmData()) {
                    presetBean.setEdit(false);
                    presetBean.setSelected(false);
                }
                PresetFragment.this.presetAdapter.notifyDataSetChanged();
                PresetFragment.this.mListener.presetBack();
            }
        });
        if (this.isOwner) {
            this.textView.setVisibility(0);
        } else {
            this.textView.setVisibility(8);
        }
        this.appCompatEditText = (EditText) view2.findViewById(R.id.et_preset);
        this.presetInvokeButton = (Button) view2.findViewById(R.id.bt_preset_invoke);
        this.presetInvokeButton.setOnClickListener(new View.OnClickListener() { // from class: fragment.PresetFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                if (PresetFragment.this.getActivity() == null) {
                    return;
                }
                PresetFragment presetFragment = PresetFragment.this;
                presetFragment.hideKeyboard(presetFragment.getActivity());
                if ("".equals(((Editable) Objects.requireNonNull(PresetFragment.this.appCompatEditText.getText())).toString())) {
                    return;
                }
                IPCManager.getInstance().getDevice(PresetFragment.this.iotId).changePresetLocation(Integer.parseInt(PresetFragment.this.appCompatEditText.getText().toString()), new IPanelCallback() { // from class: fragment.PresetFragment.3.1
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, @Nullable Object obj) {
                        if (z) {
                            try {
                                ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
        this.presetAddButton = (Button) view2.findViewById(R.id.bt_preset_add);
        this.presetAddButton.setOnClickListener(new View.OnClickListener() { // from class: fragment.PresetFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                if (PresetFragment.this.getActivity() != null) {
                    PresetFragment presetFragment = PresetFragment.this;
                    presetFragment.hideKeyboard(presetFragment.getActivity());
                    if ("".equals(((Editable) Objects.requireNonNull(PresetFragment.this.appCompatEditText.getText())).toString())) {
                        return;
                    }
                    IPCManager.getInstance().getDevice(PresetFragment.this.iotId).addPresetLocation(Integer.parseInt(PresetFragment.this.appCompatEditText.getText().toString()), new IPanelCallback() { // from class: fragment.PresetFragment.4.1
                        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                        public void onComplete(boolean z, @Nullable Object obj) {
                            if (z) {
                                try {
                                    ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        });
        update(null);
    }

    @Override // adapter.PresetAdapter.OnItemClickListener
    public void onItemClick(final PresetBean presetBean, final int i) {
        if (presetBean.isCanDelete()) {
            IPCManager.getInstance().getDevice(this.iotId).changePresetLocation(i + 1, new IPanelCallback() { // from class: fragment.PresetFragment.5
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (z) {
                        try {
                            ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else if (this.isOwner) {
            IPCManager.getInstance().getDevice(this.iotId).addPresetLocation(i + 1, new IPanelCallback() { // from class: fragment.PresetFragment.6
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (z) {
                        try {
                            if (((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                                PresetFragment.this.listener.snapPicture(presetBean, PresetFragment.this.presetAdapter, i);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    @Override // adapter.PresetAdapter.OnItemClickListener
    public void onItemDeleteClick(PresetBean presetBean, int i) {
        if (this.isEdit) {
            if (presetBean.isCanDelete()) {
                if (!presetBean.isSelected()) {
                    this.positionList.add(Integer.valueOf(i));
                    this.positionPresetList.add(presetBean);
                } else {
                    this.positionList.remove(Integer.valueOf(i));
                    this.positionPresetList.remove(presetBean);
                }
                presetBean.setSelected(!presetBean.isSelected());
            }
            if (i < 8) {
                this.presetAdapter.notifyItemChanged(i);
            }
            if (i == 57) {
                this.presetAdapter.notifyItemChanged(8);
            }
        }
    }

    public void hideKeyboard(Activity activity2) {
        ((InputMethodManager) activity2.getSystemService("input_method")).hideSoftInputFromWindow(activity2.getWindow().getDecorView().getWindowToken(), 0);
    }

    public void update(List<Integer> list) {
        if (isAdded()) {
            if (list == null || list.size() == 0) {
                List<Integer> list2 = this.presetList;
                if (list2 == null) {
                    this.presetList = new ArrayList();
                } else {
                    list2.clear();
                }
            } else {
                List<Integer> list3 = this.presetList;
                if (list3 == null) {
                    this.presetList = new ArrayList();
                } else {
                    list3.clear();
                }
                this.presetList = list;
            }
            List<PresetBean> list4 = this.presetBeanList;
            if (list4 == null) {
                this.presetBeanList = new ArrayList();
            } else {
                list4.clear();
            }
            for (int i = 1; i < 10; i++) {
                PresetBean presetBean = new PresetBean(i, this.iotId);
                presetBean.setOwner(this.isOwner);
                if (i == 9) {
                    List<Integer> list5 = this.presetList;
                    if (list5 == null || list5.size() == 0) {
                        presetBean.setString("");
                        presetBean.setCanDelete(false);
                    } else if (this.presetList.contains(Integer.valueOf(i))) {
                        presetBean.setCanDelete(true);
                        presetBean.setString(SpUtil.getString(getContext(), Utils.getDevSnapKey(this.iotId) + String.valueOf(58), ""));
                    } else {
                        presetBean.setString("");
                        presetBean.setCanDelete(false);
                    }
                } else {
                    List<Integer> list6 = this.presetList;
                    if (list6 == null || list6.size() == 0) {
                        presetBean.setString("");
                        presetBean.setCanDelete(false);
                    } else if (this.presetList.contains(Integer.valueOf(i))) {
                        presetBean.setCanDelete(true);
                        presetBean.setString(SpUtil.getString(getContext(), Utils.getDevSnapKey(this.iotId) + String.valueOf(i), ""));
                    } else {
                        presetBean.setString("");
                        presetBean.setCanDelete(false);
                    }
                }
                this.presetBeanList.add(presetBean);
            }
            this.gridLayoutManager = new GridLayoutManager(getContext(), 3);
            if (this.recyclerView.getLayoutManager() == null) {
                this.recyclerView.setLayoutManager(this.gridLayoutManager);
                this.recyclerView.addItemDecoration(new PresetGridSpaceItemDecoration(2, 2));
                this.presetAdapter = new PresetAdapter(getContext(), this.presetBeanList, this.presetList);
                this.presetAdapter.setOnItemClickListener(this);
                this.recyclerView.setAdapter(this.presetAdapter);
                return;
            }
            this.presetAdapter.setmData(this.presetBeanList, this.presetList);
            this.presetAdapter.notifyDataSetChanged();
        }
    }

    public void deletePresetPosition() {
        if (getActivity() != null) {
            if (this.positionList.size() <= 0) {
                getActivity().runOnUiThread(new Runnable() { // from class: fragment.PresetFragment.7
                    @Override // java.lang.Runnable
                    public void run() {
                        ToastUtils.toast(PresetFragment.this.getActivity(), PresetFragment.this.getString(R.string.delete_picture_ipc_tip));
                    }
                });
                return;
            }
            getActivity().runOnUiThread(new Runnable() { // from class: fragment.PresetFragment.8
                @Override // java.lang.Runnable
                public void run() {
                    PresetFragment.this.showProgressDialog();
                }
            });
            for (final int i = 0; i < this.positionList.size(); i++) {
                IPCManager.getInstance().getDevice(this.iotId).deletePresetLocation(this.positionList.get(i).intValue() + 1, new IPanelCallback() { // from class: fragment.PresetFragment.9
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, @Nullable Object obj) {
                        PresetFragment.access$908(PresetFragment.this);
                        if (z) {
                            try {
                                if (((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                                    PresetFragment.this.listener.deletePicture((PresetBean) PresetFragment.this.positionPresetList.get(i), PresetFragment.this.presetAdapter, ((Integer) PresetFragment.this.positionList.get(i)).intValue());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (PresetFragment.this.result == PresetFragment.this.positionList.size()) {
                            PresetFragment.this.result = 0;
                            PresetFragment.this.positionList.clear();
                            PresetFragment.this.positionPresetList.clear();
                            PresetFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: fragment.PresetFragment.9.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    PresetFragment.this.dismissProgressDialog();
                                    ToastUtils.toast(PresetFragment.this.getActivity(), PresetFragment.this.getString(R.string.delete_success_ipc));
                                }
                            });
                        }
                    }
                });
            }
        }
    }
}
