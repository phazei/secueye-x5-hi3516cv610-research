package dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import bean.RefreshPicture;
import com.seculink.app.R;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.greenrobot.eventbus.EventBus;
import tools.MediaStoreUtil;
import tools.ScreenUtil;
import tools.Utils;
import uk.co.senab.photoview.PhotoView;

/* JADX INFO: loaded from: classes3.dex */
public class SnapshotPreviewDialog extends Dialog implements View.OnClickListener {
    Bitmap bitmap;
    Button closeBtn;
    Activity context;
    PhotoView photoView;
    Button saveBtn;

    public SnapshotPreviewDialog(@NonNull Activity activity2) {
        super(activity2);
        this.context = activity2;
    }

    public SnapshotPreviewDialog(@NonNull Activity activity2, int i) {
        super(activity2, i);
        this.context = activity2;
    }

    protected SnapshotPreviewDialog(@NonNull Activity activity2, boolean z, @Nullable DialogInterface.OnCancelListener onCancelListener) {
        super(activity2, z, onCancelListener);
        this.context = activity2;
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        requestWindowFeature(1);
        setContentView(R.layout.snapshot_preview_dialog);
        this.photoView = (PhotoView) findViewById(R.id.photo_view);
        this.closeBtn = (Button) findViewById(R.id.close_btn);
        this.saveBtn = (Button) findViewById(R.id.save_btn);
        this.closeBtn.setOnClickListener(this);
        this.saveBtn.setOnClickListener(this);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = this.context.getResources().getConfiguration().orientation == 2 ? (ScreenUtil.getDisplayMetrics(this.context)[0] * 4) / 5 : (ScreenUtil.getDisplayMetrics(this.context)[0] * 8) / 9;
        attributes.height = this.context.getResources().getConfiguration().orientation == 2 ? (ScreenUtil.getDisplayMetrics(this.context)[1] * 8) / 9 : (ScreenUtil.getDisplayMetrics(this.context)[1] * 4) / 5;
        getWindow().setAttributes(attributes);
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.photoView.setImageBitmap(bitmap);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        if (view2 == this.closeBtn) {
            if (isShowing()) {
                dismiss();
            }
        } else if (view2 == this.saveBtn) {
            saveImageToGallery(getContext(), this.bitmap);
            Toast.makeText(getContext(), "已保存", 0).show();
            dismiss();
        }
    }

    public static void saveImageToGallery(Context context, Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getPackageName() + "/" + Utils.getUserPhone() + "/photo/");
        if (file.exists() || file.mkdirs()) {
            File file2 = new File(file, System.currentTimeMillis() + ".jpg");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            EventBus.getDefault().post(new RefreshPicture());
            long j = 0;
            try {
                j = Long.parseLong(file2.getName().substring(0, file2.getName().indexOf(".")));
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            try {
                MediaStoreUtil.addPictureFileToMediaStore(context, file2, j);
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        }
    }
}
