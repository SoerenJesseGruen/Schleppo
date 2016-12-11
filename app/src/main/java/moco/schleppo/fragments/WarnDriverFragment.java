package moco.schleppo.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import moco.schleppo.R;

/**
 * Created by soere on 23.11.2016.
 */

public class WarnDriverFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_warn_driver, container, false);
        setClickListener(rootView);

        return rootView;
    }

    public void setClickListener(final View rootView){

        final Button bAbbruch = (Button) rootView.findViewById(R.id.msg_warn_driver_abbrechen);

        bAbbruch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), R.string.msg_quit_warning, Toast.LENGTH_LONG).show();

            }
        });


    }
}
