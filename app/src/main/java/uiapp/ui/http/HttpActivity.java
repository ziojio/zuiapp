package uiapp.ui.http;

import android.os.Bundle;

import java.io.IOException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import uiapp.data.api.HttpService;
import uiapp.databinding.ActivityHttpBinding;
import uiapp.ui.base.BaseActivity;

public class HttpActivity extends BaseActivity {
    private ActivityHttpBinding binding;

    private HttpService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHttpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.input.setText("http://www.baidu.com");
        binding.request.setOnClickListener(v -> {
            String text = binding.input.getText().toString();
            if (text.isBlank()) {
                showToast("text is Blank");
            } else {
                request(text);
            }
        });

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(client)
                .build();

        apiService = retrofit.create(HttpService.class);

    }

    private void request(String url) {
        apiService.get(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<>() {
                    @Override
                    public void onNext(@NonNull ResponseBody body) {
                        try {
                            String result = body.string();
                            binding.text.setText(result);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        binding.text.setText(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
