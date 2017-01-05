package moco.schleppo.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import moco.schleppo.R;

/**
 * Created by soere on 23.11.2016.
 */

public class WarnDriverFragment extends Fragment {
    EditText license = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_warn_driver, container, false);
        setClickListener(rootView);

        license = (EditText) rootView.findViewById(R.id.kennzeichenEingabe);
        CheckBox checkTowing = (CheckBox) rootView.findViewById(R.id.warn_driver_abschleppdienst);
        CheckBox checkOffice = (CheckBox) rootView.findViewById(R.id.warn_driver_halteverbot);
        EditText msg = (EditText) rootView.findViewById(R.id.msg_warn_driver);

        return rootView;
    }

    private void cancelWarning() {
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new MainFragment());
        ft.commit();
    }

    public void setClickListener(final View rootView){

        final Button bAbbruch = (Button) rootView.findViewById(R.id.msg_warn_driver_abbrechen);

        bAbbruch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), R.string.msg_quit_warning, Toast.LENGTH_LONG).show();
                cancelWarning();



            }
        });

        final Button bSend = (Button) rootView.findViewById(R.id.msg_warn_drive_sende);
        bSend.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                senden(rootView);
            }
        });

    }

    private void senden(View rootView) {
        String eingabe = license.getText().toString();
    }
}
