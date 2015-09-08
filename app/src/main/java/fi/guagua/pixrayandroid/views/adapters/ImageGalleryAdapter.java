package fi.guagua.pixrayandroid.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import fi.guagua.pixrayandroid.R;
import fi.guagua.pixrayandroid.network.VolleySingleton;
import fi.guagua.pixrayandroid.models.Image;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.GalleryItemViewHolder> {

    private static final String TAG = "ImageGalleryAdapter";
    private Context mAppContext;
    private LayoutInflater mInflater;
    private ArrayList<Image> mImages;

    public ImageGalleryAdapter(Context context, ArrayList<Image> images) {
        //Log.d(TAG, "Adpter constructor is called");
        //Log.d(TAG, "image number is " + mImages.size());
        mAppContext = context;
        mInflater = LayoutInflater.from(context);
        mImages = images;
        Log.d(TAG, "Adpter constructor  finished");
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GalleryItemViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        //Log.d(TAG, "Adapter onCreateViewHolder is called");
        View v = mInflater.inflate(R.layout.gallery_item, parent, false);
        return new GalleryItemViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(GalleryItemViewHolder viewHolder, int position) {
        //Log.d(TAG, "Adapter onBindViewHolder is called");
        Image image = mImages.get(position);
        String url = image.getThumbnailUrl();
        String label = image.getLabel();

        // Get the ImageLoader through the singleton class.
        ImageLoader imageLoader = VolleySingleton.getInstance(mAppContext).getImageLoader();

        viewHolder.image.setDefaultImageResId(R.drawable.loading);
        viewHolder.image.setErrorImageResId(R.drawable.image_error);

        // Set the URL of the image that should be loaded into this view, and
        // specify the ImageLoader that will be used to make the request.
        viewHolder.image.setImageUrl(url, imageLoader);
        // set label of the image
        viewHolder.imageCaption.setText(label);
    }

    // Return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mImages.size();
    }

    // Provide a reference to the type of views that are being used (custom ViewHolder)
    class GalleryItemViewHolder extends RecyclerView.ViewHolder {
        TextView imageCaption;
        NetworkImageView image;

        public GalleryItemViewHolder(View itemView) {
            super(itemView);
            image = (NetworkImageView) itemView.findViewById(R.id.gallery_item_networkImageView);
            imageCaption = (TextView) itemView.findViewById(R.id.image_caption);
        }
    }
}

