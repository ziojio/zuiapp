package uiapp.data.response;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import io.reactivex.rxjava3.observers.DisposableObserver;
import retrofit2.HttpException;

public abstract class BaseObserver<T> extends DisposableObserver<DataResult<T>> {

    /**
     * 解析数据失败
     */
    public static final int PARSE_ERROR = 1001;
    public static final int BAD_NETWORK = 1002;
    public static final int CONNECT_ERROR = 1003;
    public static final int CONNECT_TIMEOUT = 1004;

    @Override
    protected void onStart() {
        showLoading();
    }

    @Override
    public void onComplete() {
        dismissDialog();
    }

    @Override
    public void onNext(DataResult<T> response) {
        // 业务相关的返回code
        if (response.code == 0) {
            onSuccess(response.data);
        } else {
            onError(response.code, response.message);
        }
    }

    @Override
    public void onError(Throwable e) {
        dismissDialog();
        if (e instanceof HttpException) {
            //   HTTP错误
            onException(BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {
            //   连接错误
            onException(CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {
            //  连接超时
            onException(CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //  解析错误
            onException(PARSE_ERROR);
        } else {
            if (e != null) {
                onError(-1, e.toString());
            } else {
                onError(-1, "未知错误");
            }
        }
    }

    private void onException(int unknownError) {
        switch (unknownError) {
            case CONNECT_ERROR:
                onError(CONNECT_ERROR, "连接错误");
                break;
            case CONNECT_TIMEOUT:
                onError(CONNECT_TIMEOUT, "连接超时");
                break;
            case BAD_NETWORK:
                onError(BAD_NETWORK, "网络问题");
                break;
            case PARSE_ERROR:
                onError(PARSE_ERROR, "解析数据失败");
                break;
            default:
                break;
        }
    }

    public abstract void onSuccess(T t);

    public abstract void onError(int errCode, String errMsg);

    /**
     * 显示Dialog
     */
    public void showLoading() {

    }

    /**
     * 隐藏Dialog
     */
    public void dismissDialog() {

    }

}