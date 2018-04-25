package com.example.twomack.flickrapp.ViewPager;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.twomack.flickrapp.MainActivity;
import com.example.twomack.flickrapp.MainViewModel;
import com.example.twomack.flickrapp.PhotoClickedViewModel;
import com.example.twomack.flickrapp.R;
import com.example.twomack.flickrapp.data.Photo;
import com.example.twomack.flickrapp.networking.Networker;

import java.util.List;

public class PhotoClickedFragment extends Fragment {

    PhotoClickedViewModel viewModel;

    public static PhotoClickedFragment newInstance(int position) {
        PhotoClickedFragment myFragment = new PhotoClickedFragment();

        Bundle args = new Bundle();
        args.putInt("position", position);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle == null){
            return null;
        }
        int position = bundle.getInt("position");

        if (getActivity() == null){
            return null;
        }
        viewModel = ViewModelProviders.of(getActivity()).get(PhotoClickedViewModel.class);

        String url = viewModel.getPhotos().get(position).getUrl();
        String title = viewModel.getPhotos().get(position).getTitle();

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.photo_clicked_view, container, false);
                ImageView imageView = rootView.findViewById(R.id.photoView);
                TextView imageTitle = rootView.findViewById(R.id.picture_title);

        Glide.with(getContext()).load(url).into(imageView);

                if (!title.isEmpty() && title != null){
                    imageTitle.setText(title);
                    imageTitle.setVisibility(View.VISIBLE);
                }else {
                    imageTitle.setVisibility(View.INVISIBLE);
                }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getActivity() == null) {
            return;
        }
    }
}