package com.cactus

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.content.pm.PackageManager
import android.os.Environment
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import com.cactus.util.FileManager
import com.cactus.util.PermissionManager
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private val permissionManager = PermissionManager(activity = this)
    private val fileManager = FileManager(context = this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        photo_button.setOnClickListener { requestCamera() }
        gallery_button.setOnClickListener { requestPhotos() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            resultCode != Activity.RESULT_OK -> {}
            requestCode == OpenAction.PHOTOS.requestCode -> requestPhotos()
            requestCode == OpenAction.CAMERA.requestCode -> requestCamera()
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when {
            !allPermissionsGranted(grantResults) -> onMissingPermissions()
            requestCode == OpenAction.PHOTOS.requestCode -> requestPhotos()
            requestCode == OpenAction.CAMERA.requestCode -> requestCamera()
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun allPermissionsGranted(grantResults: IntArray): Boolean {
        return grantResults.filter { result -> result == PackageManager.PERMISSION_GRANTED }.size == grantResults.size
    }

    private fun onMissingPermissions() {
        Toast.makeText(this, R.string.missing_permissions_error, Toast.LENGTH_LONG).show()
    }

    private fun requestCamera() {
        permissionManager.verify(storagePermissions(), OpenAction.CAMERA.requestCode) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileManager.createTempPicture())
            startActivityForResult(intent, OpenAction.CAMERA.requestCode)
        }
    }

    private fun requestPhotos() {
        permissionManager.verify(storagePermissions(), OpenAction.PHOTOS.requestCode) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "file/*"
            startActivityForResult(intent, OpenAction.PHOTOS.requestCode)
        }
    }

    private fun storagePermissions() = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    enum class OpenAction(val requestCode: Int) {
        PHOTOS(requestCode = 1233),
        CAMERA(requestCode = 1234)
    }
}
