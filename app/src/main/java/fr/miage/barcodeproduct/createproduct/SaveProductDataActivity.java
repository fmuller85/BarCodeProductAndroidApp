package fr.miage.barcodeproduct.createproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import fr.miage.barcodeproduct.R;
import fr.miage.barcodeproduct.model.Product;
import fr.miage.barcodeproduct.widget.DatePickerFragment;

public class SaveProductDataActivity extends AppCompatActivity {

    private Button btNextStep;
    private EditText etProductPrice;

    private Button btPickPurchaseDate;
    private Button btPickCommercialWarrantyDate;
    private Button btPickConstructorWarrantyDate;

    private TextView tvPurchaseDate;
    private TextView tvCommercialWarranty;
    private TextView tvConstructorWarranty;

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_product_data);

        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        product = (Product) Parcels.unwrap(getIntent().getParcelableExtra("product"));

        btNextStep = findViewById(R.id.bt_next_step);
        etProductPrice = findViewById(R.id.product_price);
        btPickPurchaseDate = findViewById(R.id.bt_pick_purchase_date);
        btPickCommercialWarrantyDate = findViewById(R.id.bt_pick_commercial_warranty_date);
        btPickConstructorWarrantyDate = findViewById(R.id.bt_pick_constructor_warranty_date);

        tvPurchaseDate = findViewById(R.id.tv_purchase_date);
        tvCommercialWarranty = findViewById(R.id.tv_commercial_warranty_date);
        tvConstructorWarranty = findViewById(R.id.tv_constructor_warranty_date);

        btPickPurchaseDate.setOnClickListener(btPurchaseDateClickListener);
        btPickCommercialWarrantyDate.setOnClickListener(btCommercialWarrantyDateClickListener);
        btPickConstructorWarrantyDate.setOnClickListener(btConstructorWarrantyDateClickListener);

        btNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct(etProductPrice.getText().toString(),
                        tvPurchaseDate.getText().toString(),
                        tvCommercialWarranty.getText().toString(),
                        tvConstructorWarranty.getText().toString());
                Intent intent = new Intent(SaveProductDataActivity.this, SaveProductStoreActivity.class);
                intent.putExtra("product", Parcels.wrap(product));
                startActivity(intent);
            }
        });

        initForm();
    }

    public void displayDatePicker(@IdRes int textViewId) {
        DialogFragment datePickerFragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt("textViewId", textViewId);
        datePickerFragment.setArguments(args);
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void updateProduct(String price, String purchaseDate, String commercialWarranty, String constructeurWarranty) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd");
            if(price.isEmpty()){
                product.setPrice(0.0);
            }else{
                product.setPrice(Double.valueOf(price));
            }
            product.setPurchaseDate(sdf.parse(purchaseDate));
            product.setCommercialWarrantyDate(sdf.parse(commercialWarranty));
            product.setConstructorWarrantyDate(sdf.parse(constructeurWarranty));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initForm(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        String dateFormat = "%s/%s/%s";
        tvPurchaseDate.setText(String.format(dateFormat,year,month,day));
        tvCommercialWarranty.setText(String.format(dateFormat,year+1,month,day));
        tvConstructorWarranty.setText(String.format(dateFormat,year+2,month,day));
    }

    View.OnClickListener btPurchaseDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            displayDatePicker(R.id.tv_purchase_date);
        }
    };

    View.OnClickListener btCommercialWarrantyDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            displayDatePicker(R.id.tv_commercial_warranty_date);
        }
    };

    View.OnClickListener btConstructorWarrantyDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            displayDatePicker(R.id.tv_constructor_warranty_date);
        }
    };
}
