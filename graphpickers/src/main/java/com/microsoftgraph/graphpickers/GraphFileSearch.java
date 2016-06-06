package com.microsoftgraph.graphpickers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.extensions.DriveItem;
import com.microsoft.graph.extensions.DriveItemCollectionRequest;
import com.microsoft.graph.extensions.IDriveItemCollectionPage;
import com.microsoft.graph.logger.DefaultLogger;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.serializer.DefaultSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dan on 6/4/16.
 */

//icons from https://www.iconfinder.com/iconsets/FileTypesIcons

public class GraphFileSearch extends GraphSearchActivity {
    public static String SHOW_FILE_EXTENSIONS = "FILE_EXTENSIONS";
    String currentQuery;
    String currentFolder;
    boolean showFileExtensions;

    @Override
    protected void handleSearch(String newText) {
        currentQuery = newText;
        getFiles();
    }

    @Override
    protected void handleItemClick(int position) {
        DriveItem item = ((FileSearchListAdapter) rvModelList.getAdapter()).getItem(position);

        if (item.folder == null) {
            // item is a file
            Intent returnIntent = new Intent();

            DefaultSerializer serializer = new DefaultSerializer(new DefaultLogger());
            String fileStr = serializer.serializeObject(item);

            returnIntent.putExtra("file", fileStr);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();

        } else {
            // item is a folder
            currentFolder = item.id;
            getFiles();
        }
    }

    @Override
    public SearchListAdapter createAdapter() {
        return new FileSearchListAdapter(this, new ArrayList<DriveItem>());
    }


    public void getFiles() {
        String path = "https://graph.microsoft.com/v1.0/me/drive/root/children";

        if (currentFolder != null) {
           path = "https://graph.microsoft.com/v1.0/me/drive/items/" + currentFolder + "/children";
        }


        DriveItemCollectionRequest request = new DriveItemCollectionRequest(path, GraphPickerLib.getClient(), new ArrayList<Option>());

        if (currentQuery != null) {
            request.addQueryOption(new QueryOption("$filter", "startswith(name, '" + currentQuery + "')"));
        }

        GraphPickerLib.getClient().getAuthenticationProvider().authenticateRequest(request);
        request.get(new ICallback<IDriveItemCollectionPage>() {
            @Override
            public void success(IDriveItemCollectionPage iDriveItemCollectionPage) {
                List<DriveItem> a = iDriveItemCollectionPage.getCurrentPage();

                adapter.animateTo(a);
                rvModelList.scrollToPosition(0);
            }

            @Override
            public void failure(ClientException ex) {

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        if (extras.containsKey(SHOW_FILE_EXTENSIONS)) {
            showFileExtensions = extras.getBoolean(SHOW_FILE_EXTENSIONS, true);
        }
        getFiles();
    }

    public static DriveItem getFile(Intent data) {
        String jsonStr = data.getStringExtra("file");
        return new DefaultSerializer(new DefaultLogger()).deserializeObject(jsonStr, DriveItem.class);
    }

    public static class IntentBuilder extends com.microsoftgraph.graphpickers.IntentBuilder {
        boolean showFileExtensions = true;

        @Override
        protected void addSearchSpecificExtras(Intent intent) {
            intent.putExtra(GraphFileSearch.SHOW_FILE_EXTENSIONS, showFileExtensions);
        }

        @Override
        protected void init() {

        }

        public void setShowFileExtensions(boolean showFileExtensions) {
            this.showFileExtensions = showFileExtensions;
        }

        @Override
        protected Class getSearchClass() {
            return GraphFileSearch.class;
        }
    }
}
