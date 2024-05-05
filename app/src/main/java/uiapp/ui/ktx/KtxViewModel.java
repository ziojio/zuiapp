package uiapp.ui.ktx;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class KtxViewModel extends ViewModel {
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

    public MutableLiveData<String> date = new MutableLiveData<>(format.format(new Date()));

}
