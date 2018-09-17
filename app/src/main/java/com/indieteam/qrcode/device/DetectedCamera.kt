package com.indieteam.qrcode.device

import android.content.Context
import android.content.pm.PackageManager

class DetectedCamera(private val context: Context) {

    fun checkCameraHardware(): Boolean = context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)

}