package fi.guagua.pixrayandroid;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Every project contains multiple plates.
 */
public class Plate {

    private static final String TAG = "Plate";
    private int mProjectId;
    private int mPlateId;
    private String mName;
    private int mDrops;
    private Date mCreated;
    private Date mImaged;
    private String mDriver;
    private String mBarcode;

    public Plate() {
    }

    public Plate(int projectId, JSONObject json) throws JSONException {
        if (json != null) {
            mProjectId = projectId;
            mPlateId = json.getInt(Pixray.JSON_ID);
            mName = json.getString(Pixray.JSON_NAME);
        } else {
            Log.i(TAG, "JSON object missing.");
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
