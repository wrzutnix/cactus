package com.cactus.permission

import android.content.pm.PackageManager
import org.junit.Assert.assertEquals
import org.junit.Test

class PermissionControllerTest {

    @Test
    fun allGranted() {
        assertEquals(true, PermissionController().allGranted(
            intArrayOf(
                PackageManager.PERMISSION_GRANTED,
                PackageManager.PERMISSION_GRANTED,
                PackageManager.PERMISSION_GRANTED)))
        assertEquals(false, PermissionController().allGranted(
            intArrayOf(
                PackageManager.PERMISSION_GRANTED,
                PackageManager.PERMISSION_DENIED,
                PackageManager.PERMISSION_GRANTED)))
        assertEquals(true, PermissionController().allGranted(intArrayOf()))
    }
}