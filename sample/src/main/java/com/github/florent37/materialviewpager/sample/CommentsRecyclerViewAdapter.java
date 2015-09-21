package com.github.florent37.materialviewpager.sample;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class CommentsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Object> contents;

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;

    public CommentsRecyclerViewAdapter(List<Object> contents) {
        this.contents = contents;

        Log.d("contents", "comment contents: " + contents.toString());
    }

    public void updateContents(List<Object> newContents) {
        contents = newContents;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
//        switch (position) {
//            case 0:
//                return TYPE_HEADER;
//            default:
                return TYPE_CELL;
//        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);
        return new CommentItem(view) {
        };

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            Log.d("onBindViewHolder", "comment contents: " + contents.toString());
            ((CommentItem) holder).bindComment((JSONObject) contents.get(position));
        } catch (ClassCastException e) {
            Log.d("onBindViewHolder", "Error: " + e.getMessage());
        }
    }
}
