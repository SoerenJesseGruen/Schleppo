package moco.schleppo.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

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
                ParseObject message = new ParseObject("Message");

                EditText licenseNumber = (EditText) rootView.findViewById(R.id.kennzeichenEingabe);
                CheckBox checkTowing = (CheckBox) rootView.findViewById(R.id.warn_driver_abschleppdienst);
                CheckBox checkPOH = (CheckBox) rootView.findViewById(R.id.warn_driver_halteverbot);
                EditText messageText = (EditText) rootView.findViewById(R.id.msg_warn_driver);
                boolean isTowing = checkTowing.isChecked();
                boolean isPOH = checkPOH.isChecked();

                if(checkLicenseNumber(licenseNumber.getText().toString().trim().toUpperCase())) {
                    message.put("Sender", UserManagement.parseUser.getUsername());
                    message.put("Reciever", licenseNumber.getText().toString().trim().toUpperCase());
                    message.put("MessageText", messageText.getText().toString());
                    message.put("TowingService", isTowing);
                    message.put("ProhibitionOfHolding", isPOH);
                    message.saveInBackground();

                    Toast.makeText(getActivity(), getString(R.string.msg_send_warning), Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.driver_not_available), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private boolean checkLicenseNumber (String licenseNumber) {
        ParseQuery query = ParseUser.getQuery(); //ParseQuery.getQuery("User");
        List<ParseUser> user = null;
        query.whereEqualTo("licenseNumber", licenseNumber);
        try {
            user = (List<ParseUser>) query.find();
        } catch (Exception e) {
            Log.d("CheckLicenseNumber", e.getMessage());
        }

        if (user.isEmpty()) {
            return false;
        }
        return true;
    }
}
