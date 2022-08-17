package uiapp.util;

import android.annotation.SuppressLint;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Supplier;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AsyncTask {

    private static final EmptyAction EMPTY_ACTION = new EmptyAction();

    private static final Consumer<Throwable> ERROR_CONSUMER = new ErrorConsumer();


    public static <T> void doAction(Supplier<T> action, Consumer<T> onResult) {
        doAction(action, onResult, ERROR_CONSUMER);
    }

    @SuppressLint("CheckResult")
    public static <T> void doAction(Supplier<T> action, Consumer<T> onResult, Consumer<Throwable> onError) {
        Single.create((SingleOnSubscribe<T>) emitter -> emitter.onSuccess(action.get()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onResult, onError);
    }

    public static void doAction(Action action) {
        doAction(action, EMPTY_ACTION, ERROR_CONSUMER);
    }

    public static void doAction(Action action, Action onComplete) {
        doAction(action, onComplete, ERROR_CONSUMER);
    }

    @SuppressLint("CheckResult")
    public static void doAction(Action action, Action onComplete, Consumer<Throwable> onError) {
        Completable.fromAction(action)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onComplete, onError);
    }

    private static final class EmptyAction implements Action {
        @Override
        public void run() {
        }
    }

    private static final class ErrorConsumer implements Consumer<Throwable> {
        @Override
        public void accept(Throwable tr) {
            if (tr != null) {
                tr.printStackTrace();
            }
        }
    }

}