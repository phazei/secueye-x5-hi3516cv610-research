package activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.databinding.DataBindingUtil;
import com.alibaba.cloudapi.sdk.constant.HttpConstant;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.huawei.hms.framework.common.ContainerUtils;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivitySimInputBinding;
import com.taobao.accs.common.Constants;
import fragment.MyAccountTabFragment;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import tools.OnMultiClickListener;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class SimInputActivity extends CommonActivity {
    private ActivitySimInputBinding binding;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_sim_input;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivitySimInputBinding) DataBindingUtil.setContentView(this, R.layout.activity_sim_input);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.SimInputActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                SimInputActivity.this.finish();
            }
        });
        this.binding.ivScan.setOnClickListener(new OnMultiClickListener() { // from class: activity.SimInputActivity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(SimInputActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                intentIntegrator.setPrompt("" + SimInputActivity.this.getString(R.string.scan_barcode));
                intentIntegrator.initiateScan();
            }
        });
        this.binding.btSave.setOnClickListener(new OnMultiClickListener() { // from class: activity.SimInputActivity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (!SimInputActivity.this.binding.editSearch.getText().toString().isEmpty()) {
                    if (SimInputActivity.this.binding.editSearch.getText().length() == 15 || SimInputActivity.this.binding.editSearch.getText().length() == 16) {
                        String string = SimInputActivity.this.binding.editSearch.getText().toString();
                        Intent intent = new Intent(SimInputActivity.this, (Class<?>) SIMWebActivity.class);
                        intent.putExtra(Constants.KEY_IMEI, string);
                        SimInputActivity.this.startActivity(intent);
                        return;
                    }
                    if (SimInputActivity.this.binding.editSearch.getText().length() == 19 || SimInputActivity.this.binding.editSearch.getText().length() == 20) {
                        String string2 = SimInputActivity.this.binding.editSearch.getText().toString();
                        Intent intent2 = new Intent(SimInputActivity.this, (Class<?>) SIMWebActivity.class);
                        intent2.putExtra("iccid", string2);
                        SimInputActivity.this.startActivity(intent2);
                        return;
                    }
                    SimInputActivity.this.showToast("" + SimInputActivity.this.getString(R.string.search_sim_tips));
                    return;
                }
                SimInputActivity.this.showToast("" + SimInputActivity.this.getString(R.string.input));
            }
        });
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 0 && i2 == -1) {
            String stringExtra = intent.getStringExtra(Intents.Scan.RESULT);
            Toast.makeText(this, "扫描结果: " + stringExtra, 1).show();
            if (stringExtra.length() == 15 || stringExtra.length() == 16) {
                Intent intent2 = new Intent(this, (Class<?>) SIMWebActivity.class);
                intent2.putExtra(Constants.KEY_IMEI, stringExtra);
                startActivity(intent2);
                finish();
            } else if (stringExtra.length() == 19 || stringExtra.length() == 20) {
                Intent intent3 = new Intent(this, (Class<?>) SIMWebActivity.class);
                intent3.putExtra("iccid", stringExtra);
                startActivity(intent3);
                finish();
            }
        }
        IntentResult activityResult = IntentIntegrator.parseActivityResult(i, i2, intent);
        if (activityResult != null) {
            if (activityResult.getContents() != null) {
                String formatName = activityResult.getFormatName();
                String contents = activityResult.getContents();
                Log.d("Scan", "类型: " + formatName + "，内容: " + contents);
                if (contents.contains(Constants.KEY_IMEI) || contents.contains("im") || contents.contains("iccid")) {
                    if (contents.contains(Constants.KEY_IMEI) || contents.contains("im")) {
                        if (contents.contains("pk=") && contents.contains("dn=") && contents.contains("im=")) {
                            String strSubstring = null;
                            for (String str : contents.split("&")) {
                                if (str.length() > 3 && str.startsWith("pk=")) {
                                    str.substring(3);
                                }
                                if (str.length() > 3 && str.startsWith("dn=")) {
                                    str.substring(3);
                                }
                                if (str.length() > 3 && str.startsWith("ic=")) {
                                    str.substring(3);
                                }
                                if (str.length() > 3 && str.startsWith("ic2=")) {
                                    str.substring(3);
                                }
                                if (str.length() > 3 && str.startsWith("im=")) {
                                    strSubstring = str.substring(3);
                                }
                            }
                            Intent intent4 = new Intent(this, (Class<?>) SIMWebActivity.class);
                            intent4.putExtra(Constants.KEY_IMEI, strSubstring);
                            Log.d("Scan", "imei: " + strSubstring);
                            startActivity(intent4);
                            finish();
                        } else {
                            String strExtractImei = extractImei(contents);
                            Intent intent5 = new Intent(this, (Class<?>) SIMWebActivity.class);
                            intent5.putExtra(Constants.KEY_IMEI, strExtractImei);
                            Log.d("Scan", "imei: " + strExtractImei);
                            startActivity(intent5);
                            finish();
                        }
                    }
                    if (contents.contains("iccid")) {
                        String strExtractIccid = extractIccid(contents);
                        Intent intent6 = new Intent(this, (Class<?>) SIMWebActivity.class);
                        intent6.putExtra(Constants.KEY_IMEI, strExtractIccid);
                        startActivity(intent6);
                        finish();
                    }
                    if (contents.length() == 15 || contents.length() == 16) {
                        Intent intent7 = new Intent(this, (Class<?>) SIMWebActivity.class);
                        intent7.putExtra(Constants.KEY_IMEI, contents);
                        Log.d("Scan", "imei: " + contents);
                        startActivity(intent7);
                        finish();
                        return;
                    }
                    if (contents.length() == 19 || contents.length() == 20) {
                        Intent intent8 = new Intent(this, (Class<?>) SIMWebActivity.class);
                        intent8.putExtra("iccid", contents);
                        startActivity(intent8);
                        finish();
                        return;
                    }
                    return;
                }
                showToast("" + getString(R.string.qr_code_error_title));
                return;
            }
            return;
        }
        super.onActivityResult(i, i2, intent);
    }

    public static String extractImei(String str) {
        String[] strArrSplit;
        try {
            strArrSplit = str.split("\\?");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (strArrSplit.length < 2) {
            return null;
        }
        for (String str2 : strArrSplit[1].split("&")) {
            String[] strArrSplit2 = str2.split(ContainerUtils.KEY_VALUE_DELIMITER, 2);
            if (strArrSplit2.length == 2 && "im".equals(strArrSplit2[0])) {
                return URLDecoder.decode(strArrSplit2[1], "UTF-8");
            }
            if (strArrSplit2.length == 2 && Constants.KEY_IMEI.equals(strArrSplit2[0])) {
                return URLDecoder.decode(strArrSplit2[1], "UTF-8");
            }
        }
        return null;
    }

    public static String extractIccid(String str) {
        String[] strArrSplit;
        try {
            strArrSplit = str.split("\\?");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (strArrSplit.length < 2) {
            return null;
        }
        for (String str2 : strArrSplit[1].split("&")) {
            String[] strArrSplit2 = str2.split(ContainerUtils.KEY_VALUE_DELIMITER, 2);
            if (strArrSplit2.length == 2 && "ic".equals(strArrSplit2[0])) {
                return URLDecoder.decode(strArrSplit2[1], "UTF-8");
            }
            if (strArrSplit2.length == 2 && "ic2".equals(strArrSplit2[0])) {
                return URLDecoder.decode(strArrSplit2[1], "UTF-8");
            }
        }
        return null;
    }

    private void getList() {
        OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("uid", MyAccountTabFragment.getUserNick());
        } catch (Exception e) {
            e.printStackTrace();
        }
        okHttpClient.newCall(new Request.Builder().url("https://traffic.secueye.app/api/app/get/record").post(RequestBody.create(MediaType.parse(HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON), jSONObject.toString())).build()).enqueue(new Callback() { // from class: activity.SimInputActivity.4
            @Override // okhttp3.Callback
            public void onFailure(Call call, IOException iOException) {
                iOException.printStackTrace();
            }

            @Override // okhttp3.Callback
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.e(Constants.KEY_IMEI, "" + response.body().string());
                }
            }
        });
    }

    private void setImei() {
        OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("id", "100001");
            jSONObject.put("uid", MyAccountTabFragment.getUserNick());
            jSONObject.put("name", "小眯眼");
            jSONObject.put(Constants.KEY_IMEI, "357621098733239");
        } catch (Exception e) {
            e.printStackTrace();
        }
        okHttpClient.newCall(new Request.Builder().url("https://traffic.secueye.app/api/app/update/record").post(RequestBody.create(MediaType.parse(HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON), jSONObject.toString())).build()).enqueue(new Callback() { // from class: activity.SimInputActivity.5
            @Override // okhttp3.Callback
            public void onFailure(Call call, IOException iOException) {
                iOException.printStackTrace();
            }

            @Override // okhttp3.Callback
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.e(Constants.KEY_IMEI, "" + response.body().string());
                }
            }
        });
    }

    private void delImei() {
        OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("id", "100001");
        } catch (Exception e) {
            e.printStackTrace();
        }
        okHttpClient.newCall(new Request.Builder().url("https://traffic.secueye.app/api/app/delete/record").post(RequestBody.create(MediaType.parse(HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON), jSONObject.toString())).build()).enqueue(new Callback() { // from class: activity.SimInputActivity.6
            @Override // okhttp3.Callback
            public void onFailure(Call call, IOException iOException) {
                iOException.printStackTrace();
            }

            @Override // okhttp3.Callback
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.e(Constants.KEY_IMEI, "" + response.body().string());
                }
            }
        });
    }
}
