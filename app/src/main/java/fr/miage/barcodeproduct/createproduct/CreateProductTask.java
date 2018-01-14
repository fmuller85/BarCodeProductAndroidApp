package fr.miage.barcodeproduct.createproduct;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.miage.barcodeproduct.listProduct.ProductListActivity;
import fr.miage.barcodeproduct.model.Document;
import fr.miage.barcodeproduct.model.Product;
import fr.miage.barcodeproduct.model.ProductMapper;
import fr.miage.barcodeproduct.model.remote.BarcodeProductAPI;
import fr.miage.barcodeproduct.model.remote.ProductDocument;
import fr.miage.barcodeproduct.model.remote.ProductStore;
import fr.miage.barcodeproduct.model.remote.UserProduct;
import fr.miage.barcodeproduct.utils.ApplicationPreferences;
import fr.miage.barcodeproduct.utils.ImageUtils;
import fr.miage.barcodeproduct.utils.StorageUtils;
import fr.miage.barcodeproduct.utils.Util;
import io.realm.Realm;
import io.realm.RealmList;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class CreateProductTask extends AsyncTask<Product, Integer, Boolean> {
    private static final String TAG = CreateProductTask.class.getSimpleName();
    private WeakReference<Context> context;
    private String email;
    private String password;
    private Product product;

    CreateProductTask(Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    protected void onPreExecute() {
        ApplicationPreferences appPrefs = new ApplicationPreferences(context.get());
        email = appPrefs.getEmail();
        password = appPrefs.getPassword();
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            goToMainActivity();
        }else{
            Toast.makeText(context.get(), "Something wrong happened, try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCancelled() {
    }

    @Override
    protected Boolean doInBackground(Product... products) {
        boolean success = false;
        product = products[0];
        createDocument();
        try {
            ProductMapper productMapper = new ProductMapper();
            UserProduct userProduct = productMapper.from(product);

            String productId = createProduct(userProduct);

            if(productId != null){
                boolean documentUploaded = uploadFile(productId);
                if(documentUploaded){
                    insertProductToDB(product, productId);
                    success = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }

    private void insertProductToDB(final Product product, final String productId) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                product.setId(UUID.randomUUID().toString());
                product.setSyncId(productId);
                product.setSend(true);
                realm.insert(product);
            }
        });
        realm.close();
    }

    private void createDocument(){
        List<String> documentsPath = Util.stringToList(product.getAttachedFilesPath());
        RealmList<Document> documents = new RealmList<>();
        Document document;
        for(String path : documentsPath){
            document = new Document();
            document.setId(null);
            document.setAbsolutePath(path);
            document.setFileName(StorageUtils.getFileNameFromPath(path));
            document.setDownloaded(true);
            documents.add(document);
        }
        product.setDocuments(documents);
    }

    private void goToMainActivity() {
        Intent intent1 = new Intent(context.get(), ProductListActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.get().startActivity(intent1);
    }

    private String createProduct(UserProduct userProduct) throws IOException {
        Response<UserProduct> response = BarcodeProductAPI
                .getAuthInstance(email, password)
                .getApiService()
                .createProduct(userProduct)
                .execute();

        if(response.code() == 201){
            return response.body().getId();
        }else{
            return null;
        }
    }

    private boolean uploadFile(String productId) throws FileNotFoundException {
        boolean success = true;
        Uri selectedUri;
        File fileToUpload;

        for(Document document : product.getDocuments()){
            if(document.getAbsolutePath() != null){
                File docFile = new File(document.getAbsolutePath());
                selectedUri = Uri.fromFile(docFile);
                String fileExtension
                        = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());

                if(fileExtension.equals("png") || fileExtension.equals("jpg") || fileExtension.equals("jpeg") ){
                    String compressedPicturePath = ImageUtils.compressImage(document.getAbsolutePath());
                    fileToUpload = new File(compressedPicturePath);
                    Log.i(TAG, "Compressed picture path : "+compressedPicturePath);
                    StorageUtils.galleryAddPic(context.get(), compressedPicturePath);
                }else{
                    fileToUpload = docFile;
                }

                if(fileToUpload.length() != 0){
                    RequestBody requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), fileToUpload);
                    // MultipartBody.Part is used to send also the actual file name
                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData("file", fileToUpload.getName(), requestFile);

                    MultipartBody.Part fileName =
                            MultipartBody.Part.createFormData("fileName", document.getFileName());

                    try {
                        Response<ProductDocument> response = BarcodeProductAPI
                                .getAuthInstance(email, password)
                                .getApiService()
                                .addDocumentToProduct(productId, body, fileName)
                                .execute();
                        if (response.isSuccessful()) {
                            document.setId(response.body().getId());
                            success = true;
                        }else{
                            success = false;
                            break;
                        }
                    } catch (IOException e) {
                        success = false;
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }

        return success;
    }
}
