package fi.guagua.pixrayandroid.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fi.guagua.pixrayandroid.utils.Pixray;
import fi.guagua.pixrayandroid.R;
import fi.guagua.pixrayandroid.utils.Urls;
import fi.guagua.pixrayandroid.activities.PlateListActivity;
import fi.guagua.pixrayandroid.models.Project;
import fi.guagua.pixrayandroid.network.PixrayAPI;
import fi.guagua.pixrayandroid.network.PixrayAPICallback;

public class ProjectListFragment extends Fragment {

    private static final String TAG = "ProjectListFragment";
    private View mRootView;
    private ArrayList<Project> mProjects = new ArrayList<>();
    private ProjectAdapter mAdapter;
    private Context mAppContext;
    private ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_simple_list, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAppContext = getActivity().getApplicationContext();

        Pixray.setToolBar(mRootView, (AppCompatActivity) getActivity(), R.string.project_list);
        final ListView listView = (ListView) mRootView.findViewById(R.id.listView);
        mAdapter = new ProjectAdapter(mProjects);
        listView.setAdapter(mAdapter);

        // show loading progress
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading project list.");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        if (Pixray.checkNetworkConnectivity(mAppContext)) {
            mProgressDialog.show();
            // Initialize dataset, this data will come from a remote server.
            initDataset();
        } else {
            Toast.makeText(getActivity(), R.string.err_network_unavailable, Toast.LENGTH_LONG).show();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Project p = (Project) listView.getItemAtPosition(position);
                Log.d(TAG, p.getName() + " with id " + p.getId() + " was clicked");

                // start PlateListActivity
                Intent i = new Intent(getActivity(), PlateListActivity.class);
                i.putExtra(Pixray.EXTRA_PROJECT_ID, p.getId());
                startActivity(i);
            }
        });
    }

    private void initDataset() {
        String url = Urls.getUrlProjects();
        PixrayAPI.DownloadJson(new PixrayAPICallback() {
            @Override
            public void callback(JSONObject response) {
                buildProjectList(response);
            }
        }, mAppContext, url);
    }

    private class ProjectAdapter extends ArrayAdapter<Project> {

        public ProjectAdapter(ArrayList<Project> projects) {
            super(getActivity(), android.R.layout.simple_list_item_1, projects);
        }
    }

    // Build the project list based on the downloaded data.
    private void buildProjectList(JSONObject response) {
        try {
            JSONArray array = response.getJSONArray("projects");
            // Build the array of projects from JSONObjects
            for (int i = 0; i < array.length(); i++) {
                JSONObject singleProject = array.getJSONObject(i);
                int id = singleProject.getInt(Pixray.JSON_ID);
                String name = singleProject.getString(Pixray.JSON_NAME);
                mProjects.add(new Project(id, name));
                //for (Project p : mProjects) { System.out.println(p.toString()); } // debug
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        mProgressDialog.cancel();
        mAdapter.notifyDataSetChanged();
    }

}
