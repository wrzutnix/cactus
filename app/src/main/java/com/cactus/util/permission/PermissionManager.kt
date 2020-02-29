package com.cactus.util.permission

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class PermissionManager(private val activity: Activity) {

    fun allGranted(grantResults: IntArray): Boolean {
        return grantResults.filter { result -> result == PackageManager.PERMISSION_GRANTED }.size == grantResults.size
    }

    fun verify(permissions: Array<String>, permissionsRequestCode: Int, onSuccessAction: () -> Unit) {
        if (has(permissions)) onSuccessAction() else request(permissions, permissionsRequestCode)
    }

    private fun has(permissions: Array<String>): Boolean {
        return permissions.all { permission -> has(permission) }
    }

    private fun request(permissions: Array<String>, permissionsRequestCode: Int) {
        ActivityCompat.requestPermissions(activity, permissions, permissionsRequestCode)
    }

    private fun has(permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED
    }
}
