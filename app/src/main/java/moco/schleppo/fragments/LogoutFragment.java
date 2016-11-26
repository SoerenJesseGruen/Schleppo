package moco.schleppo.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moco.schleppo.MainActivity;
import moco.schleppo.R;

/**
 * Created by soere on 23.11.2016.
 */

public class LogoutFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //TODO: Logout mit Parse realisieren

        MainActivity.loginView.setVisible(true);
        MainActivity.logoutView.setVisible(false);

        return rootView;
    }
}
