package com.example.paw.myapplication.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paw.myapplication.R;

public class CustomViewHolder extends RecyclerView.ViewHolder {

    public final TextView titleView;
    public final TextView dateView;
    public final TextView descriptionView;
    public final ImageView handleView;

    public CustomViewHolder(View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.textView2);
        dateView = itemView.findViewById(R.id.textView3);
        descriptionView = itemView.findViewById(R.id.textView4);
        handleView = itemView.findViewById(R.id.imageView2);
    }

}