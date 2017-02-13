package moco.schleppo.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Date;

import moco.schleppo.R;

public class MessageDetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message_detail, container, false);
        ParseObject message = null;
        String messageID = getArguments().get("messageID").toString();
        ParseQuery query = ParseQuery.getQuery("Message");

        try {
            message = query.get(messageID);
        } catch(Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        if (message != null) {
            ((TextView) rootView.findViewById(R.id.message_detail_date)).setText(parseDate(message.getCreatedAt()));
            ((TextView) rootView.findViewById(R.id.message_detail_cause)).setText(getMessageCauseString(message));
            ((TextView) rootView.findViewById(R.id.message_detail_text)).setText(message.getString("MessageText"));
        }

        return rootView;
    }

    private String getMessageCauseString (ParseObject message) {
        StringBuilder messageCause = new StringBuilder();
        if(message.getBoolean("TowingService")) {
            messageCause.append(getString(R.string.abschleppdienst));
        }
        if(message.getBoolean("ProhibitionOfHolding")) {
            if(messageCause.length()!=0) {
                messageCause.append(", ");
            }
            messageCause.append(getString(R.string.prohibition_of_parking));
        }

        return messageCause.toString();
    }

    private String parseDate (Date date) {

        SimpleDateFormat format = new SimpleDateFormat();
        try {
            date = format.parse(date.toString());
        } catch (Exception e) {
            Log.d("DateParse", e.getMessage());
        }

        format = new SimpleDateFormat("E, dd.MM.yyyy HH:mm");
        return format.format(date);
    }
}
