package com.example.twomack.flickrapp.recyclerView;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.twomack.flickrapp.data.Photo;
import com.example.twomack.flickrapp.R;




public class MainRecyclerViewAdapter extends PagedListAdapter<Photo, MainViewHolder>{

    private OnPhotoSelectedListener listener;

    public MainRecyclerViewAdapter(OnPhotoSelectedListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    public interface OnPhotoSelectedListener {
        void onPhotoClicked(int position, Photo photo);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_item, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, final int position) {
        final Photo photo = getItem(position);

        if (photo != null) {
            Glide.with(holder.getItemView().getContext())
                    .load(photo.getUrl())
                    .apply(new RequestOptions().placeholder(R.drawable.place_holder_image))
                    .into(holder.getImageView());
        }

        holder.getItemView().setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPhotoClicked(holder.getAdapterPosition(), photo);
            }
        });
    }

    //todo: gain a better understanding of this.
    private static final DiffUtil.ItemCallback<Photo> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Photo>() {
                @Override
                public boolean areItemsTheSame(@NonNull Photo oldPhoto, @NonNull Photo newPhoto) {
                    // User properties may have changed if reloaded from the DB, but ID is fixed
                    return oldPhoto.getId().equals(newPhoto.getId());
                }
                @Override
                public boolean areContentsTheSame(@NonNull Photo oldPhoto, @NonNull Photo newPhoto) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldPhoto.equals(newPhoto);
                }
            };
}
