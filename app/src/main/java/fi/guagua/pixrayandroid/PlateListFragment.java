package fi.guagua.pixrayandroid;

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

public class PlateListFragment extends Fragment {

    private static final String TAG = "PlateListFragment";
    private View mRootView;
    private ArrayList<Plate> mPlates = new ArrayList<>();
    private PlateAdapter mAdapter;
    private Context mAppContext;
    private int mProjectId;
    private ProgressDialog mProgressDialog;

    public static PlateListFragment newInstance(int projectId) {
        Bundle args = new Bundle();
        args.putInt(Pixray.EXTRA_PROJECT_ID, projectId);
        PlateListFragment fragment = new PlateListFragment();
        fragment.setArguments(args);
        Log.d(TAG, projectId + " platelist fragment is now created.");
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_simple_list, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAppContext = getActivity().getApplicationContext();
        mProjectId = getArguments().getInt(Pixray.EXTRA_PROJECT_ID);

        Pixray.setToolBar(mRootView, (AppCompatActivity)getActivity(), R.string.plate_list);
        final ListView listView = (ListView) mRootView.findViewById(R.id.listView);
        mAdapter = new PlateAdapter(mPlates);
        listView.setAdapter(mAdapter);

        // show loading progress
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading plate list.");
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
                Plate p = (Plate) listView.getItemAtPosition(position);
                Log.d(TAG, p.getName() + " with id " + p.getPlateId() + " was clicked");
                // start ImageGalleryActivity
                Intent i = new Intent(getActivity(), ImageGalleryActivity.class);
                i.putExtra(Pixray.EXTRA_PROJECT_ID, p.getProjectId());
                i.putExtra(Pixray.EXTRA_PLATE_ID, p.getPlateId());
                startActivity(i);
            }
        });
    }

    private void initDataset() {
        String url = Urls.getUrlPlates(mProjectId);
        PixrayAPI.DownloadJson(new PixrayAPICallback() {
            @Override
            public void callback(JSONObject response) {
                buildPlateList(response);
            }
        }, mAppContext, url);
    }

    private class PlateAdapter extends ArrayAdapter<Plate> {
        public PlateAdapter(ArrayList<Plate> plates) {
            super(getActivity(), android.R.layout.simple_list_item_1, plates);
        }
    }

    private void buildPlateList(JSONObject response) {
        try {
            JSONArray array = response.getJSONArray("plates");
            // Build the array of plates from JSONObjects
            for (int i = 0; i < array.length(); i++) {
                mPlates.add(new Plate(mProjectId, array.getJSONObject(i)));
                for (Plate p : mPlates) { // debug
                    System.out.println(p.toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        mProgressDialog.cancel();
        mAdapter.notifyDataSetChanged();
    }

}
