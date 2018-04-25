package com.example.twomack.flickrapp.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.twomack.flickrapp.data.Photo;
import com.example.twomack.flickrapp.R;

import java.util.List;


public class MainRecyclerViewAdapter  extends RecyclerView.Adapter<MainViewHolder>{

    private List<Photo> photoList;
    private OnPhotoSelectedListener listener;

    public interface OnPhotoSelectedListener {
        void onPhotoClicked(int position, Photo photo);
    }

    public MainRecyclerViewAdapter(OnPhotoSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_item, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, final int position) {

        Glide.with(holder.getItemView().getContext())
                .load(photoList.get(position).getUrl())
                .apply(new RequestOptions().placeholder(R.drawable.place_holder_image))
                .into(holder.getImageView());


        /*
        Glide.with(holder.getItemView().getContext())
                .load(R.drawable.place_holder_image)
                .into(holder.getImageView());
                */

        holder.getItemView().setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPhotoClicked(holder.getAdapterPosition(), photoList.get(position));
            }
        });
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(photoList != null) {
            return photoList.size();
        }
        return 0;
    }
}
