package fr.miage.barcodeproduct.createproduct;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.parceler.Parcels;

import fr.miage.barcodeproduct.R;
import fr.miage.barcodeproduct.barcodereader.BarcodeCaptureActivity;
import fr.miage.barcodeproduct.model.Product;
import fr.miage.barcodeproduct.model.remote.BarcodeProductAPI;
import fr.miage.barcodeproduct.model.remote.ProductSuggestion;
import fr.miage.barcodeproduct.utils.ApplicationPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A screen that allow user to save a product
 */
public class SaveProductActivity extends AppCompatActivity {
    private ApplicationPreferences appPrefs;

    private Button btNextStep;
    private Button btSuggestion;
    private EditText etProductName;
    private TextView tvBarcode;
    private ProgressBar loadSuggestionProgressBar;
    private FrameLayout suggestionContainer;

    private String barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_product);

        appPrefs = new ApplicationPreferences(this);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        barcode = getIntent().getStringExtra(BarcodeCaptureActivity.BarcodeObject);

        tvBarcode = findViewById(R.id.tv_barcode);
        btNextStep = findViewById(R.id.bt_next_step);
        etProductName = findViewById(R.id.product_name);
        loadSuggestionProgressBar = findViewById(R.id.load_suggestion_progress);
        suggestionContainer = findViewById(R.id.suggestion_container);

        tvBarcode.setText(barcode);
        btNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etProductName.getText().length() > 0) {
                    Intent intent = new Intent(SaveProductActivity.this, SaveProductDataActivity.class);
                    Product product = createProduct(barcode, etProductName.getText().toString());
                    intent.putExtra("product", Parcels.wrap(product));
                    startActivity(intent);
                } else {
                    etProductName.setError("Name is required");
                }
            }
        });

        searchProduct(appPrefs.getEmail(), appPrefs.getPassword(), barcode);
    }

    /**
     * Create a button with the given suggestion
     */
    private void createSuggestion(@NonNull final String productName) {
        btSuggestion = new Button(this);
        btSuggestion.setBackground(AppCompatResources.getDrawable(this, R.color.btn_logut_bg));
        btSuggestion.setTextColor(getResources().getColor(R.color.white));
        btSuggestion.setText(productName);
        btSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaveProductActivity.this, SaveProductDataActivity.class);
                Product product = createProduct(barcode, productName);
                intent.putExtra("product", Parcels.wrap(product));
                startActivity(intent);
            }
        });

        loadSuggestionProgressBar.setVisibility(View.GONE);
        suggestionContainer.removeAllViews();
        suggestionContainer.addView(btSuggestion);
    }

    /**
     * Create a button with the given suggestion
     */
    private void displayNoSuggestion() {
        btSuggestion = new Button(this);
        btSuggestion.setBackground(AppCompatResources.getDrawable(this, R.color.input_login_hint));
        btSuggestion.setTextColor(getResources().getColor(R.color.white));
        btSuggestion.setText("No suggestion");

        loadSuggestionProgressBar.setVisibility(View.GONE);
        suggestionContainer.removeAllViews();
        suggestionContainer.addView(btSuggestion);
    }

    public Product createProduct(String codebar, String name) {
        Product product = new Product();
        product.setCodebar(codebar);
        product.setName(name);
        return product;
    }

    private void showProgress(final boolean show) {
        // simply show and hide the relevant UI components.
        loadSuggestionProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void searchProduct(final String email, final String password, final String codebar) {
        BarcodeProductAPI.getAuthInstance(email, password).getApiService().getProduct(codebar)
                .enqueue(new Callback<ProductSuggestion>() {
                    @Override
                    public void onResponse(@NonNull Call<ProductSuggestion> call, @NonNull Response<ProductSuggestion> response) {
                        System.out.println("response = [" + response.code() + "]");
                        if (response.isSuccessful()) {
                            if (response.body() == null) {
                                displayNoSuggestion();
                            } else {
                                createSuggestion(response.body().getName());
                            }
                        } else {
                            displayNoSuggestion();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ProductSuggestion> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        displayNoSuggestion();
                    }
                });
    }
}
