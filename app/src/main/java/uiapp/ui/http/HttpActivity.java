package uiapp.ui.http;

import android.os.Bundle;

import com.google.gson.JsonElement;

import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import uiapp.data.api.RxHttpService;
import uiapp.data.repository.HttpRepository;
import uiapp.data.response.BaseObserver;
import uiapp.data.response.HttpCallback;
import uiapp.databinding.ActivityHttpBinding;
import uiapp.ui.base.BaseActivity;

public class HttpActivity extends BaseActivity {
    private ActivityHttpBinding binding;

    private RxHttpService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHttpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.input.setText("https://mdn.github.io/learning-area/javascript/apis/fetching-data/can-store/products.json");
        binding.request.setOnClickListener(v -> {
            String text = binding.input.getText().toString();
            if (text.isBlank()) {
                showToast("text is Blank");
            } else {
                request(text);
            }
        });
        binding.rxRequest.setOnClickListener(v -> {
            String text = binding.input.getText().toString();
            if (text.isBlank()) {
                showToast("text is Blank");
            } else {
                rxrequest(text);
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

        apiService = retrofit.create(RxHttpService.class);
    }

    private void request(String url) {
        HttpRepository.getInstance().getJson(url, new HttpCallback<>() {
            @Override
            public void onSuccess(JsonElement body) {
                binding.text.setText(body.toString());
            }

            @Override
            public void onError(int errCode, String errMsg) {
                binding.text.setText(String.format(Locale.getDefault(), "code=%d, msg=%s", errCode, errMsg));
            }
        });
    }

    private void rxrequest(String url) {
        apiService.getJson(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<>() {
                    @Override
                    public void onNext(@NonNull JsonElement jsonElement) {
                        try {
                            String result = jsonElement.toString();
                            binding.text.setText(result);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(int errCode, String errMsg) {
                        binding.text.setText(errMsg);
                    }
                });
    }

}
