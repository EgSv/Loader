package ru.startandroid.develop.loader

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.loader.content.Loader
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TimeLoader(context: Context?, args: Bundle?): Loader<String>(context!!) {
    val LOG_TAG = "myLogs"
    val PAUSE = 10

    private var getTimeTask: GetTimeTask? = null
    private var format: String? = null

    init {
        Log.d(LOG_TAG, hashCode().toString() + " create TimeLoader");
        if (args != null)
            format = args.getString(ARGS_TIME_FORMAT);
        if (TextUtils.isEmpty(format))
            format = TIME_FORMAT_SHORT
    }

    override fun onStartLoading() {
        super.onStartLoading()
        Log.d(LOG_TAG, hashCode().toString() + "onStartLoading")
    }

    override fun onStopLoading() {
        super.onStopLoading()
        Log.d(LOG_TAG, hashCode().toString() + "onStopLoading")
    }

    override fun onForceLoad() {
        super.onForceLoad()
        Log.d(LOG_TAG, hashCode().toString() + "onForceLoad")
        if (getTimeTask != null) getTimeTask!!.cancel(true)
        getTimeTask = GetTimeTask()
        getTimeTask!!.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, format)
    }

    override fun onAbandon() {
        super.onAbandon()
        Log.d(LOG_TAG, hashCode().toString() + "onAbandon")
    }

    override fun onReset() {
        super.onReset()
        Log.d(LOG_TAG, hashCode().toString() + "onReset")
    }

    fun getResultFromTask(result: String) {
        deliverResult(result)
    }

    internal inner class GetTimeTask: AsyncTask<String, Unit, String>() {
        override fun doInBackground(vararg params: String?): String {
            Log.d(LOG_TAG, this@TimeLoader.hashCode().toString() + " doInBackground")
            try {
                TimeUnit.SECONDS.sleep(PAUSE.toLong())
            } catch (e: InterruptedException) {
                return null.toString()
            }

            val sdf = SimpleDateFormat(params[0], Locale.getDefault())
            return sdf.format(Date())
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Log.d(LOG_TAG, this@TimeLoader.hashCode().toString() + " onPostExecute " + result)
            getResultFromTask(result!!)
        }
    }

    companion object {
        const val ARGS_TIME_FORMAT = "time_format"
        const val TIME_FORMAT_SHORT = "h:mm:ss a"
        const val TIME_FORMAT_LONG = "yyyy.MM.dd G 'at' HH:mm:ss"
    }
}
