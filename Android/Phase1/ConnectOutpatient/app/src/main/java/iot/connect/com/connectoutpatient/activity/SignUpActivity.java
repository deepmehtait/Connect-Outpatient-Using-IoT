package iot.connect.com.connectoutpatient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.activity.doctor.DoctorDashboardActivity;
import iot.connect.com.connectoutpatient.activity.patient.PatientDashboardActivity;
import iot.connect.com.connectoutpatient.utils.Validator;
import iot.connect.com.connectoutpatient.modals.SignUp;

/**
 * Created by Deep on 10-Apr-16.
 */
public class SignUpActivity extends AppCompatActivity {
    EditText email,password,confirmPass,FirstName,LastName;
    Button SignUp;
    RadioGroup userType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Edit text reference
        email=(EditText)findViewById(R.id.input_email);
        password=(EditText)findViewById(R.id.input_password);
        confirmPass=(EditText)findViewById(R.id.input_password_confirm);
        FirstName=(EditText)findViewById(R.id.input_firstName);
        LastName=(EditText)findViewById(R.id.input_lastName);
        // Radio Button Group reference
        userType=(RadioGroup)findViewById(R.id.UserTypeRG);
        // Button Reference
        SignUp=(Button)findViewById(R.id.btn_SignUP);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FName=FirstName.getText().toString();
                String LName=LastName.getText().toString();
                String emailid=email.getText().toString();


                if(!Validator.isValidEmail(emailid)){
                    email.setError("Invalid Email");
                }
                String pass=password.getText().toString();
                if(!Validator.isValidPassword(pass)){
                    password.setError("Password should contain a-z,A-Z,0-9,symbol");
                }
                String confPass=confirmPass.getText().toString();
                if(!Validator.isValidConfirmPassword(pass,confPass)){
                    confirmPass.setError("Password not matching");
                }
                String RadioSelection = new String();
                final ArrayList<String> userlist=new ArrayList<String>();
                if(userType.getCheckedRadioButtonId()!=-1){
                    int id=userType.getCheckedRadioButtonId();
                    View radioButton=userType.findViewById(id);
                    int radioId = userType.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) userType.getChildAt(radioId);
                    RadioSelection = (String) btn.getText();
                    userlist.add(RadioSelection.toLowerCase());

                }
                Log.d("Set Objects"," Set Objects 1");
                // Set object
                final SignUp signUp=new SignUp();
                signUp.setEmail(emailid);
                signUp.setUsername(emailid);
                signUp.setFirstName(FName);
                signUp.setLastName(LName);
                signUp.setPassword(pass);
                signUp.setRoles(userlist);
                JSONObject js=null;
                //js.
                Log.d("Set Objects"," Set Objects 2");
                Gson gs=new Gson();
                Log.d("gson to object",gs.toJson(signUp));
                try{
                     js=new JSONObject(gs.toJson(signUp));
                }catch (JSONException e){
                    e.printStackTrace();
                }

                //String s1=gs.toJson(signUp);
                //Log.d("Request in String",s1);
                //JsonParser parser=new JsonParser();
                //JSONObject js=gs.fromJson(s1,JSONObject.class);
                //Log.d("JSONOBJECT", js.toString());

                // Register User
               String url="http://ec2-54-67-123-247.us-west-1.compute.amazonaws.com/api/auth/signup";
                JSONObject jsonObject;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                        js, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("Response", jsonObject.toString());
                        Toast.makeText(getApplicationContext(),jsonObject.toString(),Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.d("Error.Response", error.getMessage());
                    }
                }
                );
                Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
                /* StringRequest postRequest=new StringRequest(Request.Method.POST, url, new JSONObject(),new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String site = jsonResponse.toString();
                            Toast.makeText(getApplicationContext(),site,Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Log.d("GetParams="," in Params 1");
                        HashMap<String,String> params = new HashMap<String, String>();
                        // the POST parameters:
                        Log.d("GetParams="," "+signUp.toString());
                        params.put("",signUp.toString());
                        Log.d("GetParams="," in Params 2");
                        return params;
                    }
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }
                };



                Volley.newRequestQueue(getApplicationContext()).add(postRequest);
*/





            }
        });
    }
}
