package uiapp.ui.ktx

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidz.util.App
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityKtBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var n = 0;
        binding.ktx.setOnClickListener {
            when (n++ % 2) {
                0 -> {
                    Timber.d("onCreate: ${viewModel.date.value}")
                }
                1 -> {
                    Timber.d("onCreate: ${viewModel3.date.value}")
                }
            }
        }

        App.globalData["a"] = "A"
        Timber.d("onCreate: $App")
        Timber.d("onCreate: ${App.isDebuggable}")
        Timber.d("onCreate: ${App.applicationInfo}")
        Timber.d("onCreate: ${App.packageInfo}")
        Timber.d("onCreate: ${App.globalData}")

    }

}
