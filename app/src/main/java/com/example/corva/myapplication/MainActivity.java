package com.example.corva.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private VerticalSwipeView verticalSwipeView;
    private String[] image = {"https://www.ecestaticos.com/imagestatic/clipping/b19/9c8/b199c8d80de10570337e05163a1fbe50/imagen-sin-titulo.jpg?mtime=1529612743",
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d9/Lionel_Messi_2018.png/245px-Lionel_Messi_2018.png",
                          "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ3_4uVxippb-zrfJyjx4n_Lj9YKNJZkt4p_vfUoseW3CTMaOBTOA",
                          "https://www.infobae.com/new-resizer/vXoiuWUZrsHAbzCMxZaSLahd9Ac=/600x0/filters:quality(100)/s3.amazonaws.com/arc-wordpress-client-uploads/infobae-wp/wp-content/uploads/2018/05/28131359/Mauricio-Macri-pide-racionalidad-a-Peronistas.jpg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageAdapter imageAdapter = new ImageAdapter();
        imageAdapter.setImages(Arrays.asList(image));
        verticalSwipeView = findViewById(R.id.swipeSelector);
        verticalSwipeView.setAdapter(imageAdapter);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verticalSwipeView.swipeView();
            }
        });
    }
}
