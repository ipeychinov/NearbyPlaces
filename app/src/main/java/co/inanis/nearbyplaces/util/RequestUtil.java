package co.inanis.nearbyplaces.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import co.inanis.nearbyplaces.R;

public class RequestUtil {
    private static RequestUtil mInstance;
    private static Context mContext;

    private RequestQueue mRequestQueue;

    private RequestUtil(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized RequestUtil getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestUtil(context);
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    public static JsonObjectRequest buildNearbyPlacesRequest(double lat, double lon, int radius, String type, Response.Listener<JSONObject> onSuccessCallback, Response.ErrorListener onErrorCallback) {
        String apiKey = mContext.getString(R.string.places_api_key);
        String url = mContext.getString(R.string.url_template, lat, lon, radius, type, apiKey);
        return new JsonObjectRequest(Request.Method.GET, url, null, onSuccessCallback, onErrorCallback);
    }
}
