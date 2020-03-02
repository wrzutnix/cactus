package com.cactus

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

object MainDrawableMatcher {

    fun withDrawable(@DrawableRes id: Int) = object : TypeSafeMatcher<View>() {

        override fun describeTo(description: Description) {
            description.appendText("same drawable as $id")
        }

        override fun matchesSafely(view: View): Boolean {
            val expectedBitmap = view.context.getDrawable(id)?.toBitmap()
            return view is ImageView && view.drawable.toBitmap().sameAs(expectedBitmap)
        }
    }

    fun withUri(uri: Uri?) = object : TypeSafeMatcher<View>() {

        override fun describeTo(description: Description) {
            description.appendText("same uri as $uri")
        }

        override fun matchesSafely(view: View): Boolean {
            return uri != null && view is ImageView && view.tag == uri
        }
    }
}