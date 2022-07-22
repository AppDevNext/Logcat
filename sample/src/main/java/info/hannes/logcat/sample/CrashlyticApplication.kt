package info.hannes.logcat.sample

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.provider.Settings
import com.google.firebase.crashlytics.FirebaseCrashlytics
import info.hannes.crashlytic.CrashlyticsTree
import info.hannes.logcat.LoggingApplication
import info.hannes.timber.FileLoggingTree
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

        externalCacheDir?.let {
            Timber.plant(FileLoggingTree(it, this))
        }

        FirebaseCrashlytics.getInstance().setCustomKey("VERSION_NAME", info.hannes.logcat.ui.BuildConfig.VERSIONNAME)
        Timber.plant(CrashlyticsTree(Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)))

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
    }
}
