package com.microsoftgraph.graphpickers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dan on 6/4/16.
 */
public abstract class SearchListAdapter<Model, CustomViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<CustomViewHolder> {
    protected List<Model> mDataset;

    public abstract int getModelRowLayout();

    public Model getItem(int position) {
        return mDataset.get(position);
    }

    public SearchListAdapter(List<Model> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(getModelRowLayout(), parent, false);
        return getNewViewHolder(v);
    }

    public abstract CustomViewHolder getNewViewHolder(View v);


    public void setModels(List<Model> newModels) {
        mDataset = new ArrayList<>(newModels);
    }

    public void animateTo(List<Model> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private boolean modelInList(Model u) {
        for (Model model : mDataset) {
            if (itemsAreSame(model, u)) {
                return true;
            }
        }
        return false;
    }


    private boolean modelInList(List<Model> models, Model u) {
        for (Model model : models) {
            if (itemsAreSame(model, u)) {
                return true;
            }
        }
        return false;
    }

    private int getPosition(Model searchUser) {
        int i=0;
        for (Model model : mDataset) {
            if (itemsAreSame(model, searchUser)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public abstract boolean itemsAreSame(Model a, Model b);

    private void applyAndAnimateRemovals(List<Model> newModels) {
        for (int i = mDataset.size() - 1; i >= 0; i--) {
            final Model model = mDataset.get(i);
            if (!modelInList(newModels, model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Model> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Model model = newModels.get(i);
            if (!modelInList(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Model> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Model model = newModels.get(toPosition);
            final int fromPosition = getPosition(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Model removeItem(int position) {
        final Model model = mDataset.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Model model) {
        mDataset.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Model model = mDataset.remove(fromPosition);
        mDataset.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}