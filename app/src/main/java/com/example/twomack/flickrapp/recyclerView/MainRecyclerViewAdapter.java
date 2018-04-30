package com.example.twomack.flickrapp.recyclerView;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.twomack.flickrapp.data.Photo;
import com.example.twomack.flickrapp.R;

import java.util.List;


public class MainRecyclerViewAdapter  extends PagedListAdapter<Photo, MainViewHolder>{

    //extends RecyclerView.Adapter<MainViewHolder>

    private List<Photo> photoList;
    private OnPhotoSelectedListener listener;


    public MainRecyclerViewAdapter() {
        super(DIFF_CALLBACK);
    }

    /*
    protected MainRecyclerViewAdapter(@NonNull DiffUtil.ItemCallback<Photo> diffCallback) {
        super(diffCallback);
    }
    */

    public interface OnPhotoSelectedListener {
        void onPhotoClicked(int position, Photo photo);
    }

    /*
    public MainRecyclerViewAdapter(OnPhotoSelectedListener listener) {
        this.listener = listener;
    }
    */


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

    public static final DiffUtil.ItemCallback<Photo> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Photo>() {
                @Override
                public boolean areItemsTheSame(@NonNull Photo oldPhoto, @NonNull Photo newPhoto) {
                    // User properties may have changed if reloaded from the DB, but ID is fixed
                    return oldPhoto.getId() == newPhoto.getId();
                }
                @Override
                public boolean areContentsTheSame(@NonNull Photo oldPhoto, @NonNull Photo newPhoto) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldPhoto.equals(newPhoto);
                }
            };
}
