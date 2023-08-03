package info.hannes.logcat.sample

import android.annotation.SuppressLint
import android.app.Activity
import android.os.*
import android.provider.Settings
import com.google.firebase.crashlytics.FirebaseCrashlytics
import info.hannes.crashlytic.CrashlyticsTree
import info.hannes.logcat.LoggingApplication
import info.hannes.timber.FileLoggingTree
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


class CrashlyticApplication : LoggingApplication() {

    @SuppressLint("HardwareIds")
    override fun onCreate() {
        super.onCreate()

        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyDialog()
                .penaltyLog()
                .build()
        )

        CoroutineScope(Dispatchers.IO).launch {
            externalCacheDir?.let {
                Timber.plant(FileLoggingTree(it, this@CrashlyticApplication))
            }
        }

        if (BuildConfig.WITH_FIREBASE) {
            Timber.i("I use Firebase")
            FirebaseCrashlytics.getInstance().setCustomKey("VERSION_NAME", info.hannes.logcat.ui.BuildConfig.VERSIONNAME)
            Timber.plant(CrashlyticsTree(Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)))
        } else
            Timber.w("No valid Firebase key was given")

        Timber.d("Debug test")
        Timber.i("Info test")
        Timber.w("Warning test")
        Timber.e("Error test")

        var x = 0
        val runner: Runnable = object : Runnable {
            override fun run() {
                Timber.d("live=$x")
                x++
                Handler(Looper.getMainLooper()).postDelayed(this, 3000)
            }
        }

        Handler(Looper.getMainLooper()).postDelayed(runner, 3000)

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                Timber.v("${activity.javaClass.simpleName} onCreate(Bundle) starting")
            }

            override fun onActivityStarted(activity: Activity) {
                Timber.v("${activity.javaClass.simpleName} onStart() starting")
            }

            override fun onActivityResumed(activity: Activity) {
                Timber.v("${activity.javaClass.simpleName} onResume() starting")
            }

            override fun onActivityPaused(activity: Activity) {
                Timber.v("${activity.javaClass.simpleName} onPause() ending")
            }

            override fun onActivityStopped(activity: Activity) {
                Timber.v("${activity.javaClass.simpleName} onStop() ending")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                Timber.v("${activity.javaClass.simpleName} onSaveInstanceState(Bundle) starting")
            }

            override fun onActivityDestroyed(activity: Activity) {
                Timber.v("${activity.javaClass.simpleName} onDestroy() ending")
            }
        })
    }
}
