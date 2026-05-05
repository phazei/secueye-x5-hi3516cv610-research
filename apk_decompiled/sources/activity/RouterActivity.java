package activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.DataBindingUtil;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityAdBinding;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import tools.OnMultiClickListener;
import tools.SharePreferenceManager;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class RouterActivity extends CommonActivity {
    private ActivityAdBinding binding;
    private boolean isOpenAD = false;
    private SwitchCompat swAD;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_ad;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityAdBinding) DataBindingUtil.setContentView(this, R.layout.activity_ad);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.RouterActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                RouterActivity.this.finish();
            }
        });
        this.binding.itemAd.addRightView(new SwitchCompat(getActivity()));
        this.swAD = (SwitchCompat) this.binding.itemAd.getRightView();
        this.swAD.setTextOff("");
        this.swAD.setTextOn("");
        this.swAD.setText("");
        this.swAD.setThumbDrawable(null);
        this.swAD.setBackgroundResource(R.drawable.sel_switch);
        this.isOpenAD = SharePreferenceManager.getInstance().getADSwitch() == 1;
        this.swAD.setChecked(this.isOpenAD);
        this.swAD.setOnClickListener(new OnMultiClickListener() { // from class: activity.RouterActivity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                RouterActivity.this.isOpenAD = !r3.isOpenAD;
                SharePreferenceManager.getInstance().setADSwitch(RouterActivity.this.isOpenAD ? 1 : 2);
            }
        });
        this.binding.btGoMap.setOnClickListener(new OnMultiClickListener() { // from class: activity.RouterActivity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                RouterActivity.this.openGaoDeNavi();
            }
        });
    }

    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
    }

    public static String sendPostRequest(String str, String str2) throws Throwable {
        Throwable th;
        HttpURLConnection httpURLConnection;
        BufferedReader bufferedReader = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            try {
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                dataOutputStream.writeBytes(str2);
                dataOutputStream.flush();
                dataOutputStream.close();
                int responseCode = httpURLConnection.getResponseCode();
                StringBuilder sb = new StringBuilder();
                if (responseCode == 200) {
                    BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    while (true) {
                        try {
                            String line = bufferedReader2.readLine();
                            if (line == null) {
                                break;
                            }
                            sb.append(line);
                        } catch (Throwable th2) {
                            bufferedReader = bufferedReader2;
                            th = th2;
                            if (httpURLConnection != null) {
                                httpURLConnection.disconnect();
                            }
                            if (bufferedReader != null) {
                                bufferedReader.close();
                            }
                            throw th;
                        }
                    }
                    bufferedReader = bufferedReader2;
                }
                String string = sb.toString();
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                return string;
            } catch (Throwable th3) {
                th = th3;
            }
        } catch (Throwable th4) {
            th = th4;
            httpURLConnection = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openGaoDeNavi() {
        StringBuffer stringBuffer = new StringBuffer("androidamap://navi?sourceApplication=");
        stringBuffer.append("yitu8_driver");
        stringBuffer.append("&lat=");
        stringBuffer.append("34.264642646862");
        stringBuffer.append("&lon=");
        stringBuffer.append("108.95108518068");
        stringBuffer.append("&dev=");
        stringBuffer.append(1);
        stringBuffer.append("&style=");
        stringBuffer.append(0);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()));
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setPackage("com.autonavi.minimap");
        startActivity(intent);
    }
}
