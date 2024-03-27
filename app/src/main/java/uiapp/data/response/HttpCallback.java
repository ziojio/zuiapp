package uiapp.data.response;

import android.net.ParseException;

import androidx.annotation.NonNull;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public abstract class HttpCallback<T> implements Callback<T> {
    public static final int CONNECT_ERROR = 1001;
    public static final int CONNECT_TIMEOUT = 1002;
    /**
     * Http 响应失败
     */
    public static final int HTTP_ERROR = 1003;
    /**
     * 解析数据失败
     */
    public static final int PARSE_ERROR = 1004;


    @Override
    public void onResponse(@NonNull Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(response.body());
        } else {
            onError(new HttpException(response));
        }
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull Throwable throwable) {
        onError(throwable);
    }

    private void onError(Throwable e) {
        if (e instanceof ConnectException || e instanceof UnknownHostException) {
            onError(CONNECT_ERROR, "连接错误");
        } else if (e instanceof InterruptedIOException) {
            onError(CONNECT_TIMEOUT, "连接超时");
        } else if (e instanceof HttpException) {
            // onError(HTTP_ERROR, "响应失败");
            HttpException ex = (HttpException) e;
            onError(ex.code(), ex.message());
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            onError(PARSE_ERROR, "解析数据失败");
        } else {
            if (e != null) {
                onError(-1, e.getMessage());
            } else {
                onError(-1, "未知错误");
            }
        }
    }

    public abstract void onSuccess(T t);

    public abstract void onError(int errCode, String errMsg);

}