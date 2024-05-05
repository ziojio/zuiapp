package uiapp.ui.ktx

import android.os.Bundle
import androidx.activity.viewModels
import androidz.App
import timber.log.Timber
import uiapp.databinding.ActivityKtBinding
import uiapp.ui.base.BaseActivity


class KotlinActivity : BaseActivity() {
    companion object {
        private const val TAG = "KotlinActivity"
    }

    private val viewModel: KtxViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityKtBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ktx.setOnClickListener {
            Timber.d("onCreate: ${viewModel.date.value}")
        }

        App.globalData["a"] = "A"
        Timber.d("onCreate: $App")
        Timber.d("onCreate: ${App.isDebuggable}")
        Timber.d("onCreate: ${App.applicationInfo}")
        Timber.d("onCreate: ${App.packageInfo}")
        Timber.d("onCreate: ${App.globalData}")

    }

}
