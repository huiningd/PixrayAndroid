package fi.guagua.pixrayandroid.activities;

import android.app.Fragment;

import fi.guagua.pixrayandroid.fragments.ProjectListFragment;

public class ProjectListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new ProjectListFragment();
    }
}
