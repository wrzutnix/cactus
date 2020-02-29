package com.cactus

import android.net.Uri
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner

interface MainContract {

    interface View: LifecycleOwner {
        fun navigateToCamera(tempPictureUri: Uri)
        fun navigateToPicturePicker()
        fun showErrorToast(@StringRes errorStringRes: Int)
        fun switchPictureToProcessingState(pictureUri: Uri)
        fun switchPictureToProcessedState()
    }

    interface Presenter {
        fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
        fun onCameraRequested()
        fun onPhotoGalleryRequested()
        fun onPhotoSelected(photoUri: Uri?)
        fun onPictureTaken()
    }
}
