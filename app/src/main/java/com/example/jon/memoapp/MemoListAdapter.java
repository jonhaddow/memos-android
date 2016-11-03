package com.example.jon.memoapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

public class MemoListAdapter extends ArrayAdapter<String> {

    private Context mContext;

    public MemoListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);

        mContext = context;
    }

    /**
     *  Get the layout for each row in a listview.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // Check if old view is available to use.
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.memo_item, parent, false);
        }

        // Set the memo content for this row.
        TextView text = (TextView) convertView.findViewById(R.id.tvMemoContent);
        text.setText(getItem(position));

        return convertView;
    }
}