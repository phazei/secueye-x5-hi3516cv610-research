package kt;

import activity.CommonActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import bean.APNBean;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.google.gson.Gson;
import com.seculink.app.R;
import config.Constants;
import dialog.InputDialogViewAPN;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sdk.IPCManager;
import tools.SharePreferenceManager;
import view.ItemView;
import view.SelectorDialogFragment;
import view.TitleView;

/* JADX INFO: compiled from: NewAPNActivity.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002:\u0001.B\u0005¢\u0006\u0002\u0010\u0003J\u0006\u0010\u001b\u001a\u00020\u001cJ\b\u0010\u001d\u001a\u00020\u000fH\u0014J\u0012\u0010\u001e\u001a\u00020\u001c2\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0016J\u0012\u0010!\u001a\u00020\u001c2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0014J\u0006\u0010$\u001a\u00020\u001cJ\u0018\u0010%\u001a\u00020\u001c2\u0006\u0010&\u001a\u00020\u00052\u0006\u0010'\u001a\u00020(H\u0002J\u000e\u0010)\u001a\u00020\u001c2\u0006\u0010*\u001a\u00020\bJ\u0016\u0010)\u001a\u00020\u001c2\u0006\u0010*\u001a\u00020\b2\u0006\u0010+\u001a\u00020\u000fJ\u0016\u0010)\u001a\u00020\u001c2\u0006\u0010*\u001a\u00020\b2\u0006\u0010+\u001a\u00020\bJ\u000e\u0010,\u001a\u00020\u001c2\u0006\u0010-\u001a\u00020\bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082.¢\u0006\u0004\n\u0002\u0010\tR\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0011\u001a\u0006\u0012\u0002\b\u00030\u0012X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u0016\u001a\n\u0012\u0004\u0012\u00020\r\u0018\u00010\u0017X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082.¢\u0006\u0004\n\u0002\u0010\tR\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u000bX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006/"}, d2 = {"Lkt/NewAPNActivity;", "Lactivity/CommonActivity;", "Landroid/view/View$OnClickListener;", "()V", "apnBean", "Lbean/APNBean;", "authTypeMode", "", "", "[Ljava/lang/String;", "authTypeSelectDialogFragment", "Lview/SelectorDialogFragment;", "bean", "Lbean/APNBean$bean;", "currentAuthType", "", "currentProtocol", "inputDialogView", "Ldialog/InputDialogViewAPN;", "iotId", "isEdit", "", AlinkConstants.KEY_LIST, "", RequestParameters.POSITION, "protocolMode", "protocolSelectDialogFragment", RequestParameters.SUBRESOURCE_DELETE, "", "getContentLayoutId", "onClick", "v", "Landroid/view/View;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "save", "setAPNConfig", "i", NotificationCompat.CATEGORY_CALL, "Lkt/NewAPNActivity$APN;", "showDialog", "string", "value", "showNullDialog", "str", "APN", "secueye_googleRelease"}, k = 1, mv = {1, 1, 15})
public final class NewAPNActivity extends CommonActivity implements View.OnClickListener {
    private HashMap _$_findViewCache;
    private String[] authTypeMode;
    private SelectorDialogFragment authTypeSelectDialogFragment;
    private APNBean.bean bean;
    private int currentAuthType;
    private int currentProtocol;
    private InputDialogViewAPN<?> inputDialogView;
    private boolean isEdit;
    private int position;
    private String[] protocolMode;
    private SelectorDialogFragment protocolSelectDialogFragment;
    private List<? extends APNBean.bean> list = new ArrayList();
    private APNBean apnBean = new APNBean();
    private String iotId = "";

    /* JADX INFO: compiled from: NewAPNActivity.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b`\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\u0006"}, d2 = {"Lkt/NewAPNActivity$APN;", "", "finish", "", "b", "", "secueye_googleRelease"}, k = 1, mv = {1, 1, 15})
    public interface APN {
        void finish(boolean b2);
    }

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
        return R.layout.activity_new_apn_layout;
    }

    public static final /* synthetic */ String[] access$getAuthTypeMode$p(NewAPNActivity newAPNActivity) {
        String[] strArr = newAPNActivity.authTypeMode;
        if (strArr == null) {
            Intrinsics.throwUninitializedPropertyAccessException("authTypeMode");
        }
        return strArr;
    }

    public static final /* synthetic */ APNBean.bean access$getBean$p(NewAPNActivity newAPNActivity) {
        APNBean.bean beanVar = newAPNActivity.bean;
        if (beanVar == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bean");
        }
        return beanVar;
    }

    public static final /* synthetic */ InputDialogViewAPN access$getInputDialogView$p(NewAPNActivity newAPNActivity) {
        InputDialogViewAPN<?> inputDialogViewAPN = newAPNActivity.inputDialogView;
        if (inputDialogViewAPN == null) {
            Intrinsics.throwUninitializedPropertyAccessException("inputDialogView");
        }
        return inputDialogViewAPN;
    }

    public static final /* synthetic */ String[] access$getProtocolMode$p(NewAPNActivity newAPNActivity) {
        String[] strArr = newAPNActivity.protocolMode;
        if (strArr == null) {
            Intrinsics.throwUninitializedPropertyAccessException("protocolMode");
        }
        return strArr;
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] stringArray = getResources().getStringArray(R.array.authTypeMode);
        Intrinsics.checkExpressionValueIsNotNull(stringArray, "resources.getStringArray(R.array.authTypeMode)");
        this.authTypeMode = stringArray;
        String[] stringArray2 = getResources().getStringArray(R.array.protocolMode);
        Intrinsics.checkExpressionValueIsNotNull(stringArray2, "resources.getStringArray(R.array.protocolMode)");
        this.protocolMode = stringArray2;
        String string = getString(R.string.auth_type);
        String[] strArr = this.authTypeMode;
        if (strArr == null) {
            Intrinsics.throwUninitializedPropertyAccessException("authTypeMode");
        }
        this.authTypeSelectDialogFragment = new SelectorDialogFragment(string, (String[]) Arrays.copyOf(strArr, strArr.length));
        SelectorDialogFragment selectorDialogFragment = this.authTypeSelectDialogFragment;
        if (selectorDialogFragment == null) {
            Intrinsics.throwNpe();
        }
        selectorDialogFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: kt.NewAPNActivity.onCreate.1
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public final void onItemClick(int i) {
                ItemView authtype = (ItemView) NewAPNActivity.this._$_findCachedViewById(R.id.authtype);
                Intrinsics.checkExpressionValueIsNotNull(authtype, "authtype");
                authtype.setRightText(NewAPNActivity.access$getAuthTypeMode$p(NewAPNActivity.this)[i]);
                NewAPNActivity.this.currentAuthType = i;
                NewAPNActivity.access$getBean$p(NewAPNActivity.this).setAuthType(i);
            }
        });
        String string2 = getString(R.string.protocol);
        String[] strArr2 = this.protocolMode;
        if (strArr2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("protocolMode");
        }
        this.protocolSelectDialogFragment = new SelectorDialogFragment(string2, (String[]) Arrays.copyOf(strArr2, strArr2.length));
        SelectorDialogFragment selectorDialogFragment2 = this.protocolSelectDialogFragment;
        if (selectorDialogFragment2 == null) {
            Intrinsics.throwNpe();
        }
        selectorDialogFragment2.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: kt.NewAPNActivity.onCreate.2
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public final void onItemClick(int i) {
                ItemView protocol = (ItemView) NewAPNActivity.this._$_findCachedViewById(R.id.protocol);
                Intrinsics.checkExpressionValueIsNotNull(protocol, "protocol");
                protocol.setRightText(NewAPNActivity.access$getProtocolMode$p(NewAPNActivity.this)[i]);
                NewAPNActivity.this.currentProtocol = i;
                NewAPNActivity.access$getBean$p(NewAPNActivity.this).setProtocol(i);
            }
        });
        if (getIntent().getSerializableExtra("bean") == null) {
            this.bean = new APNBean.bean();
            this.position = -1;
        } else {
            Serializable serializableExtra = getIntent().getSerializableExtra("bean");
            if (serializableExtra == null) {
                throw new TypeCastException("null cannot be cast to non-null type bean.APNBean.bean");
            }
            this.bean = (APNBean.bean) serializableExtra;
            this.position = getIntent().getIntExtra(RequestParameters.POSITION, -1);
        }
        APNBean.bean beanVar = this.bean;
        if (beanVar == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bean");
        }
        this.currentAuthType = beanVar.getAuthType();
        ItemView authtype = (ItemView) _$_findCachedViewById(R.id.authtype);
        Intrinsics.checkExpressionValueIsNotNull(authtype, "authtype");
        String[] strArr3 = this.authTypeMode;
        if (strArr3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("authTypeMode");
        }
        authtype.setRightText(strArr3[this.currentAuthType]);
        APNBean.bean beanVar2 = this.bean;
        if (beanVar2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bean");
        }
        this.currentProtocol = beanVar2.getProtocol();
        ItemView protocol = (ItemView) _$_findCachedViewById(R.id.protocol);
        Intrinsics.checkExpressionValueIsNotNull(protocol, "protocol");
        String[] strArr4 = this.protocolMode;
        if (strArr4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("protocolMode");
        }
        protocol.setRightText(strArr4[this.currentProtocol]);
        String stringExtra = getIntent().getStringExtra("iotId");
        if (stringExtra == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        this.iotId = stringExtra;
        this.isEdit = getIntent().getBooleanExtra("isEdit", false);
        if (this.isEdit) {
            Button bt_delete = (Button) _$_findCachedViewById(R.id.bt_delete);
            Intrinsics.checkExpressionValueIsNotNull(bt_delete, "bt_delete");
            bt_delete.setVisibility(0);
        } else {
            Button bt_delete2 = (Button) _$_findCachedViewById(R.id.bt_delete);
            Intrinsics.checkExpressionValueIsNotNull(bt_delete2, "bt_delete");
            bt_delete2.setVisibility(8);
        }
        ItemView name = (ItemView) _$_findCachedViewById(R.id.name);
        Intrinsics.checkExpressionValueIsNotNull(name, "name");
        APNBean.bean beanVar3 = this.bean;
        if (beanVar3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bean");
        }
        name.setRightText(beanVar3.getCarrier());
        ItemView apn = (ItemView) _$_findCachedViewById(R.id.apn);
        Intrinsics.checkExpressionValueIsNotNull(apn, "apn");
        APNBean.bean beanVar4 = this.bean;
        if (beanVar4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bean");
        }
        apn.setRightText(beanVar4.getAPN());
        ItemView username = (ItemView) _$_findCachedViewById(R.id.username);
        Intrinsics.checkExpressionValueIsNotNull(username, "username");
        APNBean.bean beanVar5 = this.bean;
        if (beanVar5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bean");
        }
        username.setRightText(beanVar5.getUser());
        try {
            Object objFromJson = new Gson().fromJson(SharePreferenceManager.getInstance().getAPNConfig(this.iotId), (Class<Object>) APNBean.class);
            Intrinsics.checkExpressionValueIsNotNull(objFromJson, "Gson().fromJson(\n       …:class.java\n            )");
            this.apnBean = (APNBean) objFromJson;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.apnBean.getList() != null) {
            this.list = this.apnBean.getList();
            if (this.position != -1) {
                List<? extends APNBean.bean> list = this.list;
                if (list == null) {
                    Intrinsics.throwNpe();
                }
                List<? extends APNBean.bean> list2 = list;
                List<? extends APNBean.bean> list3 = this.list;
                if (list3 == null) {
                    Intrinsics.throwNpe();
                }
                this.list = CollectionsKt.minus(list2, list3.get(this.position));
            }
        }
        ((TitleView) _$_findCachedViewById(R.id.titleView)).setOnViewClick(new TitleView.OnViewClick() { // from class: kt.NewAPNActivity.onCreate.3
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(@Nullable View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(@Nullable View view2) {
                NewAPNActivity.this.finish();
            }
        });
        ((Button) _$_findCachedViewById(R.id.bt_save_settings)).setOnClickListener(new View.OnClickListener() { // from class: kt.NewAPNActivity.onCreate.4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ItemView name2 = (ItemView) NewAPNActivity.this._$_findCachedViewById(R.id.name);
                Intrinsics.checkExpressionValueIsNotNull(name2, "name");
                if (name2.getRightText() != null) {
                    ItemView name3 = (ItemView) NewAPNActivity.this._$_findCachedViewById(R.id.name);
                    Intrinsics.checkExpressionValueIsNotNull(name3, "name");
                    if (name3.getRightText().equals("")) {
                        NewAPNActivity.this.showNullDialog("Name is required");
                        return;
                    }
                    ItemView apn2 = (ItemView) NewAPNActivity.this._$_findCachedViewById(R.id.apn);
                    Intrinsics.checkExpressionValueIsNotNull(apn2, "apn");
                    if (apn2.getRightText() != null) {
                        ItemView apn3 = (ItemView) NewAPNActivity.this._$_findCachedViewById(R.id.apn);
                        Intrinsics.checkExpressionValueIsNotNull(apn3, "apn");
                        if (apn3.getRightText().equals("")) {
                            NewAPNActivity.this.showNullDialog("APN is required");
                            return;
                        } else {
                            NewAPNActivity.this.save();
                            return;
                        }
                    }
                    NewAPNActivity.this.showNullDialog("APN is required");
                    return;
                }
                NewAPNActivity.this.showNullDialog("Name is required");
            }
        });
        ((Button) _$_findCachedViewById(R.id.bt_delete)).setOnClickListener(new View.OnClickListener() { // from class: kt.NewAPNActivity.onCreate.5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                NewAPNActivity.this.delete();
            }
        });
        InputDialogViewAPN<?> inputDialogViewAPNBuild = new InputDialogViewAPN.Builder().build();
        if (inputDialogViewAPNBuild == null) {
            throw new TypeCastException("null cannot be cast to non-null type dialog.InputDialogViewAPN<*>");
        }
        this.inputDialogView = inputDialogViewAPNBuild;
        InputDialogViewAPN<?> inputDialogViewAPN = this.inputDialogView;
        if (inputDialogViewAPN == null) {
            Intrinsics.throwUninitializedPropertyAccessException("inputDialogView");
        }
        inputDialogViewAPN.addOnClickListener(new InputDialogViewAPN.OnClickListener<Object>() { // from class: kt.NewAPNActivity.onCreate.6
            @Override // dialog.InputDialogViewAPN.OnClickListener
            public void onNegativeClick() {
                NewAPNActivity.access$getInputDialogView$p(NewAPNActivity.this).dismiss();
            }

            @Override // dialog.InputDialogViewAPN.OnClickListener
            public void onPositiveClick(@Nullable String modifyContent, @Nullable Object extra) {
                if (Intrinsics.areEqual(extra, "Name")) {
                    NewAPNActivity.access$getBean$p(NewAPNActivity.this).setCarrier(modifyContent);
                    ItemView name2 = (ItemView) NewAPNActivity.this._$_findCachedViewById(R.id.name);
                    Intrinsics.checkExpressionValueIsNotNull(name2, "name");
                    name2.setRightText(modifyContent);
                    return;
                }
                if (Intrinsics.areEqual(extra, "APN")) {
                    NewAPNActivity.access$getBean$p(NewAPNActivity.this).setAPN(modifyContent);
                    ItemView apn2 = (ItemView) NewAPNActivity.this._$_findCachedViewById(R.id.apn);
                    Intrinsics.checkExpressionValueIsNotNull(apn2, "apn");
                    apn2.setRightText(modifyContent);
                    return;
                }
                if (Intrinsics.areEqual(extra, "Server")) {
                    NewAPNActivity.access$getBean$p(NewAPNActivity.this).setServer(modifyContent);
                    ItemView server = (ItemView) NewAPNActivity.this._$_findCachedViewById(R.id.server);
                    Intrinsics.checkExpressionValueIsNotNull(server, "server");
                    server.setRightText(modifyContent);
                    return;
                }
                if (Intrinsics.areEqual(extra, "Port")) {
                    APNBean.bean beanVarAccess$getBean$p = NewAPNActivity.access$getBean$p(NewAPNActivity.this);
                    if (modifyContent == null) {
                        Intrinsics.throwNpe();
                    }
                    beanVarAccess$getBean$p.setPort(Integer.parseInt(modifyContent));
                    ItemView port = (ItemView) NewAPNActivity.this._$_findCachedViewById(R.id.port);
                    Intrinsics.checkExpressionValueIsNotNull(port, "port");
                    port.setRightText(modifyContent);
                    return;
                }
                if (Intrinsics.areEqual(extra, "MCC")) {
                    NewAPNActivity.access$getBean$p(NewAPNActivity.this).setMCC(modifyContent);
                    ItemView mcc = (ItemView) NewAPNActivity.this._$_findCachedViewById(R.id.mcc);
                    Intrinsics.checkExpressionValueIsNotNull(mcc, "mcc");
                    mcc.setRightText(modifyContent);
                    return;
                }
                if (Intrinsics.areEqual(extra, "MNC")) {
                    NewAPNActivity.access$getBean$p(NewAPNActivity.this).setMNC(modifyContent);
                    ItemView mnc = (ItemView) NewAPNActivity.this._$_findCachedViewById(R.id.mnc);
                    Intrinsics.checkExpressionValueIsNotNull(mnc, "mnc");
                    mnc.setRightText(modifyContent);
                    return;
                }
                if (Intrinsics.areEqual(extra, "Proxy")) {
                    NewAPNActivity.access$getBean$p(NewAPNActivity.this).setProxy(modifyContent);
                    ItemView proxy = (ItemView) NewAPNActivity.this._$_findCachedViewById(R.id.proxy);
                    Intrinsics.checkExpressionValueIsNotNull(proxy, "proxy");
                    proxy.setRightText(modifyContent);
                    return;
                }
                if (Intrinsics.areEqual(extra, "Username")) {
                    NewAPNActivity.access$getBean$p(NewAPNActivity.this).setUser(modifyContent);
                    ItemView username2 = (ItemView) NewAPNActivity.this._$_findCachedViewById(R.id.username);
                    Intrinsics.checkExpressionValueIsNotNull(username2, "username");
                    username2.setRightText(modifyContent);
                    return;
                }
                if (Intrinsics.areEqual(extra, "Password")) {
                    NewAPNActivity.access$getBean$p(NewAPNActivity.this).setPassword(modifyContent);
                    ItemView password = (ItemView) NewAPNActivity.this._$_findCachedViewById(R.id.password);
                    Intrinsics.checkExpressionValueIsNotNull(password, "password");
                    password.setRightText(modifyContent);
                    return;
                }
                if (Intrinsics.areEqual(extra, "AuthType")) {
                    return;
                }
                if (Intrinsics.areEqual(extra, "Type")) {
                    NewAPNActivity.access$getBean$p(NewAPNActivity.this).setType(modifyContent);
                    ItemView type = (ItemView) NewAPNActivity.this._$_findCachedViewById(R.id.type);
                    Intrinsics.checkExpressionValueIsNotNull(type, "type");
                    type.setRightText(modifyContent);
                    return;
                }
                Intrinsics.areEqual(extra, "Protocol");
            }
        });
        NewAPNActivity newAPNActivity = this;
        ((ItemView) _$_findCachedViewById(R.id.name)).setOnClickListener(newAPNActivity);
        ((ItemView) _$_findCachedViewById(R.id.apn)).setOnClickListener(newAPNActivity);
        ((ItemView) _$_findCachedViewById(R.id.server)).setOnClickListener(newAPNActivity);
        ((ItemView) _$_findCachedViewById(R.id.port)).setOnClickListener(newAPNActivity);
        ((ItemView) _$_findCachedViewById(R.id.mcc)).setOnClickListener(newAPNActivity);
        ((ItemView) _$_findCachedViewById(R.id.mnc)).setOnClickListener(newAPNActivity);
        ((ItemView) _$_findCachedViewById(R.id.proxy)).setOnClickListener(newAPNActivity);
        ((ItemView) _$_findCachedViewById(R.id.username)).setOnClickListener(newAPNActivity);
        ((ItemView) _$_findCachedViewById(R.id.password)).setOnClickListener(newAPNActivity);
        ((ItemView) _$_findCachedViewById(R.id.authtype)).setOnClickListener(newAPNActivity);
        ((ItemView) _$_findCachedViewById(R.id.type)).setOnClickListener(newAPNActivity);
        ((ItemView) _$_findCachedViewById(R.id.protocol)).setOnClickListener(newAPNActivity);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(@Nullable View v) {
        Integer numValueOf = v != null ? Integer.valueOf(v.getId()) : null;
        if (numValueOf != null && numValueOf.intValue() == R.id.name) {
            APNBean.bean beanVar = this.bean;
            if (beanVar == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bean");
            }
            if (beanVar.getCarrier() == null) {
                showDialog("Name");
                return;
            }
            APNBean.bean beanVar2 = this.bean;
            if (beanVar2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bean");
            }
            String carrier = beanVar2.getCarrier();
            Intrinsics.checkExpressionValueIsNotNull(carrier, "bean.carrier");
            showDialog("Name", carrier);
            return;
        }
        if (numValueOf != null && numValueOf.intValue() == R.id.apn) {
            APNBean.bean beanVar3 = this.bean;
            if (beanVar3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bean");
            }
            if (beanVar3.getAPN() == null) {
                showDialog("APN");
                return;
            }
            APNBean.bean beanVar4 = this.bean;
            if (beanVar4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bean");
            }
            String apn = beanVar4.getAPN();
            Intrinsics.checkExpressionValueIsNotNull(apn, "bean.apn");
            showDialog("APN", apn);
            return;
        }
        if (numValueOf != null && numValueOf.intValue() == R.id.server) {
            APNBean.bean beanVar5 = this.bean;
            if (beanVar5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bean");
            }
            if (beanVar5.getServer() == null) {
                showDialog("Server");
                return;
            }
            APNBean.bean beanVar6 = this.bean;
            if (beanVar6 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bean");
            }
            String server = beanVar6.getServer();
            Intrinsics.checkExpressionValueIsNotNull(server, "bean.server");
            showDialog("APN", server);
            return;
        }
        if (numValueOf != null && numValueOf.intValue() == R.id.port) {
            APNBean.bean beanVar7 = this.bean;
            if (beanVar7 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bean");
            }
            showDialog("Port", beanVar7.getPort());
            return;
        }
        if (numValueOf != null && numValueOf.intValue() == R.id.mcc) {
            APNBean.bean beanVar8 = this.bean;
            if (beanVar8 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bean");
            }
            if (beanVar8.getMCC() == null) {
                showDialog("MCC");
                return;
            }
            APNBean.bean beanVar9 = this.bean;
            if (beanVar9 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bean");
            }
            String mcc = beanVar9.getMCC();
            Intrinsics.checkExpressionValueIsNotNull(mcc, "bean.mcc");
            showDialog("MCC", mcc);
            return;
        }
        if (numValueOf != null && numValueOf.intValue() == R.id.mnc) {
            APNBean.bean beanVar10 = this.bean;
            if (beanVar10 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bean");
            }
            if (beanVar10.getMNC() == null) {
                showDialog("MNC");
                return;
            }
            APNBean.bean beanVar11 = this.bean;
            if (beanVar11 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bean");
            }
            String mnc = beanVar11.getMNC();
            Intrinsics.checkExpressionValueIsNotNull(mnc, "bean.mnc");
            showDialog("MNC", mnc);
            return;
        }
        if (numValueOf != null && numValueOf.intValue() == R.id.proxy) {
            APNBean.bean beanVar12 = this.bean;
            if (beanVar12 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bean");
            }
            if (beanVar12.getProxy() == null) {
                showDialog("Proxy");
                return;
            }
            APNBean.bean beanVar13 = this.bean;
            if (beanVar13 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bean");
            }
            String proxy = beanVar13.getProxy();
            Intrinsics.checkExpressionValueIsNotNull(proxy, "bean.proxy");
            showDialog("Proxy", proxy);
            return;
        }
        if (numValueOf != null && numValueOf.intValue() == R.id.username) {
            APNBean.bean beanVar14 = this.bean;
            if (beanVar14 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bean");
            }
            if (beanVar14.getUser() == null) {
                showDialog("Username");
                return;
            }
            APNBean.bean beanVar15 = this.bean;
            if (beanVar15 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bean");
            }
            String user = beanVar15.getUser();
            Intrinsics.checkExpressionValueIsNotNull(user, "bean.user");
            showDialog("Username", user);
            return;
        }
        if (numValueOf != null && numValueOf.intValue() == R.id.password) {
            APNBean.bean beanVar16 = this.bean;
            if (beanVar16 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bean");
            }
            if (beanVar16.getPassword() == null) {
                showDialog("Password");
                return;
            }
            APNBean.bean beanVar17 = this.bean;
            if (beanVar17 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bean");
            }
            String password = beanVar17.getPassword();
            Intrinsics.checkExpressionValueIsNotNull(password, "bean.password");
            showDialog("Password", password);
            return;
        }
        if (numValueOf != null && numValueOf.intValue() == R.id.authtype) {
            SelectorDialogFragment selectorDialogFragment = this.authTypeSelectDialogFragment;
            if (selectorDialogFragment == null) {
                Intrinsics.throwNpe();
            }
            selectorDialogFragment.showAllowingStateLoss(getSupportFragmentManager(), "", this.currentAuthType);
            return;
        }
        if (numValueOf != null && numValueOf.intValue() == R.id.type) {
            APNBean.bean beanVar18 = this.bean;
            if (beanVar18 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bean");
            }
            if (beanVar18.getType() == null) {
                showDialog("Type");
                return;
            }
            APNBean.bean beanVar19 = this.bean;
            if (beanVar19 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bean");
            }
            String type = beanVar19.getType();
            Intrinsics.checkExpressionValueIsNotNull(type, "bean.type");
            showDialog("Type", type);
            return;
        }
        if (numValueOf != null && numValueOf.intValue() == R.id.protocol) {
            SelectorDialogFragment selectorDialogFragment2 = this.protocolSelectDialogFragment;
            if (selectorDialogFragment2 == null) {
                Intrinsics.throwNpe();
            }
            selectorDialogFragment2.showAllowingStateLoss(getSupportFragmentManager(), "", this.currentProtocol);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void save() {
        List<? extends APNBean.bean> listPlus;
        List<? extends APNBean.bean> list = this.list;
        if (list != null) {
            List<? extends APNBean.bean> list2 = list;
            APNBean.bean beanVar = this.bean;
            if (beanVar == null) {
                Intrinsics.throwUninitializedPropertyAccessException("bean");
            }
            listPlus = CollectionsKt.plus((Collection<? extends APNBean.bean>) list2, beanVar);
        } else {
            listPlus = null;
        }
        this.list = listPlus;
        this.apnBean.setList(this.list);
        setAPNConfig(this.apnBean, new C06321());
    }

    /* JADX INFO: renamed from: kt.NewAPNActivity$save$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: NewAPNActivity.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0017¨\u0006\u0006"}, d2 = {"kt/NewAPNActivity$save$1", "Lkt/NewAPNActivity$APN;", "finish", "", "b", "", "secueye_googleRelease"}, k = 1, mv = {1, 1, 15})
    public static final class C06321 implements APN {
        C06321() {
        }

        @Override // kt.NewAPNActivity.APN
        @SuppressLint({"ShowToast"})
        public void finish(boolean b2) {
            if (b2) {
                SharePreferenceManager.getInstance().setAPNConfig(NewAPNActivity.this.iotId, new Gson().toJson(NewAPNActivity.this.apnBean));
                NewAPNActivity.this.runOnUiThread(new Runnable() { // from class: kt.NewAPNActivity$save$1$finish$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        Toast.makeText(NewAPNActivity.this, R.string.mofify_succeed, 0).show();
                    }
                });
                NewAPNActivity.this.finish();
                return;
            }
            NewAPNActivity.this.runOnUiThread(new Runnable() { // from class: kt.NewAPNActivity$save$1$finish$2
                @Override // java.lang.Runnable
                public final void run() {
                    Toast.makeText(NewAPNActivity.this, R.string.mofify_failed, 0).show();
                }
            });
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void delete() {
        this.apnBean.setList(this.list);
        setAPNConfig(this.apnBean, new AnonymousClass1());
    }

    /* JADX INFO: renamed from: kt.NewAPNActivity$delete$1, reason: invalid class name */
    /* JADX INFO: compiled from: NewAPNActivity.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0017¨\u0006\u0006"}, d2 = {"kt/NewAPNActivity$delete$1", "Lkt/NewAPNActivity$APN;", "finish", "", "b", "", "secueye_googleRelease"}, k = 1, mv = {1, 1, 15})
    public static final class AnonymousClass1 implements APN {
        AnonymousClass1() {
        }

        @Override // kt.NewAPNActivity.APN
        @SuppressLint({"ShowToast"})
        public void finish(boolean b2) {
            if (b2) {
                SharePreferenceManager.getInstance().setAPNConfig(NewAPNActivity.this.iotId, new Gson().toJson(NewAPNActivity.this.apnBean));
                NewAPNActivity.this.runOnUiThread(new Runnable() { // from class: kt.NewAPNActivity$delete$1$finish$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        Toast.makeText(NewAPNActivity.this, R.string.mofify_succeed, 0).show();
                    }
                });
                NewAPNActivity.this.finish();
                return;
            }
            NewAPNActivity.this.runOnUiThread(new Runnable() { // from class: kt.NewAPNActivity$delete$1$finish$2
                @Override // java.lang.Runnable
                public final void run() {
                    Toast.makeText(NewAPNActivity.this, R.string.mofify_failed, 0).show();
                }
            });
        }
    }

    public final void showDialog(@NotNull String string) {
        Intrinsics.checkParameterIsNotNull(string, "string");
        InputDialogViewAPN<?> inputDialogViewAPN = this.inputDialogView;
        if (inputDialogViewAPN == null) {
            Intrinsics.throwUninitializedPropertyAccessException("inputDialogView");
        }
        inputDialogViewAPN.setTitle(string);
        InputDialogViewAPN<?> inputDialogViewAPN2 = this.inputDialogView;
        if (inputDialogViewAPN2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("inputDialogView");
        }
        inputDialogViewAPN2.setExtra(string);
        InputDialogViewAPN<?> inputDialogViewAPN3 = this.inputDialogView;
        if (inputDialogViewAPN3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("inputDialogView");
        }
        inputDialogViewAPN3.setContent((String) null);
        InputDialogViewAPN<?> inputDialogViewAPN4 = this.inputDialogView;
        if (inputDialogViewAPN4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("inputDialogView");
        }
        inputDialogViewAPN4.show(getSupportFragmentManager(), this.TAG);
    }

    public final void showDialog(@NotNull String string, @NotNull String value) {
        Intrinsics.checkParameterIsNotNull(string, "string");
        Intrinsics.checkParameterIsNotNull(value, "value");
        InputDialogViewAPN<?> inputDialogViewAPN = this.inputDialogView;
        if (inputDialogViewAPN == null) {
            Intrinsics.throwUninitializedPropertyAccessException("inputDialogView");
        }
        inputDialogViewAPN.setTitle(string);
        InputDialogViewAPN<?> inputDialogViewAPN2 = this.inputDialogView;
        if (inputDialogViewAPN2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("inputDialogView");
        }
        inputDialogViewAPN2.setExtra(string);
        InputDialogViewAPN<?> inputDialogViewAPN3 = this.inputDialogView;
        if (inputDialogViewAPN3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("inputDialogView");
        }
        inputDialogViewAPN3.setContent(value);
        InputDialogViewAPN<?> inputDialogViewAPN4 = this.inputDialogView;
        if (inputDialogViewAPN4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("inputDialogView");
        }
        inputDialogViewAPN4.show(getSupportFragmentManager(), this.TAG);
    }

    public final void showDialog(@NotNull String string, int value) {
        Intrinsics.checkParameterIsNotNull(string, "string");
        InputDialogViewAPN<?> inputDialogViewAPN = this.inputDialogView;
        if (inputDialogViewAPN == null) {
            Intrinsics.throwUninitializedPropertyAccessException("inputDialogView");
        }
        inputDialogViewAPN.setTitle(string);
        InputDialogViewAPN<?> inputDialogViewAPN2 = this.inputDialogView;
        if (inputDialogViewAPN2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("inputDialogView");
        }
        inputDialogViewAPN2.setExtra(string);
        InputDialogViewAPN<?> inputDialogViewAPN3 = this.inputDialogView;
        if (inputDialogViewAPN3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("inputDialogView");
        }
        inputDialogViewAPN3.setContent(String.valueOf(value));
        InputDialogViewAPN<?> inputDialogViewAPN4 = this.inputDialogView;
        if (inputDialogViewAPN4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("inputDialogView");
        }
        inputDialogViewAPN4.show(getSupportFragmentManager(), this.TAG);
    }

    private final void setAPNConfig(APNBean i, final APN call) {
        HashMap map = new HashMap();
        List<APNBean.bean> list = i.getList();
        Intrinsics.checkExpressionValueIsNotNull(list, "i.list");
        map.put(Constants.APNConfig, list);
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: kt.NewAPNActivity.setAPNConfig.1
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public final void onComplete(boolean z, @Nullable Object obj) {
                call.finish(z);
            }
        });
    }

    public final void showNullDialog(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "str");
        NewAPNActivity newAPNActivity = this;
        View viewInflate = View.inflate(newAPNActivity, R.layout.dialog_check, null);
        Intrinsics.checkExpressionValueIsNotNull(viewInflate, "View.inflate(this, R.layout.dialog_check, null)");
        AlertDialog.Builder view2 = new AlertDialog.Builder(newAPNActivity).setView(viewInflate);
        Intrinsics.checkExpressionValueIsNotNull(view2, "AlertDialog.Builder(this).setView(view)");
        final AlertDialog alertDialogCreate = view2.create();
        Intrinsics.checkExpressionValueIsNotNull(alertDialogCreate, "builder.create()");
        alertDialogCreate.setCanceledOnTouchOutside(true);
        Window window = alertDialogCreate.getWindow();
        if (window == null) {
            Intrinsics.throwNpe();
        }
        window.setBackgroundDrawableResource(android.R.color.transparent);
        Window window2 = alertDialogCreate.getWindow();
        if (window2 == null) {
            Intrinsics.throwNpe();
        }
        window2.setGravity(80);
        alertDialogCreate.show();
        TextView content = (TextView) viewInflate.findViewById(R.id.tv_content);
        Intrinsics.checkExpressionValueIsNotNull(content, "content");
        content.setText(str);
        ((Button) viewInflate.findViewById(R.id.btn)).setOnClickListener(new View.OnClickListener() { // from class: kt.NewAPNActivity.showNullDialog.1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                alertDialogCreate.dismiss();
            }
        });
    }
}
