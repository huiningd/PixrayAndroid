package fi.guagua.pixrayandroid.activities;

import android.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;

import fi.guagua.pixrayandroid.utils.Pixray;
import fi.guagua.pixrayandroid.fragments.PlateListFragment;

public class PlateListActivity extends SingleFragmentActivity {
    private static final String TAG = "PlateListActivity";

    @Override
    protected Fragment createFragment() {
        int projectId = (int)getIntent().getSerializableExtra(Pixray.EXTRA_PROJECT_ID);
        Log.d("PlateListActivity", projectId + " was clicked, now create fragment.");
        return PlateListFragment.newInstance(projectId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected is called");
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(this) != null) {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

