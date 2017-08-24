package com.mht.pic

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.provider.MediaStore
import android.support.design.widget.BottomSheetDialog
import android.widget.ListView
import com.mht.pic.listner.PicTakeIntraction
import com.mht.pic.presenter.PicTakePresenter
import com.mht.pic.utilz.FileUtils


/**
 * Created by mht on 22-Aug-17.
 */
class PicTake constructor(mContext: Activity) : PicTakeIntraction {

    val mContext: Activity = mContext
    val TAG: String = "PicTake"
    lateinit var  mPocTakePresenter : PicTakePresenter
    val mPickImageRequestCode : Int = 1001
    var mCamera : Boolean = false

    /**
     * Make Sure permission need to be allow
     */
    fun build(){
        mPocTakePresenter = PicTakePresenter(mContext, this)
        mPocTakePresenter.getListOfIntents()
    }

    /**
     * Open a new dialog with the detected items.
     */
     override fun openDialog(intents: List<Intent>,
                                    activitiesInfo: List<ResolveInfo>) {
        val mBottomSheetDialog = BottomSheetDialog(mContext)
        val sheetView = mContext.layoutInflater.inflate(R.layout.dialog_intent, null)
        var mListView = sheetView.findViewById<ListView>(R.id.list_view)
        mListView.adapter = mPocTakePresenter.buildAdapter(activitiesInfo)
        mListView.setOnItemClickListener { adapterView, view, position, l ->
            /**
             * @param position == 0  means Camera
             */
            when (position) {
                0 -> {
                    mCamera = true
                    val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    takePhotoIntent.putExtra("return-data", true)
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(FileUtils.createImageFile()))
                    mContext.startActivityForResult(takePhotoIntent, mPickImageRequestCode)
                    mBottomSheetDialog.dismiss()
                }
                else -> {
                    mCamera = false
                    val pickIntent = intents[position]
                    mContext.startActivityForResult(pickIntent, mPickImageRequestCode)
                    mBottomSheetDialog.dismiss()
                }

            }
        }
        mBottomSheetDialog.setContentView(sheetView)
        mBottomSheetDialog.show()
    }

    fun getImagePathFromResult(context: Context, requestCode: Int, resultCode: Int,
                               imageReturnedIntent: Intent?): String? {
        return mPocTakePresenter.getImagePathFromResult(context, requestCode, resultCode, imageReturnedIntent, mPickImageRequestCode, mCamera)
    }

}