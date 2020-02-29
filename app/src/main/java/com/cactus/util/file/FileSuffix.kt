package com.cactus.util.file

import java.util.*

enum class FileSuffix {
    JPG, PNG;

    fun fullName() = ".${name.toLowerCase(Locale.getDefault())}"
}
