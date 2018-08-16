package com.indieteam.qrcode.device

import android.content.Context
import android.content.pm.PackageManager

class DetectedCamera(val context: Context) {

    fun checkCamereHardware(): Boolean{
        return if(context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            true
        }
        else {
            false
        }
    }
}