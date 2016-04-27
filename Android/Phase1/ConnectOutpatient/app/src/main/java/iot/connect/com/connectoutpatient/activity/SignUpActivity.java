package iot.connect.com.connectoutpatient.activity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.activity.doctor.DoctorDashboardActivity;
import iot.connect.com.connectoutpatient.activity.patient.PatientDashboardActivity;
import iot.connect.com.connectoutpatient.gcm.RegisterToken;
import iot.connect.com.connectoutpatient.modals.SignIn;
import iot.connect.com.connectoutpatient.modals.SignUp;
import iot.connect.com.connectoutpatient.utils.AppBaseURL;
import iot.connect.com.connectoutpatient.utils.AppStatus;
import iot.connect.com.connectoutpatient.utils.Validator;

/**
 * Created by Deep on 10-Apr-16.
 */
public class SignUpActivity extends AppCompatActivity {
    EditText email, password, confirmPass, FirstName, LastName;
    Button SignUp;
    RadioGroup userType;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        sharedpreferences = getSharedPreferences("ConnectIoT", getApplicationContext().MODE_PRIVATE);
        // Edit text reference
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        confirmPass = (EditText) findViewById(R.id.input_password_confirm);
        FirstName = (EditText) findViewById(R.id.input_firstName);
        LastName = (EditText) findViewById(R.id.input_lastName);
        // Radio Button Group reference
        userType = (RadioGroup) findViewById(R.id.UserTypeRG);
        // Button Reference
        SignUp = (Button) findViewById(R.id.btn_SignUP);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FName = FirstName.getText().toString();
                String LName = LastName.getText().toString();
                String emailid = email.getText().toString();


                if (!Validator.isValidEmail(emailid)) {
                    email.setError("Invalid Email");
                }
                String pass = password.getText().toString();
                if (!Validator.isValidPassword(pass)) {
                    password.setError("Password should contain a-z,A-Z,0-9,symbol");
                }
                String confPass = confirmPass.getText().toString();
                if (!Validator.isValidConfirmPassword(pass, confPass)) {
                    confirmPass.setError("Password not matching");
                }
                String RadioSelection = new String();
                final ArrayList<String> userlist = new ArrayList<String>();
                if (userType.getCheckedRadioButtonId() != -1) {
                    int id = userType.getCheckedRadioButtonId();
                    View radioButton = userType.findViewById(id);
                    int radioId = userType.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) userType.getChildAt(radioId);
                    RadioSelection = (String) btn.getText();
                    userlist.add(RadioSelection.toLowerCase());

                }
                Log.d("Set Objects", " Set Objects 1");
                // Set object
                final SignUp signUp = new SignUp();
                signUp.setEmail(emailid);
                signUp.setUsername(emailid);
                signUp.setFirstName(FName);
                signUp.setLastName(LName);
                signUp.setPassword(pass);
                signUp.setRoles(userlist);
                JSONObject js = null;
                //js.
                Log.d("Set Objects", " Set Objects 2");
                Gson gs = new Gson();
                Log.d("gson to object", gs.toJson(signUp));
                try {
                    js = new JSONObject(gs.toJson(signUp));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // Register User
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {


                    String url = AppBaseURL.BaseURL + "api/auth/signup";
                    JSONObject jsonObject;
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                            js, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d("Response", jsonObject.toString());
                            Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_SHORT).show();
                            SignIn signIn = new SignIn();
                            Gson gs = new Gson();
                            String msg = new String();
                            try {
                                msg = jsonObject.getString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (msg.matches("Success")) {
                                try {
                                    signIn = gs.fromJson(jsonObject.getJSONObject("data").toString(), SignIn.class);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String role = signIn.getRoles().get(0).toString();
                                String username = signIn.getUsername();
                                String profilepic = AppBaseURL.BaseURL + signIn.getProfileImageURL();
                                String email = signIn.getEmail();
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("role", role);
                                editor.putString("username", username);
                                editor.putString("profilepic", profilepic);
                                editor.putString("email", email);
                                editor.putString("LoggedIn", "true");
                                editor.commit();
                                RegisterToken rt = new RegisterToken();
                                rt.register(getApplicationContext());
                                Log.d("Registration Complete", "yea.!");
                                if (role.matches("patient")) {
                                    Intent i = new Intent(getApplicationContext(), PatientDashboardActivity.class);
                                    startActivity(i);
                                } else if (role.matches("doctor")) {
                                    Intent i = new Intent(getApplicationContext(), DoctorDashboardActivity.class);
                                    startActivity(i);
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", error.toString());
                        }
                    }
                    ) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json; charset=utf-8");
                            return headers;
                        }
                    };
                    Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
                } else {
                    // If no network connectivity notify user
                    Toast.makeText(getApplicationContext(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
