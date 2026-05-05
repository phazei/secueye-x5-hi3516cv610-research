package tools;

import android.graphics.Bitmap;
import android.util.LruCache;

/* JADX INFO: loaded from: classes4.dex */
public class ImageCache {
    private LruCache<String, Bitmap> cache;

    private ImageCache() {
    }

    private static class ImageCacheHolder {
        private static final ImageCache IMAGE_CACHE = new ImageCache();

        private ImageCacheHolder() {
        }
    }

    public static ImageCache getInstance() {
        return ImageCacheHolder.IMAGE_CACHE;
    }

    public void init() {
        this.cache = new LruCache<String, Bitmap>(((int) Runtime.getRuntime().maxMemory()) / 4) { // from class: tools.ImageCache.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.util.LruCache
            public int sizeOf(String str, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }

    public Bitmap getImage(String str) {
        return this.cache.get(str);
    }

    public void addImgIntoCache(String str, Bitmap bitmap) {
        if (getImage(str) == null) {
            this.cache.put(str, bitmap);
        }
    }
}
