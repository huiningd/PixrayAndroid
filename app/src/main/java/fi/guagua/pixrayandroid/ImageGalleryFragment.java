package fi.guagua.pixrayandroid;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ImageGalleryFragment extends Fragment {

    private static final String TAG = "ImageGalleryFragment";

    private Context mAppContext;
    private ArrayList<Image> mImages = new ArrayList<>();
    private int mProjectId;
    private int mPlateId;
    private int mRequestDateId;
    private int mRequestTypeId;
    private String mGalleryJson;
    private GalleryInfo mGalleryInfo;

    public static ImageGalleryFragment newInstance(GalleryInfo gallery, String galleryJson) {
        Bundle args = new Bundle();
        args.putSerializable(Pixray.EXTRA_GALLERY_INFO, gallery);
        args.putString(Pixray.EXTRA_GALLERY_JSON, galleryJson);
        Log.d(TAG, "gallery json in new instance:" + galleryJson);

        ImageGalleryFragment fragment = new ImageGalleryFragment();
        fragment.setArguments(args);
        Log.d(TAG, gallery.getPlateId() + " ImageGalleryFragment is now created.");
        return fragment;
    }

    public ImageGalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mGalleryJson = getArguments().getString(Pixray.EXTRA_GALLERY_JSON);
            //Log.d(TAG, "gallery json in fragment onCreate:" + mGalleryJson);
            mGalleryInfo = (GalleryInfo) getArguments().getSerializable(Pixray.EXTRA_GALLERY_INFO);
            mProjectId = mGalleryInfo.getProjectId();
            mPlateId = mGalleryInfo.getPlateId();
            Log.d(TAG, "date id is " + mRequestDateId + ", update it to " + mGalleryInfo.getRequestDateId());
            mRequestDateId = mGalleryInfo.getRequestDateId();
            Log.d(TAG, "type id is " + mRequestTypeId + ", update it to " + mGalleryInfo.getRequestTypeId());
            mRequestTypeId = mGalleryInfo.getRequestTypeId();
            //Log.d(TAG, "project id is " + mProjectId + " plate id is " + mPlateId + " date id is " + mRequestDateId);
        }

        setHasOptionsMenu(true);
        mAppContext = getActivity().getApplicationContext();
        Log.d(TAG, "onCreate finished");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_gallery_grid, container, false);
        Pixray.setToolBar(v, (ActionBarActivity)getActivity(), R.string.image_grid);
        final RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.gallery_recycler_view);

        // auto-fit grid
        int columnCount = findColumnCount();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), columnCount));

        parseImageGalleryJson(mGalleryJson);

        // set up adapter here, then every time a new GridView is created on rotation,
        // it is reconfigured with an appropriate adapter
        ImageGalleryAdapter adapter = new ImageGalleryAdapter(mAppContext, mImages);
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "adapter is set!"); // debug

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.i(TAG, "image with position " + position + " is clicked.");
                        //Image image = (Image) recyclerView.getChildAt(position);
                        Image image = mImages.get(position);
                        String url = Urls.getLargeImageUrl(image.getThumbnailUrl());
                        image.setLargeImageUrl(url);
                        Intent i = new Intent(getActivity(), ImageActivity.class);
                        i.putExtra(Pixray.EXTRA_IMAGE, image);
                        startActivity(i);
                    }
                })
        );
        return v;
    }

    // Calculate what is the appropriate number of columns for the grid to fit the screen.
    private int findColumnCount() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float dpWidth  = outMetrics.widthPixels / density;
        return Math.round(dpWidth/100);
    }

    private void parseImageGalleryJson(String galleryJson) {
        //Log.i(TAG, "Received ImageGallery json: " + response.toString());
        JSONObject response = null;
        try { response = new JSONObject(galleryJson);
        } catch (JSONException e) { e.printStackTrace(); }

        try {
            int rows = response.getInt("rows");
            int cols = response.getInt("columns");
            int drops = response.getInt("drops");
            //int types = response.getJSONArray("types").length();
            int imageCount = rows*cols*drops;
            Log.i(TAG, "image count is " + imageCount);
            if (imageCount == 0) {
                Toast.makeText(mAppContext, R.string.no_image,
                        Toast.LENGTH_LONG).show();
            } else {
                String urlGallery = Urls.getUrlImageGallery(mProjectId, mPlateId, mRequestDateId);
                if (mRequestTypeId == -1) {
                    mRequestTypeId = Pixray.getDefaultTypeId(response);
                    mGalleryInfo.setRequestTypeId(mRequestTypeId);
                }
                ArrayList<String> urlsThumbnail = Urls.getUrlsOfImageThumbnail(urlGallery,
                        mRequestTypeId, rows, cols, drops);
                ArrayList<String> labels = getImageLabels(rows, cols, drops);
                for (int i = 0; i < labels.size() && i < urlsThumbnail.size(); i++) {
                    Image image = new Image(mGalleryInfo, rows, cols, drops, urlsThumbnail.get(i), labels.get(i));
                    mImages.add(image);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error in gallery json", e);
            e.printStackTrace();
        }
    }

    private ArrayList<String> getImageLabels(int rows, int cols, int drops) {
        ArrayList<String> labels = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < drops; k++) {
                    String l = Pixray.IMAGE_INIT[i] + (j+1) + "." + (k+1); // row 7, col 11, drop 2 -> H12.3
                    labels.add(l);
                }
            }
        }
        return labels;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //menu.clear();
        inflater.inflate(R.menu.memu_info_date_and_type, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected is called");
        FragmentManager fm = getActivity().getFragmentManager();
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                return false; // not implemented here.
            case R.id.action_date:
                ArrayList<Integer> dateIds = mGalleryInfo.getDateIds();
                ArrayList<String> dates = mGalleryInfo.getDates();
                SelectorDialogFragment dateDialog = SelectorDialogFragment.newInstance(
                        Pixray.DATE_SELECTOR, dates, dateIds);
                dateDialog.show(fm, Pixray.DATE_SELECTOR); // fragment transaction is handled in show()
                return true;
            case R.id.action_type:
                ArrayList<Integer> typeIds = mGalleryInfo.getTypeIds();
                ArrayList<String> types = mGalleryInfo.getTypes();
                SelectorDialogFragment typeDialog = SelectorDialogFragment.newInstance(
                        Pixray.TYPE_SELECTOR, types, typeIds);
                typeDialog.show(fm, Pixray.TYPE_SELECTOR);
                return true;
            case R.id.action_info:
                int dateId = mGalleryInfo.getRequestDateId();
                int typeId = mGalleryInfo.getRequestTypeId();
                String date = mGalleryInfo.getDateById(dateId);
                String type = mGalleryInfo.getTypeById(typeId);
                InfoDialogFragment infoDialog = InfoDialogFragment.newInstance(date, type);
                infoDialog.show(fm, Pixray.INFO_DIALOG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}