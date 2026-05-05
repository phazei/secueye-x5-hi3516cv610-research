package tools;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;
import com.google.android.exoplayer2.util.MimeTypes;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/* JADX INFO: loaded from: classes4.dex */
public class MediaStoreUtil {
    public static void addVideoFileToMediaStore(Context context, File file, long j) throws IOException {
        ContentResolver contentResolver = context.getContentResolver();
        FileInputStream fileInputStream = new FileInputStream(file);
        File file2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), file.getName());
        FileOutputStream fileOutputStream = new FileOutputStream(file2);
        try {
            try {
                byte[] bArr = new byte[1024];
                while (true) {
                    int i = fileInputStream.read(bArr, 0, bArr.length);
                    if (i == -1) {
                        break;
                    } else {
                        fileOutputStream.write(bArr, 0, i);
                    }
                }
                fileOutputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            fileInputStream.close();
            fileOutputStream.close();
            ContentValues contentValues = new ContentValues();
            contentValues.put("title", file2.getName());
            contentValues.put("_display_name", file2.getName());
            contentValues.put("_data", file2.getAbsolutePath());
            contentValues.put("datetaken", Long.valueOf(j));
            long j2 = j / 1000;
            contentValues.put("date_added", Long.valueOf(j2));
            contentValues.put("date_modified", Long.valueOf(j2));
            contentValues.put("_size", Long.valueOf(file2.length()));
            contentValues.put("mime_type", MimeTypes.VIDEO_MP4);
            context.getApplicationContext().sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)));
        } catch (Throwable th) {
            fileInputStream.close();
            fileOutputStream.close();
            throw th;
        }
    }

    public static void addPictureFileToMediaStore(Context context, File file, long j) throws IOException {
        ContentResolver contentResolver = context.getContentResolver();
        FileInputStream fileInputStream = new FileInputStream(file);
        File file2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), file.getName());
        FileOutputStream fileOutputStream = new FileOutputStream(file2);
        try {
            try {
                byte[] bArr = new byte[1024];
                while (true) {
                    int i = fileInputStream.read(bArr, 0, bArr.length);
                    if (i == -1) {
                        break;
                    } else {
                        fileOutputStream.write(bArr, 0, i);
                    }
                }
                fileOutputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            fileInputStream.close();
            fileOutputStream.close();
            ContentValues contentValues = new ContentValues();
            contentValues.put("title", file2.getName());
            contentValues.put("_display_name", file2.getName());
            contentValues.put("_data", file2.getAbsolutePath());
            contentValues.put("datetaken", Long.valueOf(j));
            long j2 = j / 1000;
            contentValues.put("date_added", Long.valueOf(j2));
            contentValues.put("date_modified", Long.valueOf(j2));
            contentValues.put("_size", Long.valueOf(file2.length()));
            contentValues.put("mime_type", "image/jpg");
            context.getApplicationContext().sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)));
        } catch (Throwable th) {
            fileInputStream.close();
            fileOutputStream.close();
            throw th;
        }
    }
}
