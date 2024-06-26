package com.example.budgetbonsai.ui.add

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.example.budgetbonsai.data.local.Receipts
import com.example.budgetbonsai.utils.findFormattedNumber
import com.example.budgetbonsai.utils.firstLine
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import java.io.File
import java.util.Collections
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Date

class ReceiptsViewModel {
    val textDeviceDetector: FirebaseVisionTextRecognizer
    lateinit var imageURI: Uri

    init {
        textDeviceDetector = FirebaseVision.getInstance().getOnDeviceTextRecognizer()
    }

    fun uploadIntent(): Intent {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        return intent
    }

    fun cameraIntent(context: Context): Intent {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val filephoto = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )
        imageURI = FileProvider.getUriForFile(context, "com.example.budgetbonsai.provider", filephoto)
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI)
        return pictureIntent
    }

    fun getReceipts(text: String): Receipts {
        val originalResult = text.findFormattedNumber()
        if (originalResult.isEmpty()) return Receipts()
        else {
            val receipts = Receipts()
            receipts.name = text.firstLine()
            val totalF = Collections.max(originalResult)
            receipts.total = totalF.toString()

            return receipts
        }
    }
}