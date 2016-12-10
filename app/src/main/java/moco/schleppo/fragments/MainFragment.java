package moco.schleppo.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import moco.schleppo.R;

/**
 * Created by soere on 23.11.2016.
 */

public class MainFragment extends Fragment {
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        setClickListener(rootView);

        return rootView;
    }

    private void setClickListener (View rootView) {

        ImageButton btnProfile = (ImageButton) rootView.findViewById(R.id.imageButton_Profile);
        ImageButton btnMap = (ImageButton) rootView.findViewById(R.id.imageButton_Map);
        ImageButton btnMessages = (ImageButton) rootView.findViewById(R.id.imageButton_Messages);
        ImageButton btnWarnDriver = (ImageButton) rootView.findViewById(R.id.imageButton_Warning);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new ProfilFragment(), "profile");
                ft.addToBackStack("profile");
                ft.commit();
            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new MapsFragment(), "map");
                ft.addToBackStack("map");
                ft.commit();
            }
        });
        btnMessages.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new MessagesFragment(), "messages");
                ft.addToBackStack("messages");
                ft.commit();
            }
        });
        btnWarnDriver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new WarnDriverFragment(), "warnDriver");
                ft.addToBackStack("warnDriver");
                ft.commit();
            }
        });

    }
}
