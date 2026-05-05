package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import bean.DeviceInfoBean;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iotx.linkvisual.media.LinkVisualMedia;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.seculink.app.R;
import com.xiaomi.mipush.sdk.Constants;
import config.AppConfig;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import tools.BitmapUtil;
import tools.ScreenUtil;
import tools.SpUtil;
import tools.TimeUtil;
import tools.Utils;
import view.AdaptiveImageView;
import view.GlideRoundTransform;

/* JADX INFO: loaded from: classes.dex */
public class GripDeviceAdapter extends BaseQuickAdapter<DeviceInfoBean, com.chad.library.adapter.base.BaseViewHolder> {
    private boolean isChina;
    HashMap<String, String> mHashMap;
    OfflineBtnClick offlineBtnClick;

    public interface OfflineBtnClick {
        void OnClick(String str, String str2, String str3, String str4);
    }

    public GripDeviceAdapter(int i) {
        super(i);
        this.mHashMap = new HashMap<>();
        this.isChina = AppConfig.isChina;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.chad.library.adapter.base.BaseQuickAdapter
    @SuppressLint({"SetTextI18n"})
    public void convert(com.chad.library.adapter.base.BaseViewHolder baseViewHolder, final DeviceInfoBean deviceInfoBean) {
        if (deviceInfoBean.getStatus() == 1) {
            LinkVisualMedia.getInstance().preConnectByIotId(deviceInfoBean.getIotId());
        }
        AdaptiveImageView adaptiveImageView = (AdaptiveImageView) baseViewHolder.getView(R.id.iv_img);
        ImageView imageView = (ImageView) baseViewHolder.getView(R.id.iv_line);
        TextView textView = (TextView) baseViewHolder.getView(R.id.tv_line);
        FrameLayout frameLayout = (FrameLayout) baseViewHolder.getView(R.id.fl_img);
        imageView.setImageResource(deviceInfoBean.getStatus() != 1 ? R.drawable.oval_gray : R.drawable.oval_green);
        textView.setText(deviceInfoBean.getStatus() != 1 ? R.string.offline : R.string.online);
        ((TextView) baseViewHolder.getView(R.id.count)).setText("0" + (baseViewHolder.getAdapterPosition() + 1));
        deviceInfoBean.getOwned();
        RequestOptions requestOptionsTransform = new RequestOptions().transform(new GlideRoundTransform(this.mContext, 4));
        TextUtils.isEmpty(deviceInfoBean.getName());
        if (deviceInfoBean.getReceiverStatus() == -1) {
            baseViewHolder.setGone(R.id.iv_play, false).setGone(R.id.fl_offline, false).setGone(R.id.tv_center_offline, false).setGone(R.id.ll_line, false);
            Glide.with(this.mContext).load(Integer.valueOf(R.mipmap.default_snap2)).apply(requestOptionsTransform).into(adaptiveImageView);
        } else {
            baseViewHolder.setGone(R.id.iv_play, deviceInfoBean.getStatus() == 1).setGone(R.id.fl_offline, deviceInfoBean.getStatus() != 1).setGone(R.id.tv_center_offline, deviceInfoBean.getStatus() != 1).setGone(R.id.ll_line, true);
            StringBuilder sb = new StringBuilder();
            File[] fileArr = new File[4];
            Context context = this.mContext;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(Utils.getDevSnapKey(OpenAccountUIConstants.UNDER_LINE + deviceInfoBean.getIotId()));
            sb2.append(OpenAccountUIConstants.UNDER_LINE);
            List<String> listHasPrefix = SpUtil.hasPrefix(context, sb2.toString());
            if (listHasPrefix.size() <= 0) {
                sb = new StringBuilder(SpUtil.getString(this.mContext, Utils.getDevSnapKey(deviceInfoBean.getIotId()), ""));
            } else {
                for (int i = 0; i < listHasPrefix.size(); i++) {
                    try {
                        fileArr[Integer.parseInt(listHasPrefix.get(i).replace(".png", "").split(Constants.ACCEPT_TIME_SEPARATOR_SERVER)[1])] = new File(listHasPrefix.get(i));
                        sb.append(listHasPrefix.get(i));
                        sb.append(OpenAccountUIConstants.UNDER_LINE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (sb.length() <= 0 || !sb.toString().equals(imageView.getTag())) {
                if (listHasPrefix.size() <= 0) {
                    Glide.with(this.mContext).load(sb.toString()).apply(requestOptionsTransform.error(R.drawable.bg_black_rectangle).dontAnimate()).into(adaptiveImageView);
                } else {
                    Bitmap bitmapCompoundBitmap = BitmapUtil.compoundBitmap(fileArr, 4);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmapCompoundBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    Glide.with(this.mContext).load(byteArrayOutputStream.toByteArray()).apply(requestOptionsTransform.error(R.drawable.bg_black_rectangle).dontAnimate()).into(adaptiveImageView);
                }
            }
            imageView.setTag(sb.toString());
        }
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) frameLayout.getLayoutParams();
        layoutParams.height = ((ScreenUtil.getDisplayMetrics(this.mContext)[0] - (this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_10) * 2)) * 4) / 16;
        frameLayout.setLayoutParams(layoutParams);
        boolean z = this.isChina;
        if (baseViewHolder.itemView.findViewById(R.id.tv_center_offline).getVisibility() == 0) {
            getThingsStatus(deviceInfoBean.getIotId(), (TextView) baseViewHolder.itemView.findViewById(R.id.offline_text));
            baseViewHolder.itemView.findViewById(R.id.offline_btn).setVisibility(0);
        } else {
            baseViewHolder.itemView.findViewById(R.id.offline_text).setVisibility(8);
            baseViewHolder.itemView.findViewById(R.id.offline_btn).setVisibility(8);
        }
        baseViewHolder.itemView.findViewById(R.id.offline_btn).setOnClickListener(new View.OnClickListener() { // from class: adapter.GripDeviceAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (GripDeviceAdapter.this.offlineBtnClick != null) {
                    GripDeviceAdapter.this.offlineBtnClick.OnClick(deviceInfoBean.getNetType(), deviceInfoBean.getIotId(), deviceInfoBean.getDeviceName(), deviceInfoBean.getProductKey());
                }
            }
        });
    }

    private void getThingsStatus(final String str, final TextView textView) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/thing/status/get").setScheme(Scheme.HTTPS).setApiVersion("1.0.4").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: adapter.GripDeviceAdapter.2
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                try {
                    if (((JSONObject) ioTResponse.getData()).get("status").toString().equals("3")) {
                        final String strTimeStamp2Date = TimeUtil.TimeStamp2Date(((JSONObject) ioTResponse.getData()).get("time").toString());
                        if (GripDeviceAdapter.this.mHashMap.get(str) == null) {
                            textView.post(new Runnable() { // from class: adapter.GripDeviceAdapter.2.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    textView.setText(((Object) GripDeviceAdapter.this.getRecyclerView().getResources().getText(R.string.time_of_off_line)) + strTimeStamp2Date);
                                    textView.setVisibility(0);
                                }
                            });
                        }
                        GripDeviceAdapter.this.mHashMap.put(str, strTimeStamp2Date);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        if (this.mHashMap.containsKey(str)) {
            textView.setText(((Object) getRecyclerView().getResources().getText(R.string.time_of_off_line)) + this.mHashMap.get(str));
            textView.setVisibility(0);
        }
    }

    public void setOfflineBtnClick(OfflineBtnClick offlineBtnClick) {
        this.offlineBtnClick = offlineBtnClick;
    }
}
