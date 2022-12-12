package info.hannes.logcat

import org.jetbrains.annotations.NonNls

interface Logger {
    fun v(@NonNls message: String)
    fun v(t: Throwable, @NonNls message: String)

    fun d(@NonNls message: String)
    fun d(t: Throwable, @NonNls message: String)

    fun i(@NonNls message: String)
    fun i(t: Throwable, @NonNls message: String)

    fun w(@NonNls message: String)
    fun w(t: Throwable, @NonNls message: String)

    fun e(@NonNls message: String)
    fun e(t: Throwable, @NonNls message: String)

    fun trace(owner: Any, @NonNls message: String = "")
}
