package fi.guagua.pixrayandroid;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ImageFragment extends Fragment {
    private static final String TAG = "ImageFragment";
    private Context mAppContext;
    private Image mImage;
    //private TextView mImageLabel;
    private ScoreTypes mScoreTypes;
    private String mSample;
    private String mScreenName;
    private ArrayList<WellConditions> mWellConditionses;
    private int mCurrentScoreId;

    public ImageFragment() {
    }

    public static ImageFragment newInstance(Image image) {
        Bundle args = new Bundle();
        args.putSerializable(Pixray.EXTRA_IMAGE, image);
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        Log.d(TAG, "ImageFragment is now created.");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImage = (Image) getArguments().getSerializable(Pixray.EXTRA_IMAGE);
        setHasOptionsMenu(true);
        mAppContext = getActivity().getApplicationContext();
        initDataset();
        setRetainInstance(true);
    }

    private void initDataset() {
        String urlScoreTypes = Urls.getUrlScoreTypes();
        PixrayAPI.DownloadJson(new PixrayAPICallback() {
            @Override
            public void callback(JSONObject response) {
                buildScoreTypes(response);
            }
        }, mAppContext, urlScoreTypes);

        String urlSampleScreenScore = Urls.getUrlSampleScreenScore(mImage);
        PixrayAPI.DownloadJson(new PixrayAPICallback() {
            @Override
            public void callback(JSONObject response) {
                buildSampleScreenAndScore(response);
            }
        }, mAppContext, urlSampleScreenScore);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_display_single_image, container, false);
        Pixray.setToolBar(v, (ActionBarActivity) getActivity(), R.string.single_image);

        final String url = mImage.getLargeImageUrl();
        Log.d(TAG, "image url is " + url);
        String label = mImage.getLabel();

        // load image from server
        final NetworkImageView networkImageView = (NetworkImageView) v.findViewById(R.id.singleImagePic);
        ImageLoader imageLoader = VolleySingleton.getInstance(mAppContext).getImageLoader();
        networkImageView.setDefaultImageResId(R.drawable.loading);
        networkImageView.setErrorImageResId(R.drawable.image_error);
        networkImageView.setImageUrl(url, imageLoader);

        // set image label
        TextView imageLabel = (TextView) v.findViewById(R.id.singleImageLabel);
        imageLabel.setText("Label: " + label);

        networkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "image is touched!");
                Intent i = new Intent(mAppContext, EnlargedImageActivity.class);
                i.putExtra(Pixray.EXTRA_IMAGE_URL, url);
                startActivity(i);
            }
        });

        // load current score
        TextView score = (TextView) v.findViewById(R.id.scoreColor);
        score.setText(" test score color ");
        score.setBackgroundColor(getResources().getColor(R.color.colorAccent)); // testing
        // TODO: get info from json

        // choose new score
        Button changeScore = (Button) v.findViewById(R.id.changeScore);
        changeScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getFragmentManager();
                ChooseNewScoreDialog newScoreDialog = ChooseNewScoreDialog.newInstance(mScoreTypes);
                newScoreDialog.show(fm, Pixray.NEW_SCORE_DIALOG);
            }
        });
        return v;
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
        GalleryInfo info = mImage.getGalleryInfo();
        FragmentManager fm = getActivity().getFragmentManager();
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                return false; // not implemented here.
            case R.id.action_date:
                ArrayList<Integer> dateIds = info.getDateIds();
                ArrayList<String> dates = info.getDates();
                SelectorDialogFragment dateDialog = SelectorDialogFragment.newInstance(
                        Pixray.DATE_SELECTOR, dates, dateIds);
                dateDialog.show(fm, Pixray.DATE_SELECTOR); // fragment transaction is handled in show()
                return true;
            case R.id.action_type:
                ArrayList<Integer> typeIds = info.getTypeIds();
                ArrayList<String> types = info.getTypes();
                SelectorDialogFragment typeDialog = SelectorDialogFragment.newInstance(
                        Pixray.TYPE_SELECTOR, types, typeIds);
                typeDialog.show(fm, Pixray.TYPE_SELECTOR);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void buildSampleScreenAndScore(JSONObject response) {

    }

    private void buildScoreTypes(JSONObject response) {
        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> colors = new ArrayList<>();
        try {
            JSONArray array = response.getJSONArray(Pixray.JSON_SCORE_TYPES);
            // Build the array of projects from JSONObjects
            for (int i = 0; i < array.length(); i++) {
                int id = array.getJSONObject(i).getInt(Pixray.JSON_ID);
                String score = array.getJSONObject(i).getString(Pixray.JSON_SCORE);
                String color = array.getJSONObject(i).getString(Pixray.JSON_COLOR);
                ids.add(id);
                names.add(score);
                colors.add(color);
                //for (Project p : mProjects) { System.out.println(p.toString()); } // debug
            }
            mScoreTypes = new ScoreTypes(ids, names, colors);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to fetch project list!", e);
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


}
