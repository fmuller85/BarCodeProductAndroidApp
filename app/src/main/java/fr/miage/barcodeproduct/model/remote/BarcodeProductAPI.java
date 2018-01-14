package fr.miage.barcodeproduct.model.remote;

import java.util.concurrent.TimeUnit;

import fr.miage.barcodeproduct.utils.BasicAuthInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BarcodeProductAPI{
    private static final String BASE_URL = "http://machin.ddns.net:8080/BarCodeProduct/v1/";
    private BarcodeProductApiService apiService;

    public static BarcodeProductAPI getAuthInstance(String email, String password) {
        return new BarcodeProductAPI(email, password);
    }

    public static BarcodeProductAPI getInstance() {
        return new BarcodeProductAPI();
    }

    private BarcodeProductAPI()
    {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(40, TimeUnit.SECONDS);
        httpClient.readTimeout(20, TimeUnit.SECONDS);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(BarcodeProductApiService.class);
    }

    private BarcodeProductAPI(String email, String password)
    {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(40, TimeUnit.SECONDS);
        httpClient.readTimeout(20, TimeUnit.SECONDS);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(new BasicAuthInterceptor(email, password));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(BarcodeProductApiService.class);
    }

    public BarcodeProductApiService getApiService()
    {
        return apiService;
    }
}
