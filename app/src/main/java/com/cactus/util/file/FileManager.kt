package com.cactus.util.file

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.util.*

class FileManager(private val context: Context) {

    fun createTempPicture(authority: FileAuthority = FileAuthority.CACTUS): Uri? =
        createTempFile().let { file -> FileProvider.getUriForFile(context, authority.code, file) }

    fun createTempFile(suffix: FileSuffix): File? = try {
        File.createTempFile(
            UUID.randomUUID().toString(),
            suffix.fullName(),
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
    } catch (e: IOException) {
        null
    }
}
