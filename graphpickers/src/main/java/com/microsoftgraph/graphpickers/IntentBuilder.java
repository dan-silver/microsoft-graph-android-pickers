package com.microsoftgraph.graphpickers;

import android.content.Context;
import android.content.Intent;

/**
 * Created by dan on 6/4/16.
 */

public abstract class IntentBuilder {
    private int debounceTime = 300;
    private String placeholderText;
    private boolean openKeyboardByDefault = false;
    private int defaultIcon = -1;
    private int placeholderTextResId = -1;

    public IntentBuilder() {
        init();
    }

    public Intent build(Context context) {

        Intent intent = new Intent(context, getSearchClass());

        addSearchSpecificExtras(intent);

        intent.putExtra(GraphSearchActivity.DEBOUNCE_TIME, debounceTime);
        intent.putExtra(GraphSearchActivity.OPENKEYBOARD, openKeyboardByDefault);

        if (placeholderText != null) {
            intent.putExtra(GraphSearchActivity.SEARCH_PLACEHOLDER, placeholderText);
        } else if (placeholderTextResId != -1) {
            intent.putExtra(GraphSearchActivity.SEARCH_PLACEHOLDER, context.getString(placeholderTextResId));
        }

        if (defaultIcon != -1) {
            intent.putExtra(GraphSearchActivity.DEFAULT_ICON, defaultIcon);
        }


        return intent;
    }

    protected abstract void addSearchSpecificExtras(Intent intent);

    public void setDebounceTime(int debounceTime) {
        this.debounceTime = debounceTime;
    }
    public void setOpenKeyboardByDefault(boolean openKeyboardByDefault) {
        this.openKeyboardByDefault = openKeyboardByDefault;
    }

    public void setSearchPlaceholderText(String text) {
        this.placeholderText = text;
    }

    public void setSearchPlaceholderText(int resId) {
        this.placeholderTextResId = resId;
    }

    public void setDefaultIcon(int res) {
        this.defaultIcon = res;
    }

    protected abstract void init();

    protected abstract Class getSearchClass();
}