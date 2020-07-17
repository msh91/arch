package io.github.msh91.arch.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.util.Random
import javax.inject.Inject

/**
 * Permission util class for requesting permissions
 */
class PermissionUtil @Inject constructor() {

    private val requestCode: Int by lazy { Random().nextInt(4000) }
    private lateinit var callback: (grantedPermissions: List<String>, deniedPermissions: List<String>) -> Unit

    /**
     * Receives a list of permissions and attempt to grant them by requesting them to user.<br></br>
     * @param activity requested activity
     * @param permissions desired permissions to request grant
     * @param callback callback lambda function needed to send the result back through it
     */
    fun request(
        activity: Activity,
        vararg permissions: String,
        callback: (grantedPermissions: List<String>, deniedPermissions: List<String>) -> Unit
    ) {
        if (permissions.isEmpty()) return
        this.callback = callback
        if (isAllPermissionsGranted(activity, *permissions)) {
            callback(permissions.toList(), emptyList())
            return
        }
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }

    /**
     * Completely same as [request] but instead of activity, you can request permissions inside a fragment
     * @param fragment requested fragment
     * @param permissions desired permissions to request grant
     * @param callback callback lambda function needed to send the result back through it
     */
    fun request(
        fragment: Fragment,
        vararg permissions: String,
        callback: (grantedPermissions: List<String>, deniedPermissions: List<String>) -> Unit
    ) {
        if (permissions.isEmpty()) return
        this.callback = callback

        if (isAllPermissionsGranted(fragment.context!!, *permissions)) {
            callback(permissions.toList(), emptyList())
            return
        }
        fragment.requestPermissions(permissions, requestCode)
    }

    /**
     * Check to see if all of requested permissions are granted or not
     * @param context to check permissions
     * @param permissions requested permissions
     *
     * @return returns true if all of permissions are granted
     */
    private fun isAllPermissionsGranted(context: Context, vararg permissions: String): Boolean =
        permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    /**
     * Get the result of permission request and send back the result through the given callback in
     * [request][.request] method.<br></br>
     *
     * Should be called in your [Activity.onRequestPermissionsResult] or [Fragment.onRequestPermissionsResult].
     * <br></br>
     * The signature is exactly same as above methods
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (this.requestCode != requestCode) return

        val grantedPermissions = permissions.filterIndexed { index, _ ->
            grantResults[index] == PackageManager.PERMISSION_GRANTED
        }
        val deniedPermissions = permissions.filterIndexed { index, _ ->
            grantResults[index] != PackageManager.PERMISSION_GRANTED
        }
        callback(grantedPermissions, deniedPermissions)
    }
}
