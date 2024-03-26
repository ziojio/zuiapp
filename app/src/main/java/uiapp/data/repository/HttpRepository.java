package uiapp.data.repository;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uiapp.UIApp;
import uiapp.data.api.HttpService;
import uiapp.data.response.HttpCallback;

public class HttpRepository {
    private static final HttpRepository sHttpRepository = new HttpRepository();

    private final HttpService httpService;

    private HttpRepository() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (UIApp.debuggable()) {
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
        httpService = retrofit.create(HttpService.class);
    }

    public static HttpRepository getInstance() {
        return sHttpRepository;
    }

    public void getString(@NonNull String url, @NonNull HttpCallback<String> callback) {
        httpService.get(url).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    callback.onSuccess(response.body().string());
                } catch (IOException e) {
                    callback.onFailure(call, e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                callback.onFailure(call, throwable);
            }
        });
    }

}
