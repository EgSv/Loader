package ru.startandroid.develop.loader

import android.content.Loader
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.app.LoaderManager.LoaderCallbacks

class MainActivity : AppCompatActivity(),
    LoaderCallbacks<String> {
    private val LOG_TAG = "myLogs"
    private var tvTime: TextView? = null
    private var rgTimeFormat: RadioGroup? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvTime = findViewById<View>(R.id.tvTime) as TextView
        rgTimeFormat = findViewById<View>(R.id.rgTimeFormat) as RadioGroup
        val bndl = Bundle()
        bndl.putString(TimeLoader.ARGS_TIME_FORMAT, timeFormat)
        loaderManager.initLoader(LOADER_TIME_ID, bndl, this)
        lastCheckedId = rgTimeFormat!!.checkedRadioButtonId
    }

    override fun onCreateLoader(id: Int, args: Bundle?): androidx.loader.content.Loader<String> {
        var loader: androidx.loader.content.Loader<String>? = null
        if (id == LOADER_TIME_ID) {
            loader = TimeLoader(this, args)
            Log.d(LOG_TAG, "onCreateLoader: " + loader.hashCode())
        }
        return loader!!
    }

    override fun onLoadFinished(loader: androidx.loader.content.Loader<String>, result: String?) {
        Log.d(LOG_TAG, "onLoadFinished for loader " + loader.hashCode()
                + ", result = " + result);
        tvTime!!.text = result
    }

    override fun onLoaderReset(loader: androidx.loader.content.Loader<String>) {
        Log.d(LOG_TAG, "onLoaderReset for loader " + loader.hashCode())
    }

    fun getTimeClick(v: View?) {
        val loader: Loader<String>
        val id = rgTimeFormat!!.checkedRadioButtonId
        if (id == lastCheckedId) {
            loader = loaderManager.getLoader(LOADER_TIME_ID)
        } else {
            val bndl = Bundle()
            bndl.putString(TimeLoader.ARGS_TIME_FORMAT, timeFormat)
            loader = loaderManager.restartLoader(LOADER_TIME_ID, bndl,
                this)
            lastCheckedId = id
        }
        loader.forceLoad()
    }

    private val timeFormat: String
        get() {
            var result = TimeLoader.TIME_FORMAT_SHORT
            when (rgTimeFormat!!.checkedRadioButtonId) {
                R.id.rdShort -> result = TimeLoader.TIME_FORMAT_SHORT
                R.id.rdLong -> result = TimeLoader.TIME_FORMAT_LONG
            }
            return result
        }

    fun observerClick(v: View?) {}

    companion object {
        const val LOADER_TIME_ID = 1
        var lastCheckedId = 0
    }
}