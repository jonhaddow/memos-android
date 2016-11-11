package com.example.jon.memoapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This is the adapter which manages the list view items.
 */
public class MemoListAdapter extends ArrayAdapter<Memo> {

    // Hold the activity context.
    private final Context mContext;

    /**
     * Constructor.
     */
    public MemoListAdapter(Context context, int resource, ArrayList<Memo> objects) {
        super(context, resource, objects);
        mContext = context;
    }

    /**
     * Get the layout for each row in a list view.
     *
     * @param position    Position in list view.
     * @param convertView View to be inflated or reused.
     * @param parent      Parent view.
     * @return View to display for that list item.
     */
    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // Check if old view is available to use.
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.memo_item, parent, false);
        }

        // Get current memo properties.
        Memo memo = getItem(position);
        String memoName = memo.getName();
        int memoFlag = memo.getFlag();

        // Get the list layout elements.
        TextView tvMemoContent = (TextView) convertView.findViewById(R.id.tvMemoContent);
        ImageView ivEditMemo = (ImageView) convertView.findViewById(R.id.ivEditMemo);
        ImageView ivDeleteMemo = (ImageView) convertView.findViewById(R.id.ivDeleteMemo);

        // Set the text view as memo content.
        tvMemoContent.setText(memoName);

        // Set on click listeners.
        ivDeleteMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Call main activity method to delete selected memo.
                ((MainActivity) mContext).deleteMemo(position);
            }
        });

        ivEditMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Call main activity method to edit selected memo.
                ((MainActivity) mContext).editMemo(position);
            }
        });

        // Set background colour of list item layout based on memo flag.
        LinearLayout memoRow = (LinearLayout) convertView.findViewById(R.id.memoListRow);
        switch (memoFlag) {
            case MainActivity.FLAG_NORMAL:
                memoRow.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorNormalFlag));
                break;
            case MainActivity.FLAG_IMPORTANT:
                memoRow.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorImportantFlag));
                break;
            case MainActivity.FLAG_URGENT:
                memoRow.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorUrgentFlag));
                break;
        }

        return convertView;
    }
}