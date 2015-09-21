package com.github.florent37.materialviewpager.sample;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.florent37.materialviewpager.sample.R;

/**
 * Created by jennykim on 9/20/15.
 */
public class NewPostItem extends RecyclerView.ViewHolder {

    private TextView mCharCount;
    private EditText mNewPost;


    public NewPostItem(View view) {
        super(view);

        mNewPost = (EditText) view.findViewById(R.id.new_post_input);
        mNewPost.setFocusable(true);
        mNewPost.setFocusableInTouchMode(true);

        mCharCount = (TextView) view.findViewById(R.id.char_count);
        mNewPost.addTextChangedListener(mNewPostWatcher);

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
