package fragment;

import activity.AboutAppActivity;
import activity.AuthActivity;
import activity.CountryListActivity;
import activity.FeedbackRecordActivity;
import activity.MainActivity;
import activity.ProfileActivity;
import activity.SettingsActivity;
import activity.TmallServiceActivity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import bean.SwitchLanguageBean;
import com.aliyun.iot.aep.sdk.framework.config.GlobalConfig;
import com.aliyun.iot.aep.sdk.login.ILoginCallback;
import com.aliyun.iot.aep.sdk.login.ILogoutCallback;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.aliyun.iot.aep.sdk.login.data.UserInfo;
import com.seculink.app.R;
import config.AppConfig;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import kt.HelpActivity;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import sdk.DemoApplication;
import tools.ActivityManager;
import tools.DensityUtil;
import tools.ScreenUtils;
import tools.SharePreferenceManager;
import tools.ToastUtils;

/* JADX INFO: loaded from: classes4.dex */
public class MyAccountTabFragment extends CommonFragment implements View.OnClickListener {
    LinearLayout all_setting;
    LinearLayout layout_ad;
    View line;
    View line2;
    LinearLayout llHelp;
    LinearLayout llMeApp;
    LinearLayout llStar;
    LinearLayout llTmall;
    LinearLayout ll_about_app;
    LinearLayout ll_alexa;
    LinearLayout ll_exit;
    LinearLayout ll_feed_back;
    LinearLayout ll_help;
    LinearLayout ll_me_app;
    LinearLayout ll_select_area;
    LinearLayout ll_star;
    LinearLayout ll_tmall;
    ImageButton logout;
    LinearLayout mine;
    TextView myUserNameTV;
    ImageButton my_logout;
    LinearLayout setting;
    TextView tvAreaCurrent;
    private long time = 0;
    private int count = 1;
    private Handler handler = new Handler();

    @Override // fragment.CommonFragment
    protected int getContentLayoutId() {
        return R.layout.myaccounttab_fragment_layout;
    }

    @Override // fragment.CommonFragment
    protected void initWidget(View view2) {
        super.initWidget(view2);
        this.myUserNameTV = (TextView) view2.findViewById(R.id.my_username_textview);
        this.tvAreaCurrent = (TextView) view2.findViewById(R.id.tv_area_current);
        this.llTmall = (LinearLayout) view2.findViewById(R.id.ll_tmall);
        this.line = view2.findViewById(R.id.line);
        this.llHelp = (LinearLayout) view2.findViewById(R.id.ll_help);
        this.setting = (LinearLayout) view2.findViewById(R.id.all_setting);
        this.mine = (LinearLayout) view2.findViewById(R.id.mine);
        this.logout = (ImageButton) view2.findViewById(R.id.my_logout);
        this.llMeApp = (LinearLayout) view2.findViewById(R.id.ll_me_app);
        this.llStar = (LinearLayout) view2.findViewById(R.id.ll_star);
        this.line2 = view2.findViewById(R.id.line2);
        this.ll_about_app = (LinearLayout) view2.findViewById(R.id.ll_about_app);
        this.ll_me_app = (LinearLayout) view2.findViewById(R.id.ll_me_app);
        this.ll_exit = (LinearLayout) view2.findViewById(R.id.ll_exit);
        this.ll_tmall = (LinearLayout) view2.findViewById(R.id.ll_tmall);
        this.ll_select_area = (LinearLayout) view2.findViewById(R.id.ll_select_area);
        this.all_setting = (LinearLayout) view2.findViewById(R.id.all_setting);
        this.ll_help = (LinearLayout) view2.findViewById(R.id.ll_help);
        this.ll_star = (LinearLayout) view2.findViewById(R.id.ll_star);
        this.ll_alexa = (LinearLayout) view2.findViewById(R.id.ll_alexa);
        this.ll_feed_back = (LinearLayout) view2.findViewById(R.id.ll_feed_back);
        this.layout_ad = (LinearLayout) view2.findViewById(R.id.layout_ad);
        this.ll_star.setOnClickListener(this);
        this.ll_me_app.setOnClickListener(this);
        this.logout.setOnClickListener(this);
        this.mine.setOnClickListener(this);
        this.all_setting.setOnClickListener(this);
        this.ll_select_area.setOnClickListener(this);
        this.ll_help.setOnClickListener(this);
        this.ll_tmall.setOnClickListener(this);
        this.ll_exit.setOnClickListener(this);
        this.ll_about_app.setOnClickListener(this);
        this.ll_alexa.setOnClickListener(this);
        this.ll_feed_back.setOnClickListener(this);
        this.layout_ad.setOnClickListener(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.myUserNameTV.setText(getUserNick());
        if (GlobalConfig.getInstance() != null && GlobalConfig.getInstance().getCountry() != null && GlobalConfig.getInstance().getCountry().areaName != null) {
            this.tvAreaCurrent.setText("" + GlobalConfig.getInstance().getCountry().areaName);
        }
        if (AppConfig.isChina) {
            this.llTmall.setVisibility(8);
            this.line.setVisibility(0);
            this.llStar.setVisibility(0);
            this.line2.setVisibility(0);
            this.ll_alexa.setVisibility(8);
            this.ll_feed_back.setVisibility(8);
            return;
        }
        this.ll_alexa.setVisibility(8);
        this.llTmall.setVisibility(8);
        this.line.setVisibility(8);
        this.llStar.setVisibility(8);
        this.line2.setVisibility(8);
        this.ll_feed_back.setVisibility(0);
    }

    @Override // fragment.CommonFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public static String getUserNick() {
        if (!LoginBusiness.isLogin()) {
            return null;
        }
        UserInfo userInfo = LoginBusiness.getUserInfo();
        if (userInfo == null) {
            return "";
        }
        if (!TextUtils.isEmpty(userInfo.userPhone)) {
            return userInfo.userPhone;
        }
        if (!TextUtils.isEmpty(userInfo.userEmail)) {
            return userInfo.userEmail;
        }
        return DemoApplication.getInstance().getString(R.string.username_not_obtained);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void switchLanguage(SwitchLanguageBean switchLanguageBean) {
        if (switchLanguageBean.isShowProgressDialog()) {
            showProgressDialog();
            return;
        }
        TextView textView = this.tvAreaCurrent;
        if (textView != null) {
            textView.setText("" + GlobalConfig.getInstance().getCountry().areaName);
        }
        dismissProgressDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loginOut() {
        showProgressDialog();
        ((DemoApplication) DemoApplication.getInstance()).stopPushAndDisconnect();
        LoginBusiness.logout(new ILogoutCallback() { // from class: fragment.MyAccountTabFragment.1
            @Override // com.aliyun.iot.aep.sdk.login.ILogoutCallback
            public void onLogoutSuccess() {
                SharePreferenceManager.getInstance().setADSwitch(0);
                MyAccountTabFragment.this.dismissProgressDialog();
                Toast.makeText(DemoApplication.getInstance(), DemoApplication.getInstance().getString(R.string.logout_success), 0).show();
                SharePreferenceManager.getInstance().clear();
                ActivityManager.getInstance().clear();
                LoginBusiness.login(new ILoginCallback() { // from class: fragment.MyAccountTabFragment.1.1
                    @Override // com.aliyun.iot.aep.sdk.login.ILoginCallback
                    public void onLoginSuccess() {
                        MyAccountTabFragment.this.handler.postDelayed(new Runnable() { // from class: fragment.MyAccountTabFragment.1.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                ((DemoApplication) DemoApplication.getInstance()).startPushAndConnect();
                                Intent intent = new Intent(DemoApplication.getInstance(), (Class<?>) MainActivity.class);
                                intent.setFlags(268468224);
                                DemoApplication.getInstance().startActivity(intent);
                            }
                        }, 0L);
                    }

                    @Override // com.aliyun.iot.aep.sdk.login.ILoginCallback
                    public void onLoginFailed(int i, String str) {
                        if (i != 10003) {
                            Toast.makeText(DemoApplication.getInstance(), DemoApplication.getInstance().getString(R.string.login_falied_by) + str, 0).show();
                        }
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.login.ILogoutCallback
            public void onLogoutFailed(int i, String str) {
                MyAccountTabFragment.this.dismissProgressDialog();
                Toast.makeText(DemoApplication.getInstance(), DemoApplication.getInstance().getString(R.string.logout_falied_by) + str, 0).show();
                ActivityManager.getInstance().clear();
                LoginBusiness.login(new ILoginCallback() { // from class: fragment.MyAccountTabFragment.1.2
                    @Override // com.aliyun.iot.aep.sdk.login.ILoginCallback
                    public void onLoginSuccess() {
                        MyAccountTabFragment.this.handler.postDelayed(new Runnable() { // from class: fragment.MyAccountTabFragment.1.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                ((DemoApplication) DemoApplication.getInstance()).startPushAndConnect();
                                Intent intent = new Intent(DemoApplication.getInstance(), (Class<?>) MainActivity.class);
                                intent.setFlags(268468224);
                                DemoApplication.getInstance().startActivity(intent);
                            }
                        }, 0L);
                    }

                    @Override // com.aliyun.iot.aep.sdk.login.ILoginCallback
                    public void onLoginFailed(int i2, String str2) {
                        if (i2 != 10003) {
                            Toast.makeText(DemoApplication.getInstance(), DemoApplication.getInstance().getString(R.string.login_falied_by) + str2, 0).show();
                        }
                    }
                });
            }
        });
    }

    public void showDialog() {
        View viewInflate = View.inflate(getContext(), R.layout.qr_view, null);
        ImageButton imageButton = (ImageButton) viewInflate.findViewById(R.id.close_btn);
        Drawable drawable = getResources().getDrawable(R.drawable.et_cancel);
        drawable.setBounds(0, 0, (int) (((double) drawable.getIntrinsicWidth()) * 0.5d), (int) (((double) drawable.getIntrinsicHeight()) * 0.5d));
        imageButton.setBackground(drawable);
        final AlertDialog alertDialogCreate = new AlertDialog.Builder(getContext()).setView(viewInflate).create();
        alertDialogCreate.setCanceledOnTouchOutside(false);
        alertDialogCreate.show();
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(getContext(), 300.0f), -2);
        ((Button) viewInflate.findViewById(R.id.save_btn)).setOnClickListener(new View.OnClickListener() { // from class: fragment.MyAccountTabFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Bitmap bitmapDecodeResource = BitmapFactory.decodeResource(MyAccountTabFragment.this.getResources(), R.drawable.qrcode);
                MyAccountTabFragment.this.saveBitmap(bitmapDecodeResource, System.currentTimeMillis() + ".jpg");
                ToastUtils.showToast(MyAccountTabFragment.this.getContext(), MyAccountTabFragment.this.getString(R.string.save_success));
                alertDialogCreate.dismiss();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: fragment.MyAccountTabFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
    }

    public boolean saveBitmap(Bitmap bitmap, String str) {
        String str2;
        String str3 = Build.BRAND;
        if (str3.equals("xiaomi")) {
            str2 = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + str;
        } else if (str3.equalsIgnoreCase("Huawei")) {
            str2 = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + str;
        } else {
            str2 = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + str;
        }
        if (Build.VERSION.SDK_INT >= 29) {
            saveSignImage(str, bitmap);
            return true;
        }
        Log.v("saveBitmap brand", "" + str3);
        File file = new File(str2);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream)) {
                fileOutputStream.flush();
                fileOutputStream.close();
                if (Build.VERSION.SDK_INT >= 29) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("_data", file.getAbsolutePath());
                    contentValues.put("mime_type", "image/jpeg");
                    getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                } else {
                    MediaStore.Images.Media.insertImage(getContext().getContentResolver(), file.getAbsolutePath(), str, (String) null);
                }
            }
            getContext().sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + str2)));
            return true;
        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", "FileNotFoundException:" + e.getMessage().toString());
            e.printStackTrace();
            return false;
        } catch (IOException e2) {
            Log.e("IOException", "IOException:" + e2.getMessage().toString());
            e2.printStackTrace();
            return false;
        } catch (Exception e3) {
            Log.e("IOException", "IOException:" + e3.getMessage().toString());
            e3.printStackTrace();
            return false;
        }
    }

    public void saveSignImage(String str, Bitmap bitmap) {
        OutputStream outputStreamOpenOutputStream;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("_display_name", str);
            if (Build.VERSION.SDK_INT >= 29) {
                contentValues.put("relative_path", "DCIM/");
            } else {
                contentValues.put("_data", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
            }
            contentValues.put("mime_type", "image/JPEG");
            Uri uriInsert = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            if (uriInsert == null || (outputStreamOpenOutputStream = getContext().getContentResolver().openOutputStream(uriInsert)) == null) {
                return;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStreamOpenOutputStream);
            outputStreamOpenOutputStream.flush();
            outputStreamOpenOutputStream.close();
        } catch (Exception unused) {
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        switch (view2.getId()) {
            case R.id.all_setting /* 2131296386 */:
                startActivity(new Intent(getActivity(), (Class<?>) SettingsActivity.class));
                break;
            case R.id.ll_about_app /* 2131297260 */:
                AboutAppActivity.start(getActivity());
                break;
            case R.id.ll_alexa /* 2131297263 */:
                startActivity(new Intent(getActivity(), (Class<?>) AuthActivity.class));
                break;
            case R.id.ll_exit /* 2131297274 */:
                AlertDialog alertDialogCreate = new AlertDialog.Builder(getContext()).setTitle(R.string.login_out).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() { // from class: fragment.MyAccountTabFragment.5
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyAccountTabFragment.this.loginOut();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: fragment.MyAccountTabFragment.4
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
                alertDialogCreate.show();
                WindowManager.LayoutParams attributes = alertDialogCreate.getWindow().getAttributes();
                attributes.width = (ScreenUtils.getScreenWidth(getContext()) * 9) / 10;
                attributes.gravity = 17;
                alertDialogCreate.getWindow().setAttributes(attributes);
                break;
            case R.id.ll_feed_back /* 2131297275 */:
                startActivity(new Intent(getActivity(), (Class<?>) FeedbackRecordActivity.class));
                break;
            case R.id.ll_help /* 2131297282 */:
                startActivity(new Intent(getActivity(), (Class<?>) HelpActivity.class));
                break;
            case R.id.ll_me_app /* 2131297289 */:
                ProfileActivity.start(getActivity());
                break;
            case R.id.ll_select_area /* 2131297298 */:
                Intent intent = new Intent(getActivity(), (Class<?>) CountryListActivity.class);
                intent.putExtra("needLogin", true);
                intent.putExtra("type", "0");
                startActivity(intent);
                break;
            case R.id.ll_star /* 2131297302 */:
                showDialog();
                break;
            case R.id.ll_tmall /* 2131297305 */:
                TmallServiceActivity.start(getActivity());
                break;
        }
    }
}
