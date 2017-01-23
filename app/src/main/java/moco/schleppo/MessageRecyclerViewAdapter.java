package moco.schleppo;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.UnicodeSetSpanner;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import moco.schleppo.fragments.MessageFragment.OnListFragmentInteractionListener;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder> {

    private final List<ParseObject> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public MessageRecyclerViewAdapter(List<ParseObject> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_message, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        String messageDate = parseDate(mValues.get(position).getCreatedAt());
        holder.mIdView.setText(messageDate);
        holder.mContentView.setText(getMessageCauseString(mValues.get(position)));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    private String parseDate (Date date) {

        SimpleDateFormat format = new SimpleDateFormat();
        try {
            date = format.parse(date.toString());
        } catch (Exception e) {
            Log.d("DateParse", e.getMessage());
        }

        format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return format.format(date);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public ParseObject mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.message_date);
            mContentView = (TextView) view.findViewById(R.id.message_content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    private String getMessageCauseString (ParseObject message) {
        StringBuilder messageCause = new StringBuilder();
            if(message.getBoolean("TowingService")) {
            messageCause.append(context.getString(R.string.abschleppdienst));
        }
        if(message.getBoolean("ProhibitionOfHolding")) {
            if(messageCause.length()!=0) {
                messageCause.append(", ");
            }
            messageCause.append(context.getString(R.string.prohibition_of_parking));
        }

        return messageCause.toString();
    }
}
