package com.example.twomack.flickrapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.example.twomack.flickrapp.recyclerView.MainRecyclerViewAdapter;
import com.example.twomack.flickrapp.recyclerView.MainViewHolder;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainRecyclerViewAdapter.OnPhotoSelectedListener {

    private ViewModel viewModel;
    private MainRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ViewModel.class);

        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        configureRecyclerView();
        configureObservables();
        viewModel.changePhotos("cats");

    }

    public void configureObservables(){
        viewModel.getPhotos().observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(@Nullable List<Photo> photos) {
                adapter.setPhotoList(photos);
            }
        });
    }

    private void configureRecyclerView() {
        adapter = new MainRecyclerViewAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,
                4,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPhotoClicked(int position) {
        Intent intent = new Intent(this, PhotoClickedActivity.class);

        MainViewHolder viewHolder = (MainViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        ImageView imageView = viewHolder.getImageView();

        Drawable drawable = imageView.getDrawable();
        Bitmap imageToPass = ((BitmapDrawable) drawable).getBitmap();

        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        imageToPass.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] byteArray = bStream.toByteArray();

        intent.putExtra("image", byteArray);
        startActivity(intent);
    }
}
