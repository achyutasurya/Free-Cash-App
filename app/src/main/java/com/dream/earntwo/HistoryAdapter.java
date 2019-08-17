package com.dream.earntwo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private ArrayList<HistoryModel> dataSet;
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
        ImageView imageViewIcon;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            this.textViewName = itemView.findViewById(R.id.head_text_textview);
            this.textViewMobile =  itemView.findViewById(R.id.status_textview);
            this.imageViewIcon =  itemView.findViewById(R.id.imageRecently);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }

                }
            });*/
        }
    }

    public HistoryAdapter(ArrayList<HistoryModel> data, Context context) {
        this.dataSet = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view, mListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewMobile = holder.textViewMobile;
        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(dataSet.get(listPosition).getHeadText());
        textViewMobile.setText(dataSet.get(listPosition).getStatus());
        Integer drawableImage;
        if(dataSet.get(listPosition).getGateWay() == 0){
            drawableImage = R.drawable.ic_paytm;
        }else if(dataSet.get(listPosition).getGateWay() == 1) {
            drawableImage = R.drawable.ic_tez;
        }else if(dataSet.get(listPosition).getGateWay() == 2) {
            drawableImage = R.drawable.ic_paypal;
        }else if(dataSet.get(listPosition).getGateWay() == 3) {
            drawableImage = R.drawable.ic_wechat;
        }else{
            drawableImage = R.drawable.ic_bitcoin;
        }
        //Glide.with(context).load(dataSet.get(listPosition).getImage()).into(imageView);
        imageView.setImageResource(drawableImage);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}