package io.github.msh91.arch.ui.base

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

/**
 * Handles navigation between Activities and fragments in the app.
 */
interface BaseNavigator {
    /**
     * Start an Activity
     *
     * @param activity requested activity
     * @param cls the Activity class to be opened.
     * @param bundle which provides from getCallingBundle()
     */
    fun startActivity(activity: FragmentActivity, cls: Class<*>, bundle: Bundle) {
        val intent = Intent(activity, cls)
        intent.putExtras(bundle)
        activity.startActivity(intent)
    }

    /**
     * Finish an Activity
     * @param activity requested activity
     */
    fun finishActivity(activity: FragmentActivity) {
        activity.finish()
    }

    /**
     * Finish an Activity with a result.
     * @param activity requested activity
     * @param resultCode the result code to be set when finishing the Activity.
     */
    fun finishActivityWithResult(activity: FragmentActivity, resultCode: Int, bundle: Bundle) {
        val intent = Intent()
        intent.putExtras(bundle)
        activity.setResult(resultCode, intent)
        activity.finish()
    }

    /**
     * Start a new Activity for a result.
     *
     * @param activity requested activity
     * @param cls         the Activity class to be opened.
     * @param requestCode the request code that will be passed to the opened Activity.
     */
    fun startActivityForResult(activity: FragmentActivity, cls: Class<*>, requestCode: Int, bundle: Bundle) {
        val intent = Intent(activity, cls)
        intent.putExtras(bundle)
        activity.startActivityForResult(intent, requestCode)
    }

    /**
     * attempt to start login activity if  token expired
     *
     * @param activity requested activity
     */
    fun onTokenExpired(activity: FragmentActivity) {
        // todo: open login activity
    }
}
