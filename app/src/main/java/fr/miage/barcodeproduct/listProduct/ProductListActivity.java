package fr.miage.barcodeproduct.listProduct;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import fr.miage.barcodeproduct.LoginActivity;
import fr.miage.barcodeproduct.R;
import fr.miage.barcodeproduct.barcodereader.BarcodeCaptureActivity;
import fr.miage.barcodeproduct.listProduct.adapter.ProductListViewAdapter;
import fr.miage.barcodeproduct.model.Product;
import fr.miage.barcodeproduct.utils.ApplicationPreferences;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class ProductListActivity extends AppCompatActivity {
    ApplicationPreferences appPrefs;

    ListView lvProduct;
    FloatingActionButton fabAddProduct;

    Realm realm;
    ProductListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        appPrefs = new ApplicationPreferences(this);

        lvProduct = findViewById(R.id.lv_product);
        fabAddProduct = findViewById(R.id.fab_add_product);

        fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this, BarcodeCaptureActivity.class);
                startActivity(intent);
            }
        });

        adapter = new ProductListViewAdapter(this);
        lvProduct.setAdapter(adapter);
        lvProduct.setOnItemClickListener(onItemClickListener);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Product product = products.get(position);
            if(product != null){
                Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                intent.putExtra("productId", product.getId());
                startActivity(intent);
            }else{
                Toast.makeText(ProductListActivity.this,"Something wrong happened, try again",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout: {
                logout();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private OrderedRealmCollectionChangeListener<RealmResults<Product>> callback = new OrderedRealmCollectionChangeListener<RealmResults<Product>>() {
        @Override
        public void onChange(@NonNull RealmResults<Product> results, OrderedCollectionChangeSet changeSet) {
            if (changeSet == null) {
                // The first time async returns with an null changeSet.
                adapter.updateList(results);
            } else {
                // Called on every update.
                adapter.updateList(results);
            }
        }
    };

    private RealmResults<Product> products;

    @Override
    public void onStart() {
        super.onStart();
        Realm realm = Realm.getDefaultInstance();
        products = realm
                .where(Product.class)
                .sort("purchaseDate", Sort.DESCENDING)
                .findAllAsync();
        products.addChangeListener(callback);
    }

    @Override
    public void onStop() {
        if (products != null) {
            products.removeAllChangeListeners(); // remove all registered listeners
        }
        if (realm != null) {
            realm.close();
        }
        super.onStop();
    }

    private void logout() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.deleteAll();
            }
        });
        realm.close();

        appPrefs.logout();
        Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent1);
        finish();
    }


}
