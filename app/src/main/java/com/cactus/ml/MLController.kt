package com.cactus.ml

import android.content.Context
import android.net.Uri
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions
import javax.inject.Inject

class MLController @Inject constructor() {

    fun ratingOf(
        context: Context,
        imageUri: Uri,
        onSuccessAction: (rating: MLRating) -> Unit,
        onErrorAction: (e: Exception) -> Unit) =
            labelerOf(optionsOf(defaultModel())).processImage(visionImageFrom(context, imageUri))
                .addOnSuccessListener { labels -> onSuccessAction(topRating(labels)) }
                .addOnFailureListener { exception -> onErrorAction(exception) }

    fun topRating(labels: List<FirebaseVisionImageLabel>): MLRating =
        labels.firstOrNull()?.let { label -> MLRating(label.text, label.confidence) } ?: MLRating()

    private fun defaultModel(): FirebaseAutoMLLocalModel =
        FirebaseAutoMLLocalModel.Builder().setAssetFilePath("manifest.json").build()

    private fun optionsOf(model: FirebaseAutoMLLocalModel): FirebaseVisionOnDeviceAutoMLImageLabelerOptions =
        FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(model)
            .setConfidenceThreshold(0.5f).build()

    private fun labelerOf(options: FirebaseVisionOnDeviceAutoMLImageLabelerOptions): FirebaseVisionImageLabeler =
        FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options)

    private fun visionImageFrom(context: Context, uri: Uri): FirebaseVisionImage =
        FirebaseVisionImage.fromFilePath(context, uri)
}
