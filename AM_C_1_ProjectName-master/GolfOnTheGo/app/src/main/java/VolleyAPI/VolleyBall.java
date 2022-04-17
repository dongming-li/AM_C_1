package VolleyAPI;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Map;

/**
 * Created by Tyler on 9/23/17 volley stuffs.
 * Used to call the volley utility
 */

public class VolleyBall {
    private static String tag_string_request = "string_req";
    private static String tag_Json_request = "json_req";

    /**
     * Used to get a Json object back from the database
     * @param context The context of the method calling this method
     * @param callback Interface that handles actions after the response
     * @param url The url to send the request to, most are found in ConstantURL
     */
    public static void getResponseJson(Context context, final VolleyCallback callback, String url){
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        try{
                            callback.doThings(response);
                        }
                        catch (Exception e){
                            System.out.println(e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString() + "get Response method");
                        VolleyLog.d(tag_Json_request, "Error: " + error.getMessage());
                    }
                });

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    /**
     * Used to get a Json object back from the database
     * @param context The context of the method calling this method
     * @param callback Interface that handles actions after the response
     * @param url The url to send the request to, most are found in ConstantURL
     */
    public static void getResponseJsonArray(Context context, final VolleyCallback callback, String url){
        final JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println(response.toString());
                        try{
                            callback.doThings(response);
                        }
                        catch (Exception e){
                            System.out.println(e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString() + "get Response method");
                        VolleyLog.d(tag_Json_request, "Error: " + error.getMessage());
                    }
                });

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    /**
     * Used to get a String back from the server
     * @param context The context that the method was called from
     * @param callback Interface that executes the jobs the programmer wants done after response
     * @param url The url that the method connects to, most are found in ConstantURL
     */
    public static void getResponseString(Context context, final VolleyCallback callback, String url){
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.doThings(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                        VolleyLog.d(tag_Json_request, "Error: " + error.getMessage());
                    }
        });

        RequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     * Used to run code on response, written by the programmer
     * @param <T> Whatever type of response the use called
     */
    public interface VolleyCallback<T> {
        void doThings(T result);
    }
}
