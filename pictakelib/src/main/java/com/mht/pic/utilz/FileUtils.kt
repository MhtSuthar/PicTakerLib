package com.mht.pic.utilz

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


object FileUtils {

    private val BASE_IMAGE_NAME = "pic_prefix_"
    var mSelectedImagePath : String = ""

    fun savePicture(context: Context, bitmap: Bitmap, imageSuffix: String): String {
        val savedImage = getTemporalFile(context, imageSuffix + ".jpeg")
        var fos: FileOutputStream? = null
        if (savedImage.exists()) {
            savedImage.delete()
        }
        try {
            fos = FileOutputStream(savedImage.path)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        } catch (e: java.io.IOException) {
            e.printStackTrace()
        } finally {
            if (!bitmap.isRecycled) {
                bitmap.recycle()
            }
            if (fos != null) {
                try {
                    fos!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        return savedImage.absolutePath
    }

    fun getTemporalFile(context: Context, payload: String): File {
        return File(context.externalCacheDir, BASE_IMAGE_NAME + payload)
    }

    fun getFilePathFromUri(context: Context, uri: Uri): String? {
        var input: InputStream? = null
        if (uri.authority != null) {
            try {
                input = context.contentResolver.openInputStream(uri)
                val bmp = BitmapFactory.decodeStream(input)
                return savePicture(context, bmp, ""+uri.path.hashCode())
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } finally {
                try {
                    if(input != null)
                        input!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return null
    }

    fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "PicTaker" + timeStamp + "_"
        val sdCard = File(Environment.getExternalStorageDirectory().toString()+"/PicTaker/Images")
        if (!sdCard.exists())
            sdCard.mkdirs()
        var image: File? = null
        try {
            image = File.createTempFile(
                    imageFileName, /* prefix */
                    ".jpg", /* suffix */
                    sdCard      /* directory */
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        mSelectedImagePath = image!!.absolutePath
        return image!!
    }
}