package com.example.twomack.flickrapp;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.example.twomack.flickrapp.recyclerView.MainViewHolder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class PhotoClickedActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_clicked_view);
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        imageView = (ImageView) findViewById(R.id.photoView);
        imageView.setImageBitmap(bitmap);
    }
}
