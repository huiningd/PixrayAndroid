package fi.guagua.pixrayandroid.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.toolbox.ImageLoader;
import com.navercorp.volleyextensions.view.ZoomableNetworkImageView;

import fi.guagua.pixrayandroid.utils.Pixray;
import fi.guagua.pixrayandroid.R;
import fi.guagua.pixrayandroid.network.VolleySingleton;


public class EnlargedImageActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlarged_image);
        Context mAppContext = getApplicationContext();

        Intent i = getIntent();
        String url = i.getExtras().getString(Pixray.EXTRA_IMAGE_URL);

        ZoomableNetworkImageView zoomableImageView = (ZoomableNetworkImageView) findViewById(R.id.zoom_networkimageview);
        ImageLoader mImageLoader = VolleySingleton.getInstance(mAppContext).getImageLoader();
        zoomableImageView.setImageUrl(url, mImageLoader);

    }

}
