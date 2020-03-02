package com.cactus.ml

import com.cactus.R
import org.junit.Assert.assertEquals
import org.junit.Test

class MLRatingTest {

    @Test
    fun summary() {
        assertEquals(MLRating(model = "cactus", confidence = 0.2f).summary(), R.string.not_cactus_summary)
        assertEquals(MLRating(model = "cactus", confidence = 0.6f).summary(), R.string.probably_cactus_summary)
        assertEquals(MLRating(model = "cactus", confidence = 0.9f).summary(), R.string.surely_cactus_summary)
        assertEquals(MLRating(model = "flower", confidence = 1.0f).summary(), R.string.not_cactus_summary)
    }
}