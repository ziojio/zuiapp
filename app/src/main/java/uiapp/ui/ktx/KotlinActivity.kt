package uiapp.ui.ktx

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import timber.log.Timber
import uiapp.databinding.ActivityKtBinding
import uiapp.ui.base.BaseActivity


class KotlinActivity : BaseActivity() {
    companion object {
        private const val TAG = "KotlinActivity"
    }

    private val viewModel: KtxViewModel by viewModels()

    private val viewModel3: KtxViewModel by lazy {
        ViewModelProvider(this).get(KtxViewModel::class.java)
    }

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityKtBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var n = 0;
        binding.ktx.setOnClickListener {
            when (n++ % 2) {
                0 -> {
                    Log.d(TAG, "onCreate: ${viewModel.date.value}")
                    Timber.d("onCreate: ${viewModel.date.value}")
                }
                1 -> {
                    Log.d(TAG, "onCreate: ${viewModel3.date.value}")
                    Timber.d("onCreate: ${viewModel3.date.value}")
                }
            }
        }

        App.globalData["a"] = "A"
        Log.d(TAG, "onCreate: $App")
        Log.d(TAG, "onCreate: ${App.attachApplication}")
        Log.d(TAG, "onCreate: ${App.isDebuggable}")
        Log.d(TAG, "onCreate: ${App.applicationInfo}")
        Log.d(TAG, "onCreate: ${App.packageInfo}")
        Log.d(TAG, "onCreate: ${App.globalData}")

    }

}
