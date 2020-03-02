package com.cactus.main

import android.Manifest
import android.app.Activity
import android.net.Uri
import androidx.core.content.FileProvider
import com.cactus.ml.MLController
import com.cactus.permission.PermissionController
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class MainPresenter @Inject constructor(private val view: MainContract.View, private val domain: Domain): MainContract.Presenter {

    val latestPictureUriSubject: BehaviorSubject<Uri> = BehaviorSubject.create()

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) = when {
            !domain.permissions.allGranted(grantResults) -> view.onPermissionNotGranted()
            requestCode == MainOpenAction.PHOTOS.code -> onPhotoGalleryRequested()
            requestCode == MainOpenAction.CAMERA.code -> onCameraRequested()
            else -> {}
        }

    override fun onCameraRequested() =
        domain.permissions.verify(activity(), storagePermissions(), MainOpenAction.CAMERA.code) {
            createTempFile()
                .let { file -> FileProvider.getUriForFile(view.context(), Authority.CACTUS.code, file) }
                .let { tempPictureUri -> latestPictureUriSubject.onNext(tempPictureUri).also { view.navigateToCamera(tempPictureUri) }}
            }

    override fun onPhotoGalleryRequested() =
        domain.permissions.verify(activity(), storagePermissions(), MainOpenAction.PHOTOS.code) {
            view.navigateToPicturePicker()
        }

    override fun onPhotoSelected(photoUri: Uri?) =
        photoUri?.let { uri ->
            latestPictureUriSubject.onNext(uri).also { onPhotoUpdateRequested() }
        } ?: view.onPictureSelectionError()

    private fun onPhotoUpdateRequested() {
        latestPictureUriSubject.value?.also { imageUri ->
            view.onPictureProcessingState(imageUri)
            domain.ml.ratingOf(
                view.context(), imageUri,
                { rating -> view.onPictureProcessedState(rating) },
                { exception -> view.onPictureErrorState(exception) })
        }
    }

    override fun onPictureTaken() = onPhotoUpdateRequested()

    private fun activity() = view as Activity

    private fun storagePermissions() = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    data class Domain @Inject constructor(
        val permissions: PermissionController,
        val ml: MLController
    )

    enum class Authority(val code: String) {
        CACTUS(code = "com.cactus.provider")
    }
}
