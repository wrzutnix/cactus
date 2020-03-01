package com.cactus.ml

import java.util.*

enum class MLModel {
    CACTUS, FLOWER, LEAF, GRASS;

    fun shortName(): String {
        return name.toLowerCase(Locale.getDefault())
    }
}
