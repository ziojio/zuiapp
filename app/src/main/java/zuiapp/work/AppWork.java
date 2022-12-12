package zuiapp.work;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import timber.log.Timber;

public class AppWork {

    public static void doLogWork(Context context) {
        Timber.d("doLogWork");
        String tag = AppLogUploadWork.class.getSimpleName();
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest
                .Builder(AppLogUploadWork.class, 1, TimeUnit.HOURS)
                .addTag(tag)
                .setConstraints(constraints)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build();

        WorkManager workManager = WorkManager.getInstance(context);
        workManager.enqueueUniquePeriodicWork(tag, ExistingPeriodicWorkPolicy.REPLACE, workRequest);
    }
}
