package com.microsoftgraph.graphpickers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.extensions.IUserCollectionPage;
import com.microsoft.graph.extensions.User;
import com.microsoft.graph.logger.DefaultLogger;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.serializer.DefaultSerializer;

import java.util.ArrayList;
import java.util.List;

public class GraphUserSearch extends GraphSearchActivity {

    @Override
    protected void handleSearch(String newText) {
        getUsers(newText);
    }

    @Override
    protected void handleItemClick(int position) {
        User user = ((UserSearchListAdapter) rvModelList.getAdapter()).getItem(position);

        DefaultSerializer serializer = new DefaultSerializer(new DefaultLogger());
        String userStr = serializer.serializeObject(user);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("user", userStr);
        setResult(RESULT_OK, returnIntent);
        finish();

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public SearchListAdapter createAdapter() {
        return new UserSearchListAdapter(new ArrayList<User>(), this);
    }


    public void getUsers(String query) {
        List<Option> options = new ArrayList<>();

        options.add(new QueryOption("$filter", "(startswith(displayName,'" + query + "') or startswith(mail,'" + query + "') or startswith(mailNickname,'" + query + "')) and accountEnabled eq true and userType eq 'Member'"));
        GraphPickerLib.getClient()
                .getUsers()
                .buildRequest(options)
                .get(new ICallback<IUserCollectionPage>() {
                    @Override
                    public void success(IUserCollectionPage iUserCollectionPage) {
                        List<User> users = iUserCollectionPage.getCurrentPage();

                        adapter.animateTo(users);
                        rvModelList.scrollToPosition(0);
                    }

                    @Override
                    public void failure(ClientException ex) {
                        Log.e(GraphPickerLib.TAG, ex.toString());
                    }
                });


    }

    public static User getUser(Intent data) {
        String userJSON = data.getStringExtra("user");
        return new DefaultSerializer(new DefaultLogger()).deserializeObject(userJSON, User.class);
    }


    public static class IntentBuilder extends com.microsoftgraph.graphpickers.IntentBuilder {
        @Override
        protected void addSearchSpecificExtras(Intent intent) {

        }

        @Override
        protected void init() {
            setOpenKeyboardByDefault(true);
            setDefaultIcon(R.drawable.graphpickers__ic_person_black_24dp); // can still be set by user
        }

        @Override
        protected Class getSearchClass() {
            return GraphUserSearch.class;
        }
    }
}