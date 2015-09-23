package fi.guagua.pixrayandroid.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import fi.guagua.pixrayandroid.models.GalleryInfo;
import fi.guagua.pixrayandroid.models.Image;
import fi.guagua.pixrayandroid.views.widgets.InfoDialogFragment;
import fi.guagua.pixrayandroid.utils.Utility;
import fi.guagua.pixrayandroid.R;
import fi.guagua.pixrayandroid.models.ScoreTypes;
import fi.guagua.pixrayandroid.views.widgets.SelectorDialogFragment;
import fi.guagua.pixrayandroid.network.VolleySingleton;
import fi.guagua.pixrayandroid.models.WellConditions;
import fi.guagua.pixrayandroid.views.widgets.WellConditionsDialog;
import fi.guagua.pixrayandroid.activities.EnlargedImageActivity;
import fi.guagua.pixrayandroid.views.widgets.ChooseNewScoreDialog;

public class ImageFragment extends Fragment {
    private static final String TAG = ImageFragment.class.getSimpleName();
    private View mRootView;
    private Context mAppContext;
    private Image mImage;

    public ImageFragment() {
    }

    public static ImageFragment newInstance(Image image) {
        Bundle args = new Bundle();
        args.putSerializable(Utility.EXTRA_IMAGE, image);
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        Log.d(TAG, "ImageFragment is now created.");
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_display_single_image, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAppContext = getActivity().getApplicationContext();
        mImage = (Image) getArguments().getSerializable(Utility.EXTRA_IMAGE);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        Utility.setToolBar(mRootView, (AppCompatActivity) getActivity(), R.string.single_image);

        // load image from server
        final String url = mImage.getLargeImageUrl();
        Log.d(TAG, "image url is " + url);
        final NetworkImageView networkImageView = (NetworkImageView) mRootView.findViewById(R.id.singleImagePic);
        ImageLoader imageLoader = VolleySingleton.getInstance(mAppContext).getImageLoader();
        networkImageView.setDefaultImageResId(R.drawable.loading);
        networkImageView.setErrorImageResId(R.drawable.image_error);
        networkImageView.setImageUrl(url, imageLoader);

        // enlarge image on click
        networkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "image is touched!");
                Intent i = new Intent(mAppContext, EnlargedImageActivity.class);
                i.putExtra(Utility.EXTRA_IMAGE_URL, url);
                startActivity(i);
            }
        });

        // set image label
        TextView imageLabel = (TextView) mRootView.findViewById(R.id.singleImageLabel);
        imageLabel.setText("Label: " + mImage.getLabel());

        // load current score
        TextView score = (TextView) mRootView.findViewById(R.id.scoreColor);
        int scoreId = mImage.getCurrentScoreId();
        final ScoreTypes scoreTypes = mImage.getScoreTypes();
        score.setText(Utility.getScoreName(scoreTypes, scoreId));
        String color = Utility.getScoreColor(scoreTypes, scoreId);
        score.setBackgroundColor(Color.parseColor(color));

        // choose new score
        Button changeScore = (Button) mRootView.findViewById(R.id.changeScore);
        changeScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getFragmentManager();
                ChooseNewScoreDialog newScoreDialog = ChooseNewScoreDialog.newInstance(scoreTypes);
                newScoreDialog.show(fm, Utility.NEW_SCORE_DIALOG);
            }
        });

        // set sample name
        String sampleName = mImage.getSample();
        TextView imageSample = (TextView) mRootView.findViewById(R.id.singleImageSample);
        imageSample.setText("Sample: " + sampleName);

        // set screen name
        String screenName = mImage.getScreenName();
        TextView imageScreen = (TextView) mRootView.findViewById(R.id.singleImageScreen);
        imageScreen.setText("Screen: " + screenName);

        // check details of well-conditions
        final ArrayList<WellConditions> wcs = mImage.getWellConditionses();
        Button seeWellConditions = (Button) mRootView.findViewById(R.id.singleImageWCondition);
        if (wcs.isEmpty()) {
            seeWellConditions.setVisibility(View.GONE);
        } else {
            seeWellConditions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getActivity().getFragmentManager();
                    WellConditionsDialog wcDialog = WellConditionsDialog.newInstance(wcs);
                    wcDialog.show(fm, Utility.WELL_CONDITIONS_DIALOG);
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //menu.clear();
        inflater.inflate(R.menu.memu_info_date_and_type, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
                        Utility.DATE_SELECTOR, dates, dateIds);
                dateDialog.show(fm, Utility.DATE_SELECTOR); // fragment transaction is handled in show()
                return true;
            case R.id.action_type:
                ArrayList<Integer> typeIds = info.getTypeIds();
                ArrayList<String> types = info.getTypes();
                SelectorDialogFragment typeDialog = SelectorDialogFragment.newInstance(
                        Utility.TYPE_SELECTOR, types, typeIds);
                typeDialog.show(fm, Utility.TYPE_SELECTOR);
                return true;
            case R.id.action_info:
                GalleryInfo galleryInfo = mImage.getGalleryInfo();
                int dateId = galleryInfo.getRequestDateId();
                int typeId = galleryInfo.getRequestTypeId();
                String date = galleryInfo.getDateById(dateId);
                String type = galleryInfo.getTypeById(typeId);
                InfoDialogFragment infoDialog = InfoDialogFragment.newInstance(date, type);
                infoDialog.show(fm, Utility.INFO_DIALOG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
