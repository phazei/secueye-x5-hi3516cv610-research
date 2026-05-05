package dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.seculink.app.R;
import java.net.HttpURLConnection;
import java.net.URL;
import tools.LogEx;

/* JADX INFO: loaded from: classes3.dex */
public class PictureDialog {
    private String TAG;
    private Handler handler;
    private TextView infoTv;
    private ImageView picIv;

    private PictureDialog() {
        this.TAG = getClass().getSimpleName();
    }

    private static class PictureDialogHolder {

        /* JADX INFO: renamed from: dialog, reason: collision with root package name */
        private static final PictureDialog f7873dialog = new PictureDialog();

        private PictureDialogHolder() {
        }
    }

    public static PictureDialog getInstance() {
        return PictureDialogHolder.f7873dialog;
    }

    public void openDialog(Context context, String str) {
        this.handler = new Handler(context.getMainLooper()) { // from class: dialog.PictureDialog.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 0:
                        PictureDialog.this.picIv.setVisibility(4);
                        PictureDialog.this.infoTv.setVisibility(0);
                        PictureDialog.this.infoTv.setText("请求失败");
                        break;
                    case 1:
                        PictureDialog.this.picIv.setVisibility(0);
                        PictureDialog.this.picIv.setImageBitmap((Bitmap) message.obj);
                        break;
                }
            }
        };
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.picture_dialog_layout, (ViewGroup) null);
        this.picIv = (ImageView) viewInflate.findViewById(R.id.iv_picture);
        this.infoTv = (TextView) viewInflate.findViewById(R.id.tv_info);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, 3);
        builder.setTitle("照片");
        builder.setView(viewInflate);
        builder.setNegativeButton("关闭", (DialogInterface.OnClickListener) null);
        builder.create().show();
        getPic(str);
    }

    private void getPic(final String str) {
        new Thread() { // from class: dialog.PictureDialog.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.connect();
                    if (httpURLConnection.getResponseCode() != 200) {
                        Message messageObtainMessage = PictureDialog.this.handler.obtainMessage();
                        messageObtainMessage.what = 0;
                        PictureDialog.this.handler.sendMessage(messageObtainMessage);
                    } else {
                        Bitmap bitmapDecodeStream = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                        Message message = new Message();
                        message.obj = bitmapDecodeStream;
                        message.what = 1;
                        PictureDialog.this.handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogEx.e(true, PictureDialog.this.TAG, e.toString());
                }
            }
        }.start();
    }
}
