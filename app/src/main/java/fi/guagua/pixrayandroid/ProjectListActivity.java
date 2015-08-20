package fi.guagua.pixrayandroid;

import android.app.Fragment;

public class ProjectListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new ProjectListFragment();
    }
}
