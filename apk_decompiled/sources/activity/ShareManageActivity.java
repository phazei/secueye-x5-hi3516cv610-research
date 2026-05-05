package activity;

import adapter.ShareAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bean.AreaCodeModel;
import bean.DeviceInfoBean;
import bean.SharePermissionBean;
import bean.SharePersonBean;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.sdk.android.openaccount.ui.util.ToastUtils;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.business.devicecenter.api.share.DeviceShareManager;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageImpl;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.seculink.app.R;
import dialog.BaseDialog;
import dialog.DialogUtil;
import dialog.ShareDialog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tools.LogEx;
import tools.SystemUtil;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class ShareManageActivity extends CommonActivity {
    private DeviceInfoBean device;
    private DeviceInfoBean device1;
    private DeviceInfoBean device2;
    private DeviceInfoBean device3;
    private TitleView fl_titlebar;
    private ImageView imageView;
    ConstraintLayout layout_main;
    private RecyclerView mRecyclerView;
    private TextView noShareText;
    private DeviceInfoBean nvrDevice;
    private ShareAdapter shareAdapter;
    private Button shareButton;
    private ShareDialog shareDialog;
    private SharePermissionBean sharePermissionBean;
    private Handler handler = new Handler();
    private Handler mHandler = new Handler();

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_share_manage;
    }

    @Override // activity.CommonActivity
    protected boolean initArgs(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            this.device1 = (DeviceInfoBean) extras.getSerializable("device1");
            this.device2 = (DeviceInfoBean) extras.getSerializable("device2");
            this.device3 = (DeviceInfoBean) extras.getSerializable("device3");
            this.nvrDevice = (DeviceInfoBean) extras.getSerializable("nvrDevice");
            Log.d("分享", "device: " + this.device.getIotId());
            if (this.device1 != null) {
                Log.d("分享", "device1: " + this.device1.getIotId());
            }
            if (this.device2 != null) {
                Log.d("分享", "device2: " + this.device2.getIotId());
            }
            if (this.nvrDevice != null) {
                Log.d("分享", "nvrDevice: " + this.nvrDevice.getIotId());
            }
        }
        return super.initArgs(intent);
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (ConstraintLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        Drawable drawable = getResources().getDrawable(R.drawable.share_btn);
        drawable.setBounds(0, 0, (int) (((double) drawable.getIntrinsicWidth()) * 0.5d), (int) (((double) drawable.getIntrinsicHeight()) * 0.5d));
        this.shareButton = (Button) findViewById(R.id.share_btn);
        this.shareButton.setCompoundDrawables(drawable, null, null, null);
        this.shareButton.setOnClickListener(new View.OnClickListener() { // from class: activity.ShareManageActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ShareManageActivity.this.shareDialog = ((ShareDialog.Builder) new ShareDialog.Builder().view(R.layout.dialog_input2).leftBtnText(ShareManageActivity.this.getString(R.string.cancel)).rightBtnText(ShareManageActivity.this.getString(R.string.share_immediately)).clickRight(new View.OnClickListener() { // from class: activity.ShareManageActivity.1.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view3) {
                        if (ShareManageActivity.this.shareDialog.getContent() != null) {
                            if (ShareManageActivity.this.shareDialog.getMode() != 0 || SystemUtil.isPhone(ShareManageActivity.this.shareDialog.getContent())) {
                                if (ShareManageActivity.this.shareDialog.getMode() != 1 || SystemUtil.isEmail(ShareManageActivity.this.shareDialog.getContent())) {
                                    DeviceInfoBean deviceInfoBean = (DeviceInfoBean) ShareManageActivity.this.shareDialog.getExtra();
                                    ArrayList arrayList = new ArrayList();
                                    arrayList.add(ShareManageActivity.this.device.getIotId());
                                    if (ShareManageActivity.this.device1 != null) {
                                        arrayList.add(ShareManageActivity.this.device1.getIotId());
                                    }
                                    if (ShareManageActivity.this.device2 != null) {
                                        arrayList.add(ShareManageActivity.this.device2.getIotId());
                                    }
                                    if (ShareManageActivity.this.device3 != null) {
                                        arrayList.add(ShareManageActivity.this.device3.getIotId());
                                    }
                                    if (ShareManageActivity.this.nvrDevice != null) {
                                        arrayList.add(ShareManageActivity.this.nvrDevice.getIotId());
                                    }
                                    Log.d("分享", "" + arrayList.size());
                                    ShareManageActivity.this.shareDevice(ShareManageActivity.this.shareDialog.getContent(), deviceInfoBean, arrayList, ShareManageActivity.this.shareDialog.getMode() == 0 ? ShareManageActivity.this.shareDialog.getDistinct() : null);
                                    return;
                                }
                                ToastUtils.toast(ShareManageActivity.this.getActivity(), ShareManageActivity.this.getString(R.string.email_invalid));
                                return;
                            }
                            ToastUtils.toast(ShareManageActivity.this.getActivity(), ShareManageActivity.this.getString(R.string.phone_invalid));
                            return;
                        }
                        Toast.makeText(ShareManageActivity.this.getActivity(), R.string.share_user_null_error, 0).show();
                    }
                })).create();
                ShareManageActivity.this.shareDialog.setOnShareClick(new ShareDialog.OnShareClickListener() { // from class: activity.ShareManageActivity.1.2
                    @Override // dialog.ShareDialog.OnShareClickListener
                    public void onDistinctChange() {
                    }

                    @Override // dialog.ShareDialog.OnShareClickListener
                    public void onDistinctSelect(AreaCodeModel areaCodeModel) {
                    }

                    @Override // dialog.ShareDialog.OnShareClickListener
                    public void onShareSwitchChange() {
                    }
                });
                ShareManageActivity.this.shareDialog.setExtra(ShareManageActivity.this.device);
                ShareManageActivity.this.shareDialog.show(ShareManageActivity.this.getSupportFragmentManager(), "");
            }
        });
        this.noShareText = (TextView) findViewById(R.id.text_no_share);
        this.imageView = (ImageView) findViewById(R.id.no_share);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        this.fl_titlebar = (TitleView) findViewById(R.id.fl_titlebar);
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.ShareManageActivity.2
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                ShareManageActivity.this.finish();
            }
        });
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.shareAdapter = new ShareAdapter(R.layout.item_share_person, getActivity(), new ShareAdapter.DeleteShareOnClick() { // from class: activity.ShareManageActivity.3
            @Override // adapter.ShareAdapter.DeleteShareOnClick
            public void update(int i) {
                final SharePersonBean sharePersonBean = ShareManageActivity.this.shareAdapter.getData().get(i);
                new BaseDialog.Builder().view(R.layout.dialog_common).content(ShareManageActivity.this.getString(R.string.sure_unbind, new Object[]{sharePersonBean.identityAlias})).leftBtnText(ShareManageActivity.this.getString(R.string.cancel)).rightBtnText(ShareManageActivity.this.getString(R.string.confirm)).clickRight(new View.OnClickListener() { // from class: activity.ShareManageActivity.3.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(ShareManageActivity.this.device.getIotId());
                        if (ShareManageActivity.this.device1 != null) {
                            arrayList.add(ShareManageActivity.this.device1.getIotId());
                        }
                        if (ShareManageActivity.this.device2 != null) {
                            arrayList.add(ShareManageActivity.this.device2.getIotId());
                        }
                        if (ShareManageActivity.this.device3 != null) {
                            arrayList.add(ShareManageActivity.this.device3.getIotId());
                        }
                        if (ShareManageActivity.this.nvrDevice != null) {
                            arrayList.add(ShareManageActivity.this.nvrDevice.getIotId());
                        }
                        Log.d("取消分享", "" + arrayList.size());
                        ShareManageActivity.this.unbindDevice(sharePersonBean.identifyId, arrayList);
                    }
                }).canCancel(false).create().show(ShareManageActivity.this.getSupportFragmentManager(), "");
            }
        });
        this.shareAdapter.bindToRecyclerView(this.mRecyclerView);
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        getSharePersonsList();
    }

    public void unbindDevice(String str, List<String> list) {
        showProgressDialog();
        HashMap map = new HashMap();
        map.put(AlinkConstants.KEY_TARGET_IDENTITY_ID, str);
        map.put("iotIdList", list);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath(AlinkConstants.HTTP_PATH_SHARE_UNBIND).setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.ShareManageActivity.4
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                ALog.d(ShareManageActivity.this.TAG, "onFailure");
                ShareManageActivity.this.handler.post(new Runnable() { // from class: activity.ShareManageActivity.4.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ShareManageActivity.this.showToast(ShareManageActivity.this.getString(R.string.delete_fail));
                        ShareManageActivity.this.dismissProgressDialog();
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getCode() != 200) {
                    ShareManageActivity.this.handler.post(new Runnable() { // from class: activity.ShareManageActivity.4.2
                        @Override // java.lang.Runnable
                        public void run() {
                            ShareManageActivity.this.showToast(ShareManageActivity.this.getString(R.string.delete_fail));
                            ShareManageActivity.this.dismissProgressDialog();
                        }
                    });
                } else {
                    ShareManageActivity.this.handler.post(new Runnable() { // from class: activity.ShareManageActivity.4.3
                        @Override // java.lang.Runnable
                        public void run() {
                            ShareManageActivity.this.showToast(ShareManageActivity.this.getString(R.string.delete_succeed));
                            ShareManageActivity.this.getSharePersonsList();
                        }
                    });
                }
            }
        });
    }

    public void getSharePersonsList() {
        showProgressDialog();
        HashMap map = new HashMap();
        map.put("iotId", this.device.getIotId());
        map.put(AlinkConstants.KEY_PAGE_NO, 1);
        map.put(AlinkConstants.KEY_PAGE_SIZE, 100);
        map.put("owned", 0);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/uc/listBindingByDev").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.ShareManageActivity.5
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                ALog.d(ShareManageActivity.this.TAG, "onFailure");
                ShareManageActivity.this.handler.post(new Runnable() { // from class: activity.ShareManageActivity.5.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ShareManageActivity.this.showToast(ShareManageActivity.this.getString(R.string.get_share_failed));
                        ShareManageActivity.this.dismissProgressDialog();
                    }
                });
                ShareManageActivity.this.dismissProgressDialog();
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getCode() != 200) {
                    ShareManageActivity.this.handler.post(new Runnable() { // from class: activity.ShareManageActivity.5.2
                        @Override // java.lang.Runnable
                        public void run() {
                            ShareManageActivity.this.showToast(ShareManageActivity.this.getString(R.string.get_share_failed));
                            ShareManageActivity.this.dismissProgressDialog();
                        }
                    });
                    return;
                }
                Object data = ioTResponse.getData();
                if (data == null || !(data instanceof JSONObject)) {
                    ShareManageActivity.this.handler.post(new Runnable() { // from class: activity.ShareManageActivity.5.5
                        @Override // java.lang.Runnable
                        public void run() {
                            ShareManageActivity.this.showToast(ShareManageActivity.this.getString(R.string.get_share_failed));
                            ShareManageActivity.this.dismissProgressDialog();
                        }
                    });
                    return;
                }
                try {
                    JSONArray jSONArray = ((JSONObject) data).getJSONArray("data");
                    final ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jSONObject = (JSONObject) jSONArray.get(i);
                        String str = (String) jSONObject.get(IoTCredentialManageImpl.AUTH_IOT_TOKEN_IDENTITY_ID_KEY);
                        String str2 = (String) jSONObject.get("identityAlias");
                        SharePersonBean sharePersonBean = new SharePersonBean();
                        sharePersonBean.identifyId = str;
                        sharePersonBean.identityAlias = str2;
                        ShareManageActivity.this.sharePermissionBean = new SharePermissionBean();
                        sharePersonBean.sharePermissionBean = ShareManageActivity.this.sharePermissionBean;
                        arrayList.add(sharePersonBean);
                    }
                    ShareManageActivity.this.handler.post(new Runnable() { // from class: activity.ShareManageActivity.5.3
                        @Override // java.lang.Runnable
                        public void run() {
                            ShareManageActivity.this.shareAdapter.replaceData(arrayList);
                            ShareManageActivity.this.imageView.setVisibility(8);
                            ShareManageActivity.this.noShareText.setVisibility(8);
                            ShareManageActivity.this.dismissProgressDialog();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    ShareManageActivity.this.handler.post(new Runnable() { // from class: activity.ShareManageActivity.5.4
                        @Override // java.lang.Runnable
                        public void run() {
                            ShareManageActivity.this.shareAdapter.getData().clear();
                            ShareManageActivity.this.shareAdapter.notifyDataSetChanged();
                            ShareManageActivity.this.dismissProgressDialog();
                        }
                    });
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shareDevice(String str, DeviceInfoBean deviceInfoBean, List<String> list, String str2) {
        HashMap map = new HashMap();
        map.put(AlinkConstants.KEY_ACCOUNT_ATTR, str);
        if (TextUtils.isEmpty(str2)) {
            map.put(AlinkConstants.KEY_ACCOUNT_ATTR_TYPE, DeviceShareManager.SHARE_DEVICE_ACCOUNT_ATTRTYPE_EMAIL);
        } else {
            map.put(AlinkConstants.KEY_ACCOUNT_ATTR_TYPE, DeviceShareManager.SHARE_DEVICE_ACCOUNT_ATTRTYPE_MOBILE);
            map.put(AlinkConstants.KEY_MOBILE_LOCATION_CODE, str2);
        }
        map.put("iotIdList", list);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath(AlinkConstants.HTTP_PATH_DEVICE_SHARE).setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass6(deviceInfoBean, str));
    }

    /* JADX INFO: renamed from: activity.ShareManageActivity$6, reason: invalid class name */
    class AnonymousClass6 implements IoTCallback {
        final /* synthetic */ String val$accountAttr;
        final /* synthetic */ DeviceInfoBean val$deviceInfoBean;

        AnonymousClass6(DeviceInfoBean deviceInfoBean, String str) {
            this.val$deviceInfoBean = deviceInfoBean;
            this.val$accountAttr = str;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.d(true, ShareManageActivity.this.TAG, "onFailure");
            Toast.makeText(ShareManageActivity.this.getActivity(), R.string.share_failed, 0).show();
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            final int code = ioTResponse.getCode();
            Log.e(ShareManageActivity.this.TAG, "shareDevice onResponse: code: " + code);
            final String localizedMsg = ioTResponse.getLocalizedMsg();
            if (code != 200) {
                ShareManageActivity.this.mHandler.post(new Runnable() { // from class: activity.ShareManageActivity.6.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Activity activity2 = ShareManageActivity.this.getActivity();
                        if (activity2 == null || activity2.isFinishing()) {
                            return;
                        }
                        if (code == 2077) {
                            ShareManageActivity.this.mHandler.post(new Runnable() { // from class: activity.ShareManageActivity.6.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Activity activity3 = ShareManageActivity.this.getActivity();
                                    if (activity3 == null || activity3.isFinishing()) {
                                        return;
                                    }
                                    DialogUtil.showTipsConfirmDiaLog(ShareManageActivity.this.getActivity(), ShareManageActivity.this.getString(R.string.sharing_failed), ShareManageActivity.this.getString(R.string.sharing_tips_1) + SdkConstant.CLOUDAPI_LF + ShareManageActivity.this.getString(R.string.sharing_tips_2) + SdkConstant.CLOUDAPI_LF + ShareManageActivity.this.getString(R.string.sharing_tips_3) + SdkConstant.CLOUDAPI_LF + ShareManageActivity.this.getString(R.string.sharing_tips_4), ShareManageActivity.this.getString(R.string.i_know));
                                }
                            });
                        } else {
                            Toast.makeText(ShareManageActivity.this.getActivity(), localizedMsg, 0).show();
                        }
                    }
                });
            } else {
                ShareManageActivity.this.mHandler.post(new Runnable() { // from class: activity.ShareManageActivity.6.2
                    @Override // java.lang.Runnable
                    public void run() {
                        Activity activity2 = ShareManageActivity.this.getActivity();
                        if (activity2 == null || activity2.isFinishing()) {
                            return;
                        }
                        Toast.makeText(ShareManageActivity.this.getActivity(), ShareManageActivity.this.getString(R.string.share_succeed, new Object[]{AnonymousClass6.this.val$deviceInfoBean.getName(), AnonymousClass6.this.val$accountAttr}), 0).show();
                    }
                });
            }
        }
    }
}
