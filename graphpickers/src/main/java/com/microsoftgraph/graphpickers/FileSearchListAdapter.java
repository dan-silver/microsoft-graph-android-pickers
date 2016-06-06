package com.microsoftgraph.graphpickers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.microsoft.graph.extensions.DriveItem;

import java.text.DateFormat;
import java.util.List;

/**
 * Created by dan on 6/4/16.
 */
public class FileSearchListAdapter extends SearchListAdapter<DriveItem, FileSearchListAdapter.FileViewHolder> {

    private final GraphFileSearch graphFileSearchActivity;

    @Override
    public int getModelRowLayout() {
        return R.layout.graphpickers__generic_result_row;
    }

    public FileSearchListAdapter(GraphFileSearch graphFileSearch, List<DriveItem> myDataset) {
        super(myDataset);
        this.graphFileSearchActivity = graphFileSearch;
    }

    @Override
    public FileViewHolder getNewViewHolder(View v) {
        return new FileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FileViewHolder holder, int position) {
        final DriveItem model = mDataset.get(position);

        // file or folder name
        String fileName = model.name;
        if (!graphFileSearchActivity.showFileExtensions && fileName.contains(".")) {
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        }
        holder.tvHeaderPrimary.setText(fileName);

        // last updated date
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.FULL);
        formatter.setTimeZone(model.lastModifiedDateTime.getTimeZone());
        String formatted = formatter.format(model.lastModifiedDateTime.getTime());

        holder.tvHeaderSecondary.setText(holder.tvHeaderSecondary.getContext().getString(R.string.graphpickers__last_modified, formatted));

        holder.ivIcon.setImageBitmap(null);
        int image = -1;
        String fileExtension = getExtension(model.name);
        if (model.folder != null) {
            image = R.mipmap.graphpickers__folder;
        } else if (fileExtension != null) {
            switch (fileExtension.toLowerCase()) {
                case "pptx":
                  image = R.mipmap.graphpickers__pptx;
                  break;
                case "docx":
                    image = R.mipmap.graphpickers__docx;
                    break;
                case "txt":
                    image = R.mipmap.graphpickers__txt;
                    break;
                case "zip":
                    image = R.mipmap.graphpickers__zip;
                    break;
                case "jpeg":
                    image = R.mipmap.graphpickers__jpeg;
                    break;
                case "png":
                    image = R.mipmap.graphpickers__png;
                    break;
                case "bmp":
                    image = R.mipmap.graphpickers__bmp;
                    break;
                case "mp3":
                    image = R.mipmap.graphpickers__mp3;
                    break;
                case "ini":
                    image = R.mipmap.graphpickers__ini;
                    break;
                case "rdg":
                case "rdp":
                    image = R.mipmap.graphpickers__remote_desktop;
                    break;
                case "exe":
                case "msi":
                    image = R.mipmap.graphpickers__program;
                    break;
                case "html":
                case "url":
                    image = R.mipmap.graphpickers__link;
                    break;
            }
        }

        if (image != -1) {
            holder.ivIcon.setImageResource(image);
        } else if (graphFileSearchActivity.defaultIcon != -1) {
            holder.ivIcon.setImageResource(graphFileSearchActivity.defaultIcon);
        }


    }

    private String getExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            return fileName.substring(i+1);
        }
        return null;
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvHeaderPrimary, tvHeaderSecondary;
        protected ImageView ivIcon;

        public FileViewHolder(View v) {
            super(v);
            tvHeaderPrimary = (TextView) v.findViewById(R.id.header_primary);
            tvHeaderSecondary = (TextView) v.findViewById(R.id.header_secondary);
            ivIcon = (ImageView) v.findViewById(R.id.icon);
        }
    }

}
