package info.hannes.logcat

import android.annotation.SuppressLint
import android.app.Application
import android.os.Handler
import android.provider.Settings
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.core.CrashlyticsCore
import info.hannes.crashlytic.CrashlyticsTree
import info.hannes.timber.FileLoggingTree
import io.fabric.sdk.android.Fabric
import timber.log.Timber


class CrashlyticApplication : Application() {

    @SuppressLint("HardwareIds")
    override fun onCreate() {
        super.onCreate()

        externalCacheDir?.let {
            Timber.plant(FileLoggingTree(it, this))
        }

        val crashlytics = CrashlyticsCore.Builder()
                // .disabled(BuildConfig.DEBUG)
                .disabled(false)
                .build()
        Fabric.with(baseContext, Crashlytics.Builder().core(crashlytics).build(), Answers())
        Crashlytics.setString(BuildConfig.FLAVOR, BuildConfig.VERSION_NAME)
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
                Handler().postDelayed(this, 3000)
            }
        }

        Handler().postDelayed(runner, 3000)
    }
}