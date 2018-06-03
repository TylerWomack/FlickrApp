package com.example.twomack.flickrapp.recyclerView;

import android.app.Application;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.twomack.flickrapp.R;

public class MainViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView;

    public MainViewHolder(View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.listImageView);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public View getItemView() {
        return itemView;
    }


}
