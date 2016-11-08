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

public class MemoListAdapter extends ArrayAdapter<Memo> {

    private Context mContext;

    public MemoListAdapter(Context context, int resource, ArrayList<Memo> objects) {
        super(context, resource, objects);
        mContext = context;
    }

    /**
     * Get the layout for each row in a listview.
     */
    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // Check if old view is available to use.
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.memo_item, parent, false);
        }

        // get current memo
        Memo memo = getItem(position);
        String memoName = null;
        int memoFlag = 0;
        if (memo != null) {
            memoName = memo.getName();
            memoFlag = memo.getFlag();
        }

        // Set the memo content for this row.
        TextView text = (TextView) convertView.findViewById(R.id.tvMemoContent);
        text.setText(memoName);

        // Get the edit and delete icons and set OnClickListeners
        ImageView ivEditMemo = (ImageView) convertView.findViewById(R.id.ivEditMemo);
        ImageView ivDeleteMemo = (ImageView) convertView.findViewById(R.id.ivDeleteMemo);

        ivDeleteMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) mContext).deleteMemo(position);
            }
        });

        ivEditMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) mContext).editMemo(position);
            }
        });

        // Set background colour based on memo flag
        LinearLayout memoRow = (LinearLayout) convertView.findViewById(R.id.memoListRow);

        if (memoFlag == MainActivity.FLAG_IMPORTANT) {
            memoRow.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorImportantFlag));
        } else if (memoFlag == MainActivity.FLAG_URGENT) {
            memoRow.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorUrgentFlag));
        }

        return convertView;
    }

}