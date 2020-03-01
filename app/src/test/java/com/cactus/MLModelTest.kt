package com.cactus

import com.cactus.ml.MLModel
import org.junit.Test

import org.junit.Assert.*

class MLModelTest {

    @Test
    fun shortName() {
        assertEquals(MLModel.CACTUS.shortName(), "cactus")
        assertEquals(MLModel.FLOWER.shortName(), "flower")
    }
}