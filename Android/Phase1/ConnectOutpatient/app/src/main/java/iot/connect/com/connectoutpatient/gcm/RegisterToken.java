package iot.connect.com.connectoutpatient.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import iot.connect.com.connectoutpatient.utils.AppBaseURL;

/**
 * Created by Deep on 24-Apr-16.
 */
public class RegisterToken {
    SharedPreferences sharedpreferences;

    public void register(final Context context){
        //Register Token
        sharedpreferences = context.getSharedPreferences("ConnectIoT", context.MODE_PRIVATE);
        String username=sharedpreferences.getString("username","");
        String UUID=sharedpreferences.getString("UUID","null");
        String token=sharedpreferences.getString("token","null");
        JSONObject obj=new JSONObject();
        try{
            obj.put("username",username);
            obj.put("uuid",UUID);
            obj.put("token",token);

        }catch(JSONException e){
            e.printStackTrace();
        }
        Log.d("Request=",obj.toString());
        String url= AppBaseURL.BaseURL+"deviceToken";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("Response", jsonObject.toString());
                Toast.makeText(context,jsonObject.toString(),Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.getMessage());
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }
}
