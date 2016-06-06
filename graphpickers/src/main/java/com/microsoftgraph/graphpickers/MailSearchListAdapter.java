package com.microsoftgraph.graphpickers;

/**
 * Created by dan on 6/5/16.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.microsoft.graph.extensions.Message;

import java.text.DateFormat;
import java.util.List;


/**
 * Created by dan on 6/4/16.
 */
public class MailSearchListAdapter extends SearchListAdapter<Message, MailSearchListAdapter.MessageViewHolder> {

    private final GraphMailSearch graphMessageSearchActivity;

    @Override
    public int getModelRowLayout() {
        return R.layout.graphpickers__generic_result_row;
    }

    public MailSearchListAdapter(GraphMailSearch activity, List<Message> myDataset) {
        super(myDataset);
        graphMessageSearchActivity = activity;
    }

    @Override
    public MessageViewHolder getNewViewHolder(View v) {
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder holder, int position) {
        final Message msg = mDataset.get(position);
        holder.primaryHeader.setText(msg.subject);
        holder.secondaryHeader.setText(msg.sender.emailAddress.name);

        DateFormat formatter = DateFormat.getDateInstance(DateFormat.FULL);
        formatter.setTimeZone(msg.receivedDateTime.getTimeZone());
        String formatted = formatter.format(msg.receivedDateTime.getTime());
        holder.tertiaryHeader.setText(formatted);

        holder.ivPhoto.setVisibility(View.GONE);

//            holder.ivProfilePhoto.setImageDrawable(null);

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        protected TextView primaryHeader, secondaryHeader, tertiaryHeader;
        protected ImageView ivPhoto;

        public MessageViewHolder(View v) {
            super(v);
            primaryHeader = (TextView) v.findViewById(R.id.header_primary);
            secondaryHeader = (TextView) v.findViewById(R.id.header_secondary);
            tertiaryHeader = (TextView) v.findViewById(R.id.header_tertiary);
            ivPhoto = (ImageView) v.findViewById(R.id.icon);
        }
    }

}
