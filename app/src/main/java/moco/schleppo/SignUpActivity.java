package moco.schleppo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by soere on 05.12.2016.
 */

public class SignUpActivity  extends AppCompatActivity {

    Button btnSignUp;
    ParseUser parseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        parseUser = new ParseUser();

        setClickListener();
    }

    private void setClickListener () {

        btnSignUp = (Button) findViewById(R.id.buttonSignUp_signUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username = (EditText) findViewById(R.id.regUsernameInputText);
                EditText name = (EditText) findViewById(R.id.regNameInputText);
                EditText forename = (EditText) findViewById(R.id.regSurnameInputText);
                EditText email = (EditText) findViewById(R.id.regMailInputText);
                EditText licenseNumber = (EditText) findViewById(R.id.regLicencePlateInputText);
                EditText password1 = (EditText) findViewById(R.id.regPassword1InputText);
                EditText password2 = (EditText) findViewById(R.id.regPassword2InputText);

                if(password1.getText().toString().compareTo(password2.getText().toString()) == 0) {
                    parseUser.setUsername(username.getText().toString());
                    parseUser.setEmail(email.getText().toString());
                    parseUser.setPassword(password1.getText().toString());
                    parseUser.put("name", name.getText().toString());
                    parseUser.put("forename", forename.getText().toString());
                    parseUser.put("licenseNumber", licenseNumber.getText().toString());

                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                finish();
                            } else {
                                Log.d("SignUp", e.getMessage());
                            }
                        }
                    });
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Passwörter stimmen nicht überein!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });
    }
}
