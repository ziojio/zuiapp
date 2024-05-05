package uiapp.ui.ktx

import android.annotation.SuppressLint
import android.app.Application
import android.content.ComponentCallbacks
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.PackageInfoFlags
import android.content.res.Configuration
import android.os.Build


object App : Application() {
    private lateinit var app: Application

    @JvmField
    val globalData = mutableMapOf<String, Any>()

    @JvmStatic
    val isDebuggable: Boolean by lazy {
        applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE == 2
    }

    @JvmStatic
    val packageInfo: PackageInfo by lazy {
        val info = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(packageName, PackageInfoFlags.of(0))
        } else {
            packageManager.getPackageInfo(packageName, 0)
        }
        // consistent with app
        info.applicationInfo = applicationInfo
        return@lazy info
    }

    @JvmStatic
    val attachApplication: Application by lazy { app }

    @JvmStatic
    fun attachApplication(application: Application) {
        app = application
        attachBaseContext(application.baseContext)
    }

    @SuppressLint("MissingSuperCall")
    override fun onConfigurationChanged(newConfig: Configuration) {
        app.onConfigurationChanged(newConfig)
    }

    @SuppressLint("MissingSuperCall")
    override fun onLowMemory() {
        app.onLowMemory()
    }

    @SuppressLint("MissingSuperCall")
    override fun onTrimMemory(level: Int) {
        app.onTrimMemory(level)
    }

    override fun registerComponentCallbacks(callback: ComponentCallbacks) {
        app.registerComponentCallbacks(callback)
    }

    override fun unregisterComponentCallbacks(callback: ComponentCallbacks) {
        app.unregisterComponentCallbacks(callback)
    }

    override fun registerActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks) {
        app.registerActivityLifecycleCallbacks(callback)
    }

    override fun unregisterActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks) {
        app.unregisterActivityLifecycleCallbacks(callback)
    }

    override fun registerOnProvideAssistDataListener(callback: OnProvideAssistDataListener) {
        app.registerOnProvideAssistDataListener(callback)
    }

    override fun unregisterOnProvideAssistDataListener(callback: OnProvideAssistDataListener) {
        app.unregisterOnProvideAssistDataListener(callback)
    }

    override fun equals(other: Any?): Boolean {
        return app == other
    }

    override fun hashCode(): Int {
        return app.hashCode()
    }

    override fun toString(): String {
        return app.toString()
    }
}
