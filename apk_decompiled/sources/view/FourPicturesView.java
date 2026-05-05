package view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import bean.DeviceInfoBean;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iotx.linkvisual.media.video.PlayerException;
import com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener;
import com.aliyun.iotx.linkvisual.media.video.player.LivePlayer;
import com.google.gson.Gson;
import com.seculink.app.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import view.ZoomableTextureView;

/* JADX INFO: loaded from: classes5.dex */
public class FourPicturesView extends ConstraintLayout {

    /* JADX INFO: renamed from: bean, reason: collision with root package name */
    private DeviceInfoBean f8108bean;
    private List<View> borderList;
    private HashMap<Integer, DeviceInfoBean> hashMap;
    private int isSelect;
    LinearLayout layout;
    private List<MyGlTextureView> list;
    private List<LivePlayer> playerList;
    private List<ProgressBar> progressBarList;
    ShowFloat showFloat;

    public interface ShowFloat {
        void tap();
    }

    public FourPicturesView(Context context) {
        super(context);
        this.list = new ArrayList();
        this.playerList = new ArrayList();
        this.progressBarList = new ArrayList();
        this.borderList = new ArrayList();
        this.isSelect = 0;
        this.hashMap = new HashMap<>();
        init(context);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public FourPicturesView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.list = new ArrayList();
        this.playerList = new ArrayList();
        this.progressBarList = new ArrayList();
        this.borderList = new ArrayList();
        this.isSelect = 0;
        this.hashMap = new HashMap<>();
        init(context);
        this.showFloat = (ShowFloat) context;
    }

    private void init(Context context) {
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.four_pictures_view, (ViewGroup) this, true);
        MyGlTextureView myGlTextureView = (MyGlTextureView) viewInflate.findViewById(R.id.player_glsurfaceview1);
        MyGlTextureView myGlTextureView2 = (MyGlTextureView) viewInflate.findViewById(R.id.player2);
        MyGlTextureView myGlTextureView3 = (MyGlTextureView) viewInflate.findViewById(R.id.player_glsurfaceview3);
        MyGlTextureView myGlTextureView4 = (MyGlTextureView) viewInflate.findViewById(R.id.player_glsurfaceview4);
        this.list.add(myGlTextureView);
        this.list.add(myGlTextureView2);
        this.list.add(myGlTextureView3);
        this.list.add(myGlTextureView4);
        LivePlayer livePlayer = new LivePlayer(context.getApplicationContext());
        LivePlayer livePlayer2 = new LivePlayer(context.getApplicationContext());
        LivePlayer livePlayer3 = new LivePlayer(context.getApplicationContext());
        LivePlayer livePlayer4 = new LivePlayer(context.getApplicationContext());
        this.playerList.add(livePlayer);
        this.playerList.add(livePlayer2);
        this.playerList.add(livePlayer3);
        this.playerList.add(livePlayer4);
        ProgressBar progressBar = (ProgressBar) viewInflate.findViewById(R.id.video_buffering_bar1);
        ProgressBar progressBar2 = (ProgressBar) viewInflate.findViewById(R.id.video_buffering_bar2);
        ProgressBar progressBar3 = (ProgressBar) viewInflate.findViewById(R.id.video_buffering_bar3);
        ProgressBar progressBar4 = (ProgressBar) viewInflate.findViewById(R.id.video_buffering_bar4);
        this.progressBarList.add(progressBar);
        this.progressBarList.add(progressBar2);
        this.progressBarList.add(progressBar3);
        this.progressBarList.add(progressBar4);
        View viewFindViewById = viewInflate.findViewById(R.id.border1);
        View viewFindViewById2 = viewInflate.findViewById(R.id.border2);
        View viewFindViewById3 = viewInflate.findViewById(R.id.border3);
        View viewFindViewById4 = viewInflate.findViewById(R.id.border4);
        this.borderList.add(viewFindViewById);
        this.borderList.add(viewFindViewById2);
        this.borderList.add(viewFindViewById3);
        this.borderList.add(viewFindViewById4);
    }

    public void setData(final String[] strArr) {
        for (int i = 0; i < this.list.size(); i++) {
            if (this.playerList.get(i).getPlayState() != 3) {
                this.list.get(i).reset();
            }
            this.playerList.get(i).stop();
            this.playerList.get(i).setTextureView(this.list.get(i));
        }
        for (final int i2 = 0; i2 < this.progressBarList.size(); i2++) {
            this.playerList.get(i2).setOnPlayerStateChangedListener(new OnPlayerStateChangedListener() { // from class: view.FourPicturesView.1
                @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener
                public void onPlayerStateChange(int i3) {
                    switch (i3) {
                        case 2:
                            ((ProgressBar) FourPicturesView.this.progressBarList.get(i2)).setVisibility(0);
                            break;
                        case 3:
                            ((ProgressBar) FourPicturesView.this.progressBarList.get(i2)).setVisibility(8);
                            break;
                    }
                }
            });
        }
        for (final int i3 = 0; i3 < this.progressBarList.size(); i3++) {
            this.playerList.get(i3).setOnErrorListener(new OnErrorListener() { // from class: view.FourPicturesView.2
                @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener
                public void onError(PlayerException playerException) {
                    Log.d("TAG", "onError: ----------" + i3 + "    " + playerException.getMessage());
                }
            });
        }
        for (final int i4 = 0; i4 < this.playerList.size(); i4++) {
            this.playerList.get(i4).setIPCLiveDataSource(strArr[i4], 0, false, 0, true, 0);
            this.playerList.get(i4).setVolume(0.0f);
            this.playerList.get(i4).setOnPreparedListener(new OnPreparedListener() { // from class: view.FourPicturesView.3
                @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener
                public void onPrepared() {
                    ((LivePlayer) FourPicturesView.this.playerList.get(i4)).start();
                }
            });
            this.playerList.get(i4).setReconnectCount(3);
            this.playerList.get(i4).setVideoScalingMode(1);
            this.playerList.get(i4).prepare();
        }
        for (final int i5 = 0; i5 < this.list.size(); i5++) {
            this.list.get(i5).setOnZoomableTextureListener(new ZoomableTextureView.OnZoomableTextureListener() { // from class: view.FourPicturesView.4
                @Override // view.ZoomableTextureView.OnZoomableTextureListener
                public void onLongPress(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
                }

                @Override // view.ZoomableTextureView.OnZoomableTextureListener
                public void onScaleChanged(ZoomableTextureView zoomableTextureView, float f) {
                }

                @Override // view.ZoomableTextureView.OnZoomableTextureListener
                public boolean onDoubleTap(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
                    FourPicturesView.this.hideBorder();
                    FourPicturesView.this.isSelect = i5;
                    ((View) FourPicturesView.this.borderList.get(i5)).setVisibility(0);
                    if (strArr[i5] == null || ((LivePlayer) FourPicturesView.this.playerList.get(i5)).getPlayState() != 3) {
                        return true;
                    }
                    FourPicturesView.this.layout.performClick();
                    return true;
                }

                @Override // view.ZoomableTextureView.OnZoomableTextureListener
                public boolean onSingleTapConfirmed(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
                    FourPicturesView.this.hideBorder();
                    FourPicturesView.this.isSelect = i5;
                    ((View) FourPicturesView.this.borderList.get(i5)).setVisibility(0);
                    FourPicturesView.this.showFloat.tap();
                    return true;
                }
            });
        }
        for (int i6 = 0; i6 < strArr.length; i6++) {
            getDeviceMsg(strArr[i6], i6);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideBorder() {
        for (int i = 0; i < this.borderList.size(); i++) {
            this.borderList.get(i).setVisibility(8);
        }
    }

    public void stop() {
        for (int i = 0; i < this.playerList.size(); i++) {
            this.playerList.get(i).stop();
        }
    }

    public void releasePlayer() {
        for (int i = 0; i < this.playerList.size(); i++) {
            this.playerList.get(i).release();
        }
    }

    public void startPlayer() {
        for (int i = 0; i < this.playerList.size(); i++) {
            this.playerList.get(i).prepare();
        }
    }

    private void getDeviceMsg(String str, final int i) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("uc/getByAccountAndDev").setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: view.FourPicturesView.5
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                Log.d("TAG", "onResponse: ============= " + exc.getMessage());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                Gson gson = new Gson();
                if (ioTResponse.getCode() != 200) {
                    ioTResponse.getLocalizedMsg();
                } else {
                    FourPicturesView.this.f8108bean = (DeviceInfoBean) gson.fromJson(ioTResponse.getData().toString(), DeviceInfoBean.class);
                    FourPicturesView.this.hashMap.put(Integer.valueOf(i), FourPicturesView.this.f8108bean);
                }
                Log.d("TAG", "onResponse: ----------------" + FourPicturesView.this.hashMap.size());
            }
        });
    }

    public int getStatus() {
        return this.playerList.get(this.isSelect).getPlayState();
    }

    public void setDevice(DeviceInfoBean deviceInfoBean) {
        this.f8108bean = deviceInfoBean;
    }

    public DeviceInfoBean getDevice() {
        return this.hashMap.get(Integer.valueOf(this.isSelect));
    }

    public DeviceInfoBean getDevice(int i) {
        return this.hashMap.get(Integer.valueOf(i));
    }

    public void setTwoTap(LinearLayout linearLayout) {
        this.layout = linearLayout;
    }
}
