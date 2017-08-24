package com.mht.pic.listner

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo

/**
 * Created by mht on 22-Aug-17.
 */
interface PicTakeIntraction {

    fun openDialog(intents: List<Intent>,
                   activitiesInfo: List<ResolveInfo>)
}