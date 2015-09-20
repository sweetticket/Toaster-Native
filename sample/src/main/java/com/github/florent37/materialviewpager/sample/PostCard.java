package com.github.florent37.materialviewpager.sample;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by jennykim on 9/20/15.
 */
public class PostCard extends RecyclerView.ViewHolder {

    private TextView mPostBody;
    private TextView mPostDate;
    private TextView mPostNumComments;
    private TextView mPostNumVotes;


    public PostCard(View view) {
        super(view);

        mPostBody = (TextView) view.findViewById(R.id.post_body);
        mPostDate = (TextView) view.findViewById(R.id.new_post_input);
        mPostNumComments = (TextView) view.findViewById(R.id.new_post_input);
        mPostNumVotes = (TextView) view.findViewById(R.id.num_votes);

    }

    private final TextWatcher mNewPostWatcher= new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            mCharCount.setText(String.valueOf(140 - s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
    };
}
