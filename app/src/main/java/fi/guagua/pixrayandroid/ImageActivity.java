package fi.guagua.pixrayandroid;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class ImageActivity extends ActionBarActivity implements
        SelectorDialogFragment.OnDateOrTypeSelectedListener,
        ChooseNewScoreDialog.OnScoreSelectedListener {

    private static final String TAG = "ImageActivity";
    private Image mImage;
    private int mProjectId;
    private int mPlateId;
    boolean mReplaceFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        mImage = (Image) getIntent().getSerializableExtra(Pixray.EXTRA_IMAGE);
        mProjectId = mImage.getGalleryInfo().getProjectId();
        mPlateId = mImage.getGalleryInfo().getPlateId();

        createFragment();
    }

    private Fragment createFragment() {
        Log.d("ImageActivity", mImage.getLabel() + " was clicked, now create fragment.");
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = ImageFragment.newInstance(mImage);
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
        return fragment;
    }

    private void replaceFragment() {
        ImageFragment newFragment = ImageFragment.newInstance(mImage);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragmentContainer, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected is called");
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, ImageGalleryActivity.class);
                intent.putExtra(Pixray.EXTRA_PROJECT_ID, mProjectId);
                intent.putExtra(Pixray.EXTRA_PLATE_ID, mPlateId);
                startActivity(intent);
                finish();
                return true;
            case R.id.action_date:
                return false; // not implement here
            case R.id.action_type:
                return false; // not implement here.
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // The user selected the date/type from the SelectorDialogFragment
    // Do something here to update the image gallery to that date/type.
    public void onDateOrTypeSelected(int id, String whichSelector) {
        GalleryInfo info = mImage.getGalleryInfo();
        String urlThumbnail = mImage.getThumbnailUrl();

        // If date/type is changed, urls of thumbnail-image and large-image both change.
        if (whichSelector.equals(Pixray.DATE_SELECTOR)) {
            info.setRequestDateId(id);
            // Generate new url of thumbnail-image.
            String newThumbUrl = Urls.newUrlThumbnailDateChanged(urlThumbnail, id);
            mImage.setThumbnailUrl(newThumbUrl);
            //Log.i(TAG,"date changed, new thumbnail url is " + newThumbUrl); // debug
            // Generate new url of large-image.
            String newImageUrl = Urls.getLargeImageUrl(newThumbUrl);
            mImage.setLargeImageUrl(newImageUrl);
            //Log.i(TAG, "date changed, new image url is " + newImageUrl);
        } else {
            info.setRequestTypeId(id);
            // Generate new url of thumbnail-image.
            String newThumbUrl = Urls.newUrlThumbnailTypeChanged(urlThumbnail, id);
            mImage.setThumbnailUrl(newThumbUrl);
            //Log.i(TAG, "type changed, new thumbnail url is " + newThumbUrl);
            // Generate new url of large-image.
            String newImageUrl = Urls.getLargeImageUrl(newThumbUrl);
            mImage.setLargeImageUrl(newImageUrl);
            //Log.i(TAG, "type changed, new image url is " + newImageUrl);
        }

        ImageFragment imageFrag = (ImageFragment) getFragmentManager()
                .findFragmentById(R.id.singleImageAndInfo);

        if (imageFrag != null) {
            // If imageGalleryfrag is available, we're in two-pane layout...
            // Call a method in the ImageGalleryFragment to update its content
            Log.d(TAG, "update image view");
            //imageGalleryFrag.updateGalleryView(requestDateId); TODO: fix this
        } else {
            // Otherwise, we're in the one-pane layout and must swap frags...
            // Create fragment and give it an argument for the selected date id
            Log.d(TAG, "create new fragment");
            mReplaceFragment = true;
            replaceFragment(); // replace the old fragment
        }
    }

    public void onNewScoreSelected(int scoreId) {
        Toast.makeText(this, "You have selected score with ID " + scoreId, Toast.LENGTH_SHORT).show();
        ImageFragment imageFrag = (ImageFragment) getFragmentManager()
                .findFragmentById(R.id.singleImageAndInfo);

        // Send now score back to server.
        String url = Urls.urlPutNewScore(mImage, scoreId);
        Log.d(TAG, "url PUT new score: " + url);
        PixrayAPI.putDataToServer(getApplicationContext(), url);

        // TODO: update new score in mImage

        if (imageFrag != null) {
            // If imageGalleryfrag is available, we're in two-pane layout...
            // Call a method in the ImageGalleryFragment to update its content
            Log.d(TAG, "update image view");
            //imageGalleryFrag.updateGalleryView(requestDateId); TODO: fix this
        } else {
            // Otherwise, we're in the one-pane layout and must swap frags...
            // Create fragment and give it an argument for the selected date id
            Log.d(TAG, "create new fragment");
            mReplaceFragment = true;
            replaceFragment(); // replace the old fragment
        }
    }



}
