package com.cactus

import android.Manifest
import android.net.Uri
import com.cactus.firebase.FirebasePresenter
import com.cactus.util.file.FileManager
import com.cactus.util.permission.PermissionManager
import io.reactivex.subjects.BehaviorSubject

class MainPresenter(
    private val view: MainContract.View,
    private val domain: Domain): MainContract.Presenter {

    private val latestUriSubject: BehaviorSubject<Uri> = BehaviorSubject.create()

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) = when {
        !domain.permissions.allGranted(grantResults) -> view.showErrorToast(R.string.missing_permissions_error)
        requestCode == MainOpenAction.PHOTOS.code -> onPhotoGalleryRequested()
        requestCode == MainOpenAction.CAMERA.code -> onCameraRequested()
        else -> {}
    }

    override fun onCameraRequested() = domain.permissions.verify(storagePermissions(), MainOpenAction.CAMERA.code) {
        domain.files.createTempPicture()?.let { tempPictureUri ->
            latestUriSubject.onNext(tempPictureUri)
            view.navigateToCamera(tempPictureUri)
        }
    }

    override fun onPhotoGalleryRequested() = domain.permissions.verify(storagePermissions(), MainOpenAction.PHOTOS.code) {
        view.navigateToPicturePicker()
    }

    override fun onPhotoSelected(photoUri: Uri?) = photoUri?.let { uri ->
        latestUriSubject.onNext(uri)
        onPhotoUpdateRequested()
    } ?: view.showErrorToast(R.string.cant_process_photo_error)

    override fun onPictureTaken() = onPhotoUpdateRequested()

    private fun onPhotoUpdateRequested() {
        view.switchPictureToProcessedState()
    }

    private fun storagePermissions() = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    data class Domain(
        val files: FileManager,
        val permissions: PermissionManager,
        val firebase: FirebasePresenter
    )
}
