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

    // Download project list from remote server.
    public static void DownloadProjectsJson(final PixrayAPICallback projectList, final Context mAppContext) {
        VolleySingleton volleySingleton = VolleySingleton.getInstance(mAppContext);
        RequestQueue requestQueue = volleySingleton.getRequestQueue();
        String url = Urls.getUrlProjects();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                (String)null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.i(TAG, "Received json: " + response.toString());
                        projectList.callback(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Failed to fetch project list!", error);
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Toast.makeText(mAppContext, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);
    }

    public static void DownloadPlatesJson(final PixrayAPICallback plateList, final Context mAppContext,
                                          final int projectId) {
        VolleySingleton volleySingleton = VolleySingleton.getInstance(mAppContext);
        RequestQueue requestQueue = volleySingleton.getRequestQueue();
        String url = Urls.getUrlPlates(projectId);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                (String)null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Received json: " + response.toString());
                        plateList.callback(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Failed to fetch plate list!", error);
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Toast.makeText(mAppContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(request);
    }

    public static  void DownloadSinglePlateJson(final PixrayAPICallback imageGallery, final Context mAppContext,
                                                final int projectId, final int plateId) {
        VolleySingleton volleySingleton = VolleySingleton.getInstance(mAppContext);
        RequestQueue requestQueue = volleySingleton.getRequestQueue();
        String urlSinglePlate = Urls.getUrlSinglePlate(projectId, plateId);
        Log.i(TAG, "DownloadSinglePlateJson single plate url: " + urlSinglePlate);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                urlSinglePlate,
                (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        imageGallery.callback(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Failed to fetch plate JSON!", error);
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Toast.makeText(mAppContext, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);
    }

    public static void DownloadImageGalleryJson(final PixrayAPICallback imageGallery, final Context mAppContext,
                                                final String urlImagesOfDefaultDate) {
        VolleySingleton volleySingleton = VolleySingleton.getInstance(mAppContext);
        RequestQueue requestQueue = volleySingleton.getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                urlImagesOfDefaultDate,
                (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        imageGallery.callback(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Failed to fetch PhotoGallery JSON!", error);
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Toast.makeText(mAppContext, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);
    }

    public static void DownloadScoreTypesJson(final PixrayAPICallback imageFragment, final Context mAppContext) {
        VolleySingleton volleySingleton = VolleySingleton.getInstance(mAppContext);
        RequestQueue requestQueue = volleySingleton.getRequestQueue();
        String url = Urls.getUrlScoreTypes();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                (String)null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.i(TAG, "Received json: " + response.toString());
                        imageFragment.callback(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Failed to fetch project list!", error);
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Toast.makeText(mAppContext, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(request);
    }

    public static void DownloadSampleScreenAndScore(final PixrayAPICallback imageFragment, final Context mAppContext,
                                                    final Image image) {
        VolleySingleton volleySingleton = VolleySingleton.getInstance(mAppContext);
        RequestQueue requestQueue = volleySingleton.getRequestQueue();
        String url = Urls.getUrlSampleScreenScore(image);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                (String)null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.i(TAG, "Received json: " + response.toString());
                        imageFragment.callback(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Failed to fetch project list!", error);
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Toast.makeText(mAppContext, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(request);
    }

}
