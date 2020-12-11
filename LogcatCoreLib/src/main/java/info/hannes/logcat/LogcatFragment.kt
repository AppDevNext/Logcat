package info.hannes.logcat

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import info.hannes.logcat.base.LogBaseFragment
import java.io.IOException
import java.util.*


class LogcatFragment : LogBaseFragment() {

    private val logList = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        live = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @SuppressLint("LogNotTimber")
    override fun readLogFile(): ArrayList<String> {
        try {
            val process = Runtime.getRuntime().exec("logcat -dv time")

            process.inputStream.bufferedReader().use {
                it.readLines().map { line ->
                    val newLine = line.replace(" W/", " W: ")
                            .replace(" E/", " E: ")
                            .replace(" V/", " V: ")
                            .replace(" I/", " I: ")
                            .replace(" D/", " D: ")

                    if (!logList.contains(newLine))
                        logList.add(newLine)
                }
            }

        } catch (e: IOException) {
            Log.e("LoadingLogcatTask", e.message!!)
        }

        return logList
    }

    override fun clearLog() {
        Runtime.getRuntime().exec("logcat -c")
    }

    companion object {
        fun newInstance(targetFileName: String, searchHint: String, logMail: String = ""): LogcatFragment {
            val fragment = LogcatFragment()
            val args = Bundle()
            args.putString(FILE_NAME, targetFileName)
            args.putString(SEARCH_HINT, searchHint)
            args.putString(MAIL_LOGGER, logMail)
            fragment.arguments = args
            return fragment
        }

    }
}