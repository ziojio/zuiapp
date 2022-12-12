package zuiapp.ui.activity;

import android.os.Bundle;
import android.view.View;

import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import androidz.util.UiThreadUtil;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeEmitter;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.core.MaybeOnSubscribe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
import zuiapp.databinding.ActivityRxjavaBinding;

public class RxJavaActivity extends BaseActivity implements View.OnClickListener {
    private ActivityRxjavaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRxjavaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.request.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (binding.request == v) {
            test();
        }
    }

    private void test() {

    }

    private void callable() {
        Timber.d("callable");
        Flowable.fromCallable(() -> {
                    Thread.sleep(1000); //  imitate expensive computation
                    return "Done";
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(System.out::println, Throwable::printStackTrace);
    }

    private void interval() {
        Timber.d("interval");
        Observable.intervalRange(1, 10, 1000, 10, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .map(v -> v * v)
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Long integer) {
                        Timber.d("onNext " + integer + " isMainThread: " + UiThreadUtil.isMainThread());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.d(e, "onError ");
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("onComplete ");
                    }
                });
        Timber.d("interval End");
    }

    private void range() {
        Timber.d("range ");
        Observable.range(1, 10)
                .subscribeOn(Schedulers.computation())
                .map(v -> {
                    Timber.d("map isMainThread: " + UiThreadUtil.isMainThread());
                    return v * v;
                })
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Timber.d("onNext " + integer + " isMainThread: " + UiThreadUtil.isMainThread());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.d(e, "onError ");
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("onComplete ");
                    }
                });
        Timber.d("range End");
    }

    private void maybe() {
        Maybe.create(new MaybeOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull MaybeEmitter<String> emitter) throws Throwable {
                        Timber.d("subscribe ");
                        Timber.d("isMainThread: " + UiThreadUtil.isMainThread());
                        // emitter.onSuccess("A");
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new MaybeObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull String s) {
                        Timber.d("onSuccess " + s);
                        Timber.d("isMainThread: " + UiThreadUtil.isMainThread());
                    }

                    @Override
                    public void onError(Throwable t) {
                        Timber.d(t, "onError ");
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("onComplete ");
                    }
                });
    }

    private void observable() {
        Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                        Timber.d("subscribe ");
                        Timber.d("isMainThread: " + UiThreadUtil.isMainThread());

                        emitter.onNext("A");
                        Thread.sleep(200);
                        emitter.onNext("B");
                        Thread.sleep(200);
                        emitter.onNext("C");
                        Thread.sleep(200);
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Timber.d("onNext " + s);
                        Timber.d("isMainThread: " + UiThreadUtil.isMainThread());
                    }

                    @Override
                    public void onError(Throwable t) {
                        Timber.d(t, "onError ");
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("onComplete ");
                    }
                });
    }

    private void flowable() {
        Flowable.create(new FlowableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull FlowableEmitter<String> emitter) throws Throwable {
                        Timber.d("subscribe ");
                        Timber.d("isMainThread: " + UiThreadUtil.isMainThread());

                        emitter.onNext("A");
                        Thread.sleep(200);
                        // the consumer might have cancelled the flow
                        if (emitter.isCancelled()) {
                            return;
                        }

                        emitter.onNext("B");
                        Thread.sleep(200);
                        emitter.onNext("C");
                        Thread.sleep(200);
                        emitter.onComplete();
                    }
                }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .subscribe(new FlowableSubscriber<>() {
                    @Override
                    public void onSubscribe(@NonNull Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(String s) {
                        Timber.d("onNext " + s);
                        Timber.d("isMainThread: " + UiThreadUtil.isMainThread());
                    }

                    @Override
                    public void onError(Throwable t) {
                        Timber.d(t, "onError ");
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("onComplete ");
                    }
                });
    }

    private void single() {
        Timber.d("single");
        Single.create(new SingleOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull SingleEmitter<String> emitter) {
                        Timber.d("subscribe ");
                        Timber.d("isMainThread: " + UiThreadUtil.isMainThread());
                        emitter.onSuccess("single-item");
                    }
                })
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull String s) {
                        Timber.d("onSuccess " + s);
                        Timber.d("isMainThread: " + UiThreadUtil.isMainThread());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.d(e);
                        Timber.d("isMainThread: " + UiThreadUtil.isMainThread());
                    }
                });
    }

    private void complete() {
        Timber.d("execute");
        Completable.fromRunnable(() -> {
                    Timber.d("Completable fromRunnable");
                    Timber.d("isMainThread: " + UiThreadUtil.isMainThread());
                    throw new IllegalStateException("Completable Error");
                })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("Done!");
                        Timber.d("isMainThread: " + UiThreadUtil.isMainThread());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.d(e, "onError");
                        Timber.d("isMainThread: " + UiThreadUtil.isMainThread());
                    }
                });

    }

}
