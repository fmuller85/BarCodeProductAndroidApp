package fr.miage.barcodeproduct.listProduct;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import fr.miage.barcodeproduct.R;

public class FullScreenImageActivity extends AppCompatActivity {

    private ImageView ivFullScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ivFullScreen = findViewById(R.id.iv_full_screen);

        String imagePath = getIntent().getStringExtra("path");
        if(imagePath != null){
            displayInFullScreen(imagePath);
        }
    }

    private void displayInFullScreen(@NonNull String imagePath){
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.ic_file);

        Glide.with(this)
                .load(imagePath)
                .apply(requestOptions)
                .into(ivFullScreen);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                super.onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
