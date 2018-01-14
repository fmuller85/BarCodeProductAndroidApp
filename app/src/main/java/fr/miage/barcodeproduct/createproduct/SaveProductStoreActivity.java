package fr.miage.barcodeproduct.createproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import org.parceler.Parcels;

import fr.miage.barcodeproduct.R;
import fr.miage.barcodeproduct.model.Product;
import fr.miage.barcodeproduct.model.Store;

public class SaveProductStoreActivity extends AppCompatActivity {

    private Button btNextStep;

    private EditText etStoreName;
    private EditText etStoreAddress;
    private EditText etStoreCity;
    private EditText etStoreWebsite;

    private Switch swStoreOnline;
    private View storeOnlineForm;
    private View storeOfflineForm;

    private Product product;
    private Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_product_store);

        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        product = (Product) Parcels.unwrap(getIntent().getParcelableExtra("product"));

        btNextStep = findViewById(R.id.bt_next_step);
        etStoreName = findViewById(R.id.store_name);
        etStoreAddress = findViewById(R.id.store_address);
        etStoreCity = findViewById(R.id.store_city);
        etStoreWebsite = findViewById(R.id.store_website);
        etStoreWebsite.setText("www.");

        storeOfflineForm = findViewById(R.id.form_store_not_online);
        storeOnlineForm = findViewById(R.id.form_store_online);

        swStoreOnline = findViewById(R.id.sw_online);
        swStoreOnline.setChecked(false);
        swStoreOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swStoreOnline.isChecked()){
                    displayStoreOnlineForm();
                }else{
                    displayStoreOfflineForm();
                }
            }
        });

        btNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Store store;
                if(swStoreOnline.isChecked()){
                    store = createOnlineStore();
                }else{
                    store = createOfflineStore();
                }
                product.setStore(store);
                Intent intent = new Intent(SaveProductStoreActivity.this, ChooseFileActivity.class);
                intent.putExtra("product", Parcels.wrap(product));
                startActivity(intent);
            }
        });
    }

    private void displayStoreOnlineForm(){
        storeOnlineForm.setVisibility(View.VISIBLE);
        storeOfflineForm.setVisibility(View.INVISIBLE);
    }

    private void displayStoreOfflineForm(){
        storeOnlineForm.setVisibility(View.INVISIBLE);
        storeOfflineForm.setVisibility(View.VISIBLE);
    }

    public Store createOnlineStore(){
        Store store = new Store();
        store.setWebsite(etStoreWebsite.getText().toString());
        store.setOnline(true);
        return store;
    }

    public Store createOfflineStore(){
        Store store = new Store();
        store.setName(etStoreName.getText().toString());
        store.setAddress(etStoreAddress.getText().toString());
        store.setCity(etStoreCity.getText().toString());
        store.setOnline(false);
        return store;
    }
}
