package com.example.jon.memoapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MemoListAdapter extends ArrayAdapter<String> {

    private static final String TAG = "MemoListAdapter";


    private Context mContext;
//    private DeleteBtnClickListener deleteClickListener = null;

    public MemoListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);

//        this.deleteClickListener = deleteClickListener;
        mContext = context;
    }

    /**
     * Get the layout for each row in a listview.
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


        // Get the edit and delete icons
        ImageView ivEditMemo = (ImageView) convertView.findViewById(R.id.ivEditMemo);
        ImageView ivDeleteMemo = (ImageView) convertView.findViewById(R.id.ivDeleteMemo);

        // Set tag as current list position
        ivEditMemo.setTag(position);
        ivDeleteMemo.setTag(position);

        ivDeleteMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                ((MainActivity) mContext).deleteMemo(position);
            }
        });

        ivEditMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                ((MainActivity) mContext).editMemo(position);
            }
        });

        return convertView;
    }

}