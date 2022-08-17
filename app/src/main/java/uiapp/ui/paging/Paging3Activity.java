package uiapp.ui.paging;

import android.os.Bundle;

import uiapp.databinding.ActivityPaging3Binding;
import uiapp.ui.base.BaseActivity;

import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;
import androidx.recyclerview.widget.ConcatAdapter;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import kotlinx.coroutines.CoroutineScope;

public class Paging3Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPaging3Binding binding = ActivityPaging3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        PagingViewModel model = getActivityViewModel(PagingViewModel.class);

        ItemPagingAdapter pagingAdapter = new ItemPagingAdapter();
        ConcatAdapter adapter = pagingAdapter.withLoadStateFooter(new ExampleLoadStateAdapter(v -> pagingAdapter.retry()));
        binding.recyclerview.setAdapter(adapter);

        Pager<Integer, ItemData> pager = new Pager<>(
                new PagingConfig(10),
                () -> new ExamplePagingSource("Paging"));

        Flowable<PagingData<ItemData>> flowable = PagingRx.getFlowable(pager);
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(model);
        PagingRx.cachedIn(flowable, viewModelScope);

        Disposable dispose = flowable.subscribe(new Consumer<>() {
            @Override
            public void accept(PagingData<ItemData> pagingData) {
                pagingAdapter.submitData(getLifecycle(), pagingData);
            }
        });
    }

}