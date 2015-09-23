package fi.guagua.pixrayandroid.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import fi.guagua.pixrayandroid.utils.Utility;

/**
 * Every project contains multiple plates.
 */
public class Plate {

    private static final String TAG = Plate.class.getSimpleName();
    private int mProjectId;
    private int mPlateId;
    private String mName;

    public Plate() {
    }

    public Plate(int projectId, JSONObject json) throws JSONException {
        if (json != null) {
            mProjectId = projectId;
            mPlateId = json.getInt(Utility.JSON_ID);
            mName = json.getString(Utility.JSON_NAME);
        } else {
            Log.e(TAG, "JSON object missing.");
        }
    }

    public String getName() {
        return mName;
    }

    public int getProjectId() {
        return mProjectId;
    }

    public int getPlateId() {
        return mPlateId;
    }

    public String toString() {
        return mName;
    }
}
