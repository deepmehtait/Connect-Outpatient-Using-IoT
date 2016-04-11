package iot.connect.com.connectoutpatient.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.utils.Validator;

/**
 * Created by Deep on 10-Apr-16.
 */
public class SignUpActivity extends AppCompatActivity {
    EditText email,password,confirmPass,FirstName,LastName;
    Button SignUp;
    RadioGroup UserType;

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
        UserType=(RadioGroup)findViewById(R.id.UserTypeRG);
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
                    password.setError("Invalid Password");
                }
                String confPass=confirmPass.getText().toString();
                if(!Validator.isValidConfirmPassword(pass,confPass)){
                    confirmPass.setError("Password not matching");
                }



            }
        });
    }
}
