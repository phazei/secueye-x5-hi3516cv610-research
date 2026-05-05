package kt;

import activity.CommonActivity;
import adapter.APNDeviceInfoAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bean.APNBean;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.seculink.app.R;
import config.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import sdk.IPCManager;
import tools.SharePreferenceManager;
import view.ButtonView;
import view.TitleView;

/* JADX INFO: compiled from: APNActivity.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\f\u001a\u00020\rH\u0014J\b\u0010\u000e\u001a\u00020\u000fH\u0014J\u0012\u0010\u0010\u001a\u00020\u000f2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0014J\b\u0010\u0013\u001a\u00020\u000fH\u0014J\b\u0010\u0014\u001a\u00020\u000fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lkt/APNActivity;", "Lactivity/CommonActivity;", "()V", "apnBean", "Lbean/APNBean;", "iotId", "", "list1", "", "Lbean/APNBean$bean;", "mAPNAdapter", "Ladapter/APNDeviceInfoAdapter;", "getContentLayoutId", "", "initData", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onResume", "resetData", "secueye_googleRelease"}, k = 1, mv = {1, 1, 15})
public final class APNActivity extends CommonActivity {
    private HashMap _$_findViewCache;
    private String iotId;
    private APNDeviceInfoAdapter mAPNAdapter;
    private List<? extends APNBean.bean> list1 = new ArrayList();
    private APNBean apnBean = new APNBean();

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
        return R.layout.activity_apn_layout;
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        this.iotId = getIntent().getStringExtra("iotId");
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TitleView) _$_findCachedViewById(R.id.titleView)).setOnViewClick(new TitleView.OnViewClick() { // from class: kt.APNActivity.onCreate.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(@Nullable View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(@Nullable View view2) {
                APNActivity.this.finish();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(1);
        RecyclerView apn_adapter = (RecyclerView) _$_findCachedViewById(R.id.apn_adapter);
        Intrinsics.checkExpressionValueIsNotNull(apn_adapter, "apn_adapter");
        apn_adapter.setLayoutManager(linearLayoutManager);
        RecyclerView apn_adapter2 = (RecyclerView) _$_findCachedViewById(R.id.apn_adapter);
        Intrinsics.checkExpressionValueIsNotNull(apn_adapter2, "apn_adapter");
        apn_adapter2.setItemAnimator((RecyclerView.ItemAnimator) null);
        this.mAPNAdapter = new APNDeviceInfoAdapter(R.layout.apn_list_item);
        APNDeviceInfoAdapter aPNDeviceInfoAdapter = this.mAPNAdapter;
        if (aPNDeviceInfoAdapter == null) {
            Intrinsics.throwNpe();
        }
        aPNDeviceInfoAdapter.bindToRecyclerView((RecyclerView) _$_findCachedViewById(R.id.apn_adapter));
        APNDeviceInfoAdapter aPNDeviceInfoAdapter2 = this.mAPNAdapter;
        if (aPNDeviceInfoAdapter2 == null) {
            Intrinsics.throwNpe();
        }
        aPNDeviceInfoAdapter2.setListener(new APNDeviceInfoAdapter.OnChildClickListener() { // from class: kt.APNActivity.onCreate.2
            @Override // adapter.APNDeviceInfoAdapter.OnChildClickListener
            public final void click(int i) {
                Intent intent = new Intent(APNActivity.this.getApplicationContext(), (Class<?>) NewAPNActivity.class);
                APNDeviceInfoAdapter aPNDeviceInfoAdapter3 = APNActivity.this.mAPNAdapter;
                if (aPNDeviceInfoAdapter3 == null) {
                    Intrinsics.throwNpe();
                }
                APNBean.bean beanVar = aPNDeviceInfoAdapter3.getData().get(i);
                if (beanVar == null) {
                    throw new TypeCastException("null cannot be cast to non-null type bean.APNBean.bean");
                }
                intent.putExtra("bean", beanVar);
                intent.putExtra(RequestParameters.POSITION, i);
                intent.putExtra("iotId", APNActivity.this.iotId);
                intent.putExtra("isEdit", true);
                APNActivity.this.startActivity(intent);
            }
        });
        APNDeviceInfoAdapter aPNDeviceInfoAdapter3 = this.mAPNAdapter;
        if (aPNDeviceInfoAdapter3 == null) {
            Intrinsics.throwNpe();
        }
        aPNDeviceInfoAdapter3.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() { // from class: kt.APNActivity.onCreate.3
            @Override // com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener
            public final void onItemClick(BaseQuickAdapter<Object, BaseViewHolder> baseQuickAdapter, View view2, int i) {
                APNDeviceInfoAdapter aPNDeviceInfoAdapter4 = APNActivity.this.mAPNAdapter;
                if (aPNDeviceInfoAdapter4 == null) {
                    Intrinsics.throwNpe();
                }
                List<APNBean.bean> data = aPNDeviceInfoAdapter4.getData();
                Intrinsics.checkExpressionValueIsNotNull(data, "mAPNAdapter!!.data");
                int size = data.size();
                for (int i2 = 0; i2 < size; i2++) {
                    APNBean.bean beanVar = data.get(i2);
                    Intrinsics.checkExpressionValueIsNotNull(beanVar, "tempList[i]");
                    beanVar.setEnable(0);
                }
                APNBean.bean beanVar2 = data.get(i);
                Intrinsics.checkExpressionValueIsNotNull(beanVar2, "tempList[position]");
                beanVar2.setEnable(1);
                HashMap map = new HashMap();
                map.put(Constants.APNConfig, data);
                IPCManager.getInstance().getDevice(APNActivity.this.iotId).setProperties(map, new IPanelCallback() { // from class: kt.APNActivity.onCreate.3.1
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public final void onComplete(boolean z, @Nullable Object obj) {
                        Log.d(APNActivity.this.TAG, "setAPNConfig: puppet:o====" + String.valueOf(obj));
                    }
                });
                APNDeviceInfoAdapter aPNDeviceInfoAdapter5 = APNActivity.this.mAPNAdapter;
                if (aPNDeviceInfoAdapter5 == null) {
                    Intrinsics.throwNpe();
                }
                aPNDeviceInfoAdapter5.replaceData(data);
                APNDeviceInfoAdapter aPNDeviceInfoAdapter6 = APNActivity.this.mAPNAdapter;
                if (aPNDeviceInfoAdapter6 == null) {
                    Intrinsics.throwNpe();
                }
                aPNDeviceInfoAdapter6.notifyDataSetChanged();
            }
        });
        ((ButtonView) _$_findCachedViewById(R.id.reset_apn_button)).setOnClickListener(new View.OnClickListener() { // from class: kt.APNActivity.onCreate.4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                APNActivity.this.resetData();
            }
        });
        ((ButtonView) _$_findCachedViewById(R.id.new_apn_button)).setOnClickListener(new View.OnClickListener() { // from class: kt.APNActivity.onCreate.5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                Intent intent = new Intent(APNActivity.this.getApplicationContext(), (Class<?>) NewAPNActivity.class);
                intent.putExtra("iotId", APNActivity.this.iotId);
                intent.putExtra("isEdit", false);
                APNActivity.this.startActivity(intent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void resetData() {
        HashMap map = new HashMap();
        List<? extends APNBean.bean> list = this.list1;
        if (list == null) {
            Intrinsics.throwNpe();
        }
        map.put(Constants.APNConfig, list);
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: kt.APNActivity.resetData.1
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public final void onComplete(boolean z, @Nullable Object obj) {
                Log.d(APNActivity.this.TAG, "setAPNConfig: puppet:o====" + String.valueOf(obj));
            }
        });
        APNDeviceInfoAdapter aPNDeviceInfoAdapter = this.mAPNAdapter;
        if (aPNDeviceInfoAdapter == null) {
            Intrinsics.throwNpe();
        }
        aPNDeviceInfoAdapter.getData().clear();
        APNDeviceInfoAdapter aPNDeviceInfoAdapter2 = this.mAPNAdapter;
        if (aPNDeviceInfoAdapter2 == null) {
            Intrinsics.throwNpe();
        }
        aPNDeviceInfoAdapter2.notifyDataSetChanged();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        try {
            Object objFromJson = new Gson().fromJson(SharePreferenceManager.getInstance().getAPNConfig(this.iotId), (Class<Object>) APNBean.class);
            Intrinsics.checkExpressionValueIsNotNull(objFromJson, "Gson().fromJson(\n       …:class.java\n            )");
            this.apnBean = (APNBean) objFromJson;
        } catch (Exception e) {
            e.printStackTrace();
        }
        APNDeviceInfoAdapter aPNDeviceInfoAdapter = this.mAPNAdapter;
        if (aPNDeviceInfoAdapter == null) {
            Intrinsics.throwNpe();
        }
        aPNDeviceInfoAdapter.replaceData(this.apnBean.getList());
        APNDeviceInfoAdapter aPNDeviceInfoAdapter2 = this.mAPNAdapter;
        if (aPNDeviceInfoAdapter2 == null) {
            Intrinsics.throwNpe();
        }
        aPNDeviceInfoAdapter2.notifyDataSetChanged();
    }
}
