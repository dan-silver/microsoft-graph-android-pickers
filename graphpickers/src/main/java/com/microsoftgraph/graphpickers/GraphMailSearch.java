package com.microsoftgraph.graphpickers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.extensions.IMessageCollectionPage;
import com.microsoft.graph.extensions.Message;
import com.microsoft.graph.extensions.MessageCollectionRequest;
import com.microsoft.graph.logger.DefaultLogger;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.serializer.DefaultSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dan on 6/4/16.
 */

public class GraphMailSearch extends GraphSearchActivity {

    @Override
    protected void handleSearch(String newText) {
        getContent(newText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getContent(null);
    }

    @Override
    protected void handleItemClick(int position) {
        Message item = ((MailSearchListAdapter) rvModelList.getAdapter()).getItem(position);

        Intent returnIntent = new Intent();

        DefaultSerializer serializer = new DefaultSerializer(new DefaultLogger());
        String messageStr = serializer.serializeObject(item);

        returnIntent.putExtra("message", messageStr);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public SearchListAdapter createAdapter() {
        return new MailSearchListAdapter(this, new ArrayList<Message>());
    }


    public void getContent(String query) {
        String path = "https://graph.microsoft.com/v1.0/me/mailFolders/Inbox/messages";

        MessageCollectionRequest request = new MessageCollectionRequest(path, GraphPickerLib.getClient(), new ArrayList<Option>());

        if (query != null && query.length() > 0) {
            request.addQueryOption(new QueryOption("$filter", "startswith(subject, '" + query + "')"));
        }

        GraphPickerLib.getClient().getAuthenticationProvider().authenticateRequest(request);
        request.get(new ICallback<IMessageCollectionPage>() {
            @Override
            public void success(IMessageCollectionPage iMessageCollectionPage) {
                List<Message> a = iMessageCollectionPage.getCurrentPage();

                adapter.animateTo(a);
                rvModelList.scrollToPosition(0);

            }

            @Override
            public void failure(ClientException ex) {

            }
        });
    }

    public static Message getMessage(Intent data) {
        String jsonStr = data.getStringExtra("message");
        return new DefaultSerializer(new DefaultLogger()).deserializeObject(jsonStr, Message.class);
    }

    public static class IntentBuilder extends com.microsoftgraph.graphpickers.IntentBuilder {

        @Override
        protected void addSearchSpecificExtras(Intent intent) {
        }

        @Override
        protected void init() {
            setSearchPlaceholderText(R.string.graphpickers__search_for_email);
        }

        @Override
        protected Class getSearchClass() {
            return GraphMailSearch.class;
        }
    }
}
