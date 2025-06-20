package com.example.safeluggpartner.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun getFileFromUri(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
    inputStream?.use { input ->
        FileOutputStream(tempFile).use { output ->
            input.copyTo(output)
        }
    }
    return tempFile
}
