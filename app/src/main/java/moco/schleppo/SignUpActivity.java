package moco.schleppo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.Locale;
import java.util.regex.Pattern;

import moco.schleppo.fragments.UserManagement;

/**
 * Created by soeren on 05.12.2016.
 */

public class SignUpActivity  extends AppCompatActivity {

    Button btnSignUp;
    ParseUser parseUser;
    ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        parseUser = UserManagement.parseUser;
        currentUser = ParseUser.getCurrentUser();

        setClickListener();
    }

    private void setClickListener () {

        btnSignUp = (Button) findViewById(R.id.buttonSignUp_signUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ((EditText) findViewById(R.id.regUsernameInputText)).getText().toString().trim();
                String name = ((EditText) findViewById(R.id.regNameInputText)).getText().toString();
                String forename = ((EditText) findViewById(R.id.regSurnameInputText)).getText().toString();
                String email = ((EditText) findViewById(R.id.regMailInputText)).getText().toString();
                String licenseNumber = ((EditText) findViewById(R.id.regLicencePlateInputText)).getText().toString().toUpperCase();
                String password1 = ((EditText) findViewById(R.id.regPassword1InputText)).getText().toString();
                String password2 = ((EditText) findViewById(R.id.regPassword2InputText)).getText().toString();

                boolean bla = Locale.getDefault().equals(Locale.GERMANY);
                boolean bla1 = !isLicenseValid(licenseNumber);

                if(username.isEmpty() || name.isEmpty() || forename.isEmpty() || email.isEmpty() || licenseNumber.isEmpty()
                        || password1.isEmpty() || password2.isEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
                    return;
                } else if(Locale.getDefault().equals(Locale.GERMANY) && !isLicenseValid(licenseNumber)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.invalid_license), Toast.LENGTH_SHORT).show();
                    return;
                }else if(!isEmailValid(email)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
                    return;
                } else if(password1.compareTo(password2)!=0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.msg_signup_password_dismatch), Toast.LENGTH_SHORT).show();
                    return;
                }

                parseUser.setUsername(username);
                parseUser.setEmail(email);
                parseUser.setPassword(password1);
                parseUser.put("name", name);
                parseUser.put("forename", forename);
                parseUser.put("licenseNumber", licenseNumber);

                parseUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            UserManagement.parseUser.put("isAnonymous", false);
                            UserManagement.parseUser.saveInBackground( new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    UserManagement.checkUser();
                                }
                            });

                            setResult(Activity.RESULT_OK);
                            finish();
                        } else {
                            Log.d("SignUp", e.getMessage());
                        }
                    }
                });
            }
        });
    }

    // Works only for german license numbers
    private boolean isLicenseValid(String licenseNumber) {
        Pattern p = Pattern.compile( "^[A-Z]{1,3}-[A-Z]{1,2}[0-9]{1,4}" );

        return p.matcher(licenseNumber).find();
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        finishActivity(-1);
    }
}
