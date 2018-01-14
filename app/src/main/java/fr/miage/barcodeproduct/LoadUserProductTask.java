package fr.miage.barcodeproduct;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import fr.miage.barcodeproduct.listProduct.ProductListActivity;
import fr.miage.barcodeproduct.model.ProductMapper;
import fr.miage.barcodeproduct.model.remote.BarcodeProductAPI;
import fr.miage.barcodeproduct.model.remote.UserProduct;
import fr.miage.barcodeproduct.utils.ApplicationPreferences;
import io.realm.Realm;
import retrofit2.Response;

public class LoadUserProductTask extends AsyncTask<String, Integer, Boolean> {
    private WeakReference<Context> context;
    private String email;
    private String password;

    LoadUserProductTask(Context context, String email, String password) {
        this.context = new WeakReference<>(context);
        this.email = email;
        this.password = password;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            ApplicationPreferences appPrefs = new ApplicationPreferences(context.get());
            appPrefs.saveUser(email, password);
            appPrefs.saveBooleanPreference("loaded", true);
            goToMainActivity();
        }
    }

    @Override
    protected void onCancelled() {
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        boolean success = false;
        try {
            Response<ArrayList<UserProduct>> response = BarcodeProductAPI
                    .getAuthInstance(email, password)
                    .getApiService()
                    .getAllUserProducts()
                    .execute();

            if (response.isSuccessful()) {
                if (response.body() != null) {
                    insertProductToDB(response.body());
                    success = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }

    private void insertProductToDB(final ArrayList<UserProduct> userProducts) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                ProductMapper productMapper = new ProductMapper();
                for (UserProduct userProduct : userProducts) {
                    realm.insert(productMapper.from(userProduct));
                }
            }
        });
        realm.close();
    }

    private void goToMainActivity() {
        Intent intent1 = new Intent(context.get(), ProductListActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.get().startActivity(intent1);
    }
}
