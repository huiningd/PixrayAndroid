package fi.guagua.pixrayandroid;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class PixrayAPI {

    private static final String TAG = "PixrayAPI";

    public static void DownloadJson(final PixrayAPICallback callbackObject,
                                    final Context mAppContext, final String url) {
        VolleySingleton volleySingleton = VolleySingleton.getInstance(mAppContext);
        RequestQueue requestQueue = volleySingleton.getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                (String)null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.i(TAG, "Received json: " + response.toString());
                        callbackObject.callback(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Failed to fetch JSON from remote server!", error);
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Toast.makeText(mAppContext, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);
    }

}
