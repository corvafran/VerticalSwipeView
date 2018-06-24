package com.example.corva.myapplication;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageAdapter extends VerticalSwipeView.Adapter {
    private List<String> images;

    @Override
    public void onBindView(View view, int position) {
        Glide.with(view.getContext())
                .load(images.get(position))
                .into( ((ImageView) view));
    }


    @Override
    public int getItemCount() {
        return images.size();
    }

    @Override
    public int getViewLayoutResource() {
        return R.layout.view_example;
    }

    @Override
    public int getEmptyViewLayoutResource() {
        return R.layout.view_example;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
