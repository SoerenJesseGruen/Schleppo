package moco.schleppo.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moco.schleppo.R;

/**
 * Created by soere on 23.11.2016.
 */

public class WarnDriverFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_warn_driver, container, false);

        return rootView;
    }
}
