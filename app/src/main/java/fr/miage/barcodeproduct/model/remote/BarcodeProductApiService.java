package fr.miage.barcodeproduct.model.remote;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface BarcodeProductApiService {
    @FormUrlEncoded
    @POST("users")
    Call<ResponseBody> createUser(@Field("email") String email, @Field("password") String password);

    @GET("users/{email}/")
    Call<ResponseBody> getUser(@Path("email") String email);

    @GET("products/{codebar}")
    Call<ProductSuggestion> getProduct(@Path("codebar") String codebar);

    @GET("users/products")
    Call<ArrayList<UserProduct>> getAllUserProducts();

    @POST("users/products")
    Call<UserProduct> createProduct(@Body UserProduct userProduct);

    @DELETE("users/products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") String productId);

    @POST("users/products/{id}/shops")
    Call<ResponseBody> createStore(@Path("id") String productId, @Body ProductStore productStore);

    @POST("users/products/{id}/documents")
    @Multipart
    Call<ProductDocument> addDocumentToProduct(@Path("id") String productId,
                                            @Part MultipartBody.Part file,
                                            @Part MultipartBody.Part fileName);

    @GET("users/products/{id}/documents/{docId}/download")
    Call<ResponseBody> downloadDocument(@Path("id") String productId, @Path("docId") String docId);
}
