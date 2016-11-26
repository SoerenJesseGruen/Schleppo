package moco.schleppo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ui.ParseLoginBuilder;

import moco.schleppo.fragments.LoginFragment;
import moco.schleppo.fragments.LogoutFragment;
import moco.schleppo.fragments.MainFragment;
import moco.schleppo.fragments.MessagesFragment;
import moco.schleppo.fragments.ProfilFragment;
import moco.schleppo.fragments.WarnDriverFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static public MenuItem loginView;
    static public MenuItem logoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loginView = navigationView.getMenu().findItem(R.id.nav_login);
        logoutView = navigationView.getMenu().findItem(R.id.nav_logout);

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame, new MainFragment(), "CURRENT_FRAGMENT").commit();

        final String YOUR_APPLICATION_ID = "Parse_DB_Team_2";
        final String YOUR_CLIENT_KEY = "team2";
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId(YOUR_APPLICATION_ID)
                .clientKey(YOUR_CLIENT_KEY)
                .server("https://team2.parse.dock.moxd.io/api/")   // '/' important after 'api'
                .build());


        //ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.this);
        //startActivityForResult(builder.build(), 0);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        FragmentManager fm = getFragmentManager();
        fm.popBackStack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            ft.replace(R.id.content_frame, new MainFragment(), "home");
            ft.addToBackStack("home");
        } else if (id == R.id.nav_map) {
            //TODO: Map-Activity starten
        } else if (id == R.id.nav_profile) {
            ft.replace(R.id.content_frame, new ProfilFragment(), "profile");
            ft.addToBackStack("profile");
        } else if (id == R.id.nav_settings) {
            //TODO: Settings-Activity starten
        } else if (id == R.id.nav_login) {
            ft.replace(R.id.content_frame, new LoginFragment());
        } else if (id == R.id.nav_logout) {
            ft.replace(R.id.content_frame, new LogoutFragment());
        } else if (id == R.id.nav_messages) {
            ft.replace(R.id.content_frame, new MessagesFragment(), "messages");
            ft.addToBackStack("messages");
        } else if (id == R.id.nav_warn_driver) {
            ft.replace(R.id.content_frame, new WarnDriverFragment(), "warnDriver");
            ft.addToBackStack("warnDriver");
        }
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
