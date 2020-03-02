package com.cactus.ml

import com.google.android.gms.vision.label.ImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import org.junit.Assert.assertEquals
import org.junit.Test

class MLControllerTest {

    @Test
    fun topRating() {
        assertEquals(
            MLController().topRating(arrayListOf(
                FirebaseVisionImageLabel(ImageLabel("label", "cactus", 0.8f)),
                FirebaseVisionImageLabel(ImageLabel("label", "flower", 0.15f)),
                FirebaseVisionImageLabel(ImageLabel("label", "leaf", 0.05f))
            )), MLRating(model = "cactus", confidence = 0.8f))
    }
}