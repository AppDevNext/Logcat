package info.hannes.crashlytic

import com.crashlytics.android.Crashlytics

object CrashlyticsTrackerDelegate {

    /**
     * Logs string to Crashlytics key values.
     *
     * @param key   key that will be tracked.
     * @param value value that is tracked.
     */
    fun setString(key: String, value: String) {
        try {
            Crashlytics.setString(key, value)
        } catch (ignored: IllegalStateException) {
        }
    }

    /**
     * Logs Integer to Crashlytics key values.
     *
     * @param key   key that will be tracked.
     * @param value value that is tracked.
     */
    fun setInt(key: String, value: Int?) {
        try {
            Crashlytics.setInt(key, value!!)
        } catch (ignored: IllegalStateException) {
        }
    }

    /**
     * Logs boolean to Crashlytics key values.
     *
     * @param key   key that will be tracked.
     * @param value value that is tracked.
     */
    fun setBool(key: String, value: Boolean) {
        try {
            Crashlytics.setBool(key, value)
        } catch (ignored: IllegalStateException) {
        }
    }

}
