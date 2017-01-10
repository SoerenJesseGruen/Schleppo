package moco.schleppo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.HashMap;

import moco.schleppo.LoginActivity;
import moco.schleppo.MainActivity;
import moco.schleppo.R;

/**
 * Created by soere on 27.11.2016.
 */

public class UserManagement extends Activity {

    public static final int LOGIN_REQUEST = 0;
    public static ParseUser parseUser;
    public static boolean isAnonymousUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(isAnonymousUser) {
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
            //parseUser.put("authData", null);
            //parseUser.saveInBackground();
            finish();
        }
    }

    private void doLogin() {
        try {
            ParseUser.getCurrentUser().delete();
        } catch (Exception e) {
            Log.d("AnonymousUser", "Failed to delete AnonymousUser");
            Log.d("AnonymousUser", e.getMessage());
        }
        parseUser = null;
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
                    doAnonymousLogin();
                } else {
                    Log.d("UserManagement", "Logout failed!");
                }

                finish();
            }
        });
    }

    public static void checkUser () {
        parseUser = ParseUser.getCurrentUser();

        if(parseUser==null) {
            doAnonymousLogin();
        } else {
            isAnonymousUser = (parseUser.get("authData")!=null? true: false);
        }

        if(!isAnonymousUser) {
            MainActivity.loginView.setVisible(false);
            MainActivity.logoutView.setVisible(true);

            String licenseNumber = parseUser.getString("licenseNumber");
            View headerView = MainActivity.navigationView.getHeaderView(0);
            ((TextView) headerView.findViewById(R.id.userName)).setText(parseUser.getUsername());
            ((TextView) headerView.findViewById(R.id.userLicenseNumber)).setText(licenseNumber);
        }
    }

    private static void doAnonymousLogin() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.d("AnonymousUser", "Anonymous login failed.");
                    Log.d("AnonymousUser", e.getMessage());
                } else {
                    Log.d("AnonymousUser", "Anonymous user logged in.");
                    parseUser = user;
                    isAnonymousUser = true;
                }
            }
        });
    }

    private void resetTextFields () {
        MainActivity.loginView.setVisible(true);
        MainActivity.logoutView.setVisible(false);

        View headerView = MainActivity.navigationView.getHeaderView(0);
        ((TextView) headerView.findViewById(R.id.userName)).setText(getString(R.string.standard_userName_header));
        ((TextView) headerView.findViewById(R.id.userLicenseNumber)).setText(R.string.standard_licenseNumber_header);
    }
}
