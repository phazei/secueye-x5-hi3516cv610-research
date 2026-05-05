package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import bean.DeviceInfoBean;
import com.bumptech.glide.Glide;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes5.dex */
public class DevicePanelView extends RelativeLayout {
    ImageView mImageView;
    TextView mNameTv;
    TextView mStatusTv;
    TextView mTypeTv;

    public DevicePanelView(Context context) {
        this(context, null);
    }

    public DevicePanelView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.device_panel_layout, this);
        this.mNameTv = (TextView) findViewById(R.id.device_panel_name);
        this.mTypeTv = (TextView) findViewById(R.id.device_panel_type);
        this.mStatusTv = (TextView) findViewById(R.id.device_panel_status);
        this.mImageView = (ImageView) findViewById(R.id.iv_image);
    }

    public void setName(String str) {
        this.mNameTv.setText(str);
    }

    public void setType(String str) {
        this.mTypeTv.setText(str);
    }

    public void setStatus(String str) {
        this.mStatusTv.setText(str);
    }

    @Override // android.view.View
    public void setAlpha(float f) {
        super.setAlpha(f);
        this.mNameTv.setAlpha(f);
        this.mTypeTv.setAlpha(f);
        this.mStatusTv.setAlpha(f);
    }

    public void setDeviceInfo(DeviceInfoBean deviceInfoBean) {
        setName(deviceInfoBean.getDeviceName());
        if ("VIRTUAL".equalsIgnoreCase(deviceInfoBean.getThingType()) || "VIRTUAL_SHADOW".equalsIgnoreCase(deviceInfoBean.getThingType())) {
            setType("虚拟");
        } else {
            setType(deviceInfoBean.getNetType());
        }
        String str = "离线";
        if (1 == deviceInfoBean.getReceiverStatus()) {
            str = "在线";
        } else {
            setAlpha(0.8f);
        }
        setStatus(str);
        Glide.with(getContext()).load(deviceInfoBean.getCategoryImage()).into(this.mImageView);
    }
}
