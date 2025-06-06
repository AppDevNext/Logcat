package info.hannes.countly

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import info.hannes.logcat.BuildConfig
import ly.count.android.sdk.Countly
import ly.count.android.sdk.CountlyConfig


class Analytics : IAnalytics {

    var countlyInstance: Countly = Countly.sharedInstance()
        private set

    override fun isInitialized(): Boolean {
        return countlyInstance.isInitialized
    }

    override fun recordEvent(event: String) {
        if (isInitialized()) {
            countlyInstance.events().recordEvent(event, segmentation, 1)
        }
    }

    override fun recordError(message: String) {
        if (isInitialized()) {
            countlyInstance.crashes().recordHandledException(RuntimeException(message))
        }
    }

    override fun recordError(throwable: Throwable) {
        if (isInitialized()) {
            countlyInstance.crashes().recordHandledException(throwable)
        }
    }

    override fun recordWarning(message: String) {
        if (isInitialized()) {
            countlyInstance.crashes().recordHandledException(RuntimeException(message))
        }
    }

    override fun onStart(activity: Activity?) {
        if (isInitialized()) {
            countlyInstance.onStart(activity)
        }
    }

    override fun onStop() {
        if (isInitialized()) {
            countlyInstance.onStop()
        }
    }

    companion object {
        val segmentation = mutableMapOf<String, Any>()

        @SuppressLint("HardwareIds")
        fun initAnalytics(context: Context, loggingEnabled: Boolean = false, countlyKey: String, countlyHost: String) {
            var version = ""
            try {
                val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                version = pInfo.versionName.toString()
            } catch (ignore: PackageManager.NameNotFoundException) {
            }
            segmentation["app_version"] = version
            segmentation["logging_version"] = BuildConfig.VERSIONNAME

            val config = CountlyConfig()
                    .setAppKey(countlyKey)
                    .setContext(context)
                    .setDeviceId(Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID))
                    .setServerURL(countlyHost)
                    .setLoggingEnabled(loggingEnabled)
                    .enableAutomaticViewTracking()
                    .setHttpPostForced(true)
                    .enableCrashReporting()
            Countly.sharedInstance().init(config)
        }
    }
}
