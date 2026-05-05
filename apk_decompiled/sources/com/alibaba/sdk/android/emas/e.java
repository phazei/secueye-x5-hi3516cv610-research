package com.alibaba.sdk.android.emas;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.alibaba.sdk.android.tbrest.utils.AppUtils;
import com.alibaba.sdk.android.tbrest.utils.LogUtil;
import com.alibaba.sdk.android.tbrest.utils.MD5Utils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: DiskCacheManager.java */
/* JADX INFO: loaded from: classes.dex */
public class e implements Cache<f> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private long f2884a;

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private final String f11a;

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private Set<String> f12a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String f2885b;
    private int diskCacheLimitCapacity;
    private int diskCacheLimitCount;

    public e(Context context, String str, String str2, String str3) {
        this.f2885b = str2;
        this.f11a = context.getFilesDir() + File.separator + "emas-rest-log" + File.separator + (str + OpenAccountUIConstants.UNDER_LINE + str2) + File.separator + (TextUtils.isEmpty(str3) ? "common" : str3);
        File file = new File(this.f11a);
        if (file.exists()) {
            return;
        }
        file.mkdirs();
    }

    public void a(int i, int i2, int i3) {
        this.diskCacheLimitCount = i;
        this.diskCacheLimitCapacity = i2;
        this.f2884a = ((long) i3) * 86400000;
    }

    @Override // com.alibaba.sdk.android.emas.Cache
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public synchronized void add(f fVar) {
        if (fVar != null) {
            if (fVar.a() != d.DISK_CACHE) {
                List<g> listM17a = fVar.m17a();
                if (listM17a != null && !listM17a.isEmpty()) {
                    JSONArray jSONArray = new JSONArray();
                    for (int i = 0; i != listM17a.size(); i++) {
                        JSONObject jSONObjectA = listM17a.get(i).a();
                        if (jSONObjectA != null) {
                            jSONArray.put(jSONObjectA);
                        }
                    }
                    String strAesEncrypt = h.aesEncrypt(this.f2885b, jSONArray.toString());
                    if (!TextUtils.isEmpty(strAesEncrypt)) {
                        LogUtil.d("DiskCacheManager putting into cache.");
                        File file = new File(this.f11a, MD5Utils.getMd5Hex(strAesEncrypt.getBytes(Charset.forName("UTF-8"))) + OpenAccountUIConstants.UNDER_LINE + System.currentTimeMillis() + ".log");
                        if (file.exists()) {
                            file.delete();
                        }
                        a(file, strAesEncrypt);
                        LogUtil.d("DiskCacheManager success put into " + file.getAbsolutePath());
                    } else {
                        LogUtil.d("DiskCacheManager failed put into cache.");
                    }
                }
                return;
            }
        }
        if (fVar == null) {
            LogUtil.d("DiskCacheManager add failed. data is null");
        } else {
            LogUtil.d("DiskCacheManager add failed. cache type: " + fVar.a().name());
        }
    }

    @Override // com.alibaba.sdk.android.emas.Cache
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public synchronized f get() {
        File file;
        File file2 = new File(this.f11a);
        if (!file2.exists()) {
            return null;
        }
        if (!file2.isDirectory()) {
            file2.delete();
            return null;
        }
        List<File> listA = a(file2, new ArrayList());
        if (listA.size() <= 0) {
            return null;
        }
        Collections.sort(listA, new Comparator<File>() { // from class: com.alibaba.sdk.android.emas.e.1
            @Override // java.util.Comparator
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public int compare(File file3, File file4) {
                if (file3 == null && file4 == null) {
                    return 0;
                }
                if (file3 == null) {
                    return -1;
                }
                if (file4 == null) {
                    return 1;
                }
                if (file3 == file4) {
                    return 0;
                }
                if (file3.lastModified() < file4.lastModified()) {
                    return -1;
                }
                return file3.lastModified() == file4.lastModified() ? 0 : 1;
            }
        });
        if (this.f12a == null || this.f12a.isEmpty()) {
            file = listA.get(0);
        } else {
            int i = 0;
            while (true) {
                if (i == listA.size()) {
                    file = null;
                    break;
                }
                if (!this.f12a.contains(listA.get(i).getAbsolutePath())) {
                    file = listA.get(i);
                    break;
                }
                LogUtil.d("DiskCacheManager disk cache is in the Sending Queue. skip location: " + listA.get(i).getAbsolutePath());
                i++;
            }
        }
        if (file == null) {
            return null;
        }
        String strAesDecrypt = h.aesDecrypt(this.f2885b, a(file));
        if (TextUtils.isEmpty(strAesDecrypt)) {
            return null;
        }
        try {
            JSONArray jSONArray = new JSONArray(strAesDecrypt);
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 != jSONArray.length(); i2++) {
                g gVarA = g.a(jSONArray.getJSONObject(i2));
                if (gVarA != null) {
                    arrayList.add(gVarA);
                }
            }
            if (this.f12a == null) {
                this.f12a = new HashSet();
            }
            this.f12a.add(file.getAbsolutePath());
            return new f(arrayList, d.DISK_CACHE, file.getAbsolutePath());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.alibaba.sdk.android.emas.Cache
    /* JADX INFO: renamed from: a, reason: collision with other method in class and merged with bridge method [inline-methods] */
    public synchronized boolean remove(f fVar) {
        if (fVar != null) {
            if (fVar.a() == d.DISK_CACHE && !TextUtils.isEmpty(fVar.getLocation())) {
                LogUtil.d("DiskCacheManager removing. cache type: " + fVar.a().name());
                if (this.f12a != null) {
                    this.f12a.remove(fVar.getLocation());
                }
                File file = new File(fVar.getLocation());
                if (!file.exists()) {
                    return false;
                }
                return file.delete();
            }
        }
        if (fVar == null) {
            LogUtil.d("DiskCacheManager remove failed. data is null");
        } else {
            LogUtil.d("DiskCacheManager remove failed. cache type: " + fVar.a().name());
        }
        return false;
    }

    @Override // com.alibaba.sdk.android.emas.Cache
    public synchronized void clear() {
        File file = new File(this.f11a);
        if (file.exists()) {
            if (!file.isDirectory()) {
                file.delete();
                return;
            }
            List<File> listA = a(file, new ArrayList());
            LogUtil.d("DiskCacheManager num: " + listA.size());
            if (listA.size() > 0) {
                Collections.sort(listA, new Comparator<File>() { // from class: com.alibaba.sdk.android.emas.e.2
                    @Override // java.util.Comparator
                    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
                    public int compare(File file2, File file3) {
                        if (file2 == null && file3 == null) {
                            return 0;
                        }
                        if (file2 == null) {
                            return -1;
                        }
                        if (file3 == null) {
                            return 1;
                        }
                        if (file2 == file3) {
                            return 0;
                        }
                        if (file2.lastModified() < file3.lastModified()) {
                            return -1;
                        }
                        return file2.lastModified() == file3.lastModified() ? 0 : 1;
                    }
                });
                long jCurrentTimeMillis = System.currentTimeMillis();
                Iterator<File> it = listA.iterator();
                int i = 0;
                long length = 0;
                int i2 = 0;
                while (it.hasNext()) {
                    File next = it.next();
                    if (jCurrentTimeMillis - next.lastModified() >= this.f2884a) {
                        it.remove();
                        next.delete();
                    } else {
                        i2++;
                        length += next.length();
                    }
                }
                if (i2 > this.diskCacheLimitCount || length > this.diskCacheLimitCapacity) {
                    LogUtil.d("DiskCacheManager exceed limit. start clear.");
                    int i3 = (int) (((double) this.diskCacheLimitCount) * 0.8d);
                    int i4 = (int) (((double) this.diskCacheLimitCapacity) * 0.8d);
                    while (true) {
                        if ((i2 <= i3 && length <= i4) || i >= listA.size()) {
                            break;
                        }
                        File file2 = listA.get(i);
                        if (file2.delete()) {
                            i2--;
                            length -= file2.length();
                        }
                        i++;
                    }
                }
            }
        }
    }

    private List<File> a(File file, List<File> list) {
        File[] fileArrListFiles;
        if (file.isDirectory() && (fileArrListFiles = file.listFiles()) != null && fileArrListFiles.length > 0) {
            for (File file2 : fileArrListFiles) {
                if (file2.isDirectory()) {
                    a(file2, list);
                } else if (!list.contains(file2)) {
                    list.add(file2);
                }
            }
        }
        return list;
    }

    private String a(File file) throws Throwable {
        BufferedInputStream bufferedInputStream;
        StringBuilder sb = new StringBuilder();
        byte[] bArr = new byte[4096];
        try {
            try {
                bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                while (true) {
                    try {
                        int i = bufferedInputStream.read(bArr);
                        if (i != -1) {
                            sb.append(new String(bArr, 0, i));
                        } else {
                            String string = sb.toString();
                            AppUtils.closeQuietly(bufferedInputStream);
                            return string;
                        }
                    } catch (FileNotFoundException e) {
                        e = e;
                        e.printStackTrace();
                        AppUtils.closeQuietly(bufferedInputStream);
                        return null;
                    } catch (IOException e2) {
                        e = e2;
                        e.printStackTrace();
                        AppUtils.closeQuietly(bufferedInputStream);
                        return null;
                    }
                }
            } catch (Throwable th) {
                th = th;
                AppUtils.closeQuietly(null);
                throw th;
            }
        } catch (FileNotFoundException e3) {
            e = e3;
            bufferedInputStream = null;
        } catch (IOException e4) {
            e = e4;
            bufferedInputStream = null;
        } catch (Throwable th2) {
            th = th2;
            AppUtils.closeQuietly(null);
            throw th;
        }
    }

    private void a(File file, String str) throws Throwable {
        BufferedOutputStream bufferedOutputStream;
        BufferedOutputStream bufferedOutputStream2 = null;
        try {
            try {
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            } catch (Throwable th) {
                th = th;
            }
        } catch (FileNotFoundException e) {
            e = e;
        } catch (UnsupportedEncodingException e2) {
            e = e2;
        } catch (IOException e3) {
            e = e3;
        }
        try {
            bufferedOutputStream.write(str.getBytes("utf-8"));
            AppUtils.closeQuietly(bufferedOutputStream);
        } catch (FileNotFoundException e4) {
            e = e4;
            bufferedOutputStream2 = bufferedOutputStream;
            e.printStackTrace();
            AppUtils.closeQuietly(bufferedOutputStream2);
        } catch (UnsupportedEncodingException e5) {
            e = e5;
            bufferedOutputStream2 = bufferedOutputStream;
            e.printStackTrace();
            AppUtils.closeQuietly(bufferedOutputStream2);
        } catch (IOException e6) {
            e = e6;
            bufferedOutputStream2 = bufferedOutputStream;
            e.printStackTrace();
            AppUtils.closeQuietly(bufferedOutputStream2);
        } catch (Throwable th2) {
            th = th2;
            bufferedOutputStream2 = bufferedOutputStream;
            AppUtils.closeQuietly(bufferedOutputStream2);
            throw th;
        }
    }

    public synchronized void b(f fVar) {
        if (this.f12a != null && !this.f12a.isEmpty()) {
            if (TextUtils.isEmpty(fVar.getLocation())) {
                return;
            }
            this.f12a.remove(fVar.getLocation());
        }
    }
}
