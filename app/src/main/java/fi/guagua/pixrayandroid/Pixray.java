package fi.guagua.pixrayandroid;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Pixray {

    public static final String TAG = "PIXRAY";
    public static final String EXTRA_PROJECT_ID = "fi.guagua.pixrayandroid.project_id";
    public static final String EXTRA_PLATE_ID = "fi.guagua.pixrayandroid.plate_id";
    public static final String EXTRA_IMAGE_URL = "fi.guagua.pixrayandroid.image_url";
    public static final String EXTRA_IMAGE_CAPTION = "fi.guagua.pixrayandroid.image_caption";
    public static final String EXTRA_DATA_NAMES = "fi.guagua.pixrayandroid.data_names";
    public static final String EXTRA_DATA_IDS = "fi.guagua.pixrayandroid.data_ids";
    public static final String EXTRA_WHICH_SELECTOR = "fi.guagua.pixrayandroid.which_selector";
    public static final String EXTRA_IMAGE = "fi.guagua.pixrayandroid.image";
    public static final String EXTRA_GALLERY_INFO = "fi.guagua.pixrayandroid.gallery_info";
    public static final String EXTRA_GALLERY_JSON = "fi.guagua.pixrayandroid.gallery_json";
    public static final String EXTRA_GALLERY_DATE = "fi.guagua.pixrayandroid.gallery_date";
    public static final String EXTRA_GALLERY_TYPE = "fi.guagua.pixrayandroid.gallery_type";
    public static final String EXTRA_CURRENT_SCORE = "fi.guagua.pixrayandroid.current_score";
    public static final String EXTRA_SCORE_TYPES = "fi.guagua.pixrayandroid.score_types";
    public static final String EXTRA_WC = "fi.guagua.pixrayandroid.well_conditions";

    public static final String TYPE_SELECTOR = "type_selector";
    public static final String DATE_SELECTOR = "date_selector";
    public static final String INFO_DIALOG = "info_dialog";
    public static final String NEW_SCORE_DIALOG = "new_score_dialog";
    public static final String WELL_CONDITIONS_DIALOG = "well_conditions_dialog";
    //public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss Z");

    // images have at most 8 rows, each row takes an init char.
    public static final String[] IMAGE_INIT = {"A", "B", "C", "D", "E", "F", "G", "H"};

    public static final String JSON_ID = "id";
    public static final String JSON_NAME = "name";
    private static final String JSON_DATES = "dates";
    private static final String JSON_DATE = "date";
    private static final String JSON_DATE_ID = "date_id";
    private static final String JSON_TYPES = "types";
    private static final String JSON_TYPE = "type";
    public static final String JSON_SCORE_TYPES = "score_types";
    public static final String JSON_SCORE = "score";
    public static final String JSON_COLOR = "color";
    public static final String JSON_SAMPLE = "sample";
    public static final String JSON_SCREEN_NAME = "screen_name";
    public static final String JSON_SCREEN = "screen";
    public static final String JSON_CLASS = "class";
    public static final String JSON_CONCENTRATION = "concentration";
    public static final String JSON_UNITS = "units";
    public static final String JSON_PH = "ph";

    public Pixray () {
        // empty constructor
    }

    public static boolean checkNetworkConnectivity(Context appContext) {
        ConnectivityManager connMgr = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    // In order to use toolbar for Android API level under 21, need to use ActionBarActivity and setSupportActionBar().
    public static void setToolBar(View v, ActionBarActivity activity, int resId) {
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.app_bar);
        activity.setSupportActionBar(toolbar);
        if (NavUtils.getParentActivityName(activity) != null) { // no parent, no caret
            activity.getSupportActionBar().setHomeButtonEnabled(true);
            // show "go back" caret on action bar.
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setTitle(resId);
    }

    public static int getDefaultDateId(JSONObject response) {
        int dateId = -1;
        try {
            JSONArray array = response.getJSONArray(JSON_DATES);
            // get the last item in JSONArray, use it as default date
            dateId = array.getJSONObject(array.length() - 1).getInt(JSON_DATE_ID);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to get default date id!", e);
            e.printStackTrace();
        }
        return dateId;
    }

    public static int getDefaultTypeId(JSONObject response) { // TODO: comeplete this method
        return  0;
    }

    public static ArrayList<String> getDates(JSONObject response) {
        ArrayList<String> dates = new ArrayList<>();
        try {
            JSONArray array = response.getJSONArray(JSON_DATES);
            for (int i = 0; i < array.length(); i++) {
                String date = array.getJSONObject(i).getString(JSON_DATE); // example: "date":"2012-10-10 14:51:50 +0300"
                date = date.substring(0, date.indexOf('+')); // trim the string to drop "+0300"
                dates.add(date);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Failed to get dates!", e);
            e.printStackTrace();
        }
        return dates;
    }

    public static ArrayList<Integer> getDateIds(JSONObject response) {
        ArrayList<Integer> date_ids = new ArrayList<>();
        try {
            JSONArray array = response.getJSONArray(JSON_DATES);
            for (int i = 0; i < array.length(); i++) {
                int date_id = array.getJSONObject(i).getInt(JSON_DATE_ID);
                date_ids.add(date_id);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Failed to get date ids!", e);
            e.printStackTrace();
        }
        return date_ids;
    }

    public static ArrayList<String> getTypes(JSONObject response) {
        ArrayList<String> types = new ArrayList<>();
        try {
            JSONArray array = response.getJSONArray(JSON_TYPES);
            for (int i = 0; i < array.length(); i++) {
                String type = array.getJSONObject(i).getString(JSON_TYPE);
                types.add(type);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Failed to get types!", e);
            e.printStackTrace();
        }
        return types;
    }

    public static ArrayList<Integer> getTypeIds(JSONObject response) {
        ArrayList<Integer> type_ids = new ArrayList<>();
        try {
            JSONArray array = response.getJSONArray(JSON_TYPES);
            for (int i = 0; i < array.length(); i++) {
                int type_id = array.getJSONObject(i).getInt(JSON_ID);
                type_ids.add(type_id);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Failed to get type ids!", e);
            e.printStackTrace();
        }
        return type_ids;
    }

    public static String getImageLabel(int[] rowColDrop) {
        int row = rowColDrop[0];
        int col = rowColDrop[1];
        int drop = rowColDrop[2];
        return Pixray.IMAGE_INIT[row] + (col+1) + "." + (drop+1); // row 7, col 11, drop 2 -> H12.3
    }

    public static ArrayList<int[]> getImageRowColDropList(int rows, int cols, int drops) {
        ArrayList<int[]> rowColDropList = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < drops; k++) {
                    int[] rowColDrop = new int[3];
                    rowColDrop[0] = i; // row
                    rowColDrop[1] = j; // column
                    rowColDrop[2] = k; // drop
                    //Log.d(TAG, " row is " + i + ", col is " + j + ", drop is " + k);
                    rowColDropList.add(rowColDrop);
                }
            }
        }
        return rowColDropList;
    }

    public static String getScoreName(ScoreTypes scoreTypes, int scoreId){
        if (scoreTypes == null) {Log.e(TAG, "score types is null");}
        ArrayList<Integer> ids = scoreTypes.getIds();
        //Log.e(TAG, "score size is " + ids.size());
        ArrayList<String> names = scoreTypes.getNames();
        for (int i=0; i<ids.size(); i++) {
            if (scoreId == ids.get(i)) return names.get(i);
        }
        return null;
    }

    public static String getScoreColor(ScoreTypes scoreTypes, int scoreId){
        ArrayList<Integer> ids = scoreTypes.getIds();
        ArrayList<String> colors = scoreTypes.getColors();
        for (int i=0; i<ids.size(); i++) {
            if (scoreId == ids.get(i)) return colors.get(i);
        }
        return null;
    }

}
