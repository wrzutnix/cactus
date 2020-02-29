package com.cactus

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity: AppCompatActivity(), MainContract.View {

    @Inject lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        photo_button.setOnClickListener { presenter.onCameraRequested() }
        gallery_button.setOnClickListener { presenter.onPhotoGalleryRequested() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultIntent: Intent?) = when {
        resultIntent?.data == null || resultCode != Activity.RESULT_OK -> {}
        requestCode == MainOpenAction.PHOTOS.code -> presenter.onPhotoSelected(resultIntent.data)
        requestCode == MainOpenAction.CAMERA.code -> presenter.onCameraRequested()
        else -> super.onActivityResult(requestCode, resultCode, resultIntent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) = when (requestCode) {
        in arrayOf(MainOpenAction.PHOTOS.code, MainOpenAction.CAMERA.code) ->
            presenter.onRequestPermissionsResult(requestCode, permissions, grantResults)
        else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun showErrorToast(@StringRes errorStringRes: Int) =
        Toast.makeText(this, errorStringRes, Toast.LENGTH_LONG).show()

    override fun navigateToCamera(tempPictureUri: Uri) = startActivityForResult(
        Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, tempPictureUri),
        MainOpenAction.CAMERA.code)

    override fun navigateToPicturePicker() = startActivityForResult(
        Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
        MainOpenAction.PHOTOS.code)

    override fun switchPictureToProcessingState(pictureUri: Uri) {
        picture_background.setBackgroundColor(ContextCompat.getColor(this, android.R.color.black))
        picture_progress.visibility = View.VISIBLE
        image.setImageURI(pictureUri)
        image.alpha = 0.5f
    }

    override fun switchPictureToProcessedState() {
        picture_progress.visibility = View.GONE
        image.alpha = 1.0f
    }
}
