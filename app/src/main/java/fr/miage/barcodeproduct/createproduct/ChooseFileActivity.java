package fr.miage.barcodeproduct.createproduct;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import fr.miage.barcodeproduct.R;
import fr.miage.barcodeproduct.createproduct.adapter.AttachedFilesListAdapter;
import fr.miage.barcodeproduct.model.Product;
import fr.miage.barcodeproduct.utils.StorageUtils;
import fr.miage.barcodeproduct.utils.Util;

public class ChooseFileActivity extends AppCompatActivity implements AttachedFilesListAdapter.OnItemClickListener {
    /**
     * Constant used in onResultActivity to
     */
    public static final int REQUEST_TAKE_PHOTO = 0x2;

    private CreateProductTask createProductTask;

    private RecyclerView recyclerView;
    private FloatingActionButton fabAddDoc;
    private FloatingActionButton fabTakePic;
    private FloatingActionButton fabAddPicFromGallery;
    private Button btCreateProduct;
    private ProgressBar progressBar;
    private View fileForm;

    private AttachedFilesListAdapter adapter;
    private ArrayList<String> filesPath;

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_file);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        product = Parcels.unwrap(getIntent().getParcelableExtra("product"));

        fabAddDoc = findViewById(R.id.fab_insert_doc);
        fabAddPicFromGallery = findViewById(R.id.fab_insert_pic);
        fabTakePic = findViewById(R.id.fab_take_pic);
        btCreateProduct = findViewById(R.id.bt_next_step);
        progressBar = findViewById(R.id.send_progress);
        fileForm = findViewById(R.id.file_form);

        btCreateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendProduct();
            }
        });

        fabAddDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilePickerBuilder.getInstance().setMaxCount(1)
                        .setActivityTheme(R.style.AppTheme)
                        .pickFile(ChooseFileActivity.this);
            }
        });

        fabAddPicFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilePickerBuilder.getInstance().setMaxCount(1)
                        .setActivityTheme(R.style.AppTheme)
                        .pickPhoto(ChooseFileActivity.this);
            }
        });

        fabTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(REQUEST_TAKE_PHOTO);
            }
        });

        if (savedInstanceState == null) {
            filesPath = new ArrayList<>();
        } else {
            filesPath = savedInstanceState.getStringArrayList("filesPath");
            System.out.println("SAVED INSTANCE STATE " + savedInstanceState.toString());
        }
        //filesPath.add("Default path");

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView = findViewById(R.id.grid_attached_files);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AttachedFilesListAdapter(this, filesPath);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void sendProduct() {
        createProductTask = new CreateProductTask(this);
        product.setAttachedFilesPath(Util.listToString(filesPath));
        createProductTask.execute(product);
    }

    @Override
    public void onItemClicked(View view, int pos) {
        filesPath.remove(pos);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemLongClick(View view, int pos) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    filesPath.addAll(0, data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                }
                break;
            case FilePickerConst.REQUEST_CODE_DOC:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    filesPath.addAll(0, data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                }
                break;
            case REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK && mCurrentPhotoPath != null) {
                    filesPath.add(mCurrentPhotoPath);
                    StorageUtils.galleryAddPic(this, mCurrentPhotoPath);
                }
                break;
        }
        System.out.println("onActivityResult SIZE : " + filesPath.size());
        adapter.notifyDataSetChanged();
    }

    private String mCurrentPhotoPath;

    public void dispatchTakePictureIntent(int requestResult) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = StorageUtils.createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCurrentPhotoPath = photoFile.getAbsolutePath();
                //Uri photoURI = Uri.fromFile(photoFile);
                Uri photoURI = FileProvider.getUriForFile(this,
                        "fr.miage.barcodeproduct.fileprovider",
                        photoFile);
                Log.i(ChooseFileActivity.class.getSimpleName(), "PHOTO URI : " + photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, requestResult);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("filesPath", filesPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (createProductTask != null) {
            createProductTask.cancel(true);
            createProductTask = null;
        }
        super.onStop();
    }

    void showProgress(final boolean show) {
        // simply show and hide the relevant UI components.
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        fileForm.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
