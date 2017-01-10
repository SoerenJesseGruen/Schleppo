package moco.schleppo.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

import moco.schleppo.R;

/**
 * Created by soere on 23.11.2016.
 */

public class MessagesFragment extends Fragment {
    ArrayList<ParseObject> messages = new ArrayList<ParseObject>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);

        //getMessagesFromParse();
        //String messagePreview = messages.get(0).getCreatedAt().toString() + " - " + messages.get(0).getString("MessageText");

        Toast.makeText(getActivity(), "Diese Funktion ist noch nicht implementiert!", Toast.LENGTH_SHORT).show();

        return rootView;
    }

    private void getMessagesFromParse () {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
        query.getInBackground(UserManagement.parseUser.get("LicenseNumber").toString(), new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Log.d("GetMessagesFromParse", "Load Message successfull");
                    messages.add(object);
                } else {
                    Log.d("GetMessagesFromParse", e.getMessage());
                }
            }
        });
    }
}
