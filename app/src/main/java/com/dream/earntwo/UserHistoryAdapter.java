package com.dream.earntwo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

class UserHistoryAdapter extends RecyclerView.Adapter<UserHistoryAdapter.MyViewHolder> {

    private ArrayList<UserHistoryModel> dataSet;
    Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewMobile;
        TextView imageViewIcon;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            this.textViewName = itemView.findViewById(R.id.head_text_textview);
            this.textViewMobile =  itemView.findViewById(R.id.status_textview);
            this.imageViewIcon =  itemView.findViewById(R.id.user_point);
        }
    }

    public UserHistoryAdapter(ArrayList<UserHistoryModel> data, Context context) {
        this.dataSet = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_history_item, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view, mListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewMobile = holder.textViewMobile;
        TextView textViewPoints = holder.imageViewIcon;

        textViewName.setText(dataSet.get(listPosition).getActivity());
        textViewMobile.setText(dataSet.get(listPosition).getDate());
        textViewPoints.setText(String.valueOf(dataSet.get(listPosition).getPoint()));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}