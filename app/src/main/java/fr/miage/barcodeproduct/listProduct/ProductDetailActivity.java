package fr.miage.barcodeproduct.listProduct;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

import fr.miage.barcodeproduct.R;
import fr.miage.barcodeproduct.model.Document;
import fr.miage.barcodeproduct.model.Product;
import fr.miage.barcodeproduct.model.remote.BarcodeProductAPI;
import fr.miage.barcodeproduct.utils.ApplicationPreferences;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView tvProductBarcode;
    private TextView tvProductName;
    private TextView tvProductPrice;
    private TextView tvProductPurchaseDate;
    private TextView tvProductCommercialWarrantyDate;
    private TextView tvProductConstructorWarrantyDate;

    private TextView tvStoreName;
    private TextView tvStoreCity;
    private TextView tvStoreAddress;
    private TextView tvStoreWebsite;

    private View formOnlineStore;
    private View formOfflineStore;

    private LinearLayout documentContainer;

    Realm realm;
    Product product;
    RealmList<Document> documents;

    String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        init();

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Log.i("ProductDetailActivity", "onCreate");

        if(savedInstanceState != null){
            Log.i("ProductDetailActivity", "savedInstanceState");
            productId = savedInstanceState.getString("path");

        }

    }

    private void init() {
        tvProductBarcode = findViewById(R.id.tv_barcode);
        tvProductName = findViewById(R.id.tv_product_name);
        tvProductPrice = findViewById(R.id.tv_product_price);
        tvProductPurchaseDate = findViewById(R.id.tv_purchase_date);
        tvProductCommercialWarrantyDate = findViewById(R.id.tv_commercial_warranty_date);
        tvProductConstructorWarrantyDate = findViewById(R.id.tv_constructor_warranty_date);

        formOnlineStore = findViewById(R.id.form_store_online);
        formOfflineStore = findViewById(R.id.form_store_not_online);

        tvStoreName = findViewById(R.id.store_name);
        tvStoreAddress = findViewById(R.id.store_address);
        tvStoreCity = findViewById(R.id.store_city);
        tvStoreWebsite = findViewById(R.id.store_website);

        documentContainer = findViewById(R.id.document_container);
    }

    private void displayProductDetail() {
        tvProductBarcode.setText(product.getCodebar());
        tvProductName.setText(product.getName());
        tvProductPrice.setText(String.valueOf(product.getPrice()));
        tvProductPurchaseDate.setText(displayDate(product.getPurchaseDate()));
        tvProductCommercialWarrantyDate.setText(displayDate(product.getCommercialWarrantyDate()));
        tvProductConstructorWarrantyDate.setText(displayDate(product.getConstructorWarrantyDate()));

        if (product.getStore() != null) {
            if (product.getStore().isOnline()) {
                formOfflineStore.setVisibility(View.GONE);
                formOnlineStore.setVisibility(View.VISIBLE);

                tvStoreWebsite.setText(product.getStore().getWebsite());
            } else {
                formOnlineStore.setVisibility(View.GONE);
                formOfflineStore.setVisibility(View.VISIBLE);

                tvStoreName.setText(product.getStore().getName());
                tvStoreCity.setText(product.getStore().getCity());
                tvStoreAddress.setText(product.getStore().getAddress());
            }
        }

        displayDocuments();
    }

    public void displayDocuments() {
        documentContainer.removeAllViews();

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.ic_file)
                .centerCrop();

        View documentView;
        View ivDeleteFile;
        for (final Document document : product.getDocuments()) {

            documentView = LayoutInflater.from(this)
                    .inflate(R.layout.list_item_files, documentContainer, false);
            ivDeleteFile = documentView.findViewById(R.id.iv_delete_file);
            ivDeleteFile.setVisibility(View.INVISIBLE);

            documentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!document.isDownloaded()) {
                        DownloadFileTask downloadFileTask = new DownloadFileTask(ProductDetailActivity.this);
                        downloadFileTask.execute(document.getId());
                    }else{
                        Intent intent = new Intent(ProductDetailActivity.this, FullScreenImageActivity.class);
                        intent.putExtra("path",document.getAbsolutePath());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            });

            ImageView ivFile = documentView.findViewById(R.id.iv_file);

            if (document.isDownloaded()) {
                Glide.with(this)
                        .load(document.getAbsolutePath())
                        .apply(requestOptions)
                        .into(ivFile);
            } else {
                Glide.with(this)
                        .load(R.drawable.ic_file_download_white_36dp)
                        .into(ivFile);
            }

            documentContainer.addView(documentView);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("ProductDetailActivity", "onStart");

        if(productId == null){
            productId = getIntent().getStringExtra("productId");
        }
        Log.i("ProductDetailActivity", "onStart product : "+productId);

        realm = Realm.getDefaultInstance();
        product = realm.where(Product.class).equalTo("id", productId).findFirst();
        if (product != null){
            documents = product.getDocuments();
            documents.addChangeListener(callback);

            displayProductDetail();
        }
    }

    @Override
    protected void onStop() {
        Log.i("ProductDetailActivity", "onStop");
        if (product != null) {
            product.removeAllChangeListeners();
            documents.removeAllChangeListeners();
            documents = null;
            product = null;
        }
        if (realm != null && !realm.isClosed()) {
            realm.close();
            realm = null;
        }
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete: {
                deleteProduct();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private String displayDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        if (date != null) {
            return sdf.format(date);
        } else {
            return "Not defined";
        }
    }

    private void deleteProduct() {
        ApplicationPreferences appPrefs = new ApplicationPreferences(this);
        BarcodeProductAPI.getAuthInstance(appPrefs.getEmail(), appPrefs.getPassword())
                .getApiService()
                .deleteProduct(product.getSyncId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.code() == 200) {
                            showSucces();
                            deleteProductFromDB();
                            finish();
                        } else {
                            showError();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        showError();
                    }
                });
    }

    private void deleteProductFromDB() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                product.deleteFromRealm();
            }
        });
    }

    private void showSucces() {
        Toast.makeText(this, "Product deleted !", Toast.LENGTH_SHORT).show();
    }

    private void showError() {
        Toast.makeText(this, "Product not deleted, try again !", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        Log.i("ProductDetailActivity", "onDestroy");
        super.onDestroy();
    }

    private RealmChangeListener<RealmList<Document>> callback = new RealmChangeListener<RealmList<Document>>() {
        @Override
        public void onChange(@NonNull RealmList<Document> documents) {
            Log.i("ProductDetailActivity", "onChange");
            displayDocuments();
        }
    };
}
