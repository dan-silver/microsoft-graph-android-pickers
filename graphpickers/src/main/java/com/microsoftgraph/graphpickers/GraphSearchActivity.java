package com.microsoftgraph.graphpickers;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by dan on 6/4/16.
 */
abstract public class GraphSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static String DEBOUNCE_TIME = "DEBOUNCE_TIME";
    public static String SEARCH_PLACEHOLDER = "SEARCH_PLACEHOLDER";
    public static String OPENKEYBOARD = "OPENKEYBOARD";
    public static String DEFAULT_ICON = "DEFAULT_ICON";

    protected int debouneTime;
    protected String searchPlaceholderText;
    protected boolean openKeyboardByDefault;
    protected int defaultIcon = -1;


    private Handler handler = new Handler();
    private Runnable runnable;


    RecyclerView rvModelList;
    protected SearchListAdapter adapter;

    protected abstract void handleSearch(String newText);

    protected abstract void handleItemClick(int position);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphpickers__activity_graph_search);

        Bundle extras = getIntent().getExtras();

        debouneTime = extras.getInt(DEBOUNCE_TIME);

        if (extras.containsKey(SEARCH_PLACEHOLDER)) {
            searchPlaceholderText = extras.getString(SEARCH_PLACEHOLDER);
        }

        if (extras.containsKey(OPENKEYBOARD)) {
            openKeyboardByDefault = extras.getBoolean(OPENKEYBOARD);
        }

        if (extras.containsKey(DEFAULT_ICON)) {
            defaultIcon = extras.getInt(DEFAULT_ICON);
        }


        getSupportActionBar().setDisplayShowTitleEnabled(false);
        rvModelList = (RecyclerView) findViewById(R.id.user_list);
        rvModelList.addOnItemTouchListener(
            new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override public void onItemClick(View view, int position) {
                    handleItemClick(position);
                }
        }));
        rvModelList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));


        adapter = createAdapter();
        rvModelList.setAdapter(adapter); // start with an empty user list

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.graphpickers__menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(false);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        if (searchPlaceholderText != null) {
            searchView.setQueryHint(searchPlaceholderText);
        } else {
            searchView.setQueryHint(getString(R.string.graphpickers__search_for_users));
        }

        if (openKeyboardByDefault) {
            searchView.setFocusable(true);
            searchView.requestFocusFromTouch();
        }

        return true;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        // Remove all previous callbacks.
        handler.removeCallbacks(runnable);

        runnable = new Runnable() {
            @Override
            public void run() {
                handleSearch(newText);
            }

        };
        handler.postDelayed(runnable, debouneTime);
        return false;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    public abstract SearchListAdapter createAdapter();
}
