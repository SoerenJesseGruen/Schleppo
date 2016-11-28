package moco.schleppo.fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseUser;
import com.parse.ui.ParseLoginActivity;
import com.parse.ui.ParseLoginBuilder;

import moco.schleppo.MainActivity;
import moco.schleppo.R;

import static java.lang.Thread.sleep;
import static moco.schleppo.R.styleable.NavigationView;

/**
 * Created by soere on 27.11.2016.
 */

public class UserManagement {

    public static final int LOGIN_REQUEST = 0;
    public static ParseUser parseUser;

    public static boolean doLogin(Activity activity) {

        ParseLoginBuilder builder = new ParseLoginBuilder(activity);
        activity.startActivityForResult(builder.build(), LOGIN_REQUEST);
        /*
        while(parseUser==null) {
            parseUser = ParseUser.getCurrentUser();
            try {
                //sleep(1000);
            } catch (Exception e) {
                Log.d("UserManagement", e.getMessage());
            }

        }
        */
        MainActivity.loginView.setVisible(false);
        MainActivity.logoutView.setVisible(true);
        return true;
    }

    public static boolean doLogout(Activity activity) {

        ParseUser.logOut();
        parseUser = ParseUser.getCurrentUser();

        if(parseUser==null) {
            MainActivity.loginView.setVisible(true);
            MainActivity.logoutView.setVisible(false);

            View headerView = MainActivity.navigationView.getHeaderView(0);
            ((TextView) headerView.findViewById(R.id.userName)).setText(activity.getString(R.string.standard_userName_header));
            ((TextView) headerView.findViewById(R.id.userLicenseNumber)).setText(R.string.standard_licenseNumber_header);
        } else {
            Log.d("UserManagement", "Logout failed!");
        }

        return (parseUser==null)?true:false;
    }

    public static boolean checkUser (NavigationView navigationView) {
        parseUser = ParseUser.getCurrentUser();

        if(parseUser!=null) {
            MainActivity.loginView.setVisible(false);
            MainActivity.logoutView.setVisible(true);

            String licenseNumber = parseUser.getString("licenseNumber");
            View headerView = navigationView.getHeaderView(0);
            ((TextView) headerView.findViewById(R.id.userName)).setText(parseUser.getUsername());
            ((TextView) headerView.findViewById(R.id.userLicenseNumber)).setText(licenseNumber);

            return true;
        }

        return false;
    }
}
