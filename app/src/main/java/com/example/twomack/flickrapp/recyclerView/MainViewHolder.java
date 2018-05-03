package com.example.twomack.flickrapp.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.twomack.flickrapp.R;

public class MainViewHolder extends RecyclerView.ViewHolder{

    private ImageView imageView;

    MainViewHolder(View itemView) {
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
