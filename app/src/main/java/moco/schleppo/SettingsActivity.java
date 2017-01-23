package moco.schleppo;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

/**
 * Created by soeren on 08.12.2016.
 */

public class SettingsActivity extends PreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String PREF_KEY_NOTIFICATION = "pref_key_notifications";
    public static final String PREF_KEY_LOCATION = "pref_key_location";

    Preference notificationPref;
    Preference locationPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.layout_settings);

        notificationPref = findPreference(PREF_KEY_NOTIFICATION);
        locationPref = findPreference(PREF_KEY_LOCATION);

        boolean notificationPrefIsSet = notificationPref.getSharedPreferences().getBoolean(PREF_KEY_NOTIFICATION, true);
        boolean locationPrefIsSet = locationPref.getSharedPreferences().getBoolean(PREF_KEY_LOCATION, true);
        String newNotificationSummary;
        String newLocationSummary;
        if(notificationPrefIsSet) {
            newNotificationSummary = getString(R.string.pref_notification_true);
        } else {
            newNotificationSummary = getString(R.string.pref_notification_false);
        }
        notificationPref.setSummary(newNotificationSummary);

        if(locationPrefIsSet) {
            newLocationSummary = getString(R.string.pref_location_true);
        } else {
            newLocationSummary = getString(R.string.pref_location_false);
        }
        locationPref.setSummary(newLocationSummary);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PREF_KEY_NOTIFICATION)) {
            boolean notificationPrefIsSet = sharedPreferences.getBoolean(PREF_KEY_NOTIFICATION, true);
            String newSummary;
            if(notificationPrefIsSet) {
                newSummary = getString(R.string.pref_notification_true);
            } else {
                newSummary = getString(R.string.pref_notification_false);
            }
            notificationPref.setSummary(newSummary);

        } else if(key.equals(PREF_KEY_LOCATION)) {
            boolean locationPrefIsSet = sharedPreferences.getBoolean(PREF_KEY_LOCATION, true);
            String newSummary;
            if(locationPrefIsSet) {
                newSummary = getString(R.string.pref_location_true);
            } else {
                newSummary = getString(R.string.pref_location_false);
            }
            locationPref.setSummary(newSummary);
        }
    }
}
