package com.mht.pic.presenter

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.mht.pic.listner.PicTakeIntraction
import com.mht.pic.R
import android.app.Activity
import android.net.Uri
import android.util.Log
import com.mht.pic.utilz.FileUtils


/**
 * Created by mht on 22-Aug-17.
 */
class PicTakePresenter constructor(mContext : Context, mListener: PicTakeIntraction){

    val mListener: PicTakeIntraction = mListener
    val mContext: Context = mContext
    var TAG: String = "PicTakePresenter"

   /* companion object {

    }*/

    fun getListOfIntents(){
        val camIntent = Intent("android.media.action.IMAGE_CAPTURE")
        //val gallIntent = Intent(Intent.ACTION_GET_CONTENT)
        val gallIntent = Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        gallIntent.type = "image/*"

        // look for available intents
        val info = ArrayList<ResolveInfo>()
        val yourIntentsList = ArrayList<Intent>()
        val packageManager = mContext.packageManager
        val listCam = packageManager.queryIntentActivities(camIntent, 0)
        for (res in listCam) {
            val finalIntent = Intent(camIntent)
            finalIntent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            yourIntentsList.add(finalIntent)
            info.add(res)
        }
        val listGall = packageManager.queryIntentActivities(gallIntent, 0)
        for (res in listGall) {
            val finalIntent = Intent(gallIntent)
            finalIntent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            yourIntentsList.add(finalIntent)
            info.add(res)
        }

        mListener?.openDialog(yourIntentsList, info)
    }

    /**
     * Build the list of items to show using the intent_listview_row layout.
     *
     */
    fun buildAdapter(activitiesInfo: List<ResolveInfo>): ArrayAdapter<ResolveInfo> {
        return object : ArrayAdapter<ResolveInfo>(mContext, R.layout.intent_listview_row, R.id.txt_app_name, activitiesInfo) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
                val view = super.getView(position, convertView, parent)
                val res = activitiesInfo[position]
                val image = view!!.findViewById<ImageView>(R.id.img_icon)
                image.setImageDrawable(res.loadIcon(context.packageManager))
                val textview = view!!.findViewById<TextView>(R.id.txt_app_name)
                textview.text = res.loadLabel(context.packageManager).toString()
                return view
            }
        }
    }

    fun getImagePathFromResult(context: Context, requestCode: Int, resultCode: Int,
                               imageReturnedIntent: Intent?, mPickImageRequestCode : Int, mCamera : Boolean): String? {
        var selectedImage: Uri? = null
        if (resultCode == Activity.RESULT_OK && requestCode == mPickImageRequestCode) {
            /*val imageFile = FileUtils.getTemporalFile(context, ""+mPickImageRequestCode)
            val isCamera = imageReturnedIntent == null
                    || imageReturnedIntent.data == null
                    || imageReturnedIntent.data.toString().contains(imageFile.toString())*/
            if (mCamera) {
                return FileUtils.mSelectedImagePath
            } else {
                selectedImage = imageReturnedIntent!!.data
            }
            Log.e(TAG, "selectedImage: " + selectedImage!!)
        }
        if (selectedImage == null) {
            return null
        }
        return FileUtils.getFilePathFromUri(context, selectedImage)
    }
}