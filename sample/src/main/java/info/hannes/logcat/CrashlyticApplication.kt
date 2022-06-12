package info.hannes.logcat

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import com.google.firebase.crashlytics.FirebaseCrashlytics
import info.hannes.crashlytic.CrashlyticsTree
import info.hannes.timber.FileLoggingTree
import timber.log.Timber


class CrashlyticApplication : LoggingApplication(delegator = CustomLogger::class.java) {

    private val logger = CustomLogger()

    @SuppressLint("HardwareIds")
    override fun onCreate() {
        super.onCreate()

        externalCacheDir?.let {
            Timber.plant(FileLoggingTree(it, this))
        }

        FirebaseCrashlytics.getInstance().setCustomKey("VERSION_NAME", BuildConfig.VERSIONNAME)
        Timber.plant(CrashlyticsTree(Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)))

        logger.d("Debug test")
        logger.i("Info test")
        logger.w("Warning test")
        logger.e("Error test")

        var x = 0
        val runner: Runnable = object : Runnable {
            override fun run() {
                logger.d("live=$x")
                x++
                Handler(Looper.getMainLooper()).postDelayed(this, 3000)
            }
        }

        Handler(Looper.getMainLooper()).postDelayed(runner, 3000)
    }
}
