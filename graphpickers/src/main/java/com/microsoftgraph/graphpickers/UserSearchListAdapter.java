package com.microsoftgraph.graphpickers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.extensions.ProfilePhotoStreamRequest;
import com.microsoft.graph.extensions.User;
import com.microsoft.graph.options.Option;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dan on 6/4/16.
 */
public class UserSearchListAdapter extends SearchListAdapter<User, UserSearchListAdapter.UserViewHolder> {

    private HashMap<User, Bitmap> bmpCache = new HashMap<>();
    private final GraphUserSearch graphUserSearchActivity;

    @Override
    public int getModelRowLayout() {
        return R.layout.graphpickers__generic_result_row;
    }

    public UserSearchListAdapter(List<User> myDataset, GraphUserSearch activity) {
        super(myDataset);
        graphUserSearchActivity = activity;
    }

    @Override
    public UserViewHolder getNewViewHolder(View v) {
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        final User user = mDataset.get(position);
        holder.tvDisplayName.setText(user.displayName);
        holder.tvMail.setText(user.mail);
        if (graphUserSearchActivity.defaultIcon == -1) {
            holder.ivProfilePhoto.setImageDrawable(null);
        } else {
            holder.ivProfilePhoto.setImageResource(graphUserSearchActivity.defaultIcon); // must clear out image since holders are reused and the new user might not have an image
        }

        if (bmpCache.containsKey(user)) {
            holder.ivProfilePhoto.setImageBitmap(bmpCache.get(user));
        } else {
            ProfilePhotoStreamRequest request = new ProfilePhotoStreamRequest("https://graph.microsoft.com/v1.0/users/" + user.id + "/photo/$value", GraphPickerLib.getClient(), new ArrayList<Option>());

            GraphPickerLib.getClient().getAuthenticationProvider().authenticateRequest(request);
            request.get(new ICallback<InputStream>() {
                @Override
                public void success(final InputStream inputStream) {
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                        final Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                        holder.ivProfilePhoto.post(new Runnable() {
                            @Override
                            public void run() {
                            bmpCache.put(user, bmp);
                            holder.ivProfilePhoto.setImageBitmap(bmp);
                            }
                        });
                        }
                    });
                }

                @Override
                public void failure(ClientException ex) {

                }
            });
        }

    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvDisplayName, tvMail;
        protected ImageView ivProfilePhoto;

        public UserViewHolder(View v) {
            super(v);
            tvDisplayName = (TextView) v.findViewById(R.id.header_primary);
            tvMail = (TextView) v.findViewById(R.id.header_secondary);
            ivProfilePhoto = (ImageView) v.findViewById(R.id.icon);
        }
    }

}
