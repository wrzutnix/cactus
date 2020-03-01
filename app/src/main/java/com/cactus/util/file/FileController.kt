package com.cactus.util.file

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import javax.inject.Inject

class FileController @Inject constructor(private val context: Context) {

    fun createTempPicture(authority: Authority = Authority.CACTUS): Uri? =
        createTempFile().let { file -> FileProvider.getUriForFile(context, authority.code, file) }

    enum class Authority(val code: String) {
        CACTUS(code = "com.cactus.provider")
    }
}
