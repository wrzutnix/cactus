package com.cactus

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.ActivityResultFunction
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.intent.IntentCallback
import androidx.test.runner.intent.IntentMonitorRegistry
import com.cactus.main.MainActivity
import com.cactus.matcher.ImageViewHasDrawableMatcher.withDrawable
import com.cactus.matcher.ImageViewHasDrawableMatcher.withUri
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainScreenTest {

    @get:Rule
    val intentsRule = IntentsTestRule(MainActivity::class.java)

    @Before
    fun stubCameraIntent() {
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(ActivityResult(Activity.RESULT_OK, Intent()))
        intending(hasAction(Intent.ACTION_GET_CONTENT)).respondWith(createImageCaptureActivityResultStub())
    }

    @Test
    fun openCameraOnClick() {
        onView(withId(R.id.image)).check(matches(withDrawable(R.drawable.cactus)))
        IntentMonitorRegistry.getInstance().addIntentCallback(intentCallback)
        onView(withId(R.id.photo_button)).perform(click())
        onView(withId(R.id.image)).check(matches(withUri(intentsRule.activity.presenter.latestPictureUriSubject.value)))
        IntentMonitorRegistry.getInstance().removeIntentCallback(intentCallback)
    }

    @Test
    fun openGalleryOnClick() {
        onView(withId(R.id.image)).check(matches(withDrawable(R.drawable.cactus)))
        onView(withId(R.id.gallery_button)).perform(click())
        onView(withId(R.id.image)).check(matches(withDrawable(R.drawable.test)))
    }

    private val intentCallback = IntentCallback { intent -> when(intent.action) {
        MediaStore.ACTION_IMAGE_CAPTURE -> intent.extras?.getParcelable<Uri>("output")?.let { uri ->
            InstrumentationRegistry.getInstrumentation().targetContext.let { context ->
                BitmapFactory.decodeResource(context.resources, R.drawable.test)
                    .compress(Bitmap.CompressFormat.JPEG, 100, context.contentResolver.openOutputStream(uri))
            }}
    }}

    private fun createImageCaptureActivityResultStub(): ActivityResult {
        return ActivityResult(Activity.RESULT_OK, Intent().setData(testDrawableUri(
            InstrumentationRegistry.getInstrumentation().targetContext.resources
        )))
    }

    private fun testDrawableUri(resources: Resources): Uri {
        return Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(resources.getResourcePackageName(R.drawable.test))
            .appendPath(resources.getResourceTypeName(R.drawable.test))
            .appendPath(resources.getResourceEntryName(R.drawable.test))
            .build()
    }
}
