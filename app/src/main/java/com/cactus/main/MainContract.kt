package com.cactus.main

import android.app.Activity
import android.net.Uri
import com.cactus.ml.MLRating

interface MainContract {

    interface View {
        fun navigateToCamera(tempPictureUri: Uri)
        fun navigateToPicturePicker()
        fun onPermissionNotGranted()
        fun onPictureSelectionError()
        fun onPictureProcessingState(pictureUri: Uri)
        fun onPictureProcessedState(rating: MLRating)
        fun onPictureErrorState(error: Exception)
    }

    interface Presenter {
        fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults: IntArray)
        fun onCameraRequested()
        fun onPhotoGalleryRequested()
        fun onPhotoSelected(photoUri: Uri?)
        fun onPictureTaken()
    }
}
