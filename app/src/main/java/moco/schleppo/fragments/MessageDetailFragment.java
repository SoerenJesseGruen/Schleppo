package moco.schleppo.fragments;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.parse.ParseObject;
import moco.schleppo.R;

public class MessageDetailFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.message_detail, container, false);

        ParseObject message = (ParseObject) savedInstanceState.get("Message");

        // Show the dummy content as text in a TextView.
        if (message != null) {
            ((TextView) rootView.findViewById(R.id.message_detail)).setText(message.toString());
        }

        return rootView;
    }
}
