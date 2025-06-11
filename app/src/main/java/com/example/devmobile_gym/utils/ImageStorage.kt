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

    // !!! ATENÇÃO !!!: REMOVA ESTA LINHA DE IMPORTAÇÃO CONFLITANTE SE ELA ESTIVER NO SEU CÓDIGO
    // import com.google.ai.client.generativeai.type.Schema.Companion.obj // <-- REMOVER ESTA LINHA!


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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
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
            return ""
        }
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP)
    }

    // Descomprime uma String Base64 (que foi previamente comprimida com GZIP) e retorna a String original
    fun gzipDecompress(compressedBase64Data: String): String {
        if (compressedBase64Data.isEmpty()) return ""

        val decodedBytes = try {
            Base64.decode(compressedBase64Data, Base64.NO_WRAP)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            return ""
        }

        val byteArrayInputStream = ByteArrayInputStream(decodedBytes)
        val byteArrayOutputStream = ByteArrayOutputStream() // Renomeado para 'decompressedStream' para evitar conflito de nome
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
            return ""
        }
        // CORRIGIDO: Chame toString() na ByteArrayOutputStream e especifique o charset
        return byteArrayOutputStream.toString(StandardCharsets.UTF_8.name())
    }

    // --- NOVA FUNÇÃO: Tenta descomprimir GZIP e, se falhar, decodifica Base64 diretamente ---
    fun tryDecompressAndDecodeBase64ToBitmap(base64Data: String?): Bitmap? {
        if (base64Data.isNullOrEmpty()) return null

        var bitmap: Bitmap? = null

        // Tenta descomprimir com GZIP primeiro
        try {
            val decompressedString = gzipDecompress(base64Data)
            if (decompressedString.isNotEmpty()) {
                bitmap = base64ToBitmap(decompressedString)
                if (bitmap != null) {
                    println("✅ Imagem decodificada de Base64 GZIP.")
                    return bitmap
                }
            }
        } catch (e: Exception) {
            println("⚠️ Falha na descompressão GZIP, tentando Base64 puro. Erro: ${e.message}")
        }

        // Se GZIP falhou ou o bitmap foi nulo, tenta decodificar Base64 diretamente
        try {
            bitmap = base64ToBitmap(base64Data)
            if (bitmap != null) {
                println("✅ Imagem decodificada de Base64 puro.")
                return bitmap
            }
        } catch (e: Exception) {
            println("🔴 Falha na decodificação de Base64 puro. Erro: ${e.message}")
        }

        println("❌ Não foi possível decodificar Base64 (GZIP ou puro).")
        return null
    }
}