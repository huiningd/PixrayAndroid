package fi.guagua.pixrayandroid.network;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Volley for transmitting network data.
 */
public class VolleySingleton {

    private static VolleySingleton sInstance = null;
    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;
    private static Context mContex;

    @TargetApi(12)
    private VolleySingleton (Context appContext) { // the context need to be application context
        mContex = appContext;
        mRequestQueue = Volley.newRequestQueue(appContext);
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private int size = (int)(Runtime.getRuntime().maxMemory()/1024/8);
            private LruCache<String, Bitmap> cache = new LruCache<>(size);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public static VolleySingleton getInstance(Context appContext) {
        if (sInstance == null) {
            sInstance = new VolleySingleton(appContext);
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

 }
