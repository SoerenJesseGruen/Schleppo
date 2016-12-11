package moco.schleppo.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginActivity;
import com.parse.ui.ParseLoginBuilder;

import moco.schleppo.LoginActivity;
import moco.schleppo.MainActivity;
import moco.schleppo.R;

import static java.lang.Thread.sleep;
import static moco.schleppo.R.styleable.NavigationView;

/**
 * Created by soere on 27.11.2016.
 */

public class UserManagement extends Activity {

    public static final int LOGIN_REQUEST = 0;
    public static ParseUser parseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(parseUser==null) {
            doLogin();
        } else {
            doLogout();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == LOGIN_REQUEST) {
            checkUser();
            finish();
        }
    }

    private void doLogin() {

        //ParseLoginBuilder builder = new ParseLoginBuilder(this);
        //Intent loginIntent = builder.build();
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(loginIntent, LOGIN_REQUEST);
    }


    private void doLogout() {

        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                parseUser = ParseUser.getCurrentUser();

                if(parseUser==null) {
                    resetTextFields ();
                } else {
                    Log.d("UserManagement", "Logout failed!");
                }

                finish();
            }
        });
    }

    public static boolean checkUser () {
        parseUser = ParseUser.getCurrentUser();

        if(parseUser!=null) {
            MainActivity.loginView.setVisible(false);
            MainActivity.logoutView.setVisible(true);

            String licenseNumber = parseUser.getString("licenseNumber");
            View headerView = MainActivity.navigationView.getHeaderView(0);
            ((TextView) headerView.findViewById(R.id.userName)).setText(parseUser.getUsername());
            ((TextView) headerView.findViewById(R.id.userLicenseNumber)).setText(licenseNumber);

            return true;
        }

        return false;
    }

    private void resetTextFields () {
        MainActivity.loginView.setVisible(true);
        MainActivity.logoutView.setVisible(false);

        View headerView = MainActivity.navigationView.getHeaderView(0);
        ((TextView) headerView.findViewById(R.id.userName)).setText(getString(R.string.standard_userName_header));
        ((TextView) headerView.findViewById(R.id.userLicenseNumber)).setText(R.string.standard_licenseNumber_header);
    }
}
