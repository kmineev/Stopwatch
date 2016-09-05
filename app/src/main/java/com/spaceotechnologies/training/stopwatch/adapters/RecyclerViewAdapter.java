package com.spaceotechnologies.training.stopwatch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.spaceotechnologies.training.stopwatch.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kostez on 16.08.2016.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.SimpleViewHolder> {

    public static final int LAST_POSITION = -1 ;
    private final Context mContext;
    private List<String> mData;

    public void add(String s,int position) {
        position = position == LAST_POSITION ? getItemCount()  : position;
        mData.add(position,s);
        notifyItemInserted(position);
    }

    public void remove(int position){
        if (position == LAST_POSITION && getItemCount() > 0)
            position = getItemCount() -1 ;

        if (position > LAST_POSITION && position < getItemCount()) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;

        public SimpleViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.simple_text);
        }
    }

    public RecyclerViewAdapter(Context context, ArrayList<String> data) {
        mContext = context;
        if (data != null)
            mData = (data);
        else mData = new ArrayList<String>();
    }

    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.simple_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        holder.title.setText(mData.get(position));
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Position =" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}