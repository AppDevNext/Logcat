package info.hannes.logcat

import android.annotation.SuppressLint
import android.app.Application
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import com.google.firebase.crashlytics.FirebaseCrashlytics
import info.hannes.crashlytic.CrashlyticsTree
import info.hannes.timber.FileLoggingTree
import timber.log.Timber


class CrashlyticApplication : Application() {

    @SuppressLint("HardwareIds")
    override fun onCreate() {
        super.onCreate()

        externalCacheDir?.let {
            Timber.plant(FileLoggingTree(it, this))
        }

        FirebaseCrashlytics.getInstance().setCustomKey("VERSION_NAME", BuildConfig.VERSIONNAME)
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
