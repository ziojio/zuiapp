package uiapp.ui.activity

import android.os.Bundle
import uiapp.databinding.ActivityMainBinding
import uiapp.ui.base.BaseActivity


class KotlinActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())
    }

}
