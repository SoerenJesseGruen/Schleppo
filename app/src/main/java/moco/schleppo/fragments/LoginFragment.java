package moco.schleppo.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ui.ParseLoginBuilder;

import moco.schleppo.MainActivity;
import moco.schleppo.R;

/**
 * Created by soere on 23.11.2016.
 */

public class LoginFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        boolean loginSucceeded = true;

        ParseLoginBuilder builder = new ParseLoginBuilder(getActivity());
        startActivityForResult(builder.build(), 0);

        //TODO: Abfragen ob Login erfolgreich war und "loginSucceeded" entspr. setzen

        if(loginSucceeded) {
            MainActivity.loginView.setVisible(false);
            MainActivity.logoutView.setVisible(true);
        }

        return rootView;
    }
}
