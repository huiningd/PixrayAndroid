package fi.guagua.pixrayandroid;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

/*
 * Have to use ActionBarActivity, otherwise getSupportActionBar(toolbar) doesn't work.
 */
public class ImageGalleryActivity extends AppCompatActivity implements
        SelectorDialogFragment.OnDateOrTypeSelectedListener {

    private static final String TAG = "ImageGalleryActivity";
    private Context mAppContext;
    private GalleryInfo mGalleryInfo;
    private int mProjectId;
    private int mPlateId;
    private int mRequestDateId = -1;
    private int mRequestTypeId = -1;
    private ArrayList<String> mDates;
    private ArrayList<Integer> mDateIds;
    private ArrayList<String> mTypes;
    private ArrayList<Integer> mTypeIds;
    private String mImageGalleryJson;
    private boolean mReplaceFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get data across rotation
        if (savedInstanceState != null) {
            mRequestDateId = savedInstanceState.getInt(Pixray.EXTRA_GALLERY_DATE);
            mRequestTypeId = savedInstanceState.getInt(Pixray.EXTRA_GALLERY_TYPE);
        }

        setContentView(R.layout.activity_fragment);

        mAppContext = getApplicationContext();
        mProjectId = (int) getIntent().getSerializableExtra(Pixray.EXTRA_PROJECT_ID);
        Log.d(TAG, "project id " + mProjectId);
        mPlateId = (int) getIntent().getSerializableExtra(Pixray.EXTRA_PLATE_ID);
        Log.d(TAG, mPlateId + " was clicked.");

        if (Pixray.checkNetworkConnectivity(mAppContext)) { // check if network is available
            // Initialize dataset, this data will come from a remote server.
            initDataset();
        } else {
            Toast.makeText(this, R.string.network_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    private void createFragment() {
        //Log.d(TAG, "gallery json in activity:" + mImageGalleryJson);
        mGalleryInfo = new GalleryInfo(mProjectId, mPlateId, mDates, mDateIds, mRequestDateId,
                    mTypeIds, mTypes, mRequestTypeId);
        FragmentManager fm = this.getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = ImageGalleryFragment.newInstance(mGalleryInfo, mImageGalleryJson);
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }

    private void replaceFragment() {
        Log.d(TAG, "going to replace fragment, dateid is " + mRequestDateId + ", typeid is " + mRequestTypeId);
        ImageGalleryFragment newFragment = ImageGalleryFragment.newInstance(mGalleryInfo,
                mImageGalleryJson);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragmentContainer, newFragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    private void initDataset() {
        String url = Urls.getUrlSinglePlate(mProjectId, mPlateId);
        PixrayAPI.DownloadJson(new PixrayAPICallback() {
            @Override
            public void callback(JSONObject response) {
                buildSinglePlate(response);
            }
        }, mAppContext, url);
    }

    private void buildSinglePlate(JSONObject response) {
        //Log.i(TAG, "Received SinglePlate json: " + response.toString());
        if (mRequestDateId == -1) {
            mRequestDateId = Pixray.getDefaultDateId(response);
        }
        Log.d(TAG, "date id in DownloadSinglePlateJson is " + mRequestDateId);
        mDates = Pixray.getDates(response);
        mDateIds = Pixray.getDateIds(response);
        String url = Urls.getUrlImageGallery(mProjectId, mPlateId, mRequestDateId);
        Log.i(TAG, "photos of requested date url: " + url);

        PixrayAPI.DownloadJson(new PixrayAPICallback() {
            @Override
            public void callback(JSONObject response) {
                buildImageGallery(response);
            }
        }, mAppContext, url);
    }

    private void buildImageGallery(JSONObject response) {
        mImageGalleryJson = response.toString();
        //Log.d(TAG, "Recieved gallery json:" + response.toString());
        mTypes = Pixray.getTypes(response);
        mTypeIds = Pixray.getTypeIds(response);
        if (mReplaceFragment) {
            replaceFragment();
        } else {
            createFragment();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected is called");
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home: // navigate back
                Intent intent = new Intent(this, PlateListActivity.class);
                intent.putExtra(Pixray.EXTRA_PROJECT_ID, mProjectId);
                startActivity(intent);
                finish();
                return true;
            case R.id.action_date:
                return false; // not implemented here.
            case R.id.action_type:
                return false; // not implemented here.
            case R.id.action_info:
                return false; // not implemented here.
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // The user selected the date/type from the SelectorDialogFragment
    // Do something here to update the image gallery to that date/type.
    public void onDateOrTypeSelected(int requestId, String whichSelector) {
        if (whichSelector.equals(Pixray.DATE_SELECTOR)) {
            Log.d(TAG, "date id is " + mRequestDateId + ", user selected " + requestId);
            mGalleryInfo.setRequestDateId(requestId);
            mRequestDateId = requestId;
        } else {
            Log.d(TAG, "type id is " + mRequestTypeId + ", user selected " + requestId);
            mGalleryInfo.setRequestTypeId(requestId);
            mRequestTypeId = requestId;
        }

        ImageGalleryFragment imageGalleryFrag = (ImageGalleryFragment)
                getFragmentManager().findFragmentById(R.id.gallery_recycler_view);

        if (imageGalleryFrag != null) {
            // If imageGalleryfrag is available, we're in two-pane layout...
            // Call a method in the ImageGalleryFragment to update its content
            Log.d(TAG, "update gallery view");
            //imageGalleryFrag.updateGalleryView(requestDateId); TODO: fix this
        } else {
            // Otherwise, we're in the one-pane layout and must swap frags...
            // Create fragment and give it an argument for the selected date id
            Log.d(TAG, "create new fragment");
            String url = Urls.getUrlImageGallery(mProjectId, mPlateId, mRequestDateId);
            mReplaceFragment = true;
            // download data and replace the old fragment
            PixrayAPI.DownloadJson(new PixrayAPICallback() {
                @Override
                public void callback(JSONObject response) {
                    buildImageGallery(response);
                }
            }, mAppContext, url);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) { // Save data across rotation
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(Pixray.EXTRA_GALLERY_DATE, mRequestDateId);
        savedInstanceState.putInt(Pixray.EXTRA_GALLERY_TYPE, mRequestTypeId);
    }

}