package io.github.msh91.arch.util.navigator

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import java.lang.ref.WeakReference

/**
 * Handles navigation between Activities in the app.
 */
interface BaseNavigator<T : AppCompatActivity> {
    val mActivity: WeakReference<T>


    /**
     *
     * @return SupportFragmentManager from activity which provided in constructor
     */
    val sFragmentManager: FragmentManager?
        get() = mActivity.get()?.supportFragmentManager

    /**
     * Start an Activity
     *
     * @param cls the Activity class to be opened.
     * @param bundle which provides from getCallingBundle()
     */
    fun startActivity(cls: Class<*>, bundle: Bundle) {
        if (mActivity.get() != null) {
            val intent = Intent(mActivity.get(), cls)
            intent.putExtras(bundle)
            mActivity.get()!!.startActivity(intent)
        }
    }

    /**
     * Finish an Activity
     */
    fun finishActivity() {
        if (mActivity.get() != null) {
            mActivity.get()!!.finish()
        }
    }

    /**
     * Finish an Activity with a result.
     *
     * @param resultCode the result code to be set when finishing the Activity.
     */
    fun finishActivityWithResult(resultCode: Int, bundle: Bundle) {
        if (mActivity.get() != null) {
            val intent = Intent()
            intent.putExtras(bundle)
            mActivity.get()!!.setResult(resultCode, intent)
            mActivity.get()!!.finish()
        }
    }

    /**
     * Start a new Activity for a result.
     *
     * @param cls         the Activity class to be opened.
     * @param requestCode the request code that will be passed to the opened Activity.
     */
    fun startActivityForResult(cls: Class<*>, requestCode: Int, bundle: Bundle) {
        if (mActivity.get() != null) {
            val intent = Intent(mActivity.get(), cls)
            intent.putExtras(bundle)
            mActivity.get()!!.startActivityForResult(intent, requestCode)
        }
    }

    fun onTokenExpired() {
    }
}
