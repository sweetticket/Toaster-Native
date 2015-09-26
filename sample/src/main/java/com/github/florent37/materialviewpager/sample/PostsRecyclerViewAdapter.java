package com.github.florent37.materialviewpager.sample;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class PostsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Object> contents;
    private Map<String, Integer> mCommentsCountMap;
    private Map<String, PostItem> mPostItemMap;

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;

    public PostsRecyclerViewAdapter(List<Object> contents, Map<String, Integer> commentsCountMap) {
        this.contents = contents;
        this.mCommentsCountMap = commentsCountMap;
        this.mPostItemMap = new HashMap<String, PostItem>();

//        Log.d("contents", "contents: " + contents.toString());
//        Log.d("mCommentsCountMap", "mCommentsCountMap: " + mCommentsCountMap.toString());
    }

    public void updateContents(List<Object> newContents, Map<String, Integer> newCommentsCountMap) {
        contents = newContents;
        mCommentsCountMap = newCommentsCountMap;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.new_post_item, parent, false);
                NewPostItem newpostitem = new NewPostItem(view);
                newpostitem.setAdapter(this);
                return newpostitem;
            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.post_item, parent, false);
                return new PostItem(view) {
                };
            }
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                break;
            case TYPE_CELL:
                try {
//                    Log.d("onBindViewHolder", "contents: " +  contents.toString());
                    ((PostItem) holder).bindPost((JSONObject) contents.get(position), mCommentsCountMap);
                    try {
                        String postId = ((JSONObject) contents.get(position)).getString("_id");
                        mPostItemMap.put(postId, (PostItem) holder);
                        Log.d("onBindViewHolder", "mPostItemMap: " + mPostItemMap.toString());
                    } catch (org.json.JSONException e) {
                        Log.d("onBindViewHolder", "Error getting postId: " + e.getMessage());
                    }
                } catch (java.lang.ClassCastException e) {
                    Log.d("onBindViewHolder", e.getMessage());
                }
                break;
        }
    }

    public PostItem getPostItem(String postId) {
        Log.d("getPostItem", "mPostItemMap: " + mPostItemMap.toString());
        return mPostItemMap.get(postId);
    }
}
