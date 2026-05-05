package activity;

import adapter.PhotoAdapter;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.viewpager.widget.ViewPager;
import bean.RefreshPicture;
import com.seculink.app.R;
import dialog.BaseDialog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.greenrobot.eventbus.EventBus;
import tools.FileUtil;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import view.PhotoViewPager;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class LocalPhotoDetailsActivity extends CommonActivity {
    private TextView index;
    private boolean isFromPicture;
    private boolean isMediaStoreDeleteSucceed;
    LinearLayout layout_main;
    private LinearLayout ll_bottom;
    private LinearLayout ll_delete;
    private LinearLayout ll_download;
    private LinearLayout ll_share;
    private PhotoAdapter photoAdapter;
    private ArrayList<String> photos;
    private TitleView tv_title;
    private PhotoViewPager viewPager;
    private int pos = -1;
    private ExecutorService service = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler();

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_photo_view;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.isFromPicture = getIntent().getBooleanExtra("isFromPicture", false);
        this.viewPager = (PhotoViewPager) findViewById(R.id.viewPager);
        this.index = (TextView) findViewById(R.id.tv_index);
        this.ll_share = (LinearLayout) findViewById(R.id.ll_share);
        this.ll_delete = (LinearLayout) findViewById(R.id.ll_delete);
        this.ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        this.ll_download = (LinearLayout) findViewById(R.id.ll_download);
        this.tv_title = (TitleView) findViewById(R.id.tv_title);
        this.tv_title.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.LocalPhotoDetailsActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                LocalPhotoDetailsActivity.this.onBackPressed();
            }
        });
        this.photos = getIntent().getStringArrayListExtra("photos");
        this.pos = getIntent().getIntExtra("pos", 0);
        this.photoAdapter = new PhotoAdapter(this.photos);
        this.viewPager.setAdapter(this.photoAdapter);
        this.viewPager.setCurrentItem(this.pos);
        this.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: activity.LocalPhotoDetailsActivity.2
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                LocalPhotoDetailsActivity.this.resetPhoto();
                LocalPhotoDetailsActivity.this.index.setText((i + 1) + "/" + LocalPhotoDetailsActivity.this.photos.size());
                LocalPhotoDetailsActivity.this.pos = i;
            }
        });
        this.index.setText((this.pos + 1) + "/" + this.photos.size());
        this.ll_share.setOnClickListener(new View.OnClickListener() { // from class: activity.LocalPhotoDetailsActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("image/*");
                intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File((String) LocalPhotoDetailsActivity.this.photos.get(LocalPhotoDetailsActivity.this.pos))));
                LocalPhotoDetailsActivity localPhotoDetailsActivity = LocalPhotoDetailsActivity.this;
                localPhotoDetailsActivity.startActivity(Intent.createChooser(intent, localPhotoDetailsActivity.getResources().getString(R.string.share)));
            }
        });
        this.ll_delete.setOnClickListener(new AnonymousClass4());
        this.ll_download.setOnClickListener(new View.OnClickListener() { // from class: activity.LocalPhotoDetailsActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                LocalPhotoDetailsActivity localPhotoDetailsActivity = LocalPhotoDetailsActivity.this;
                localPhotoDetailsActivity.savePhoto((String) localPhotoDetailsActivity.photos.get(LocalPhotoDetailsActivity.this.pos), LocalPhotoDetailsActivity.this.getIntent().getBooleanExtra("isHttpUrl", false));
            }
        });
        if (!this.isFromPicture) {
            this.ll_delete.setVisibility(8);
            this.ll_share.setVisibility(8);
        } else {
            this.ll_delete.setVisibility(0);
            this.ll_share.setVisibility(0);
        }
    }

    /* JADX INFO: renamed from: activity.LocalPhotoDetailsActivity$4, reason: invalid class name */
    class AnonymousClass4 implements View.OnClickListener {
        AnonymousClass4() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view2) {
            new BaseDialog.Builder().view(R.layout.dialog_delete_camera).content(LocalPhotoDetailsActivity.this.getString(R.string.sure_cancel)).leftBtnText(LocalPhotoDetailsActivity.this.getString(R.string.sure_delete)).rightBtnText(LocalPhotoDetailsActivity.this.getString(R.string.cancel)).clickLeft(new View.OnClickListener() { // from class: activity.LocalPhotoDetailsActivity.4.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view3) {
                    if (FileUtil.delete((String) LocalPhotoDetailsActivity.this.photos.get(LocalPhotoDetailsActivity.this.pos))) {
                        Toast.makeText(LocalPhotoDetailsActivity.this.getActivity(), R.string.delete_success, 1).show();
                        EventBus.getDefault().post(new RefreshPicture());
                        LocalPhotoDetailsActivity.this.photos.remove(LocalPhotoDetailsActivity.this.pos);
                        LocalPhotoDetailsActivity.this.photoAdapter.notifyDataSetChanged();
                        LocalPhotoDetailsActivity.this.viewPager.post(new Runnable() { // from class: activity.LocalPhotoDetailsActivity.4.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                LocalPhotoDetailsActivity.this.resetPhoto();
                                if (LocalPhotoDetailsActivity.this.photos.size() <= 0) {
                                    LocalPhotoDetailsActivity.this.viewPager.setCurrentItem(0);
                                    LocalPhotoDetailsActivity.this.index.setText("0/" + LocalPhotoDetailsActivity.this.photos.size());
                                    LocalPhotoDetailsActivity.this.ll_bottom.setVisibility(8);
                                    LocalPhotoDetailsActivity.this.viewPager.setVisibility(8);
                                    return;
                                }
                                if (LocalPhotoDetailsActivity.this.pos < LocalPhotoDetailsActivity.this.photos.size() - 1) {
                                    LocalPhotoDetailsActivity.this.viewPager.setCurrentItem(LocalPhotoDetailsActivity.this.pos);
                                    LocalPhotoDetailsActivity.this.index.setText((LocalPhotoDetailsActivity.this.pos + 1) + "/" + LocalPhotoDetailsActivity.this.photos.size());
                                    LocalPhotoDetailsActivity.this.ll_bottom.setVisibility(0);
                                    LocalPhotoDetailsActivity.this.viewPager.setVisibility(0);
                                    return;
                                }
                                LocalPhotoDetailsActivity.this.pos = LocalPhotoDetailsActivity.this.photos.size() - 1;
                                LocalPhotoDetailsActivity.this.viewPager.setCurrentItem(LocalPhotoDetailsActivity.this.pos);
                                LocalPhotoDetailsActivity.this.index.setText((LocalPhotoDetailsActivity.this.pos + 1) + "/" + LocalPhotoDetailsActivity.this.photos.size());
                                LocalPhotoDetailsActivity.this.ll_bottom.setVisibility(0);
                                LocalPhotoDetailsActivity.this.viewPager.setVisibility(0);
                            }
                        });
                        return;
                    }
                    Toast.makeText(LocalPhotoDetailsActivity.this.getActivity(), R.string.delete_fail, 1).show();
                }
            }).canCancel(false).create().show(LocalPhotoDetailsActivity.this.getSupportFragmentManager(), "");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetPhoto() {
        int childCount = this.viewPager.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.viewPager.getChildAt(i);
            if (childAt != null) {
                try {
                    if (childAt instanceof PhotoView) {
                        new PhotoViewAttacher((PhotoView) childAt).getDisplayMatrix().reset();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        ExecutorService executorService = this.service;
        if (executorService != null) {
            executorService.shutdown();
            this.service = null;
        }
    }

    public void savePhoto(final String str, final boolean z) {
        showProgressDialog(R.string.downloading);
        this.service.execute(new Runnable() { // from class: activity.LocalPhotoDetailsActivity.6
            @Override // java.lang.Runnable
            public void run() {
                LocalPhotoDetailsActivity.this.isMediaStoreDeleteSucceed = true;
                if (z) {
                    try {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                        httpURLConnection.setConnectTimeout(5000);
                        httpURLConnection.setRequestMethod("GET");
                        if (httpURLConnection.getResponseCode() != 200) {
                            LocalPhotoDetailsActivity.this.isMediaStoreDeleteSucceed = false;
                        } else {
                            Bitmap bitmapDecodeStream = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                            LocalPhotoDetailsActivity.this.isMediaStoreDeleteSucceed = LocalPhotoDetailsActivity.this.saveBitmap(bitmapDecodeStream, System.currentTimeMillis() + ".jpg");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Bitmap bitmapDecodeStream2 = BitmapFactory.decodeStream(new FileInputStream(str));
                        LocalPhotoDetailsActivity.this.isMediaStoreDeleteSucceed = LocalPhotoDetailsActivity.this.saveBitmap(bitmapDecodeStream2, System.currentTimeMillis() + ".jpg");
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                LocalPhotoDetailsActivity.this.handler.post(new Runnable() { // from class: activity.LocalPhotoDetailsActivity.6.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (LocalPhotoDetailsActivity.this.isMediaStoreDeleteSucceed) {
                            Toast.makeText(LocalPhotoDetailsActivity.this.getActivity(), R.string.download_success, 0).show();
                        } else {
                            Toast.makeText(LocalPhotoDetailsActivity.this.getActivity(), R.string.download_failed, 0).show();
                        }
                        LocalPhotoDetailsActivity.this.dismissProgressDialog();
                    }
                });
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
                    getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                } else {
                    MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), str, (String) null);
                }
            }
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + str2)));
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
            Uri uriInsert = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            if (uriInsert == null || (outputStreamOpenOutputStream = getContentResolver().openOutputStream(uriInsert)) == null) {
                return;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStreamOpenOutputStream);
            outputStreamOpenOutputStream.flush();
            outputStreamOpenOutputStream.close();
        } catch (Exception unused) {
        }
    }
}
