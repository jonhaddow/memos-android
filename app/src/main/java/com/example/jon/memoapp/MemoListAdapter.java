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

/**
 * Created by jon on 02/11/16.
 */

public class MemoListAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private List<String> memos;

    public MemoListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);

        mContext = context;
        memos = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.memo_item, null);

        TextView text = (TextView) view.findViewById(R.id.tvMemoContent);

        text.setText(getItem(position));
        return view;
    }
}