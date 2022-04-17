package VolleyAPI;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by tyler on 9/23/17.
 * Used as a utility for server calls
 */

public class RequestQueueSingleton {
    private static RequestQueueSingleton oInstance;
    private RequestQueue oRequestQueue;
    private static Context oCtx;

    private RequestQueueSingleton(Context context){
        oCtx = context;
        oRequestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue(){
        if(oRequestQueue == null){
            oRequestQueue = Volley.newRequestQueue(oCtx.getApplicationContext());
        }
        return oRequestQueue;
    }

    public static synchronized RequestQueueSingleton getInstance(Context context){
        if(oInstance == null){
            oInstance = new RequestQueueSingleton(context);
        }
        return oInstance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
