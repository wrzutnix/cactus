package com.cactus.permission

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import javax.inject.Inject

class PermissionController @Inject constructor() {

    fun allGranted(grantResults: IntArray): Boolean {
        return grantResults.filter { result -> result == PackageManager.PERMISSION_GRANTED }.size == grantResults.size
    }

    fun verify(activity: Activity, permissions: Array<String>, permissionsRequestCode: Int, onSuccessAction: () -> Unit) {
        if (has(activity, permissions)) onSuccessAction() else request(activity, permissions, permissionsRequestCode)
    }

    private fun has(activity: Activity, permissions: Array<String>): Boolean {
        return permissions.all { permission -> has(activity, permission) }
    }

    private fun request(activity: Activity, permissions: Array<String>, permissionsRequestCode: Int) {
        ActivityCompat.requestPermissions(activity, permissions, permissionsRequestCode)
    }

    private fun has(activity: Activity, permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED
    }
}
