// utils/ImageUtils.kt
package com.example.devmobile_gym.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

object ImageUtils {

    // Converte uma Uri para um Bitmap
    fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Converte um Bitmap para uma String Base64
    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        // Ajuste a qualidade aqui para a compressão JPEG
        // Um valor entre 70-85 é comum para fotos de perfil.
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        // Use Base64.NO_WRAP para evitar quebras de linha na string, o que é bom para armazenamento em BD
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    // Converte uma String Base64 de volta para um Bitmap
    fun base64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.NO_WRAP)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
    }

    // --- NOVAS FUNÇÕES: Compressão e Descompressão GZIP para Strings ---

    // Comprime uma String usando GZIP e retorna o resultado como uma String Base64
    fun gzipCompress(data: String): String {
        if (data.isEmpty()) return ""

        val byteArrayOutputStream = ByteArrayOutputStream()
        try {
            GZIPOutputStream(byteArrayOutputStream).use { gzip ->
                gzip.write(data.toByteArray(StandardCharsets.UTF_8))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return "" // Retorna string vazia ou lança uma exceção
        }
        // Retorna os bytes comprimidos codificados em Base64 para armazenamento
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP)
    }

    // Descomprime uma String Base64 (que foi previamente comprimida com GZIP) e retorna a String original
    fun gzipDecompress(compressedBase64Data: String): String {
        if (compressedBase64Data.isEmpty()) return ""

        val decodedBytes = try {
            Base64.decode(compressedBase64Data, Base64.NO_WRAP)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            return "" // Retorna string vazia se a codificação Base64 for inválida
        }

        val byteArrayInputStream = ByteArrayInputStream(decodedBytes)
        val byteArrayOutputStream = ByteArrayOutputStream()
        try {
            GZIPInputStream(byteArrayInputStream).use { gzip ->
                val buffer = ByteArray(1024)
                var len: Int
                while (gzip.read(buffer).also { len = it } != -1) {
                    byteArrayOutputStream.write(buffer, 0, len)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return "" // Retorna string vazia ou lança uma exceção
        }
        // Retorna a string descomprimida
        return byteArrayOutputStream.toString(StandardCharsets.UTF_8.name())
    }
}