package com.cactus.ml

import androidx.annotation.StringRes
import com.cactus.R

data class MLRating(val model: String = "none", val confidence: Int = 0) {

    constructor(model: String, confidence: Float): this(model, (confidence * 100).toInt())

    @StringRes fun summary(): Int = when {
        !isCactusModel() -> R.string.not_cactus_summary
        confidence in Range.LOW.maxRating..Range.DECENT.maxRating -> R.string.probably_cactus_summary
        else -> R.string.surely_cactus_summary
    }

    private fun isCactusModel(): Boolean =
        model == MLModel.CACTUS.shortName() && confidence >= Range.LOW.maxRating

    /**
     * Names the range of rating confidence
     * @param maxRating - maximal rating percentage for given range
     */
    enum class Range(val maxRating: Int) {
        LOW(maxRating = 50), DECENT(maxRating = 75), GOOD(maxRating = 100)
    }
}