package moco.schleppo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import moco.schleppo.fragments.MapsFragment;
import moco.schleppo.fragments.UserManagement;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    Button btnSignUp;
    Button btnChangePassword;

    static final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setClickListener();
    }

    private void setClickListener () {

        btnLogin = (Button) findViewById(R.id.buttonLogin);
        btnSignUp = (Button) findViewById(R.id.buttonSignUp_Login);
        btnChangePassword = (Button) findViewById(R.id.btnPasswort);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText userName = (EditText) findViewById(R.id.editTextUsername);
                EditText userPassword = (EditText) findViewById(R.id.editTextPassword);
                ParseUser.logInInBackground(userName.getText().toString(), userPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user==null || e!=null) {
                            showExceptionToast(e);
                        } else {
                            setResult(Activity.RESULT_OK);
                            UserManagement.isAnonymousUser = false;
                            UserManagement.parseUser = user;
                            finish();
                        }
                    }
                });
            }
        });
        btnChangePassword.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            showPasswordResetDialog();
            }
        });
        btnSignUp.setOnClickListener(this);
    }

    private void showPasswordResetDialog() {
        final Dialog passwordResetDialog = new Dialog(this);
        passwordResetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        passwordResetDialog.setContentView(R.layout.reset_password_dialog);
        passwordResetDialog.setCancelable(true);

        passwordResetDialog.findViewById(R.id.reset_password_cancel_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        passwordResetDialog.cancel();
                    }
                });

        passwordResetDialog.findViewById(R.id.reset_password_btn)
            .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                EditText emailInput = (EditText)passwordResetDialog.findViewById(R.id.email_input_reset_password);
                String userEmail = emailInput.getText().toString().trim().toLowerCase();

                if(userEmail!=null && userEmail!="") {
                    ParseUser.requestPasswordResetInBackground(userEmail, new RequestPasswordResetCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(getApplicationContext(), getString(R.string.passwordreset_successfull), Toast.LENGTH_SHORT);
                            } else {
                                Log.d("ResetPassword", e.getMessage());
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
                            }
                        }
                    });
                    passwordResetDialog.cancel();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_mail_input), Toast.LENGTH_SHORT);
                }
                }
            });
        passwordResetDialog.show();
    }

    private void showExceptionToast(Exception e) {
        Log.d("Login", e.getMessage());

        Context context = getApplicationContext();
        CharSequence text = getString(R.string.msg_login_wrong_entries);
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==Activity.RESULT_OK) {
            finish();
        } else if(resultCode== Activity.RESULT_CANCELED) {
            Context context = getApplicationContext();
            CharSequence text = getString(R.string.msg_no_signup);
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
}
